package ru.yundon.shoplist.domain.usecases

import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.domain.ShopListRepository
import javax.inject.Inject

class GetShopItemByIdUseCase @Inject constructor (
    private val shopListRepository: ShopListRepository
    ){

    suspend fun getShopItem(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItem(shopItemId)
    }
}
