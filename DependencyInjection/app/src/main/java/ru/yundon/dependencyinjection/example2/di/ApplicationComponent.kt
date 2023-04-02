package ru.yundon.dependencyinjection.example2.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, DomainModule::class])
interface ApplicationComponent {

    //возвращает активити компонент фактори
    fun activityComponentFactory(): ActivityComponent.Factory

//Использование билдера для передачи в граф дополнительных переменных (context, currentTime и т.п.)
//
    //Component.Builder
//    interface ApplicationComponentBuilder{
//
//        @BindsInstance
//        fun context(context: Context) : ApplicationComponentBuilder
//
//        @BindsInstance
//        fun currentTime(currentTime: Long) : ApplicationComponentBuilder
//
//        fun build() : ApplicationComponent
//    }


//Использование фактори для передачи в граф дополнительных переменных (context, currentTime и т.п.)
    @Component.Factory
    interface ApplicationComponentFactory{

        fun create(
            @BindsInstance context: Context,
            @BindsInstance currentTime: Long
        ) : ApplicationComponent
    }
}