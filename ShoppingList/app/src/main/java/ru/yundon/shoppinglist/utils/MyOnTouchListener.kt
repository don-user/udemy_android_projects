package ru.yundon.shoppinglist.utils

import android.view.MotionEvent
import android.view.View

//класс для перемещения КолорПикера по экрану
class MyOnTouchListener: View.OnTouchListener {
    //Сюда записываем координаты для колорПикера
    var xDelta = 0.0f
    var yDelta = 0.0f

    override fun onTouch(view: View, event: MotionEvent?): Boolean {
        when(event?.action){
            //когда отпускаем наш КолорПикер
            MotionEvent.ACTION_DOWN ->{
                xDelta = view.x - event.rawX //view.x где находимся минус event.rawX куда переместили
                yDelta = view.y - event.rawY //view.y где находимся минус event.rawY куда переместили
            }
            MotionEvent.ACTION_MOVE ->{
                view.x = xDelta + event.rawX
                view.y = yDelta + event.rawY
            }
        }
        return true
    }
}