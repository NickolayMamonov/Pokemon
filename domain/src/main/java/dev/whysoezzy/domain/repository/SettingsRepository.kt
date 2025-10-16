package dev.whysoezzy.domain.repository

import dev.whysoezzy.domain.model.AppTheme
import dev.whysoezzy.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>

    suspend fun saveTheme(theme: AppTheme)

    suspend fun clearCache(): Result<Unit>

    suspend fun getCacheSize(): Long
}