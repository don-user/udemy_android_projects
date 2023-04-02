package ru.yundon.shoppinglist.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {

    const val DEF_TIME_FORMAT = "hh:mm:ss - yyyy/MM/dd"
    //получаем время в читаемом формате
    fun getCurrentTime(): String{
        //устанавливаем формат даты и времени
        val formatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
//форматируют дату и время из строки в фрмат класса Date для дальнейшего преобразования в необходимый шаблон
    fun getTimeFormat(time: String, defPreference: SharedPreferences): String{
        val defFormatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val newFormat = defPreference.getString("time_format_key", DEF_TIME_FORMAT)
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
        return if (defDate != null){
            newFormatter.format(defDate)
        }else time
    }
}