package ru.yundon.shoppinglist.fragments

import androidx.fragment.app.Fragment

//базовый фрагмент для настройки остальных фрагментов, через наследование этого фрагмента
abstract class BaseFragment: Fragment() {
    //функция которую будем переопредлять для каждого фрагмента и принажатии NEW будут разные результаты
    abstract fun onClickNew()
}