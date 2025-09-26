package dev.whysoezzy.pokemon.data.remote.dto

data class PokemonListResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResultDto>
)

data class PokemonResultDto(
    val name: String,
    val url: String
)
