package dev.whysoezzy.core_uikit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Pokemon Shapes
 * Предопределенные формы для UI элементов
 */
val PokemonShapes = Shapes(
    // Extra Small - Маленькие chips, badges
    extraSmall = RoundedCornerShape(4.dp),

    // Small - Buttons, small cards
    small = RoundedCornerShape(8.dp),

    // Medium - Cards, dialogs (по умолчанию)
    medium = RoundedCornerShape(12.dp),

    // Large - Bottom sheets, larger cards
    large = RoundedCornerShape(16.dp),

    // Extra Large - Special surfaces
    extraLarge = RoundedCornerShape(28.dp),
)

/**
 * Custom Shapes для Pokemon элементов
 */
object CustomShapes {
    /**
     * Форма для карточек покемонов
     */
    val PokemonCard = RoundedCornerShape(16.dp)

    /**
     * Форма для type chips
     */
    val TypeChip = RoundedCornerShape(8.dp)

    /**
     * Форма для кнопок
     */
    val Button = RoundedCornerShape(12.dp)

    /**
     * Форма для progress bar
     */
    val ProgressBar = RoundedCornerShape(4.dp)

    /**
     * Полностью круглая форма
     */
    val Circle = RoundedCornerShape(50)

    /**
     * Форма для bottom sheet
     */
    val BottomSheet = RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    /**
     * Форма для диалогов
     */
    val Dialog = RoundedCornerShape(28.dp)
}