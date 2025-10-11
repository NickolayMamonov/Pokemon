package dev.whysoezzy.core_uikit.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing System
 * - none: 0dp - Нет отступа
 * - extraSmall: 4dp - Минимальный отступ между близкими элементами
 * - small: 8dp - Стандартный отступ между элементами
 * - medium: 16dp - Отступ между секциями в карточке
 * - large: 24dp - Отступ между карточками/секциями
 * - extraLarge: 32dp - Большой отступ между блоками
 * - huge: 48dp - Огромный отступ для особых случаев
 */
data class Spacing(
    val none: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 48.dp,
    val mega: Dp = 64.dp,
) {
    /**
     * Утилита для получения отступа по множителю базового spacing (4dp)
     * Например: spacing.grid(2) = 8dp, spacing.grid(3) = 12dp
     */
    fun grid(multiplier: Int): Dp = (4 * multiplier).dp
}

val LocalSpacing = compositionLocalOf { Spacing() }


/**
 * Helper function для получения текущего Spacing
 *
 * Использование:
 * val spacing = spacing()
 * Modifier.padding(spacing.medium)
 */
@Composable
@ReadOnlyComposable
fun spacing(): Spacing = LocalSpacing.current

/**
 * Адаптивные отступы для разных размеров экрана
 * Можно использовать для поддержки планшетов и складных устройств
 */
data class ResponsiveSpacing(
    val compact: Spacing = Spacing(),      // Телефоны в портретной ориентации
    val medium: Spacing = Spacing(         // Телефоны в landscape, маленькие планшеты
        extraSmall = 6.dp,
        small = 12.dp,
        medium = 20.dp,
        large = 28.dp,
        extraLarge = 40.dp,
        huge = 56.dp,
        mega = 72.dp
    ),
    val expanded: Spacing = Spacing(       // Планшеты, складные устройства
        extraSmall = 8.dp,
        small = 16.dp,
        medium = 24.dp,
        large = 32.dp,
        extraLarge = 48.dp,
        huge = 64.dp,
        mega = 80.dp
    )
)