package ru.yundon.shoplist.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.yundon.shoplist.R

@BindingAdapter("numberToString")
fun bindNumberToString(textView: TextView, number: Int){
    textView.text = number.toString()
}

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean){
    val messageName = if (isError) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else null

    textInputLayout.error = messageName

}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean){

    val messageCount = if (isError) {
        textInputLayout.context.getString(R.string.error_input_count)
    } else null

    textInputLayout.error = messageCount
}
