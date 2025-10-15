package dev.whysoezzy.core_uikit.components.snackbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.theme.Elevation
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.StatusColors
import dev.whysoezzy.core_uikit.theme.spacing

enum class SnackbarType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

@Composable
fun PokemonSnackbar(
    message: String,
    type: SnackbarType,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val (backgroundColor, contentColor, icon) = when (type) {
        SnackbarType.SUCCESS -> Triple(
            StatusColors.SuccessContainer,
            StatusColors.Success,
            ImageVector.vectorResource(R.drawable.baseline_check_circle)
        )

        SnackbarType.ERROR -> Triple(
            StatusColors.ErrorContainer,
            StatusColors.Error,
            ImageVector.vectorResource(R.drawable.baseline_error)
        )

        SnackbarType.WARNING -> Triple(
            StatusColors.WarningContainer,
            StatusColors.Warning,
            ImageVector.vectorResource(R.drawable.baseline_warning)
        )

        SnackbarType.INFO -> Triple(
            StatusColors.InfoContainer,
            StatusColors.Info,
            ImageVector.vectorResource(R.drawable.baseline_info)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing().medium),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Elevation.Level3
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing().medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(spacing().medium))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }

            if (actionLabel != null && onAction != null) {
                TextButton(
                    onClick = onAction,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = contentColor
                    )
                ) {
                    Text(
                        text = actionLabel,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
    ) { data ->
        // Определяем тип на основе сообщения
        val type = when {
            data.visuals.message.contains("успешно", ignoreCase = true) ||
                    data.visuals.message.contains(
                        "добавлен",
                        ignoreCase = true
                    ) -> SnackbarType.SUCCESS

            data.visuals.message.contains("ошибка", ignoreCase = true) ||
                    data.visuals.message.contains(
                        "не удалось",
                        ignoreCase = true
                    ) -> SnackbarType.ERROR

            data.visuals.message.contains("внимание", ignoreCase = true) ||
                    data.visuals.message.contains(
                        "предупреждение",
                        ignoreCase = true
                    ) -> SnackbarType.WARNING

            else -> SnackbarType.INFO
        }

        PokemonSnackbar(
            message = data.visuals.message,
            type = type,
            actionLabel = data.visuals.actionLabel,
            onAction = { data.performAction() }
        )
    }
}

suspend fun showPokemonSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    type: SnackbarType,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
): SnackbarResult {
    val messageWithType = when (type) {
        SnackbarType.SUCCESS -> "✓ $message"
        SnackbarType.ERROR -> "✗ Ошибка: $message"
        SnackbarType.WARNING -> "⚠ Внимание: $message"
        SnackbarType.INFO -> message
    }

    return snackbarHostState.showSnackbar(
        message = messageWithType,
        actionLabel = actionLabel,
        duration = duration
    )
}

@Preview(name = "Snackbars", showBackground = true, heightDp = 600)
@Composable
private fun PokemonSnackbarPreview() {
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
                    text = "Pokemon Snackbars",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // Success
                PokemonSnackbar(
                    message = "Покемон добавлен в избранное",
                    type = SnackbarType.SUCCESS
                )

                // Error
                PokemonSnackbar(
                    message = "Не удалось загрузить данные",
                    type = SnackbarType.ERROR,
                    actionLabel = "Повторить",
                    onAction = { }
                )

                // Warning
                PokemonSnackbar(
                    message = "Слабое подключение к интернету",
                    type = SnackbarType.WARNING
                )

                // Info
                PokemonSnackbar(
                    message = "Обновление доступно",
                    type = SnackbarType.INFO,
                    actionLabel = "Обновить",
                    onAction = { }
                )
            }
        }
    }
}