package ru.yundon.shoppinglist.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.yundon.shoppinglist.R
import ru.yundon.shoppinglist.databinding.NewListDialogBinding


//объект для обработки дилогового окна по списку покупок
object NewListDialog {
    //Показывает и настраивает АлерДиалог
    fun showDialog(context: Context, listener: ListenerDialog, name: String) {
        var dialog: AlertDialog? = null
        //строитель Алертдиалога
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context).setView(binding.root)

        binding.apply {
            editTextNewList.setText(name)

            if (name.isNotEmpty()) {
                textViewNewList.text = context.getString(R.string.update_list_name)
                buttonNewList.text = context.getString(R.string.update)
            }
            buttonNewList.setOnClickListener{
                val listName = editTextNewList.text.toString()
                if (listName.isNotEmpty()){
                    listener.onClick(listName)
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
        fun onClick(name: String)
    }
}