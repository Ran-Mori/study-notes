# 学习Kotlin

## 基本类型

> ### 前述
>
> * Kotlin同Java一样，一切皆对象
> * kotlin百分之百兼容Java，能使用Java的所有框架
> * 因此kotlin可以说是继承了Java的所有财富，而自己本省更加的优秀
>
> ### kotlin历史
>
> * kotlin是由jetbrains公司设计开发的语言‘
> * 在发布第一个版本后就被谷歌推荐为Android开发的首选语言
> * 之所以jetbrains能够以一个第三方的身份设计出Android的开发语言，是因为JVM只认class文件，而对于class文件从何而来根本不care，因此kotlin可以认为是Java的语法糖
>
> ### 数字
>
> * 声明：`val x:Byte = 4`，类型有`Byte, Short, Int, Long, Float, Double`
> * 浮点数默认是`Double`，如果要声明`Float`必须显示地末尾添加`f`
> * 注意：Kotlin不像Java有数字**隐式拓宽转换**，要转换就显示地转，即`Double`参数不允许传入的参数是`Float`，不允许把`Byte`的值赋值为`Int`类型的变量
> * Kotlin数字支持下划线方便阅读，如：`val x:Int = 0xFF_2B_1D`
> * 上面提到的都是原生基本类型，如果要变成引用类型会发生自动装箱。如：`val x:Int? = b`。同Java一样，数字做比较会自动拆箱以保证相等性
> * Kotlin不支持数字隐式类型转换，但可以显示地做转换。提供了很多如`toInt()`的方法
> * 位操作不支持运算符，而是要显示地函数调用
>
> ### 字符
>
> * 字符不能直接当数字用
> * 当需要非空引用时也会发生装箱操作
>
> ### 布尔值
>
> * 当需要非空引用时也会发生装箱操作
>
> ### 数组
>
> * 直接初始化值`arrayOf()`
> * 创建一个指定大小的数组`Array<String>(5){it.toString()}`，必须显示地赋初始值
> * 原生不装包数组类型`IntArray(5),ByteArray(3)`
>
> ### 字符串
>
> * 和Java一样，字符串是不可变的
> * 尽量使用字符串模板而不是字符串拼接
> * 字符串模板：`${}`里面的内容会自动被解析成代码执行
>
> ***

## 包与导入

> * 和Java一模一样
>
> ***

## 控制流

> ### if
>
> * 既可以当控制流控制流程结构，也可以做表达式
> * 做表达式时语句块中还可以包含其他代码，只要语句块最后一句代码是值就行
>
> ### when
>
> * 取代了swich表达式
>
> * 可以流程控制也可以表达式
>
>   ```kotlin
>   when (x) {
>       parseInt(x) -> print("s encodes x")
>       in 1..10 -> print("x is in the range")
>       in validNumbers -> print("x is valid")
>       !in 10..20 -> print("x is outside the range")
>       else -> print("none of the above")
>   }
>   ```
>
> * 使用`->`符号，最后一般有个else(如果作为表达式必须有)
>
> * 当when后面不接判断的输入时，就是一个if-else if链条，顺序遍历一个为TRUE时执行，否则执行最后的else
>
> ### For
>
> * 使用举例
>
>   ```kotlin
>   for(item:Int in items)
>   for(i in 1..100)
>   for((index,value) in array.withIndex())
>   ```
>
> ### while循环
>
> * 和Java一样
>
> ***

## 返回与跳转

> ### 返回到标签
>
> * 和Java一样支持返回到标签
> * 只不过Kotlin的lambda表达式支持得更好，常常lamba表达式需要返回，因此标签的使用可能更多
>
> ***

## 类与继承

> ### 主构造函数
>
> * 示例
>
>   ```kotlin
>   class Student public @Inject constructor(
>       var name: String,
>       var age: Int,
>       var sex:Boolean
>   ){
>       ...
>   }
>   //省略后
>   class Student (var name: String,var age: Int,var sex:Boolean){
>       //...
>   }
>   ```
>
> * 当既无访问权限修饰符，也无注解时，`constructor`关键字可以进行省略
>
> * 主构造函数不能有任何的代码，初始化代码可以放在`init{}`块中
>
> ### 次构造函数
>
> * 次构造函数可以直接或间接地委托给主构造函数。委托必须满足主构造函数全部定义，即至少参数个数必须全
>
> * 示例
>
>   ```kotlin
>   class Student (var name: String,var age: Int){
>       init {
>           this.name = name
>           this.age = age
>       }
>       constructor(name: String) :this(name,30){
>
>       }
>   }
>   ```
>
> * 执行顺序：首先`init块`，其次`次构委托给主构部分`,最后`次构造`
>
> * 以上面举例，此Student对象有两种初始化方式，分别是传入(name, age)，另一种是只传入age
>
> ### 构造函数理解整体举例
>
> ```kotlin
> class Student(var sno: String, var grade: Int, name: String, age: Int): Person(name,age){
>     constructor(name: String,age: Int): this("", 0, name, age){
>         println("次构造函数一被调用")
>     }
>     constructor():this("", 0){
>         println("次构造函数二别调用")
>     }
> }
> ```
>
> * 注意`name: Stirng, age: Int`不能添加`val`和var ，因为如果加了就等于是声明称成员变量，就会覆盖父类。这里的意思只是传递给父类构造函数，并不是想覆盖，因此不加
> * 当执行`val student = Student("",0,"",0)`时执行的是主构造函数
> * 当执行`val student = Student(name = "name",age = 3)`，会先调用主构造函数，在调用次构造函数一
> * 当执行`val student = Student()`会先调用主构造函数，在调用次构造函数一，在调用次构造函数二
> * 但是次构造函数一般不会使用，因为kotlin的参数支持默认值，有了默认值后基本不会使用次构造函数
>
> ### 创建对象
>
> * kotlin里面没有new关键字
> * 调用构造函数创建对象就和调用普通函数一模一样，如`val student: Student = Student("n",2)`
>
> ### 继承
>
> * 默认都继承自`Any`
> * 默认自定义类都是final，如要开放继承需要加`open`关键字
>
> ### 属性和方法重载
>
> * kotlin十分重视显示关系，讨厌隐式关系
> * 因此如要要重载父类必须前面加`open`，子类前面必须加`override`
>
> ### 覆盖规则
>
> * 如果一个类从它的直接超类继承相同成员的多个实现， 它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）
> * 使用`super<base>`来区分是那个父类
> * 即不允许产生歧义，但这种情况本来就应该很少遇到才对
>
> ***

## 属性与字段

> ### 属性
>
> * var和val的一大区别是var既有getter也有setter，而val只有getter没有setter
>
> * kotlin可以实现类似于C#的计算属性
>
>   ```kotlin
>   var num: Int = 0
>   var isOdd: Boolean
>       get() = this.isOdd
>       set(value) {
>           num % 2 == 1
>   }
>   ```
>
> * 且getter和setter支持访问权限控制和注解
>
> ***

## 接口

> ### 接口
>
> * 和Java一样
> * 只不过支持getter而已
> * kotlin的接口支持默认实现，其实Java8以后也支持，即给抽象方法一个默认实现，在子类实现接口时可以选择重写也可以选择不重写
>
> ### 函数式接口(SAM)
>
> * SAM：single abstract method
>
> * 定义：只有一个抽象方法的接口。即可以有多个非抽象成员，但抽象成员只能有一个
>
> * 声明
>
>   ```kotlin
>   fun interface SAM{
>       fun test(num: Int): Int
>   }
>   ```
>
> * 使用lamba表达式方便创建实例举例
>
>   ```kotlin
>   fun interface IntPredicate {
>      fun accept(i: Int): Boolean
>   }
>               
>   val isEven = IntPredicate { it % 2 == 0 }
>               
>   fun main() {
>      println("Is 7 even? - ${isEven.accept(7)}")
>   }
>   ```
>
> ***

## 可见性修饰符

> ### 四种类型
>
> * public，protected，internal，private
>
> ### internal
>
> * 次声明在同一个模块下保持可见性
>
> ### 方法位置
>
> * 和Java不一样，kotlin直接允许方法定义在package下
>
> ### 模块
>
> * 模块是编译在一起的一套kotlin文件
>
> ### 构造函数
>
> * kotlin构造函数也能有修饰符
>
> ***

## 拓展函数

> ### 功能
>
> * 不必修改一个类的前提下拓展次类的属性和方法
>
> ### Java对比
>
> * 在Java中想去拓展一个类的行为时，一把都是通过创建一个Util类，在类中创建一个静态方法，并且此静态方法的第一个参数一般是想要拓展类的实例
> * 但kotlin的拓展函数其实更符合面向对象的思维。虽然它说白了其实也是一种语法糖，在JVM层是使用的静态分配即在编译期就确定了函数的调用版本。但至少从使用层面来说它是十分符合面向对象的思维的
>
> ### 做法
>
> * 声明方法时指明方法的接收者，在方法中使用this代替接收者
>
>   ```kotlin
>   fun <T> MutableList<T>.swap(index1: Int, index2: Int) {}
>   ```
>
> ### 静态解析
>
> * 拓展并没有真正地去改变这个类，而是在调用时做静态解析，即编译期就确定其调用行为
>
> ### 定义位置
>
> * kotlin并没有规定拓展函数一定要定义在那里
> * 不过一般建议把kotlin函数定义成顶层函数，因为这样无论在什么地方都能使用
> * 而且建议重新创建一个`拓展类名.kt`的文件，不然你根不不知道扩展函数定义在那里
>
> ### 可空接收者
>
> * 接收者甚至可以为空，即声明接收者为`Int?`类型
>
> ***

## 数据类

> ### 特点
>
> * 只保存数据的类
> * 自动生成getter，setter，hashCode，toString，equal方法
>
> ### 声明
>
> ```kotlin
> data class User(val name: String, val age: Int)
> ```
>
> ***

## 单例类

> ### 概述
>
> * kotlin中的单例类有编写简单，可以当静态方法用的特点
> * 因此kotlin这门语言极度弱化静态方法，当需要使用工具类时一般也是推荐使用单例类
> * 单例类的方法可以像静态方法一样正常使用，可以说是非常强大
> * kotlin使用一行代码写出来的单例其实和Java的双锁检测单例模式的安全性是一样的
>
> ### 声明
>
> ```kotlin
> object Util{
>     fun do(){
>         
>     }
> }
> 
> //调用
> Util.do()
> ```
>
> 

## 集合

> ### 不可变集合
>
> * 所有的类似于`listOf()`这种方法全是不可变集合，即无法对集合进行增删改。使用`listof()`只是简化了初始化的过程，是一种语法糖
>
> ### 可变集合
>
> * 所有的类似于`mutabeSetOf()`全是可变集合，这种集合就和普通正常使用的Java集合相差无几了。只是对初始化过程有所简化，想`listOf()`一样提供了语法糖
>
> ### map
>
> * map初始化过程`map = mutableMapOf("Apple" to 1)`，这里的`to`不是关键字，而是一个`infix`函数

## Lamba表达式

> ### 实质
>
> * 就是一段包含参数和执行体的代码当做参数进行传递
>
> ### 化简过程
>
> ```kotlin
> val length = list.maxBy({fruit: String -> fruit.length})
> 
> //lamba表达式作为最后一个参数可以写出括号
> val length = list.maxBy(){fruit: String -> fruit.length}
> 
> //只有一个参数时，括号可以省略
> val length = list.maxBy{fruit: String -> fruit.length}
> 
> //类型自动推断系统化很强，类型可以省略
> val length = list.maxBy(){fruit -> fruit.length}
> 
> //只有一个参数时，可以用it代替
> val length = list.maxBy{it.length}
> ```
>
> ### Java函数式API使用
>
> * 使用条件：在kotlin中调用Java方法，该方法接收一个接口作为参数，且该接口是单抽象接口
>
> * 举例
>
>   ```java
>   //Java写法，实际上是使用了匿名类
>   button.setOnClickListener(new View.OnClickListner(){
>       @Override
>       public void onClick(View v){
>           //代码逻辑
>       }
>   })
>   ```
>
>   ```kotlin
>   //复杂写法，实际上也是匿名类
>   button.setOnclickListner(object: View.OnClickListner{
>       override fun onClick(v: View){
>           //代码逻辑
>       }
>   })
>         
>   //上面很多东西都是多余的，去掉多余东西。由于只有这一个方法，因此重写的肯定是它，因此函数名不用写
>   button.setOnClickListner(View.OnClickLinstner{
>       //代码逻辑
>   })
>         
>   //根据lamba表达式化简原则，移到括号外面，去除括号
>   button.setOnClickListner{
>       //代码逻辑
>   }
>   ```
>
> ***

## 空指针检查

> ### 实现原理
>
> * 在编译期就进行空指针检查，将空指针检查提前到编译期
> * 不带?的类型是根本不可能为空的，因此可以不检查。凡是带?的就有可能会为空，编译期使用时就必须进行检查
>
> ### 判空辅助工具
>
> * `?.` ：如果不为空就执行，如果为空就什么都不执行
> * `?:` 如果左边不为空就取左边，如果左边为空就取右边。如`list?.length?:-1`
>
> ### 强制忽略编译
>
> * `!! `：两个感叹号表示让编译期不做空指针检查，但很可能程序员认为的不可能空指针正是有可能出现空指针的时候，不能对自己过于自信
>
> ### let函数
>
> * 使用举例
>
>   ```kotlin
>   fun doStudy(student: Student?){
>       study?.let{
>           it.readBook()
>           it.doHomeWork()
>       }
>   }
>   ```
>
> * 解释：即把let函数体包含study的上下文
>
> ***

## 标准函数

> ### 何为标准函数
>
> * 定义在`Standard.kt`文件中的几个函数，是任何kotlin对象都可以自由调用的标准函数
>
> ### with
>
> ```kotlin
> val result = with(obj){
>     //obj的上下文
>     "value" //返回值
> }
> ```
>
> * 即`{}`中的代码隐式包含了对象obj，且最后一行为返回值
>
> ### run
>
> ```kotlin
> val result = obj.run{
>     //obj的上下文
>     "value"//返回值
> }
> ```
>
> ### apply
>
> ```kotlin
> val result = obj.apply{
>     //obj的上下文
> }
> println(obj.toString())
> ```
>
> * apply相比于上面两个的区别就是没有返回值，只提供了一个上下文环境
>
> ### 静态方法
>
> * kotlin中极度弱化静态方法，因为kotlin中的单例类能够很好地提供类似于静态方法的功能，因此实在没有太大的必要去实现静态方法
> * 但单例类有一个缺点就是所有的方法都是静态的，如果希望一部分静态而另一部分非静态就不能实现我们的目的
> * 但kotlin也支持类似于Java的静态类的使用，只不过语法稍微有些变化而已
>
> ### 伴生类
>
> * 定义
>
>   ```kotlin
>   class Student{
>       companion object{
>           fun doSomething(){
>               //代码逻辑
>           }
>       }
>   }
>   ```
>
> * 伴生类实际上就是一个类，JVM会保证永远只存在一个伴生对象，就有点类似于单例类的感觉。因此调用伴生类的方法就可以不用创建对象直接调用
>
> * 但这其实只是kotlin的语法糖而已，实际上还是通过对象点.方法的形式调用了方法，只不过这个对象在全局只有一个而已
>
> * 因此这不是真正的JVM层面的静态方法，只能说是披着静态方法外衣的单例对象方法
>
> ### 顶层方法
>
> * 只有kotlin中才有顶层方法的概念，Java中没有顶层方法的概念
> * 顶层方法在JVM层面就会被编译成传统Java意义上的静态方法
> * kotlin中直接声明在包下面，类外的方法就是顶层方法；而Java的方法只允许声明在类中
> * kotlin不管包路径啥都不管，顶层方法无论在那里都能直接随便调用。
> * Java中无法直接调用kotlin的顶层方法，只有通过`类名.方法名`，像调用静态方法，实际上就是静态方法那样调用
>
> ***

## 延迟加载

> ### 诞生原因
>
> * 由于kotlin这门语言是禁止在编译期出现空指针的
> * 因此对于我们很多的类的成员变量，我们其实每次使用的时候都会进行初始化，它是不会为空的
> * 但是由于kotlin的语法规则限制，每次使用都要进行`?.`的判断
> * 当全局变量数量非常非常大时，这种方法不太好
> * 因此有了延迟加载
>
> ### 写法
>
> ```kotlin
> private lazyinit var num:Int
> ```
>
> * 声明成了延迟加载后，不用对其进行初始化(一般就是赋值为null)
> * 且`:`后面的类型可以不加`?`，因为延迟加载就是认为其不会为空
> * 既然不是空对象，因此编译期就可以随便用了
>
> ### 依然可能出错
>
> * 当使用延迟加载对象但并未对其进行初始化时，就会报空指针异常
> * 因此使用延迟加载对象时一定要对其进行初始化
>
> ***

## 密封类

> ### 出现原因
>
> * 对于`when()`这种判断语句，一定要在最后加上一句`else`，否则就会报错
> * 但是有些情况我们可以肯定出现的情况可能性一定就只有这几种，不可能会出现其他情况。但由于kotlin的语法规则我们依然要写`else`部分
> * 使用密封类就能有效避免这个问题，会在编译期对其所有的可能性做一个检查
>
> ### 写法
>
> ```kotlin
> sealed class Result
> class Success(val msg: String): Result()
> class Failure(val err: Exception):Result()
> ```
>
> * `sealed class`是一个类而不是接口，因此继承的时候要加()代表无参主构造方法
>
> ### when语句
>
> ```kotlin
> when(result){
>     is Success -> result.msg
>     is Failure -> result.err.toString()
> }
> ```
>
> * 不用在写else
>
> ***

## 运算符重载

> ### 概述
>
> * 就是运算符重载，使用得好往往能有非常奇妙的效果
> * 多的不说，直接举个例子就懂了
>
> ### 举例
>
> ```kotlin
> //StringExtend.kt
> 
> //运算法重载 + 拓展函数
> operator fun String.times(n: Int):String{
>     val builder = StringBuilder()
>     repeat(n){
>         builder.append(this)
>     }
>     return builder.toString()
> }
> 
> fun String.getTimes(n: Int):String{
>     return this.times(n)
> }
> 
> class StringExtend {}
> ```
>
> 