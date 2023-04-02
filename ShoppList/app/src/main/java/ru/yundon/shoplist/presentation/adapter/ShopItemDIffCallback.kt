package ru.yundon.shoplist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.yundon.shoplist.domain.model.ShopItem

//класс который сравнивает конкретные элементы из списка
class ShopItemDIffCallback: DiffUtil.ItemCallback<ShopItem>(){

    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }
//сравниваем все поля в двух элементы
    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}