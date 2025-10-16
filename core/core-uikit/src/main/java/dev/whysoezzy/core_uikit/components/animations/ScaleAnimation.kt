package dev.whysoezzy.core_uikit.components.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.core_uikit.theme.AnimationDuration
import dev.whysoezzy.core_uikit.theme.AnimationSpecs

@Composable
fun ScaleAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = 0.8f,
            animationSpec = AnimationSpecs.Bouncy
        ) + fadeIn(),
        exit = scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(AnimationDuration.Fast)
        ) + fadeOut(),
        modifier = modifier
    ) {
        content()
    }
}