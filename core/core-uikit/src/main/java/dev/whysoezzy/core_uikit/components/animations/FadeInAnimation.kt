package dev.whysoezzy.core_uikit.components.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.core_uikit.theme.AnimationDuration

@Composable
fun FadeInAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    durationMillis: Int = AnimationDuration.Medium,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis
            )
        ) + expandVertically(),
        exit = fadeOut(
            animationSpec = tween(durationMillis = AnimationDuration.Fast)
        ) + shrinkVertically(),
        modifier = modifier
    ) {
        content()
    }
}