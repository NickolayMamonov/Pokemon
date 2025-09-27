package dev.whysoezzy.feature_pokemon_list.presentation

import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonListItem

data class PokemonListUiState(
    val pokemonList: List<PokemonListItem> = emptyList(),
    val detailedPokemon: Map<String, Pokemon> = emptyMap(),
    val filteredPokemon: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val availableTypes: Set<String> = emptySet(),
    val hasNextPage: Boolean = true,
    val currentPage: Int = 0,
    val totalCount: Int = 0,
    val cachedCount: Int = 0
)