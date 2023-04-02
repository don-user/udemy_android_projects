package ru.yundon.shoplist.presentation

import android.app.Application
import ru.yundon.shoplist.di.DaggerApplicationComponent

class ShopItemApp: Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}