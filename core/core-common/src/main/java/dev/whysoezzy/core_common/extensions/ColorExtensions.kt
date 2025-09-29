package dev.whysoezzy.core_common.extensions

import androidx.compose.ui.graphics.Color


fun String.toTypeColor(): Color = when (this.lowercase()) {
    "normal" -> Color(0xFFA8A878)
    "fire" -> Color(0xFFF08030)
    "water" -> Color(0xFF6890F0)
    "electric" -> Color(0xFFF8D030)
    "grass" -> Color(0xFF78C850)
    "ice" -> Color(0xFF98D8D8)
    "fighting" -> Color(0xFFC03028)
    "poison" -> Color(0xFFA040A0)
    "ground" -> Color(0xFFE0C068)
    "flying" -> Color(0xFFA890F0)
    "psychic" -> Color(0xFFF85888)
    "bug" -> Color(0xFFA8B820)
    "rock" -> Color(0xFFB8A038)
    "ghost" -> Color(0xFF705898)
    "dragon" -> Color(0xFF7038F8)
    "dark" -> Color(0xFF705848)
    "steel" -> Color(0xFFB8B8D0)
    "fairy" -> Color(0xFFEE99AC)
    else -> Color(0xFFA8A878)

}

fun Int.toStatColor(): Color = when {
    this >= 120 -> Color(0xFF4CAF50) // Зеленый для высоких значений
    this >= 80 -> Color(0xFFFF9800)  // Оранжевый для средних значений
    this >= 50 -> Color(0xFFFFC107)  // Желтый для низких значений
    else -> Color(0xFFF44336)             // Красный для очень низких значений
}
