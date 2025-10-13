package dev.whysoezzy.core_uikit.components.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.whysoezzy.core_uikit.components.buttons.PokemonButton
import dev.whysoezzy.core_uikit.components.cards.PokemonCard
import dev.whysoezzy.core_uikit.components.cards.PokemonCardVariant
import dev.whysoezzy.core_uikit.components.loadings.LoadingOverlay
import dev.whysoezzy.core_uikit.components.loadings.PokemonCardSkeleton
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.spacing

@Composable
fun TestCardsScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(spacing().medium),
            verticalArrangement = Arrangement.spacedBy(spacing().medium)
        ) {
            // Loading skeletons
            if (isLoading) {
                items(5) {
                    PokemonCardSkeleton()
                }
            } else {
                // Real cards
                item {
                    PokemonCard(
                        id = 25,
                        name = "pikachu",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                        types = listOf("electric"),
                        onClick = { },
                        showFavouriteButton = true,
                        isFavorite = isFavorite,
                        onFavoriteClick = { isFavorite = !isFavorite }
                    )
                }

                item {
                    PokemonCard(
                        id = 6,
                        name = "charizard",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png",
                        types = listOf("fire", "flying"),
                        onClick = { },
                        variant = PokemonCardVariant.LARGE,
                        showFavouriteButton = true,
                        isFavorite = false,
                        onFavoriteClick = { }
                    )
                }

                item {
                    PokemonCard(
                        id = 1,
                        name = "bulbasaur",
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                        types = listOf("grass", "poison"),
                        onClick = { },
                        variant = PokemonCardVariant.COMPACT
                    )
                }
            }

            // Toggle button
            item {
                PokemonButton(
                    text = if (isLoading) "Скрыть Skeleton" else "Показать Skeleton",
                    onClick = { isLoading = !isLoading },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Loading overlay example
        LoadingOverlay(
            isLoading = false, // поменяйте на true для теста
            message = "Загрузка покемонов..."
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TestCardsScreenPreview() {
    PokemonTheme(darkTheme = false) {
        TestCardsScreen()
    }
}