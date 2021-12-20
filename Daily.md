# 日常

## 210805 - 210808

> ### 安卓动画未完成前对View进行操作
>
> * 不能对View进行操作，不然当最后的动画`onAnimationEnd()`调用时，导致预期和实际差异巨大
> * 要对View进行操作一定要停止动画`animator.clear()`或者`animator.end()`
>
> ### 长连接使用
>
> * 端上和server一直操持长连接
> * server给端上push一条`/count`请求
> * 端上根据返回的结果，如果`multiNoticeCountList.size() > 0`就调用处理未读消息的方法
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
> * `关联关系`：对象与对象之间的连接关系。通常为一个对象持有另一个对象的引用。往往还有数量关系。线条是一个带箭头的实线，箭头指向被使用者
> * `依赖关系`：对象与对象之间的弱关联关系。通常为构造器、方法局部参数、返回值、静态方法调用。线条是一个带箭头的虚线，箭头指向被使用者
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
>     val view = inflater.inflate(viewGroup, container, false)
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
> * `二进制传输`：tcp以二进制传输，要用字符编码。内容为二进制，以预先定义好的格式拼接在一起
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
> * 自此就完成了一个观察者模式
>
> ### LayoutInflater.inflate()
>
> * 函数签名：`public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)`
> * `@LayoutRes int resource`：想要初始化创造的View
> * `@Nullable ViewGroup root`
>   * `attachToRoot = true`：调用`root.addView(view)`
>   * `@Nullable`注解说明可以为空
>
> ***

## 210823 - 210829

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
>
> ***

## 210927 - 211003

> ### 受检异常
>
> * 这玩意有好处也有坏处
> * Java保留有受检异常，但新的kotlin完全把受检异常去掉了
> * 无论是否有受检异常，均不能解决处理所有异常。两者的区别只是有受检异常时在编译期处理的异常数量更多，无受检异常时在编译期处理的异常更少。但例如空指针这种异常还是照抛不误
> * 往往Java开发人员捕获异常后不做任何处理，只是做一个打印操作。这样只是欺骗自己，实际上对于程序的健壮性并没有任何的好处。
> * 异常一步步往上抛，结果顶层要处理太多太多异常。如果顶层不做处理那和没有受检异常实际上是一样的
> * 所以kotlin去掉受检异常还是有一定道理的
>
> ***

## copy from past files

> ### 阿里云ECS主机
>
> * ECS简介
>
> > * ECS(Elastic Compute Service) 弹性计算服务
> > *  弹性：可以不用提前固定买死配置，而是根据需要随时扩容、随时增带宽
> > *  ECS涉及资源：实例规格、块存储、镜像、快照、带宽、安全组
>
> * 有关概念
>
> >* 地域和可用区：指ECS实例所在的物理位置
> >* 实例：就是一台虚拟的计算机，包含硬盘、内存、系统等基础部件
> >* 实例规格：指这台计算机的配置
> >* 镜像：指虚拟机运行的操作系统
> >* 块存储：包括基于分布式存储架构的**云盘和共享块存**储，以及基于物理机本地硬盘的**本地存储**
> >* 快照：某一个时间点的数据备份
> >* 安全组：虚拟的防火墙
>
> * 创建实例
>
> >* 按量计费：只有关机了不用才不扣费
> >* 分类：是按照配置来分。如内存型、大数据型、通用型、共享型……
> >* 网络类型
> > * 经典网络
> >   * 一个物理地区的主机的防火墙是统一的，一个破全部都有风险
> >   * IP地址绑定在实例主机上面
> > * 专有网络
> >   * 各个云主机做了隔离，不能连锁入侵破坏
> >   * IP地址配置在网关上，在VPC内还可以实现多实例主机组网
> > * 创建私人专有网络：就是创建一个192.168不能访问互联网的内网，通过网关连接互联网
> >* 弹性公网IP
> > * 作用：用于绑定到未分配公网IP的主机、网关或网卡上
> >* 密钥对
> > * 作用：防止登录工具有后门
> > * 公钥，秘钥：秘钥自己保管，公钥复制到主机上
>
> ***
>
> ### JavaEE第一节课
>
> * 1.1 ORM
>   * 全自动生成SQL语句的框架反而不是特别好，后期数据库优化调优很困难。
>
>
> * 1.2 面向对象五大原则
>
> > * 单一责任原则 - 一个类负责一件事
> > * 开放封闭原则 - 对功能的扩展是开放的，对代码的修改是封闭的 —— **AOP**
> > * 里式替换原则 - 当你在程序当中用了一个类，我可以用这个类的子类来替代它而不产生任何影响。 —— **多态**
> > * 接口分离原则 - 通过接口把一些不相关的功能分离出来，类似于单一责任原则
> > * 依赖倒置原则 - 具体的东西应该依赖于抽象的 —— **IOC**
>
> * 1.3 面向抽象编程
>
> > ```java
> > Rectangle rectangle =new Rectangle(); //方式一
> > Shape rectangle =new Rectangle(); //方式二
> > ```
> >
> > * 方式二更好
>
> * 1.4 注解实现原理
>   * 每一个自定义注解都有负责处理的Handler，在这个handler里面利用反射来处理相应的逻辑
>
> ***
>
> ### 嵌入式软件系统第一节课
>
> * 1.1 什么是嵌入式芯片与软件
>
> > * 用在非通用计算机，例如安防监控，移动基站，仪器仪表等的处理器芯片
> > * 用在嵌入式设备上的软件
> > * 嵌入式计算机系统要嵌入到对象体系中，实现的是对象的智能化控制，因此，它有着与通用计算机系统完全不同的技术要求和发展方向（嵌入式性能，控制能力和控制可靠性）
> > * 嵌入式软件通常暂时不变，因此也被称作**固件**
>
> * 1.2什么是嵌入式系统
>   * 是一种完全嵌入在受控器件内部，为特定的应用而设计的专用计算机系统。
>
>
> * 1.3 嵌入式处理能力
>   * 目前嵌入式处理器的能力不一定比通用计算机弱，很可能更强。比如军工方面的边缘处理器
>
>
> * 1.4嵌入式系统举例
>
> > * 打印机共享器 
> >   * 一边是串口，一边是网口
> >   * 内嵌微处理器+软件
> >   * **吞吐量**、**响应** 最重要
> >   * 易测性 
> >     * 高低温、碰撞、续航等专业性测试
> >     * 并发测试
> > * 无线条形码扫描仪
> >   * **能耗**问题是关键
> > * 地下油罐监视器
> >   * **成本**（性能，处理器）问题很重要
> > * 智能灭火器压力检测
>
> * 1.5 FPGA可编程逻辑器件
>
> > * 设计硬件不只要硬件工程师，也需要软件写代码
> > * 给一块空板。与门非门自己写，自己控制
>
> * 1.6 嵌入式微处理器
>
> > * 嵌入式微处理器又叫做单片机
> > * 一般以某一种微处理器内核为核心，芯片内部集成一大串东西
>
> * 1.7 嵌入式系统的典型硬件
>
> > * 必须要有微处理器和内存，现在的系统通常还有**串行接口**和**网络接口**
> > * 一般没有键盘，显示屏，磁盘驱动器
>
> * 2.1 术语
>
> > * 芯片 - 就是芯片
> > * 引脚 - 芯片两侧或四周的外面连接部分
> > * 说明书
> > * 原理图和PCB图
> > * 印刷电路板
>
> ***

