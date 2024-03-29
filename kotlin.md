# 学习Kotlin

## 基本类型

### 前述

* Kotlin同Java一样，一切皆对象
* kotlin百分之百兼容Java，能使用Java的所有框架
* 因此kotlin可以说是继承了Java的所有财富，而自己本身更加的优秀

### kotlin历史

* kotlin是由jetbrains公司设计开发的语言
* 在发布第一个版本后就被谷歌推荐为Android开发的首选语言
* 之所以jetbrains能够以一个第三方的身份设计出Android的开发语言，是因为JVM只认class文件，而对于class文件从何而来根本不care，因此kotlin可以认为是Java的语法糖

### 数字

* 声明：`val x:Byte = 4`，类型有`Byte, Short, Int, Long, Float, Double`
* 浮点数默认是`Double`，如果要声明`Float`必须显示地末尾添加`f`
* 注意：Kotlin不像Java有数字**隐式拓宽转换**，要转换就显示地转，即`Double`参数不允许传入的参数是`Float`，不允许把`Byte`的值赋值为`Int`类型的变量
* Kotlin数字支持下划线方便阅读，如：`val x:Int = 0xFF_2B_1D`
* kotlin数字声明一般编译后都是对应java的基本数据类型。但当声明类型变成可空类型或者泛型会发生自动装箱，可空很好理解，泛型是因为类型会被擦除变成`Object`所以也会自动装箱
* Kotlin不支持数字隐式类型转换，但可以显示地做转换。提供了很多如`toInt()`的方法
* 位操作不支持运算符，而是要显示地函数调用

### 字符

* 字符不能直接当数字用
* 当需要非空引用时也会发生装箱操作

### 布尔值

* 当需要非空引用时也会发生装箱操作

### 数组

* 直接初始化值`arrayOf()`
* 创建一个指定大小的数组`Array<String>(5){it.toString()}`，必须显示地赋初始值
* 原生不装包数组类型`IntArray(5)、ByteArray(3)`

### 字符串

* 和Java一样，字符串是不可变的
* 尽量使用字符串模板而不是字符串拼接
* 字符串模板：`${}`里面的内容会自动被解析成代码执行

***

## 包与导入

* 和Java一模一样

***

## 控制流

### if

* 既可以当控制流控制流程结构，也可以做表达式
* 做表达式时语句块中还可以包含其他代码，只要语句块最后一句代码是值就行

### when

* 取代了swich表达式

* 可以流程控制也可以表达式

  ```kotlin
  when (x) {
      parseInt(x) -> print("s encodes x")
      in 1..10 -> print("x is in the range")
      in validNumbers -> print("x is valid")
      !in 10..20 -> print("x is outside the range")
      else -> print("none of the above")
  }
  ```

* 使用`->`符号，最后一般有个else(如果作为表达式必须有)

* 当when后面不接判断的输入时，就是一个if-else if链条，顺序遍历一个为TRUE时执行，否则执行最后的else

### For

* 使用举例

  ```kotlin
  for(item:Int in items)
  for(i in 1..100)
  for((index,value) in array.withIndex())
  for(i in 0 until array.length())
  ```

### while循环

* 和Java一样

***

## 返回与跳转

### 返回到标签

* 和Java一样支持返回到标签
* 只不过Kotlin的lambda表达式支持得更好，常常lamba表达式需要返回，因此标签的使用可能更多

***

## 类与继承

### 主构造函数

* 示例

  ```kotlin
  class Student public @Inject constructor(
      var name: String,
      var age: Int,
      var sex:Boolean
  ){
      ...
  }
  //省略后
  class Student (var name: String,var age: Int,var sex:Boolean){
      //...
  }
  ```

* 当既无访问权限修饰符，也无注解时，`constructor`关键字可以进行省略

* 主构造函数不能有任何的代码，初始化代码可以放在`init{}`块中

### 次构造函数

* 次构造函数可以直接或间接地委托给主构造函数。委托必须满足主构造函数全部定义，即至少参数个数必须全

* 示例

  ```kotlin
  class Student (var name: String,var age: Int){
      init {
          this.name = name
          this.age = age
      }
      constructor(name: String) :this(name,30){
  
      }
  }
  ```

* 执行顺序：首先`init块`，其次`次构委托给主构部分`,最后`次构造本身`

* 以上面举例，此Student对象有两种初始化方式，分别是传入(name, age)，另一种是只传入age

### 构造函数理解整体举例

```kotlin
class Student(var sno: String, var grade: Int, name: String, age: Int): Person(name,age){
    constructor(name: String,age: Int): this("", 0, name, age){
        println("次构造函数一被调用")
    }
    constructor():this("", 0){
        println("次构造函数二别调用")
    }
}
```

* 注意`name: Stirng, age: Int`不能添加`val`和var ，因为如果加了就等于是声明称成员变量，就会覆盖父类。这里的意思只是传递给父类构造函数，并不是想覆盖，因此不加
* 当执行`val student = Student("",0,"",0)`时执行的是主构造函数
* 当执行`val student = Student(name = "name",age = 3)`，会先调用主构造函数，在调用次构造函数一
* 当执行`val student = Student()`会先调用主构造函数，在调用次构造函数一，在调用次构造函数二
* 但是次构造函数一般不会使用，因为kotlin的参数支持默认值，有了默认值后基本不会使用次构造函数

### 创建对象

* kotlin里面没有new关键字
* 调用构造函数创建对象就和调用普通函数一模一样，如`val student: Student = Student("n",2)`

### 继承

* 默认都继承自`Any`
* 默认自定义类都是final，如要开放继承需要加`open`关键字

### 属性和方法重载

* kotlin十分重视显示关系，讨厌隐式关系
* 因此如要要重载父类必须前面加`open`，子类前面必须加`override`

### 覆盖规则

* 如果一个类从它的直接超类继承相同成员的多个实现， 它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）
* 使用`super<base>`来区分是那个父类
* 即不允许产生歧义，但这种情况本来就应该很少遇到才对

***

## 属性与字段

### 属性

* var和val的一大区别是var既有getter也有setter，而val只有getter没有setter

* kotlin可以实现类似于C#的计算属性

  ```kotlin
  var num: Int = 0
  var isOdd: Boolean
      get() = this.isOdd
      set(value) {
          num % 2 == 1
  }
  ```

* 且getter和setter支持访问权限控制和注解

***

## 接口

### 接口

* 和Java一样
* 只不过支持getter而已
* kotlin的接口支持默认实现，其实Java8以后也支持，即给抽象方法一个默认实现，在子类实现接口时可以选择重写也可以选择不重写

### 函数式接口(SAM)

* SAM：single abstract method

* 定义：只有一个抽象方法的接口。即可以有多个非抽象成员，但抽象成员只能有一个

* 声明

  ```kotlin
  fun interface SAM{
      fun test(num: Int): Int
  }
  ```

* 使用lamba表达式方便创建实例举例

  ```kotlin
  fun interface IntPredicate {
     fun accept(i: Int): Boolean
  }
  
  val isEven = IntPredicate { it % 2 == 0 }
  
  fun main() {
     println("Is 7 even? - ${isEven.accept(7)}")
  }
  ```

***

## 可见性修饰符

### 四种类型

* public，protected，internal，private

### internal

* 次声明在同一个模块下保持可见性

### 方法位置

* 和Java不一样，kotlin直接允许方法定义在package下

### 模块

* 模块是编译在一起的一套kotlin文件

### 构造函数

* kotlin构造函数也能有修饰符

***

## 拓展函数

### 功能

* 不必修改一个类的前提下拓展次类的属性和方法

### Java对比

* 在Java中想去拓展一个类的行为时，一把都是通过创建一个Util类，在类中创建一个静态方法，并且此静态方法的第一个参数一般是想要拓展类的实例
* 但kotlin的拓展函数其实更符合面向对象的思维。虽然它说白了其实也是一种语法糖，在JVM层是使用的静态分配即在编译期就确定了函数的调用版本。但至少从使用层面来说它是十分符合面向对象的思维的

### 做法

* 声明方法时指明方法的接收者，在方法中使用this代替接收者

  ```kotlin
  fun <T> MutableList<T>.swap(index1: Int, index2: Int) {}
  ```

### 静态解析

* 拓展并没有真正地去改变这个类，而是在调用时做静态解析，即编译期就确定其调用行为
* 具体实现原理是在这个类中加入了一个静态方法，这个静态方法的第一个参数是这个类本身的一个引用

### 定义位置

* kotlin并没有规定拓展函数一定要定义在那里
* 不过一般建议把kotlin函数定义成顶层函数，因为这样无论在什么地方都能使用
* 而且建议重新创建一个`拓展类名.kt`的文件，不然你根不不知道扩展函数定义在那里

### 可空接收者

* 接收者甚至可以为空，即声明接收者为`Int?`类型

***

## 数据类

### 特点

* 只保存数据的类
* 自动生成getter，setter，hashCode，toString，equal方法

### 声明

```kotlin
data class User(val name: String, val age: Int)
```

***

## 单例类

### 概述

* kotlin中的单例类有编写简单，可以当静态方法用的特点
* 因此kotlin这门语言极度弱化静态方法，当需要使用工具类时一般也是推荐使用单例类
* 单例类的方法可以像静态方法一样正常使用，可以说是非常强大
* kotlin使用一行代码写出来的单例其实和Java的双锁检测单例模式的安全性是一样的

### 声明

```kotlin
object Util{
    fun do(){

    }
}

//调用
Util.do()
```

***

## 集合

### 不可变集合

* 所有的类似于`listOf()`这种方法全是不可变集合，即无法对集合进行增删改。使用`listof()`只是简化了初始化的过程，是一种语法糖

### 可变集合

* 所有的类似于`mutabeSetOf()`全是可变集合，这种集合就和普通正常使用的Java集合相差无几了。只是对初始化过程有所简化，想`listOf()`一样提供了语法糖

### map

* map初始化过程`map = mutableMapOf("Apple" to 1)`，这里的`to`不是关键字，而是一个`infix`函数

***

## Lamba表达式

### 实质

* 就是一段包含参数和执行体的代码当做参数进行传递

### 化简过程

```kotlin
val length = list.maxBy({fruit: String -> fruit.length})

//lamba表达式作为最后一个参数可以写出括号
val length = list.maxBy(){fruit: String -> fruit.length}

//只有一个参数时，括号可以省略
val length = list.maxBy{fruit: String -> fruit.length}

//类型自动推断系统化很强，类型可以省略
val length = list.maxBy(){fruit -> fruit.length}

//只有一个参数时，可以用it代替
val length = list.maxBy{it.length}
```

### Java函数式API使用

* 使用条件：在kotlin中调用Java方法，该方法接收一个接口作为参数，且该接口是单抽象接口

* 举例

  ```java
  //Java写法，实际上是使用了匿名类
  button.setOnClickListener(new View.OnClickListner(){
      @Override
      public void onClick(View v){
          //代码逻辑
      }
  })
  ```

  ```kotlin
  //复杂写法，实际上也是匿名类
  button.setOnclickListner(object: View.OnClickListner{
      override fun onClick(v: View){
          //代码逻辑
      }
  })
  
  //上面很多东西都是多余的，去掉多余东西。由于只有这一个方法，因此重写的肯定是它，因此函数名不用写
  button.setOnClickListner(View.OnClickLinstner{
      //代码逻辑
  })
  
  //根据lamba表达式化简原则，移到括号外面，去除括号
  button.setOnClickListner{
      //代码逻辑
  }
  ```

***

## 空指针检查

### 实现原理

* 在编译期就进行空指针检查，将空指针检查提前到编译期
* 不带?的类型是根本不可能为空的，因此可以不检查。凡是带?的就有可能会为空，编译期使用时就必须进行检查

### 判空辅助工具

* `?.` ：如果不为空就执行，如果为空就什么都不执行
* `?:` 如果左边不为空就取左边，如果左边为空就取右边。如`list?.length?:-1`

### 强制忽略编译

* `!! `：两个感叹号表示让编译期不做空指针检查，但很可能程序员认为的不可能空指针正是有可能出现空指针的时候，不能对自己过于自信

### let函数

* 使用举例

  ```kotlin
  fun doStudy(student: Student?){
      study?.let{
          it.readBook()
          it.doHomeWork()
      }
  }
  ```

* 解释：即把let函数体包含study的上下文

***

## 标准函数

### 何为标准函数

* 定义在`Standard.kt`文件中的几个函数，是任何kotlin对象都可以自由调用的标准函数

### with

```kotlin
val result = with(obj){
    //obj的上下文
    "value" //返回值
}
```

* 即`{}`中的代码隐式包含了对象obj，且最后一行为返回值

### run

```kotlin
val result = obj.run{
    //obj的上下文
    "value"//返回值
}
```

### apply

```kotlin
val result = obj.apply{
    //obj的上下文
}
println(obj.toString())
```

* apply相比于上面两个的区别就是没有返回值，只提供了一个上下文环境
* 其实也不是没有返回值，它的返回值默认就是自己本身

### 静态方法

* kotlin中极度弱化静态方法，因为kotlin中的单例类能够很好地提供类似于静态方法的功能，因此实在没有太大的必要去实现静态方法
* 但单例类有一个缺点就是所有的方法都是静态的，如果希望一部分静态而另一部分非静态就不能实现我们的目的
* 但kotlin也支持类似于Java的静态类的使用，只不过语法稍微有些变化而已

### 伴生类

* 定义

  ```kotlin
  class Student{
      companion object{
          fun doSomething(){
              //代码逻辑
          }
      }
  }
  ```

* 伴生类实际上就是一个类，JVM会保证永远只存在一个伴生对象，就有点类似于单例类的感觉。因此调用伴生类的方法就可以不用创建对象直接调用

* 但这其实只是kotlin的语法糖而已，实际上还是通过对象点.方法的形式调用了方法，只不过这个对象在全局只有一个而已

* 因此这不是真正的JVM层面的静态方法，只能说是披着静态方法外衣的单例对象方法

### 顶层方法

* 只有kotlin中才有顶层方法的概念，Java中没有顶层方法的概念
* 顶层方法在JVM层面就会被编译成传统Java意义上的静态方法
* kotlin中直接声明在包下面，类外的方法就是顶层方法；而Java的方法只允许声明在类中
* kotlin不管包路径啥都不管，顶层方法无论在那里都能直接随便调用。
* Java中无法直接调用kotlin的顶层方法，只有通过`类名.方法名`，像调用静态方法，实际上就是静态方法那样调用

***

## 延迟加载

### 诞生原因

* 由于kotlin这门语言是禁止在编译期出现空指针的
* 因此对于我们很多的类的成员变量，我们其实每次使用的时候都会进行初始化，它是不会为空的
* 但是由于kotlin的语法规则限制，每次使用都要进行`?.`的判断
* 当全局变量数量非常非常大时，这种方法不太好
* 因此有了延迟加载

### 写法

```kotlin
private lazyinit var num:Int
```

* 声明成了延迟加载后，不用对其进行初始化(一般就是赋值为null)
* 且`:`后面的类型可以不加`?`，因为延迟加载就是认为其不会为空
* 既然不是空对象，因此编译期就可以随便用了

### 依然可能出错

* 当使用延迟加载对象但并未对其进行初始化时，就会报空指针异常
* 因此使用延迟加载对象时一定要对其进行初始化

***

## 密封类

### 出现原因

* 对于`when()`这种判断语句，一定要在最后加上一句`else`，否则就会报错
* 但是有些情况我们可以肯定出现的情况可能性一定就只有这几种，不可能会出现其他情况。但由于kotlin的语法规则我们依然要写`else`部分
* 使用密封类就能有效避免这个问题，会在编译期对其所有的可能性做一个检查

### 写法

```kotlin
sealed class Result
class Success(val msg: String): Result()
class Failure(val err: Exception):Result()
```

* `sealed class`是一个类而不是接口，因此继承的时候要加()代表无参主构造方法

### when语句

```kotlin
when(result){
    is Success -> result.msg
    is Failure -> result.err.toString()
}
```

* 不用在写else

***

## 运算符重载

### 概述

* 就是运算符重载，使用得好往往能有非常奇妙的效果
* 多的不说，直接举个例子就懂了

### 举例

```kotlin
//StringExtend.kt

//运算法重载 + 拓展函数
operator fun String.times(n: Int):String{
    val builder = StringBuilder()
    repeat(n){
        builder.append(this)
    }
    return builder.toString()
}

fun String.getTimes(n: Int):String{
    return this.times(n)
}

class StringExtend {}
```

***

## 高阶函数

### 定义

* 参数或者返回值是函数的函数称为高阶函数
* 其实`高阶`这个名词真的很形象了，就是高阶函数嘛

### 函数参数声明

```kotlin
fun test(num1: Int,num2: Int,operation:(Int,Int) -> Int):Int{
    return operation(num1,num2)
}
```

* 即`operation:(Int,Int) -> Int`

### 函数参数声明带调用者

* 有时候某个高阶函数不是直接调用的(像上面一样)，而是有一个调用的接收者
* 这种情况下应该在传入函型参数的时候就声明好函数的调用者

```kotlin
//此处声明了高阶函数的调用者是intent
inline fun <reified T> startActivity(context: Context,block:Intent.() -> Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context.startActivity(intent)
}

//调用
startActivity<MainActivity>(this){
    putExtra("key","value")
}
```

### 函数参数传入

```kotlin
fun add(num1: Int,num2: Int):Int = num1 + num2

val i = myTest(1,2,::add)				
```

* 即`::add`

### 底层实现原理

* 实际上的lamba表达式或者高阶函数中的函数就是一个接口
* 这个接口有一个invoke方法，应该是通过反射来执行这个方法
* 但接口肯定不行，实际上调用的过程中是使用匿名类
* 但这问题就大了，每次使用高阶函数都要动态创建类和对象，性能开销过于巨大
* 为了解决这个问题，可以使用函数内联来实现

### 函数内联

* 作用之一是为了解决高阶函数在JVM层实现的匿名类性能损失
* 解决逻辑：在高阶函数实际调用过程中我把函数体直接传过去，不进行调用过程，那不就把匿名类的性能损失给解决了吗
* 基于此原因，kotlin中的大多数高阶函数都声明成内联形式
* 内联就可以理解成`降阶`

### 声明内联

```kotlin
inline fun myTest(num1: Int,num2: Int,operation:(Int,Int) -> Int):Int{
    return operation(num1,num2)
}
```

* 直接加一个inline就行了

### 内联缺陷

* 内联在运行时无法获取到函数参数的类型
* 因为都已经内联成代码了，那里还知道本来的参数是什么类型

### 内联的返回

* 使用内联是可以直接一步到位返回到本函数的
* 因为实际内联过后就是降阶，降阶过后就是一阶函数，一阶函数返回肯定是本函数直接返回
* 但非内联就不一样了，非内联是二阶甚至更高阶函数，高阶函数内返回肯定只能返回它那一阶，即局部返回

### 无法使用内联的情况

* 当在高阶函数的函数体中使用lambda表达式时，且将此高阶函数的函数参数传入lambda表达式，且此lambda表达式还是被定义成非内联的
* 因为按照道理来说内联情况下参数函数一返回整个高阶函数就要返回，但在函数体的lambda表达式实际上是一个内部类(因为没有内联)，这时在内部类中返回无法返回到高阶函数本身
* 因此这种情况下禁止使用函数内联

### 解决方式

* 使用`crossinline`关键字
* 此关键字的作用就是在编译期检查高阶函数的函数体中的lambda表达式中一定没有return语句
* 只要没有return语句，那无论怎么嵌套其实都是合乎逻辑无伤大雅的
* 一旦有return语句就在编译期进行报错

***

## 泛型

### 概述

* Java的泛型很垃圾
* kotlin的泛型于Java的泛型是有区别的
* 此节先学习它们相同的部分

### 泛型上界

* 泛型不是什么类都可以填进来的，填什么类也是有一定限度的

* 一般的过滤方式是可以在声明时填入泛型的上界，这种情况下只有上界的子类才能填入泛型，其他的泛型填入就会报错

* 举例。此处声明了泛型只能填入Number及其子类

  ```kotlin
  fun <T : Number> myFunction(param: T): T{
      return param
  }
  ```

### 默认上界和可空

* 不添加上界时默认的泛型上界是`Any?`，即默认是可以为空的
* 如果不想为空即可以手动声明函数的上界为`Any`

***

## 委托

###  基本理念

* 操作对象自己不会去处理某段逻辑，而是会把工作委托给另外一个辅助对象去处理

### 类委托

* 把一个类的工作委托给另外一个类来实现
* 好处：大部分方法由委托类来实现，少部分方法重写，甚至自己添加几个新方法。这样就很容易创建一个全新的类

### 类委托举例

```kotlin
class MySet<T : Any?>(val helper: HashSet<T>): Set<T> by helper {
    fun addNewFunction(){
        Log.d("MySet","新添加的方法")
    }

    override fun isEmpty(): Boolean {
        Log.d("MySet","重写一个方法")
        return helper.isEmpty()
    }
}
```

* 使用关键字`by`

### 属性委托思想

* 将一个属性(字段)委托给**另一个类**去完成

### 语法结构

* 委托者

```kotlin
var p by MyDelegate()
```

* 被委托者

```kotlin
class MyDelegate {
    private var value: Any? = null

    operator fun getValue(myClass: Any?, prop: KProperty<*>): Any?{
        return this.value
    }

    operator fun setValue(myClass: Any?,prop: KProperty<*>,value: Any?){
        this.value = value
    }
}
```

* 这种实现是一种标准模板
* 其中myclass指定了什么类能够委托给此类，此处设置成Any，即都可以

### 委托属性实现延迟加载

* 代码

```kotlin
val p by lazy{
    //...
    //最后一行是返回值
}
```

* `by`是委托的一个关键字，`lazy`是一个高阶函数
* lazy函数实际返回的是一个对象，此对象接收一个函数作为参数。即返回的对象是`MyDelegate(code)`

### 延迟加载代码

* 被委托者

```kotlin
class MyDelegate(val block: () -> Any?) {
    private var value: Any? = null

    operator fun getValue(myClass: Any?, prop: KProperty<*>): Any?{
        return block
    }
}
```

* 构造函数参数是一个函数

* 顶层方法

  ```kotlin
  fun laterInit(block: () -> Any?) = MyDelegate(block)
  ```

  * 返回的实际上是一个被委托者的实例对象

* 委托者

  ```kotlin
  val p: Any? by laterInit {
  
  }
  ```

  * 在`{}`中编写代码，最后一行作为初始化的结果。就实现了延迟加载

***

## infix函数

### to例子

* `map`初始化过程中的`to`不是kotlin的关键字，相反它是kotlin中定义的一个顶层infix函数

### infix函数本质

* infix函数实际上就是一个高级的语法糖
* 它让函数调用的形式更贴近了英语的语法，从而让可读性更高
* 即`A to B`的实际写法是`A.to(B)`，即B作为A的参数
* 在比如`A.constains(B)` 可以使用infix函数简化成`A contains B`

### 定义

* `to`例子：`public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)`

***

## lazy 函数

* `lazy`不是关键字而是一个函数
* 函数签名：`public actual fun <T> lazy(mode: LazyThreadSafetyMode, initializer: () -> T): Lazy<T>`

***

## 泛型的实例化

### java泛型

* Java的泛型约束只存在在编译期，在运行期会对泛型进行擦除
* 即Java在运行期是获取不到泛型的类型信息的

### kotlin泛型

* kotlin中调用`val t = T()`是合法的
* kotlin实现泛型可实例化即运行时可以获得泛型信息的途径是内联函数
* 即将泛型声明时的函数设置成内联函数`inline`，然后在实际运行时做一个替换，这样就根本没有了擦除的影响，即实现了泛型可实例化

### 可实例化的两个条件

* 定义泛型的函数必须是内联函数`inline`
* 声明泛型的地方必须要加上`reified`才行
* 如`inline fun <reified T>getGenericType(){}`

### 注意

* kotlin中的泛型声明位置是在`fun`关键字的前面，而Java的泛型声明一般是在`fun`关键字的后面，这要注意区分。

### 泛型实例化的应用

```kotlin
inline fun <reified T> startActivity(context: Context,block:Intent.() -> Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context.startActivity(intent)
}
```

* 即此处将高阶函数和泛型的实例化结合，提供了非常便捷的应用

***

## 泛型协逆变

### in和out的约定

* 约定在泛型类或者泛型接口中
* 传入参数的地方的泛型是`in`泛型；返回返回值的地方的泛型是`out`泛型

### 冲突原因

* `A extends B`，即A是B的子类。按照道理来说`AnnotherClass<A>`应该是`AnnotherClass<B>`子类，但是Java不允许他们像子类那样进行传参
* 现在假设他们可以像子类那样传参。即某处接收`AnotherClass<B>`作为参数实际上传入了`AnnotherClass<A>`
* 首先是读问题。正常读肯定没有任何问题，正常读那里会有问题
* 然后是写问题。写其实也是没有任何问题的，因为静态类型声明为父类但是实际类型完全可以赋值成子类的类型，即子类向上转型为父类并赋值。但真正的问题是出在写后的读上面，当某静态类型为父类但赋值的实际类型是子类时，当调用get获取时。返回值肯定是父类类型，但接收的静态类型是子类类型。这样就会出错

### 解决办法

* 在泛型可以协变时，禁止进行写即赋值操作，就可以实现协变的安全
* 即泛型只能声明在返回值`out`上面，而不能声明在参数`in`上面，这样就可以完成泛型协变

### 示例

```kotlin
public interface Collection<out E> : Iterable<E> {
    public operator fun contains(element: @UnsafeVariance E): Boolean
}
```

* 对于kotlin中的不可变集合，是不存在赋值行为的，因此就声明时泛型为`out`
* 对于`contains`方法，此时泛型在`in`位置，按照道理来说是不符合要求的，但使用了`@UnsafeVariance`注解。表示让kotlin编译期忽略，实际上`contains`方法的逻辑中并没有进行赋值的行为。因此这种使用是合理的

### 逆变定义

* 即`A extends B`，A是B的父类，但`AnnotherClass<A>`是`AnnotherClass<B>`的子类
* 也就是逆变时泛型只能出现在`in`上，而不能出现在`out`上

### 逆变一般用法

* 逆变一般是泛型类或者泛型方法不返回泛型内容，而是返回统一的内容如`String`，"Boolean"这种
* 那么在这种情况下理论上只要传进去都能处理
* 这也就是会有协变的原因

***

## Effective kotlin

### 装箱与拆箱

* 为了避免装箱与拆箱，可以将意图的可空类型设置成不可空类型并给它加上一个默认值

### 基本/引用类型与序列化

* 声明成基本类型的变量，即是server未下发也一定不会为空，会有缺省默认值。但此时可能会有默认值与业务相关的问题
* 声明成可空类型的变量，server未下发则其一定为`null`

### 数组与列表

* 列表底层由数组实现，虽然数组性能更高，但业务中使用列表更加方便

### data class慎用

* data class只有在所有成员变量都声明了默认值时，编译器才会生成无参构造函数
* gson在反序列化时，会使用无参数的构造函数反射来创建实例，在找不到无参数的构造函数时，会使用`Unsafe`来创建，此时除了基础类型外不会设置任何的默认值(即data class的默认值失效)
* data class在配合json使用时，无比保证所有成员变量都有默认值或手动增加一个无参构造函数

### java 与kotlin混调

* 当kotlin调用java类的一个属性时，如果java类的属性没加上`@Nullable`注解，则kotlin无法推断这个属性是可空还是非空，且编译器不会给任何提示，如果有问题提则会在运行时崩掉
* 所以建议java的属性加上`@Nullable`或非空注解来帮助kotlin推断

### when 与switch

* java的switch语句只能比较整型，即是支持了`string`,也是通过`hashcode`来实现的
* java的switch不支持`boolean, float, double, long`。第一个是没必要，第二三个是会丢失精度，第四个是因为都是转换成`int`比较，也会丢失精度
* `when`语句的实现是能转换成`switch`就转，因为`switch`有字节码优化效率更高；不能转就编译成`if-else`语句

### 循环

* `foreach > for > while`，因为过程中局部变量更少性能更高
* 善于使用kotlin中各种列表的拓展函数
