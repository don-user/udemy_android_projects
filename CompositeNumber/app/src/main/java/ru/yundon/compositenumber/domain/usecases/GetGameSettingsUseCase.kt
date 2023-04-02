package ru.yundon.compositenumber.domain.usecases

import ru.yundon.compositenumber.domain.entity.GameSettings
import ru.yundon.compositenumber.domain.entity.Level
import ru.yundon.compositenumber.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings{
        return repository.getGameSettings(level)
    }
}