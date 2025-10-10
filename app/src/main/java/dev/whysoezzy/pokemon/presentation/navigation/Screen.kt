package dev.whysoezzy.pokemon.presentation.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetails : Screen("pokemon_details/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_details/$pokemonId"
    }
}