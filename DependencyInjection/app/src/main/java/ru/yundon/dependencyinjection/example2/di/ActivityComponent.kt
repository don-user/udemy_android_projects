package ru.yundon.dependencyinjection.example2.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.yundon.dependencyinjection.example2.presentation.MainActivity
import ru.yundon.dependencyinjection.example2.presentation.MainActivity2

//подкомпонент апликэйшн компонента, для того чтоб передать в конструктор зависимость до создания активити
@Subcomponent(modules = [ViewModelModule::class] )
interface ActivityComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: MainActivity2)

    //создаем фабрику которую переопределит апликэйшн компанент, потому что мы не можем созадть фабрику для сабкомпоненты
    @Subcomponent.Factory
    interface Factory{
        fun create(
            @BindsInstance @IdQualifier id: String,
            @BindsInstance @NameQualifier name: String
        ) : ActivityComponent
    }
}