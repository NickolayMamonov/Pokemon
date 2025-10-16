package dev.whysoezzy.core_uikit.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.extensions.toStatColor
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing

enum class StatBarStyle {
    DEFAULT,
    GRADIENT,
    COMPACT
}

@Composable
fun StatBar(
    modifier: Modifier = Modifier,
    statName: String,
    statValue: Int,
    maxValue: Int = 255,
    style: StatBarStyle = StatBarStyle.DEFAULT,
    animated: Boolean = true,
    showValue: Boolean = true
) {
    val percentage = (statValue.toFloat() / maxValue).coerceIn(0f, 1f)

    var animationPlayed by remember { mutableStateOf(false) }

    val animatedPercentage by animateFloatAsState(
        targetValue = if (animationPlayed && animated) percentage else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 100
        ),
        label = "stat_bar_animation"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val statColor = statValue.toStatColor()

    when (style) {
        StatBarStyle.DEFAULT -> {
            DefaultStatBar(
                statName = statName,
                statValue = statValue,
                percentage = if(animated) animatedPercentage else percentage,
                color = statColor,
                showValue = showValue,
                modifier = modifier
            )
        }

        StatBarStyle.GRADIENT -> {
            GradientStatBar(
                statName = statName,
                statValue = statValue,
                percentage = if(animated) animatedPercentage else percentage,
                color = statColor,
                showValue = showValue,
                modifier = modifier
            )
        }
        StatBarStyle.COMPACT -> {
            CompactStatBar(
                percentage = if(animated) animatedPercentage else percentage,
                color = statColor,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DefaultStatBar(
    statName: String,
    statValue: Int,
    percentage: Float,
    color: Color,
    showValue: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing().medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(80.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(dimensions().progressBarHeight)
                .clip(CustomShapes.ProgressBar)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .fillMaxHeight()
                    .background(color)
            )
        }

        if (showValue) {
            Text(
                text = statValue.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
                color = color
            )
        }
    }
}

@Composable
private fun GradientStatBar(
    statName: String,
    statValue: Int,
    percentage: Float,
    color: Color,
    showValue: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing().medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(80.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(dimensions().progressBarHeightLarge)
                .clip(RoundedCornerShape(dimensions().progressBarCornerRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                color.copy(alpha = 0.6f),
                                color
                            )
                        )
                    )
            )
        }
        if (showValue) {
            Text(
                text = statValue.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
                color = color
            )
        }
    }
}

@Composable
private fun CompactStatBar(
    percentage: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(percentage)
                .fillMaxHeight()
                .background(color)
        )
    }
}

@Composable
fun StatSummaryCard(
    stats: Map<String,Int>,
    modifier: Modifier = Modifier,
    style: StatBarStyle = StatBarStyle.DEFAULT
){
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CustomShapes.PokemonCard
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing().medium),
            verticalArrangement = Arrangement.spacedBy(spacing().medium)
        ) {
            Text(
                text = "Характеристики",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensions().spacerSmall))

            stats.forEach { (name,value) ->
                StatBar(
                    statName = name,
                    statValue = value,
                    style = style
                )

                val total = stats.values.sum()

                Spacer(modifier = Modifier.height(dimensions().spacerSmall))

                HorizontalDivider(
                    thickness = dimensions().dividerThickness,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(modifier = Modifier.height(dimensions().spacerSmall))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Всего",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = total.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(name = "Stat Bars", showBackground = true)
@Composable
private fun StatBarPreview(){
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
                Text(
                    text = "Stat Bars"
                )

                StatBar(statName = "HP", statValue = 80)
                StatBar(statName = "Attack", statValue = 120)
                StatBar(statName = "Defense", statValue = 60)

                Spacer(modifier = Modifier.height(dimensions().spacerMedium))

                StatBar(statName = "Speed", statValue = 100, style = StatBarStyle.COMPACT)

                Spacer(modifier = Modifier.height(dimensions().spacerMedium))

                StatSummaryCard(
                    stats = mapOf(
                        "HP" to 80,
                        "Attack" to 120,
                        "Defense" to 60,
                        "Sp. Atk" to 150,
                        "Sp. Def" to 90,
                        "Speed" to 100
                    ),
                    style = StatBarStyle.GRADIENT

                )
            }

        }
    }

}