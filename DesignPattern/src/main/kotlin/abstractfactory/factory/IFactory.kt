package abstractfactory.factory

import abstractfactory.product.IFileManager
import abstractfactory.product.IMusicPlayer

//产品不能加，但系统可以增加，如增加Linux
interface IFactory {
    fun createMusicPlayer(): IMusicPlayer
    fun createFileManager(): IFileManager
}