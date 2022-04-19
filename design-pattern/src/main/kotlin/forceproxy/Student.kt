package forceproxy

class Student:IStudent {
    private var proxy:IStudent? = null
    override fun learn() {
        if (proxy == null){
            println("请使用代理访问")
        }else{
            println("learn")
        }
    }

    override fun getProxy(): IStudent? {
        //因为有这个赋值的过程，因此获取代理后接可以执行了
        proxy = StudentProxy(this)
        return proxy
    }
}