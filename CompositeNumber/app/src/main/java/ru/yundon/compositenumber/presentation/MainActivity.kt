package ru.yundon.compositenumber.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.yundon.compositenumber.R
import ru.yundon.compositenumber.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}