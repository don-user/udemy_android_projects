package ru.yundon.shoplist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import ru.yundon.shoplist.R
import ru.yundon.shoplist.databinding.ItemShopDisabledBinding
import ru.yundon.shoplist.databinding.ItemShopEnabledBinding
import ru.yundon.shoplist.domain.model.ShopItem

class ShopListAdapter: ListAdapter<ShopItem, ShopItemViewHolder> (ShopItemDIffCallback()) {

//    var listItem = listOf<ShopItem>()
//        set(value) {
//            val callback = ShopListDiffCallback(listItem, value) // value это новый список
//            val diffResult = DiffUtil.calculateDiff(callback) //делает все расчеты, по каким элементам прошли изменения и хранит эти изменения
//            diffResult.dispatchUpdatesTo(this) //делает обновления в адаптаре удаляя конкретный элемент или перерисовывая конкретный элемент
//
//            field = value // обновляем сам список
//
//        }


    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null //переменная в которой хранится лябмда функция

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null //переменная в которой хранится лябмда функция

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType){
            TYPE_VIEW_ENABLED -> R.layout.item_shop_enabled
            TYPE_VIEW_DISABLED -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>( //нужно явно указать ViewDataBinding
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {

        val shopItem = getItem(position)
        val binding = holder.binding

        when (binding){
            is ItemShopDisabledBinding ->{
                binding.shopItem = shopItem
            }
            is ItemShopEnabledBinding -> {
                binding.shopItem = shopItem
            }
        }


        binding.root.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItem) // если тип hullable то необходимо вызывать invoke
            true
        }

        binding.root.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }

//    override fun getItemCount(): Int = getItem().size

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enable) TYPE_VIEW_ENABLED else TYPE_VIEW_DISABLED
    }

    companion object{
        const val TYPE_VIEW_ENABLED = 1
        const val TYPE_VIEW_DISABLED = 0

        const val MAX_PULL_SIZE = 15
    }
}