# Go Lang

## 序言

### 译者序

* **Unix操作系统**和**C系家族语言**可以说成是整个计算机体系的根基
* 为了解决多核网络环境下越来越复杂的编程问题而发明了go语言
* go已渐成云计算、云服务的时代最重要的基础编程语言

### go特点

* 并发编程特性很新
* 数据抽象和面向对象编程十分灵活
* 自动垃圾收集GC
* 适合编写网络服务的基础设施
* 一次编写到处运行
* 借鉴其他语言，集大成之作

### go语言项目

* go的核心精髓 —— **简洁**
* 必须要有的特性一定有，可有可无的特性就选择无

***

## 第一章 - 入门

### Hello World

* go语言在代码格式上采取了很强硬的态度

### 格式化

* `fmt.Printf("two = %b,eight = %o,ten = %d,sixteen = %x",100,100,100,100)`

***

## 第二章 - 程序结构

### 命名

* 小写字母开头的定义在包外是不可见的，但在包内是可见的
* 即不论是否大小写在包内都是可见的，大小写只决定包外的可见性

### 变量

* 基础格式`var 变量名 变量类型 = 变量值`
* 如果没有类型会根据变量值自动推导
* 即使没有变量值也会初始化零值
* go语言不存在未初始化的变量

### 简短变量

* 基础格式`变量名 := 变量值`
* 简短变量只能够在函数中使用，在`.go`文件内声明变量必须严格使用`var`格式
* 变量类型根据变量值自动推导
* `=`是一个赋值符号，而`:=`是一个声明符号
* 多个简短变量联合声明必须保证左边至少有一个变量是声明

### 指针

* go不像c语言，go的函数内的指针在返回后依然有效，而c则是无效
* `go`有语法糖。本应该使用`(*p).field`的地方可以使用`p.field`

### new()函数

* 返回的是一个指针
* 用new()创建和普通的声明创建没有任何区别，只是一种语法糖

### 变量生命周期

* 包级的变量生命周期和整个程序的生命周期是相同的，而局部变量则是不同情况生命周期不同

* 局部变量生命周期 **只取决于是否可达**，因此生命周期可以超过作用域，如：

  ```go
  var global *int
  func f(){
      var x int
      x = 1
      global = &x
  }
  ```

* 即x的生命周期被延伸了

### 变量分配位置问题

* 栈上分配和堆上分配不取决于用`new()`还是用`var`，而且`new()`本身就是语法糖
* go会自动判断在堆上分配还是在栈上分配
* 即上面的x常理来看应该在栈上分配，然而实际上是在堆上进行分配

### 元组赋值

* 太猛了，交换变量直接一步搞定
* `x, y = y, x`

### 类型重命名

* 基础语法`type 类型新名字 底层类型`
* 底层类型一般为`struct`，如果不是`struct`则为类型另外取名

### 包的初始化和依赖

* 导入得不好会最后搞成循环依赖
* 包会进行初始化，且只进行一次初始化，最后初始化的是main包
* 可以定义`func init(){}`来进行初始化

### 作用域与生命周期

* 作用域是有效的文本范围，是一个编译时属性
* 生命周期是存在的时间段，是一个运行时属性
* `for`、`if`这种语句在判断时就有一个作用域

### for

```go
sum := 0
for i := 1; i <= 100; i++ {
  sum += i
}

//for就是while
for x > 0 {

}
```

### if

```go
if x < 0 {
  x = -x
} else if {
  x = x	
}

//变量v只在if体内生效
if v := math.Pow(x, n); v < lim {
  v = lim
}
```

### swich

```go
switch os := runtime.GOOS; os {
  case "darwin":
  	fmt.Println("OS x")
  case "linux":
  	fmt.Println("linux")
  default:
  	fmt.Println("others")
}
```

### defer

* 直到方法体结束`defer`才执行

* 同一个方法体多个`defer`按照顺序压栈，先进后执行

  ```go
  fmt.Println("counting")
  for i := 0; i < 10; i++ {
    defer fmt.Println(i)
  }
  fmt.Println("done")
  ```

***

## 第三章 - 基础数据类型

### go语言四种类型

* 基础类型
* 复合类型
* 引用类型
* 接口类型

### 类型转换

* 强制转换 -> `Type(value)`
* 尝试转换 -> `value.(Type)`

### 查看字节数

* `unsafe.Sizeof(bool(false)`

### 基础数据类型

* `bool`
* `string`
* `int  int8  int16  int32  int64 uint uint8 uint16 uint32 uint64 uintptr`
* `float32 float64`
* `complex64 complex128`

### 布尔

* 长度为1个字节

### 字符串

* 在`runtime/string.go`下的定义

  ```go
  type stringStruct struct {
  	str unsafe.Pointer
  	len int
  }
  ```

* 长度 -> `16`字节；编码方式 -> `UTF - 8`；`len`含义 -> **字节数目**而不是**rune**数目

* 因为使用`utf-8`编码，因此涉及字符数组的编码与解码。实现在`unicode/utf8/utf8.go`下

* 字符串支持比较，比较原则就是字符串的字典序

* 不可变性

  * 和`Java`一样，`go`语言中的字符串是不可变的
  * `s[i:j]`和Java的`substring(i,j)`一样，会重新产生一个字符串
  * 由于字符串不变，因此某些情况下可以高效地共享底层的内存而不用重新分配，如进行字符串复制和切片的时候


### 整数

* 其中`int、uint、uintptr`是自适应机器字长的。`int、uint` + `8,16,32,64`这种是强制写死不适应机器字长
* `byte // alias for uint8`
* `rune // alias for int32`

### 复数

* `complex64`   `complex128` 分别对应32位和64位的 float
* 复数声明`x := 3 + 4i`，`var x complex128 = complex(3,4)`

### nil

* 定义 -> `builtin/builtin.go#nil`

  ```go
  // nil is a predeclared identifier representing the zero value for a
  // pointer, channel, func, interface, map, or slice type.
  var nil Type // Type must be a pointer, channel, func, interface, map, or slice type
  ```

* 含义

  * `nil`本质是一个变量，就是一个普通的变量而已
  * `nil`的类型实际有`6`种`type`，且是这6种type的`zero value`

### Unicode

* `type rune = int32`
* `rune`对应的是一个`int32`类型，即Unicode每个码点都占4字节，即使是ASCII码都占4字节

### UTF-8

* 一种将Unicode码点编码为字节序列的**变长**编码
* 每个字符用`1-4`个字节来表示，在保证能表达信息的前提下使文本占据的体积尽量小
* 中间不设置分隔符，即使用前缀编码
* 缺点是无法通过索引直接定位到某一字符，有那么一点链表的感觉了

### iota使用

* iota初始值被设置为0，用于常量的初始化操作
* 如`B int = 1 << (iota * 10)`

### 无类型常量

* 为一个常量提供了至少256位的精度，则无论在任何地方进行运算都能保证很高的精度
* 只有常量能被声明为无类型，变量则不能

***

## 第四章 - 复合数据类型

### struct

* 示例

  ```go
  type Song struct {
    Id int
    Name string
    Singer string
  }
  ```

* 空结构体

  ```go
  type Song struct {}
  ```

  * 内存大小

    * 在内存中所占的大小是`0`字节

    * `runtime/molloc.go` 
  
      ```go
      // base address for all 0-byte allocations
      var zerobase uintptr
      ```
  
    * 作用 -> 节约内存，如`hashset`的`value`就可以是空结构体
  
  * 值
  
    * 值不可能是`nil`，因为`nil`的6种类型中没有`struct`
    * 指针也不可能是`nil`，因为它的指针是`zerobase`即`uintptr`

### 数组

* 大小固定，很少使用，除非能确定大小，否则不使用数组
* 数组的长度必须是常量，即必须在编译期确定大小，如果不能确定，就不应该使用数组
* 相同长度的数组可以进行大小比较
* go语言也是和Java一样是值传递，因此传入一个数组要进行复制效率肯定比较低，因此大多数是传递数组的指针
* 声明

```go
var array [2]int //自动初始为零值
var array = [2]int{1, 2} //手动初始化
```

### slice

* 底层数据结构 -> `runtime/slice.go`

  ```go
  type slice struct {
  	array unsafe.Pointer
  	len   int
  	cap   int
  }
  ```

* 本质 -> 对数组的引用，可以理解成就是一个指针

* 两个重要属性
  * cap：代表现在底层数组的实际大小。实际上底层就是一个数组
  * len：代表现在实际正在使用的有多少

* 即使`cap`够但访问范围超过`len`依然是不允许的

* slice是不能进行比较的

* 声明

```go
//字面量声明, 本质是先创建一个数组，然后在创建了一个runtime/slice.go下的结构体
s := []int{1, 2, 3}


//截取固定长度数组式声明
primes := [6]int{2, 3, 5, 7, 11, 13}
s := primes[1:4]

//使用make声明，本质调用的是runtime/slice.go#makeslice(et *_type, len, cap int)方法
a := make([]int, 2, 3) //len=2,cap=3
a[2] = 2 //会报错
a := a[0,3] //把len从2扩成3
a[2] = 2 //不会报错
```

* `append`

  ```go
  old := make([]int, 2, 2)
  new := append(old, 2)
  ```

  * `cap`足够时调整一下`len`就可以了
  * `cap`不够时，原来底层数组不要了，扩大长度重新分配。实现在`runtime/slice.go#growslice()`
  * slice扩容时因为老的底层数组不要了，涉及数组替换。它是并发不安全的，可能需要上锁


* range

```go
q := make([]int, 2, 2)
for i,v := range q {
  fmt.Println(i,v)
}
```

### map

* 实际实现 -> `runtime/map.go`

* 底层数据结构 -> `runtime/map.go#hmap`

  ```go
  type hmap struct {
  	// Note: the format of the hmap is also encoded in cmd/compile/internal/reflectdata/reflect.go.
  	// Make sure this stays in sync with the compiler's definition.
  	count     int // # live cells == size of map.  Must be first (used by len() builtin)
  	flags     uint8
  	B         uint8  // log_2 of # of buckets (can hold up to loadFactor * 2^B items)
  	noverflow uint16 // approximate number of overflow buckets; see incrnoverflow for details
  	hash0     uint32 // hash seed
  
  	buckets    unsafe.Pointer // array of 2^B Buckets. may be nil if count==0.
  	oldbuckets unsafe.Pointer // previous bucket array of half the size, non-nil only when growing
  	nevacuate  uintptr        // progress counter for evacuation (buckets less than this have been evacuated)
  
  	extra *mapextra // optional fields
  }
  ```

* 初始化

  ```go
  //make -> runtime/map.go#makemap()
  m := make(map[string]struct{}, 10)
  
  //字面量
  m := map[string]struct{} {
    "a": {},
    "b": {},
  }
  ```

* 读写删

  ```go
  //读
  elem, ok = m[key]
  //写
  m["izumisakai"] = "So together"
  //删
  delete(m, key)
  ```

* 算法实现差异
  * `java`中的`HashMap`是用的`桶法`，每个桶后可能是一个链表，甚至可能是红黑树
  * `go`中的`map`也是用的`桶法`，但每个桶的大小是固定的`8`，当一个桶装不下时它有一个溢出桶的概念
  * 每个桶还存了8个key的`topHash`，方便在一个桶中找元素时快速找到
* 访问 -> `elem, ok = m[key]`，给`m`传入一个`key`尝试去读出`elem`
  1. `key + hmap.hash0` 得到`hashvalue`
  2. `hashvalue + hmap.B` 得到 `bmap`
  3. `hashvalue` 得到 `keyTopHashValue`
  4. `bmap.tophash 中找 keyTopHashValue ` 得到 `index`
     * 找不到则可能在`溢出桶`里
     * `溢出桶`都找不到说明根本没有这个`key`
  5. `bmap.keys[index]` 得到 `value_key`
  6. `value_key 与 key`进行比较，如果OK则真正的`elem`为`bmap.elems[index]`
     * 不OK则可能是`topHash`碰撞了，就重复`4`步骤
* 扩容
  * 溢出桶太多时 -> 实际就成了`java HashMap`的链表结构，只不过这里的链表的每一个节点是一个桶而不是一个值
  * 底层实现 -> `runtime/map.go#hashGrow(t *maptype, h *hmap)`
  * 扩容类型
    1. 等量扩容 -> 数据不多但是溢出桶太多了(常见于删除case)
    2. 翻倍扩容 -> 数据太多，溢出桶也多。采用渐进式扩容，不是一次就将所有老桶的数据迁移到所有新桶，而是每次写的时候将一个老桶数据迁移到两个新桶
* 并发
  * `map`是并发不安全的，极致追求性能
  * go语言提供了另外一个`sync/map.go`，它在原生的`map`上包了一层，是并发安全的。它不是读写分离，而是扩容操作与非扩容操作分离

### json

* 编码`json.Marshal()`
* 解码`json.Unmarshal()`

### 封装

* go语言可见性是由字段的大小写决定的
* 因此go语言封装的最小啊单位是 **包package**

### 方法

* 方法的返回值可以是多个

```go
func swap(x, y int) (int, int) {
  return y, x
}
```

* 返回值命名和裸返回

```go
func split(sum int) (x, y int) {
	x = sum * 4 / 9
	y = sum - x
	return
}
```

* 指定方法的接收者. `a method is just a function with a receiver argument.`

```go
func (s *SongController)QueryAll() {}
```

* 方法的接受者可以是对象也可以是指针，当对象能够直接`.`方法调用接受者是指针的方法时，这是go的语法糖，会隐式地做一个类型转换
* go推荐函数接收者使用指针而不是对象
  * `The first is so that the method can modify the value that its receiver points to.`
  * `The second is to avoid copying the value on each method call. This can be more efficient if the receiver is a large struct`

### 高阶函数

```go
func main() {
	a := func(x int, y int) int { return x + y }
	fmt.Println(add(a,1,2))
}

func add(add func(x int, y int) int, x int, y int) int {
	return add(x, y)
}
```

### 闭包

```go
func getSequence() func() int {
   i:=0
   return func() int {
      i+=1
     return i  
   }
}

func main(){
   /* nextNumber 为一个函数，函数 i 为 0 */
   nextNumber := getSequence()  

   /* 调用 nextNumber 函数，i 变量自增 1 并返回 */
   fmt.Println(nextNumber()) // 1
   fmt.Println(nextNumber()) // 2
   fmt.Println(nextNumber()) // 3

   /* 创建新的函数 nextNumber1，并查看结果 */
   nextNumber1 := getSequence()  
   fmt.Println(nextNumber1()) // 1
   fmt.Println(nextNumber1()) // 2
}
```

***

## 第七章 - 接口

### 特点

* 不需要显示地声明实现了某个接口，只需要实现类含有特定的方法就行

  ```go
  //定义接口
  type HasLife interface {
  	isAlive() bool
  }
  
  //定义类
  type People struct {
  	name string
  	age  int
  }
  
  //手动隐式实现这个接口
  func (p People) isAlive() bool  {
  	return true
  }
  ```

* 类似`java`里面的`toString()`，go语言有一个接口如下

  ```go
  type Stringer interface{
      String() string
  }
  ```

### 底层实现

* 示例语句 -> `var p HasLife = People {}`

* `p`的实际类型是`runtime/runtime2.go#iface`

  ```go
  type iface struct {
  	tab  *itab //存储实现了那些方法
  	data unsafe.Pointer //真正的实现
  }
  
  type itab struct {
  	inter *interfacetype
  	_type *_type
  	hash  uint32 // copy of _type.hash. Used for type switches.
  	_     [4]byte
  	fun   [1]uintptr // variable sized. fun[0]==0 means _type does not implement inter.
  }
  ```

### 实体方法和指针方法

1. 当手动实现`func (p People) isAlive() bool`方法时，编译器会自动生成`func (p *People) isAlive() bool`方法
2. 但当手动实现`func (p *People) isAlive() bool`方法时，编译器不会自动生成`func (p People) isAlive() bool`方法

### 空接口

* 底层类型 -> `runtime/runtime2#eface`

  ```go
  type eface struct {
  	_type *_type //这个字段更是简略
  	data  unsafe.Pointer //这个字段和iface一样
  }
  ```

* 任何类型的值都能转成空接口

  * 原理 -> 将传入的值转换成一个`eface`对象，只不过这一步是在编译的时候做的

* 是否为`nil`

  * 当且仅当`type`为`nil`，且`data`为`nil`时，空接口变量才是`nil`


### 接口的值

* 接口的值就是一个具体的类型和那个类型的值
* 即如`var w io.Writer = os.Stdout`，此时w的type类型值为`*os.File`，它的type的值为`os.Stdout`的指针副本
* `var x interface{} = time.Now()`，不论接口值多大，理论上动态值都能把它容下
* 接口类型不为空，但接口类型的实际值为空调用方法时不会空指针
* `Note that an interface value that holds a nil concrete value is itself non-nil.`

```go
type I interface {
	M()
}

type T struct {
	S string
}

func (t *T) M() {
	if t == nil {
		fmt.Println("<nil>")
		return
	}
	fmt.Println(t.S)
}

func describe(i I) {
	fmt.Printf("(%v, %T)\n", i, i)
}

func main() {
	var i I
	var t *T
	i = t
  //此处不会报空指针异常
  describe(i) // (<nil>, *main.T)
}
```

* 接口类型推断

```go
//i是一个接口。现在在判断i的类型是不是string，如果不是，ok = false，s为string的零值""
s, ok := i.(string)
```

* type swich

```go
func do(i interface{}) {
	switch v := i.(type) {
	case int:
		fmt.Printf("Twice %v is %v\n", v, v*2)
	case string:
		fmt.Printf("%q is %v bytes long\n", v, len(v))
	default:
		fmt.Printf("I don't know about type %T!\n", v)
	}
}
```

### Sort接口

* sort接口代码

  ```go
  package sort
  type Interface interface{
      Len() int
      Less(i,j int) bool
      Swap(i,j int)
  }
  ```

* 如果要自定义排序就实现上面三个方法就行了

### Error接口

* 一般都直接调用`fmt.Errorf(format string, args ...interface{}) error`方法

### 接口断言操作

* 总结概括理解起来就是判断此接口对象的类型
* 如`if _,ok := w.(* os.File); ok{ ... }`

***

## 第八章 - Goroutines和Channels

### goroutines

* 在go语言中，每一个并发执行的单元叫做一个goroutine
* 主函数在main goroutine执行
* 主函数返回时，所有其他的goroutine都会被直接打断
* 除了从主函数退出或者直接终止程序外，没有其他的编程方式能让一个goroutine打断另外一个goroutine的执行

### go关键字

* `f()`调用函数f()并等待返回
* `go f()`创建一个goroutine，调用f()并不等待返回继续执行

### 底层表示

* `runtime/runtime2.go#g`

  ```go
  type g struct {
    stack     stack //栈起始地址-结束地址
    sched     gobuf //有sp和pc，表示目前协程的运行现场
    atomicstatus atomic.Uint32
    goid         uint64
  }
  
  type stack struct {
  	lo uintptr
  	hi uintptr
  }
  ```

### 线程

* 底层结构 - `runtime/runtime2.go#m`

  ```go
  type m struct {
    g0      *g //g0协程，操作调度器
    curg    *g //当前运行的协程
    mOS //每种操作系统特有的信息
  }
  ```

* 协程单线程循环

  1. `runtime/proc.go#schedule()`

     ```go
     func schedule() {
       gp, inheritTime, tryWakeP := findRunnable()
       execute(gp, inheritTime)
     }

  2. `runtime/proc.go#execute()`

     ```go
     func execute(gp *g, inheritTime bool) {
       gogo(&gp.sched)
     }
     ```

  3. `runtime/asm_arm64.s#gogo` //平台相关，用汇编实现

     * 将`goexit()`方法插入栈帧
     * 将`pc`指定到协程的第一行代码
     * 在协程栈上执行协程

  4. 在协程栈中执行业务方法

  5. `runtime/asm_arm64.x#goexit` 

  6. `runtime/proc.go#goexit1`

     ```go
     func goexit1() {
       //传入一个方法指针
     	mcall(goexit0)
     }
     
     //mcall switches from the g to the g0 stack and invokes fn(g)
     func mcall(fn func(*g))
     
     // goexit continuation on g0.
     func goexit0(gp *g) {
       //重新执行步骤1
       schedule()
     }
     ```

### GMP调度模型

* 含义: `G -> 协程`、`M -> 线程`、`P -> `

* 当前问题

  * 多线程调度队列里面的协程时，需要加锁，冲突厉害，影响性能

* 解决思路

  * 每次不从协程队列里面取一个，而是取一堆，这样就能降低锁冲突

* `p`底层结构 -> `runtime/runtime2.go#p`

  ```go
  type p struct {
    m    muintptr
    // Queue of runnable goroutines. Accessed without lock.
  	runqhead uint32
  	runqtail uint32
  	runq     [256]guintptr
    runnext guintptr
  }
  ```

* `schedule()`方法如何找到协程来执行 -> `runtime.proc.go#findRunnable()`内
* 关系结构
  * 一个`m`对应一个`p`
  * 一个`p`对应多个`g`
* 新建协程 -> `runtime.proc.go#newproc()`
  * 随机寻找一个`p`
  * 将新协程放入`p`的`runnext` -> `runtime.proc.go#runqput()`
  * 若这个`p`的队列满了，则放在全局队列
  * 思想是新创建的协程先执行

### 协程并发

* 协程饥饿问题

  * 某个`m`上正在执行的`g`需要很久才能执行结束
  * 而`p`上的某些`g`如网络请求回来需要快速响应，因为正在执行的`g`阻塞而导致无法执行

* 全局饥饿问题

  * `p`上所有的`g`都很耗时，导致全局队列中的`g`得不到执行

* 主动挂起协程

  * 底层位置 -> `runtime/proc.go#gopark()`

    ```go
    func gopark() {
      mcall(park_m) //调用mcall则切换栈
    }
    func park_m(gp *g) {
      schedule() //让g0重新调度
    }
    ```

  * `time.sleep()`底层就是这个原理

* 系统调用完成时

  * 在系统调用完成时会重新调度
  * `runtime/proc.go#exitsyscall()`

* `runtime/stubs.go#morestack()`

  * 编译器会在`g`中的每个函数调用前插入代码执行这个函数

    ```assembly
    //编译器插入代码
    0x00e8 00232 (main.go:25)  CALL    runtime.morestack_noctxt(SB)
    0x00ec 00236 (main.go:25)  PCDATA  $0, $-1
    0x00ec 00236 (main.go:25)  JMP     0
    ```

  * 本意：在执行一个函数前检查协程栈空间是否足够

  * 标记抢占

    * 系统监控到某一个`g`运行超过`10ms`，会将`g`的某一个字段置成某个值

  * 这个函数的实现里面有一个逻辑

    * 判断`g`的某个字段是否为某个特定值
    * 如果是，则保存现场，切换栈，调用`schedule`

* 基于信号的抢占式调度

  * 线程注册信号处理函数`runtime/signal_unix.go/doSigPreempt()`
  * `gc`时发送这个信号
  * 收到这个信号后调用`doSigPreempt()`，然后调用到`schedule`完成调度

### 协程太多

* 结果：`g`太多会耗尽系统资源，最后`panic`，类似于`sof`
* 处理方案
  * 优化业务逻辑 - 不说了
  * 利用`channel`缓冲区 - 推荐
  * 协程池 - 别用
  * 调整系统资源 - 说了等于白说

### channel

* 作用：可以让一个`goroutine`通过`channel`给另一个`goroutine`发送消息

* 操作

  1. 创建：`ch := make(char 发送的类型, size)`
  2. 发送：`ch <- x` 
  3. 接收：`x = <- ch` 
  4. 关闭`close(channel)`

* 缓冲

  1. 不带缓冲 -> `ch := make(char) or ch := make(char, 0)`
  2. 带缓冲 -> `ch := make(char, 1)`

* 不带缓冲

  * 不带缓冲时，发送操作会使`g`进入阻塞挂起状态，直到有接收方接收时`g`才会继续执行

* 底层结构 -> `runtime#hchan`

  ```go
  type hchan struct {
  	qcount   uint           // total data in the queue
  	dataqsiz uint           // size of the circular queue
  	buf      unsafe.Pointer // points to an array of dataqsiz elements
  	elemsize uint16
  	closed   uint32
  	elemtype *_type // element type
  	sendx    uint   // send index
  	recvx    uint   // receive index
  	recvq    waitq  // list of recv waiters
  	sendq    waitq  // list of send waiters
  	lock mutex
  }
  ```

  1. 缓存区
     * 用的是`环形队列`
     * 优势：环形队列不用频繁分配和释放内存，可以降低`GC`的压力达到更高的性能
  2. 发送队列
     * 用的链表实现
  3. 接收队列
     * 同发送队列
  4. `mutex`
     * 访问`chan`之前必须获得锁
     * 即进入队列和操作数据那很小一段时间是锁的，其他时间都不锁
  5. 状态值
     * 标志是否关闭

* 发送数据

  * 语法糖：编译器会将`<-`转换成对应的函数`chansend1()、chanrecv1()`
  * 三种情况
    1. 直接发送 -> 接收队列里面已有等待的`g`，直接取`g`出来，赋值然后唤醒
    2. 有缓存 -> 直接放入环形队列
    3. 阻塞 -> 把自己放入发送队列

* 非阻塞chanel

  * 使用

    ```go
    select {
      case 1 <- c1:
      	//...
      case c2 >- 2:
      	//...
      default:
      	//...
    }
    ```


### 共享内存与通信

1. 通过共享内存来通信

   * 代码示例

     ```go
     func printHello(p *int) {
     	for true {
     		if *p == 1 {
     			fmt.Println("hello")
     			break
     		}
     	}
     }
     
     func main() {
     	i := 0
     	go printHello(&i)
     	time.Sleep(time.Second)
     	i = 1
     	time.Sleep(time.Second)
     }
     ```

   * 上述代码通过共享`p`的指针来达到通信的目的

2. 通过通信来共享内存

   * 代码示例

     ```go
     func printHello(c chan int) {
     	if <-c == 1 {
     		fmt.Println("hello")
     	}
     }
     
     func main() {
     	c := make(chan int)
     	go printHello(c)
     	time.Sleep(time.Second)
     	c <- 1
     	time.Sleep(time.Second)
     }
     ```

   * 优势

     1. 共享内存来通信会频繁读内存，很可能会造成`g`读同一段内存而冲突
     2. 更高级的抽象，提高可读性和可维护性
     3. 更容易解耦，增强拓展性和可维护性

### happen before原则

* 通常说的X时间要在Y事件之前发生，不是说的X事件一定比Y事件先发生，而是指必须等X事件完成后才能进行B事件
* 即开始时间的先后没有关系，我们只看衔接的先后顺序

### goroutine泄露

* 某goroutine因为没人接收或等待读入而永远卡住，泄露的goroutine不会被自动回收，会对系统造成很大的风险

***

## 第九章 - 基于共享变量的并发

### atomic.AddInt32

* 实现位置 - `runtime/intenal/atomic/atomic_arm.s#·Xaddint32`

* 汇编原理

  ```assembly
  TEXT ·Xadd64(SB),NOSPLIT,$-4-20
  	MOVB	runtime·goarm(SB), R11
  	CMP	$7, R11 #核心实现
  	BLT	2(PC)
  	JMP	armXadd64<>(SB)
  	JMP	·goXadd64(SB)
  ```

### semaphore

* 底层结构 - `runtime/sema.go#semaRoot`

  ```go
  type semaRoot struct {
  	lock  mutex
  	treap *sudog        // 每一个sudog都指向了一个协程
  	nwait atomic.Uint32 // Number of waiters. Read w/o the lock.
  }
  
  type sudog struct {
    g *g 
    next *sudog
  	prev *sudog
  	elem unsafe.Pointer
    //……
  }
  ```

* `uint32 > 0`时信号`+1`与`-1`

  ```go
  // -1
  func semacquire(addr *uint32) {
  	semacquire1(addr, false, 0, 0, waitReasonSemacquire)
  }	
  
  func semacquire1(addr *uint32) {
    if cansemacquire(addr) {
  		return
  	}
  }
  
  func cansemacquire(addr *uint32) bool {
  	for {
  		v := atomic.Load(addr) //原子load
  		if v == 0 {
  			return false
  		}
  		if atomic.Cas(addr, v, v-1) { //cas减1
  			return true
  		}
  	}
  }
  
  //+1
  func semrelease1(addr *uint32, handoff bool, skipframes int) {
  	root := semtable.rootFor(addr)
    atomic.Xadd(addr, 1) //直接 +1
  }
  ```

* `uint32 < 0`时

  ```go
  func semacquire1(addr *uint32) {
    // Harder case:
  	//	increment waiter count
  	//	try cansemacquire one more time, return if succeeded
  	//	enqueue itself as a waiter
  	//	sleep
  	//	(waiter descriptor is dequeued by signaler)
    root.queue(addr, s, lifo) //加入到等待队列
    goparkunlock(&root.lock, reason, traceEvGoBlockSync, 4+skipframes) //休眠
  }
  
  func semrelease1(addr *uint32) {
    s, t0 := root.dequeue(addr) //取出来
    readyWithTime(s, 5+skipframes) //里面会调用goready
  }
  ```

* 总结
  1. 大于`0`时就是普通的信号量
  2. 等于`0`时就是普通的等待队列(实际是树)

### sync.Mutex

* 底层结构

  ```go
  type Mutex struct {
  	state int32
  	sema  uint32
  }
  ```

  * `state`
    * `0 - 28` - 排队等待个数
    * `29` - 饥饿标志位
    * `30` - 唤醒标志位
    * `31` - 上锁标志位

* 特点

  * 就是`Java`里面的锁
  * 只不过这个锁是不可重入的，和`Java`稍微有些不一样
  * 特点是无论读和写都会上锁和释放锁

* 普通加锁

  * 过程
    * 尝试`CAS`上锁
    * 尝试自旋几次
    * 放到`semaphore`的队列里面

  * 代码位置 -> `mutex#Lock()`
  * 可能导致问题 - `锁饥饿`
    * 即已经等待很久从等待队列释放出来的协程依旧要和外面刚来在自旋的协程竞争，而且很可能竞争不过

* 饥饿模式

  * 标志位 - `Mutex#state`的第29位
  * 进入条件 - 某个协程等待锁时间超过`1ms`
  * 表现
    * 新来的协程不自旋
    * 被唤醒的协程直接获得锁

  * 退出
    * `semaphore`队列清空


### sync.RWMutex

* 底层结构

  ```go
  type RWMutex struct {
  	w           Mutex
  	writerSem   uint32
  	readerSem   uint32
  	readerCount atomic.Int32
  	readerWait  atomic.Int32 
  }
  ```

* 实现

  * 一个读队列，一个写队列
  * 无论读还是写都需要上锁，只不过上的锁不一样而已
  * 当且仅当没上写锁时才能上读锁，已上读锁可以再上读锁
  * 当且仅当没上读锁与写锁时才能上写锁


### sync.WaitGroup

* 底层结构

  ```go
  type WaitGroup struct {
  	noCopy noCopy
  	state atomic.Uint64 // high 32 bits are counter, low 32 bits are waiter count.
  	sema  uint32 //等待执行结束后被唤醒的g队列
  }
  ```

### 锁拷贝

* 不要去拷贝锁，可能导致死锁问题

### race竞争检测

* 可以用它来检测本来应该上锁但没上锁的情况

### GOMAXPROCS

* go中的一个变量，用于决定会有几个CPU核心数来执行go代码
* 也就是goroutine中m:n中的n的数量
* 一般情况默认值是CPU的核心数
* 即当GOMAXPROCS被设置为1时，整个go程序任意时刻只能有一个goroutine在运行，其他goroutine都在啊休眠。只有当GOMAXPROCS数量大于1时，才能实现真正地并行

### goroutine没有ID号

* 其他语言中的线程一般都有一个唯一的线程ID号
* 拥有这个ID号后，做ThreadLocal就十分方便
* 但goroutine并没有ID号，这体现了go语言的简洁性

***

## 第十章 - 包和工具

### go语言编译快速的原因

* 所有导入的包必须显示声明，这样编译器就不用分析整个源文件来判断依赖关系
* 禁止包的循环依赖，这样就更简单甚至可以并发编译
* 编译后的包不仅记录包本省的导出信息，目标文件同时还记录包的依赖关系

### 导入包的重命名

* 当导入的两个包的最尾部名字相同时
* 导入时要自定义一个别名来给两个包名做区分，不然就会混淆

### 匿名导入

* 使用`_`

### GOPATH

* src：用于存放源代码文件
* pkg：用于存放下载的包
* bin：用于存放编译后的可执行程序

### go test

* 所有`_test.go`结尾的文件都是测试的一部分，在go build的时候不会被build
* 三种测试函数：
  * 测试函数：以`Test`为前缀的函数
  * benchmark函数：以`Benchmark`为前缀的函数
  * 示例函数：以`Example`为前缀的函数
* 执行过程：扫描所有符合规则的函数，生成一个临时的main包来执行

***

## 第十二章 - 反射

### 应用举例

* `fmt.Sprintf()`函数中的各种格式化输出
* 需要动态获取输入的类型，这时候就使用反射

#### 反射

* 和Java的反射差不多，啥都能干

***

## 第十三章 - 底层编程

### 作者忠告

* 能不用reflect和unsafe包就尽量别用

***

## 高并发应用 - TCP网络编程

### 分层

1. g - 协程
2. GMP调度 - 协程调度
3. goroutine-per-connect code style
4. net 包
5. `Network Poller` - 每个操作系统的实现不一样
6. 多路复用抽象层 - 用于屏蔽不同操作系统的IO多路复用实现
7. IO多路复用(epoll、kqueue、IOCP) - 操作系统对IO多路复用的实现
8. socket - 操作系统对网络连接的抽象

### 预期

1. 在操作系统层使用IO多路复用，在应用程序层使用阻塞式IO
2. 一个`socket`给一个`g`处理，且是阻塞式处理
3. 当阻塞时就挂起`g`不进行调度，不阻塞时通知`g`恢复调度

### 抽象多路复用层

* 实现 -> `runtime#netpoll.go`

  ```go
  // Integrated network poller (platform-independent part).
  ```

* 关键类型

  * `runtime#pollDesc` -> 将`g`与`socket抽象`成一个对象

    ```go
    type pollDesc struct {
      link *pollDesc // 链表结构，表头是pollCache
      fd   uintptr //socket文件描述符
      rg atomic.Uintptr // pdReady, pdWait, G waiting for read or pdNil
    	wg atomic.Uintptr // pdReady, pdWait, G waiting for write or pdNil
    }
    ```

  * `runtime#pollCache` -> 带一个锁的链表头

    ```go
    type pollCache struct {
    	lock  mutex
    	first *pollDesc
    }
    ```

* 关键方法

  * `runtime#netpollinit()` -> 新建多路复用器
  * `runtime#netpollopen()` -> 插入监听事件
  * `runtime#netpoll()` -> 返回就绪事件列表

### net poller

* 循环调用
  * `g0`协程调用`runtime#gcStart()`方法会调用到`runtime#netpoll()`，因此一直会去查询就绪事件列表并返回

### `net package` -> `net poller` -> `os`

* 研究清楚三者之间的关系

***

## 建立Go项目

* 配置环境变量

```bash
export PATH=$JAVA_HOME/bin:/Users/izumisakai/enviroment/go/bin:$PATH
export GO111MODULE=on
```

* 使用Golang新建一个项目，路径为`$home/awesomeProject`
* 在`$home/awesomeProject`下执行`go mod init awesome/project`命令生成`go.mod`文件
* 设置环境变量`GO111MODULE`值为`on`，完全抛弃老旧的`GOPATH`模式包依赖
* 此时路径关系如下

```bash
- awesomeProject
	- src
		- model
			- song.go
		- main.go
	- go.mod
```

* 在`main.go` 可以使用`import "awemesome/project/src/model"`引入自己自定义包
* `main.go`

```go
package main

import (
	"awemesome/project/src/model"
	"fmt"
)

func main() {
	var song = model.Song{10}
	fmt.Print(song)
}
```

* `song.go`

```go
package model

type Song struct {
	Id int
}
```

* `go.mod`

```go
module awemesome/project
go 1.16
```

***

## runtime

### what is?

* it is a critical component of a programming language implementation.
* it provides an interface between the program and the runtime system.

### why use it?

* It abstracts away many low-level system functions, providing a high-level interface that simplifies programming and makes it easier to write portable code that can run on different platforms.

### some examples

* Java Virtual Machine (JVM)
* .NET Framework
* Go Runtime
* Node.js
* V8 Engine

### go runtime

* where the code?
  * `go-sdk/src/runtime`
* key features
  1. Goroutines - Go runtime manages the scheduling and execution of goroutines, allowing them to run concurrently and communicate through channels.
  2. Garbage collection
  3. Stack management - Each goroutine in Go has its own stack. The size of the stack is dynamically adjusted based on the needs of the goroutine, allowing efficient use of memory.
  4. Channels
  5. System calls - it provides a platform-independent interface for making system calls
  6. Profiling - it allows developers to identify performance bottlenecks and other issues
  7. C interoperability - The Go runtime includes support for interoperability with C code
* runtime and application
  * Go runtime is an essential part of any Go application. it's automatically linked into the resulting executable binary by the Go linker. 
  * the size of the Go runtime is relatively small compared to other runtimes, such as the Java Virtual Machine or the .NET Common Language Runtime.
  * the size increase by runtime is generally not significant and is outweighed by the benefits of the Go runtime, such as the ability to run concurrent and scalable programs.
  * When a Go application is compiled, the `entire` Go runtime is included in the resulting executable binary. 
  * Go runtime is designed to be statically linked into the Go application's executable file. Two Go applications cannot share the same runtime library at the same physical addresses in memory. 

***

## compilation process

### generated file

* `.o` - They are created during the compilation of individual Go source files and are linked together to form the final executable.
* `.s` - These are human-readable assembly language files that are generated by the Go compiler from the Go source code. 
* Executable files - it is generated by the Go linker from the object files.

### compile unit

* `.go` file is the smallest compilation unit in Go.
* packages are the more typical and recommended way to organize and compile Go code.

### static or dynamic?

* By default, Go uses static linking to build executables.

### go build -n 

* what it for?

  * it prints the commands that it would execute, without actually executing them

* output example

  ```bash
  # internal/goarch
  /opt/homebrew/Cellar/go/1.20.3/libexec/pkg/tool/darwin_arm64/compile -o $WORK/b004/_pkg_.a # omit goarch related source files
  
  # internal/abi
  # import config
  packagefile internal/goarch=$WORK/b004/_pkg_.a
  /opt/homebrew/Cellar/go/1.20.3/libexec/pkg/tool/darwin_arm64/compile -o $WORK/b003/_pkg_.a # omit abi related source files
  
  # internal/cpu
  # internal/goos
  # runtime/internal/atomic
  # runtime/internal/math
  # runtime/internal/sys
  # 通过上面步骤，已将必要的sdk源文件编译成.o文件
  
  # import config 将上面编译好的.o文件引进来
  packagefile internal/abi=$WORK/b003/_pkg_.a
  packagefile internal/bytealg=$WORK/b006/_pkg_.a
  packagefile internal/coverage/rtcov=$WORK/b008/_pkg_.a
  packagefile internal/cpu=$WORK/b007/_pkg_.a
  packagefile internal/goarch=$WORK/b004/_pkg_.a
  packagefile internal/goexperiment=$WORK/b009/_pkg_.a
  packagefile internal/goos=$WORK/b010/_pkg_.a
  packagefile runtime/internal/atomic=$WORK/b011/_pkg_.a
  packagefile runtime/internal/math=$WORK/b012/_pkg_.a
  packagefile runtime/internal/sys=$WORK/b013/_pkg_.a
  
  /opt/homebrew/Cellar/go/1.20.3/libexec/pkg/tool/darwin_arm64/compile -o $WORK/b002/_pkg_.a # omit os related source files
  # 截止这一步，runtime已编译成$WORK/b002/_pkg_.a文件
  
  # import config
  packagefile runtime=$WORK/b002/_pkg_.a # 只import一个runtime
  /opt/homebrew/Cellar/go/1.20.3/libexec/pkg/tool/darwin_arm64/compile -o $WORK/b001/_pkg_.a ./main.go # 将main.go编译成$WORK/b001/_pkg_.a
  
  packagefile HelloWorld=$WORK/b001/_pkg_.a
  packagefile runtime=$WORK/b002/_pkg_.a
  packagefile internal/abi=$WORK/b003/_pkg_.a
  packagefile internal/bytealg=$WORK/b006/_pkg_.a
  packagefile internal/coverage/rtcov=$WORK/b008/_pkg_.a
  packagefile internal/cpu=$WORK/b007/_pkg_.a
  packagefile internal/goarch=$WORK/b004/_pkg_.a
  packagefile internal/goexperiment=$WORK/b009/_pkg_.a
  packagefile internal/goos=$WORK/b010/_pkg_.a
  packagefile runtime/internal/atomic=$WORK/b011/_pkg_.a
  packagefile runtime/internal/math=$WORK/b012/_pkg_.a
  packagefile runtime/internal/sys=$WORK/b013/_pkg_.a
  
  /opt/homebrew/Cellar/go/1.20.3/libexec/pkg/tool/darwin_arm64/link -o $WORK/b001/exe/a.out # 将所有.o链接起来，输出$WORK/b001/exe/a.out
  mv $WORK/b001/exe/a.out HelloWorld # 重命名
  ```

### compilation process

1. The Go compiler scans each source file and generates corresponding object files containing compiled machine code. The object files will have the ".o" extension.
2. If there are any cgo files in the package, the C compiler is invoked to compile the C code and generate object files.
3. After all object files are generated, the linker is invoked to link all object files into a single executable file.

### assemble 

* how to see?

  * `go build -gcflags=-S main.go > main.s 2>&1`

* source code

  ```go
  package main // line 1
  
  import "fmt" // line 3
  
  func main() { // line 5
  	fmt.Printf("hello world\n") // line 6
  }
  ```

* assemble

  ```assembly
  main.main STEXT size=96 args=0x0 locals=0x48 funcid=0x0 align=0x0
  	0x0000 00000 (main.go:5)   TEXT    main.main(SB), ABIInternal, $80-0
    0x0000 00000 (main.go:5)   MOVD    16(g), R16
    0x0004 00004 (main.go:5)   PCDATA  $0, $-2
    0x0004 00004 (main.go:5)   CMP     R16, RSP
    0x0008 00008 (main.go:5)   BLS     84
    0x000c 00012 (main.go:5)   PCDATA  $0, $-1
    0x000c 00012 (main.go:5)   MOVD.W  R30, -80(RSP)
    0x0010 00016 (main.go:5)   MOVD    R29, -8(RSP)
    0x0014 00020 (main.go:5)   SUB     $8, RSP, R29
    0x0018 00024 (main.go:5)   FUNCDATA        $0, gclocals·g2BeySu+wFnoycgXfElmcg==(SB)
    0x0018 00024 (main.go:5)   FUNCDATA        $1, gclocals·g2BeySu+wFnoycgXfElmcg==(SB)
    0x0018 00024 (<unknown line number>)    NOP
    0x0018 00024 (main.go:6)   MOVD    $os.Stdout(SB), R7
    0x0020 00032 (print.go:233)      MOVD    (R7)
    0x0024 00036 (print.go:233)      MOVD    $go:itab.*os.File,io.Writer(SB), R0
    0x002c 00044 (print.go:233)      MOVD    $go:string."hello world\n"(SB), R2
    0x0034 00052 (print.go:233)      MOVD    $12, R3
    0x0038 00056 (print.go:233)      MOVD    ZR, R4
    0x003c 00060 (print.go:233)      MOVD    ZR, R5
    0x0040 00064 (print.go:233)      MOVD    R5, R6
    0x0044 00068 (print.go:233)      PCDATA  $1, $0
    0x0044 00068 (print.go:233)      CALL    fmt.Fprintf(SB)
    0x0048 00072 (main.go:7)   LDP     -8(RSP), (R29, R30)
    0x004c 00076 (main.go:7)   ADD     $80, RSP
    0x0050 00080 (main.go:7)   RET     (R30)
    0x0054 00084 (main.go:7)   NOP
    0x0054 00084 (main.go:5)   PCDATA  $1, $-1
    0x0054 00084 (main.go:5)   PCDATA  $0, $-2
    0x0054 00084 (main.go:5)   MOVD    R30, R3
    0x0058 00088 (main.go:5)   CALL    runtime.morestack_noctxt(SB)
    0x005c 00092 (main.go:5)   PCDATA  $0, $-1
    0x005c 00092 (main.go:5)   JMP     0
  ```

***

## 程序如何运行

### 执行汇编

* 代码位置 -> `runtime/rto0_darwin_arm64.s`，`asm_arm64.s`

* 将argc和argv压栈

  ```assembly
  TEXT runtime·rt0_go(SB),NOSPLIT|TOPFRAME,$0
  	// SP = stack; R0 = argc; R1 = argv
  
  	SUB	$32, RSP
  	MOVW	R0, 8(RSP) // argc
  	MOVD	R1, 16(RSP) // argv
  ```

* 初始化`g0`协程

  ```assembly
  // create istack out of the given (operating system) stack.
  // _cgo_init may update stackguard.
  MOVD	$runtime·g0(SB), g
  MOVD	RSP, R7
  MOVD	$(-64*1024)(R7), R0
  MOVD	R0, g_stackguard0(g)
  MOVD	R0, g_stackguard1(g)
  MOVD	R0, (g_stack+stack_lo)(g)
  MOVD	R7, (g_stack+stack_hi)(g)
  ```


### 汇编代码的一系列操作

* 运行时检测 -> `BL	runtime·check(SB)`
* 拷贝argc、argv到go语言里面去 -> BL  runtime·args
* 调度器初始化 -> `BL  runtime·schedinit(SB)`
* 创建主协程(第二个协程)(待调度) 来执行`runtime.main()`-> `MOVD  $runtime·mainPC(SB), R0; BL  runtime·newproc(SB)`
* 将主协程放入调度器等待调度
* 执行`rumtime.main()` -> `runtime/proc.go`

### runtime.main()的系列操作

* 执行一些初始化 -> `doInit(&runtime_inittask)`

* 开启垃圾回收 -> `gcenable()`

* 找到用户`main()`函数开始执行

  ```go
  //runtime/proc.go
  
  //go:linkname main_main main.main
  func main_main() //链接的时候main_main会被链接到用户的main方法
  
  func main() {
    fn := main_main 
  	fn()
  }
  ```

***

## 面向对象

### 官方解释

* [yes or no](https://go.dev/doc/faq#Is_Go_an_object-oriented_language)

### 继承

* `go`不支持继承，只支持组合。所谓的继承只是编译器的语法糖

  ```go
  type People struct {
  	name string
  	age  int
  }
  
  type Woman struct {
  	People   // 这是一个匿名字段
  	isBeauty bool
  }
  
  func main() {
  	woman := Woman{}
  	woman.name = "woman.name" //这是一个语法糖，实际执行的是下面的语句
  	woman.People.name = "woman.name"
  
  	fmt.Printf("hello world\n")
  }
  ```

### 接口

* `go`的接口是隐式实现的，而不是显式实现的

  ```go
  //定义接口
  type HasLife interface {
  	isAlive() bool
  }
  
  //定义类
  type People struct {
  	name string
  	age  int
  }
  
  //手动隐式实现这个接口
  func (p People) isAlive() bool  {
  	return true
  }
  ```

### 总结

* 通过匿名字段，用组合的方式来达到继承的效果
* 通过隐式的方法实现了传统语言的`interface`
* 通过以上手段去除了面向对象复杂而冗余的部分，保留了基本的面向对象特性

***

## 包管理

### java的包管理

* `maven`和`gradle`都是很好的包管理方式
* `java`引入的类型可以不单一
  * 源码 - 很好理解，依赖源码肯定是没有问题的
  * 字节码文件 - 即源代码编译的`.class`字节码文件，因为字节码文件是跨平台的，因此一般依赖的`jar`包内就是字节码文件

### 必须依赖源码

* 由于`go`没有`字节码`这种中间码，因此它的包依赖必须是源代码

### go mod

* 将go包与git项目关联起来
* 版本号就是git项目的tag

### 参考指令

1. `go help mod`
2. `go help get`

***

