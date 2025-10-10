package dev.whysoezzy.feature_pokemon_list.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.SortBy
import dev.whysoezzy.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

class PokemonPagingSource(
    private val repository: PokemonRepository,
    private val filter: PokemonFilter
) : PagingSource<Int, Pokemon>() {

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val page = params.key ?: STARTING_PAGE
            val offset = page * PAGE_SIZE

            Timber.d("Загружаем детальную страницу $page (offset: $offset) с фильтром: ${filter.searchQuery}, типы: ${filter.selectedTypes}")

            val listResult = repository.getPokemonList(
                limit = params.loadSize,
                offset = offset
            )

            listResult.fold(
                onSuccess = { paginatedData ->
                    Timber.d("Загружено ${paginatedData.items.size} элементов списка, загружаем детали...")

                    // Загружаем детали для каждого покемона параллельно
                    val detailedPokemon = coroutineScope {
                        val jobs = paginatedData.items.map { item ->
                            async {
                                repository.getPokemonDetails(item.id).getOrNull()
                            }
                        }
                        // Явно ждем все задачи
                        jobs.awaitAll().filterNotNull()
                    }

                    // Применяем фильтрацию
                    val filteredPokemon = applyFilters(detailedPokemon)

                    Timber.i("Загружено ${detailedPokemon.size} детальных покемонов, после фильтрации: ${filteredPokemon.size}")

                    LoadResult.Page(
                        data = filteredPokemon,
                        prevKey = if (page == STARTING_PAGE) null else page - 1,
                        nextKey = if (paginatedData.hasNextPage) page + 1 else null
                    )
                },
                onFailure = { error ->
                    Timber.e(error, "Ошибка загрузки страницы покемонов")
                    LoadResult.Error(error)
                }
            )
        } catch (e: Exception) {
            Timber.e(e, "Неожиданная ошибка при загрузке страницы")
            LoadResult.Error(e)
        }
    }

    private fun applyFilters(pokemonList: List<Pokemon>): List<Pokemon> {
        var filtered = pokemonList

        // Фильтр по поисковому запросу
        if (filter.searchQuery.isNotBlank()) {
            filtered = filtered.filter { pokemon ->
                pokemon.name.contains(filter.searchQuery, ignoreCase = true)
            }
        }

        // Фильтр по типам
        if (filter.selectedTypes.isNotEmpty()) {
            filtered = filtered.filter { pokemon ->
                pokemon.types.any { type ->
                    filter.selectedTypes.contains(type.name)
                }
            }
        }

        // Сортировка
        filtered = when (filter.sortBy) {
            SortBy.ID -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.id }
                } else {
                    filtered.sortedByDescending { it.id }
                }
            }

            SortBy.NAME -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.name }
                } else {
                    filtered.sortedByDescending { it.name }
                }
            }

            SortBy.HEIGHT -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.height }
                } else {
                    filtered.sortedByDescending { it.height }
                }
            }

            SortBy.WEIGHT -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.weight }
                } else {
                    filtered.sortedByDescending { it.weight }
                }
            }

            SortBy.HP -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.hp }
                } else {
                    filtered.sortedByDescending { it.hp }
                }
            }

            SortBy.ATTACK -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.attack }
                } else {
                    filtered.sortedByDescending { it.attack }
                }
            }

            SortBy.DEFENSE -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.defense }
                } else {
                    filtered.sortedByDescending { it.defense }
                }

            }

            SortBy.SPEED -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.speed }
                } else {
                    filtered.sortedByDescending { it.speed }
                }
            }

            SortBy.TOTAL_STATS -> {
                if (filter.isAscending) {
                    filtered.sortedBy { it.totalStats }
                } else {
                    filtered.sortedByDescending { it.totalStats }
                }
            }
        }

        return filtered
    }

    companion object {
        private const val STARTING_PAGE = 0
        private const val PAGE_SIZE = 20
    }
}