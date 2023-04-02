package ru.yundon.shoplist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.domain.usecases.DeleteShopItemUseCase
import ru.yundon.shoplist.domain.usecases.EditShopItemUseCase
import ru.yundon.shoplist.domain.usecases.GetShopListUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getShopListUseCase: GetShopListUseCase,
    private val deleteShopItemUseCase: DeleteShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
): ViewModel() {

    val shopList = getShopListUseCase.getShopList()


    fun deleteShopListItem(shopItem: ShopItem){
        viewModelScope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun changeEnableState(shopItem: ShopItem){

        viewModelScope.launch {
            val newItem = shopItem.copy(enable = !shopItem.enable)
            editShopItemUseCase.editShopItem(newItem)
        }

    }

}