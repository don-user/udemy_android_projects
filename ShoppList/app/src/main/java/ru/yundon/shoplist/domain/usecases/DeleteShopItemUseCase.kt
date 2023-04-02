package ru.yundon.shoplist.domain.usecases

import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.domain.ShopListRepository
import javax.inject.Inject

class DeleteShopItemUseCase @Inject constructor (
    private val shopListRepository: ShopListRepository
    ) {

    suspend fun deleteShopItem(shopItem: ShopItem){
        shopListRepository.deleteShopItem(shopItem)
    }
}