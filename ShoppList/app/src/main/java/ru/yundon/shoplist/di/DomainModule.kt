package ru.yundon.shoplist.di

import dagger.Binds
import dagger.Module
import ru.yundon.shoplist.data.ShopListRepositoryImpl
import ru.yundon.shoplist.domain.ShopListRepository

@Module
interface DomainModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(repositoryImpl: ShopListRepositoryImpl) : ShopListRepository

}