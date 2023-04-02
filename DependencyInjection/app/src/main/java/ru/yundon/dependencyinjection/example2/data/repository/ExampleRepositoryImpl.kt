package ru.yundon.dependencyinjection.example2.data.repository

import ru.yundon.dependencyinjection.example2.data.datasource.ExampleLocalDataSource
import ru.yundon.dependencyinjection.example2.data.datasource.ExampleRemoteDataSource
import ru.yundon.dependencyinjection.example2.data.mapper.ExampleMapper
import ru.yundon.dependencyinjection.example2.di.ProdQualifier
import ru.yundon.dependencyinjection.example2.di.TestQualifier
import ru.yundon.dependencyinjection.example2.domain.ExampleRepository
import javax.inject.Inject


class ExampleRepositoryImpl @Inject constructor(
    private val localDataSource: ExampleLocalDataSource,
    @ProdQualifier private val remoteDataSource: ExampleRemoteDataSource,
    private val mapper: ExampleMapper
) : ExampleRepository {

    override fun method() {

        mapper.map()
        localDataSource.method()
        remoteDataSource.method()
    }
}
