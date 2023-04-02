package ru.yundon.shoppinglist.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import ru.yundon.shoppinglist.databinding.DeleteDialogBinding
import ru.yundon.shoppinglist.databinding.NewListDialogBinding


//объект для обработки дилогового окна по удалению списка покупок
object DeleteDialog {
    //Показывает и настраивает АлерДиалог
    fun showDialog(context: Context, listener: ListenerDialog) {
        var dialog: AlertDialog? = null
        //строитель Алертдиалога
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(context).setView(binding.root)

        binding.apply {
            //кнопка удаление нажатие
            buttonDelete.setOnClickListener{
                listener.onClick()
                dialog?.dismiss()
            }
            //кнопка Cancel нажатие
            buttonCancel.setOnClickListener{
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.apply {
            window?.setBackgroundDrawable(null)
            show()
        }
    }
    //Слушатель нажатия на кнопку для удаления
    interface ListenerDialog{
        fun onClick()
    }
}