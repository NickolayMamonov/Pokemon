package dev.whysoezzy.core_uikit.theme

import androidx.compose.ui.graphics.Color

//Основные цвета
object PokemonBrandColors {
    val Red = Color(0xFFDC0A2D)      // Классический Pokemon красный
    val Blue = Color(0xFF3B4CCA)     // Классический Pokemon синий
    val Yellow = Color(0xFFFFCB05)   // Классический Pokemon желтый
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF1C1C1C)
}

object LightThemeColors {
    // Primary
    val Primary = PokemonBrandColors.Red
    val OnPrimary = Color.White
    val PrimaryContainer = Color(0xFFFFDAD6)
    val OnPrimaryContainer = Color(0xFF410002)

    // Secondary
    val Secondary = PokemonBrandColors.Blue
    val OnSecondary = Color.White
    val SecondaryContainer = Color(0xFFD8E2FF)
    val OnSecondaryContainer = Color(0xFF001A41)

    // Tertiary
    val Tertiary = PokemonBrandColors.Yellow
    val OnTertiary = Color(0xFF3E2E00)
    val TertiaryContainer = Color(0xFFFFDF9B)
    val OnTertiaryContainer = Color(0xFF261A00)

    // Background & Surface
    val Background = Color(0xFFFFFBFE)
    val OnBackground = Color(0xFF1C1B1F)
    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF1C1B1F)
    val SurfaceVariant = Color(0xFFF3F2F7)
    val OnSurfaceVariant = Color(0xFF49454F)

    // Outline
    val Outline = Color(0xFF79747E)
    val OutlineVariant = Color(0xFFCAC4D0)
}

object DarkThemeColors {
    // Primary
    val Primary = Color(0xFFFFB4AB)
    val OnPrimary = Color(0xFF690005)
    val PrimaryContainer = Color(0xFF93000A)
    val OnPrimaryContainer = Color(0xFFFFDAD6)

    // Secondary
    val Secondary = Color(0xFFADC6FF)
    val OnSecondary = Color(0xFF002E69)
    val SecondaryContainer = Color(0xFF004494)
    val OnSecondaryContainer = Color(0xFFD8E2FF)

    // Tertiary
    val Tertiary = Color(0xFFFFD54F)
    val OnTertiary = Color(0xFF3E2E00)
    val TertiaryContainer = Color(0xFF5C4400)
    val OnTertiaryContainer = Color(0xFFFFDF9B)

    // Background & Surface
    val Background = Color(0xFF1C1B1F)
    val OnBackground = Color(0xFFE6E1E5)
    val Surface = Color(0xFF1C1B1F)
    val OnSurface = Color(0xFFE6E1E5)
    val SurfaceVariant = Color(0xFF49454F)
    val OnSurfaceVariant = Color(0xFFCAC4D0)

    // Outline
    val Outline = Color(0xFF938F99)
    val OutlineVariant = Color(0xFF49454F)
}


//Цвета для типов покемонов
object PokemonTypeColors {
    val Normal = Color(0xFFA8A878)
    val Fire = Color(0xFFF08030)
    val Water = Color(0xFF6890F0)
    val Electric = Color(0xFFF8D030)
    val Grass = Color(0xFF78C850)
    val Ice = Color(0xFF98D8D8)
    val Fighting = Color(0xFFC03028)
    val Poison = Color(0xFFA040A0)
    val Ground = Color(0xFFE0C068)
    val Flying = Color(0xFFA890F0)
    val Psychic = Color(0xFFF85888)
    val Bug = Color(0xFFA8B820)
    val Rock = Color(0xFFB8A038)
    val Ghost = Color(0xFF705898)
    val Dragon = Color(0xFF7038F8)
    val Dark = Color(0xFF705848)
    val Steel = Color(0xFFB8B8D0)
    val Fairy = Color(0xFFEE99AC)
}

//Цвета для статусов и уведомлений
object StatusColors {
    val Success = Color(0xFF4CAF50)
    val SuccessContainer = Color(0xFFC8E6C9)
    val OnSuccess = Color.White

    val Warning = Color(0xFFFF9800)
    val WarningContainer = Color(0xFFFFE0B2)
    val OnWarning = Color.White

    val Error = Color(0xFFF44336)
    val ErrorContainer = Color(0xFFFFCDD2)
    val OnError = Color.White

    val Info = Color(0xFF2196F3)
    val InfoContainer = Color(0xFFBBDEFB)
    val OnInfo = Color.White
}

//Цвета для отображения статистик покемонов
object StatColors {
    val VeryLow = Color(0xFFF44336)      // 0-49
    val Low = Color(0xFFFFC107)           // 50-79
    val Medium = Color(0xFFFF9800)        // 80-99
    val High = Color(0xFF4CAF50)          // 100-119
    val VeryHigh = Color(0xFF2196F3)      // 120+
    val Excellent = Color(0xFF9C27B0)     // 150+


    //Получить цвет на основе значения статистики
    fun getColorForStat(value: Int): Color = when {
        value >= 150 -> Excellent
        value >= 120 -> VeryHigh
        value >= 100 -> High
        value >= 80 -> Medium
        value >= 50 -> Low
        else -> VeryLow
    }
}

//Градиенты для различных элементов UI
object GradientColors {
    val PokemonRed = listOf(
        Color(0xFFDC0A2D),
        Color(0xFFB71C1C)
    )

    val PokemonBlue = listOf(
        Color(0xFF3B4CCA),
        Color(0xFF1976D2)
    )

    val Sunset = listOf(
        Color(0xFFFF6B6B),
        Color(0xFFFFD93D)
    )

    val Ocean = listOf(
        Color(0xFF4A90E2),
        Color(0xFF50E3C2)
    )

    val Forest = listOf(
        Color(0xFF56AB2F),
        Color(0xFFA8E063)
    )
}

//Цвета для эффектов загрузки
object ShimmerColors {
    val Light = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0)
    )

    val Dark = listOf(
        Color(0xFF2C2C2C),
        Color(0xFF3A3A3A),
        Color(0xFF2C2C2C)
    )
}