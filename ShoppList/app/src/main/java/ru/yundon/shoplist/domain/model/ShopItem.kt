package ru.yundon.shoplist.domain.model

data class ShopItem(
    val name: String,
    val count: Int,
    val enable: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object{
        const val UNDEFINED_ID = 0 //-1 означает, что id еще не установлен
    }
}
