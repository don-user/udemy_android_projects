package ru.yundon.dependencyinjection.example1

//реализация иньекции зависимости класса Activity от  Computer при помощи иньекции в из вне
class Component {

   private fun getComputer() : Computer{
        val monitor = Monitor()
        val keyboard = Keyboard()
        val mouse = Mouse()
        val computerTower = ComputerTower(
            Storage(),
            Memory(),
            Processor()
        )
        return Computer(monitor, computerTower, keyboard, mouse)
    }

    fun inject(activity: Activity) {
//        activity.computer = getComputer()
//        activity.keyboard = Keyboard()
    }
}