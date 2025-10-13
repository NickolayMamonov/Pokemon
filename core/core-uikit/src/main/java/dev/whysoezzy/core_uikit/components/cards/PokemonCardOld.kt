package dev.whysoezzy.core_uikit.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.components.stats.StatItem
import dev.whysoezzy.core_uikit.extensions.toDisplayName
import dev.whysoezzy.core_uikit.extensions.toTypeColor
import dev.whysoezzy.core_uikit.theme.PokemonElevation
import dev.whysoezzy.core_uikit.theme.PokemonShapes
import dev.whysoezzy.core_uikit.theme.PokemonTextStyles
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing
import dev.whysoezzy.domain.model.Pokemon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonCardOld(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
    onPokemonClick: ((Pokemon) -> Unit)? = null,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable {
                    onPokemonClick?.invoke(pokemon)
                }
                .padding(spacing().small),
        elevation = CardDefaults.cardElevation(PokemonElevation.PokemonCard),
        shape = PokemonShapes.large,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(spacing().medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = stringResource(R.string.image, pokemon.name),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(spacing().small),
                contentScale = ContentScale.Fit,
            )

            Spacer(modifier = Modifier.height(dimensions().spacerMediumSmall))

            // Имя покемона
            Text(
                text = pokemon.name.toDisplayName(),
                style = PokemonTextStyles.PokemonName,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Типы покемона
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing().extraSmall),
                modifier = Modifier.fillMaxWidth(),
            ) {
                pokemon.types.take(2).forEach { type ->
                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor = type.name.toTypeColor().copy(alpha = 0.3f),
                            ),
                        shape = PokemonShapes.small,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = type.name.uppercase(),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = spacing().extraSmall),
                            style = PokemonTextStyles.TypeLabel,
                            textAlign = TextAlign.Center,
                            color = type.name.toTypeColor()
                        )
                    }
                }

                if (pokemon.types.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Характеристики
            pokemon.let { p ->
                Spacer(modifier = Modifier.height(dimensions().spacerMediumSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    StatItem(label = stringResource(R.string.height), value = "${p.height * 10} см")
                    StatItem(
                        label = stringResource(R.string.weight),
                        value = "${p.weight / 10f} кг"
                    )
                }
            }
        }
    }
}
