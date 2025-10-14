package dev.whysoezzy.core_uikit.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Elevation System
 * Система теней согласно Material Design 3
 * Используется для создания визуальной иерархии элементов
 */
object Elevation {
    /**
     * Level 0 - Нет тени
     * Для: Встроенных элементов, background
     */
    val Level0: Dp = 0.dp

    /**
     * Level 1 - Минимальная тень
     * Для: Cards в списках, минимально приподнятые элементы
     */
    val Level1: Dp = 1.dp

    /**
     * Level 2 - Легкая тень
     * Для: Стандартных cards, содержащих контент
     */
    val Level2: Dp = 3.dp

    /**
     * Level 3 - Средняя тень
     * Для: Меню, поднятые cards
     */
    val Level3: Dp = 6.dp

    /**
     * Level 4 - Заметная тень
     * Для: Navigation drawer, bottom sheets
     */
    val Level4: Dp = 8.dp

    /**
     * Level 5 - Сильная тень
     * Для: Dialogs, modals, FAB
     */
    val Level5: Dp = 12.dp
}

/**
 * Pokemon-specific Elevations
 * Elevation для конкретных Pokemon элементов
 */
object PokemonElevation {
    val PokemonCard: Dp = Elevation.Level2
    val PokemonCardHover: Dp = Elevation.Level3
    val BottomNavigation: Dp = Elevation.Level3
    val TopAppBar: Dp = Elevation.Level0
    val Dialog: Dp = Elevation.Level5
    val BottomSheet: Dp = Elevation.Level4
    val FAB: Dp = Elevation.Level3


}