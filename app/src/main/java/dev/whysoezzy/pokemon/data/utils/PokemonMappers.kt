package dev.whysoezzy.pokemon.data.utils

import dev.whysoezzy.pokemon.data.local.entity.PokemonEntity
import dev.whysoezzy.pokemon.data.local.entity.PokemonStatEntity
import dev.whysoezzy.pokemon.data.local.entity.PokemonTypeEntity
import dev.whysoezzy.pokemon.domain.model.Pokemon
import dev.whysoezzy.pokemon.domain.model.PokemonStat
import dev.whysoezzy.pokemon.domain.model.PokemonType

fun PokemonEntity.toDomainModel(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        imageUrl = imageUrl,
        types = types.map { PokemonType(it.name, it.slot) },
        stats = stats.map { PokemonStat(it.name, it.baseStat, it.effort) }
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
        stats = stats.map { PokemonStatEntity(it.name, it.baseStat, it.effort) }
    )
}