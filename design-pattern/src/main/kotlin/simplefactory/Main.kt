package simplefactory

import simplefactory.factory.Factory
import simplefactory.factory.IFactory


fun main(args:Array<String>){
    val factory:IFactory = Factory()
    factory.createPhone("Huawei").call()
    factory.createPhone("Samsung").hold()
}