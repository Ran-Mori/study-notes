# Go Lang

## 序言

> ### 译者序
>
> * **Unix操作系统**和**C系家族语言**可以说成是整个计算机体系的根基
> * 为了解决多核网络环境下越来越复杂的编程问题而发明了go语言
> * go已渐成云计算、云服务的时代最重要的基础编程语言
>
> ### go特点
>
> * 并发编程特性很新
> * 数据抽象和面向对象编程十分灵活
> * 自动垃圾收集GC
> * 适合编写网络服务的基础设施
> * 一次编写到处运行
> * 借鉴其他语言，集大成之作
>
> ### go语言项目
>
> * go的核心精髓 —— **简洁**
> * 必须要有的特性一定有，可有可无的特性就选择无
>
> ***

## 第一章 - 入门

> ### Hello World
>
> * go语言在代码格式上采取了很强硬的态度
> 
> ### 格式化
>
> * `fmt.Printf("two = %b,eight = %o,ten = %d,sixteen = %x",100,100,100,100)`
> 
> ***

## 第二章 - 程序结构

> ### 命名
>
> * 小写字母开头的定义在包外是不可见的，但在包内是可见的
> * 即不论是否大小写在包内都是可见的，大小写只决定包外的可见性
>
> ### 变量
>
> * 基础格式`var 变量名 变量类型 = 变量值`
> * 如果没有类型会根据变量值自动推导
> * 即使没有变量值也会初始化零值
> * go语言不存在未初始化的变量
>
> ### 简短变量
>
> * 基础格式`变量名 := 变量值`
> * 简短变量只能够在函数中使用，在`.go`文件内声明变量必须严格使用`var`格式
> * 变量类型根据变量值自动推导
> * `=`是一个赋值符号，而`:=`是一个声明符号
> * 多个简短变量联合声明必须保证左边至少有一个变量是声明
>
> ### 指针
>
> * go不像c语言，go的函数内的指针在返回后依然有效，而c则是无效
> * `go`有语法糖。本应该使用`(*p).field`的地方可以使用`p.field`
>
> ### new()函数
>
> * 返回的是一个指针
> * 用new()创建和普通的声明创建没有任何区别，只是一种语法糖
>
> ### 变量生命周期
>
> * 包级的变量生命周期和整个程序的生命周期是相同的，而局部变量则是不同情况生命周期不同
>
> * 局部变量生命周期 **只取决于是否可达**，因此生命周期可以超过作用域，如：
>
>   ```go
>   var global *int
>   func f(){
>       var x int
>       x = 1
>       global = &x
>   }
>   ```
>
> * 即x的生命周期被延伸了
>
> ### 变量分配位置问题
>
> * 栈上分配和堆上分配不取决于用`new()`还是用`var`，而且`new()`本身就是语法糖
> * go会自动判断在堆上分配还是在栈上分配
> * 即上面的x常理来看应该在栈上分配，然而实际上是在堆上进行分配
>
> ### 元组赋值
>
> * 太猛了，交换变量直接一步搞定
> * `x, y = y, x`
>
> ### 类型
>
> * 基础语法`type 类型名字 底层类型`
> * 底层类型一般为`struct`，如果不是`struct`则为类型另外取名
>
> ### 包的初始化和依赖
>
> * 导入得不好会最后搞成循环依赖
> * 包会进行初始化，且只进行一次初始化，最后初始化的是main包
> * 可以定义`func init(){}`来进行初始化
>
> ### 作用域与生命周期
>
> * 作用域是有效的文本范围，是一个编译时属性
> * 生命周期是存在的时间段，是一个运行时属性
> * `for`、`if`这种语句在判断时就有一个作用域
>
> ### For
>
> ```go
> sum := 0
> for i := 1; i <= 100; i++ {
>   sum += i
> }
> 
> //for就是while
> for x > 0 {
>   
> }
> ```
>
> ### If
>
> ```go
> if x < 0 {
>   x = -x
> } else if {
>   x = x	
> }
> 
> //变量v只在if体内生效
> if v := math.Pow(x, n); v < lim {
>   v = lim
> }
> ```
>
> ### Swich
>
> ```go
> switch os := runtime.GOOS; os {
>   case "darwin":
>   	fmt.Println("OS x")
>   case "linux":
>   	fmt.Println("linux")
>   default:
>   	fmt.Println("others")
> }
> ```
>
> ### defer
>
> * 直到方法体结束`defer`才执行
> * 同一个方法体多个`defer`按照顺序压栈，先进后执行
>
> ```go
> fmt.Println("counting")
> for i := 0; i < 10; i++ {
>   defer fmt.Println(i)
> }
> fmt.Println("done")
> ```
>
> ***

## 第三章 - 基础数据类型

> ### go的基础数据类型
>
> * `string、int、float、complex`
> * 其中`int`有`8,16,32,64,u8,u16,u32,u64`。`int、uint`位宽取决于机器大小
> * `byte`其实是`uint8`
> * `rune`其实是`int32`
>
> ### 强制转换
>
> * `Type(value)`
>
> ### go语言四种类型
>
> * 基础类型
> * 复合类型
> * 引用类型
> * 接口类型
>
> ### 等价类型
>
> * `type rune int32`    `type byte uint8`   
> * 即实际底层类型是一样的
>
> ### 复数
>
> * `complex64`   `complex128` 分别对应32位和64位的 float
> * 复数声明`x := 3 + 4i`，`var x complex128 = complex(3,4)`
>
> ### 字符串
>
> * 和Java一样，go语言中的字符串是不可变的
> * 字符串是采用UTF-8进行编码的
> * `len()`函数返回的是字符串的**字节数目**而不是**rune**数目，这点要做区分。如`len("大人") = 6`
> * `s[i:j]`和Java的`substring(i,j)`一样，会重新产生一个字符串
> * 字符串支持比较，比较原则就是字符串的字典序
> * 由于字符串不变，因此某些情况下可以高效地共享底层的内存而不用重新分配，如进行字符串复制和切片的时候
>
> ### Unicode
>
> * `rune`对应的是一个`int32`类型，即Unicode每个码点都占4字节，即使是ASCII码都占4字节
>
> ### UTF-8
>
> * 一种将Unicode码点编码为字节序列的**变长**编码
> * 每个字符用`1-4`个字节来表示，在保证能表达信息的前提下使文本占据的体积尽量小
> * 中间不设置分隔符，即使用前缀编码
> * 缺点是无法通过索引直接定位到某一字符，有那么一点链表的感觉了
>
> ### iota使用
>
> * iota初始值被设置为0，用于常量的初始化操作
> * 如`B int = 1 << (iota * 10)`
>
> ### 无类型常量
>
> * 为一个常量提供了至少256位的精度，则无论在任何地方进行运算都能保证很高的精度
> * 只有常量能被声明为无类型，变量则不能
>
> ***

## 第四章 - 复合数据类型

> ### Struct
>
> ```go
> type Song struct {
>   Id int
>   Name string
>   Singer string
> }
> ```
>
> 
>
> ### 数组
>
> * 大小固定，很少使用，除非能确定大小，否则不使用数组
> * 数组的长度必须是常量，即必须在编译期确定大小，如果不能确定，就不应该使用数组
> * 相同长度的数组可以进行大小比较
> * go语言也是和Java一样是值传递，因此传入一个数组要进行复制效率肯定比较低，因此大多数是传递数组的指针
> * 声明
>
> ```go
> var array [2]int //自动初始为零值
> var array = [2]int{1, 2} //手动初始化
> ```
>
> ### Slice
>
> * 可以理解成Java里面的List
> * Slice有两个重要属性
>   * cap值：代表现在底层数组的实际大小。实际上底层就是一个数组
>   * len值：代表现在实际正在使用的有多少
> * 即使`cap`够但访问范围超过`len`依然是不允许的
> * slice是不能进行比较的
> * slice不存储数据，它只是一段数组的引用
> * 声明
>
> ```go
> //截取固定长度数组式声明
> primes := [6]int{2, 3, 5, 7, 11, 13}
> s := primes[1:4]
> 
> //使用make声明
> a := make([]int,2,3) //len=2,cap=3
> a[2] = 2//会报错
> a := a[0,3]//把len从2扩成3
> a[2] = 2//不会报错
> ```
>
> * append cap不够会重新分配内存
>
> ```go
> old := make([]int,2,2)
> new := append(old,2)
> ```
>
> * range
>
> ```go
> q := make([]int,2,2)
> for i,v := range q {
>   fmt.Println(i,v)
> }
> ```
>
> ### map
>
> * 声明：`m := make(map[string]string)`
> * 赋值：`m["izumisakai"] = "So together"`
> * 读值：`elem, ok = m[key]`
> * 删除：`delete(m, key)`
>
> ### json
>
> * 编码`json.Marshal()`
> * 解码`json.Unmarshal()`
>
> ### 封装
>
> * go语言可见性是由字段的大小写决定的
> * 因此go语言封装的最小啊单位是 **包package**
>
> ### 方法
>
> * 方法的返回值可以是多个
>
> ```go
> func swap(x, y int) (int, int) {
>   return y, x
> }
> ```
>
> * 返回值命名和裸返回
>
> ```go
> func split(sum int) (x, y int) {
> 	x = sum * 4 / 9
> 	y = sum - x
> 	return
> }
> ```
>
> * 指定方法的接收者. `a method is just a function with a receiver argument.`
>
> ```go
> func (s *SongController)QueryAll() {}
> ```
>
> * 方法的接受者可以是对象也可以是指针，当对象能够直接`.`方法调用接受者是指针的方法时，这是go的语法糖，会隐式地做一个类型转换
> * go推荐函数接收者使用指针而不是对象
>   * `The first is so that the method can modify the value that its receiver points to.`
>   * `The second is to avoid copying the value on each method call. This can be more efficient if the receiver is a large struct`
>
> ### 高阶函数
>
> ```go
> func main() {
> 	a := func(x int, y int) int { return x + y }
> 	fmt.Println(add(a,1,2))
> }
> 
> func add(add func(x int, y int) int, x int, y int) int {
> 	return add(x, y)
> }
> ```
>
> ### 闭包
>
> ```go
> func getSequence() func() int {
>    i:=0
>    return func() int {
>       i+=1
>      return i  
>    }
> }
> 
> func main(){
>    /* nextNumber 为一个函数，函数 i 为 0 */
>    nextNumber := getSequence()  
> 
>    /* 调用 nextNumber 函数，i 变量自增 1 并返回 */
>    fmt.Println(nextNumber()) // 1
>    fmt.Println(nextNumber()) // 2
>    fmt.Println(nextNumber()) // 3
>    
>    /* 创建新的函数 nextNumber1，并查看结果 */
>    nextNumber1 := getSequence()  
>    fmt.Println(nextNumber1()) // 1
>    fmt.Println(nextNumber1()) // 2
> }
> ```
>
> 
>
> ***

## 第七章 - 接口

> ### go接口特点
>
> * 不需要显示地声明实现了某个接口，只需要实现类含有特定的方法就行
>
> * 类似Java里面的`toString()`，go语言有一个接口如下
>
>   ```go
>   type Stringer interface{
>       String() string
>   }
>   ```
>
> ### 空接口
>
> * `interface{}`空接口类型对实现它的类型没有任何要求，因此可以把任何一个类型赋值给空接口
> * 因为每个类都实现了没有任何方法的接口
> * 有那么一点Java的Object内味了
>
> ### 接口的值
>
> * 接口的值就是一个具体的类型和那个类型的值
> * 即如`var w io.Writer = os.Stdout`，此时w的type类型值为`*os.File`，它的type的值为`os.Stdout`的指针副本
> * `var x interface{} = time.Now()`，不论接口值多大，理论上动态值都能把它容下
> * 接口类型不为空，但接口类型的实际值为空调用方法时不会空指针
> * `Note that an interface value that holds a nil concrete value is itself non-nil.`
>
> ```go
> type I interface {
> 	M()
> }
> 
> type T struct {
> 	S string
> }
> 
> func (t *T) M() {
> 	if t == nil {
> 		fmt.Println("<nil>")
> 		return
> 	}
> 	fmt.Println(t.S)
> }
> 
> func describe(i I) {
> 	fmt.Printf("(%v, %T)\n", i, i)
> }
> 
> func main() {
> 	var i I
> 	var t *T
> 	i = t
>   //此处不会报空指针异常
>   describe(i) // (<nil>, *main.T)
> }
> ```
>
> * 接口类型推断
>
> ```go
> //i是一个接口。现在在判断i的类型是不是string，如果不是，ok = false，s为string的零值""
> s, ok := i.(string)
> ```
>
> * type swich
>
> ```go
> func do(i interface{}) {
> 	switch v := i.(type) {
> 	case int:
> 		fmt.Printf("Twice %v is %v\n", v, v*2)
> 	case string:
> 		fmt.Printf("%q is %v bytes long\n", v, len(v))
> 	default:
> 		fmt.Printf("I don't know about type %T!\n", v)
> 	}
> }
> ```
>
> ### Sort接口
>
> * sort接口代码
>
>   ```go
>   package sort
>   type Interface interface{
>       Len() int
>       Less(i,j int) bool
>       Swap(i,j int)
>   }
>   ```
>
> * 如果要自定义排序就实现上面三个方法就行了
>
> ### Error接口
>
> * 一般都直接调用`fmt.Errorf(format string, args ...interface{}) error`方法
>
> ### 接口断言操作
>
> * 总结概括理解起来就是判断此接口对象的类型
> * 如`if _,ok := w.(* os.File); ok{ ... }`
>
> ***

## 第八章 - Goroutines和Channels

> ### goroutines
>
> * 在go语言中，每一个并发执行的单元叫做一个goroutine
> * 主函数在main goroutine执行
> * 主函数返回时，所有其他的goroutine都会被直接打断
> * 除了从主函数退出或者直接终止程序外，没有其他的编程方式能让一个goroutine打断另外一个goroutine的执行
>
> ### go关键字
>
> * `f()`调用函数f()并等待返回
> * `go f()`创建一个goroutine，调用f()并不等待返回继续执行
>
> ### channel
>
> * 可以让一个goroutine通过channel给另一个goroutine发送消息
> * 创建：`ch := make(char 发送的类型, size)`
> * 发送：`ch <- x` ，接收：`x = <- ch`  ，关闭`close(channel)`
>
> ### 不带缓存的channel
>
> * 无缓存channel(即size默认为0的channel)的发送操作会使自身陷入阻塞状态，直到被接收方成功接收才会继续执行
> * 如果接收着接收无缓存的channel，也会同样陷入阻塞状态
> * 有那么一点同步阻塞内味了，实际就是一次同步操作
>
> ### Happen before原则
>
> * 通常说的X时间要在Y事件之前发生，不是说的X事件一定比Y事件先发生，而是指必须等X事件完成后才能进行B事件
> * 即开始时间的先后没有关系，我们只看衔接的先后顺序
>
> ### 管道
>
> * 将多个channel串联起来就形成了管道
>
> ### 单方向channel
>
> * `chan<- type`只发送不接收
> * `<-chan type`只接收不发送
> * 将双向的channel赋值给单向的channel会做隐式的类型转换，而倒过来赋值则可能会报错
>
> ### 带缓存的channel
>
> * 创建：`var ch := make(chan int,3)`，其内含一个元素队列
> * 真的有消息队列内味了，发送操作就是向队尾插入元素，接收操作就是从队头取元素
> * `cap(channel)`返回的是元素队列的大小，`len(channel)`返回的是元素队列中含有多少个消息
> * 当队列满了继续发送时会进入阻塞状态，当队列空了继续读入也会陷入阻塞状态
> * 应用举例：并发地从三个源处获取信息，信息返回存放在channel中，且channel缓存设置成3，最后只从缓存中读取一次并做最后的返回，这样就能获取最先返回的内容，且慢的也不会被阻塞
>
> ### goroutine泄露
>
> * 某goroutine因为没人接收或等待读入而永远卡住，泄露的goroutine不会被自动回收，会对系统造成很大的风险
>
> ### select多路复用
>
> * select语句使用
>
>   ```go
>   select{
>       case <-ch :
>       //...
>       case x := <-ch2
>       //...
>   }
>   ```
>
> * 整体结构类似于switch - case语句
>
> * select会等待case中有一个然后去执行，当case条件满足时就去执行其中一个case。
>
> * 一个没有任何case的select语句会永远阻塞在那里
>
> * 当多个case同时满足时会随机选择一个进行执行
>
> * 举例
>
>   ```go
>   ch := make(chan int, 1)
>   for i := 0;i < 10;i++{
>       select {
>           case ch <- i:
>       case x := <-ch:
>           fmt.Println(x)
>       }
>   }
>   ```
>
> ***

## 第九章 - 基于共享变量的并发

> ### sync.Mutex
>
> * 就是Java里面的锁
> * 只不过这个锁是不可重入的，和Java稍微有些不一样
> * 特点是无论读和写都会上锁和释放锁
>
> ### sync.RWMutex
>
> * 读时不上锁，写时才上锁
>
> ### goroutine和线程
>
> * 动态栈：goroutine的栈大小是可以动态变化的，因此可以创建成千上万的goroutine
> * 调度：goroutine的调度类似于协程调度
>
> ### GOMAXPROCS
>
> * go中的一个变量，用于决定会有几个CPU核心数来执行go代码
> * 也就是goroutine中m:n中的n的数量
> * 一般情况默认值是CPU的核心数
> * 即当GOMAXPROCS被设置为1时，整个go程序任意时刻只能有一个goroutine在运行，其他goroutine都在啊休眠。只有当GOMAXPROCS数量大于1时，才能实现真正地并行
>
> ### goroutine没有ID号
>
> * 其他语言中的线程一般都有一个唯一的线程ID号
> * 拥有这个ID号后，做ThreadLocal就十分方便
> * 但goroutine并没有ID号，这体现了go语言的简洁性
>
> ***

## 第十章 - 包和工具

> ### go语言编译快速的原因
>
> * 所有导入的包必须显示声明，这样编译器就不用分析整个源文件来判断依赖关系
> * 禁止包的循环依赖，这样就更简单甚至可以并发编译
> * 编译后的包不仅记录包本省的导出信息，目标文件同时还记录包的依赖关系
>
> ### 导入包的重命名
>
> * 当导入的两个包的最尾部名字相同时
> * 导入时要自定义一个别名来给两个包名做区分，不然就会混淆
>
> ### 匿名导入
>
> * 使用`_`
>
> ### GOPATH
>
> * src：用于存放源代码文件
> * pkg：用于存放下载的包
> * bin：用于存放编译后的可执行程序
>
> ### go test
>
> * 所有`_test.go`结尾的文件都是测试的一部分，在go build的时候不会被build
> * 三种测试函数：
>   * 测试函数：以`Test`为前缀的函数
>   * benchmark函数：以`Benchmark`为前缀的函数
>   * 示例函数：以`Example`为前缀的函数
> * 执行过程：扫描所有符合规则的函数，生成一个临时的main包来执行
>
> ***

## 第十二章 - 反射

> ### 应用举例
>
> * `fmt.Sprintf()`函数中的各种格式化输出
> * 需要动态获取输入的类型，这时候就使用反射
>
> #### 反射
>
> * 和Java的反射差不多，啥都能干
>
> ***

## 第十三章 - 底层编程

> ### 作者忠告
>
> * 能不用reflect和unsafe包就尽量别用
>
> ***

## 建立Go项目

> * 配置环境变量
>
> ```bash
> export PATH=$JAVA_HOME/bin:/Users/bytedance/enviroment/go/bin:$PATH
> export GO111MODULE=on
> ```
>
> * 使用Golang新建一个项目，路径为`$home/awesomeProject`
> * 在`$home/awesomeProject`下执行`go mod init awesome/project`命令生成`go.mod`文件
> * 设置环境变量`GO111MODULE`值为`on`，完全抛弃老旧的`GOPATH`模式包依赖
> * 此时路径关系如下
>
> ```bash
> - awesomeProject
> 	- src
> 		- model
> 			- song.go
> 		- main.go
> 	- go.mod
> ```
>
> * 在`main.go` 可以使用`import "awemesome/project/src/model"`引入自己自定义包
> * `main.go`
>
> ```go
> package main
> 
> import (
> 	"awemesome/project/src/model"
> 	"fmt"
> )
> 
> func main() {
> 	var song = model.Song{10}
> 	fmt.Print(song)
> }
> ```
>
> * `song.go`
>
> ```go
> package model
> 
> type Song struct {
> 	Id int
> }
> ```
>
> * `go.mod`
>
> ```go
> module awemesome/project
> go 1.16
> ```
>
> ***

