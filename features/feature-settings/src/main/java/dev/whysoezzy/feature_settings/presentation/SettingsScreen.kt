package dev.whysoezzy.feature_settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.components.buttons.PokemonButton
import dev.whysoezzy.core_uikit.components.buttons.PokemonButtonSize
import dev.whysoezzy.core_uikit.components.buttons.PokemonButtonStyle
import dev.whysoezzy.core_uikit.components.errors.ErrorMessage
import dev.whysoezzy.core_uikit.components.loadings.LoadingIndicator
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonElevation
import dev.whysoezzy.core_uikit.theme.spacing
import dev.whysoezzy.domain.model.AppTheme
import dev.whysoezzy.domain.model.Settings
import org.koin.androidx.compose.koinViewModel
import kotlin.math.log10
import kotlin.math.pow

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showClearCacheDialog by remember { mutableStateOf(false) }

    LaunchedEffect(events) {
        events?.let { event ->
            when (event) {
                is SettingsEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is SettingsEvent.CacheClearSuccess -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is SettingsEvent.CacheClearError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is SettingsUIState.Loading -> {
                LoadingIndicator()
            }

            is SettingsUIState.Error -> {
                ErrorMessage(error = state.message)
            }

            is SettingsUIState.Success -> {

                SettingsContent(
                    settings = state.settings,
                    onThemeChange = viewModel::saveTheme,
                    onClearCacheClick = { showClearCacheDialog = true },
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text("Очистить кэш?") },
            text = { Text("Это действие удалит все сохранённые данные покемонов и изображения.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearCache()
                    showClearCacheDialog = false
                }) {
                    Text("Очистить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

}

@Composable
fun SettingsContent(
    settings: Settings,
    onThemeChange: (AppTheme) -> Unit,
    onClearCacheClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing().medium),
        verticalArrangement = Arrangement.spacedBy(spacing().medium)
    ) {
        item {
            Text(
                text = "Настройки",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Spacer(modifier = Modifier.height(spacing().small))
        }
        item {
            SettingsSection(
                title = "Тема приложения",
                icon = ImageVector.vectorResource(R.drawable.outline_palette)
            ) {
                ThemeSelector(
                    currentTheme = settings.theme,
                    onThemeSelected = onThemeChange
                )
            }
        }
        item {
            SettingsSection(
                title = "Хранилище",
                icon = ImageVector.vectorResource(R.drawable.outline_storage)
            ) {
                CacheInfo(
                    cacheSize = settings.cacheSize,
                    onClearCacheClick = onClearCacheClick
                )
            }
        }
        item {
            SettingsSection(
                title = "О приложении",
                icon = ImageVector.vectorResource(R.drawable.outline_info)
            ) {
                AboutInfo()
            }
        }

    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CustomShapes.PokemonCard,
        elevation = CardDefaults.cardElevation(
            defaultElevation = PokemonElevation.PokemonCard
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing().medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing().small)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(spacing().medium))
            content()
        }
    }
}

@Composable
private fun ThemeSelector(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing().small)
    ) {
        AppTheme.entries.forEach { theme ->
            ThemeOption(
                theme = theme,
                isSelected = currentTheme == theme,
                onSelect = { onThemeSelected(theme) }
            )
        }
    }
}

@Composable
private fun ThemeOption(
    theme: AppTheme,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing().extraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
        Text(
            text = when (theme) {
                AppTheme.LIGHT -> "Светлая"
                AppTheme.DARK -> "Тёмная"
                AppTheme.SYSTEM -> "Системная"
            },
            modifier = Modifier.padding(start = spacing().small)
        )
    }
}

@Composable
private fun CacheInfo(
    cacheSize: Long,
    onClearCacheClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing().medium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Размер кэша:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = formatBytes(cacheSize),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        HorizontalDivider()

        PokemonButton(
            text = "Очистить кэш",
            onClick = onClearCacheClick,
            style = PokemonButtonStyle.OUTLINED,
            size = PokemonButtonSize.MEDIUM,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AboutInfo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacing().small)
    ) {
        InfoRow("Версия приложения", "1.0.0")
        InfoRow("Разработчик", "whysoezzy")
        InfoRow("API", "PokeAPI v2")
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )

    }
}


private fun formatBytes(bytes: Long): String {
    if (bytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB")
    val digitGroups = (log10(bytes.toDouble()) / log10(1024.0)).toInt()
    return "%.2f %s".format(
        bytes / 1024.0.pow(digitGroups.toDouble()),
        units[digitGroups]
    )
}