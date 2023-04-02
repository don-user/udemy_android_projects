package ru.yundon.dependencyinjection.example2.data.database

import android.content.Context
import android.util.Log
import ru.yundon.dependencyinjection.R
import ru.yundon.dependencyinjection.example2.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Singleton


class ExampleDatabase @Inject constructor(
    private val context: Context,
    private val currentTime: Long
)
{

    fun method() {
    }
}
