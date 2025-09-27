package dev.whysoezzy.pokemon.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.PokemonListItem
//import dev.whysoezzy.pokemon.domain.model.PokemonFilter
//import dev.whysoezzy.pokemon.domain.model.PokemonListItem
//import dev.whysoezzy.pokemon.domain.usecase.FilterPokemonUseCase
//import dev.whysoezzy.pokemon.domain.usecase.GetPokemonDetailsUseCase
//import dev.whysoezzy.pokemon.domain.usecase.GetPokemonListUseCase
import dev.whysoezzy.domain.usecase.FilterPokemonUseCase
import dev.whysoezzy.domain.usecase.GetPokemonDetailsUseCase
import dev.whysoezzy.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    private val filterPokemonUseCase: FilterPokemonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private val _filter = MutableStateFlow(PokemonFilter())
    val filter: StateFlow<PokemonFilter> = _filter.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    companion object {
        private const val PAGE_SIZE = 20
    }

    init {
        Timber.d("Инициализация PokemonListViewModel")
        loadPokemonList()

        viewModelScope.launch {
            searchQueryFlow
                .debounce(300) // Задержка 300мс
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { query ->
                    _filter.value = _filter.value.copy(searchQuery = query)
                }
        }

        viewModelScope.launch {
            combine(_uiState, _filter) { state, filter ->
                Pair(state, filter)
            }
                .debounce(100)
                .distinctUntilChanged { old, new ->
                    old.second == new.second && old.first.detailedPokemon.size == new.first.detailedPokemon.size
                }
                .flowOn(Dispatchers.Default)
                .collect { (state, filter) ->
                    val allPokemon = state.detailedPokemon.values.toList()
                    val filtered = filterPokemonUseCase.invoke(allPokemon, filter)

                    Timber.v("Применены фильтры: поиск='${filter.searchQuery}', типы=${filter.selectedTypes.size}, результат=${filtered.size}")

                    _uiState.value = state.copy(filteredPokemon = filtered)
                }
        }
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            Timber.i("Начинаем загрузку первой страницы покемонов")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getPokemonListUseCase(limit = PAGE_SIZE, offset = 0).fold(
                onSuccess = { paginatedData ->
                    Timber.i("Успешно загружена первая страница: ${paginatedData.items.size} покемонов из ${paginatedData.totalCount}")
                    _uiState.value = _uiState.value.copy(
                        pokemonList = paginatedData.items,
                        isLoading = false,
                        hasNextPage = paginatedData.hasNextPage,
                        currentPage = paginatedData.currentPage,
                        totalCount = paginatedData.totalCount
                    )
                    loadPokemonDetails(paginatedData.items)
                },
                onFailure = { error ->
                    Timber.e(error, "Ошибка загрузки списка покемонов")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Неизвестная ошибка"
                    )
                }
            )
        }
    }

    fun loadMorePokemon() {
        val currentState = _uiState.value
        if (!currentState.hasNextPage || currentState.isLoadingMore) {
            Timber.d("Пропускаем загрузку: hasNextPage=${currentState.hasNextPage}, isLoadingMore=${currentState.isLoadingMore}")
            return
        }

        viewModelScope.launch {
            Timber.i("Загружаем следующую страницу покемонов (страница ${currentState.currentPage + 1})")
            _uiState.value = _uiState.value.copy(isLoadingMore = true)

            val nextOffset = currentState.pokemonList.size
            getPokemonListUseCase(limit = PAGE_SIZE, offset = nextOffset).fold(
                onSuccess = { paginatedData ->
                    Timber.i("Успешно загружена страница ${paginatedData.currentPage}: ${paginatedData.items.size} покемонов")
                    _uiState.value = _uiState.value.copy(
                        pokemonList = currentState.pokemonList + paginatedData.items,
                        isLoadingMore = false,
                        hasNextPage = paginatedData.hasNextPage,
                        currentPage = paginatedData.currentPage
                    )
                    loadPokemonDetails(paginatedData.items)
                },
                onFailure = { error ->
                    Timber.e(error, "Ошибка загрузки дополнительных покемонов")
                    _uiState.value = _uiState.value.copy(isLoadingMore = false)
                }
            )
        }
    }

    fun refreshPokemonList() {
        viewModelScope.launch {
            Timber.i("Обновляем список покемонов")
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)

            try {
                getPokemonListUseCase(limit = PAGE_SIZE, offset = 0).fold(
                    onSuccess = { paginatedData ->
                        Timber.i("Успешно обновлен список: ${paginatedData.items.size} покемонов")

                        _uiState.value = _uiState.value.copy(
                            pokemonList = paginatedData.items,
                            detailedPokemon = emptyMap(),
                            availableTypes = emptySet(),
                            hasNextPage = paginatedData.hasNextPage,
                            currentPage = paginatedData.currentPage,
                            totalCount = paginatedData.totalCount
                        )
                        loadPokemonDetails(paginatedData.items)
                        delay(500)
                        _uiState.value = _uiState.value.copy(isRefreshing = false)
                    },
                    onFailure = { error ->
                        Timber.e(error, "Ошибка обновления списка покемонов")
                        delay(500)
                        _uiState.value = _uiState.value.copy(
                            isRefreshing = false,
                            error = error.message ?: "Неизвестная ошибка"
                        )
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Неожиданная ошибка при обновлении")
                delay(500)
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    error = "Неожиданная ошибка"
                )
            }
        }
    }

    fun clearCache() {
        Timber.i("Очистка кэша недоступна (временная заглушка)")
    }

    private fun loadPokemonDetails(pokemonList: List<PokemonListItem>) {
        viewModelScope.launch {
            Timber.d("Загружаем детали для ${pokemonList.size} покемонов")

            val semaphore = Semaphore(5)
            val types = _uiState.value.availableTypes.toMutableSet()

            val results = pokemonList.map { pokemonItem ->
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
                            Timber.v("Загружены детали для ${pokemon.name} (${successCount}/${pokemonList.size})")

                            types.addAll(pokemon.types.map { it.name })

                            _uiState.value = _uiState.value.copy(
                                detailedPokemon = _uiState.value.detailedPokemon + (pokemonItem.id to pokemon),
                                availableTypes = types
                            )
                        },
                        onFailure = { error ->
                            errorCount++
                            Timber.w(
                                error,
                                "Ошибка загрузки деталей для покемона ${pokemonItem.id}"
                            )
                        }
                    )
                } catch (e: Exception) {
                    errorCount++
                    Timber.e(e, "Неожиданная ошибка при загрузке покемона ${pokemonItem.id}")
                }
            }

            Timber.i("Завершена загрузка деталей: успешно=$successCount, ошибок=$errorCount")
        }
    }

    fun updateSearchQuery(query: String) {
        Timber.d("Обновлен поисковый запрос: '$query'")
        searchQueryFlow.value = query
    }

    fun updateFilter(newFilter: PokemonFilter) {
        Timber.d("Обновлены фильтры: типы=${newFilter.selectedTypes.size}, сортировка=${newFilter.sortBy}")
        _filter.value = newFilter
    }


    fun clearFilters() {
        Timber.d("Очищены все фильтры")
        _filter.value = PokemonFilter()
    }

    fun preloadNextPage() {
        val currentState = _uiState.value
        if (!currentState.hasNextPage) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val nextOffset = currentState.pokemonList.size
                getPokemonListUseCase(limit = PAGE_SIZE, offset = nextOffset).fold(
                    onSuccess = { paginatedData ->
                        val preloadItems = paginatedData.items.take(5)
                        loadPokemonDetails(preloadItems)
                        Timber.d("Предзагрузка ${preloadItems.size} покемонов завершена")
                    },
                    onFailure = { error ->
                        Timber.w(error, "Ошибка предзагрузки следующей страницы")
                    }
                )
            } catch (e: Exception) {
                Timber.e(e, "Неожиданная ошибка предзагрузки")
            }
        }
    }
}