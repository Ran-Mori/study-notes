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
> ### ViewGroup
>
> * `ViewGroup.addView(View child)`：向一个`ViewGroup`中添加`View`
> * `ViewGroup.removeAllViews()`：删除所有View
> * `ViewGroup.getChildCount()`：获取child个数
> * `LinearLayout`
>   * 是`ViewGroup`
>   * `xml`里面定义的是编译期`childen`
>   * 运行时可以调用父类`ViewGroup`的方法进行修改
>
> ### 动态修改margin
>
> ```kotlin
> (mApproveBtn.layoutParams as? LinearLayout.LayoutParams)?.apply {
>     leftMargin = UIUtils.dip2Px(mItemView.context, 8F).toInt()
>     mApproveBtn.layoutParams = this
> }
> ```
>
> ### UML图
>
> * `实现关系`：接口及实现类的关系。由一个三角形一条直线组成
> * `泛化关系`：对象与对象之间的继承关系。由一个三角形和一条直线组成
> * `关联关系`：对象与对象之间的连接关系。通常为一个对象持有另一个对象的引用。往往还有数量关系。由一个带箭头的实现组成
> * `依赖关系`：对象与对象之间的弱关联关系。通常为构造器、方法局部参数、返回值、静态方法调用
>
> ### ViewHolder.Bind()
>
> * `ViewHolder.bind()`会在绑定的时候调用，用得好会有很多好处
> * 比如展示备注按钮
>
> ### ViewHolder获取根View
>
> ```kotlin
> class MyFragment() {
>   private mRootView: ViewGroup? = null
>   override fun onCreateView() {
>     val view = nflater.inflate(viewGroup, container, false)
>     (view as? ViewGroup)?.let{
>       mRootView = it
>     }
>   }
> }
> ```
>
> ### 传递方式
>
> * 通过构造函数传入
> * 通过私有变量，通过公有`set、get`方法对外暴露
> * 结合第二点通过高阶函数传递
>
> ***

