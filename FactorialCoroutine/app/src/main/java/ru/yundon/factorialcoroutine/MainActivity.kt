package ru.yundon.factorialcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.yundon.factorialcoroutine.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()

        binding.button.setOnClickListener {
            viewModel.calculate(binding.editText.text.toString())
        }
    }

    private fun observeViewModel() = with(viewModel){

        state.observe(this@MainActivity){

            binding.progressBar.visibility = View.GONE
            binding.button.isEnabled = true

            when(it){
                is Error -> {
                    Toast.makeText(
                        this@MainActivity,
                        "You did not entered value",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Progress -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.button.isEnabled = false
                }
                is FactorialResult ->{
                    binding.textView.text = it.value
                }
            }
        }
    }
}