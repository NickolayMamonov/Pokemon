package dev.whysoezzy.core_uikit.components.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.whysoezzy.core_uikit.R
import dev.whysoezzy.core_uikit.components.chips.TypeFilterChip
import dev.whysoezzy.core_uikit.components.dropdown.SortByDropdown
import dev.whysoezzy.core_uikit.theme.Elevation
import dev.whysoezzy.core_uikit.theme.PokemonElevation
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing
import dev.whysoezzy.domain.model.PokemonFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filter: PokemonFilter,
    availableTypes: Set<String>,
    onFilterChange: (PokemonFilter) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentFilter by remember { mutableStateOf(filter) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing().large),
    ) {
        // Заголовок
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(dimensions().spacerSmall))
                Text(
                    text = stringResource(R.string.filters_and_sort),
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            TextButton(onClick = {
                currentFilter = PokemonFilter()
            }) {
                Text(stringResource(R.string.reset))
            }
        }

        Spacer(modifier = Modifier.height(dimensions().spacerLarge))

        // Сортировка
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = PokemonElevation.PokemonCard),
        ) {
            Column(
                modifier = Modifier.padding(spacing().medium),
            ) {
                Text(
                    text = stringResource(R.string.sort),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(dimensions().spacerMediumSmall))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SortByDropdown(
                        currentSortBy = currentFilter.sortBy,
                        onSortByChange = { sortBy ->
                            currentFilter = currentFilter.copy(sortBy = sortBy)
                        },
                    )

                    FilterChip(
                        onClick = {
                            currentFilter =
                                currentFilter.copy(isAscending = !currentFilter.isAscending)
                        },
                        label = {
                            Text(
                                text =
                                    if (currentFilter.isAscending) stringResource(R.string.ascending_order)
                                    else stringResource(
                                        R.string.descending_order
                                    ),
                            )
                        },
                        selected = true,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensions().spacerMedium))

        // Фильтрация по типам
        if (availableTypes.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = Elevation.Level2),
            ) {
                Column(
                    modifier = Modifier.padding(spacing().medium),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.pokemon_types),
                            style = MaterialTheme.typography.titleMedium,
                        )

                        if (currentFilter.selectedTypes.isNotEmpty()) {
                            Text(
                                text = stringResource(
                                    R.string.selected_type_size,
                                    currentFilter.selectedTypes.size
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }

                    // Информация о логике фильтрации
                    if (currentFilter.selectedTypes.size > 1) {
                        Spacer(modifier = Modifier.height(dimensions().spacerSmall))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.3f,
                                ),
                            ),
                        ) {
                            Row(
                                modifier = Modifier.padding(spacing().mediumSmall),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(dimensions().iconSizeSmall),
                                )
                                Spacer(modifier = Modifier.width(dimensions().spacerSmall))
                                Text(
                                    text = stringResource(R.string.pokemon_with_all_the_selected_types_will_be_shown),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensions().spacerMediumSmall))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(spacing().small),
                    ) {
                        items(availableTypes.sorted()) { type ->
                            TypeFilterChip(
                                type = type,
                                isSelected = currentFilter.selectedTypes.contains(type),
                                onClick = {
                                    val newTypes = currentFilter.selectedTypes.toMutableSet()
                                    if (newTypes.contains(type)) {
                                        newTypes.remove(type)
                                    } else {
                                        newTypes.add(type)
                                    }
                                    currentFilter = currentFilter.copy(selectedTypes = newTypes)
                                },
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensions().spacerLarge))

        // Кнопки действий
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing().mediumSmall),
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.cancel))
            }

            Button(
                onClick = {
                    onFilterChange(currentFilter)
                    onDismiss()
                },
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.apply))
            }
        }
    }
}
