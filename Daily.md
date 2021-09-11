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
> * `实现关系`：接口及实现类的关系。由一个空心三角形一条直线组成，三角形指向接口
> * `泛化关系`：对象与对象之间的继承关系。由一个三角形和一条直线组成，三角形指向父对象
> * `关联关系`：对象与对象之间的连接关系。通常为一个对象持有另一个对象的引用。往往还有数量关系。线条是一个带箭头的实线，箭头执行被使用者
> * `依赖关系`：对象与对象之间的弱关联关系。通常为构造器、方法局部参数、返回值、静态方法调用。线条是一个带箭头的虚线，箭头执行被使用者
> * `聚合关系`：对象与对象之间的强关联关系。部分可以离开主体而存在。线条是带空心的菱形实线，菱形指向主体
> * `组合关系`：对象与对象之间的强关联关系。部分不可以离开主体而存在。线条是带实心的菱形实线，菱形指向主体
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
> ### 变量、方法传递方式
>
> * 通过构造函数传入
> * 通过私有变量，通过公有`set、get`方法对外暴露
> * 结合第二点通过高阶函数传递
>
> ### 编码问题详解
>
> * `字符`：各种文字和符号的总称
> * `字符集`：即字符表，如英文字符表、阿拉伯字符表、ASCII码表……
> * `coded charater set`：为字符表中每一个字符指定一个编码(码点code point)得到编码字符集。如ASCII字符集、Unicode字符集、GB2312字符集……
> * `charater encoding form`：编码字符集只规定了字符和码点的映射，并没有规定实际的`字节表示方式`。如ASCII编码、UTF-8编码、UTF-32编码……
> * `UTF-8`：对ASCII向后兼容，字节数可以变长，从1到4
> * `UTF-16`：支持2字节和4字节，是一种折中
> * `UTF-32`：无脑用4字节，浪费空间，几乎没怎么用
> * `GoLang字符编码表`
>   * 统一全部使用`utf-8`
>   * `len(string)`是字节长度而不是字符长度，即`len("早") = 3`
> * `hex编码`
>   * 是一种二进制编码，将二进制数据编码成文本数据，与上面的字符编码恰恰相反
>   * 将复杂不可见的字节数组数据，转换成可显示的字符串数据
>   * 存储ASCII效率为50%。因为1个ASCII字符占8位，编码后成了2个ASCII字符占16位，因此50%
> * `base64编码`
>   * 字符为`abc...zABC...Z012...9+/`
>   * 6位一组，即3字节4组。编ASCII效率为`75%`
> * `StdEncoding`：末尾差字节以`0`填充
> * `RawEncoding`：相比标准减少了padding，遇到不是3倍数时采用移位
> * `UrlEncoding`：`+ -> -`，`/ -> _`
> * `文本传输`：http以文本传输，要用hex或者base64编码。内容为文本，自带描述信息(参数名)
>   * `二进制传输`：tcp以二进制传输，要用字符编码。内容为二进制，以预先定义好的格式拼接在一起
>
> ***

## 210816 - 210822

> ### LayoutParams
>
> * `View.getLayoutParams()`：每个View都有这个方法，既每个View都有`mLayoutParams`属性
> * `ViewGroup.LayoutParams`决定了父容器怎么arrange这个View
> * `LayoutParams`有很多子类，如`LinearLayout.LayoutParams`、`FrameLayout.LayoutParams`、`RelativeLayout.LayoutParams`。不同的子类对应父容器不同，即如果父容器是`FrameLayout`则子View的`mLayoutParams`应该是`FrameLayout.LayoutParams`
>
> ### 多仓问题
>
> * 未用多仓同步工具时，引入组件一定要记得看看分支对不对，不然编译不过
>
> ### MyConfig
>
> ```kotlin
> //difine
> class MyConfig private constructor(builder: Builder) {
>     companion object {
>         fun build(block: Builder.() -> Unit = {}) = Builder().apply { block() }.build()
>     }
> 
>     val owner: LifecycleOwner? = builder.owner
> 
>     class Builder {
>         var owner: LifecycleOwner? = null
> 
>         fun build() = MyConfig(this)
>     }
> }
> ```
>
> ```kotlin
> //use
> private fun getMyConfig() = MyConfig.build{ lifecycleOwner = this.getLifecleOwner }
> ```
>
> * `MyConfig.buid()`：高阶函数声明了函数的接收者，即`Builder.() -> Unit`
> * 构建时传入高阶函数`lifecycleOwner = this.getLifecleOwner`
> * 调用`Builder().apply { block() }`时，因为`block()`的接收者是`builder`，实际上是调用`builder.block()`，即完成了`var owner: LifecycleOwner? = null`的初始化
> * 接着调用`Builder.build() = MyConfig(this)` 方法，传入`this`
> * 最后执行`MyConfig`的私有构造函数，初始化时执行`val owner: LifecycleOwner? = builder.owner`完成参数传递
>
> ### MyListenerWrapper
>
> ```kotlin
> private class MyListenerWrapper<T>(val listener: T, val myConfig: MyConfig, val map: MutableMap<T, MyListenerWrapper<T>>): LifecycleObserver {
>     init {
>         myConfig.owner?.lifecycle?.addObserver(this)
>     }
> 
>     @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
>     fun onDestroy() {
>         myConfig.owner?.lifecycle?.removeObserver(this)
>         map.remove(listener)
>     }
> }
> ```
>
> * 本身是一个`LifecycleObserver`
> * `init`时观察`config`传入的`LifecycleOwner`
> * `onDestroy`时取消观察，并将`listner`从`map`集合中移除
>
> ### 过程使用
>
> * 创建`MyConfig`，传入一个`LifecycleOwner`
> * 定义一个`listener`，并带上`MyConfig`一起传过来
> * 传入的内容包装成一个`wrapper`，完成生命周期管理，防止内存泄漏。并且有一个`map`，里面都是存的各种`listener`
> * 发生了一个关注事件
> * `handler`的`handlerCallback()`调用
> * 遍历`map`中的`listener`并依次调用`listener.invoke()`方法
> * 自此就完成了一个
>
> ### LayoutInflater.inflate()
>
> * 函数签名：`public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)`
> * `@LayoutRes int resource`：想要初始化创造的View
> * `@Nullable ViewGroup root`
>   * `attachToRoot = true`：调用`root.addView(view)`
>   * `attachToRoot = false`：调用
>
> ***

## 210823-210829

> ### 进设置页
>
> * Activity和Fragment会调用到`onStop()`
>
> ***

## 210906 - 210912

> ### by 关键字
>
> * 用来实现委托模式
>
> ### infix关键字
>
> * `to`例子：`public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)`
>
> ### lazy 函数
>
> * `lazy`不是关键字而是一个函数
> * 函数签名：`public actual fun <T> lazy(mode: LazyThreadSafetyMode, initializer: () -> T): Lazy<T>`
>
> ### 自定义通用View对外暴露通用点击实现
>
> * MyView.kotlin
>
> ```kotlin
> class MyView(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): FrameLayout, View.OnClickListener{
>   
>   private lateinit var mOnBarClickListener: OnBarClickListener
>   
>   override fun onClick(v: View?) {
>     mOnBarClickListener ?: return
>     when(v.id) {
>       back_item -> mOnBarClickListener.onBackClick(v)
>       confirm_item -> mOnBarClickListener.onConfirmClick(v)
>     }
>   }
>   
>   fun setOnBarClickListener(listener: OnBarClickListener) {
>     this.mOnBarClickListener = listener
>   }
> }
> ```
>
> * OnBarClickListener.kt
>
> ```kotlin
> interface OnBarClickListener {
>   fun onBackClick(view: View?)
>   fun onConfirmClick(view: View?)
> }
> ```
>
> ### LoadingController
>
> * 实际就是展示一个`Dialog`
> * 根据状态来`show`和`dismiss`
>
> ### 建造者模式
>
> ```kotlin
> class Example(builder: Builder) {
>   var text: String? = ""
>   
>   init {
>     this.text = builder.text
>   }
>   
>   open class Buidler {
>     var text: String? = ""
>     
>     fun setText(text: String): Builder {
>       this.text = text
>       return this
>     }
>     
>     fun build() = Example(this)
>   }
> }
> ```
>
> ### SPI子分层
>
> * IService.kt
>
> ```kotlin
> interface IService {
>   fun getSubService(): ISubService
> }
> ```
>
> * ServiceImpl.kt
>
> ```kotlin
> class ServiceImpl: IService {
>   @Overide
>   fun getSubService(): ISubService = SubServiceImpl
> }
> ```
>
> * ISubService.kt
>
> ```kotlin
> interface ISubService {
>   fun getFrequency(): Int
> }
> ```
>
> * SubServiceImpl.kt
>
> ```kotlin
> object SubServiceImpl:ISubService, PushCallBack {
>   @Overide
>   fun onPushSuccess(data: Data) {
>     keva.storeData(data)
>   }
>   
>   @Overide
>   fun onPushFail() {
>     keva.storeData(Data.getDefault())
>   }
>   
>   @Overide
>   fun getFrequency() = keva.getData()
> }
> ```

