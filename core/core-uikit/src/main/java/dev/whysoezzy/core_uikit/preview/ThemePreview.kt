package dev.whysoezzy.core_uikit.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.theme.Elevation
import dev.whysoezzy.core_uikit.theme.PokemonShapes
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.PokemonTypeColors
import dev.whysoezzy.core_uikit.theme.spacing

/**
 * Preview экран для тестирования Design System
 *
 * Показывает:
 * - Все цвета темы
 * - Типографику
 * - Spacing
 * - Elevations
 * - Shapes
 */
@Preview(name = "Light Theme", showBackground = true)
@Composable
private fun DesignSystemPreviewLight() {
    PokemonTheme(darkTheme = false, dynamicColor = false) {
        DesignSystemContent()
    }
}

@Preview(name = "Dark Theme", showBackground = true)
@Composable
private fun DesignSystemPreviewDark() {
    PokemonTheme(darkTheme = true, dynamicColor = false) {
        DesignSystemContent()
    }
}

@Composable
private fun DesignSystemContent() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(spacing().medium),
        verticalArrangement = Arrangement.spacedBy(spacing().large)
    ) {
        // Header
        item {
            Text(
                text = "Pokemon Design System",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Colors Section
        item {
            DesignSystemSection(title = "Colors") {
                ColorPalette()
            }
        }

        // Typography Section
        item {
            DesignSystemSection(title = "Typography") {
                TypographyShowcase()
            }
        }

        // Spacing Section
        item {
            DesignSystemSection(title = "Spacing") {
                SpacingShowcase()
            }
        }

        // Elevation Section
        item {
            DesignSystemSection(title = "Elevation") {
                ElevationShowcase()
            }
        }

        // Pokemon Types
        item {
            DesignSystemSection(title = "Pokemon Types") {
                PokemonTypesShowcase()
            }
        }
    }
}

@Composable
private fun DesignSystemSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(spacing().medium))
        content()
    }
}

@Composable
private fun ColorPalette() {
    Column(verticalArrangement = Arrangement.spacedBy(spacing().small)) {
        ColorRow("Primary", MaterialTheme.colorScheme.primary)
        ColorRow("Secondary", MaterialTheme.colorScheme.secondary)
        ColorRow("Tertiary", MaterialTheme.colorScheme.tertiary)
        ColorRow("Error", MaterialTheme.colorScheme.error)
        ColorRow("Background", MaterialTheme.colorScheme.background)
        ColorRow("Surface", MaterialTheme.colorScheme.surface)
    }
}

@Composable
private fun ColorRow(name: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun TypographyShowcase() {
    Column(verticalArrangement = Arrangement.spacedBy(spacing().small)) {
        Text("Display Large", style = MaterialTheme.typography.displayLarge)
        Text("Headline Large", style = MaterialTheme.typography.headlineLarge)
        Text("Title Large", style = MaterialTheme.typography.titleLarge)
        Text("Body Large", style = MaterialTheme.typography.bodyLarge)
        Text("Label Large", style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun SpacingShowcase() {
    Column(verticalArrangement = Arrangement.spacedBy(spacing().small)) {
        SpacingRow("Extra Small", spacing().extraSmall)
        SpacingRow("Small", spacing().small)
        SpacingRow("Medium", spacing().medium)
        SpacingRow("Large", spacing().large)
        SpacingRow("Extra Large", spacing().extraLarge)
    }
}

@Composable
private fun SpacingRow(name: String, spacing: androidx.compose.ui.unit.Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$name ($spacing)",
            modifier = Modifier.width(150.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .width(spacing)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun ElevationShowcase() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ElevationCard("0dp", Elevation.Level0)
        ElevationCard("3dp", Elevation.Level2)
        ElevationCard("8dp", Elevation.Level4)
    }
}

@Composable
private fun ElevationCard(label: String, elevation: androidx.compose.ui.unit.Dp) {
    Card(
        modifier = Modifier.size(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun PokemonTypesShowcase() {
    Column(verticalArrangement = Arrangement.spacedBy(spacing().small)) {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing().small)) {
            TypeBadge("Fire", PokemonTypeColors.Fire)
            TypeBadge("Water", PokemonTypeColors.Water)
            TypeBadge("Grass", PokemonTypeColors.Grass)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing().small)) {
            TypeBadge("Electric", PokemonTypeColors.Electric)
            TypeBadge("Psychic", PokemonTypeColors.Psychic)
            TypeBadge("Dragon", PokemonTypeColors.Dragon)
        }
    }
}

@Composable
private fun TypeBadge(name: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = PokemonShapes.small,
        modifier = Modifier.padding(spacing().extraSmall)
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}