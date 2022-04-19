package abstractfactory.product.macos

import abstractfactory.product.IFileManager

class MacosFileManager: IFileManager {
    override fun manage() {
        println("macos manage file")
    }
}