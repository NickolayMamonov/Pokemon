package dev.whysoezzy.pokemon.presentation.detail

import dev.whysoezzy.domain.model.Pokemon

//import dev.whysoezzy.pokemon.domain.model.Pokemon

data class PokemonDetailsUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isImageLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val showExtendedStats: Boolean = false
)
