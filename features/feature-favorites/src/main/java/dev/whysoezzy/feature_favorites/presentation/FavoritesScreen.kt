package dev.whysoezzy.feature_favorites.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.core_uikit.components.cards.PokemonGridCard
import dev.whysoezzy.core_uikit.components.empty.EmptyStates
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import dev.whysoezzy.domain.model.Pokemon
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreen(
    onPokemonSelected: (Pokemon) -> Unit,
    onNavigateToPokedex: () -> Unit,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (val state = uiState) {
            is FavoritesUIState.Loading -> {
                LoadingIndicator()
            }

            is FavoritesUIState.Error -> {
                ErrorMessage(
                    error = state.message,
                    onRetry = viewModel::retry
                )

            }

            is FavoritesUIState.Empty -> {
                EmptyStates.NoFavourites(
                    onOpenPokedex = onNavigateToPokedex
                )
            }

            is FavoritesUIState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.favorites,
                        key = { it.id }
                    ) { pokemon ->
                        PokemonGridCard(
                            id = pokemon.id,
                            name = pokemon.name,
                            imageUrl = pokemon.imageUrl,
                            types = pokemon.types.map { it.name },
                            onClick = { onPokemonSelected(pokemon) },
                            isFavorite = true,
                            showFavoriteButton = true,
                            onFavoriteClick = {
                                viewModel.toggleFavorite(pokemon.id)
                            }
                        )
                    }
                }
            }
        }
    }
}