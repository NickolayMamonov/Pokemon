package dev.whysoezzy.core_uikit.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import dev.whysoezzy.core_uikit.theme.AnimationSpecs
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.spacing

enum class PokemonIconButtonStyle {
    FILLED,
    TONAL,
    OUTLINED,
    STANDARD,
}

@Composable
fun PokemonIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    style: PokemonIconButtonStyle = PokemonIconButtonStyle.STANDARD,
    enabled: Boolean = true,
    selected: Boolean = false,
    tint: Color? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = AnimationSpecs.Bouncy,
        label = "icon_button_scale"
    )

    val iconColor by animateColorAsState(
        targetValue = tint ?: if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            LocalContentColor.current
        },
        label = "icon_color"
    )

    when (style) {
        PokemonIconButtonStyle.FILLED -> {
            FilledIconButton(
                onClick = onClick,
                enabled = enabled,
                interactionSource = interactionSource,
                modifier = modifier.scale(scale)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor
                )
            }
        }

        PokemonIconButtonStyle.TONAL -> {
            FilledTonalIconButton(
                onClick = onClick,
                enabled = enabled,
                interactionSource = interactionSource,
                modifier = modifier.scale(scale)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor
                )
            }
        }

        PokemonIconButtonStyle.OUTLINED -> {
            OutlinedIconButton(
                onClick = onClick,
                enabled = enabled,
                interactionSource = interactionSource,
                modifier = modifier.scale(scale)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor
                )
            }
        }

        PokemonIconButtonStyle.STANDARD -> {
            IconButton(
                onClick = onClick,
                enabled = enabled,
                interactionSource = interactionSource,
                modifier = modifier.scale(scale)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = iconColor
                )
            }
        }
    }
}

@Preview(name = "Icon Buttons", showBackground = true)
@Composable
private fun PokemonIconButtonPreview() {
    PokemonTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing().medium),
                verticalArrangement = Arrangement.spacedBy(spacing().medium)
            ) {
                Text("Icon Buttons", style = MaterialTheme.typography.headlineMedium)

                Row(horizontalArrangement = Arrangement.spacedBy(spacing().medium)) {
                    PokemonIconButton(
                        icon = androidx.compose.material.icons.Icons.Default.Favorite,
                        onClick = { },
                        style = PokemonIconButtonStyle.FILLED
                    )

                    PokemonIconButton(
                        icon = androidx.compose.material.icons.Icons.Default.Share,
                        onClick = { },
                        style = PokemonIconButtonStyle.TONAL
                    )

                    PokemonIconButton(
                        icon = androidx.compose.material.icons.Icons.Default.Settings,
                        onClick = { },
                        style = PokemonIconButtonStyle.OUTLINED
                    )

                    PokemonIconButton(
                        icon = androidx.compose.material.icons.Icons.Default.MoreVert,
                        onClick = { },
                        style = PokemonIconButtonStyle.STANDARD
                    )
                }
            }
        }
    }
}