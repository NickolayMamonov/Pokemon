package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.model.Settings
import dev.whysoezzy.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings> = settingsRepository.getSettings()
}