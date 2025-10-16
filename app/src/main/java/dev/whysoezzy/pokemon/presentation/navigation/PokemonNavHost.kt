package dev.whysoezzy.pokemon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.whysoezzy.feature_favorites.presentation.FavoritesScreen
import dev.whysoezzy.feature_pokemon_details.presentation.PokemonDetailsScreen
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonListScreen

@Composable
fun PokemonNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonSelected = { pokemon ->
                    navController.navigate(Screen.PokemonDetails.createRoute(pokemon.id))
                }
            )
        }
        composable(
            route = Screen.PokemonDetails.route,
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: return@composable
            PokemonDetailsScreen(
                pokemonId = pokemonId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onPokemonSelected = { pokemon ->
                    navController.navigate(Screen.PokemonDetails.createRoute(pokemon.id))
                },
                onNavigateToPokedex = {
                    navController.navigate(Screen.PokemonList.route) {
                        popUpTo(Screen.PokemonList.route) { inclusive = false }
                    }
                }
            )
        }

    }

}