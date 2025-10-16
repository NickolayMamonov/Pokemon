package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.extensions.toTypeColor
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonBattleStatsSection
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonHeaderSection
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonPhysicalStatsSection
import dev.whysoezzy.feature_pokemon_details.components.sections.PokemonTypesSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailsContent(
    pokemon: Pokemon,
    onBackClick: () -> Unit
) {
    val primaryType = pokemon.types.firstOrNull()?.name ?: "normal"
    val backgroundColor = primaryType.toTypeColor()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        item {
            PokemonHeaderSection(pokemon = pokemon, backgroundColor = backgroundColor)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            PokemonTypesSection(
                types = pokemon.types,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            PokemonPhysicalStatsSection(
                height = pokemon.height,
                weight = pokemon.weight,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            PokemonBattleStatsSection(
                stats = pokemon.stats,
                showExtended = false,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}