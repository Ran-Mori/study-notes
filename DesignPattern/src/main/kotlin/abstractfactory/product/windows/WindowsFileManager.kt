package abstractfactory.product.windows

import abstractfactory.product.IFileManager

class WindowsFileManager: IFileManager {
    override fun manage() {
        println("windows manage file")
    }
}