package ru.yundon.shoplist.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.yundon.shoplist.domain.model.ShopItem


//класс для сравнения нового списка и старого для адаптера
class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
): DiffUtil.Callback(){

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    //если id элементов из списков одинаковые то возравщаем true
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id  //если id одинаковые то true
    }
//сравнивает остальные элементы на изменения если id одинаковвые
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldList[oldItemPosition] == newList[newItemPosition]  // так как в data class функция equals переопределена, то в  итемах  сравниваются все позиции с руг другом

    }

}