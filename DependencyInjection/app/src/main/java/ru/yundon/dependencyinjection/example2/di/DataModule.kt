package ru.yundon.dependencyinjection.example2.di

import dagger.Binds
import dagger.Module
import ru.yundon.dependencyinjection.example2.data.datasource.*

@Module
interface DataModule {

    @ProdQualifier
    @ApplicationScope
    @Binds
    fun bindExampleRemoteDataSource(impl: ExampleRemoteDataSourceImpl): ExampleRemoteDataSource


    @ApplicationScope
    @Binds
    fun bindExampleLocalDataSource(impl: ExampleLocalDataSourceImpl): ExampleLocalDataSource

    @TestQualifier
    @ApplicationScope
    @Binds
    fun bindTestRemoteDataSource(impl: TestRemoteDataSourceImpl): ExampleRemoteDataSource
}