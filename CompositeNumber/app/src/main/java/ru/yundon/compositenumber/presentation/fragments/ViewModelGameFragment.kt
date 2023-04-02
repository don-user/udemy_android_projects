package ru.yundon.compositenumber.presentation.fragments

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.yundon.compositenumber.R
import ru.yundon.compositenumber.data.GameRepositoryImpl
import ru.yundon.compositenumber.domain.entity.GameResult
import ru.yundon.compositenumber.domain.entity.GameSettings
import ru.yundon.compositenumber.domain.entity.Level
import ru.yundon.compositenumber.domain.entity.Question
import ru.yundon.compositenumber.domain.usecases.GenerateQuestionUseCase
import ru.yundon.compositenumber.domain.usecases.GetGameSettingsUseCase

class ViewModelGameFragment(application: Application): AndroidViewModel(application) {

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val context = application
    private lateinit var level: Level

    private lateinit var gameSetting: GameSettings

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswer = MutableLiveData<Int>()
    val percentOfRightAnswer: LiveData<Int>
        get() = _percentOfRightAnswer

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private var countOfRightAnswers = 0
    private var countOfQuestion = 0

    fun startGame (level: Level){
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun updateProgress(){

        val percent = calculatePercentOfRightAnswer()
        _percentOfRightAnswer.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.answer_progress),
            countOfRightAnswers,
            gameSetting.minCountOfRightAnswers
        )

        _enoughCountOfRightAnswers.value = countOfRightAnswers >= gameSetting.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value = percent >= gameSetting.minPercentOfRightAnswers

    }

    private fun calculatePercentOfRightAnswer(): Int{
        if (countOfQuestion == 0){
            return 0
        }
        return ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()
    }

    fun chooseAnswer(number: Int){
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestion++

        updateProgress()
        generateQuestion()
    }

    private fun getGameSettings(level: Level){
        this.level = level
        this.gameSetting = getGameSettingsUseCase(level) // из-за invoke вызываем как метод
        _minPercent.value = gameSetting.minPercentOfRightAnswers
    }

    private fun startTimer(){
        timer = object : CountDownTimer(
            gameSetting.gameTimeSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(p0: Long) {
                _formattedTime.value = formattedTimer(p0)
            }
            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun generateQuestion(){
        _question.value = generateQuestionUseCase(gameSetting.maxSumValue)
    }

    private fun formattedTimer(millisUnitFinished: Long): String {

        val second = millisUnitFinished / MILLIS_IN_SECONDS
        val minute = second / SECOND_IN_MINUTES
        val leftSeconds = second - (minute * SECOND_IN_MINUTES)

        return String.format("%02d:%02d", minute, leftSeconds)

    }

    private fun finishGame(){

        _gameResult.value = GameResult(
            winner =
            enoughCountOfRightAnswers.value == true && enoughPercentOfRightAnswers.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestion,
            gameSettings = gameSetting
        )

    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object{
        const val MILLIS_IN_SECONDS = 1000L
        const val SECOND_IN_MINUTES = 60
    }
}