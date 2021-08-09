# 日常

## 210805 - 210808

> ### 安卓动画未完成前对View进行操作
>
> * 不能对View进行操作，不然最后的动画`onAnimationEnd()`会调用，导致预期和实际差异巨大
> * 要对View进行操作一定要停止动画`animator.clear()`或者`animator.end()`
>
> ### 长连接使用
>
> * 端上和server一直操持长连接
> * server给端上push一条`/count`请求
> * 端上根据返回的结果如果`multiNoticeCountList.size() > 0`就调用处理未读消息的方法
> * 处理未读消息的方法首先对`muliNoticeCountList`做一些过滤，然后通过`EventBus`抛出聚合事件
> * 事件处理方法体调用`/notice/multi`，并带上`group`作为参数，刷新对应`group`的摘要
>
> ***

## 210808 - 210815

> ### 多态
>
> * Father.kt
>
> ```kotlin
> open class Father() {
>     var name: String = ""
>     var age: Int = 0
> }
> ```
>
> * Son.kt
>
> ```kotlin
> class Son: Father() {
>     var type: Int = 0
> }
> ```
>
> * Main.kt
>
> ```kotlin
> fun main(args:Array<String>){
>     var father: Father? = null
>     var son: Son = Son()
>     son.type = 10
>     father = son
>   	father.type = 9 //编译错误，father无type字段
>     val type = (father as? Son)?.type ?: 0
>     print("type = $type") // type = 10
> }
> ```
>
> * 静态类型为父类，运行时类型为子类。将运行时子类强转成子类类型就能完全访问使用了
>
> 
