package ru.yundon.dependencyinjection.example2.di

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import ru.yundon.dependencyinjection.example2.presentation.ExampleViewModel
import ru.yundon.dependencyinjection.example2.presentation.ExampleViewModel2


//интерфейс для работы с вью модель фактори создаем список МЭП с вью модель
@Module
interface ViewModelModule {


//    @StringKey("ExampleViewModel")  //указываем ключ для МЭП для этой вью модели
    @IntoMap  // говорим даггеру, что надо сложить вьюмодели в МЭП
    @ViewModelKey(ExampleViewModel::class)  // вместо StringKey, используем собственную аннотацию
    @Binds
    fun bindExampleViewModel(impl: ExampleViewModel) : ViewModel


//    @StringKey("ExampleViewModel2")
    @IntoMap
    @ViewModelKey(ExampleViewModel2::class)
    @Binds
    fun bindExampleViewModel2(impl: ExampleViewModel2) : ViewModel
}