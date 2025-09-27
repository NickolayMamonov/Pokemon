package dev.whysoezzy.pokemon.presentation.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.whysoezzy.core_common.extensions.toDisplayName
import dev.whysoezzy.core_common.extensions.toTypeColor
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.pokemon.presentation.components.stats.StatItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonCard(
    modifier: Modifier = Modifier,
    pokemon: Pokemon,
    onPokemonClick: ((Pokemon) -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onPokemonClick?.invoke(pokemon)
            }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = "Изображение ${pokemon.name}",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Имя покемона
            Text(
                text = pokemon.name.toDisplayName(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )


            // Типы покемона
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pokemon.types.take(2).forEach { type ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = type.name.toTypeColor().copy(alpha = 0.3f)
//                                getTypeColor(
//                                type.name
//                            ).copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = type.name.uppercase(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = type.name.toTypeColor()
//                                getTypeColor(
//                                type.name
//                            )
                            ,
                            fontSize = 10.sp
                        )
                    }
                }

                if (pokemon.types.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Характеристики
            pokemon.let { p ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Рост", value = "${p.height * 10} см")
                    StatItem(label = "Вес", value = "${p.weight / 10f} кг")
                }
            }
        }
    }
}