package tempatemethod


fun main(args:Array<String>){
    val concreteA:Abstract = ConcreteA()
    val concreteB:Abstract = ConcreteB()

    concreteA.callTemplateMethod()
    concreteB.callTemplateMethod()
}