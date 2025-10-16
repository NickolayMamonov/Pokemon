package dev.whysoezzy.core_uikit.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * Animation Durations
 * Длительности анимаций согласно Material Design
 */
object AnimationDuration {
    const val Instant = 0          // Мгновенно
    const val ExtraFast = 100      // Очень быстро
    const val Fast = 200           // Быстро
    const val Medium = 300         // Средне (по умолчанию)
    const val Slow = 400           // Медленно
    const val ExtraSlow = 500      // Очень медленно
}

/**
 * Animation Specs
 * Предопределенные спецификации анимаций
 */
object AnimationSpecs {
    /**
     * Стандартная анимация для большинства переходов
     */
    val Default = tween<Float>(
        durationMillis = AnimationDuration.Medium
    )

    /**
     * Быстрая анимация для мелких элементов
     */
    val Fast = tween<Float>(
        durationMillis = AnimationDuration.Fast
    )

    /**
     * Медленная анимация для больших переходов
     */
    val Slow = tween<Float>(
        durationMillis = AnimationDuration.Slow
    )

    /**
     * Bouncy анимация для интерактивных элементов
     */
    val Bouncy = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    /**
     * Smooth spring для плавных переходов
     */
    val Smooth = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )

    /**
     * Stiff spring для быстрых ответов
     */
    val Stiff = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh
    )
}

/**
 * Pokemon-specific Animations
 * Анимации для Pokemon элементов
 */
object PokemonAnimations {
    /**
     * Анимация для карточек покемонов при нажатии
     */
    val CardPress = AnimationSpecs.Bouncy

    /**
     * Анимация появления элементов
     */
    val FadeIn = tween<Float>(
        durationMillis = AnimationDuration.Medium
    )

    /**
     * Анимация исчезновения элементов
     */
    val FadeOut = tween<Float>(
        durationMillis = AnimationDuration.Fast
    )

    /**
     * Анимация для shimmer эффекта
     */
    val Shimmer = tween<Float>(
        durationMillis = 1200
    )
}