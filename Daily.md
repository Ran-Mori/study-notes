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
> * `RawEncoding`：相比标准减少了padding，遇到不是3倍数时才用移位
> * `UrlEncoding`：`+ -> -`，`/ -> _`
> * `文本传输`：http以文本传输，要用hex或者base64编码。内容为文本，自带描述信息(参数名)
>   * `二进制传输`：tcp以二进制传输，要用字符编码。内容为二进制，以预先定义好的格式拼接在一起
>
> ***

