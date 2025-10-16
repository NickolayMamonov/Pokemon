package dev.whysoezzy.pokemon.presentation.navigation

import dev.whysoezzy.core_uikit.R

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetails : Screen("pokemon_details/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_details/$pokemonId"
    }

    object Favorites : Screen("favorites")
}

enum class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: Int,
    val selectedIcon: Int
) {
    HOME(
        screen = Screen.PokemonList,
        title = "Home",
        icon = R.drawable.outline_home,
        selectedIcon = R.drawable.filled_home
    ),
    FAVORITES(
        screen = Screen.Favorites,
        title = "Favorites",
        icon = R.drawable.outline_favorite_border_24,
        selectedIcon = R.drawable.outline_favorite
    )
}