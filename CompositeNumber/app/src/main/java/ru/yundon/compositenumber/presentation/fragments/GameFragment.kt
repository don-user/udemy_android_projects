package ru.yundon.compositenumber.presentation.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.yundon.compositenumber.Constants.EXCEPTION_MESSAGE_BINDING
import ru.yundon.compositenumber.R
import ru.yundon.compositenumber.databinding.FragmentGameBinding
import ru.yundon.compositenumber.domain.entity.GameResult
import ru.yundon.compositenumber.domain.entity.GameSettings
import ru.yundon.compositenumber.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ) [ViewModelGameFragment::class.java]
    }

    private val tvOptions by lazy {

        mutableListOf<TextView>().apply {
           add(binding.tViewOption1)
           add(binding.tViewOption2)
           add(binding.tViewOption3)
           add(binding.tViewOption4)
           add(binding.tViewOption5)
           add(binding.tViewOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException(EXCEPTION_MESSAGE_BINDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parsArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startGame(level)
        observeViewModel()
        setOnClickListenersOnOption()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() = with(binding){

        viewModel.apply {
            formattedTime.observe(viewLifecycleOwner){
                tViewTimer.text = it
            }
            question.observe(viewLifecycleOwner){
                tViewSum.text = it.sum.toString()
                tViewLeftNumber.text = it.visibleNumber.toString()

                for (i in 0 until tvOptions.size){
                    tvOptions[i].text = it.options[i].toString()
                }
            }

            percentOfRightAnswer.observe(viewLifecycleOwner){
                progressBar.setProgress(it,true)  // если апи менее 24 удалить true
            }
            //устанавливаем цвет теста где показывает сколько верных ответов
            enoughCountOfRightAnswers.observe(viewLifecycleOwner){

                tViewAnswerProgress.setTextColor(getColorByState(it))
            }
            //устанавливаем цвет прогресс бару
            enoughPercentOfRightAnswers.observe(viewLifecycleOwner){
                val color = getColorByState(it)
                progressBar.progressTintList = ColorStateList.valueOf(color)
            }
            // присвоение в прогресс баре второй прогресс
            minPercent.observe(viewLifecycleOwner){
                progressBar.secondaryProgress = it
            }
            progressAnswers.observe(viewLifecycleOwner){
                tViewAnswerProgress.text = it
            }

            gameResult.observe(viewLifecycleOwner){
                launchGameFinishedFragment(it)
            }
        }
    }

    private fun getColorByState(goodState: Boolean): Int{
        val colorResId =
            if(goodState) android.R.color.holo_green_light
            else android.R.color.holo_red_light

        return ContextCompat.getColor(requireContext(), colorResId)
    }


    private fun setOnClickListenersOnOption(){
       for (item in tvOptions){
           item.setOnClickListener {
               viewModel.chooseAnswer(item.text.toString().toInt())
           }
       }

    }

    private fun launchGameFinishedFragment(result: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(result))
            .addToBackStack(null)
            .commit()
    }

    private fun parsArgs(){
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let{
            level= it
        }
    }

    companion object{

        const val KEY_LEVEL = "level"
        const val NAME_GAME_FRAGMENT = "GameFragment"
        fun newInstance(level: Level): GameFragment{
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}