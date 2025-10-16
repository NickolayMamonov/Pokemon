package dev.whysoezzy.domain.model

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}

data class Settings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val cacheSize: Long = 0L
)