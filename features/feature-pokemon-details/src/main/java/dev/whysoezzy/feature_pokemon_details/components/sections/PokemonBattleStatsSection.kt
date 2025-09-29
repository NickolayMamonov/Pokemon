package dev.whysoezzy.feature_pokemon_details.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_common.extensions.toStatDisplayName
import dev.whysoezzy.core_uikit.components.stats.StatBar
import dev.whysoezzy.domain.model.PokemonStat
import timber.log.Timber

@Composable
fun PokemonBattleStatsSection(
    modifier: Modifier = Modifier,
    stats: List<PokemonStat>,
    showExtended: Boolean,
    onToggleExtended: ((Boolean) -> Unit)? = null,
) {
    LaunchedEffect(stats) {
        Timber.d("Stats in battle section: ${stats.map { "${it.name}: ${it.baseStat}" }}")
        Timber.d("Stats count: ${stats.size}")
        if (stats.isNotEmpty()) {
            val totalStats = stats.sumOf { it.baseStat }
            Timber.d("Total stats calculated: $totalStats")
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Battle Stats",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )

                if (onToggleExtended != null) {
                    IconButton(
                        onClick = { onToggleExtended(!showExtended) },
                    ) {
                        Icon(
                            imageVector = if (showExtended) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = if (showExtended) "Hide Extended Stats" else "Show Extended Stats",
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            stats.forEach { stat ->
                StatBar(
                    statName = stat.name.toStatDisplayName(),
                    statValue = stat.baseStat,
                    maxValue = 255,
                    effort = if (showExtended) stat.effort else null,
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (showExtended) {
                Spacer(modifier = Modifier.height(8.dp))

                // Показываем общую статистику
                val totalStats = stats.sumOf { it.baseStat }
                Text(
                    text = "Total Base Stats: $totalStats",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
