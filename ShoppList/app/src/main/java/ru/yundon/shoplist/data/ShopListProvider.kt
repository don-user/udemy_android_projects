package ru.yundon.shoplist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import ru.yundon.shoplist.data.database.ShopListDao
import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.presentation.ShopItemApp
import javax.inject.Inject

class ShopListProvider: ContentProvider() {

    //получаем экземпляр копмпонента от класса APP
    private val component by lazy {
        (context as ShopItemApp).component
    }

    //инжектим Дао
    @Inject
    lateinit var shopListDao: ShopListDao
    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        //для передачи числа (например id в "path" через / добавляем # ("path/#"))
        //для передачи строки (например в "path" через / добавляем * ("path/*"))
        addURI("ru.yundon.shoplist", "shop_items", GET_SHOP_ITEM_QUERY)
        addURI("ru.yundon.shoplist", "shop_items/#", GET_SHOP_ITEM_BY_ID_QUERY)
        addURI("ru.yundon.shoplist", "shop_items/*", GET_SHOP_ITEM_BY_NAME_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {

        //получаем код
        val code = uriMatcher.match(uri)
        //проверяем код и возвращаем необходимый ответ
        return when(code){
            GET_SHOP_ITEM_QUERY -> {
                shopListDao.getShopListCursor()
            }
            else -> null
        }
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    //добавляем элементый в базу через контент провайдер, добавление закомментированно в ShopItemFragment
    override fun insert(uri: Uri, value: ContentValues?): Uri? {
        when(uriMatcher.match(uri)){
            GET_SHOP_ITEM_QUERY -> {
                if (value == null) return null
                val id = value.getAsInteger("id")
                val name = value.getAsString("name")
                val count = value.getAsInteger("count")
                val enable = value.getAsBoolean("count")
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enable = enable
                )
                shopListDao.addShopItemContentProvider(mapper.mapModelToEntity(shopItem))
            }
        }
        return null
    }

    //удаляет выбранные элементы, selection не используем, т.к. библиотека Рум за нас добавляет  необходимую надпись, но если без рум самостоятельно писали обращение в базу, то надо было  использовать
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        // проверем uri
        when(uriMatcher.match(uri)) {
            GET_SHOP_ITEM_QUERY -> {
                //получаем необходимый id из массива  selectionArgs
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                return shopListDao.deleteShopItemContentProvider(id) //удаляем через рум
            }
        }
        return 0
    }

    override fun update(uri: Uri, value: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        when(uriMatcher.match(uri)) {
            GET_SHOP_ITEM_QUERY -> {
                if (value == null) return 0
                val id = value.getAsInteger("id")
                val name = value.getAsString("name")
                val count = value.getAsInteger("count")
                val enable = value.getAsBoolean("count")
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enable = enable
                )
                shopListDao.addShopItemContentProvider(mapper.mapModelToEntity(shopItem))
            }
        }
        return 1
    }

    companion object{
        const val GET_SHOP_ITEM_QUERY = 100
        const val GET_SHOP_ITEM_BY_ID_QUERY = 101
        const val GET_SHOP_ITEM_BY_NAME_QUERY = 102
    }
}