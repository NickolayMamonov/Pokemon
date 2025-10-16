package dev.whysoezzy.core_uikit.components.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.components.chips.PokemonTypeChip
import dev.whysoezzy.core_uikit.components.chips.TypeChipSize
import dev.whysoezzy.core_uikit.extensions.toDisplayName
import dev.whysoezzy.core_uikit.extensions.toTypeColor
import dev.whysoezzy.core_uikit.theme.AnimationSpecs
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonBrandColors
import dev.whysoezzy.core_uikit.theme.PokemonElevation
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing


@Composable
fun PokemonGridCard(
    id: Int,
    name: String,
    imageUrl: String,
    types: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null,
    showFavoriteButton: Boolean = false,
) {
    // Interaction source для анимаций
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Анимация scale при нажатии
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = AnimationSpecs.Bouncy,
        label = "card_scale"
    )

    // Анимация elevation
    val elevation by animateFloatAsState(
        targetValue = if (isPressed)
            PokemonElevation.PokemonCard.value
        else
            PokemonElevation.PokemonCardHover.value,
        label = "card_elevation"
    )

    // Цвета градиента на основе типа покемона
    val primaryType = types.firstOrNull() ?: "normal"
    val primaryColor = primaryType.toTypeColor()
    val secondaryColor = types.getOrNull(1)?.toTypeColor() ?: primaryColor

    // Gradient background - вертикальный для Grid карточки
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            primaryColor.copy(alpha = 0.15f),
            secondaryColor.copy(alpha = 0.08f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing().medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pokemon Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(spacing().small),
                    contentAlignment = Alignment.Center
                ) {
                    // Круглый фон под изображением
                    Box(
                        modifier = Modifier
                            .size(dimensions().pokemonImageSizeMedium * 0.9f)
                            .clip(CircleShape)
                            .background(PokemonBrandColors.White.copy(alpha = 0.3f))
                    )

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(spacing().small),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(dimensions().spacerSmall))

                // Pokemon Info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing().extraSmall)
                ) {
                    // ID
                    Text(
                        text = "#${id.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    // Name
                    Text(
                        text = name.toDisplayName(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                    Spacer(modifier = Modifier.height(dimensions().spacerExtraSmall))

                    // Types
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(
                            spacing().extraSmall,
                            Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(spacing().extraSmall),
                        maxItemsInEachRow = 2,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        types.take(2).forEach { type ->
                            PokemonTypeChip(
                                type = type,
                                size = TypeChipSize.SMALL
                            )
                        }
                    }
                }
            }

            if (showFavoriteButton && onFavoriteClick != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing().small)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (isFavorite) {
                                PokemonBrandColors.Red.copy(alpha = 0.15f)
                            } else {
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                            }
                        )
                        .clickable(onClick = onFavoriteClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) ImageVector.vectorResource(R.drawable.outline_favorite) else ImageVector.vectorResource(
                            R.drawable.outline_favorite_border_24
                        ),
                        contentDescription = if (isFavorite) "Удалить из избранного" else "Добавить в избранное",
                        tint = if (isFavorite) {
                            PokemonBrandColors.Red
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
