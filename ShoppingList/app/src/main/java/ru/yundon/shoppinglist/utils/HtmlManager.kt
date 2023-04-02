package ru.yundon.shoppinglist.utils

import android.text.Html
import android.text.Spanned

//Объект для сохранение HTML для сохранения в базу данных изменений формата и стилей текста
object HtmlManager {
    //преобразования текста в класс Spanned(в том формате в котором нам надо)
    fun getFromHtml(text: String): Spanned{

        //ПРоверяем версию и если версия ниже необходимой делаем старым кодом(деприкед)
        return if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N){
            Html.fromHtml(text)
        }else{
            //Новый код HTML
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    }

    //обратная функция перевода из Spanned в String
    fun toHtml(text: Spanned): String{
        //ПРоверяем версию и если версия ниже необходимой делаем старым кодом(деприкед)
        return if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N){
            Html.toHtml(text)
        }else{
            //Новый код HTML
            Html.toHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    }
}