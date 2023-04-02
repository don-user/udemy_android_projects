package ru.yundon.shoplist.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.yundon.shoplist.presentation.viewmodels.MainViewModel
import ru.yundon.shoplist.presentation.viewmodels.ShopItemViewModel

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(shopItemViewModel: ShopItemViewModel): ViewModel
}