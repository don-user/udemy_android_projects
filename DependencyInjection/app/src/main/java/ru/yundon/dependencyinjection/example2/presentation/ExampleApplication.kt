package ru.yundon.dependencyinjection.example2.presentation

import android.app.Application
import ru.yundon.dependencyinjection.example2.di.DaggerApplicationComponent

class ExampleApplication: Application() {

    val component by lazy {
        val time = System.currentTimeMillis()
        DaggerApplicationComponent.factory()
            .create(this, time)
    }

}