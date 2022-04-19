package simplefactory.factory

import simplefactory.product.IPhone
import java.lang.Exception

class Factory: IFactory {
    private val productBasePackage = "simplefactory.product"
    override fun createPhone(phoneName: String): IPhone {
        when(phoneName){
            "Huawei" -> return Class.forName("$productBasePackage.Huawei").newInstance() as IPhone
            "Samsung" -> return Class.forName("$productBasePackage.Samsung").newInstance() as IPhone
            else -> throw Exception("wrong name")
        }
    }
}