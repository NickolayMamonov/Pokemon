package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonFilter
import dev.whysoezzy.domain.model.SortBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterPokemonUseCase {
    suspend operator fun invoke(
        pokemonList: List<Pokemon>,
        filter: PokemonFilter,
    ): List<Pokemon> =
        withContext(Dispatchers.Default) {
            var filteredList = pokemonList

            // Фильтрация по поисковому запросу
            if (filter.searchQuery.isNotBlank()) {
                val query = filter.searchQuery.trim().lowercase()
                filteredList =
                    filteredList.filter { pokemon ->
                        pokemon.name.lowercase().contains(query) ||
                            pokemon.id.toString().contains(query) ||
                            pokemon.types.any { type -> type.name.lowercase().contains(query) }
                    }
            }

            // Фильтрация по типам
            if (filter.selectedTypes.isNotEmpty()) {
                filteredList =
                    filteredList.filter { pokemon ->
                        val pokemonTypes = pokemon.types.map { it.name.lowercase() }.toSet()
                        filter.selectedTypes.all { selectedType ->
                            pokemonTypes.contains(selectedType.lowercase())
                        }
                    }
            }

            // Дополнительные фильтры
            filter.minHp?.let { minHp ->
                filteredList = filteredList.filter { it.hp >= minHp }
            }

            filter.maxHp?.let { maxHp ->
                filteredList = filteredList.filter { it.hp <= maxHp }
            }

            filter.minAttack?.let { minAttack ->
                filteredList = filteredList.filter { it.attack >= minAttack }
            }

            filter.maxAttack?.let { maxAttack ->
                filteredList = filteredList.filter { it.attack <= maxAttack }
            }

            // Сортировка
            val sortedList =
                when (filter.sortBy) {
                    SortBy.ID -> filteredList.sortedBy { it.id }
                    SortBy.NAME -> filteredList.sortedBy { it.name.lowercase() }
                    SortBy.HP -> filteredList.sortedBy { it.hp }
                    SortBy.ATTACK -> filteredList.sortedBy { it.attack }
                    SortBy.DEFENSE -> filteredList.sortedBy { it.defense }
                    SortBy.SPEED -> filteredList.sortedBy { it.speed }
                    SortBy.TOTAL_STATS -> filteredList.sortedBy { it.totalStats }
                    SortBy.HEIGHT -> filteredList.sortedBy { it.height }
                    SortBy.WEIGHT -> filteredList.sortedBy { it.weight }
                }

            if (filter.isAscending) sortedList else sortedList.reversed()
        }
}
