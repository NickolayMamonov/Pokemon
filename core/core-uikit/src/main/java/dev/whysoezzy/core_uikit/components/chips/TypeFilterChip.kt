package dev.whysoezzy.core_uikit.components.chips

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import dev.whysoezzy.core_common.extensions.toDisplayName
import dev.whysoezzy.core_common.extensions.toTypeColor

@OptIn(ExperimentalMaterial3Api::class)
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
                fontSize = 14.sp,
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
