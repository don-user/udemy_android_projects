package ru.yundon.dependencyinjection.example2.domain

import android.util.Log
import javax.inject.Inject

class ExampleUseCase @Inject constructor(
    private val repository: ExampleRepository
) {

    operator fun invoke() {
        repository.method()
    }
}
