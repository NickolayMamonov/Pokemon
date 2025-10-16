package dev.whysoezzy.domain.usecase

import dev.whysoezzy.domain.repository.SettingsRepository

class ClearCacheUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Unit> = settingsRepository.clearCache()
}