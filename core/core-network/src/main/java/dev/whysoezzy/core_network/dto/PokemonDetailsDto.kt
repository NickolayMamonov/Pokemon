package dev.whysoezzy.core_network.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailsDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: SpritesDto,
    val types: List<TypeSlotDto>,
    val stats: List<StatDto>,
)

data class SpritesDto(
    @SerializedName("front_default")
    val frontDefault: String?,
    val other: OtherDto?,
)

data class OtherDto(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtworkDto?,
)

data class OfficialArtworkDto(
    @SerializedName("front_default")
    val frontDefault: String?,
)

data class TypeSlotDto(
    val slot: Int,
    val type: TypeDto,
)

data class TypeDto(
    val name: String,
    val url: String,
)

data class StatDto(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatInfoDto,
)

data class StatInfoDto(
    val name: String,
    val url: String,
)
