package dev.whysoezzy.core_uikit.components.errors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.theme.Elevation
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

/**
 * Универсальный компонент для отображения ошибок
 * Может использоваться как полноэкранный или как баннер
 */
@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    error: String,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    isFullScreen: Boolean = true,
) {
    if (isFullScreen) {
        // Полноэкранное сообщение об ошибке
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(spacing().extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.something_went_wrong),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(dimensions().spacerSmall))

            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(dimensions().spacerLarge))

            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing().mediumSmall),
            ) {
                if (onDismiss != null) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Dismiss")
                    }
                }

                if (onRetry != null) {
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.weight(if (onDismiss != null) 1f else 2f),
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    } else {
        // Компактный баннер ошибки
        Card(
            modifier = modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation.Level2),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(spacing().medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.error),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )

                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    )
                }

                if (onRetry != null) {
                    Button(
                        onClick = onRetry,
                        colors =
                            androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            ),
                    ) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }
    }
}
