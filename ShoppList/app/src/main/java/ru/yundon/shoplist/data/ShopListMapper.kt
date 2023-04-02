package ru.yundon.shoplist.data

import ru.yundon.shoplist.data.database.ShopItemEntity
import ru.yundon.shoplist.domain.model.ShopItem
import javax.inject.Inject

class ShopListMapper @Inject constructor() {

    fun mapModelToEntity(shopItem: ShopItem): ShopItemEntity {
        return ShopItemEntity(
            id = shopItem.id,
            name = shopItem.name,
            count = shopItem.count,
            enable = shopItem.enable
        )
    }

    fun mapEntityToModel(shopItemEntity: ShopItemEntity): ShopItem = ShopItem(
        id = shopItemEntity.id,
        name = shopItemEntity.name,
        count = shopItemEntity.count,
        enable = shopItemEntity.enable
    )

    fun mapEntityListToModelList(list: List<ShopItemEntity>) : List<ShopItem>{
        return list.map {
            mapEntityToModel(it)
        }
    }

}