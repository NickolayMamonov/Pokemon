package dev.whysoezzy.core_uikit.components.loadings

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.components.cards.PokemonCardVariant
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonShapes
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.ShimmerColors
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

@Composable
fun Modifier.shimmerEffect(
    durationMillis: Int = 1200,
    colors: List<Color> = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        ShimmerColors.Light
    } else {
        ShimmerColors.Dark
    }
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(translateAnimation, translateAnimation),
        end = Offset(translateAnimation + 300f, translateAnimation + 300f)
    )

    return this.background(brush)
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = PokemonShapes.small
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmerEffect()

    ) {}

}

@Composable
fun PokemonCardSkeleton(
    modifier: Modifier = Modifier,
    variant: PokemonCardVariant = PokemonCardVariant.STANDARD
) {
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
            .height(cardHeight),
        shape = CustomShapes.PokemonCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing().medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image skeleton
            ShimmerBox(
                modifier = Modifier.size(imageSize),
                shape = CircleShape
            )

            Spacer(modifier = Modifier.width(spacing().medium))

            // Info skeleton
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing().small)
            ) {
                // ID skeleton
                ShimmerBox(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                )

                // Name skeleton
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(24.dp)
                )

                // Types skeleton
                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing().small)
                ) {
                    repeat(2) {
                        ShimmerBox(
                            modifier = Modifier
                                .width(60.dp)
                                .height(dimensions().chipHeightSmall),
                            shape = CustomShapes.TypeChip
                        )
                    }
                }
            }
        }
    }
}

/**
 * Generic Skeleton Components
 * Универсальные skeleton компоненты
 */
object SkeletonComponents {
    /**
     * Skeleton для текста
     */
    @Composable
    fun TextLine(
        modifier: Modifier = Modifier,
        height: Dp = 16.dp,
        widthFraction: Float = 1f
    ) {
        ShimmerBox(
            modifier = modifier
                .fillMaxWidth(widthFraction)
                .height(height)
        )
    }

    /**
     * Skeleton для круглого изображения
     */
    @Composable
    fun CircleImage(
        modifier: Modifier = Modifier,
        size: Dp = 48.dp
    ) {
        ShimmerBox(
            modifier = modifier.size(size),
            shape = CircleShape
        )
    }

    /**
     * Skeleton для прямоугольного изображения
     */
    @Composable
    fun RectangleImage(
        modifier: Modifier = Modifier,
        width: Dp = 100.dp,
        height: Dp = 100.dp,
        cornerRadius: Dp = dimensions().cornerRadiusMedium
    ) {
        ShimmerBox(
            modifier = modifier.size(width, height),
            shape = RoundedCornerShape(cornerRadius)
        )
    }

    /**
     * Skeleton для кнопки
     */
    @Composable
    fun Button(
        modifier: Modifier = Modifier,
        width: Dp = 120.dp,
        height: Dp = dimensions().buttonHeightMedium
    ) {
        ShimmerBox(
            modifier = modifier.size(width, height),
            shape = CustomShapes.Button
        )
    }
}

/**
 * Preview
 */
@Preview(name = "Shimmer Effects - Light", showBackground = true)
@Composable
private fun ShimmerEffectPreviewLight() {
    PokemonTheme(darkTheme = false) {
        ShimmerShowcase()
    }
}

@Preview(name = "Shimmer Effects - Dark", showBackground = true)
@Composable
private fun ShimmerEffectPreviewDark() {
    PokemonTheme(darkTheme = true) {
        ShimmerShowcase()
    }
}

@Composable
private fun ShimmerShowcase() {
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
            // Pokemon Card Skeletons
            PokemonCardSkeleton(variant = PokemonCardVariant.STANDARD)
            PokemonCardSkeleton(variant = PokemonCardVariant.COMPACT)

            // Generic skeletons
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing().medium)
            ) {
                SkeletonComponents.CircleImage(size = 64.dp)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing().small)
                ) {
                    SkeletonComponents.TextLine(height = 20.dp, widthFraction = 0.6f)
                    SkeletonComponents.TextLine(height = 16.dp, widthFraction = 0.9f)
                    SkeletonComponents.TextLine(height = 16.dp, widthFraction = 0.7f)
                }
            }

            // Button skeleton
            SkeletonComponents.Button(
                modifier = Modifier.fillMaxWidth(),
                height = dimensions().buttonHeightLarge
            )
        }
    }
}