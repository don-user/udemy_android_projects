package ru.yundon.compositenumber.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.yundon.compositenumber.Constants.EXCEPTION_MESSAGE_BINDING
import ru.yundon.compositenumber.R
import ru.yundon.compositenumber.databinding.FragmentChooseLevelBinding
import ru.yundon.compositenumber.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw  RuntimeException (EXCEPTION_MESSAGE_BINDING)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chooseLevel()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun chooseLevel() = with(binding){

        buttonLevelTest.setOnClickListener {
            launchGameFragment(Level.TEST)
        }

        buttonLevelEasy.setOnClickListener {
            launchGameFragment(Level.EASY)
        }

        buttonLevelNormal.setOnClickListener {
            launchGameFragment(Level.NORMAL)
        }

        buttonLevelHard.setOnClickListener {
            launchGameFragment(Level.HARD)
        }
    }

    private fun launchGameFragment(level: Level){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFragment.newInstance(level))
            .addToBackStack(GameFragment.NAME_GAME_FRAGMENT)
            .commit()
    }

    companion object{

        const val NAME = "ChooseLevelFragment"
        fun newInstance(): ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }
}