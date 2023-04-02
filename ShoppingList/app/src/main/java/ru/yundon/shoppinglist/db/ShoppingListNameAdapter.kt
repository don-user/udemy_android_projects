package ru.yundon.shoppinglist.db

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.databinding.ListNameItemBinding
import ru.yundon.shoppinglist.entities.ShopListNameItem
import ru.yundon.shoppinglist.entities.ShoppingListItem

class ShoppingListNameAdapter(private val listener: Listener): ListAdapter<ShopListNameItem, ShoppingListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ListNameItemBinding.bind(view)
        fun setData(listNameItem: ShopListNameItem, listener: Listener) = with(binding){
            tvListName.text = listNameItem.name
            tvTime.text = listNameItem.time
            //заполняет прогресс бар
            progressBar.max = listNameItem.allItemsCounter
            progressBar.progress = listNameItem.checkedItemsCounter
            val colorState = ColorStateList.valueOf(getProgressColorState(listNameItem, binding.root.context))
            progressBar.progressTintList = colorState
            //меняется цвет на карточке где отображается количество покупок всего и сколько отмеченно
            counterCard.backgroundTintList = colorState
            //показывает сколько из списка покупок отмеченно из общего количества
            val counterText = "${listNameItem.checkedItemsCounter}/${listNameItem.allItemsCounter}"
            textViewCounter.text = counterText
            itemView.setOnClickListener {
                listener.onClickItem(listNameItem)
            }
            imageButtonDelete.setOnClickListener{
                listener.deleteItem(listNameItem.id!!)
            }
            imageButtonEdit.setOnClickListener{
                listener.editItem(listNameItem)
            }

        }

        //меняет цвет прогресс бара
        private fun getProgressColorState(item: ShopListNameItem, context: Context): Int{
            return if (item.checkedItemsCounter == item.allItemsCounter){
                ContextCompat.getColor(context, R.color.green_main)
            }else ContextCompat.getColor(context, R.color.red_main)

        }
        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_name_item, parent, false))
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ShopListNameItem>(){
        override fun areItemsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListNameItem, newItem: ShopListNameItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener{
        fun deleteItem(id: Int)
        fun editItem(shopListNameItem: ShopListNameItem)
        fun onClickItem(shopListNameItem: ShopListNameItem)

    }
}