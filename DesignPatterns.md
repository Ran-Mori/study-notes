# 设计模式-可复用的面向对象软件基础

## 前

> ### 面向对象系统质量评估
>
> * 设计者是否强调了对象之间的 **公共协调关系**
> * 强调了公共协调关系会使系统体系结构更 **精巧、简洁、便于理解**
>
> ### 本书目的
>
> * 描述了在面向对象设计过程中针对 **特定** 问题简洁而优雅的方案
> * 设计模式 **捕获** 这些方案，并用简洁的语言表达出来
>
> ### 设计模式特点
>
> * 不需要用户华丽的语言
> * 有时会多付一些功夫，但同时会大大增加软件的 **灵活性**
>
> ### 设计软件想达到的特点
>
> * 更加灵活
> * 模块化
> * 可复用
> * 易理解
>
> ### 设计模式之间关系
>
> * 23个设计模式并不是彼此单独，毫无关联的
> * 相反，他们是联系紧密的。一个设计模式往往和其他几个设计模式一起使用
>
> ### 设计模式三种分类
>
> * 创建型
> * 结构型
> * 行为型
>
> ***

## 第一章 - 引言

> ### 复用性
>
> * 设计一个面向对象系统是很难的
> * 但设计一个 **高复用性** 的面向对象系统更难
>
> ### 歌剧类比
>
> * 歌剧会沿袭大团圆式模式，浪漫主义模式，悲剧英雄模式等等
> * 而这种模式移植到软件系统设计中来就是设计模式
>
> #### 本书与设计模式关系
>
> * 将面向对象软件设计经验作为 **设计模式** 记录下来
> * 以前是有设计模式的，但是并无很好规范统一性的表述。常常是一种难以书面言出的个人式经验，此书就能很好解决这个问题
>
> ### 本书未涉及
>
> * 本书未讨论分布式和实时程序设计相关的模式
>
> ### 什么是设计模式
>
> * 每一个模式描述了一个在我们周围不断重复发生的问题，以及该问题解决方案的核心。这样，你就能一次又一次地使用这个方案而不用做重复的劳动
> * 实际上，设计模式就是对软件开发经验的总结，归纳和规范化表述
>
> ### MVC模式
>
> * 其中View和Model是一种"订阅通知"的形式，这是一种`Observer`模式
> * MVC的一大特征是视图可以进行嵌套操作，这是一种`Composite`模式
> * View使用Controller子类的实例来实现不同的响应策略，即为了相同动作的不同响应只需要进行View的controller的替换。这种view和controller的关系叫做`strategy`模式
>
> ### 设计模式按照目的进行分类
>
> * 创造型(creational)
> * 结构性(structural)
> * 行为型(behavioral)
>
> ### 设计模式按照范围划分
>
> * 类
> * 对象
>
> ### 寻找合适的对象
>
> * 面向对象设计最困难的部分就是 **将系统拆分成对象的集合**
> * 这个拆分的过程会考虑很多很多东西
>
> ### 指定对象的接口
>
> * **型构**定义：对象操作的 **操作名、参数、返回值**
> * **接口**定义：对象操作所定义的 **所有型构** 的集合
> * **类型**定义：用来表示 **特定接口** 的一个名字，有超类型，子类型之分
> * 接口作用：对象只有通过接口才能与外界进行交流，如果不通过对象的接口就不知道对象的任何事情，也无法请求对象做任何事情
> * 动态绑定：当对象发出请求时，所引起的具体操作既与 **请求本身** 相关，又与 **接受对象** 相关。发送的请求真正的实现只有到运行时才知道
>
> ### 描述对象的实现
>
> * 抽象类：为他的字类定义公共接口
> * 混入类：给其他类提供可选择的接口和功能的类。它也不能实例化，混入类要求多继承。感觉就像序列化`Serializable`和比大小的`Comparable`
> * 对象的**类**与**类型**
>   * 对象的**类**规定了对象的具体实现
>   * 而对象的**类型**只与对象的的接口有关(前提是严格遵循接口定义，如C++的.h文件)
>
> ### 对接口编程而不是对类编程
>
> * 客户无需知道他们使用的对象的特定类型，只需要对象有客户所期望的接口就好
> * 客户无需知道他们正在使用的对象由什么类来实现，他们只需要知道定义接口的抽象类
> * 不将对象声明为某一个特定的具体类的实例对象，而是让它遵循抽象类所定义的接口
>
> ### 继承与组合
>
> * 继承又叫 **白箱复用**，因为继承后被复用的父类的细节子类是可以看到的
> * 组合又叫 **黑想复用**，因为组合之间的内部细节彼此是不知道的
> * 继承的缺点
>   * 继承关系是在编译期就确定死了的，在运行期无法改变
>   * 继承时子类是可以了解到父类的细节的，因此常常被认为是破坏了封装性
>   * 当要复用子类时，会产生依赖性问题。如果顺序继承不合适常常还要从父类起fork在写另外一个子类
> * 组合的优点
>   * 组合里面存放的是对象的引用，实际上就是接口。因此它是面向接口的，依赖就会更少
>   * 使用组合能细粒度化每个类的功能，使每个类更精炼，还能使整个类继承层次保持在较小的规模
> * 即你不应该为获得复用而去创建新的构件，而是优先使用组合技术
> * 现实情况是设计者往往过度使用了继承这种复用方式，这是不太好的
>
> ### 委托
>
> * 定义：是一种组合方法，它使组合具有与继承同样的复用能力
> * 接受请求的对象将操作委托给代理者，实际上就有点类似于子类将请求委托给父类
> * 委托优点：在运行时可以进行改变
> * 委托缺点：动态的，运行时效率比较低
> * 委托举例：Window没有与矩形相关的操作，于是在Window类有一个引用，将所有的矩形操作全部委托给外面
> * 委托是对象组合的特例，它说明了对象组合作为一个代码复用机制可以代替继承
>
> ### 参数化类型
>
> * 是除了继承和组合外的另一种复用技术
>
> ### 继承、组合、参数化类型比较
>
> * 组合提供了动态运行时改变的能力，但间接，效率比较低
> * 继承提供了操作的缺省实现，子类可以重定义这些操作
> * 参数化类型允许你改变类所用的类型，但不能在运行时改变
>
> ### 设计应支持变化
>
> * 无良好的设计会导致系统的修改异常艰难
> * 一般对象组合和参数化类型是除了继承之外的很好的复用手段
>
> ### 应用程序、工具箱、框架
>
> * 应用程序就是应用程序
> * 工具箱是一组相关的、可复用的类的集合
> * 框架是构成一类特定软件可复用设计的一组相互协作的类
> * 它们三者的难度递增，框架是最难的
> * 框架与设计模式的辨别
>   * 设计模式是比框架更抽象的存在
>   * 设计模式是比框架更小的体系结构元素
>   * 框架比设计模式更特例化
>
> ***

## 第三章 - 创建型模式

> ### 前述
>
> * 创建者模式抽象了实例化的过程
>
> ### 迷宫例子
>
> * 迷宫有多种元素构成，墙，房间，通道等等
> * 如果我们采用 **硬编码** 的模式，创建一个非常简单的迷宫需要比较复杂的代码
> * 代码量多其实并不是最致命的，最致命的是一旦你想修改这个迷宫的布局，你几乎需要全部推倒重来
>
> ### 根据迷宫初识创建型模式
>
> * 不调用**构造器**创建而是调用**虚函数**构建，通过不同的子类来实现创建不同的实例 —— **Factory Method** 
> * 通过传入**不同的参数**来创建不同类型的对象 —— **Abstract Factory**
> * 传入一个对象，这个对象可以在它自己建造的迷宫里面增删改查最后形成一个新的迷宫 —— **Builder**
> * 不同的迷宫构件有不同的原型，用不同的对象来替换这些原型来改变创建 —— **Prototype**
>
> ***

## 单一职责原则

> ### 定义
>
> * single responsibility principle —— 简称SRP
> * there should never be more than one reason for a class to change
>
> ### 用户信息举例
>
> * 用户类一般分为用户的 **属性** 和 **行为**
>
> * 我们可以把属性单独抽象出一个接口，将行为单独抽象出一个接口
>
>   ```bash
>   IUserPropertiy    IUserBehave
>        |                 |
>    UserProperty      UserBehave
>        |                 |
>        ___________________
>                  |
>                UserInfo
>   ```
>
> ### 电话举例
>
> * 日常的电话类设计其实是不太正确的
> * 因为电话分为 **行为** 和 **协议**，行为指接听和挂断两种行为，协议则是指数据进行传输的方式
> * 但一般我的都将它们合并在一起只设计一个 **IPhone**接口，不同的电话实现这个接口并没什么问题
> * 如果要真正的实现单一职责，就不得不使用 **组合复用** ，因为电话内容的传输即协议通讯是依赖于行为动作的
>
> ### 方法也能单一职责
>
> * 比如一个UserManager类，不要直接传一个User对象进去进行修改
> * 而是把每个属性的修改拆分开来，不同的属性修改调用不同的方法
>
> ### 总结单一职责原则
>
> * 这个原则理论上来说是很好的，但是实际的使用并不可能完全百分之百**SRP**
> * 它只是给我们提供一个指导意见，能拆分的职责就尽量进行拆分，不要全部写在一起。这样后期进行维护是极其不方便的
> * 但也不要为了追求满足SRP而去满足SRP，那样的话类的数量会爆炸
> * 即实际使用中要做职责的细粒度化，但也不要为了细而细
>
> ***

## 里氏替换原则

> ### 名字
>
> * `Liskov Substitution principle`
> * 简称`LSP`原则
>
> ### 定义
>
> * `functions that use pointers to references to base classes must be able to use objects of derivved classes without knowning it`
> * 所有引用基类的地方必须能够透明的使用子类对象
>
> ### 实际感觉
>
> * 只要父类出现的地方，子类就能出现，替换成子类不会出现任何问题
> * 最后的结果就是调用者根本不知道是在调用父类还是子类
>
> ### 子类必须完全实现父类的方法
>
> * 如果在实际的业务逻辑中，如果替换成子类会错误，则说明已经违背了`LSP原则`
> * 在实际的业务逻辑中，声明引用处尽量使用顶层接口和父类而不要使用子类
> * 如果子类不能完全实现父类的方法，则建议断开继承关系。采用 **依赖、组合、委托** 的关系代替继承
>
> ### 玩具枪失败例子
>
> * 一个`AbstractGun`接口有击杀的`shoot`方法
> * 而玩具枪有枪的特征，但是无法 **全部实现父类方法**，这里就已经违背了里氏替换原则
> * 最好的做法是重新创建一个类。通过 **委托** 的方式，将枪中除了`shoot`的逻辑交给`AbtractGun`处理
>
> ### 子类可以具有自己的个性
>
> * 向子类转型是安全的
> * 但向父类转型是不完全且禁止的
>
> ### 覆盖或实现父类的方法时输入参数可以被放大
>
> * 父类
>
>   ```kotlin
>   open class Father {
>       open fun doSomething(set:HashSet<String>):HashMap<String, Any>{
>           println("father")
>           return HashMap()
>       }
>   }
>   ```
>
> * 子类
>
>   ```kotlin
>   class Son:Father() {
>       fun doSomething(set: Set<String>): HashMap<String, Any> {
>           println("son")
>           return HashMap()
>       }
>   }
>   ```
>
> * main函数
>
>   ```kotlin
>   fun main(args: Array<String>) {
>       val son:Father = Son()
>       son.doSomething(HashSet())
>   }
>   ```
>
> * 执行结果`father`
>
> * 父类的参数类型是`HashSet`，子类参数类型是`Set`，范围更大
>
> * 实际参数类型是`HashSet`，范围更大。输出结果是`father`
>
> * 很自然这种哦调用方法是正确的，符合 **里氏原则**
>
> * 实际上这里是方法的 **重载** 而不是重写
>
> * 对比
>
>   ```kotlin
>   class Test {
>       fun doSomething(list: MutableList<String>):HashMap<String, Any>{
>           println("MutableList")
>           return HashMap()
>       }
>       fun doSomething(list: ArrayList<String>):HashMap<String, Any>{
>           println("ArrayList")
>           return HashMap()
>       }
>   }
>   //执行
>   Test().doSomething(ArrayList())
>   //输出结果
>   ArrayList
>   ```
>
> * 同一个类的两个重载，结果就是输出`ArrayList`，这符合重载调用最小粒度
>
> ### 第三点总结
>
> * 子类可以**重载**继承自父类的方法，即使重载时参数粒度变小，实际传入参数也是小粒度，这也会调用父类方法符合里氏替换原则（经实际代码测试是这样的，这也符合动态绑定规矩）
> * 因此上面这种情况是极其反人类的，不符合里氏替换原则。我们要求子类重载父类方法时参数范围必须扩大
>
> ### 覆盖或实现父类方法时输出结果可以缩小
>
> * 重载是函数名相同，函数的参数类型或者顺序不同。与返回值没有关系，返回值不作为重载区分
>
> * 重写是函数名相同，参数类型相同(不允许继承)。返回值可以是父类返回值的子类
>
> * 以下声明是合法的
>
>   ```kotlin
>   open class Father {
>       open fun doSomething(set:HashSet<String>):Map<String, Any>?{
>           println("father")
>           return HashMap()
>       }
>   }
>
>
>   class Son:Father() {
>       override fun doSomething(set: HashSet<String>): HashMap<String, Any>? {
>           println("son")
>           return HashMap()
>       }
>   }
>   ```
> 
> * 父类的返回值是`Map`，子类重写返回值是`HashMap`。这是符合里氏替换原则的
> 
> ### 总结
> 
> * 满足以上原则就能很好地使用里氏替换原则
> * 就能增强程序的健壮性
> * 里氏替换原则要达到即使增加在多的子类，原始的子类和新的子类之间依然满足里氏替换原则
> 
> ***

## 依赖倒置原则

> ### 定义
>
> * `Dependence Inversion Principle`
> * `High level moudules should not depend upon low level modules. Both should depend upon abstractions. Abstractions should not depend upon details. Details should depend upon abstractions`
> * low level就是不可分割的底层原子操作。high level 就是原子操作组装而成的复杂操作
>
> ### Java中表示
>
> * 模块间依赖通过 **抽象** 发生，实现类之间不发生直接依赖关系。
> * 接口或抽象类不依赖于实现类
> * 实现类依赖于接口或抽象类
>
> ### 一个好的设计范例
>
> ```bash
>    IDriver      ----->   ICar
>       |                    |
>     Driver              BMW   Benz
> ```
>
> * `IDriver`和`ICar`之间有依赖
> * 但具体的实现类`Driver`,`BMW`,`Benz`之间是没有依赖关系的
>
> ### 依赖的两种写法
>
> * 构造方法注入
>
> ```kotlin
> private ICar iCar
> public Driver (ICar _icar){
>   this.icar = _icar
> }
> ```
>
> * setter注入
>
> ```kotlin
> private ICar icar
> public void setICar(Icar _icar){
>   this.icar = _icar
> }
> ```
>
> ### 遵循规则
>
> * 每个类尽量都有接口和抽象类
> * 变量的静态类型尽量是接口或抽象类
> * 任何类不从具体类派生
> * 尽量不overwrite抽象类已经实现的方法
> * 结合里氏替换原则使用
>
> ### 三者功能划分
>
> * `接口`: 声明属性和方法，定义和其他接口之间的依赖关系
> * `抽象类`：公共构造部分的实现
> * `实现类`：精确实现业务逻辑
>
> ### 为什么叫倒置
>
> * 正置思想：我要用电脑就依赖电脑，我要用手机就依赖手机。这是正常人的思维
> * 倒置思想：依赖不具体，反而在抽象进行依赖
>
> ### 最后
>
> * 不要为了用依赖倒置而用依赖倒置
> * 还是要以业务为重
>
> ***

## 接口隔离原则

> ### 定义
>
> * `Interface Segregation Principles`
> * `Clients should not be fored to depend upon interfaces that they dont use`
> * `The dependency of one class to annother class should depend on the smallest possible interface`
> * 客户端需要什么接口就依赖什么接口，把不需要的接口剔除掉
> * 要求接口细化，接口纯洁
> * 尽量建立单一的接口，不要建立庞大臃肿的接口
> * 接口尽量细化，同时接口中的方法尽量少
>
> ### 与单一职责原则
>
> * 单一职责原则要求的是类接口职责单一，注重的是职责
> * 而接口隔离原则要求的是接口方法尽量少
>
> ### Good Looking Girl举例
>
> * 之前接口
>
>   ```kotlin
>   interface IPrettyGirl{
>     fun goodLooking();
>     fun niceFigure();
>     fun greatTemperament()
>   }
>   ```
>
> * 现在接口
>
>   ```kotlin
>   interface IGreatTemperamentGirl{
>     fun greatTemperament()
>   }
>   ```
>
>   ```kotlin
>   interface IGoodBodyGirl{
>     fun goodLooking();
>     fun niceFigure();
>   }
>   ```
>
> ### 之前接口弊端
>
> * 如果是杨贵妃，就完全不适用。不能实现一个接口但没有全部实现它的方法，这样是不符合里氏替换原则的。
> * 这样就是电典型的封装过度
>
> ### 总结上面
>
> * 把一个臃肿的接口变成两个独立的接口所依赖的原则就是接口隔离原则
>
> ### 接口要尽量小
>
> * 接口小但是不能违背单一职责原则
>
> ### 接口要高内聚
>
> * 接口中尽量少对外暴露public方法
> * 暴露的public方法越多，变更的风险也就越大
>
> ### 定制服务
>
> * 有耦合就要有相互访问的接口，对于某个特定的访问者(客户端)，我们要为其量身定制一个优良的服务
> * 服务原则：只提供访问者需要的方法
> * 比如提供给权限更低的客户端，单独为其设置一个接口。这样权限更大的客户端完全不用改变，且满足接口隔离原则
>
> ### 最佳实践
>
> * 一个接口只服务于一个字模块或者业务逻辑
> * 通过业务逻辑压缩接口中的public方法
> * 已被污染接口，尽量去变更，如果变更风险大，则采用适配器模式进行转换处理
> * 了解环境，拒绝盲从
>
> ***

## 迪米特法则

> ### 定义
>
> * `Least Knowledge Principle`
> * 也称为 **最少知识原则** LKP
> * 规则：一个对象应该对其他对象有最少的了解
> * 通俗解释：一个类应该对自己需要耦合或调用的类知道得最少
> * 更通俗：你内部是如何耦合如何复杂我根本不关心，那是你的事情。我就知道你提供了多少public的方法，我就调用这些就行了。
>
> ### 朋友
>
> * 定义：出现在成员变量、方法输入参数和返回值的类型成员称为朋友。而出现在方法体中的类不属于朋友
> * 两个对象耦合就叫朋友关系，例如组合、聚合、依赖等
> * 一个在方法体中使用到的外部类，然而这个类居然不知道自己的行为已经与其他类发生了耦合。这是绝对不允许且严重违反迪米特法则的
>
> ### 违反举例
>
> * 清点人数例子
>
> ### 朋友间也是有距离的
>
> * 对朋友不要暴露太多的public方法
> * 比如安装软件例子，不用把每一步安装步骤都作为一个public方法暴露给外面。常常类在自己内部封装一个public的install方法，其他方法设置为private就好了
> * 上面这个例子是典型的高内聚例子
> * 类尽量不要公布太多public方法和非静态public变量
>
> ***

## 开闭原则

> ### 定义
>
> * `Open Close Priciple`
> * `Software entities like classes, moudules and functions should be open for extensions but closed for modifications`
> * 软件实体应该对拓展开放，对修改关闭
> * 软件实体应该通过拓展来实现变化，而不是通过修改已有的代码来实现变化
>
> ### 书籍销售举例
>
> * 一个软件在其生命周期内，都会有变化
> * 书籍销售系统要发生修改，即要将价格进行打折，应该怎样处理修改？
>
> ### 修改方式
>
> * **修改接口**： 所有实现类都要进行修改，方案否定
> * **修改实现类**： 直接在`getPrice()`方法中修改，然后`.class`文件替换。该方法十分不错，但不是最优
> * **通过拓展来实现变化**： 增加一个子类，overwrite `getPrice()`方法。这是最推荐的修改方法
>
> ### 对修改的界定
>
> * 开闭原则对拓展开放，对修改关闭，并不是意味着不做任何的修改
> * 一个模块变化，会对其他模块造成影响。特别是一个低层模块的变化必定引起更高层模块的变化
> * 通过拓展完成修改时，高层次模块的修改是必然且无法避免的
>
> ### 开闭原则重要性
>
> * 开闭原则是最基础的一个原则
> * 开闭原则是精神领袖，其他五大原则是指导设计的工具和方法
>
> ### 开闭原则对测试的影响
>
> * 测试是一个很复杂且耗时的过程
> * 如果通过拓展实现了业务逻辑，那么之前所有类的测试代码全部都不用进行测试就可以
> * 但一旦是通过修改代码实现的业务逻辑，那么涉及的重写的测试类就数不胜数了。难免会有遗漏，事故往往就是这样产生的
>
> ### 开闭原则提高复用性
>
> * 所有逻辑都是原子逻辑的聚合
> * 缩小逻辑粒度，直到一个逻辑不可再拆分为止
>
> ### 开闭原则可以提高可维护性
>
> * 维护人员最乐意的是拓展一个类，而不是修改一个类
> * 甭管一个类的代码写得多有优秀好懂，让维护人员读懂原有逻辑代码再去修改，这无疑是对维护人员的折磨与摧残
>
> ### 开闭原则是面向对象开发的要求
>
> * 业务有变化是肯定的
> * 要求要在设计之初就考虑到可能变化的因素，然后留下接口，等待预计变成现实
>
> ### 要抽象约束
>
> * 通过接口或抽象类来约束拓展
> * 参数类型，引用对象尽可能使用接口而不是实现类
> * 接口尽量保持稳定，一旦确定就不要进行修改
> * 要实现对外拓展开放，首要前提条件就是抽象约束
>
> ### 要封装变化
>
> * 将相同的变化封装到一个接口或者抽象类中
> * 将不同的变化封装到不同的接口或者抽象类中
>
> ### 最佳实践
>
> * 开闭原则是重中之重，是最基础的原则，是其他五大原则的精神领袖
> * 开闭原则只是一个原则，替换`.class`文件方法还是能用，但必须做到 **高内聚，低耦合**，否则一旦替换什么事故都会出来
> * 预知变化：架构师很重要的，设计一套系统不仅要符合现有的需求，还要适应可能发生的变化
> * 终极目标：开闭原则是一个终极目标，现实中往往不会百分之百达到，但朝着这个方向进行，可以显著地提高系统架构，真正做到 **拥抱变化**
>
> ***

## 单例模式

> ### 定义
>
> * `Ensure a class has only one instance, and provide a global point of access to it. `
>
> ### 构造函数
>
> * 注意构造函数的私有化。如果是无参构造函数，则这样写
>
>   ```java
>   private SingleTon(){
>                 
>   }
>   ```
>
> ### 单例模式缺点
>
> * 单例模式一般没有接口，扩展很困难。若想拓展，则除了修改源代码以外几乎没有第二条途径
> * 单例模式对测试不利。并发环境中单例模式没有OK，就别想进行单元测试
>
> ### 注意Clone
>
> * java中实现了`Clonable`接口的类可以调用`clone()`方法
> * 且这个clone过程是完全不必要调用构造函数的，因此即使是private的构造函数，依旧可以别clone
> * 因此要注意单例类不能实现`CLonable`接口
>
> ***

## 工厂方法模式

> ### 定义
>
> * `Define an interface for creating an object, but let subclasses decide which class to instantiate. Factory Method lets a class defer instaniation to subclasses`
> * 定义一个用于创建对象的接口，让子类决定实例化那一个类。工厂方法模式使一个类的实例化延迟到子类。
>
> ### 结构
>
> * `IProduct`：定义抽象
> * `Product`：产品
> * `IFactory`：抽象工厂
> * `Factory`：实现类工厂
>
> ### 核心代码
>
> * `product = (Product)Class.forName(c.getName()).newInstance();` 其中`c`是输入参数，类型为`Class`
>
> ### 优点
>
> * 封装性好，代码结构清晰。调用方只用知道 **产品类名**
> * 拓展性好。新增一个产品只需要增加一个实现类，其他啥都不用改(前提是接口稳定不变)
> * 解耦。高层只需要知道产品的抽象类，其他啥都不需要知道。符合迪米特法则，符合依赖倒置原则，符合里氏替换原则。
>
> ### 降级为简单工程模式
>
> * 无抽象工厂，无实例化工程
> * 只有一个工程类，且该类的方法还都是静态的。
> * 因为简单被称为简单工厂模式，也叫做静态工厂模式
>
> ### 简单工厂模式缺点
>
> * 拓展比较困难，因为工厂没有了接口类，自然拓展比较困难。
> * 而且还不符合开闭原则
>
> ### 升级为多工厂类
>
> * 抽象工厂下有多了实现类
> * 且每一个实现类只生产一种产品，也就意味着在调用生产方法时不用传入任何参数
> * 缺点：拓展性受到影响。每增加一种产品，就要增加一个工厂类
> * 改进：在高层提供一个协调类。这个类的作用是封装这些工厂实现类。
>
> ### 替代单例模式
>
> * 工厂方法模式可以替代单例模式
>
> ### 核心
>
> * Java反射创建是核心
>
> ### 对象容器减少创建
>
> * 在工厂中设置一个容器，用于存放创建了的对象
> * 当需要创建新的对象时，先去容器里面找，找不到在创建。
> * 典型的池子思想
>
> ***

## 抽象工厂模式

> ### 定义
>
> * `Provide an interface for creating families of related or dependent objects without specifying their concret classes.`
>
> ### 特点
>
> * N个产品族，在抽象工厂类中应该有N个创建方法
> * M个产品等级就应该有M个实现工厂类，同一个工厂生产**等级相同，但产品族不同**的产品
>
> ### 实际类比
>
> * 产品族一共有三个：Windows，MacOS，Linux
> * 产品等级一共有这些：文本编辑器、图片处理器、音乐播放器……
>
> ### 拓展性
>
> * 抽象工厂方法产品族无法拓展，一旦拓展就要修改接口，以至于修改所有的实现类，严重违背开闭原则
> * 但抽象工厂方法拓展产品等级是很好拓展的，只需要增加一个等级，增加一个抽象工厂实现类就可以了
>
> ### 优点
>
> * 封装性好
> * 产品族约束为非公开状态，满足迪米特原则
>
> ### 使用场景
>
> * 族数确定，有着相同的约束，就可以使用抽象工厂方法模式
> * 比如不同平台下的图形编辑器外观样式是相同的，但是实际代码是不同的。他们有共同的约束：操作系统类型
>
> ***

## 模版方法模式

> ### 定义
>
> * `Template Method`
> * `Define the skeleton of an algorithm in an operation, deferring some steps to subclasses. Template Mothod lets subclasses redefine certain step of an algothm without changing the algorithm's strcture.`
>
> ### 代码示例
>
> ```kotlin
> abstract class AbstractClass{
>     protected abstract fun doSomething();
>     protected abstract fun doAnything();
> 
>     fun templateMethod(){
>         this.doSomething()
>         this.doAnything()
>     }
> }
> ```
>
> * `templateMethod()`就是模版方法
>
> ### 抽象模版的方法
>
> * 基本方法：在子类中被实现，但在抽象类中被调用的方法
> * 模版方法：在抽象类实现其逻辑，实现对基本方法的调用。所有的子类都能进行调用
>
> ### 注意
>
> * 为了防止恶意操作。模版方法都应该加上`final`关键字，防止被恶意篡写
> * 但kotlin中不用添加，因为kotlin中本来就默认是final方法，如果不final要加上`open`关键字
>
> ### 优点
>
> * 封装不变部分，拓展可变部分。模仿方法的定义实现一般认为是不变不用拓展的
> * 提取公共部分，便于维护。如果出了问题，则只用改模版方法就可以了
> * 行为由父类控制，实现由子类实现。
>
> ### 缺点
>
> * 正向思维是抽象类负责定义抽象，而实现类负责实际的实现。
> * 然而模版方法的逻辑实现是由抽象类实现的，不太符合一般正常的逻辑
>
> ### 使用场景
>
> * 多个子类拥有公有的方法，并且逻辑基本相同。
> * 重要、复杂的算法。
> * 重构时。模版方法经常使用。把相同的代码抽到父类中，通过 **钩子函数** 约束其行为。
>
> ### 钩子(hook)函数
>
> ```kotlin
> abstract class AbstractCar{
>     protected abstract fun alarm();
>     protected open fun isAlarm():Boolean{
>         return false
>     }
> 
>     fun templateMethod(){
>         if (this.isAlarm()){
>             this.alarm()
>         }
>     }
> }
> ```
>
> * 默认所有的车子都是不鸣笛的
> * 但是有一个`protected open fun isAlarm():Boolean`方法。子类可以重写这个函数来控制模版类方法的执行逻辑
> * 即子类的一个返回值决定公共部分模版方法的执行结果
>
> ***

## 建造者模式

> ### 定义
>
> * `Builder`
> * `Separate the construction of a complex object from its representation so that the same construction process can create diffirent representations`
>
> ### 例子
>
> * 抽象构建者
>
> ```java
> public abstract class ComputerBuilder {
>     public abstract void setUsbCount();
>     public abstract void setKeyboard();
>     public abstract void setDisplay();
> 
>     public abstract IComputer build();
> }
> ```
>
> * 真实构建者
>
> ```java
> public class MacComputerBuilder extends ComputerBuilder {
>     private IComputer computer;
>     public MacComputerBuilder(String cpu, String ram) {
>         computer = new MacComputer(cpu, ram);
>     }
>     @Override
>     public void setUsbCount() {
>         computer.setUsbCount(2);
>     }
>     @Override
>     public void setKeyboard() {
>         computer.setKeyboard("苹果键盘");
>     }
>     @Override
>     public void setDisplay() {
>         computer.setDisplay("苹果显示器");
>     }
>     @Override
>     public IComputer build() {
>         return computer;
>     }
> }
> ```
>
> ### 优点
>
> * 封装性：不用知道内部的细节，直接一直set、set最后build就可以了。
> * 扩展性好：builder是继承自抽象builder或接口builder，互不影响，拓展性好
>
> ### 适合场景
>
> * **相同方法，不同执行顺序，产生不同的结果**。这种情况适合建造者，这也是此书给的例子
> * 产品零件多，且都可供选择，甚至选择顺序最后的结果不同。
>
> ### 与工厂方法比较
>
> * 工厂方法侧重于创建，是从无到有
> * 构建者模式侧重于零件的装配，产品大体的框架已经是好的。只是零件可选配，或者顺序不同导致结果不同
>
> ### 与模版方法结合
>
> * 如果选择顺序导致结果不同。那么实际执行的动作的总函数可以是模版函数，定义在抽象类中
>
> ***

## 代理模式

> ### 定义
>
> * `provide a surrogate or placeholder for another object to control access to it`
>
> ### 别名
>
> * 委托模式
>
> ### 优点
>
> * 职责清晰：被代理者不用关心非自身职责的事，这些事都由代理者来完成
>
> ### 普通代理
>
> * 就是最普通的代理
> * 在这种代理下用户是知道代理类的存在的，类似于必须知道`GamePlayerProxy`这个类
> * 客户只能访问代理角色，而不能访问真实角色
>
> ### 强制代理
>
> * 强制代理要求通过 **真实角色来找到代理角色**，思维有那么一点点不一样
>
> ### 举例
> * 接口
> ```java
> public interface IGamePlayer{
>   public void play();
>   public IGamePlayer getProxy();
> }
> ```
> * 被代理实现类
> ```java
> public class GamePlayer implenments IGamePlayer{
>   //此类的代理，初始为空
>   private IGamePlayer proxy = null;
>   //获取代理，被代理的就是自己
>   public IGamePlayer getProxy(){
>     this.proxy = new GamePlayerProxy(this);
>     return this.proxy;
>   }
>   public void play(){
>     if(this.isProxy()){
>       System.out.println("play");
>     }else{
>       System.out.println("请使用代理访问");
>     }
>   }
>   private boolean isProxy(){
>     return this.proxy != null;
>   }
> }
> ```
> * 代理者
> ```java
> public class GamePlayerProxy implements IGamePlayer{
>   private IGamePlayer gamePlayer= null;
>   public GamePlayerProxy(IGamePlaer _gamePlayer){
>     this.gamePlayer = _gamePlayer;
>   }
>   public void play(){
>     this.gamePlayer.play();
>   }
>   //循环代理，其实可以不要这一个
>   public IGamePlayer getProxy(){
>     return this;
>   }
> }
> ```
> * 场景
> ```java
> IGamePlayer player = new GamePlayer();
> IGamePlayer proxy = player.getProxy();
> ```
>
> ### 最终调用
>
> * `Player().getPaoxy().play();`
>
> ### 代理增强
>
> * 代理的过程中还可以增加其他的逻辑，最原有逻辑进行增强
>
> ### 动态代理
>
> * client不用关心代理类，代理类是自动生成的
> * 需要自己写代理类的代理是静态代理
> * 动态代理通过传入一个接口，自动生成的类实现了这个接口的所有方法
>
> ### 获取代理核心函数
>
> * ```java
>   IVehical car = new Car();
>   IVehical vehical = (IVehical)Proxy.newProxyInstance(car.getClass().getClassLoader(), Car.class.getInterfaces(), new VehicalInvacationHandler(car));
>   vehical.run();
>   ```
>
> * 类加载器、接口所有方法、InvocationHandler
>
> * 所有的方法都被handler所管理
>
> ### 要求
>
> * 被代理类必须实现一个接口，因为最后是通过接口获取被代理的方法
>
> ### 最后
>
> * 看到`$Proxy0`就是动态代理
>
> ***

## 原型模式

> ### 定义
>
> * `Prototype`
> * `Specify the kinds of objects to create using a prototypical instance, and create new objects by copying this prototype.`
>
> ### Clone接口
>
> * 没有方法
> * 只是JVM的一个标记
> * 实现了此接口才能`clone()`方法
> * @Override clone()方法。一般不是重写父类的，因为父类一般没有，一般都是重写Object类
>
> ### 示例
>
> ```java
> public class PrototypeClass implements Clonable{
>   @Override
>   public PrototypeClass clone{
>     ProtypeClass cloneObject = null;
>     try{
>       cloneObject = (PrototypeClass)super.clone()
>     }catch(Exception e){
> 
>     }
>     return cloneObject;
>   }
> }
> ```
>
> ### 优点
>
> * 性能好。它是二进制流的拷贝，性能十分好。
> * 逃避构造函数：因为是二进制(堆内存)拷贝，因此不用执行构造函数
>
> ### 使用场景
>
> * 对象创建消耗极大
> * 一般和工厂模式配套使用
>
> ### 使用流程
>
> * 一般都死有一个原型对象。然后无限clone，小修小补就又是一个新的对象
>
> ### 浅拷贝
>
> * 对于非基本变量类型的成员变量。拷贝出的对象和原对象指向堆内存中的成员变量地址是一模一样的
> * 因此浅拷贝一处修改全部乱套。
>
> ### 深拷贝
>
> ```java
> public class Thing implements Clonable{
>   private List<String> list = new ArrayList();
>   @Override
>   public Thing clone{
>     Thing cloneObject = null;
>     try{
>       cloneObject = (Thing)super.clone()
>         this.list = (ArrayList<String>)this.list.clone();
>     }catch(Exception e){
> 
>     }
>     return cloneObject;
>   }
> }
> ```
>
> ### 注意
>
> * 继承关系中深拷贝和浅拷贝不能混着乱用，不然半深不浅。
> * 浅拷贝用得很少，但并非不用。用得好甚至能救命
> * 不可变成员变量不能克隆，即加上final关键字的
>
> ***

## 中介者模式

> ### 定义
>
> * `Mediator Pattern`
> * `Define an objecet that encapsulates how a set of objects interact. Mediator promotes loose coupling by keeping objects from refrering to each other explicitly. and it lets you vary their interaction independenty.`
>
> ### 角色
>
> * Mediator
> * Colleague
>   * 自身行为：不依赖中介者和其他同事类的行为
>   * 依赖行为：要依赖中介者，间接依赖其他同事类的行为
>
> ### 优点
>
> * 减少了同事类之间的依赖
> * 避免了蜘蛛网结构
>
> ### 缺点
>
> * 中介者会膨胀得十分巨大
>
> ### 使用场景
>
> * 不能为了使用而去使用
> * 有时简单的依赖关系，使用了中介者模式反而变得越来越复杂，越来越难以理解。
> * 使用的判断依据可以根据：**是否在类图中出现了网状结构**，使用中介者模式能把网状改为星状
>
> ### 中介者模式举例
>
> * 飞机场调度系统
> * 消息服务的中间服务器(媒介网关)
> * MVC框架的Controller
>
> ### 最佳实践
>
> * 同事类各有各的不同，很难定义出一个统一的接口。因此可以直接使用实现类，不能为了抽象而抽象
>
> ***

## 命令模式

> ### 定义
>
> * `Encapsulate a request as an object, thereby letting you parameterize clients with diffiernt requests,queue or log requests, and support undoalbe oprations.`
> * 是一个高内聚的模式
>
> ### 示例
>
> * 真实接收者
>
> ```java
> public class Reciver extends IReciver{
>   public void doSomething(){
>     System.out.println("do something");
>   }
> }
> ```
>
> * 抽象命令
>
> ```java
> public abstract class ICommand{
>   protected RequireGroup rg = new RequireGroup();
>   protected CodeGourp cg = new CodeGroup();
>   protected UIGroup ug = new UIGroup();
>   public abstract excute();
> }
> ```
>
> * 真实命令
>
> ```java
> public class Command extends ICommand{
>   private IRecevier recevier;
>   public Command(IRecevier recevier){
>     this.recevier = recevier;
>   }
>   public void excute(){
>     this.revevier.doSomething();
>   }
> }
> ```
>
> * 真实发令者
>
> ```java
> public class Invoker{
>   private ICommand commond;
>   public void setCommond(ICommand commond){
>     this.commond = commond;
>   }
>   public void excute(){
>     this.commond.excute();
>   }
> }
> ```
>
> * 场景类
>
> ```java
> Invocker zhangsan = new Invoker();
> IRecevier recevier = new Recevier();
> ICommand commond = new Commond(recevier);
> zhangsan.setCommond(commond);
> zhangsan.excute();
> ```
>
> ### 优点
>
> * 解耦：调用者和接收者(实际处理者)无任何依赖关系
> * 拓展：Command子类非常容易拓展
>
> ### 缺点
>
> * 一个命令对应一个`Command`子类，可能会导致子类数量膨胀
>
> ### 适用场景
>
> * 只要认为是命令的地方都可以使用命令模式
> * 如按钮点击、DOS命令行
>
> ***

## 责任链模式

> ### 定义
>
> * `Chain of Responsibility Pattern`
> * `Avoid coupling the sender of a request to its recevier by giving more than one object a chance to handle the request. Chain the receving objects and pass the request along the chain until one object handles it.`
>
> ### 示例
>
> * 抽象Handler
>
> ```java
> public abstract class AbstractHandler{
>   private AbstractHandler next;
>   public void setHandler(AbstractHandler handler){
>     this.next = handler;
>   }
>   protected abstract Level getLevel();
>   protected abstract Response echo(Request request);
>   public final Response handlMessage(Request request){
>     Response response = null;
>     if(this.getLevel().equals(request.getLevel())){
>       response = this.echo(request);
>     }else{
>       if(this.next != null){
>         response = this.next.handleMessage(requeset);
>       }else{
>         throw Exception();
>       }
>     }
>   }
> }
> ```
>
> * `public final Response handlMessage(Request request)`是个模版方法
>
> ### 三个职责
>
> * 定义对外暴露的处理逻辑方法，在这是一个每个实现类都有的模版方法
> * 设置链条下一个处理者
> * 定义自己的处理级别，和当自己真正处理时的真实行为。
>
> ### 最后结果
>
> * 可能有处理也可能没有处理，这就是纯责任链和非纯责任链的区别
>
> ### 优点
>
> * 分离：请求者不用知道是谁处理的。处理者不用知道请求者的全貌
>
> ### 缺点
>
> * 性能问题，请求又可能从头遍历到尾。因此通常设置最大链条
> * 调试困难。想想链表的调试就知道了，调用关系就很离谱
>
> ### 最佳实践
>
> * 责任链模式可以作为补救措施。如最开始只支持处理人民币，结果后来币种越来越多，就使用责任链模式拓展
> * 普通用户和VIP用户不同的处理逻辑，可以使用责任链。先统抛给一个，然后在按照链条处理。
>
> ### 注意
>
> * 责任链的顺序可以有优先级之分。如果没有优先级，也可以使用责任链，那就是完全遍历了。
>
> ***

## 装饰者模式

> ### 定义
>
> * `Decorator Pattern`
> * `Attach additional responsibilities to an object dynamically keeping the same interface. Decorates provide a flexiable alternative to subclassing for extending functionality.`
>
> ### 角色说明
>
> * Component：一个接口或实现类。定义最核心最原始的对象
> * ConcrateComponent：接口的实现
> * Decorator：抽象类。有一个指向Component的指针
> * ConcretDecorator:实现具体的装饰逻辑
>
> ### 示例
> * Component
> ```java
> public abstract class Component {
> 	//抽象的方法
> 	public abstract void operate();
> }
> ```
>
> * ConcreteComponent
>
> ```java
> public class ConcreteComponent extends Component {
> 	//具体实现
> 	@Override
> 	public void operate() {
> 		System.out.println("do Something");
> 	}
> }
> ```
>
> * Decretor
>
> ```java
> public abstract class Decorator extends Component {
> 	private Component component = null;
> 	
> 	//通过构造函数传递被修饰者
> 	public Decorator(Component _component){
> 		this.component = _component;
> 	}
> 	
> 	//委托给被修饰者执行
> 	@Override
> 	public void operate() {
> 		this.component.operate();
> 	}
> 
> }
> ```
>
> * ConcreteDecretor
>
> ```java
> public class ConcreteDecorator extends Decorator {
> 	
> 	//定义被修饰者
> 	public ConcreteDecorator(Component _component){
> 		super(_component);
> 	}
> 	
> 	//定义自己的修饰方法
> 	private void method1(){
> 		System.out.println("method1 修饰");
> 	}
> 	
> 	//重写父类的Operation方法
> 	public void operate(){
> 		this.method1();
> 		super.operate();
> 	}
> }
> ```
>
> * client
>
> ```java
> public class Client {
> 	
> 	public static void main(String[] args) {
> 		Component component = new ConcreteComponent();
> 		
> 		//第一次修饰
> 		component = new ConcreteDecorator1(component);
> 		
> 		//第二次修饰
> 		component = new ConcreteDecorator2(component);
> 		
> 		//修饰后运行
> 		component.operate();
> 	}
> }
> ```
>
> ### 理解
>
> * 和代理模式很像
> * 装饰者模式的装饰对象和被装饰对象都有共同的父类，他们可以彼此声明类型
> * **可以进行多次修饰**，一直嵌套
>
> ### 优点
>
> * 装饰类和被装饰类可以独立发展，互不耦合
> * 不管装饰多少层，最后返回的都是Component
> * 可以 **动态拓展** 一个类的功能
>
> ### 缺点
>
> * 可以套很多层就是一个缺点
>
> ### 使用场景
>
> *  给一个类新增功能
> * 动态给一个类新增功能，然后在撤销
>
> ***

## 策略模式

> ### 定义
>
> * `Strategy Pattern`
> * `Define a family of algorithms, encapsulate each other, and make them interchangeable.`
>
> ### 示例
>
> * Strategy
>
> ```java
> public interface Strategy {
> 	//策略模式的运算法则
> 	public void doSomething();
> }
> ```
>
> * ConcreteStategy1
>
> ```java
> public class ConcreteStrategy1 implements Strategy {
> 	public void doSomething() {
> 		System.out.println("具体策略1的运算法则");
> 	}
> }
> ```
>
> * Context
>
> ```java
> public class Context {
> 	//抽象策略
> 	private Strategy strategy = null;
> 	//构造函数设置具体策略
> 	public Context(Strategy _strategy){
> 		this.strategy = _strategy;
> 	}
> 	//封装后的策略方法
> 	public void doAnythinig(){
> 		this.strategy.doSomething();
> 	}
> }
> ```
>
> * Client
>
> ```java
> public class Client {
> 	public static void main(String[] args) {
> 		//声明出一个具体的策略
> 		Strategy strategy = new ConcreteStrategy1(); 
> 		//声明出上下文对象
> 		Context context = new Context(strategy);
> 		//执行封装后的方法
> 		context.doAnythinig();
> 	}
> }
> ```
>
> ### 缺点
>
> * 不符合迪米特法则，client需要知道所有的策略，不管它是否使用。有一定风险
>
> ### 使用场景
>
> * 多个类只在算法或者行为上稍微有区别
> * 需要屏蔽策略细节的情况
>
> ### 枚举策略模式
>
> * Calculate
>
> ```java
> public enum Calculator {
> 	//加法运算
> 	ADD("+"){
> 		public int exec(int a,int b){
> 			return a+b;
> 		}
> 	},
> 	//减法运算
> 	SUB("-"){
> 		public int exec(int a,int b){
> 			return a - b;
> 		}
> 	};
> 	
> 	String value = "";
> 	//定义成员值类型
> 	private Calculator(String _value){
> 		this.value = _value;
> 	}
> 	//获得枚举成员的值
> 	public String getValue(){
> 		return this.value;
> 	}
> 	
> 	//声明一个抽象函数
> 	public abstract int exec(int a,int b);
> }
> ```
>
> * Client
>
> ```java
> public class Client {
> 	
> 	public static void main(String[] args) {
> 		//输入的两个参数是数字
> 		int a = Integer.parseInt(args[0]);
> 		String symbol = args[1];  //符号
> 		int b = Integer.parseInt(args[2]);
> 		System.out.println("输入的参数为："+Arrays.toString(args));
> 	
> 		System.out.println("运行结果为："+a + symbol + b + "=" + Calculator.ADD.exec(a, b));
> 		
> 	}
> }
> ```
>
> ***

## 适配器模式

> ### 定义
>
> *  `Adapter Pattern`
> * `Convert the interface of a calss into annother interface clients expected. Adapter lets classes work together that couldn't otherwise because of incompatible interfaces.`
> * 把一个不能使用的接口或类转换成能使用的接口
> * 即静态类型是接口不变，但动态类型可以变化。可以是原本的实现类，也可以是其他被封装过的不能使用的类
>
> ### RMI
>
> * `Remote Method Invocation`-远程对象调用
> * 只要有接口，可以把远程的对象当作本地对象用
>
> ### 示例
>
> * Target - 原接口
>
> ```java
> public interface Target {
> 	//目标角色有自己的方法
> 	void request();
> }
> ```
>
> * ConcreteTarget - 原能使用的实现类
>
> ```java
> public class ConcreteTarget implements Target {
> 	public void request() {
> 		System.out.println("I have nothing to do. if you need any help,pls call me!");
> 	}
> }
> ```
>
> * Adptee - 不能使用的其他的类或接口
>
> ```java
> public class Adaptee {
> 	//原有的业务逻辑
> 	public void doSomething(){
> 		System.out.println("I'm kind of busy,leave me alone,pls!");
> 	}
> }
> ```
>
> * Adapter - 适配器
>
> ```java
> public class Adapter extends Adaptee implements Target {
> 	public void request() {
> 		super.doSomething();
> 	}
> }
> ```
>
> * Client
>
> ```java
> public class Client {
> 	public static void main(String[] args) {
> 		//原有的业务逻辑
> 		Target target = new ConcreteTarget();
> 		target.request();
> 		//现在增加了适配器角色后的业务逻辑
> 		Target target2 = new Adapter();
> 		target2.request();
> 	}
> }
> ```
>
> * 核心是`public class Adapter extends Adaptee implements Target`
>
> ### 优点
>
> * 让没有关系的两个类或者接口协同合作
> * 适配器不想用就删除
>
> ### 使用场景
>
> * 当你想要已经投产的接口时，可以考虑使用适配器模式
> * 但在架构设计时是万万不能使用适配器模式的。适配器模式只是一个补救措施，不是设计思路。
>
> ### 对象适配器模式
>
> * 类适配器模式就是上面的
> * 类适配器实现原接口，继承不能使用的类或接口
> * 对象适配器实现原接口，聚合(组合)不能使用的类或接口
>
> ### 对象适配器模式源代码
>
> ```java
> public class OuterUserInfo implements IUserInfo {
> 	//源目标对象
> 	private IOuterUserBaseInfo baseInfo = null;  //员工的基本信息
> 	private IOuterUserHomeInfo homeInfo = null; //员工的家庭 信息
> 	private IOuterUserOfficeInfo officeInfo = null; //工作信息
> 	
> 	//数据处理
> 	private Map baseMap = null;
> 	private Map homeMap = null;
> 	private Map officeMap = null;
> 	
> 	//构造函数传递对象
> 	public OuterUserInfo(IOuterUserBaseInfo _baseInfo,IOuterUserHomeInfo _homeInfo,IOuterUserOfficeInfo _officeInfo){
> 		this.baseInfo = _baseInfo;
> 		this.homeInfo = _homeInfo;
> 		this.officeInfo = _officeInfo;
> 		
> 		//数据处理
> 		this.baseMap = this.baseInfo.getUserBaseInfo();
> 		this.homeMap = this.homeInfo.getUserHomeInfo();
> 		this.officeMap = this.officeInfo.getUserOfficeInfo();
> 	}
> }
> ```
>
> ### 单一职责
>
> * 类的职责可以不单一
> * 但接口的职责一定要单一
>
> ### 最佳实践
>
> * 适配器模式分为两种——类适配器和对象适配器
> * 一个是用继承复用，一个是用聚合复用
> * 适配器模式是一个补救措施，而不是一个设计思路
> * 对象适配器模式一般使用得比类适配器模式多
> * 对象适配器就是把现接口方法的逻辑委托给传入的对象
>
> ***

