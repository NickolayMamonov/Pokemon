package dev.whysoezzy.core_uikit.components.chips

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.whysoezzy.core_uikit.extensions.toDisplayName
import dev.whysoezzy.core_uikit.extensions.toTypeColor
import dev.whysoezzy.core_uikit.theme.PokemonTextStyles


@Composable
fun TypeFilterChip(
    type: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        onClick = onClick,
        label = {
            Text(
                text = type.toDisplayName(),
                style = PokemonTextStyles.TypeLabel,
            )
        },
        selected = isSelected,
        colors =
            FilterChipDefaults.filterChipColors(
                selectedContainerColor = type.toTypeColor().copy(alpha = 0.3f),
                selectedLabelColor = type.toTypeColor(),
            ),
    )
}
