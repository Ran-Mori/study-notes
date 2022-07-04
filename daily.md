# 日常笔记

***

## 长连接使用

* 端上和server建立一条长连接
* 当有新的通知时server给端上一个通知，端上立即发出一条`/count`请求获取未读消息数及未读消息类别
* 端上根据返回的结果，如果`multiNoticeCountList.size() > 0`就调用处理未读消息的方法
* 处理未读消息的方法首先对`muliNoticeCountList`做一些过滤，然后通过`EventBus`抛出一个事件
* 事件处理方法体内请求`/notice/multi`，并带上`group`作为参数
* `/notice/multi`返回对应的通知信息，将用其来刷新外部摘要

***

## 多态

* Father.kt

```kotlin
open class Father() {
    var name: String = ""
    var age: Int = 0
}
```

* Son.kt

```kotlin
class Son: Father() {
    var type: Int = 0
}
```

* Main.kt

```kotlin
fun main(args:Array<String>){
    var father: Father? = null
    var son: Son = Son()
    son.type = 10
    father = son
  	father.type = 9 //编译错误，father无type字段
    val type = (father as? Son)?.type ?: 0
    print("type = $type") // type = 10
}
```

* 静态类型为父类，运行时类型为子类。将运行时子类强转成子类类型就能完全访问使用了

***

## UML图

* `实现关系`：接口及实现类的关系。由一个空心三角形一条直线组成，三角形指向接口
* `泛化关系`：对象与对象之间的继承关系。由一个三角形和一条直线组成，三角形指向父对象
* `关联关系`：对象与对象之间的连接关系。通常为一个对象持有另一个对象的引用。往往还有数量关系。线条是一个带箭头的实线，箭头指向被使用者
* `依赖关系`：对象与对象之间的弱关联关系。通常为构造器、方法局部参数、返回值、静态方法调用。线条是一个带箭头的虚线，箭头指向被使用者
* `聚合关系`：对象与对象之间的强关联关系。部分可以离开主体而存在。线条是带空心的菱形实线，菱形指向主体
* `组合关系`：对象与对象之间的强关联关系。部分不可以离开主体而存在。线条是带实心的菱形实线，菱形指向主体

***

## 变量、方法传递方式

* 通过构造函数传入
* 通过私有变量，通过公有`set、get`方法对外暴露
* 结合第二点通过高阶函数传递

***

## 编码详解

* `字符`：各种文字和符号的总称
* `字符集`：即字符表，如英文字符表、阿拉伯字符表、ASCII码表……
* `coded charater set`：为字符表中每一个字符指定一个编码(码点code point)得到编码字符集。如ASCII字符集、Unicode字符集、GB2312字符集……
* `charater encoding form`：编码字符集只规定了字符和码点的映射，并没有规定实际的`字节表示方式`。如ASCII编码、UTF-8编码、UTF-32编码……
* `UTF-8`：对ASCII向后兼容，字节数可以变长，从1到4
* `UTF-16`：支持2字节和4字节，是一种折中
* `UTF-32`：无脑用4字节，浪费空间，几乎没怎么用
* `GoLang字符编码表`
  * 统一全部使用`utf-8`
  * `len(string)`是字节长度而不是字符长度，即`len("早") = 3`
* `hex编码`
  * 是一种二进制编码，将二进制数据编码成文本数据，与上面的字符编码恰恰相反
  * 将复杂不可见的字节数组数据，转换成可显示的字符串数据
  * 存储ASCII效率为50%。因为1个ASCII字符占8位，编码后成了2个ASCII字符占16位，因此50%
* `base64编码`
  * 字符为`abc...zABC...Z012...9+/`
  * 6位一组，即3字节4组。编ASCII效率为`75%`
* `StdEncoding`：末尾差字节以`0`填充
* `RawEncoding`：相比标准减少了padding，遇到不是3倍数时采用移位
* `UrlEncoding`：`+ -> -`，`/ -> _`
* `文本传输`：http以文本传输，要用hex或者base64编码。内容为文本，自带描述信息(参数名)
* `二进制传输`：tcp以二进制传输，要用字符编码。内容为二进制，以预先定义好的格式拼接在一起

***

## MyConfig一种使用

* MyConfig

  ```kotlin
  //difine
  class MyConfig private constructor(builder: Builder) {
      companion object {
          fun build(block: Builder.() -> Unit = {}) = Builder().apply { block() }.build()
      }
  
      val owner: LifecycleOwner? = builder.owner
  
      class Builder {
          var owner: LifecycleOwner? = null
  
          fun build() = MyConfig(this)
      }
  }
  ```

  ```kotlin
  //use
  private fun getMyConfig() = MyConfig.build{ lifecycleOwner = this.getLifecleOwner }
  ```

  * `MyConfig.buid()`：高阶函数声明了函数的接收者，即`Builder.() -> Unit`

  * 构建时传入高阶函数`lifecycleOwner = this.getLifecleOwner`

  * 调用`Builder().apply { block() }`时，因为`block()`的接收者是`builder`，实际上是调用`builder.block()`，即完成了`var owner: LifecycleOwner? = null`的初始化

  * 接着调用`Builder.build() = MyConfig(this)` 方法，传入`this`

  * 最后执行`MyConfig`的私有构造函数，初始化时执行`val owner: LifecycleOwner? = builder.owner`完成参数传递

* MyListenerWrapper

  ```kotlin
  private class MyListenerWrapper<T>(val listener: T, val myConfig: MyConfig, val map: MutableMap<T, MyListenerWrapper<T>>): LifecycleObserver {
      init {
          myConfig.owner?.lifecycle?.addObserver(this)
      }
  
      @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
      fun onDestroy() {
          myConfig.owner?.lifecycle?.removeObserver(this)
          map.remove(listener)
      }
  }
  ```

  * 本身是一个`LifecycleObserver`

  * `init`时观察`config`传入的`LifecycleOwner`

  * `onDestroy`时取消观察，并将`listner`从`map`集合中移除


* 过程使用

  * 创建`MyConfig`，传入一个`LifecycleOwner`

  * 定义一个`listener`，并带上`MyConfig`一起传过来

  * 传入的内容包装成一个`wrapper`，完成生命周期管理，防止内存泄漏。并且有一个`map`，里面都是存的各种`listener`

  * 发生了一个关注事件

  * `handler`的`handlerCallback()`调用

  * 遍历`map`中的`listener`并依次调用`listener.invoke()`方法

  * 自此就完成了一个观察者模式

***

## 构造者模式

```kotlin
class Example(builder: Builder) {
  var text: String? = ""

  init {
    this.text = builder.text
  }

  open class Buidler {
    var text: String? = ""

    fun setText(text: String): Builder {
      this.text = text
      return this
    }

    fun build() = Example(this)
  }
}
```

***

## SPI子分层

* IService.kt

```kotlin
interface IService {
  fun getSubService(): ISubService
}
```

* ServiceImpl.kt

```kotlin
class ServiceImpl: IService {
  @Overide
  fun getSubService(): ISubService = SubServiceImpl
}
```

* ISubService.kt

```kotlin
interface ISubService {
  fun getFrequency(): Int
}
```

* SubServiceImpl.kt

```kotlin
object SubServiceImpl:ISubService, PushCallBack {
  @Overide
  fun onPushSuccess(data: Data) {
    keva.storeData(data)
  }

  @Overide
  fun onPushFail() {
    keva.storeData(Data.getDefault())
  }

  @Overide
  fun getFrequency() = keva.getData()
}
```

***

## 受检异常

* 这玩意有好处也有坏处
* Java保留有受检异常，但新的kotlin完全把受检异常去掉了
* 无论是否有受检异常，均不能解决处理所有异常。两者的区别只是有受检异常时在编译期处理的异常数量更多，无受检异常时在编译期处理的异常更少。但例如空指针这种异常还是照抛不误
* 往往Java开发人员捕获异常后不做任何处理，只是做一个打印操作。这样只是欺骗自己，实际上对于程序的健壮性并没有任何的好处。
* 异常一步步往上抛，结果顶层要处理太多太多异常。如果顶层不做处理那和没有受检异常实际上是一样的
* 所以kotlin去掉受检异常还是有一定道理的

***

## 阿里云ECS主机

* ECS简介

  * ECS(Elastic Compute Service) 弹性计算服务

  * 弹性：可以不用提前固定买死配置，而是根据需要随时扩容、随时增带宽

  * ECS涉及资源：实例规格、块存储、镜像、快照、带宽、安全组

* 有关概念
  * 地域和可用区：指ECS实例所在的物理位置
  * 实例：就是一台虚拟的计算机，包含硬盘、内存、系统等基础部件
  * 实例规格：指这台计算机的配置
  * 镜像：指虚拟机运行的操作系统
  * 块存储：包括基于分布式存储架构的**云盘和共享块存**储，以及基于物理机本地硬盘的**本地存储**
  * 快照：某一个时间点的数据备份
  * 安全组：虚拟的防火墙
* 创建实例
  * 按量计费：只有关机了不用才不扣费
  * 分类：是按照配置来分。如内存型、大数据型、通用型、共享型……
  * 网络类型
  * 经典网络
    * 一个物理地区的主机的防火墙是统一的，一个破全部都有风险
    * IP地址绑定在实例主机上面
  * 专有网络
    * 各个云主机做了隔离，不能连锁入侵破坏
    * IP地址配置在网关上，在VPC内还可以实现多实例主机组网
  * 创建私人专有网络：就是创建一个192.168不能访问互联网的内网，通过网关连接互联网
  * 弹性公网IP
  * 作用：用于绑定到未分配公网IP的主机、网关或网卡上
  * 密钥对
  * 作用：防止登录工具有后门
  * 公钥，秘钥：秘钥自己保管，公钥复制到主机上

***

## JavaEE第一节课

* 1.1 ORM
  * 全自动生成SQL语句的框架反而不是特别好，后期数据库优化调优很困难。


* 1.2 面向对象五大原则

  * 单一责任原则 - 一个类负责一件事
  * 开放封闭原则 - 对功能的扩展是开放的，对代码的修改是封闭的 —— **AOP**
  * 里式替换原则 - 当你在程序当中用了一个类，我可以用这个类的子类来替代它而不产生任何影响。 —— **多态**
  * 接口分离原则 - 通过接口把一些不相关的功能分离出来，类似于单一责任原则
  * 依赖倒置原则 - 具体的东西应该依赖于抽象的 —— **IOC**

* 1.3 面向抽象编程

  ```java
  Rectangle rectangle =new Rectangle(); //方式一
  Shape rectangle =new Rectangle(); //方式二
  ```

  * 方式二更好

* 1.4 注解实现原理

  * 每一个自定义注解都有负责处理的Handler，在这个handler里面利用反射来处理相应的逻辑

***

## 嵌入式软件系统第一节课

* 1.1 什么是嵌入式芯片与软件
  * 用在非通用计算机，例如安防监控，移动基站，仪器仪表等的处理器芯片
  * 用在嵌入式设备上的软件
  * 嵌入式计算机系统要嵌入到对象体系中，实现的是对象的智能化控制，因此，它有着与通用计算机系统完全不同的技术要求和发展方向（嵌入式性能，控制能力和控制可靠性）
  * 嵌入式软件通常暂时不变，因此也被称作**固件**


* 1.2什么是嵌入式系统
  * 是一种完全嵌入在受控器件内部，为特定的应用而设计的专用计算机系统。


* 1.3 嵌入式处理能力
  * 目前嵌入式处理器的能力不一定比通用计算机弱，很可能更强。比如军工方面的边缘处理器


* 1.4嵌入式系统举例

  * 打印机共享器 
    * 一边是串口，一边是网口
    * 内嵌微处理器+软件
    * **吞吐量**、**响应** 最重要
    * 易测性 
      * 高低温、碰撞、续航等专业性测试
      * 并发测试
  * 无线条形码扫描仪
    * **能耗**问题是关键
  * 地下油罐监视器
    * **成本**（性能，处理器）问题很重要
  * 智能灭火器压力检测


* 1.5 FPGA可编程逻辑器件
  * 设计硬件不只要硬件工程师，也需要软件写代码
  * 给一块空板。与门非门自己写，自己控制


* 1.6 嵌入式微处理器
  * 嵌入式微处理器又叫做单片机
  * 一般以某一种微处理器内核为核心，芯片内部集成一大串东西


* 1.7 嵌入式系统的典型硬件
  * 必须要有微处理器和内存，现在的系统通常还有**串行接口**和**网络接口**
  * 一般没有键盘，显示屏，磁盘驱动器


* 2.1 术语
  * 芯片 - 就是芯片
  * 引脚 - 芯片两侧或四周的外面连接部分
  * 说明书
  * 原理图和PCB图
  * 印刷电路板

***

## kotlin协程

* 并行与并发

  * 多任务并发的反义词是多任务`顺序`

  * 多任务并行的反义词是多任务`串行`


* 广义协程
  * 优点
    * 单线程
    * 通过`控制代码执行顺序`实现并行而不是`真正的`并行
    * 单线程支持并发但无`并发冲突`
    * 比线程性能高，因为不用切换线程
    * 全程单线程因此是用户态

  * 缺点
    * 因为单线程，一旦阻塞则整个线程都阻塞
    * 因为单线程，不能处理耗时任务


* kotlin协程本质
  * `.class`JVM语言不支持广义上的协程
  * kotlin协程只是一种`线程框架`
  * 它通过切换线程来模拟广义上的协程效果
  * 底层本质还是使用`线程池+Handler`


* 推导
  * 通过线程切换来实现协程，则性能肯定不是最优的

***

## 二叉树概念

* 满二叉树：每一层都满，一点点没满都不行
* 完全二叉树：除最后一层外其他所用层都必须满，且组后一层结点依次从左到右排列

***

## 堆排序

* 概念：堆排序是利用堆这种数据结构来进行排序
* 堆性质
  * 是一棵完全二叉树
  * `arr[i] >= arr[2i + 1] && arr[i] >= arr[2i + 2]`或者`arr[i] <= arr[2i + 1] && arr[i] <= arr[2i + 2]`之一成立，分别是大顶堆或者小顶堆
* 堆的实现：很多时候都是用的数组
* 堆排序：有点复杂，不会

***

## CAS机制

* 处理器操作的原子性

  * 处理器实现了从内存中读取或者写入一个字节是原子性的

* CAS

  * 三个值
    * 内存当前值V
    * 内存预期值A
    * 内存欲更新为的值B
  * 行为
    * 当且仅当`V == A`时，才向内存中写入`B`/s

* 示例

  ```java
  int A = 1;
  int V;
  int B = 0;
  do {
      V = this.getIntVolatile(); //内存当前值V
  } while(!this.compareAndSwapInt(A,V,B); //V==A时更新内存
  ```

* 原子性保证
  * 操作内存时使用到了处理器的特殊指令，该指令操作的内存区域会加锁，导致其他线程无法同时访问这一部分内存，从而保证了原子性

***

## 计算机体系架构

* 两种架构
  * 冯诺依曼架构
    * 指令和数据使用同一个存储器
  * 哈佛架构
    * 指令和数据使用不同的存储器
    * 优势
      * 指令和数据可以有不同的位宽
      * 取指令和数据可以同步进行，效率更高，吞吐量更高
    * 缺点
      * 结构比较复杂，需要两个存储器
*  现代处理器
  * 通常是两者的结合体，总体基于冯诺依曼架构，但也会采取哈佛架构的优势

***

## Cmake

* 概念

  * 解释

    > CMake is a generator of buildsystems. It can produce Makefiles, it can produce Ninja build files, it can produce KDEvelop or Xcode projects, it can produce Visual Studio solutions.

  * `cmake`和`make`的区别

    > Make (or rather a Makefile) is a buildsystem - it drives the compiler and other build tools to build your code. But cmake is a generator of buildsystems.
    >
    > 即make由cmake构建而来，cmake是构建工具的构建工具。make和nija是平级关系

* 命令

  * 经常使用的两个命令

    ```cmake
    cmake -S . -B build
    cmake --build build
    ```

* 语法

  * `add_execuable(main.out main.cpp other.cpp)`

    * 标准格式

      ```cmake
      add_executable(<name> [WIN32] [MACOSX_BUNDLE]
                     [EXCLUDE_FROM_ALL]
                     [source1] [source2 ...])
      ```

    * 标准解释

      > Add an executable to the project using the specified source files.

  * `add_library(mainlib SHARED main.cpp other.cpp)`

    * 标准格式

      ```cmake
      add_library(<name> [STATIC | SHARED | MODULE]
                  [EXCLUDE_FROM_ALL]
                  [<source>...])
      ```

    * 标准解释

      > Add a library to the project using the specified source files.

    * 通俗解释

      > 将mian.cpp和other.cpp源文件编译成一个命名为mainlib的共享库

  * `target_link_libraries(main.out PUBLIC hellolib)`

    * 标准格式

      ```cmake
      target_link_libraries(<target>
                            <PRIVATE|PUBLIC|INTERFACE> <item>...
                           [<PRIVATE|PUBLIC|INTERFACE> <item>...]...)
      ```

    * 标准解释

      > Specify libraries or flags to use when linking a given target and/or its dependents.

    * 通俗解释

      > 在链接构建main.out时将hellolib库纳入链接库范围

  * `add_subdirectory(fmt)`

    * 标准格式

      ```cmake
      add_subdirectory(source_dir [binary_dir] [EXCLUDE_FROM_ALL])
      ```

    * 标准解释

      > Add a subdirectory to the build.

  * `target_include_directories(hellolib PUBLIC .)`

    * 标准格式

      ```cmake
      target_include_directories(<target> [SYSTEM] [AFTER|BEFORE]
        <INTERFACE|PUBLIC|PRIVATE> [items1...]
        [<INTERFACE|PUBLIC|PRIVATE> [items2...] ...])
      ```

    * 标准解释

      > Add include directories to a target.

    * 通俗解释

      > 在构建hellolib搜索头文件时，将当前目录纳入头文件搜索目录，并且可以向下传递

* `[PRIVATE|PUBLIC]`区别

  * 是否`link、include`等操作时进行`传染`
  * 如`target_link_libraries(myexec PRIVATE hellolib)`表示另一个`library`在链接时需要`myexec`库时会自动可以添加`hellolib`进行链接，否则不能进行链接

* C++声明的必要性

  * 声明即引入头文件或在顶部做一个函数等声明如`void hello();`
  * 不声明编译器无法知道名称的含义。如`vector<MyClass> a`可能会把`vector、MyClass`看作是变量，把`<、>`看作时运算符

* 递归引入头文件

  * 当递归引入头文件时会报错无法通过编译

  * 防止递归引入的两种办法

    * `#pragma once`

    * 宏定义

      ```c++
      #ifndef INC_01_HEADER_NAME_H
      #define INC_01_HEADER_NAME_H
      
      #endif //INC_01_HEADER_NAME_H
      ```

* 第三方库引入

  * 纯头文件引入
    * 直接`target_include_directories()`时将包含头文件的文件夹填入即可
    * 通常`#include <xxx.h>`时需要定义一个`xxx.h`定义的宏，因为要防止重复引入
  * 子模块引入
    * 即作为`cmake`的子模块引入，通过`add_subdirectories()`和`target_link_libraries()`即可
  * 引用系统中预安装的第三方库
    * 最麻烦，通常通过`find_package()`
    * 不推荐使用

* 包管理器

  * `C++`没有包管理器，`vcpkg`太多问题简直没法用


***

## make

* 概念

  > Make (or rather a Makefile) is a buildsystem - it drives the compiler and other build tools to build your code. But cmake is a generator of buildsystems.

* 几个关键指令

  * `configure`

    * 概念

      * 是一个`shell script`

    * 作用

      > make sure all of the dependencies
      >
      > produces a customised `Makefile` specific to your system from a template file called `Makefile.in`

    * `configure scipt file`来源

      > run `autoconf` to turn  `configure.ac` into a `configure` script

  * `make`

    > This runs a series of tasks defined in a `Makefile` to build the finished program from its source code.

    * `MakeFile.in`来源

      > Run `automake` to turn  `Makefile.am` into a `Makefile.in`

  * `make install`

    > The `make install` command will copy the built program, and its libraries and documentation, to the correct locations.
    >
    > It does do nothing less than running the install function/task in `MakeFile`

## JNI

* 文件夹结构

  ```shell
  jni
  ├── HelloWorldJNI.class
  ├── HelloWorldJNI.java
  ├── jni_HelloWorldJNI.cpp
  ├── jni_HelloWorldJNI.h
  ├── jni_HelloWorldJNI.o
  └── libnative.dylib
  ```

1. 编写一个带有`native`方法的`HelloWorldJNI.java`文件，并且从native库目录中添加名为`hello_world_jni`的动态链接库

   ```java
   package jni;
   
   public class HelloWorldJNI {
   
       static {
           //load native library
           System.loadLibrary("hello_world_jni");
       }
   
       public static void main(String[] args) {
           new HelloWorldJNI().sayHello();
       }
   
       // Declare a native method sayHello() that receives no arguments and returns void
       private native void sayHello();
   }
   ```

2. 使用`javac -h`生成`jni_HelloWorldJNI.h`头文件

   `javac -h . HelloWorldJNI.java`

   ```c++
   /* DO NOT EDIT THIS FILE - it is machine generated */
   #include <jni.h>
   /* Header for class jni_HelloWorldJNI */
   
   #ifndef _Included_jni_HelloWorldJNI
   #define _Included_jni_HelloWorldJNI
   #ifdef __cplusplus
   extern "C" {
   #endif
   /*
    * Class:     jni_HelloWorldJNI
    * Method:    sayHello
    * Signature: ()V
    */
   JNIEXPORT void JNICALL Java_jni_HelloWorldJNI_sayHello
     (JNIEnv *, jobject);
   
   #ifdef __cplusplus
   }
   #endif
   #endif
   ```

3. 创建`jni_HelloWorldJNI.cpp`并实现方法

   ```cpp
   #include "jni_HelloWorldJNI.h"
   #include<iostream>
   
   JNIEXPORT void JNICALL Java_jni_HelloWorldJNI_sayHello
     (JNIEnv *, jobject) {
   std::cout << "Hello World JNI" << std::endl;
   };
   ```

4. 编译生成可重定位目标文件

   ```shell
   clang++ 
   	-c # 生成可重定位目标文件
   	-I ${JAVA_HOME}/include # `jni.h`
   	-I ${JAVA_HOME}/include/darwin #`jni_md.h`
   	jni_HelloWorldJNI.cpp # 源文件
   	-o jni_HelloWorldJNI.o # 生成目标文件
   ```

5. 链接生成动态链接库

   ```shell
   clang++ 
   	-dynamiclib # 指定生成动态链接库
   	jni_HelloWorldJNI.o # 源文件
   	-o libhello_world_jni.dylib # 生成目标文件
   ```

6. 运行

   ```shell
   cd .. # 保证位于jni的父目录
   
   ls # jni
   
   java 
   	-Djava.library.path=jni # 将jni目录添加进native库搜索目录
   	jni.HelloWorldJNI # 全限定类名
   ```

   `Hello World JNI`

## websocket

* links
  * [What is web socket and how it is different from the HTTP](https://www.geeksforgeeks.org/what-is-web-socket-and-how-it-is-different-from-the-http/)
  * [The WebSocket Protocol - RFC](https://datatracker.ietf.org/doc/html/rfc6455)
* http features
  * `unidirectiona`l  - each HTTP or HTTPS request establish the new connection to the server every time and after getting the response the connection gets terminated by itself. 
  * `stateless ` 
* what used to be like without websocket
  * Creating web applications that need bidirectional communication between a client and a server has required an abuse of HTTP to poll the server for updates while sending upstream notifications as distinct HTTP calls.
* disadvantages of using http for bidirectional communication
  * at least two connections are created. one for sending information to the client and a new one for each incoming message.
  * The wire protocol has a high overhead, with each client-to-server message having an HTTP header.
  * The client-side script is forced to maintain a mapping from the  outgoing connections to the incoming connection to track replies.
* what is websocket
  * a bidirectional protocol, a full-duplex protocol, a stateful protocol, an independent TCP-based protocol
* websocket  feature
  * `stateful` - the connection between client and server will keep alive until it is terminated by either party
* url
  * **ws://** or **wss://**
* What scene is suitable for using it
  * real-time app
  * instant chat app
  * game app
* Relationship to TCP and HTTP
  * The WebSocket Protocol is an independent TCP-based protocol. Its only relationship to HTTP is that its handshake is interpreted by HTTP servers as an Upgrade request.
  * By default, the WebSocket Protocol uses port 80 for regular WebSocket connections and port 443 for WebSocket connections tunneled over Transport Layer Security (TLS)

***

## Linux Device Driver

* what is 
  * User activities are performed by means of a set of standardized calls that are indepen- dent of the specific driver; mapping those calls to device-specific operations that act on real hardware is then the role of the device driver.
* hot plugin
  * This programming interface is such that drivers can be built separately from the rest of the kernel and “plugged in” at runtime when needed.
* 
