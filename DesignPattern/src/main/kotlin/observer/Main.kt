package observer

fun main(args:Array<String>){
    val observable = Observable()
    val observer = Observer()
    observable.addObserver(observer)
    observable.observableChange()
}