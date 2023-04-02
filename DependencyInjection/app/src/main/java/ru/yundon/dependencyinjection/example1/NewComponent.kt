package ru.yundon.dependencyinjection.example1

import dagger.Component

@Component(modules = [ComputerModule::class])
interface NewComponent {

//    fun getKeyboard() : Keyboard
//
//    fun getMouse() : Mouse
//
//    fun getMonitor() : Monitor


    fun inject(activity: Activity)
}