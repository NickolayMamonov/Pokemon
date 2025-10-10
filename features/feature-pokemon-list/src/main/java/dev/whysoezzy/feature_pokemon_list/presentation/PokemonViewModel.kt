package dev.whysoezzy.feature_pokemon_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.repository.PokemonRepository
import dev.whysoezzy.feature_pokemon_list.data.PokemonPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {
    private val _filter = MutableStateFlow(PokemonFilter())
    val filter: StateFlow<PokemonFilter> = _filter.asStateFlow()

    private val _availableTypes = MutableStateFlow<Set<String>>(emptySet())
    val availableTypes: StateFlow<Set<String>> = _availableTypes.asStateFlow()

    val pokemonPagingFlow: Flow<PagingData<Pokemon>> = _filter
        .flatMapLatest { currentFilter ->
            Timber.d("Пересоздаем пагинацию с новым фильтром: $currentFilter")
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    prefetchDistance = 5,
                    enablePlaceholders = false,
                    initialLoadSize = 20
                ),
                pagingSourceFactory = {
                    PokemonPagingSource(repository, currentFilter)
                }
            ).flow
        }
        .cachedIn(viewModelScope)

    init {
        Timber.d("Инициализация PokemonViewModel")
    }

    fun updateSearchQuery(query: String) {
        Timber.d("Обновление поискового запроса: $query")
        _filter.value = _filter.value.copy(searchQuery = query)
    }

    fun updateFilter(newFilter: PokemonFilter) {
        Timber.d("Обновлены фильтры: типы=${newFilter.selectedTypes.size}, сортировка=${newFilter.sortBy}")
        _filter.value = newFilter
    }

    fun clearFilters() {
        Timber.d("Очистка фильтров")
        _filter.value = PokemonFilter()
    }

    fun updateAvailableTypes(types: Set<String>) {
        _availableTypes.value = types
    }
}
