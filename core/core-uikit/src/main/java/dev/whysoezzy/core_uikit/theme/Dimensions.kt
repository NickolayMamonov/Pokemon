package dev.whysoezzy.core_uikit.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Dimensions System
 *
 * Содержит:
 * - Размеры elevation (тени)
 * - Скругления углов
 * - Размеры кнопок
 * - Размеры иконок
 * - Размеры изображений покемонов
 * - Размеры разделителей
 */
data class Dimensions(
    // ========================================
    // ELEVATION (Тени)
    // ========================================
    val elevationNone: Dp = 0.dp,
    val elevationSmall: Dp = 2.dp,
    val elevationMedium: Dp = 4.dp,
    val elevationLarge: Dp = 8.dp,
    val elevationExtraLarge: Dp = 16.dp,

    // ========================================
    // CORNER RADIUS (Скругления)
    // ========================================
    val cornerRadiusNone: Dp = 0.dp,
    val cornerRadiusSmall: Dp = 4.dp,
    val cornerRadiusMedium: Dp = 8.dp,
    val cornerRadiusLarge: Dp = 12.dp,
    val cornerRadiusExtraLarge: Dp = 16.dp,
    val cornerRadiusFull: Dp = 999.dp, // Полностью скругленные края

    // ========================================
    // BUTTONS (Кнопки)
    // ========================================
    val buttonHeightSmall: Dp = 32.dp,
    val buttonHeightMedium: Dp = 40.dp,
    val buttonHeightLarge: Dp = 48.dp,
    val buttonHeightExtraLarge: Dp = 56.dp,

    val buttonMinWidth: Dp = 64.dp,
    val buttonPaddingHorizontal: Dp = 16.dp,
    val buttonPaddingVertical: Dp = 8.dp,

    // ========================================
    // ICONS (Иконки)
    // ========================================
    val iconSizeSmall: Dp = 16.dp,
    val iconSizeMedium: Dp = 24.dp,
    val iconSizeLarge: Dp = 32.dp,
    val iconSizeExtraLarge: Dp = 48.dp,
    val iconSizeHuge: Dp = 64.dp,

    // ========================================
    // POKEMON IMAGES (Изображения покемонов)
    // ========================================
    val pokemonImageSizeTiny: Dp = 48.dp,      // Для маленьких preview
    val pokemonImageSizeSmall: Dp = 80.dp,     // Для списков
    val pokemonImageSizeMedium: Dp = 120.dp,   // Для карточек
    val pokemonImageSizeLarge: Dp = 200.dp,    // Для detail экрана
    val pokemonImageSizeExtraLarge: Dp = 300.dp, // Для hero секций

    // ========================================
    // CARDS (Карточки)
    // ========================================
    val cardElevation: Dp = 4.dp,
    val cardCornerRadius: Dp = 16.dp,
    val cardPadding: Dp = 16.dp,
    val cardPaddingSmall: Dp = 12.dp,
    val cardPaddingLarge: Dp = 20.dp,

    // ========================================
    // DIVIDERS (Разделители)
    // ========================================
    val dividerThickness: Dp = 1.dp,
    val dividerThicknessThick: Dp = 2.dp,

    // ========================================
    // CHIPS (Чипсы для типов)
    // ========================================
    val chipHeightSmall: Dp = 20.dp,
    val chipHeightMedium: Dp = 28.dp,
    val chipHeightLarge: Dp = 36.dp,
    val chipPaddingHorizontal: Dp = 8.dp,
    val chipCornerRadius: Dp = 8.dp,

    // ========================================
    // PROGRESS INDICATORS (Индикаторы прогресса)
    // ========================================
    val progressBarHeight: Dp = 8.dp,
    val progressBarHeightLarge: Dp = 12.dp,
    val progressBarCornerRadius: Dp = 4.dp,

    // ========================================
    // BOTTOM NAVIGATION
    // ========================================
    val bottomNavHeight: Dp = 80.dp,
    val bottomNavIconSize: Dp = 24.dp,

    // ========================================
    // TOP APP BAR
    // ========================================
    val topAppBarHeight: Dp = 64.dp,
    val topAppBarIconSize: Dp = 24.dp,

    // ========================================
    // BORDERS (Границы)
    // ========================================
    val borderWidthThin: Dp = 1.dp,
    val borderWidthMedium: Dp = 2.dp,
    val borderWidthThick: Dp = 4.dp,

    // ========================================
    // MISC (Разное)
    // ========================================
    val minTouchTargetSize: Dp = 48.dp, // Minimum touch target (Material Design)
    val fabSize: Dp = 56.dp,             // Floating Action Button
    val fabSizeSmall: Dp = 40.dp,
    val fabSizeLarge: Dp = 96.dp,
)

val LocalDimensions = compositionLocalOf { Dimensions() }

/**
 * Helper function для получения текущих Dimensions
 *
 * Использование:
 * val dimensions = dimensions()
 * Modifier.size(dimensions.iconSizeLarge)
 */
@Composable
@ReadOnlyComposable
fun dimensions(): Dimensions = LocalDimensions.current

/**
 * Умножение Dp на число
 */
operator fun Dp.times(multiplier: Int): Dp = this * multiplier.toFloat()

/**
 * Половина от текущего значения
 */
val Dp.half: Dp get() = this / 2