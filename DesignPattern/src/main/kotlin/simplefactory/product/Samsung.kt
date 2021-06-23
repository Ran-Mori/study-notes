package simplefactory.product

class Samsung: IPhone {
    override fun call() {
        println("Samsung call")
    }

    override fun hold() {
        println("Samsung hold")
    }
}