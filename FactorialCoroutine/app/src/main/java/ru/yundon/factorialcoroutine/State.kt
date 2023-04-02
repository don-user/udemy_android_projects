package ru.yundon.factorialcoroutine

//класс для отображения состояния экрана
sealed class State

//отображеат состояние ошибки
object Error : State()
//отображает состяние пргресса
object Progress : State()
//отображает состояние результата (ответ по фкториалу)
class FactorialResult(val value: String) : State()