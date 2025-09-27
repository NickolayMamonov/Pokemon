package dev.whysoezzy.feature_pokemon_details.presentation

import dev.whysoezzy.domain.model.Pokemon

data class PokemonDetailsUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isImageLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val showExtendedStats: Boolean = false
)
