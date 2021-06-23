package abstractfactory.product.macos

import abstractfactory.product.IMusicPlayer

class MacosMusicPlayer: IMusicPlayer {
    override fun play() {
        println("macos music play")
    }
}