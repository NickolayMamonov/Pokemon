package dev.whysoezzy.core_uikit.components.chips

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.core_uikit.extensions.toTypeColor
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

enum class TypeChipSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun PokemonTypeChip(
    type: String,
    modifier: Modifier = Modifier,
    size: TypeChipSize = TypeChipSize.MEDIUM
) {
    val typeColor = type.toTypeColor()

    val (height, horizontalPadding, textStyle) = when (size) {
        TypeChipSize.SMALL -> Triple(
            dimensions().chipHeightSmall,
            spacing().small,
            MaterialTheme.typography.labelSmall
        )

        TypeChipSize.MEDIUM -> Triple(
            dimensions().chipHeightMedium,
            spacing().medium,
            MaterialTheme.typography.labelMedium
        )

        TypeChipSize.LARGE -> Triple(
            dimensions().chipHeightLarge,
            spacing().large,
            MaterialTheme.typography.labelLarge
        )
    }

    Surface(
        modifier = modifier.height(height),
        shape = CustomShapes.TypeChip,
        color = typeColor.copy(alpha = 0.2f)
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
                vertical = spacing().extraSmall
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = type.uppercase(),
                style = textStyle,
                color = typeColor
            )
        }
    }
}