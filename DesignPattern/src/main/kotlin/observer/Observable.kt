package observer

import java.util.Observable

class Observable:Observable() {
    fun observableChange(){
        println("observable change")
        notifyObservers("args to pass")
        super.setChanged()
        super.notifyObservers()
    }
}