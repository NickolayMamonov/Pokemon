package dev.whysoezzy.pokemon.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.feature_pokemon_details.presentation.PokemonDetailsScreen
import dev.whysoezzy.feature_pokemon_list.presentation.PokemonListScreen
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MainScreen(mainViewModel: MainViewModel = koinViewModel()) {
    val mainUiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(enabled = mainUiState.canNavigateBack) {
        Timber.d("System back button pressed")
        val handled = mainViewModel.navigateBack()
        if (!handled) {
            Timber.d("Navigation not handled, system will process back button")
        }
    }

    when (val currentScreen = mainUiState.currentScreen) {
        is Screen.PokemonList -> {
            PokemonListScreen(
                onPokemonSelected = { pokemon ->
                    Timber.d("Pokemon selected: ${pokemon.name}, stats: ${pokemon.stats.map { "${it.name}: ${it.baseStat}" }}")
                    mainViewModel.navigateToPokemonDetails(pokemon)
                },
            )
        }

        is Screen.PokemonDetails -> {
            PokemonDetailsScreen(
                pokemon = currentScreen.pokemon,
                onBackClick = {
                    mainViewModel.navigateBack()
                },
            )
        }
    }

    LaunchedEffect(mainUiState.isNavigating) {
        if (mainUiState.isNavigating) {
            mainViewModel.onNavigationComplete()
        }
    }
}
