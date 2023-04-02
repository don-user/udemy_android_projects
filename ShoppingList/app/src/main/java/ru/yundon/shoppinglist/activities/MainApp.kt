package ru.yundon.shoppinglist.activities

import android.app.Application
import ru.yundon.shoppinglist.db.MainDataBase

//базовый класс
class MainApp: Application() {
    //глобальная переменная дата байз для работы с базой
    val dataBase by lazy { MainDataBase.getDataBase(this)}
}