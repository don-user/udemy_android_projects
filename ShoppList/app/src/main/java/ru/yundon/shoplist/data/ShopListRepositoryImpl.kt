package ru.yundon.shoplist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ru.yundon.shoplist.data.database.ShopListDao
import ru.yundon.shoplist.domain.ShopListRepository
import ru.yundon.shoplist.domain.model.ShopItem
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: ShopListMapper
): ShopListRepository {


    override suspend fun addShopItem(shopItem: ShopItem) {

        shopListDao.addShopItem(mapper.mapModelToEntity(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {

        shopListDao.deleteShopItem(shopItem.id)
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapModelToEntity(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val shopItemEntity = shopListDao.getShopItem(shopItemId)
        return mapper.mapEntityToModel(shopItemEntity)
    }
    override fun getShopList(): LiveData<List<ShopItem>> {
        return MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopListDao.getShopList()){
                value = mapper.mapEntityListToModelList(it)
            }

        }
    }

}