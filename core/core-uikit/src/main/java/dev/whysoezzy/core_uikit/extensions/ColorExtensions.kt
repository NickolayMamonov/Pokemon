package dev.whysoezzy.core_uikit.extensions

import androidx.compose.ui.graphics.Color
import dev.whysoezzy.core_uikit.theme.PokemonTypeColors
import dev.whysoezzy.core_uikit.theme.StatColors

/**
 * Extension function для получения цвета типа покемона
 *
 * Использует централизованные цвета из Design System
 */
fun String.toTypeColor(): Color = when (this.lowercase()) {
    "normal" -> PokemonTypeColors.Normal
    "fire" -> PokemonTypeColors.Fire
    "water" -> PokemonTypeColors.Water
    "electric" -> PokemonTypeColors.Electric
    "grass" -> PokemonTypeColors.Grass
    "ice" -> PokemonTypeColors.Ice
    "fighting" -> PokemonTypeColors.Fighting
    "poison" -> PokemonTypeColors.Poison
    "ground" -> PokemonTypeColors.Ground
    "flying" -> PokemonTypeColors.Flying
    "psychic" -> PokemonTypeColors.Psychic
    "bug" -> PokemonTypeColors.Bug
    "rock" -> PokemonTypeColors.Rock
    "ghost" -> PokemonTypeColors.Ghost
    "dragon" -> PokemonTypeColors.Dragon
    "dark" -> PokemonTypeColors.Dark
    "steel" -> PokemonTypeColors.Steel
    "fairy" -> PokemonTypeColors.Fairy
    else -> PokemonTypeColors.Normal
}


/**
 * Extension function для получения цвета на основе значения статистики
 *
 * Использует градиентную шкалу из Design System
 */
fun Int.toStatColor(): Color = StatColors.getColorForStat(this)

/**
 * Получить более светлый оттенок цвета
 */
fun Color.lighter(factor: Float = 0.2f): Color {
    return Color(
        red = (red + (1 - red) * factor).coerceIn(0f, 1f),
        green = (green + (1 - green) * factor).coerceIn(0f, 1f),
        blue = (blue + (1 - blue) * factor).coerceIn(0f, 1f),
        alpha = alpha
    )
}

/**
 * Получить более темный оттенок цвета
 */
fun Color.darker(factor: Float = 0.2f): Color {
    return Color(
        red = (red * (1 - factor)).coerceIn(0f, 1f),
        green = (green * (1 - factor)).coerceIn(0f, 1f),
        blue = (blue * (1 - factor)).coerceIn(0f, 1f),
        alpha = alpha
    )
}