package dev.whysoezzy.core_data.mappers

import dev.whysoezzy.core_database.entity.PokemonEntity
import dev.whysoezzy.core_database.entity.PokemonStatEntity
import dev.whysoezzy.core_database.entity.PokemonTypeEntity
import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType

fun PokemonEntity.toDomainModel(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        imageUrl = imageUrl,
        types = types.map { PokemonType(it.name, it.slot) },
        stats = stats.map { PokemonStat(it.name, it.baseStat, it.effort) },
    )
}

fun Pokemon.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        height = height,
        weight = weight,
        imageUrl = imageUrl,
        types = types.map { PokemonTypeEntity(it.name, it.slot) },
        stats = stats.map { PokemonStatEntity(it.name, it.baseStat, it.effort) },
    )
}
