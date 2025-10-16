package dev.whysoezzy.pokemon.presentation.navigation

import dev.whysoezzy.core_uikit.R

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetails : Screen("pokemon_details/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_details/$pokemonId"
    }
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")

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
    ),
    SETTINGS(
        screen = Screen.Settings,
        title = "Settings",
        icon = R.drawable.outline_settings,
        selectedIcon = R.drawable.filled_settings
    )
}