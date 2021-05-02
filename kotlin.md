# 学习Kotlin

## 基本类型

> ### 前述
>
> * Kotlin同Java一样，一切皆对象
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
> * 以上面举例，此Student对象有两种初始化方式，分别是传入(name, age)，另一种是只传入age
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

