package ru.yundon.shoppinglist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.databinding.ListNameItemBinding
import ru.yundon.shoppinglist.databinding.ShopLibraryListItemBinding
import ru.yundon.shoppinglist.databinding.ShopListItemBinding
import ru.yundon.shoppinglist.entities.ShopListNameItem
import ru.yundon.shoppinglist.entities.ShoppingListItem

class ShoppingListItemAdapter(private val listener: Listener): ListAdapter<ShoppingListItem, ShoppingListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0) ItemHolder.createShopItem(parent) //если = 0 то создается разметка createShopItem
        else ItemHolder.createLibraryItem(parent) //если равно 1 то другая разметка
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) holder.setItemData(getItem(position), listener)
        else holder.setLibraryData(getItem(position), listener)
    }

    //возвращает во viewType из функции onCreateViewHolder 0 или 1, где 1 это RView списка покупок 0 RView в библиотеки
    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(val view: View): RecyclerView.ViewHolder(view){
        //управление нажатием на кнопки и текста, едиттекста  внутри списка покупок
        fun setItemData(listItem: ShoppingListItem, listener: Listener) {
            val binding = ShopListItemBinding.bind(view)
            binding.apply {
                textViewName.text = listItem.name
                textViewInfo.text = listItem.itemInfo
                textViewInfo.visibility = infoVisibility(listItem)
                checkBox.isChecked = listItem.itemChecked
                setPaintFlagAndColor(binding)
                //нажимаем на чекбокс и и меняем статус чекбокса в базе
                checkBox.setOnClickListener {
                    listener.onClickItem(listItem.copy(itemChecked = checkBox.isChecked), CHECK_BOX) ///слушает и записывает либо true либо false
                }
                imageButtonEditLibrary.setOnClickListener {
                    listener.onClickItem(listItem, EDIT)
                }
            }
        }
        //управление нажатием на кнопки и текста внутри библиотеки слов
        fun setLibraryData(listItem: ShoppingListItem, listener: Listener){
            val binding = ShopLibraryListItemBinding.bind(view)
            binding.apply {
                textViewName.text = listItem.name
                imageButtonEditLibrary.setOnClickListener {
                    listener.onClickItem(listItem, EDIT_LIBRARY_ITEM)
                }
                imageButtonDeleteLibrary.setOnClickListener {
                    listener.onClickItem(listItem, DELETE_LIBRARY_ITEM)
                }
                itemView.setOnClickListener {
                    listener.onClickItem(listItem, ADD)
                }
            }

        }
        //изменяет текст при нажатии на чекбокс
        private fun setPaintFlagAndColor(binding: ShopListItemBinding){
            binding.apply {
                if (checkBox.isChecked) {
                    textViewName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    textViewInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    textViewName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                    textViewInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                } else {
                    textViewName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    textViewInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    textViewName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    textViewInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                }
            }

        }

        //отображает или нет информацию к покупке
        private fun infoVisibility(listItem: ShoppingListItem): Int{
            return if (listItem.itemInfo.isNullOrEmpty()) View.GONE
            else View.VISIBLE
        }

        companion object{
            fun createShopItem(parent: ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.shop_list_item, parent, false))
            }

            fun createLibraryItem(parent: ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.shop_library_list_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ShoppingListItem>(){
        override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener{
        fun onClickItem(shopListItem: ShoppingListItem, state: Int)
    }

    companion object{
        const val EDIT = 0
        const val CHECK_BOX = 1
        const val EDIT_LIBRARY_ITEM = 2
        const val DELETE_LIBRARY_ITEM = 3
        const val ADD = 4

    }
}