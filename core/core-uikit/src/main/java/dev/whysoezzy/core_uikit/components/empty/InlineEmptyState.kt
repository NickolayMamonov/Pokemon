package dev.whysoezzy.core_uikit.components.empty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.components.buttons.PokemonButton
import dev.whysoezzy.core_uikit.components.buttons.PokemonButtonSize
import dev.whysoezzy.core_uikit.components.buttons.PokemonButtonStyle
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.spacing

@Composable
fun InlineEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = ImageVector.vectorResource(R.drawable.outline_inbox),
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = CustomShapes.PokemonCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing().large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing().medium)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (actionText != null && onActionClick != null) {
                PokemonButton(
                    text = actionText,
                    onClick = onActionClick,
                    size = PokemonButtonSize.SMALL,
                    style = PokemonButtonStyle.OUTLINED
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InlineEmptyStatePreview() {
    PokemonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing().medium),
                verticalArrangement = Arrangement.spacedBy(spacing().medium)
            ) {
                Text(
                    text = "Recent Pokemon",
                    style = MaterialTheme.typography.titleLarge
                )

                InlineEmptyState(
                    message = "Недавно просмотренных покемонов нет",
                    actionText = "Открыть Pokedex",
                    onActionClick = { }
                )
            }
        }
    }
}