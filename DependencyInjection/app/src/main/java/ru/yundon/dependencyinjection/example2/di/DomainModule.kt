package ru.yundon.dependencyinjection.example2.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.yundon.dependencyinjection.example2.data.repository.ExampleRepositoryImpl
import ru.yundon.dependencyinjection.example2.domain.ExampleRepository

@Module
interface DomainModule {


    @Binds
    fun bindRepository(impl: ExampleRepositoryImpl) : ExampleRepository
}