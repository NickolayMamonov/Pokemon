package dev.whysoezzy.pokemon.presentation.main

import dev.whysoezzy.domain.model.Pokemon


data class MainUiState(
    val currentScreen: Screen = Screen.PokemonList,
    val selectedPokemon: Pokemon? = null,
    val isNavigating: Boolean = false,
    val canNavigateBack: Boolean = false
)