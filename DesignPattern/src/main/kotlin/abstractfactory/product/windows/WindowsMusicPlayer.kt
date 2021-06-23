package abstractfactory.product.windows

import abstractfactory.product.IMusicPlayer

class WindowsMusicPlayer: IMusicPlayer {
    override fun play() {
        println("windows class music play")
    }
}