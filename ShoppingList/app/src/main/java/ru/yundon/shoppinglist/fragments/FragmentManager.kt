package ru.yundon.shoppinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import ru.yundon.shoppinglist.R

//фрагмент менеджер для опредления фрагмента и настройки
object FragmentManager {
    var currentFrag: BaseFragment? = null
//настройка фрагментов
    fun setFragment(newFragment: BaseFragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()

        transaction.replace(R.id.placeHolder, newFragment)
        transaction.commit()
        currentFrag = newFragment
    }
}