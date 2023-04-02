package ru.yundon.shoplist.presentation.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


//перенесенный класс из адаптера
class ShopItemViewHolder(
    val binding: ViewDataBinding
    ): RecyclerView.ViewHolder(binding.root)