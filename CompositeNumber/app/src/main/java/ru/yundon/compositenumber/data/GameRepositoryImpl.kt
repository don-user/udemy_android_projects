package ru.yundon.compositenumber.data

import ru.yundon.compositenumber.domain.entity.GameSettings
import ru.yundon.compositenumber.domain.entity.Level
import ru.yundon.compositenumber.domain.entity.Question
import ru.yundon.compositenumber.domain.repository.GameRepository
import java.lang.Integer.min
import kotlin.math.max
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOption: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue +1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOption, MIN_ANSWER_VALUE)
        val to = min(maxSumValue, rightAnswer + countOfOption)

        while (options.size < countOfOption) {
            options.add(Random.nextInt(from, to))
        }

        return Question(sum,visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.TEST -> GameSettings(
                10,
                3,
                50,
                8
            )
            Level.EASY -> GameSettings(
                10,
                10,
                70,
                60
            )
            Level.NORMAL -> GameSettings(
                20,
                20,
                80,
                40
            )
            Level.HARD -> GameSettings(
                30,
                30,
                90,
                40
            )
        }
    }
}