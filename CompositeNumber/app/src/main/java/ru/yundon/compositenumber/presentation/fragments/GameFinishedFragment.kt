package ru.yundon.compositenumber.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import ru.yundon.compositenumber.Constants.EXCEPTION_MESSAGE_BINDING
import ru.yundon.compositenumber.R
import ru.yundon.compositenumber.databinding.FragmentGameFinishedBinding
import ru.yundon.compositenumber.domain.entity.GameResult
import java.lang.RuntimeException


class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException(EXCEPTION_MESSAGE_BINDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupClickListener()
        bindViews(gameResult)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun parseArgs(){

        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun bindViews(gameResult: GameResult) = with(binding){

        if (gameResult.winner) emojiResult.setImageResource(R.drawable.ic_smile)
        else emojiResult.setImageResource(R.drawable.ic_sad)

        tvRequiredAnswers.text = String.format(
            getString(R.string.required_answer),
            gameResult.gameSettings.minCountOfRightAnswers
        )
        tvScoreAnswers.text = String.format(
            getString(R.string.score_answers),
            gameResult.countOfRightAnswers
        )
        tvRequiredPercentage.text = String.format(
            getString(R.string.required_percentage),
            gameResult.gameSettings.minPercentOfRightAnswers
        )
        tvScorePercentage.text = String.format(
            getString(R.string.score_percentage),
            getPercentOfRightAnswer()
        )

    }

    private fun retryGame(){

        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME_GAME_FRAGMENT,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    private fun getPercentOfRightAnswer() = with(gameResult){
        if (countOfQuestions == 0) 0
        else ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()

    }

    private fun setupClickListener(){

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)  // viewLifecycleOwner для очищение коллбэка при закрытиии фрагмента

        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    companion object{

        private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(result: GameResult): GameFinishedFragment{
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, result)
                }
            }
        }
    }
}