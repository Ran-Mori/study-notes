package abstractfactory.factory

import abstractfactory.product.IFileManager
import abstractfactory.product.IMusicPlayer
import abstractfactory.product.macos.MacosFileManager
import abstractfactory.product.macos.MacosMusicPlayer

class MacosFactory:IFactory {
    override fun createMusicPlayer(): IMusicPlayer {
        return MacosMusicPlayer()
    }

    override fun createFileManager(): IFileManager {
        return MacosFileManager()
    }
}