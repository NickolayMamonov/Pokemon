package dev.whysoezzy.pokemon.presentation.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.whysoezzy.domain.model.PokemonFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filter: PokemonFilter,
    availableTypes: Set<String>,
    onFilterChange: (PokemonFilter) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentFilter by remember { mutableStateOf(filter) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Заголовок
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Фильтры и сортировка",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = {
                currentFilter = PokemonFilter()
            }) {
                Text("Сбросить")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Сортировка
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Сортировка",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortByDropdown(
                        currentSortBy = currentFilter.sortBy,
                        onSortByChange = { sortBy ->
                            currentFilter = currentFilter.copy(sortBy = sortBy)
                        }
                    )

                    FilterChip(
                        onClick = {
                            currentFilter =
                                currentFilter.copy(isAscending = !currentFilter.isAscending)
                        },
                        label = {
                            Text(
                                text = if (currentFilter.isAscending) "По возрастанию ↑" else "По убыванию ↓"
                            )
                        },
                        selected = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Фильтрация по типам
        if (availableTypes.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Типы покемонов",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        if (currentFilter.selectedTypes.isNotEmpty()) {
                            Text(
                                text = "${currentFilter.selectedTypes.size} выбрано",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Информация о логике фильтрации
                    if (currentFilter.selectedTypes.size > 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.3f
                                )
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Будут показаны покемоны, имеющие ВСЕ выбранные типы",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопки действий
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Отмена")
            }

            Button(
                onClick = {
                    onFilterChange(currentFilter)
                    onDismiss()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Применить")
            }
        }
    }
}