package dev.whysoezzy.core_uikit.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.whysoezzy.core_uikit.R

val PokemonFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold)
)
//val Typography =
//    Typography(
//        bodyLarge =
//            TextStyle(
//                fontFamily = FontFamily.Default,
//                fontWeight = FontWeight.Normal,
//                fontSize = 16.sp,
//                lineHeight = 24.sp,
//                letterSpacing = 0.5.sp,
//            ),
//        /* Other default text styles to override
//        titleLarge = TextStyle(
//            fontFamily = FontFamily.Default,
//            fontWeight = FontWeight.Normal,
//            fontSize = 22.sp,
//            lineHeight = 28.sp,
//            letterSpacing = 0.sp
//        ),
//        labelSmall = TextStyle(
//            fontFamily = FontFamily.Default,
//            fontWeight = FontWeight.Medium,
//            fontSize = 11.sp,
//            lineHeight = 16.sp,
//            letterSpacing = 0.5.sp
//        )
//         */
//    )


/**
 * Pokemon Typography
 * Полная типографическая шкала согласно Material Design 3
 *
 * Иерархия:
 * - Display: Самые большие заголовки (Hero sections)
 * - Headline: Заголовки секций
 * - Title: Заголовки карточек и элементов
 * - Body: Основной текст
 * - Label: Метки, кнопки, caption
 */

val Typography = Typography(
    // ========================================
    // DISPLAY STYLES
    // Используется для: Hero заголовки, splash экраны
    // ========================================
    displayLarge = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),

    // ========================================
    // HEADLINE STYLES
    // Используется для: Заголовки экранов, секций
    // ========================================
    headlineLarge = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),

    // ========================================
    // TITLE STYLES
    // Используется для: Заголовки карточек, списков
    // ========================================
    titleLarge = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // ========================================
    // BODY STYLES
    // Используется для: Основной текст, параграфы
    // ========================================
    bodyLarge = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),

    // ========================================
    // LABEL STYLES
    // Используется для: Кнопки, метки, chips, captions
    // ========================================
    labelLarge = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)


/**
 * Получить стиль body текста по размеру
 */
fun Typography.getBodyStyle(size: BodySize): TextStyle = when (size) {
    BodySize.LARGE -> bodyLarge
    BodySize.MEDIUM -> bodyMedium
    BodySize.SMALL -> bodySmall
}

enum class BodySize {
    LARGE, MEDIUM, SMALL
}

/**
 * Pokemon специфичные text styles
 * Дополнительные стили для Pokemon элементов
 */
object PokemonTextStyles {
    /**
     * Стиль для отображения ID покемона (#001, #025)
     */
    val PokemonId = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )

    /**
     * Стиль для имен покемонов в карточках
     */
    val PokemonName = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    )

    /**
     * Стиль для названий типов (FIRE, WATER)
     */
    val TypeLabel = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp, // Увеличенный для uppercase
    )

    /**
     * Стиль для значений статистик
     */
    val StatValue = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    )

    /**
     * Стиль для названий статистик
     */
    val StatLabel = TextStyle(
        fontFamily = PokemonFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    )
}