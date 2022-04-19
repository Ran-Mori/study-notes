package simplefactory.product

class Huawei: IPhone {
    override fun call() {
        println("huawei call")
    }

    override fun hold() {
        println("huawei hold")
    }
}