package abstractfactory.factory

import abstractfactory.product.IFileManager
import abstractfactory.product.IMusicPlayer
import abstractfactory.product.windows.WindowsFileManager
import abstractfactory.product.windows.WindowsMusicPlayer

class WindowsFactory:IFactory {
    override fun createMusicPlayer(): IMusicPlayer {
        return WindowsMusicPlayer()
    }

    override fun createFileManager(): IFileManager {
        return WindowsFileManager()
    }
}