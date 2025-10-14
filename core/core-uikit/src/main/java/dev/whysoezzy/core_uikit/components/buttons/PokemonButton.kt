package dev.whysoezzy.core_uikit.components.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.theme.AnimationSpecs
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

enum class PokemonButtonStyle {
    PRIMARY,
    SECONDARY,
    OUTLINED,
    TEXT
}

enum class PokemonButtonSize {
    SMALL,
    MEDIUM,
    LARGE
}

@Composable
fun PokemonButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    style: PokemonButtonStyle = PokemonButtonStyle.PRIMARY,
    size: PokemonButtonSize = PokemonButtonSize.MEDIUM,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = AnimationSpecs.Bouncy,
        label = "button_scale"
    )

    val buttonHeight = when (size) {
        PokemonButtonSize.SMALL -> dimensions().buttonHeightSmall
        PokemonButtonSize.MEDIUM -> dimensions().buttonHeightMedium
        PokemonButtonSize.LARGE -> dimensions().buttonHeightLarge
    }

    val horizontalPadding = when (size) {
        PokemonButtonSize.SMALL -> spacing().medium
        PokemonButtonSize.MEDIUM -> spacing().large
        PokemonButtonSize.LARGE -> spacing().extraLarge
    }

    val iconSize = when (size) {
        PokemonButtonSize.SMALL -> dimensions().iconSizeSmall
        PokemonButtonSize.MEDIUM -> dimensions().iconSizeMedium
        PokemonButtonSize.LARGE -> dimensions().iconSizeLarge
    }

    val textStyle = when (size) {
        PokemonButtonSize.SMALL -> MaterialTheme.typography.labelMedium
        PokemonButtonSize.MEDIUM -> MaterialTheme.typography.labelLarge
        PokemonButtonSize.LARGE -> MaterialTheme.typography.titleMedium
    }

    when (style) {
        PokemonButtonStyle.PRIMARY -> {
            Button(
                onClick = onClick,
                enabled = enabled && !loading,
                shape = CustomShapes.Button,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 6.dp,
                    disabledElevation = 0.dp
                ),
                modifier = modifier
                    .height(buttonHeight)
                    .scale(scale)
                    .animateContentSize()
            ) {
                PokemonButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    iconSize = iconSize
                )
            }

        }

        PokemonButtonStyle.SECONDARY -> {
            Button(
                onClick = onClick,
                enabled = enabled && !loading,
                shape = CustomShapes.Button,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                modifier = modifier
                    .height(buttonHeight)
                    .scale(scale)
                    .animateContentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 6.dp,
                    disabledElevation = 0.dp
                )
            ) {
                PokemonButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    iconSize = iconSize
                )
            }
        }

        PokemonButtonStyle.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled && !loading,
                shape = CustomShapes.Button,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = modifier
                    .height(buttonHeight)
                    .scale(scale)
                    .animateContentSize()
            ) {
                PokemonButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    iconSize = iconSize
                )
            }
        }

        PokemonButtonStyle.TEXT -> {
            TextButton(
                onClick = onClick,
                enabled = enabled && !loading,
                shape = CustomShapes.Button,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = modifier
                    .height(buttonHeight)
                    .scale(scale)
                    .animateContentSize()
            ) {
                PokemonButtonContent(
                    text = text,
                    loading = loading,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    textStyle = textStyle,
                    iconSize = iconSize
                )
            }
        }
    }
}

@Composable
private fun PokemonButtonContent(
    text: String,
    loading: Boolean,
    leadingIcon: ImageVector?,
    trailingIcon: ImageVector?,
    textStyle: TextStyle,
    iconSize: Dp
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(iconSize),
                strokeWidth = 2.dp,
                color = LocalContentColor.current
            )
            Spacer(modifier = Modifier.width(spacing().small))
        }

        if (leadingIcon != null && !loading) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.width(spacing().small))
        }

        Text(
            text = text,
            style = textStyle,
            fontWeight = FontWeight.Medium
        )

        if (trailingIcon != null && !loading) {
            Spacer(modifier = Modifier.width(spacing().small))

            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

@Preview(name = "Buttons - Light", showBackground = true)
@Composable
private fun PokemonButtonPreviewLight() {
    PokemonTheme(darkTheme = false) {
        PokemonButtonShowcase()
    }
}

@Preview(name = "Buttons - Dark", showBackground = true)
@Composable
private fun PokemonButtonPreviewDark() {
    PokemonTheme(darkTheme = true) {
        PokemonButtonShowcase()
    }
}

@Composable
private fun PokemonButtonShowcase() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing().medium),
            verticalArrangement = Arrangement.spacedBy(spacing().medium)
        ) {
            // Заголовок
            item {
                Text(
                    text = "Pokemon Buttons",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Spacer(modifier = Modifier.height(dimensions().spacerMedium))

            }
            item {
                Text("Primary Buttons", style = MaterialTheme.typography.titleMedium)
                // Primary buttons
                PokemonButton(
                    text = "Primary Button",
                    onClick = { },
                    style = PokemonButtonStyle.PRIMARY
                )

            }
            item {
                // Secondary buttons
                Text("Secondary Buttons", style = MaterialTheme.typography.titleMedium)
                PokemonButton(
                    text = "Secondary Button",
                    onClick = { },
                    style = PokemonButtonStyle.SECONDARY
                )
            }
            item {
                // Outlined buttons
                Text("Outlined Buttons", style = MaterialTheme.typography.titleMedium)
                PokemonButton(
                    text = "Outlined Button",
                    onClick = { },
                    style = PokemonButtonStyle.OUTLINED
                )
            }
            item {
                // Text buttons
                Text("Text Buttons", style = MaterialTheme.typography.titleMedium)
                PokemonButton(
                    text = "Text Button",
                    onClick = { },
                    style = PokemonButtonStyle.TEXT
                )
            }
            item {
                // With icons
                Text("With Icons", style = MaterialTheme.typography.titleMedium)
                PokemonButton(
                    text = "With Icon",
                    onClick = { },
                    leadingIcon = androidx.compose.material.icons.Icons.Default.Favorite
                )
            }
            item {
                // Loading state
                Text("Loading State", style = MaterialTheme.typography.titleMedium)
                PokemonButton(
                    text = "Loading",
                    onClick = { },
                    loading = true
                )
            }
            item {
                // Disabled
                Text("Disabled", style = MaterialTheme.typography.titleMedium)
                PokemonButton(
                    text = "Disabled",
                    onClick = { },
                    enabled = false
                )
            }
            item {
                // Sizes
                Text("Sizes", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(spacing().small)) {
                    PokemonButton(
                        text = "Small",
                        onClick = { },
                        size = PokemonButtonSize.SMALL
                    )
                    PokemonButton(
                        text = "Medium",
                        onClick = { },
                        size = PokemonButtonSize.MEDIUM
                    )
                    PokemonButton(
                        text = "Large",
                        onClick = { },
                        size = PokemonButtonSize.LARGE
                    )
                }
            }
        }
    }
}