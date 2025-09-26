package dev.whysoezzy.pokemon.presentation.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.whysoezzy.pokemon.domain.model.PokemonStat
import dev.whysoezzy.pokemon.presentation.components.stats.StatBar
import dev.whysoezzy.pokemon.presentation.utils.getStatDisplayName
import timber.log.Timber

@Composable
fun PokemonBattleStatsSection(
    stats: List<PokemonStat>,
    showExtended: Boolean,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(stats) {
        Timber.d("Stats in battle section: ${stats.map { "${it.name}: ${it.baseStat}" }}")
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Боевые характеристики",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            stats.forEach { stat ->
                StatBar(
                    statName = getStatDisplayName(stat.name),
                    statValue = stat.baseStat,
                    maxValue = 255,
                    effort = if (showExtended) stat.effort else null
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}