package ru.yundon.shoplist.di

import android.app.Application
import dagger.Module
import dagger.Provides
import ru.yundon.shoplist.data.database.ShopListDao
import ru.yundon.shoplist.data.database.ShopListDatabase

@Module
class DataModule {

    @ApplicationScope
    @Provides
    fun provideShopListDao (application: Application): ShopListDao {

        return ShopListDatabase.getInstance(application).shopListDao()
    }
}