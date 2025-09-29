package dev.whysoezzy.core_common.extensions

import dev.whysoezzy.domain.model.SortBy
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

fun String.toSortByDisplayName(sortBy: SortBy): String {
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

fun String.toDisplayName(): String =
    this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

fun String.toStatDisplayName(): String =
    when (this) {
        "hp" -> "HP (Здоровье)"
        "attack" -> "Атака"
        "defense" -> "Защита"
        "special-attack" -> "Спец. атака"
        "special-defense" -> "Спец. защита"
        "speed" -> "Скорость"
        else ->
            this.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
    }
