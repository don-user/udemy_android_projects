package ru.yundon.shoplist.domain.usecases

import androidx.lifecycle.LiveData
import ru.yundon.shoplist.domain.ShopListRepository
import ru.yundon.shoplist.domain.model.ShopItem
import javax.inject.Inject

class GetShopListUseCase @Inject constructor (private val shopListRepository: ShopListRepository) {

    fun getShopList(): LiveData <List<ShopItem>>{
        return shopListRepository.getShopList()
    }
}