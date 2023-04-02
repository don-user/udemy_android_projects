package ru.yundon.compositenumber.domain.usecases

import ru.yundon.compositenumber.domain.entity.Question
import ru.yundon.compositenumber.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question{
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object{

        const val COUNT_OF_OPTIONS = 6
    }
}