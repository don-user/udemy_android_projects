package ru.yundon.shoplist.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.yundon.shoplist.domain.model.ShopItem
import ru.yundon.shoplist.domain.usecases.AddShopItemUseCase
import ru.yundon.shoplist.domain.usecases.EditShopItemUseCase
import ru.yundon.shoplist.domain.usecases.GetShopItemByIdUseCase
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val addShopItemUseCase: AddShopItemUseCase,
    private val getShopItemByIDUseCase: GetShopItemByIdUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
): ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData <Boolean>
        get()= _errorInputName      //вызываем геттерт и присваиваем значение в поле
    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean> = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem> = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)

        if (fieldsValid){

            viewModelScope.launch {
                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                Log.d("MyTag", "addShopItem, перед finish")
                finishWork()
            }
        }
    }

    fun getShopItemById(shopItemId: Int){
        viewModelScope.launch{
            val item = getShopItemByIDUseCase.getShopItem(shopItemId)
            _shopItem.postValue(item)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)

        if (fieldsValid){
            _shopItem.value?.let {

                viewModelScope.launch {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()
                }
            }
        }
    }

    private fun parseName(inputName: String?): String{
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int{
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean{
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        Log.d("MyTag", "RESULT $result")
        return result
    }

    fun resetErrorInputName(){
        _errorInputName.value = false
    }

    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}