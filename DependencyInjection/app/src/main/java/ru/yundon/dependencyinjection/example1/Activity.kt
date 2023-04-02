package ru.yundon.dependencyinjection.example1

import javax.inject.Inject

class Activity {

    @Inject
    lateinit var computer: Computer

//    @Inject
//    lateinit var keyboard: Keyboard
//    @Inject
//    lateinit var mouse: Mouse
//    @Inject
//    lateinit var monitor: Monitor

    //реализация при помощи даггер2 иньекции в геттер
//    val keyboard = DaggerNewComponent.create().getKeyboard()
//    val mouse = DaggerNewComponent.create().getMouse()
//    val monitor = DaggerNewComponent.create().getMonitor()

    init {

//        реализация при помощи иньекции в конструктор dagger2
       DaggerNewComponent.create().inject(this)
        //самостоятельная иньекция зависимости
//        Component().inject(this)
    }
}