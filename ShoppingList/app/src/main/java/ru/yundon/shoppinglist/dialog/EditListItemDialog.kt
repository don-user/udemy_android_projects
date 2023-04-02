package ru.yundon.shoppinglist.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.databinding.EditListItemDialogBinding
import ru.yundon.shoppinglist.databinding.NewListDialogBinding
import ru.yundon.shoppinglist.entities.ShoppingListItem


//объект для обработки дилогового окна по списку покупок
object EditListItemDialog {
    //Показывает и настраивает АлерДиалог
    fun showDialog(context: Context, item: ShoppingListItem, listener: ListenerDialog) {
        var dialog: AlertDialog? = null
        //строитель Алертдиалога
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context).setView(binding.root)

        binding.apply {
            editTextName.setText(item.name)
            editTextItemInfo.setText(item.itemInfo)

            if (item.itemType == 1) editTextItemInfo.visibility = View.GONE

            buttonUpdate.setOnClickListener {
                if (editTextName.text.toString().isNotEmpty()){
                    listener.onClick(item.copy(name = editTextName.text.toString(), itemInfo = editTextItemInfo.text.toString()))
                }
                dialog?.dismiss()
            }

        }
        dialog = builder.create()
        dialog.apply {
            window?.setBackgroundDrawable(null)
            show()
        }
    }
    //Слушатель нажатия на копку для сохранения в базу данных
    interface ListenerDialog{
        fun onClick(listItem: ShoppingListItem)
    }
}