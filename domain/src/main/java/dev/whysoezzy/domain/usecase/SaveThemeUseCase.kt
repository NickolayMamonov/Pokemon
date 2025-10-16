package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.model.AppTheme
import dev.whysoezzy.domain.repository.SettingsRepository

class SaveThemeUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(theme: AppTheme) {
        settingsRepository.saveTheme(theme)
    }
}