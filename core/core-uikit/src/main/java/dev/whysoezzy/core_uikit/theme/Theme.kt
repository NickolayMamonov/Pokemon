package dev.whysoezzy.core_uikit.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

/**
 * Light Color Scheme
 * Светлая цветовая схема для Pokemon приложения
 */
private val LightColorScheme = lightColorScheme(
    primary = LightThemeColors.Primary,
    onPrimary = LightThemeColors.OnPrimary,
    primaryContainer = LightThemeColors.PrimaryContainer,
    onPrimaryContainer = LightThemeColors.OnPrimaryContainer,

    secondary = LightThemeColors.Secondary,
    onSecondary = LightThemeColors.OnSecondary,
    secondaryContainer = LightThemeColors.SecondaryContainer,
    onSecondaryContainer = LightThemeColors.OnSecondaryContainer,

    tertiary = LightThemeColors.Tertiary,
    onTertiary = LightThemeColors.OnTertiary,
    tertiaryContainer = LightThemeColors.TertiaryContainer,
    onTertiaryContainer = LightThemeColors.OnTertiaryContainer,

    error = StatusColors.Error,
    onError = StatusColors.OnError,
    errorContainer = StatusColors.ErrorContainer,

    background = LightThemeColors.Background,
    onBackground = LightThemeColors.OnBackground,

    surface = LightThemeColors.Surface,
    onSurface = LightThemeColors.OnSurface,
    surfaceVariant = LightThemeColors.SurfaceVariant,
    onSurfaceVariant = LightThemeColors.OnSurfaceVariant,

    outline = LightThemeColors.Outline,
    outlineVariant = LightThemeColors.OutlineVariant,
)

/**
 * Dark Color Scheme
 * Темная цветовая схема для Pokemon приложения
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkThemeColors.Primary,
    onPrimary = DarkThemeColors.OnPrimary,
    primaryContainer = DarkThemeColors.PrimaryContainer,
    onPrimaryContainer = DarkThemeColors.OnPrimaryContainer,

    secondary = DarkThemeColors.Secondary,
    onSecondary = DarkThemeColors.OnSecondary,
    secondaryContainer = DarkThemeColors.SecondaryContainer,
    onSecondaryContainer = DarkThemeColors.OnSecondaryContainer,

    tertiary = DarkThemeColors.Tertiary,
    onTertiary = DarkThemeColors.OnTertiary,
    tertiaryContainer = DarkThemeColors.TertiaryContainer,
    onTertiaryContainer = DarkThemeColors.OnTertiaryContainer,

    error = StatusColors.Error,
    onError = StatusColors.OnError,
    errorContainer = StatusColors.ErrorContainer,

    background = DarkThemeColors.Background,
    onBackground = DarkThemeColors.OnBackground,

    surface = DarkThemeColors.Surface,
    onSurface = DarkThemeColors.OnSurface,
    surfaceVariant = DarkThemeColors.SurfaceVariant,
    onSurfaceVariant = DarkThemeColors.OnSurfaceVariant,

    outline = DarkThemeColors.Outline,
    outlineVariant = DarkThemeColors.OutlineVariant,
)

/**
 * Theme Mode
 * Режимы темы приложения
 */
enum class ThemeMode {
    LIGHT,   // Всегда светлая тема
    DARK,    // Всегда темная тема
    SYSTEM   // Следовать системной теме
}

/**
 * Pokemon Theme
 * Главная функция темы приложения
 *
 * @param darkTheme Использовать темную тему (по умолчанию - системная настройка)
 * @param themeMode Режим темы (переопределяет darkTheme если не SYSTEM)
 * @param dynamicColor Использовать Dynamic Colors на Android 12+ (Material You)
 * @param content Composable контент приложения
 **/

@Composable
fun PokemonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> darkTheme
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalDimensions provides Dimensions(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

/**
 * Preview функции для тестирования темы
 */
@Composable
fun PokemonThemePreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    PokemonTheme(
        darkTheme = darkTheme,
        dynamicColor = false, // Отключаем для preview
        content = content
    )
}