package ru.yundon.dependencyinjection.example2.data.datasource

import ru.yundon.dependencyinjection.example2.data.database.ExampleDatabase
import javax.inject.Inject

class ExampleLocalDataSourceImpl @Inject constructor(
    private val database: ExampleDatabase
) : ExampleLocalDataSource {

    override fun method() {
        database.method()
    }
}
