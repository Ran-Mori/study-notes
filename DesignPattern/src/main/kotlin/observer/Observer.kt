package observer

import java.util.Observable
import java.util.Observer

class Observer:Observer{
    override fun update(o: Observable?, arg: Any?) {
        println("do update method and arg = $arg")
    }
}