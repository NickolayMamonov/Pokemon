package dev.whysoezzy.core_uikit.components.dropdown

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_common.extensions.getSortByDisplayName
import dev.whysoezzy.domain.model.SortBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortByDropdown(
    currentSortBy: SortBy,
    onSortByChange: (SortBy) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = getSortByDisplayName(currentSortBy),
            onValueChange = { },
            readOnly = true,
            label = { Text("Сортировать по") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier =
                Modifier
                    .width(180.dp)
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            textStyle = MaterialTheme.typography.bodyMedium,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            SortBy.entries.forEach { sortBy ->
                DropdownMenuItem(
                    text = { Text(getSortByDisplayName(sortBy)) },
                    onClick = {
                        onSortByChange(sortBy)
                        expanded = false
                    },
                )
            }
        }
    }
}
