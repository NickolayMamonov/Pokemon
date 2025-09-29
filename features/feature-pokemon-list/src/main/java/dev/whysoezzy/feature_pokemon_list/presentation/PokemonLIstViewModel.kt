package dev.whysoezzy.feature_pokemon_list.presentation

import androidx.lifecycle.viewModelScope
import dev.whysoezzy.core_data.base.ViewModelLoader
import dev.whysoezzy.core_data.state.LoadingState
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.PokemonListItem
import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import timber.log.Timber

class PokemonListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
    private val filterPokemonUseCase: FilterPokemonUseCase,
) : ViewModelLoader<
        PokemonListUiState,
        PokemonListViewModel.Intent,
        PokemonListViewModel.Trigger,
        >() {
    sealed class Intent {
        data object LoadInitialData : Intent()

        data object LoadMore : Intent()

        data object Refresh : Intent()

        data object Retry : Intent()

        data class UpdateSearchQuery(val query: String) : Intent()

        data class UpdateFilter(val filter: PokemonFilter) : Intent()

        data object ClearFilters : Intent()
    }

    sealed class Trigger {
        data object LoadInitialData : Trigger()

        data object LoadMore : Trigger()

        data object Refresh : Trigger()

        data class SearchQueryChanged(val query: String) : Trigger()

        data class FilterChanged(val filter: PokemonFilter) : Trigger()

        data class PokemonDetailsLoaded(
            val pokemonId: String,
            val pokemon: Pokemon,
            val allTypes: Set<String>,
        ) : Trigger()
    }

    private val _filter = MutableStateFlow(PokemonFilter())
    val filter: StateFlow<PokemonFilter> = _filter.asStateFlow()

    companion object {
        private const val PAGE_SIZE = 20
        private const val CACHE_TIMEOUT = 5000L // 5 секунд кэш для быстрой навигации
        private const val SEARCH_DEBOUNCE = 300L
    }

    override val state =
        loadData(
            initialState = PokemonListUiState(loadingState = LoadingState.Idle),
            loadData = { currentState ->
                // Загружаем только если нет данных или была ошибка
                if (shouldLoadInitialData(currentState)) {
                    Timber.d("Loading initial data - no existing data or error state")
                    emit(currentState.copy(loadingState = LoadingState.Loading))
                    loadPokemonPage(offset = 0, currentState = currentState)
                } else {
                    Timber.d("Using cached data - ${currentState.pokemonList.size} items")
                    // Применяем фильтры к существующим данным
                    applyFilters(currentState)
                }
            },
            triggerData = { currentState, trigger ->
                when (trigger) {
                    Trigger.LoadInitialData -> {
                        Timber.i("User initiated: Load initial data")
                        emit(currentState.copy(loadingState = LoadingState.Loading))
                        loadPokemonPage(offset = 0, currentState = currentState)
                    }

                    Trigger.LoadMore -> {
                        if (canLoadMore(currentState)) {
                            Timber.i("User initiated: Load more (page ${currentState.currentPage + 1})")
                            emit(
                                currentState.copy(
                                    loadingState = LoadingState.LoadingMore(currentState.filteredPokemon.size),
                                ),
                            )
                            loadPokemonPage(
                                offset = currentState.pokemonList.size,
                                currentState = currentState,
                            )
                        }
                    }

                    Trigger.Refresh -> {
                        Timber.i("User initiated: Refresh")
                        emit(currentState.copy(loadingState = LoadingState.Refreshing))
                        loadPokemonPage(
                            offset = 0,
                            currentState =
                                currentState.copy(
                                    pokemonList = emptyList(),
                                    detailedPokemon = emptyMap(),
                                    availableTypes = emptySet(),
                                ),
                        )
                    }

                    is Trigger.SearchQueryChanged -> {
                        _filter.value = _filter.value.copy(searchQuery = trigger.query)
                        applyFilters(currentState)
                    }

                    is Trigger.FilterChanged -> {
                        _filter.value = trigger.filter
                        applyFilters(currentState)
                    }

                    is Trigger.PokemonDetailsLoaded -> {
                        val newDetailedPokemon = currentState.detailedPokemon + (trigger.pokemonId to trigger.pokemon)
                        val newState =
                            currentState.copy(
                                detailedPokemon = newDetailedPokemon,
                                availableTypes = currentState.availableTypes + trigger.allTypes,
                            )
                        emit(newState)
                        applyFilters(newState)
                    }
                }
            },
            timeout = CACHE_TIMEOUT,
        )

    init {
        setupSearchDebouncing()
    }

    private fun setupSearchDebouncing() {
        viewModelScope.launch {
            _filter
                .debounce(SEARCH_DEBOUNCE)
                .distinctUntilChanged { old, new -> old.searchQuery == new.searchQuery }
                .flowOn(Dispatchers.Default)
                .collect { newFilter ->
                    if (newFilter != _filter.value) {
                        sendTrigger(Trigger.FilterChanged(newFilter))
                    }
                }
        }
    }

    override fun onIntent(intent: Intent) {
        when (intent) {
            Intent.LoadInitialData -> sendTrigger(Trigger.LoadInitialData)
            Intent.LoadMore -> sendTrigger(Trigger.LoadMore)
            Intent.Refresh -> sendTrigger(Trigger.Refresh)
            Intent.Retry -> {
                when (currentState.loadingState) {
                    is LoadingState.Error -> {
                        if (currentState.pokemonList.isEmpty()) {
                            sendTrigger(Trigger.LoadInitialData)
                        } else {
                            sendTrigger(Trigger.LoadMore)
                        }
                    }
                    else -> sendTrigger(Trigger.LoadInitialData)
                }
            }
            is Intent.UpdateSearchQuery -> {
                // Немедленно обновляем UI, debounce обработает позже
                _filter.value = _filter.value.copy(searchQuery = intent.query)
            }
            is Intent.UpdateFilter -> sendTrigger(Trigger.FilterChanged(intent.filter))
            Intent.ClearFilters -> sendTrigger(Trigger.FilterChanged(PokemonFilter()))
        }
    }

    /**
     * Проверяет, нужно ли загружать начальные данные
     */
    private fun shouldLoadInitialData(currentState: PokemonListUiState): Boolean {
        return currentState.pokemonList.isEmpty() || currentState.loadingState is LoadingState.Error
    }

    /**
     * Проверяет, можно ли загрузить больше данных
     */
    private fun canLoadMore(currentState: PokemonListUiState): Boolean {
        return currentState.hasNextPage &&
            currentState.loadingState !is LoadingState.LoadingMore &&
            currentState.loadingState !is LoadingState.Loading
    }

    /**
     * Применяет текущие фильтры к данным
     */
    private suspend fun FlowCollector<PokemonListUiState>.applyFilters(currentState: PokemonListUiState) {
        val currentFilter = _filter.value
        val allPokemon = currentState.detailedPokemon.values.toList()
        val filteredPokemon = filterPokemonUseCase.invoke(allPokemon, currentFilter)

        Timber.v(
            "Applied filters: search='${currentFilter.searchQuery}', types=${currentFilter.selectedTypes.size}, result=${filteredPokemon.size}",
        )

        emit(
            currentState.copy(
                filteredPokemon = filteredPokemon,
                loadingState = if (currentState.loadingState is LoadingState.Refreshing) LoadingState.Success else currentState.loadingState,
            ),
        )
    }

    /**
     * Загружает страницу покемонов
     */
    private suspend fun FlowCollector<PokemonListUiState>.loadPokemonPage(
        offset: Int,
        currentState: PokemonListUiState,
    ) {
        try {
            val result = getPokemonListUseCase(limit = PAGE_SIZE, offset = offset)

            result.fold(
                onSuccess = { paginatedData ->
                    Timber.i("Successfully loaded page: ${paginatedData.items.size} Pokemon from ${paginatedData.totalCount}")

                    val newPokemonList =
                        if (offset == 0) {
                            paginatedData.items
                        } else {
                            currentState.pokemonList + paginatedData.items
                        }

                    val newState =
                        currentState.copy(
                            pokemonList = newPokemonList,
                            loadingState = LoadingState.Success,
                            hasNextPage = paginatedData.hasNextPage,
                            currentPage = paginatedData.currentPage,
                            totalCount = paginatedData.totalCount,
                        )

                    emit(newState)

                    // Запускаем загрузку деталей асинхронно
                    loadPokemonDetailsAsync(paginatedData.items)
                },
                onFailure = { error ->
                    Timber.e(error, "Error loading Pokemon list")
                    emit(
                        currentState.copy(
                            loadingState =
                                LoadingState.Error(
                                    message = error.message ?: "Failed to load Pokemon",
                                    isRetry = true,
                                    hasExistingData = currentState.hasData,
                                ),
                        ),
                    )
                },
            )
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error loading Pokemon")
            emit(
                currentState.copy(
                    loadingState =
                        LoadingState.Error(
                            message = "Unexpected error occurred",
                            isRetry = true,
                            hasExistingData = currentState.hasData,
                        ),
                ),
            )
        }
    }

    /**
     * Асинхронно загружает детали покемонов
     */
    private fun loadPokemonDetailsAsync(pokemonList: List<PokemonListItem>) {
        viewModelScope.launch {
            Timber.d("Loading details for ${pokemonList.size} Pokemon")

            val semaphore = Semaphore(5) // Ограничиваем количество одновременных запросов

            val results =
                pokemonList.map { pokemonItem ->
                    async(Dispatchers.IO) {
                        semaphore.withPermit {
                            getPokemonDetailsUseCase(pokemonItem.id)
                        }
                    }
                }

            var successCount = 0
            var errorCount = 0

            results.forEachIndexed { index, deferred ->
                val pokemonItem = pokemonList[index]
                try {
                    val result = deferred.await()
                    result.fold(
                        onSuccess = { pokemon ->
                            successCount++
                            Timber.v("Loaded details for ${pokemon.name} ($successCount/${pokemonList.size})")

                            val types = pokemon.types.map { it.name }.toSet()
                            sendTrigger(
                                Trigger.PokemonDetailsLoaded(
                                    pokemonId = pokemonItem.id,
                                    pokemon = pokemon,
                                    allTypes = types,
                                ),
                            )
                        },
                        onFailure = { error ->
                            errorCount++
                            Timber.w(error, "Error loading details for Pokemon ${pokemonItem.id}")
                        },
                    )
                } catch (e: Exception) {
                    errorCount++
                    Timber.e(e, "Unexpected error loading Pokemon ${pokemonItem.id}")
                }
            }

            Timber.i("Completed loading details: success=$successCount, errors=$errorCount")
        }
    }

    /**
     * Предзагрузка следующей страницы для улучшения UX
     */
    fun preloadNextPage() {
        val currentState = state.value
        if (!currentState.hasNextPage) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nextOffset = currentState.pokemonList.size
                getPokemonListUseCase(limit = PAGE_SIZE, offset = nextOffset).fold(
                    onSuccess = { paginatedData ->
                        val preloadItems = paginatedData.items.take(6)
                        loadPokemonDetailsAsync(preloadItems)
                        Timber.d("Preloaded ${preloadItems.size} Pokemon details")
                    },
                    onFailure = { error ->
                        Timber.w(error, "Error preloading next page")
                    },
                )
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error during preloading")
            }
        }
    }
}
