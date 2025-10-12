package dev.whysoezzy.core_uikit.components.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import dev.whysoezzy.core_uikit.extensions.toStatColor
import dev.whysoezzy.core_uikit.theme.PokemonShapes
import dev.whysoezzy.core_uikit.theme.PokemonTextStyles
import dev.whysoezzy.core_uikit.theme.dimensions

@Composable
fun StatBar(
    statName: String,
    statValue: Int,
    maxValue: Int,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = statName,
                style = PokemonTextStyles.StatLabel,
                fontWeight = FontWeight.Medium,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = statValue.toString(),
                    style = PokemonTextStyles.StatValue,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensions().spacerExtraSmall))

        LinearProgressIndicator(
            progress = { statValue.toFloat() / maxValue.toFloat() },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(dimensions().spacerSmall)
                    .clip(PokemonShapes.extraSmall),
            color = statValue.toStatColor(),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
