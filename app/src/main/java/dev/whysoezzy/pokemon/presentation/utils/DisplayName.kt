package dev.whysoezzy.pokemon.presentation.utils

import dev.whysoezzy.pokemon.domain.model.SortBy
import java.util.Locale

fun getSortByDisplayName(sortBy: SortBy): String {
    return when (sortBy) {
        SortBy.ID -> "ID"
        SortBy.NAME -> "Имя"
        SortBy.HP -> "HP"
        SortBy.ATTACK -> "Атака"
        SortBy.DEFENSE -> "Защита"
        SortBy.SPEED -> "Скорость"
        SortBy.TOTAL_STATS -> "Общая сила"
        SortBy.HEIGHT -> "Рост"
        SortBy.WEIGHT -> "Вес"
    }
}

fun getStatDisplayName(statName: String): String {
    return when (statName) {
        "hp" -> "HP (Здоровье)"
        "attack" -> "Атака"
        "defense" -> "Защита"
        "special-attack" -> "Спец. атака"
        "special-defense" -> "Спец. защита"
        "speed" -> "Скорость"
        else -> statName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}