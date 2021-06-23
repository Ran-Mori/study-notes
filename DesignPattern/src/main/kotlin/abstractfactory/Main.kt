package abstractfactory

import abstractfactory.factory.IFactory
import abstractfactory.factory.MacosFactory
import abstractfactory.factory.WindowsFactory


fun main(args:Array<String>){
    val windowsFactory:IFactory = WindowsFactory()
    windowsFactory.createMusicPlayer().play()
    windowsFactory.createFileManager().manage()

    val macosFactory:IFactory = MacosFactory()
    macosFactory.createMusicPlayer().play()
    macosFactory.createFileManager().manage()
}