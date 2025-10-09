package dev.whysoezzy.feature_pokemon_details.components.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_common.extensions.toDisplayName
import dev.whysoezzy.core_common.extensions.toTypeColor
import dev.whysoezzy.domain.model.PokemonType

@Composable
fun PokemonTypesSection(
    types: List<PokemonType>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Типы",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                types.forEach { type ->
                    Card(
                        colors =
                            CardDefaults.cardColors(
                                containerColor = type.name.toTypeColor().copy(alpha = 0.3f),
                            ),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            text = type.name.toDisplayName(),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = type.name.toTypeColor(),
                        )
                    }
                }
            }
        }
    }
}
