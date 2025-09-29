package dev.whysoezzy.pokemon.presentation.main

import dev.whysoezzy.domain.model.Pokemon

sealed class Screen {
    object PokemonList : Screen()

    data class PokemonDetails(val pokemon: Pokemon) : Screen()
}
