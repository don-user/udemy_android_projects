package ru.yundon.dependencyinjection.example2.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.yundon.dependencyinjection.R
import javax.inject.Inject

class MainActivity2 : AppCompatActivity() {

    // пример Inject viewModel как обучный объект не правильный
//    @Inject
//    lateinit var viewModel: ExampleViewModel

    //привильный пример Inject viewModel через вью модель фабрику
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ExampleViewModel::class.java]
    }

    private val viewModel2 by lazy {
        ViewModelProvider(this, viewModelFactory)[ExampleViewModel2::class.java]
    }
    //реализация использования кастомного билдера
//    private val component by lazy {
//        val time = System.currentTimeMillis()
//
//        DaggerApplicationComponent.builder()
//            .context(this)
//            .currentTime(time)
//            .build()
//    }

    //для реализации без скоупа (Singlton)
//        private val component by lazy {
//            val time = System.currentTimeMillis()
//
//            DaggerApplicationComponent.factory()
//                .create(application, time)
//        }

    //для реалзации с Singlton через создания класса ExampleApplication
    private val component by lazy {
        (application as ExampleApplication).component
            .activityComponentFactory()
            .create("MY_ID_2", "MY_NAME_2")
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.method()
        viewModel2.method()

//     ИЗ ПРИМЕРА EXAMPLE 1
//        val activity = Activity()
//        activity.computer.toString()
//        activity.keyboard.toString()
//        activity.mouse.toString()
//        activity.monitor.toString()
    }
}