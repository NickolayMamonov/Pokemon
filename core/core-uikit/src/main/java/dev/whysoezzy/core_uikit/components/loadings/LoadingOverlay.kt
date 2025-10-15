package dev.whysoezzy.core_uikit.components.loadings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.theme.PokemonBrandColors
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.spacing

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    message: String? = null,
    backgroundColor: Color = PokemonBrandColors.Black.copy(alpha = 0.5f)
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing().medium)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )

                if (message != null) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PokemonBrandColors.White,
                        textAlign = TextAlign.Center

                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingOverlayPreview() {
    PokemonTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            // Simulated content
            Text("Background Content")

            // Loading overlay
            LoadingOverlay(
                isLoading = true,
                message = "Загрузка покемонов..."
            )
        }
    }
}