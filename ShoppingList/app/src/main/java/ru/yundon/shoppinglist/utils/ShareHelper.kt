package ru.yundon.shoppinglist.utils

import android.content.Intent
import ru.yundon.shoppinglist.entities.ShoppingListItem

//класс для создание текста списка покупок и отправка текста по мсм почте  и т.п

object ShareHelper {

    //функция для отправки списка через интент
    fun shareShopList(shopList: List<ShoppingListItem>, listName: String): Intent{
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList, listName))
        }
        return intent
    }
//Шаблон для теста списка покупок
    private fun makeShareText(shopList: List<ShoppingListItem>, listName: String): String{
        val sBuilder = StringBuilder()
        sBuilder.append("<<$listName>>")
        sBuilder.append("\n")
        var cnt = 0
        shopList.forEach {
            sBuilder.append("${++cnt} - ${it.name} (${it.itemInfo ?: ""})")
            sBuilder.append("\n")
        }
        return sBuilder.toString()
    }
}