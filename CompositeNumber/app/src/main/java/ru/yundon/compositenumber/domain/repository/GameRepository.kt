package ru.yundon.compositenumber.domain.repository

import ru.yundon.compositenumber.domain.entity.GameSettings
import ru.yundon.compositenumber.domain.entity.Level
import ru.yundon.compositenumber.domain.entity.Question

interface GameRepository {

    fun generateQuestion(maxSumValue: Int, countOfOption: Int): Question

    fun getGameSettings(level: Level): GameSettings
}