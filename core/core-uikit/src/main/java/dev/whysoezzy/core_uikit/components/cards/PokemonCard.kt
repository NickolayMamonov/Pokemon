package dev.whysoezzy.core_uikit.components.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.whysoezzy.core_uikit.components.buttons.PokemonIconButton
import dev.whysoezzy.core_uikit.components.buttons.PokemonIconButtonStyle
import dev.whysoezzy.core_uikit.components.chips.PokemonTypeChip
import dev.whysoezzy.core_uikit.components.chips.TypeChipSize
import dev.whysoezzy.core_uikit.extensions.toDisplayName
import dev.whysoezzy.core_uikit.extensions.toTypeColor
import dev.whysoezzy.core_uikit.theme.AnimationSpecs
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonBrandColors
import dev.whysoezzy.core_uikit.theme.PokemonElevation
import dev.whysoezzy.core_uikit.theme.PokemonTextStyles
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

enum class PokemonCardVariant {
    STANDARD,
    COMPACT,
    LARGE
}

@Composable
fun PokemonCard(
    id: Int,
    name: String,
    imageUrl: String,
    types: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: PokemonCardVariant = PokemonCardVariant.STANDARD,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
    showFavouriteButton: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = AnimationSpecs.Bouncy,
        label = "card_scale"
    )

    val elevation by animateFloatAsState(
        targetValue = if (isPressed)
            PokemonElevation.PokemonCard.value
        else
            PokemonElevation.PokemonCardHover.value,
        label = "card_elevation"
    )

    val primaryType = types.firstOrNull() ?: "normal"
    val primaryColor = primaryType.toTypeColor()
    val secondaryColor = types.getOrNull(1)?.toTypeColor() ?: primaryColor

    // Проверить палитру дизайн системы
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            primaryColor.copy(alpha = 0.2f),
            secondaryColor.copy(alpha = 0.1f)
        )
    )

    val cardHeight = when (variant) {
        PokemonCardVariant.COMPACT -> 100.dp
        PokemonCardVariant.STANDARD -> 140.dp
        PokemonCardVariant.LARGE -> 200.dp
    }

    val imageSize = when (variant) {
        PokemonCardVariant.COMPACT -> dimensions().pokemonImageSizeTiny
        PokemonCardVariant.STANDARD -> dimensions().pokemonImageSizeSmall
        PokemonCardVariant.LARGE -> dimensions().pokemonImageSizeMedium
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight)
            .scale(scale),
        shape = CustomShapes.PokemonCard,
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing().medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(CircleShape)
                        .background(PokemonBrandColors.White.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = name,
                        modifier = Modifier
                            .size(imageSize * 0.85f)
                            .padding(spacing().extraSmall),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(spacing().medium))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing().extraSmall)
                ) {
                    Text(
                        text = name.toDisplayName(),
                        style = PokemonTextStyles.PokemonName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing().extraSmall),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        types.take(2).forEach { type ->
                            PokemonTypeChip(
                                type = type,
                                size = when (variant) {
                                    PokemonCardVariant.COMPACT -> TypeChipSize.SMALL
                                    PokemonCardVariant.STANDARD -> TypeChipSize.SMALL
                                    PokemonCardVariant.LARGE -> TypeChipSize.MEDIUM
                                }
                            )
                        }
                    }
                }
            }

            if (showFavouriteButton && onFavoriteClick != null) {
                PokemonIconButton(
                    icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing().small),
                    style = PokemonIconButtonStyle.FILLED,
                    tint = if (isFavorite) PokemonBrandColors.Red else MaterialTheme.colorScheme.onSurface,
                    contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное"
                )
            }
        }

    }
}

@Preview(name = "Pokemon Card - Light", showBackground = true)
@Composable
private fun PokemonCardPreviewLight() {
    PokemonTheme(darkTheme = false) {
        PokemonCardShowcase()
    }
}

@Preview(name = "Pokemon Card - Dark", showBackground = true)
@Composable
private fun PokemonCardPreviewDark() {
    PokemonTheme(darkTheme = true) {
        PokemonCardShowcase()
    }
}

@Composable
private fun PokemonCardShowcase() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing().medium),
            verticalArrangement = Arrangement.spacedBy(spacing().medium)
        ) {
            Text(
                text = "Pokemon Cards",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Standard variant
            Text("Standard", style = MaterialTheme.typography.titleMedium)
            PokemonCard(
                id = 25,
                name = "pikachu",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                types = listOf("electric"),
                onClick = { },
                variant = PokemonCardVariant.STANDARD
            )

            // With favorite button
            Text("With Favorite", style = MaterialTheme.typography.titleMedium)
            PokemonCard(
                id = 6,
                name = "charizard",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png",
                types = listOf("fire", "flying"),
                onClick = { },
                variant = PokemonCardVariant.STANDARD,
                showFavouriteButton = true,
                isFavorite = true,
                onFavoriteClick = { }
            )

            // Compact variant
            Text("Compact", style = MaterialTheme.typography.titleMedium)
            PokemonCard(
                id = 1,
                name = "bulbasaur",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                types = listOf("grass", "poison"),
                onClick = { },
                variant = PokemonCardVariant.COMPACT
            )

            // Large variant
            Text("Large", style = MaterialTheme.typography.titleMedium)
            PokemonCard(
                id = 9,
                name = "blastoise",
                imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/9.png",
                types = listOf("water"),
                onClick = { },
                variant = PokemonCardVariant.LARGE,
                showFavouriteButton = true,
                isFavorite = false,
                onFavoriteClick = { }
            )
        }
    }
}