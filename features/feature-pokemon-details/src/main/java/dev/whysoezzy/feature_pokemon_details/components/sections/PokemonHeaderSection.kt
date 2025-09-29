package dev.whysoezzy.feature_pokemon_details.components.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.whysoezzy.domain.model.Pokemon

@Composable
fun PokemonHeaderSection(
    pokemon: Pokemon,
    backgroundColor: Color,
    isImageLoading: Boolean,
    onImageLoadingChanged: (Boolean) -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    backgroundColor.copy(alpha = 0.4f),
                                    backgroundColor.copy(alpha = 0.1f),
                                    Color.Transparent,
                                ),
                        ),
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Изображение покемона
            Card(
                modifier = Modifier.size(180.dp),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = "Изображение ${pokemon.name}",
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        contentScale = ContentScale.Fit,
                        onLoading = { onImageLoadingChanged(true) },
                        onSuccess = { onImageLoadingChanged(false) },
                        onError = { onImageLoadingChanged(false) },
                    )

                    if (isImageLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Общая сила
            Card {
                Text(
                    text = "Общая сила: ${pokemon.totalStats}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
