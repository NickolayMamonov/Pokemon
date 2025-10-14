package dev.whysoezzy.core_uikit.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.whysoezzy.core_uikit.extensions.toStatColor
import dev.whysoezzy.core_uikit.theme.CustomShapes
import dev.whysoezzy.core_uikit.theme.PokemonBrandColors
import dev.whysoezzy.core_uikit.theme.PokemonTheme
import dev.whysoezzy.core_uikit.theme.dimensions
import dev.whysoezzy.core_uikit.theme.spacing
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    modifier: Modifier = Modifier,
    stats: Map<String, Int>,
    maxValue: Int = 255,
    color: Color? = null,
    animated: Boolean = true,
    showLabels: Boolean = true,
    showValues: Boolean = false
) {
    require(stats.isNotEmpty()) { "Stats map cannot be empty" }

    var animatedPlayed by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animatedPlayed && animated) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "radar_animation"
    )

    LaunchedEffect(Unit) {
        animatedPlayed = true
    }

    val normalizedStats = stats.mapValues { (it.value.toFloat() / maxValue).coerceIn(0f, 1f) }

    val chartColor = color ?: MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .padding(spacing().large),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
                val radius = (canvasWidth / 2f) * 0.8f
                val sides = stats.size

                drawRadarGrid(
                    center = center,
                    radius = radius,
                    sides = sides,
                    levels = 5
                )

                drawRadarData(
                    center = center,
                    radius = radius,
                    values = normalizedStats.values.toList(),
                    progress = if (animated) animatedProgress else 1f,
                    color = chartColor
                )

                drawRadarPoints(
                    center = center,
                    radius = radius,
                    values = normalizedStats.values.toList(),
                    progress = if (animated) animatedProgress else 1f,
                    color = chartColor
                )
            }

            if (showLabels) {
                RadarLabels(
                    stats = stats,
                    showValues = showValues
                )
            }
        }
    }
}

private fun DrawScope.drawRadarGrid(
    center: Offset,
    radius: Float,
    sides: Int,
    levels: Int
) {
    val angleStep = (2 * Math.PI / sides).toFloat()

    for (level in 1..levels) {
        val levelRadius = radius * (level.toFloat() / levels)
        val path = Path()

        for (i in 0 until sides) {
            val angle = angleStep * i - (Math.PI / 2).toFloat()
            val x = center.x + levelRadius * cos(angle)
            val y = center.y + levelRadius * sin(angle)

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()

        drawPath(
            path = path,
            color = Color.Gray.copy(alpha = 0.2f),
            style = Stroke(width = 1.dp.toPx())
        )
    }

    for (i in 0 until sides) {
        val angle = angleStep * i - (Math.PI / 2).toFloat()
        val endX = center.x + radius * cos(angle)
        val endY = center.y + radius * sin(angle)

        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = center,
            end = Offset(endX, endY),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun DrawScope.drawRadarData(
    center: Offset,
    radius: Float,
    values: List<Float>,
    progress: Float,
    color: Color
) {
    val sides = values.size
    val angleStep = (2 * Math.PI / sides).toFloat()
    val path = Path()

    values.forEachIndexed { index, value ->
        val angle = angleStep * index - (Math.PI / 2).toFloat()
        val distance = radius * value * progress
        val x = center.x + distance * cos(angle)
        val y = center.y + distance * sin(angle)
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()

    drawPath(
        path = path,
        color = color.copy(alpha = 0.3f),
    )

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = 2.dp.toPx())
    )
}

private fun DrawScope.drawRadarPoints(
    center: Offset,
    radius: Float,
    values: List<Float>,
    progress: Float,
    color: Color
) {
    val sides = values.size
    val angleStep = (2 * Math.PI / sides).toFloat()

    values.forEachIndexed { index, value ->
        val angle = angleStep * index - (Math.PI / 2).toFloat()
        val distance = radius * value * progress
        val x = center.x + distance * cos(angle)
        val y = center.y + distance * sin(angle)

        drawCircle(
            color = color,
            radius = 4.dp.toPx(),
            center = Offset(x, y)
        )

        drawCircle(
            color = PokemonBrandColors.White,
            radius = 2.dp.toPx(),
            center = Offset(x, y)
        )
    }
}


@Composable
private fun BoxScope.RadarLabels(
    stats: Map<String, Int>,
    showValues: Boolean,
) {
    val sides = stats.size
    val angleStep = (2 * Math.PI / sides).toFloat()

    stats.entries.forEachIndexed { index, (name, value) ->
        val angle = angleStep * index - (Math.PI / 2).toFloat()

        val labelDistance = 1.2f
        val xFraction = 0.5f + labelDistance * 0.4f * cos(angle)
        val yFraction = 0.5f + labelDistance * 0.4f * sin(angle)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
        ) {
            Column(
                modifier = Modifier
                    .align(
                        when {
                            xFraction < 0.4f && yFraction < 0.4f -> Alignment.TopStart
                            xFraction > 0.6f && yFraction < 0.4f -> Alignment.TopEnd
                            xFraction < 0.4f && yFraction > 0.6f -> Alignment.BottomStart
                            xFraction > 0.6f && yFraction > 0.6f -> Alignment.BottomEnd
                            yFraction < 0.4f -> Alignment.TopCenter
                            yFraction > 0.6f -> Alignment.BottomCenter
                            xFraction < 0.4f -> Alignment.CenterStart
                            else -> Alignment.CenterEnd
                        }
                    )
                    .offset(
                        x = (280.dp * (xFraction - 0.5f)),
                        y = (280.dp * (yFraction - 0.5f))
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (showValues) {
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = value.toStatColor()
                    )
                }
            }
        }
    }
}

@Composable
fun RadarChartCard(
    stats: Map<String, Int>,
    modifier: Modifier = Modifier,
    title: String = "Статистика",
    color: Color? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = CustomShapes.PokemonCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing().medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(dimensions().spacerMedium))

            RadarChart(
                stats = stats,
                color = color,
                showLabels = true,
                showValues = true
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun RadarChartPreview() {
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
                    text = "Radar Chart",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                RadarChartCard(
                    stats = mapOf(
                        "HP" to 80,
                        "Attack" to 120,
                        "Defense" to 60,
                        "Sp. Atk" to 150,
                        "Sp. Def" to 90,
                        "Speed" to 100
                    )
                )
            }
        }
    }
}