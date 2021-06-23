package tempatemethod

abstract class Abstract {
    abstract fun doA();
    abstract fun doB();
    abstract fun doC();
    fun callTemplateMethod(){
        this.doA()
        this.doB()
        this.doC()
    }
}