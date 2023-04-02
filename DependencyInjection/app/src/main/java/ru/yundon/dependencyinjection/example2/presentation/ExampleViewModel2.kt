package ru.yundon.dependencyinjection.example2.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.yundon.dependencyinjection.example2.domain.ExampleUseCase
import javax.inject.Inject


class ExampleViewModel2 @Inject constructor(
    private val useCase: ExampleUseCase
) : ViewModel(){

    fun method() {

        useCase.invoke()
        Log.d("MyTag", "ExampleViewModel2, $this")
    }
}
