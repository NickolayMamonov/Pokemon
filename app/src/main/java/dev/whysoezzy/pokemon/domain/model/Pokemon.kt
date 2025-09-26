package dev.whysoezzy.pokemon.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    val types: List<PokemonType>,
    val stats: List<PokemonStat>
) {
    val hp: Int get() = stats.find { it.name == "hp" }?.baseStat ?: 0
    val attack: Int get() = stats.find { it.name == "attack" }?.baseStat ?: 0
    val defense: Int get() = stats.find { it.name == "defense" }?.baseStat ?: 0
    val specialAttack: Int get() = stats.find { it.name == "special-attack" }?.baseStat ?: 0
    val specialDefense: Int get() = stats.find { it.name == "special-defense" }?.baseStat ?: 0
    val speed: Int get() = stats.find { it.name == "speed" }?.baseStat ?: 0

    val totalStats: Int get() = hp + attack + defense + specialAttack + specialDefense + speed
    val primaryType: String get() = types.firstOrNull()?.name ?: "unknown"
}

data class PokemonType(
    val name: String,
    val slot: Int
)

data class PokemonStat(
    val name: String,
    val baseStat: Int,
    val effort: Int
)

data class PokemonListItem(
    val id: String,
    val name: String,
    val url: String
) {
    val pokemonId: String
        get() = url.trimEnd('/').substringAfterLast('/')
}

enum class SortBy {
    ID,
    NAME,
    HP,
    ATTACK,
    DEFENSE,
    SPEED,
    TOTAL_STATS,
    HEIGHT,
    WEIGHT
}

data class PokemonFilter(
    val searchQuery: String = "",
    val selectedTypes: Set<String> = emptySet(),
    val sortBy: SortBy = SortBy.ID,
    val isAscending: Boolean = true,
    val minHp: Int? = null,
    val maxHp: Int? = null,
    val minAttack: Int? = null,
    val maxAttack: Int? = null
)

data class PaginatedData<T>(
    val items: List<T>,
    val hasNextPage: Boolean,
    val currentPage: Int,
    val totalCount: Int
)