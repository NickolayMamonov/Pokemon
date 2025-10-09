package dev.whysoezzy.feature_pokemon_details.presentation

import dev.whysoezzy.domain.model.Pokemon

sealed interface PokemonDetailsUiState {
    object Loading : PokemonDetailsUiState
    data class Success(val pokemon: Pokemon) : PokemonDetailsUiState
    data class Error(val message: String) : PokemonDetailsUiState
}