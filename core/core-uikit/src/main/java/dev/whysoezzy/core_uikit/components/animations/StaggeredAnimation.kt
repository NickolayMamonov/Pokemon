package dev.whysoezzy.core_uikit.components.animations

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StaggeredAnimation(
    visible: Boolean,
    itemCount: Int,
    modifier: Modifier = Modifier,
    delayPerItem: Int = 50,
    content: @Composable (index: Int) -> Unit
) {
    Column(modifier = modifier) {
        repeat(itemCount) { index ->
            FadeInAnimation(
                visible = visible,
                delayMillis = index * delayPerItem
            ) {
                content(index)
            }
        }
    }
}