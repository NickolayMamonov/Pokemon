package dev.whysoezzy.core_uikit.components.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.core_uikit.theme.AnimationDuration

@Composable
fun SlideInAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    direction: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Start,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { if (direction == AnimatedContentTransitionScope.SlideDirection.Start) -it else it },
            animationSpec = tween(
                durationMillis = AnimationDuration.Medium,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(),
        exit = slideOutHorizontally(
            targetOffsetX = { if (direction == AnimatedContentTransitionScope.SlideDirection.Start) -it else it },
            animationSpec = tween(
                AnimationDuration.Fast,
                easing = FastOutLinearInEasing
            )
        ) + fadeOut(),
        modifier = modifier
    ) {
        content()
    }
}