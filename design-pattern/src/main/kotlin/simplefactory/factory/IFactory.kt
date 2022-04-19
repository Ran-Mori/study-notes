package simplefactory.factory

import simplefactory.product.IPhone

interface IFactory {
    fun createPhone(phoneName:String): IPhone
}