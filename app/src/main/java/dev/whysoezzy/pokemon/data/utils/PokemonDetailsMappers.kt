package dev.whysoezzy.pokemon.data.utils

import dev.whysoezzy.domain.model.Pokemon
import dev.whysoezzy.domain.model.PokemonStat
import dev.whysoezzy.domain.model.PokemonType
import dev.whysoezzy.pokemon.data.remote.dto.PokemonDetailsDto

import timber.log.Timber

fun PokemonDetailsDto.toDomainModel(): Pokemon {
    Timber.d("Маппинг покемона $name: stats = ${stats.map { "${it.stat.name}: ${it.baseStat}" }}")

    val imageUrl = sprites.other?.officialArtwork?.frontDefault
        ?: sprites.frontDefault
        ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    return Pokemon(
        id = id,
        name = name,
        height = height,
        weight = weight,
        imageUrl = imageUrl,
        types = types.map { typeSlot ->
            PokemonType(
                name = typeSlot.type.name,
                slot = typeSlot.slot
            )
        },
        stats = stats.map { statDto ->
            PokemonStat(
                name = statDto.stat.name,
                baseStat = statDto.baseStat,
                effort = statDto.effort
            )
        }
    )
}