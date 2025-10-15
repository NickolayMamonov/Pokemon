package dev.whysoezzy.core_uikit.components.animations

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun ShakeAnimation(
    modifier: Modifier = Modifier,
    shake: Boolean,
    onShakeComplete: () -> Unit = {},
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    LaunchedEffect(shake) {
        if (shake) {
            val shakeSequence = listOf(0f, -10f, 10f, -10f, 10f, -5f, 5f, 0f)
            shakeSequence.forEach { offset ->
                offsetX = offset
                kotlinx.coroutines.delay(50)
            }
            onShakeComplete()
        }
    }
    Box(
        modifier = modifier.graphicsLayer {
            translationX = offsetX
        }
    ) {
        content()
    }
}