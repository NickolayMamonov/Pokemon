package dev.whysoezzy.feature_pokemon_details.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.whysoezzy.core_common.extensions.toDisplayName
import dev.whysoezzy.core_common.extensions.toTypeColor
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = pokemon.name.toDisplayName(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor.copy(alpha = 0.3f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = paddingValues
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

}