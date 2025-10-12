package dev.whysoezzy.core_uikit.components.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import dev.whysoezzy.core_uikit.theme.PokemonTextStyles

@Composable
fun StatItem(
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            style = PokemonTextStyles.StatLabel,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        )
        Text(
            text = value,
            style = PokemonTextStyles.StatValue,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
