package dev.whysoezzy.core_uikit.components.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.whysoezzy.core_uikit.theme.PokemonTextStyles
import dev.whysoezzy.core_uikit.theme.dimensions

@Composable
fun PhysicalStatItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(dimensions().iconSizeLarge),
        )

        Spacer(modifier = Modifier.height(dimensions().spacerSmall))

        Text(
            text = label,
            style = PokemonTextStyles.StatLabel,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )

        Text(
            text = value,
            style = PokemonTextStyles.StatValue,
        )
    }
}
