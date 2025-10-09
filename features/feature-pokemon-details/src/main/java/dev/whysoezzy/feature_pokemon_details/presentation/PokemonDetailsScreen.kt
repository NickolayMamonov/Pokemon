package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.core_uikit.components.errors.ErrorMessageDetail
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        Timber.d("Загрузка покемона с ID: $pokemonId")
        viewModel.loadPokemon(pokemonId)
    }

    when (val state = uiState) {
        is PokemonDetailsUiState.Loading -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Загрузка...") },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
        }

        is PokemonDetailsUiState.Error -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Ошибка") },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                ErrorMessageDetail(
                    error = state.message,
                    onRetry = { viewModel.loadPokemon(pokemonId) },
                    modifier = Modifier.padding(paddingValues)
                )

            }
        }

        is PokemonDetailsUiState.Success -> {
            PokemonDetailsContent(
                pokemon = state.pokemon,
                onBackClick = onBackClick
            )
        }
    }
}