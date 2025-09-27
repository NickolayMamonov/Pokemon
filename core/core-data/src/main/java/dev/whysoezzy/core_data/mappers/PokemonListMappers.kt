package dev.whysoezzy.core_data.mappers

import dev.whysoezzy.core_database.entity.PokemonListItemEntity
import dev.whysoezzy.core_network.dto.PokemonListResponseDto
import dev.whysoezzy.core_network.dto.PokemonResultDto
import dev.whysoezzy.domain.model.PaginatedData
import dev.whysoezzy.domain.model.PokemonListItem

fun PokemonListResponseDto.toDomainModel(
    currentOffset: Int,
    limit: Int
): PaginatedData<PokemonListItem> {
    val items = results.map { it.toDomainModel() }
    val hasNextPage = next != null
    val currentPage = (currentOffset / limit) + 1

    return PaginatedData(
        items = items,
        hasNextPage = hasNextPage,
        currentPage = currentPage,
        totalCount = count
    )
}

fun PokemonResultDto.toDomainModel(): PokemonListItem {
    return PokemonListItem(
        id = url.trimEnd('/').substringAfterLast('/'),
        name = name,
        url = url
    )
}

fun PokemonListItemEntity.toDomainModel(): PokemonListItem {
    return PokemonListItem(
        id = id,
        name = name,
        url = url
    )
}

fun PokemonListItem.toEntity(): PokemonListItemEntity {
    return PokemonListItemEntity(
        id = id,
        name = name,
        url = url
    )
}