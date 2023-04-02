package ru.yundon.factorialcoroutine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class MainViewModel: ViewModel() {

    //создаем собственный корутинскоуп
    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("My coroutine scope")
    )

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?){

        _state.value = Progress
        if (value.isNullOrBlank()){
            _state.value = Error
            return
        }

        //выполнение на собственной корутин коупе
        coroutineScope.launch {
            //выполняется на главном потоке
            val number = value.toLong()
            //подключаем поток Default
            val res = withContext(Dispatchers.Default){
                factorial(number)
            }
            //выполняется на главном потоке
            _state.value = FactorialResult(res)
            Log.d("MyTag", "ViewModel ${coroutineContext.toString()}")
        }
    }

    //реализация без suspend
    private fun factorial(number: Long) : String {
        var result = BigInteger.ONE
        for (i in 1..number) {
            result = result.multiply(BigInteger.valueOf(i))
        }
        return result.toString()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }


}