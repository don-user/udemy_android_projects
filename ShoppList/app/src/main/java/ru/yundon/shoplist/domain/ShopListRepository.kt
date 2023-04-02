package ru.yundon.shoplist.domain

import androidx.lifecycle.LiveData
import ru.yundon.shoplist.domain.model.ShopItem

//Этот репозиторий будет и умеет работать с данными

interface ShopListRepository {

    //добавлять элемент
    suspend fun addShopItem(shopItem: ShopItem)

    //удалять элемент
    suspend fun deleteShopItem(shopItem: ShopItem)

    //редактировать элемент
    suspend fun editShopItem(shopItem: ShopItem)

    //получать элемент по item
    suspend fun getShopItem(shopItemId: Int): ShopItem

    //получать список элементов
    fun getShopList(): LiveData<List<ShopItem>>

}