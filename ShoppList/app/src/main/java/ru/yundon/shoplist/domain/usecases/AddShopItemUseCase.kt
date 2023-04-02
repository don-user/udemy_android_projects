package ru.yundon.shoplist.domain.usecases

import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.domain.ShopListRepository
import javax.inject.Inject

//метод добавления итема
class AddShopItemUseCase @Inject constructor (private val shopListRepository: ShopListRepository) {

    suspend fun addShopItem(shopItem: ShopItem){

        shopListRepository.addShopItem(shopItem)
    }
}