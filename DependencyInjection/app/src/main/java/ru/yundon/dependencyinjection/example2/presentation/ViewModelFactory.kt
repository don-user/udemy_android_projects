package ru.yundon.dependencyinjection.example2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

//@ApplicationScope  // ApplicationScope для того чтоб не создавать новые фабрики
class ViewModelFactory @Inject constructor(
    //Provider оболочка над классом ViewModel
    private val viewModelsProvider: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>  // JvmSuppressWildcards аннтоцию пишем чтоб даггер вернул параметрореземую коллекцию
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        //универсальная модель, где даггер сам будет выбирать необходимую вью модель
        return viewModelsProvider[modelClass]?.get() as T  // get() - позвляет получать каждый раз свой viewModel

        //пример самостоятельного создание параметров вью модели
//        if (modelClass == ExampleViewModel::class.java) {
//            return ExampleViewModel(exampleUseCase) as T
//        }
//        throw RuntimeException("Unknown view model class $modelClass")
    }
}