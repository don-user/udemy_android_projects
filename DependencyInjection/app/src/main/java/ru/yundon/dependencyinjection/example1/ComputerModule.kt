package ru.yundon.dependencyinjection.example1

import dagger.Module
import dagger.Provides

@Module
class ComputerModule {

    @Provides
    fun provideMonitor() : Monitor{
        return Monitor()
    }
    @Provides
    fun provideMouse() : Mouse{
        return  Mouse()
    }
    @Provides
    fun provideKeyboard() : Keyboard{
        return Keyboard()
    }
    @Provides
    fun provideMemory() : Memory{
        return Memory()
    }
    @Provides
    fun provideStorage() : Storage{
        return Storage()
    }
    @Provides
    fun provideProcessor() : Processor {
        return Processor()
    }
    @Provides
    fun provideComputerTower(storage: Storage, memory: Memory, processor: Processor): ComputerTower{
        return ComputerTower(storage, memory, processor)
    }

    @Provides
    fun provideComputer(
        monitor: Monitor,
        computerTower: ComputerTower,
        keyboard: Keyboard,
        mouse: Mouse
    ) : Computer{
        return Computer(monitor, computerTower, keyboard, mouse)
    }

}