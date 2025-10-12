package dev.whysoezzy.core_uikit.components.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

@Composable
fun NoResultsMessage(
    modifier: Modifier = Modifier,
    message: String,
    actionText: String? = null,
    onRetry: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(spacing().extraLarge),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.no_results_found),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(dimensions().spacerSmall))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
            )

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(dimensions().spacerMedium))
                Button(
                    onClick = onRetry,
                ) {
                    Text(actionText ?: stringResource(R.string.retry))
                }
            }
        }
    }
}