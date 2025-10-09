package dev.whysoezzy.pokemon.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dev.whysoezzy.pokemon.presentation.navigation.PokemonNavHost

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    PokemonNavHost(
        navController = navController,
        modifier = modifier
    )
}