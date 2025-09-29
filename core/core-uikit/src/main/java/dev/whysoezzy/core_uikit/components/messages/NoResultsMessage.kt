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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoResultsMessage(
    message: String,
    actionText: String? = null,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔍",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Results Found",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            if (onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onRetry
                ) {
                    Text(actionText ?: "Retry")
                }
            }
        }
    }
}

// Overloaded version for backward compatibility
@Composable
fun NoResultsMessage(
    hasActiveFilters: Boolean = false,
    onClearFilters: () -> Unit = {}
) {
    val message = if (hasActiveFilters) {
        "Покемоны с выбранными фильтрами не найдены.\nПопробуйте изменить параметры поиска."
    } else {
        "Покемоны не загружены.\nПроверьте подключение к интернету."
    }

    NoResultsMessage(
        message = message,
        actionText = if (hasActiveFilters) "Очистить фильтры" else null,
        onRetry = if (hasActiveFilters) onClearFilters else null
    )
}
