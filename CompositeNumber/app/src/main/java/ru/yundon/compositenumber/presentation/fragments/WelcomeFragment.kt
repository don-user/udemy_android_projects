package ru.yundon.compositenumber.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.yundon.compositenumber.Constants.EXCEPTION_MESSAGE_BINDING
import ru.yundon.compositenumber.R
import ru.yundon.compositenumber.databinding.FragmentWelcomeBinding

class WelcomeFragment: Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException (EXCEPTION_MESSAGE_BINDING)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonUnderstand.setOnClickListener {
            launchChooseLevelFragment()
        }

    }

    private fun launchChooseLevelFragment(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChooseLevelFragment.newInstance())
            .addToBackStack(ChooseLevelFragment.NAME)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}