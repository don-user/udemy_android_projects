package ru.yundon.dependencyinjection.example2.di

import javax.inject.Qualifier

//создается вместо аннтоции @Named для отметки id
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IdQualifier
