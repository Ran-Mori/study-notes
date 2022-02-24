# Cpp Primer 笔记

***

## 第一章

* 1.1 编写一个简单的C++程序

  * 内置(built-in)类型：由语言自身定义的类型，如`int`
  * main函数的返回值一般被用来表示状态，0表示成功，非零返回值的含义由系统定义，通常用来指出错误类型
  * Unix查看命令执行状态：`echo $?`

* 1.2 初识输入输出

  * 4个IO对象

    * cin
    * cout
    * cerr
    * clog

  * 关联性：系统常常将程序运行的窗口与上面这些IO对象关联起来。也就是可以不关联程序运行窗口而关系其他东西
  * 输入输出运算符
    * **<<** 和 **>>** 都是运算符；其中 **<<** 的左侧必须是一个 **ostream** 对象, **>>** 的左侧必须是一个 **istream** 对象
    * 其中 **<<** 的运算结果是 **ostream** , **>>** 的运算结果是 **istream**
    * `std::cout << "Hello World" << std::endl;` == `(std::cout << "Hello World") << std::endl;`

  * endl，内存缓冲区，物理屏幕
    * **endl**是一个被称为 **操纵符** 的特殊值
    * 未写入 **enl** 时内从全在缓冲区，写入 **endl** 时才刷新到屏幕物理设备
    * 调试时debug日志应该实时刷新到屏幕，不然可能在缓冲区不显示出来影响问题定位

  * 作用域运算符：`::`

* 1.4 控制流

  * while

    ```c++
    int a = 0;
    while (a <= 10) {
      a++;
    }
    ```

  * for

    ```c++
    for (int i = 0;i <= 10;i++) {
    
    }
    ```

  * if

    ```c++
    if (i > 0) {
    
    }
    ```

  * 流作为判断条件

    * `int value = 0; while (std::cin >> value)`
    * 当输入流有效时为true，当遇到文件结束符或者非整数时会返回false

  * 测试cin 和 cout

    ```c++
    int value = 0;
    std::cin >> value;
    std::cout << value << " ";
    // when the input is "1 2 3 4 5", the output is "1 "
    
    ```

    ```c++
    int value = 0;
    while (std::cin >> value) {
    	std::cout << value << " ";  
    }
    // when the input is "1 2 3 4 5", the output is "1 2 3 4 5 "
    ```

    * 即`cin`会自动去匹配输入

* 1.5 类简介

  * 标准库头文件一般不带文件后缀，自定义头文件后缀一般是`.h`

  * `#include`标准库头文件时使用`<>`，非标准库头文件使用`""`

  * 左结合的文件重定向

    * `main.out < input.txt > output.txt`，从`input.txt`读入数据，程序运行输出结果到`output.txt`
    * `main.cpp`

    ```c++
    #include<iostream>
    
    int main()
    {
        int a,b;
        std::cin >> a >> b;
        std::cout << "a + b = " << a + b << std::endl;
    }
    ```

    * `input.txt`

    ```c++
    2
    7
    ```

    * bash

    ```bash
    ./main.out < input.txt
    ```

    * result

    ```bash
    a + b = 9
    ```

* 术语

  * **头文件**：使类或其他名字的定义可以被多个程序使用的一种机制。程序通过#incude指令使用头文件
  * **库类型**：标准库定义的类型，如 **istream**
  * **操作符对象**：如 std::endl, 在读写流的时候用来 "操纵" 流本身
  * **标准库**：一个类型和函数的集合，每个C++编译器都必须支持
  * **类类型**：为了与C++内置类型区分开来，非内置类型就是类类型，类名即为类型名

***

## 第二章

* 前序

  * 编程语言补充其基本特征的两种方式
    * 赋予程序员自定义数据类型的权利
    * 将一些有用的功能封装成库函数提供给程序员

  * 简单与复杂
    * C++本身是一门简单的语言。它提供了一组内置基本类型，相应的运算符，几种程序控制流语句
    * C++本身也复杂，程序员可以自定义各种复杂的数据结构

* 2.1基本内置类型

  * 类型位数：C++设计准则之一是尽可能贴近硬件。C++的算术类型必须尽可能满足各种硬件特质。因此C++内置算术类型在不同的硬件上有不同的解释。具体多少位往往也是不能确定，不像Java一样可以确定
  * 算术类型
    * bool类型:**bool**
    * char类型：**char**, **wchar_t**, **char16_t**, **char32_t**
    * 整数类型：**short**, **int**, **long**, **long long**
    * 浮点类型: **float**, **double**, **long double**
    * 空类型: **void**

  * char
    * 一个`char`的空间应确保可以存放机器基本字符集中任意字符对应的数字值。因此一个char的大小和机器字节大小一样
    * 其他字符类型用来支持拓展字符集。如`char16_t`和`char32_t`为`Unicode`字符集服务

  * 是否带符号
    * **int, long, short** 默认带符号
    * **char** 是否带类型由机器硬件决定。当需要用`char`来表示一个小整数时，要明确是`signed char`还是`unsigned char`

  * 查看位数代码

    ```c++
    #include<iostream>
    int main(){
            int value = 1;
            int length = sizeof(value);
            int realShiftLength = 0;
            for (int i = length - 1;i >= 0;i--) {
                    for (int j = 7; j >= 0;j--) {
                            realShiftLength = (i << 3) + j;
                            if(((value >> realShiftLength) & 0x01) == 1){
                                    std::cout << "1 ";
                            } else {
                                    std::cout << "0 ";
                            }
                    }
                    std::cout << "\n";
            }
            return 0;
    }
    ```

  * x86-64硬件平台实际测试位长结果

    | 类型        | X86-64平台长度 |
    | ----------- | -------------- |
    | bool        | 8              |
    | char        | 8              |
    | wchar_t     | 32             |
    | char16_t    | 16             |
    | char32_t    | 16             |
    | short       | 16             |
    | int         | 32             |
    | long        | 64             |
    | long long   | 64             |
    | float       | 32             |
    | double      | 64             |
    | long double | 128            |

  * 移植性
    * 如果默认 **int** 是固定位数的话，就可能换到其他硬件无法兼容运行
  * 类型转换
    * 类型转换本质上也是一种运算
    * 编译器会自动进行一些隐式类型转换

  * 数字的字面值(literal)
    * 定义：`24`, `024`,`0x24` `3.14159`这样的值被称为字面值
    * 不能随便在前面加零，如`011`实际是八进制的数值9，而`11`才是真正的11
    * 十进制字面值的类型是`int, long, long long`中能容纳最小的那一个
    * 八进制，十六进制的字面值类型是`int, unsigned int, long, unsigned long, long long, unsigned long long`中能够容纳最小的那一个
    * 字面值与之关联最大的类型都放不下就会产生错误
    * `short`没有对应的字面值
    * 小数的字面值类型是`double`
    * 可以改变字面值的类型，如`32L, 3.14F`

  * 字符与字符串字面值
    * 字符字面值：由`''`扩起来的
    * 字符串字面值：由`""`扩起来的
    * 字符串字面值是字符数组加一个`\0`，也就是c风格字符串
    * `sizeof('a') = 1`
    * `sizeof("a") = 2`

  * 其他字面值
    * `true, false` 的字面值类型是`bool`
    * `nullptr`的字面值类型是`指针`

* 2.2 变量

  * string
    * 是`std`命名空间下的一个类
    * 是一种类类型
    * 是一种可变长字符序列的数据类型
    * 和C风格字符串有剪不断理还乱的关系

  * 初始化与赋值
    * C++中，初始化不等于赋值。虽然这两者的区别微乎其微
    * 初始化的含义是创建变量是赋予一个初始值
    * 赋值的含义是将原来的值擦除，用一个新值代替

  * 列表初始化
    * C++ 11标准的新特性
    * 使用花括号来进行初始化：`unsigned int value{100};`
    * 当列表初始化作用于内置类型且列表初始化的值可能精度损失时编译器会报错

  * 默认初始化
    * 如果定义的变量没有指定初始值，则变量将被默认初始化
    * 内置类型如果在函数外未初始化，则默认初始化未0
    * 内置类型如果在函数体内未初始化，则 **不被初始化**，它的值是未定义的，对它访问可能出错。因此建议尽量初始化每个内置类型的值
    * 类类型对象如果没有初始化，则其值有类定义决定
    * 使用未初始化的变量且出了问题很难发现和进行调试

  * 声明与定义
    * 声明：规定变量的名字和类型
    * 定义：创建于名字相关联的实体，申请了内存空间还可能赋了一个初始值
    * 声明作用：让C++支持 **分离式编译** ，将声明与定义区分开来
    * 声明可以多处声明，定义只能一次定义
    * 使用`extern int i;`可以声明一个变量而不是定义它
    * 声明语句不能给声明的变量赋初始值，否则将抵消`extern`的作用而将声明变成定义
    * 在函数内部试图初始化一个`extern`的变量会引发错误

* 2.3复合类型

  * 复合类型：基于其他类型而定义的类型
  * 引用
    * 引用是对象的一个别名
    * 引用一旦创建就和它的初始值牢牢绑定在一起
    * 引用不需要经过复制，因为本来就是一个对象
    * 引用必须有初始值，引用无法重新绑定到另一个对象上
    * 引用不是对象，它只是对象的一个别名
    * 引用的类型要和绑定的对象的类型相匹配，除了两种特殊例外情况
    * 引用只能绑定在对象上，而不能绑定在字面值上或者绑定在某一计算结果上

  * 指针
    * 指针本身就是一个对象，允许进行赋值和拷贝
    * 指针不用在定义时赋初始值
    * 在块作用域内定义但未赋值的指针，其值是不确定的，使用可能出错

  * 指针字面值
    * `nullptr`: 是C++的关键字之一。没有指向对象时最好都赋成这个值， `int* p{nullptr};`
    * `NULL`：一个预处理变量，声明在`cstdlib`中，它的值就是0。但尽量别用，还是用`nullptr`
    * 不能把一个int变量赋值给指针，因为类型不匹配。如`int zero = 0; ptr = zero;`是错误写法
    * 但可以直接把字面值`0`赋值给指针，如`ptr = 0;`

  * 指针判断
    * 任何为0指针都是false，任何非0指针都是true。`int* p = nullptr; if(p) { /**/}`
  * 指针比较
    * 两个类型相同的指针可以做`==, !=`运算，指向的地址完全相同就返回true。注意都是空指针比较结果也是`true`
  * `void*`指针
    * 是一种特殊类型的指针，可以指向任何对象。但不能对对象做操作，因为不知道对象的类型
  * 数据类型和声明符
    * `int`是数据类型，`*, &`是声明符
    * 变量的定义包括了一个基本数据类型和一组声明符
    * 正确写法：`int *p;`， 错误写法`int* p；`。 因为`*`仅仅是修饰了`p`而与`int`无关
    * `int* p1,p2`，其中`p1`是指针，而`p2`只是简单的整数
    * **从右往左读**：`int *&p;` `p` 是一个引用，一个指针的引用

  * const
    * 声明：`const int i = 8;`
    * `const`对象必须初始化
    * 可以用非常量值去初始化常量：因为赋值实际上是对象拷贝

  * 多文件中的常量
    * 编译期：编译时会把常量直接做替换，因此编译时必须知道常量的值，即常量定义的值。
    * 文件单独编译：但C++支持文件单独编译，如果一个常量在其他文件需要访问，则必须在那个文件进行声明，但常量声明时必须赋初始值即完成常量定义，这与一个变量只能定义一次的设定冲突。
    * 默认文件内：因此C++默认常量的作用范围是文件内。如果多个文件同名的常量会被看作是不同的常量
    * 手动修改：只需要在声明和定义的地方统统加上`extern`关键字。就能多文件使用常量了。
    * 使用：A文件`extern const double d = 3.14`，B文件`extern const double d;`

  * 初始化和对const的引用
    * 引用的类型必须和其所引用对象的类型一致。但在初始化常量引用的时候有例外，允许使用任意能够转换引用类型的表达式的值
    * `double value = 3.14; const int &i = value;` 合法。因为中途编译器会生成一个`const int temp = value;`，一般称为`临时量`
    * 上面这种情况绑定的引用时临时量，因此这种写法几乎无任何用处

  * 常量绑定

    * 错误绑定

    ```c++
    const int i = 0;
    int &j = i; // 错误，因为可以通过j来改变i的值，而i本身是一个常量不能够被修改值
    ```

    * 正确绑定

    ```c++
    int i = 0;
    const int &j = i; // 正确写法
    ```


  * 对const的引用可能引用一个并非const的对象

    * 常量引用仅仅对引用可进行的操作进行了限制，而对本身所引用的对象则没有做任何的限制

    ```c++
    int i = 10;
    const int &p = i;
    std::cout << p << std::endl; // 输出10
    i = 0;
    std::cout << p << std::endl; // 输出0
    return 0;
    ```


  * 指针和const

    * 指向常量的指针：`const int *p;`，指向的内容(内存地址存的值)是常量不可变，指针内容(内存地址)可以变

    ```c++
    const int i = 10;
    const int j = 20;
    const int *p = &i;
    std::cout << *p << std::endl; // 输出 10
    p = &j;
    std::cout << *p << std::endl; // 输出 20
    ```

    * 常量指针：`int *const p;`,  指向的内容(内存地址存的值)是普通变量可以变，指针内容(内存地址)是常量不能变。常量指针声明时必须赋初始值且不能变

    ```c++
    int i = 10;
    int j = 20;
    int *const p = &i;
    std::cout << *p << std::endl; // 输出 10
    i = 0;
    std::cout << *p << std::endl; // 输出 0
    p = &j; // 编译报错
    ```

    * 指向常量的常量指针：`const int *const p;`，指向的内容(内存地址存的值)是常量不能变，指针内容(内存地址)是常量不能变
    * 怎么看：**从右往左读**
    * 有无常量是有区别的：`const int *p; int *p1;`,其中`p, p1`是不同的类型，不能互相赋值。

    ```c++
    int i = 20;
    const int *p = &i; // 正确，可以把 int* 赋值给 const int*
    const int j = 30;
    p = &j; // 正确， const int* 当然可以赋值给 const int*
    
    int k = 40;
    int *p1 = &k; // 正确， int* 当然可以赋值给 int*
    const int l = 50;
    p1 = &l; // 编译错误， const int* 不能赋值给 int*
    return 0;
    ```


  * 常量表达式

    * 定义：值不会改变，在编译期就能计算出值的表达式。

    * 显然，字面值是常量表达式

    * 举例：`const int i = get_size();`不是常量表达式，虽然它的值只赋值一次不会改变，但值到底是多少要到运行时才知道

    * `constexpr`关键字：一个表达式是不是常量表达式太难分辨了。用这个关键字告诉编译器这是一个常量表达式，如`constexpr const int i = 0;`

    * `constexpr`函数：这个关键字可以修饰函数，前提是函数的返回值能够在编译期算出来。举例如下：

      ```c++
      //声明了一个constexpt函数
      constexpr int get_size() 
      {
              return 100;
      }
      
      int main()
      {       
              constexpr const int i = get_size(); // 声明了一个constexpr变量
              std::cout << i << std::endl;
              return 0;
      }
      ```

    * 普通函数不能作为`constexpr`变量的初始值，但`constexpr`函数可以作为`constexpr`变量的初始值

    * 字面值类型：因为编译期就能计算结果，因此不是所有的类型都能使用`constexpr`，能够使用`constexpr`的类型被称作字面值类型。如`算术类型， 引用， 指针`

    * 一般来说，只要认为想把某个表达式定义为常量表达式，就应该加上`constexpr`

    * `constexpr`与指针：指针必须是`nullptr, 0, 某个固定对象的固定地址`。比如定义在函数体之外的对象的地址是不变的

* 2.4 顶底const

  * 顶层const

    * 表示这个变量本身是否是个常量
    * 顶层const变量必须初始化

    ```c++
    const int i = 0;//这是一个顶层const
    int * const p = &j;//这是一个顶层const
    ```


  * 底层const

    * 表示这个变量(一般是指针和引用)指向的内容是否是一个常量
    * 用于声明引用的const都是底层const，因为引用本来就不能更换绑定对象
    * 底层const不用立即初始化

    ```c++
    const int *p = &j;//这是一个底层const
    const int &i = k;
    ```


  * 拷贝赋值
    * 顶层const：因为拷贝不会改变变量的值，因此等号左边和等号右边的顶层const可以完全忽略相互赋值
    * 底层const：详见`绑定.错误绑定代码`，不允许把有底层const赋值变量赋值给无底层const变量，允许把无底层const变量赋值给有底层const变量

* 2.5 处理类型

  * 类型别名

    * `typedef`
      * `typedef double d`，`d`是`double`的同义词
      * `typedef char *c`, `c`是 `char*`的同义词
    * 意义理解不能错

    ```c++
    typedef char *cp;
    const cp ptr; // ptr是一个常量指针，指向 char。即 const (char *) ptr;
    ```


  * auto
    * 因为类型是由编译器去分析初始值得到的，因此`auto`定义的变量必须有初始值
    * 即加上`auto`不能说明C++是动态语言，因为其类型推断是在编译期完成的
    * 顶层const常被忽略，底层const则被保留。因此要手动加顶层const。`const auto i = j;`

  * decltype
    * 作用：选择并返回操作数的数据类型
    * 返回类型包含底层const和顶层const
    * `decltype(s.size()) count = 0;`。此时`count`的类型是`std::size_type`
    * 注意并不会真正调用执行`s.size()`这一段代码
    * 这玩意有点复杂，建议不要用

* 2.6 自定义数据结构

  * 定义struct

    ```c++
    struct Book_info {
      std::string name = "";
      int publish_year{0};
    };
    ```

  * 类内初始值：创建对象时有类内初始值的成员就用类内初始值这个值，无的就执行默认初始化

  * 声明定义对象：`Sales_data data1, data2;`，执行完后`data1, data2`已被初始化并分配内存。千万不要`Sales_data data1();`，这是一种错误写法

  * 头文件

    * 头文件一般包含那些只能被定义一次的实体，如类，const和constexpr变量
    * 头文件一旦改变，相关的源文件必须重新编译以获得更新后的声明

  > ***

  * 预处理器

    * 预处理能够保证同一个头文件被多次`#include`后仍然能够正常工作
    * 是程序在编译前执行的一段程序

    ```c++
    #ifndef SALES_DATA_H  //一旦未真，则一直执行到#endif为止。一旦为假，立即跳过
    #define SALES_DATA_H
    #include<string>
    struct Sales_data {
    	//...  
    };
    #endif
    ```

    * 预处理变量如`SALES_DATA_H`无视C++语言中关于作用域的规则

  ### 小结

  * C++的基础内置类型是与实现它的硬件密切相关的

***

## 第三章

* 前言

  * 内置数组是一种更基础的类型，string和vector都是对它的某种抽象
  * 内置类型都体现了计算机硬件本身具备的能力
  * 标准库这些更高级别的类型，它们尚未集成到计算机硬件中
  * 内置数组的实现也与机器硬件密切相关，也正因如此它不太灵活

* 3.1 using

  * `using namespace std;`或者`using std::cout`
  * 头文件不应该包含using声明，因为头文件会被复制拷贝到include它的文件中去

* 3.2 string

  * 使用string

    ```c++
    #include<string>
    using std::string;
    ```

  * 拷贝初始化与直接初始化

    * 拷贝：`std::string a = "Hello";`
    * 直接：`std::string a("Hello");`

  * string操作

    * `getline`: `while(getline(std::cin, a)){ std::count << a << std::endl;}`
    * 注意，使用`getline()`函数返回的一行字符串末尾不包括换行符，那个换行符会被丢掉
    * 从流中读入一个`string`，会自动忽略开头的空白(空格符、换行符、制表符等)

  * std::size_type
    * 是`a.size()`的返回值类型
    * 是`string`库的配套类型。配套类型一般体现了标准库类型与机器无关的特性
    * 实际上是`long unsinged int`
    * 因此`size`不要和`int`混着用， 不然有风险

  * 字面值和string相加
    * 标准库允许把字符字面值和字符串字面值转化为string对象
    * 但是必须保证加号两边的操作数必须有一个是`string`类型，另一个不是`string`类型的字面值会隐式转换成`string`类型
    * 为了兼容C，C++中的字符字面值并不是标准库类型string的对象

  * 使用C++版本的C标准头文件
    * C++标准库除了定义C++语言自身的特有功能外，还兼容C语言的标准库
    * C语言中`name.h`的头文件，在C++中是`cname`。因此`cctype`和`ctype.h`的内容是一样的
    * C++语言应该使用`cctype`而不是`ctype.h`

  * 处理每个字符
    * `for (const char s : "hello world") { std::cout << s << std::endl; }`
    * 使用引用处理每个字符：`for (char &s : str) { s = '1'; };std::cout << str << std::endl;`。想要改变遍历的值必须设置为引用类型
    * 下标运算符`[]`：接受的输入类型是`std::size_type`，返回值是对应位置字符的引用
    * C++不对下标做检查，一旦出错，自行承担

* 3.3 vector

  * 标准库类型vector
    * vector是一个类模版
    * 使用：`vector<int> ivec = {1, 2, 3};  vector<int> ivec2(20);`
    * `vector`是模版而非类型，`vector<int>`才是类型
    * `vector`不能存放引用，因为引用不是对象。除此之外，绝大部分内置类型，类类型都能存放

  * 初始化
    * `vector`模板控制着如何定义和初始化
    * `=`初始化都是拷贝
    * 值初始化：只提供容器的数量，不提供初始值，则容器内元素执行默认初始化。分为内置类型默认和类类型默认。如果某种类型必须要指定初始值，不能执行默认初始化，则创建`vector`容器时必须指明初始值

  * 括号和花括号
    * `vector<int> v1(10, 1);`有10个元素
    * `vector<int> v1{10, 1};`有两个元素
    * `vector<int> v1(10);`有10个元素，且执行默认初始化

  * 添加元素

    * C++标准规定运行时向`vector`添加元素应该是很高效的
    * 因此我们没有必要一开始指定`vector`的大小，这样做反而还可能使性能降低
    * `vector<int> v; for (std::vector<int>::size_type i = 0; i < 100; i++) { v.push_back(i); }`
    * 追加函数:`push_back()`
    * 范围for循环语句不应该改变其遍历序列的大小
    * 大小类型是：`std::vector<int>::size_type`
    * `vector`和`string`只能通过下标访问已存在的元素，而不能使用下标来添加元素。

    ```c++
    std::vector<int> vec;
    for(std::vector<int>::size_type i = 0;i <= 10; ++i) {
        vec[i] = i; //严重错误，因为vec实际上是一个容量为0的vector
    }
    ```

* 3.4迭代器

  * 前言：C++标准库容器都支持迭代器，但不是所有的容器都支持下标运算符`[]`
  * 使用迭代器
    * 有迭代器的类型同时拥有返回迭代器的成员
    * `begin`：负责返回指向第一个元素的迭代器
    * `end`：负责返回最后元素的下一个元素的迭代器，无实际作用，只是一个标记
    * 如果容器为空，`begin`和`end`返回的迭代器相等，都是尾后迭代器`end`

  * 运算符
    * `*iter`：返回所指元素的引用
    * `iter -> item`：相当于`(*item).item`
    * `++item`：指向下一个元素
    * `item1 == item2`：判断两个迭代器是否相等
    * 迭代器实质：就是**指针**
    * Java程序员for循环喜欢使用`<`，而C++程序员使用`!=`。本质原因是Java使用下标访问而C++使用迭代器访问，而且C++很多标准库容器压根不支持下标访问。
    * `for (std::string::const_iterator iter = str.begin(); iter != str.end(); iter++){ std::cout << *iter << std::endl;}`

  * 迭代器类型
    * 我们没必要知道迭代器的真正类型
    * `std::string::iterator`是`string`的迭代器类型；`std::vector<int>::iterator`是`vector`的迭代器类型
    * 迭代器分为可变和不可变。`std::string::iterator`可变，`std::string::const_iterator`不可变，相当于指向常量的指针

  * 迭代器含义
    * 可能指迭代器这个概念本身
    * 可能指某个容器定义的自身的迭代器类型
    * 也可能指实际的某个迭代器对象

  * begin和end
    * 如果是常量，`begin()`返回的是`const_iterator`类型；如果不是常量，`begin()`返回`iterator`类型
    * `cbeing()`则永远返回`const_iterator`类型

  * 谨记
    * 但凡使用了迭代器的循环，都不要向它里面添加元素。因为极有可能把迭代器整炸

  * 迭代器运算

    * 就和指针运算差不多
    * 迭代器相减：返回一个`difference_type`类型，每个容器都定义了这个类型。距离有正有负，因此这个是带符号的类型
    * 二分搜索

    ```c++
    std::vector<int> vc;
    auto begin = vc.begin(), end = vc.end();
    auto mid = vc.begin() + (end - begin)/2;
    int gold;
    while (mid != end)
    {
      if (gold == *mid)
      {
        break;
      } else if (gold < *mid)
      {
        end = mid;
      } else
      {
        begin = mid;
      }
    
      mid = begin + (end - begin) /2; 
    }
    ```

* 3.5数组

  * 前言
    * 数组的大小固定不变，不能随意向数组中添加元素
    * 如果不清楚数组的大小，请不要使用。转去使用`vector`

  * 定义和初始化内置数组
    * 数组是一种复合类型，数组的大小也属于数组类型的一部分
    * 数组的长度必须在编译时已知
    * 数组的类型必须明确定义，不能`auto`
    * 同`vector`，数组存放的事对象，因此没有引用数组

  * 字符数组特殊性
    * `const char a[6] = "abcdef";` 错误写法，无空间来存放`\0`

  * 数组不允许拷贝和赋值，即使支持也是`编译器拓展`的功能，为了移植性还是不要用
  * 复杂数组声明
    * `int (*p)[10] = &arr`,`p`是一个指针，指向一个含有10个整数的数组
    * `int (&r)[10] = arr`，`r`是一个引用，引用一个含有10个整数的数组
    * `int *(&array)[10] = ptrs`, `array`是一个引用，引用一个数组，这个数组存放着10个指针
    * 顺序：先括号里面，在右边数组符号，在从右到左。因为数组的维度是紧紧跟着被声明的名字的

  * 数组使用
    * 数组除了大小固定外，其他用法和vector类似
    * 数组的下标是`size_t`类型，这是一种机器相关的无符号类型

  * 指针和数组和迭代器
    * 使用数组的时候编译器会将其转换为指针
    * `auto`数组的类型是指针；但`decltype`数组的类型是数组；
    * 指针也是迭代器，但一个容量为10的数组手动执行`array[10]`获取尾迭代器很危险
    * 在`iterator`中定义了两个函数来获取数组的迭代器。`begin(array), end(array)`。注意尾后指针不能执行解引用操作

  * 指针运算
    * 两个指针相减，返回的是一个标准库类型`ptrdiff_t`，是一种机器相关的带符号
    * 类型

  * c风格字符串
    * c和c++这两个语言就离谱。一个最基本的字符串都能搞出那么多名堂
    * 使用C++就不推荐使用C风格的字符串，容易出问题和程序漏洞
    * `strlen(str), strcmp(str, str), strcpy(str, str)`都是C风格字符串的函数，尽量不要用，除非是适配老程序

  * 两个语言字符串的区别
    * c语言根本没有字符串这种类型，它全部使用字符数组，即`char*`类型，然后在末尾加一个`\0`作为结束符。而且c语言必须声明字符串的长度
    * 而C++是一门面向对象的语言。字符串抽象封装成了`string`，它的长度是它的一个成员。因此按照道理来说它的字符串末尾可以不用加`\0`
    * 但C++并未强制规定字段串的末尾是否需要加`\0`，这取决于编译器厂商的实现。一般编译器都加`\0`

  * 与旧代码的接口
    * 允许使用c风格字符串来初始化string对象或为string对象负责
    * 在string对象的加法运算中允许使用c风格字符串作为其中一个运算对象(不能两个都是)
    * `c_str()`：将string对象转换为`const char*`，如`const char* str_ptr = str.c_str();`
    * 现代C++程序员应多使用`vector, iterator, string`，少使用`c风格字符串, 内置数组, 指针`

  * 数组初始化vector
    * 不允许数组赋值，但允许用数组来初始化`vector`，只需要指明首尾指针就行
    * `vector<int> vec(begin(int_array), end(int_array));`

* 3.6 多维数组

  * 初始化

    ```c++
    int array[3][4] = {
      {1, 2, 3, 4},
      {5, 6, 7, 8},
      {9, 10, 11, 12}
    };
    ```

  * 遍历

    ```c++
    for(auto &row : ia) {
       for(auto &col : row) {
         //...
       }
    }
    ```

    * 范围for循环遍历除了最内层外都要将变量修改成引用。不然`auto`数组类型返回的是指针，无法进行下一层遍历


***

## 第四章

* 前言

  * C++本身定义了一套运算符，这些运算符作用于内置类型和复合类型；而类类型又支持用户自定义运算符的含义。因此C++的运算符变得十分灵活
  * C++允许用户自定义类类型运算符的含义，即运算符重载。如io库中`<<, >>`运算符，迭代器中操作的运算符等

* 4.1 基础

  * 左值右值
    * 右值：用的是对象的值(对象的内容)
    * 左值：用的是对象的身份(对象在内存中的地址)
    * 赋值预算符左边需要一个左值，右边需要一个右值。返回结果是一个左值
    * 取地址符`&`运算对象是一个左值，返回值是一个右值

  * 结合律
    * 结合律很重要
    * 要区分左结合还是右集合

  * 求值顺序
    * `int i = f1() * f2();`。在这个表达式中`f1()`和`f2()`的执行顺序是不确定的
    * `cout << i << "hello" << ++i << endl;`这条语句中`<<`运算符未规定如何求值。返回值是什么取决于编译器定义
    * 规定了求值顺序的运算符：`&&、 ||、 ?:、 ,`
    * 因此拿不准顺序的时候可以考虑加括号；或者如果改变了某个对象的值，在表达式其他地方就尽量不要使用到这个对象

* 4.2 算术运算符

  * `bool`转成`int`：`bool b = true; bool b2 = -b;`。首先将`b`从`bool`的`true`转成`int`的`1`，然后结果为`-1`，因为不等于`0`，所以从`int`的`-1`转换成`bool`的`true`

* 4.3 逻辑运算符

  * 进行比较运算时除非比较对象是布尔类型，否则不要使用`true`,`false`进行比较，因为可能会涉及隐式类型转换

* 4.4 赋值运算符

  * 类类型的赋值运算符细节由类来决定，因为可以使用运算符重载
  * 右结合律：`i = j = 0;`，先算右边返回一个左值，再用这个左值赋值给`i`
  * 赋值运算符的优先级比较低，`if((i = get_value()) != 43)`

* 4.5 ++和--

  * 除非必须，建议使用前置版本。因为后置版本编译器会临时存一个值，以便于返回这个未修改的内容，然而很多时候这个步骤都是多余的影响性能
  * `*ptr++`实际后置`++`运算符的顺序比`*`高。实际是先运算`ptr++`，但返回的却是原来的值，然后在对原来的这个值做解引用操作。自己原来的理解是错误的，并非先解引用然后再`++`，而是先`++`但返回原来的值
  * 大多数运算符都没有规定求值的顺序，因此不要让运算式的两边都改变同一个运算对象的值

* 4.8 位运算符

  * 关于位运算符如何处理符号位没有规定，因此强烈建议仅将位运算符作用于无符号类型
  * 低于`int`的执行位运算符，首先提升成`int`
  * 移位运算符是左结合的
  * 因为移位运算符优先级不高，因此最好加上可括号

* 4.9 sizeof

  * 即使指针是无效的，也可以进行`sizeof()`运算
  * `sizeof`不会实际计算运算对象的值
  * 两种使用方式：`sizeof (type);` 和 `sizeof expr;`
  * `sizeof`是右结合的
  * 对引用执行得到引用对象所占空间的大小
  * 对指针执行得到指针所占空间大小，一般是`8`
  * 对解引用指针得到的大小是指针指向对象所占的空间大小
  * 对数组执行是整个数组的大小，数组不会被转换成指针来处理
  * `constexpr size_t sz = sizeof(int_array)/sizeof(*int_array`
  * `)`

* 4.11 类型转换

  * 隐式类类型转换：类类型能够定义由编译器自动执行的转换。如字符串字面值转成`string`对象，`cin >> i`转换成`bool`
  * 显式转换
    * 强制类型转换十分危险，它破坏了原本编译器的类型检查系统，每次使用都询问自己是否真有必要使用
    * c++强转：`cast-name<type>(expresion)`.
    * `static_cast`: 任何具有明确定义的类型转换，只要不包含底层const，都可以用。常用于高精度算术类型向低精度类型转换
    * `const_cast`: 只能改变运算对象的底层const属性。常用于函数重载。只有`const_cast`能改变常量属性，其他方式都是未定义的行为
    * `reinterpret_cast`: 通常为运算对象的位模式提供较低层次的重新解释。最好不要用，很危险
    * `dynamic_cast`: 运行时多态转换
    * 旧风格转换：`type(expression)`,`(type)expression`。不推荐使用，如果这个转换能用`static_cast`和`const_cast`代替，就进行相应代替；否则就执行最危险的`reinterpet_cast`

***

## 第五章

* 5.1 简单语句

  * 单独一个分号也是一句语句，如`;`, 因此我们不能随意添加引号

* 5.4for语句

  * 范围for: 实际上是使用了迭代器`begin, end`。
  * 因为范围for语句预存了`end`的值，因此不能在循环过程中添加元素

  * 5.6 try-catch语句

  * 异常处理机制
    * throw: 抛出异常
    * try: 处理异常
    * 异常类：用于throw和try之间传递信息

  * 抛出异常：`throw std::runtime_error("error exception");`。抛出后终止当前函数，将控制权转移给能处理该异常的代码

  * 每个标准库类型都定义了一个`what()`成员函数，返回一个`const char*`

  * 如果抛到最顶层都没有异常处理，就会执行`std::terminate()`来终止程序

  * `stdexception.h`定义的异常类：`exception, runtime_error, range_error, overflow_error, underflow_error, logic_error, domain_error, invalid_error, length_error, out_of_range`

  * `exception`类定义

    ```c++
    class exception
    {
      public:
      exception() _GLIBCXX_NOTHROW { }
      virtual ~exception() _GLIBCXX_TXN_SAFE_DYN _GLIBCXX_NOTHROW;
    
      /** Returns a C-style character string describing the general cause
         *  of the current error.  */
      virtual const char*
        what() const _GLIBCXX_TXN_SAFE_DYN _GLIBCXX_NOTHROW;
    };
    ```


***

## 第六章

* 6.1函数基础

  * 本质：一个命了名的代码块
  * 调用运算符：用于调用函数，即一对括号`()`
  * 函数调用的两项工作
    * 用实参初始化形参
    * 将控制权由主调函数切换到被调函数

  * 函数return返回两项工作
    * 返回return语句中的值(如果有的话)
    * 将控制权由被调函数切换到主调函数

  * 形参和实参：实参是形参的初始值。编译器未规定实参的求值顺序

  * 返回类型：返回类型不能是数组和函数，但可以是指向数组和函数的指针

  * 局部变量：局部变量会隐藏外层作用域中其他所有同名声明

  * 自动对象：只存在于块执行期间的对象。如形式参数就是自动对象

  * 局部静态对象

    * 程序第一次执行到它时初始化，直到程序结束才销毁
    * 定义：`static int i = 0;`
    * 局部静态变量如果没有初始值就执行默认初始化，内置类型也会执行，默认为0

    ```c++
    std::size_t count_calls()
    {
      	//第一次执行到这里时会初始化为0， 后面就不会每次赋为零而是取已有的值
        static std::size_t i = 0;
        return ++i;
    }
    ```


  * 函数声明
    * 声明: `void print(vector<int>::const_iterator beg, vector<int>::const_iterator end);`
    * 定义必须在声明前，不然找不到声明
    * 可以多次声明但只能一次定义
    * 定义最好放在头文件中

  * 分离式编译

    * main.cpp

    ```c++
    #ifndef MAIN_CPP
    #define MAIN_CPP 1
    #include<iostream>
    #include "countCall.h"
    #endif
    
    int main()
    {
        for (size_t i = 0; i < 10; i++)
        {
            std::cout << count_calls() << std::endl;
        }
        return 0;
    }
    ```

    * countCall.h

    ```c++
    #include<bits/c++config.h>
    
    std::size_t count_calls();
    ```

    * countCall.cpp

    ```c++
    #ifndef COUNT_CALL_CPP
    #define COUNT_CALL_CPP
    #include "countCall.h"
    #include<bits/c++config.h>
    #endif
    
    std::size_t count_calls()
    {
        static int i = 0;
        return ++i;
    }
    ```

    * 编译命令

    ```bash
    g++ main.cpp countCall.cpp -o main.out
    ```

* 6.2 参数传递

  * 参数传递本质：形参初始化原理和变量初始化原理一模一样

  * 值传递：将实参拷贝一份，赋值给形参

  * 引用传递：形参是实参的一个别名

  * 指针形参：实际上是值传递，只不过指向的对象相同

  * 与C的习惯差别：C程序员常常通过指针类型的形参来访问外部的对象，而C++语言建议使用引用来代替指针传递

  * 使用引用避免拷贝

    * 有些对象不能拷贝，如`io`对象。这种时候就只能传递引用
    * 有些对象拷贝十分费时，比如很长的字符串，这是也可以传递引用

    ```c++
    bool isShorter(const string &str1, const string &str2);
    ```

    * 如果函数无须修改引用形参的值，最好将其设置为常量引用

  * 返回多个值：通过引用形参可以让一个函数实际上可以返回多个值

  * const形参与实参

    * 和其他初始化过程一样，用实参初始化形参时会忽略掉形式参数的顶层const。即`int fun(const int);`函数既可以穿入`int i = 0`做参数，也可以传入`const int j = 0`做参数
    * 重复定义错误

    ```c++
    int fun(const int);
    int fun(int);//重复定义
    ```

    * 可以使用非常量初始化底层const，但是反过来就不行
    * 形参尽量使用常量引用
      * 当形参为常量引用时，既可以接受常量实参也可以接受普通实参；而如果形参为普通引用时，则函数不能接受常量实参
      * 不遵循此规则还可能连环出错

  * 数组形参

    * 数组不允许拷贝，但允许传数组指针
    * 等价性
      * 下面三种形式的函数是一模一样的
      * 编译器只会检查穿入的实参类型是否为`int *`，其他的它不管
      * 即数组的大小对数组的调用没有影响

    ```c++
    void print(const int*);
    void print(const int[]);
    void print(const int[10]);
    ```

    * 明确数组长度的方式
      * 类似于C风格字符串，末尾有一个特殊的结束符`\0`
      * 传递首元素指针和尾后指针，类似于迭代器
      * 显示地传入一个形参用于表示数组的大小

    * 数组引用形参
      * `print(int (&arr)[10])`
      * 但此时只能传入大小为10的数组，其实参类型严格限制成`int [10]`，不是10就不行
    * `main`处理命令行选项
      * `int main(int argc, char *argv[])`
      * `int main(int argc, char **argv)`
      * 参数从`argv[1]`开始，因为`argv[0]`一般都是`a.out`

  * 可变形参的函数
    * `initializer_list`
      * 定义在`initializer_list`文件中
      * 定义：`void error_msg(initializer_list<string> args)`
      * 调用：`error_msg({"str1", "str2", "str3"})`
      * `initializer_list`中的元素永远是常量值，我们无法改变其中对象元素的值
    * 省略符形参`...`
      * 一般用于访问C代码，因为这种C代码使用了C标准库的`varargs`功能
      * 大多数类类型对象在传递给省略符形参时都无法正确拷贝，慎用
      * 除上一种情况外不建议使用
      * 省略形参只能存在于最后一个参数

* 6.3 返回类型和return语句

  * 无返回值函数：不用`return`，但编译器会隐式地加上`return`语句
  * 有返回值的函数
    * 编译器并不是能检测到所有case是否都有返回值。如果运行时有未返回的情况会直接崩
    * 值是如何返回的
      * 和初始化一个变量，或者初始化形参一模一样
      * 返回值用于初始化调用处的一个临界量，该临界量就是函数调用的结果
      * 返回非引用类型会进行一次拷贝，而返回引用则不会进行拷贝
    * 不要返回局部引用和局部指针。因为函数调用结束空间都被释放了
    * 调用运算符
      * 调用运算符和`.`与`->`运算符优先级相同，且满足左结合率
      * 因此可以连续调用

  * 左右值
    * 如果函数的返回类型是一个引用，那么函数的返回值是左值，否则就是右值
    * 我们能为返回类型为非常量引用的结果进行赋值操作

  * 列表初始化返回值

    ```c++
    vector<string> process()
    {
        if() {
         return {};
        } else if() {
         return {"!","2"};
        } else {
         return {"1","3","4"};
        }
    }
    ```

  * 预处理变量
    * 由预处理器处理而不是由编译器处理
    * 因此不用加`std`，而是随便直接用
    * 预处理宏其实也就是预处理变量
    * 由于预处理变量必须在程序内保持唯一，因此其他地方绝对不要在定义同名的变量

  * 返回数组指针：
    * 使用尾置返回类型：`auto get_array() -> int (*)[10];` 或者 `auto get_array() -> decltype(a)*`
    * 因为数组的大小也是数组类型的一部分，因此数组指针也要声明数组的大小

* 6.4 函数重载

  * 不允许两个函数除了返回类型外其他所有的要素都相同，即函数的返回类型不会参与重载
  * 重载和const
    * 顶层const不参与重载，底层const参与重载
    * 形参有无顶层`const`不是函数区分的依据
    * `fun(const int &i); fun(int &i)`是可以的，因为此时是底层const
    * 编译器会区分底层const选择最优函数进行调用

  * `const_cast`和重载

    ```c++
    const string &get_shorter(const str &str1, const string &str2)
    {
    	return str1.size() <= str2.size() ? str1 : str2;
    }
    
    string &get_shorter(string &str1, string &str2)
    {
    	const string &r = get_shorter(const_cast<const string&>(str1), const_cast<const str&>		(str2));
    	return const_cast<sting &>(r);
    }
    ```

  * 函数匹配
    * 找一个最优的进行匹配。能不进行类型转换就不进行
    * 如果函数有默认实参，则实际传入的参数可以比实际的数量少
    * 小算术值会一步到位转为`int`，而不会中间转为`short`慢慢升级
    * 如果没有最优，而是旗鼓相当。则发生错误，称为`二义性错误`

  * 局部作用域与重载：局部作用域内的函数不能和外面的函数重载。`C++的名字查找发生在类型检查之前`
  * 默认实参
    * 使用：`int fun(int j = 10);`
    * 不允许多次声明修改同一个参数的实参
    * 函数声明都应该放在头文件中，包括带默认形参的函数
    * 默认实参要么在最后一个，否则它后面的所有形参都有默认值
    * 默认实参不能是局部变量，可以是表达式或者函数，前提是能够在这个函数声明的作用域内进行解析

  * 内联函数
    * `inline const string &get_shorter(const str &str1, const string &str2);`
    * `inline`只是给编译器发出一个请求，而编译器可以忽略这个请求

  * constexpr函数
    * 编译器能求出值进行替换
    * 函数的返回值和形参都得是字面值类型，函数体中只得有一条`return`语句
    * `constexpr`函数被隐式地指定成了内联函数

  * 调试局部变量
    * `__func__`:编译器定义的局部静态变量，用于存放函数的名字
    * `__FILE__`:存放文件名
    * `__LINE__`:存放行号
    * ……

* 6.7 函数指针

  * 函数类型(注意与函数签名区别)决定因素
    * 返回类型
    * 形参数量
    * 形参类型
    * 与函数名无关,函数名在这里和参数名的性质是一样的

  * 实际例子
    * 函数声明：`const string &get_shorter(const str &str1, const string &str2);`
    * 函数类型：`const string& (const str&, const string&);`
    * 定义指针: `const string& (*fun_ptr)(const str&, const string&);`

  * 使用函数指针
    * 函数名可以直接当作指针使用，不用使用取地址符`&`
    * 函数指针可以直接调用，不用使用解地址符`*`

  > ***

  * 重载函数的指针

    ```c++
    void ff(int*);
    void ff(unsigned int);
    
    void (*fun_ptr)(unsigned int) = ff;
    ```

    * 必须指明指针指向的是重载函数中的那一个

  * 函数指针作形参
    * `void func(const string &ptr(const str&, const string&))`
    * `void func(const string& (*ptr)(const str&, const string&))`
    * 这两种是一样的，函数名本身就被当作指针形式

  * 函数指针作为返回值

    * 函数与函数指针

    ```c++
    typedef F int (int*, int); //F的类型是函数
    typedef FF int (*)(int*, int); //FF的类型是函数指针
    ```

    * `auto func() -> const string& (*)(const str&, const string&)`
    * 此时必须显示声明返回的是指向函数的指针而不是函数
    * 因为不能返回函数，只能返回函数的指针

***

## 第七章

* 7.1 定义抽象数据类型

  * 定义成员函数
    * 类的成员函数的定义必须在 **类内**
    * 类的成员函数的声明既可以在 **类内** 也可以在 **类外**

  * this
    * 成员函数通过`this`隐式参数来访问调用它的那个对象
    * 编译器会将对象的地址隐式地传递给`隐式形参this`
    * `this`是一个常量指针，我们不能修改this中保存的地址

  * const成员函数
    * 定义：`std::string isbn() const { return bookNo; }`
    * 默认情况下成员函数中的`this`指针的类型是`class_name *const`，即这个指针只有`顶层const`而无`底层const`。
    * 尽管`this`是隐式的，它也需要遵循初始化原则。则一个常量对象`const class_name`无法初始化赋值给`class_name *const`
    * 成员函数的参数列表后加上`const`后，`this`指针的类型则变成了`const class_name *const`，即有`顶层const`也有`底层const`，这样常量对象就行赋值了，即常量对象可以调用常量方法
    * 即普通函数只有非常量对象能调用，而常量函数既可以让非常量对象调用，也可以让常量对象调用
    * 如果可以，函数尽量声明为常量函数

  * 类作用域和成员函数
    * 编译时先编译成员，后编译成员函数
    * 因此成员可以定义在类之后

  * 返回`this`的函数
    * `class_name& class_name::combine(const class_name &rhs) { return *this; }`
  * 类的非成员函数
    * 这种函数应该放在类的头文件中，这样无论是使用类还是类的非成员函数都只用引入一个头文件
    * 定义：`istream &read(istream &in, class_name &item);`
    * 因为流不能复制，因此定义为引用；因为要改变其中的值，因此不设置成const；因为把原则圈交给用户，因此不换行

  * 构造函数

    * 作用：初始化对象成员
    * 不能声明成const,因为一个对象只有在执行完构造函数后，它才真正获得了`const`属性。
    * 默认构造函数：即 `default constructor`。如果一个类没有显示地定义一个构造函数，则编译器会隐式地创建一个默认构造函数
    * 默认构造函数执行原则：如果有类内初始值，则用类内初始值；否则执行默认初始化
    * 某些类不能依赖默认构造函数
      * 一旦要控制对象的初始化，则所有情况都要控制。因为一旦定义了构造函数，除非自己再定一个一个默认构造函数，否则编译器不会自动生成
      * 默认构造函数可能执行错误操作。如内置类型或复合类型默认初始值是未定义的
      * 有时编译器不能生成默认构造函数，如类中某个成员无默认构造函数
    * 定义

    ```c++
    class_name() = default;
    class_name(const std::string num) bookNum(num) {};
    ```

    * `= default`:作用是显示让编译器生成默认构造函数
    * 构造函数初始化列表：如上面的`bookNum(num)`

  * 拷贝、赋值、析构
    * 当使用`=`会发生赋值操作
    * 当销毁会执行析构操作
    * 如果不主动定义，则编译器默认生成。但生成的可能有一定问题

* 7.2 访问控制与封装

  * class和struct
    * class如果不加声明默认的访问级别是`private`，而struct默认是`public`
    * 上面一条是他们的唯一区别，除此之外没有区别了

  * 友元

    * 作用：允许其他类或者其他函数访问类的非公有成员
    * 举例：`read()`和`write()`不是一个类的成员函数，但他们的实现依赖访问类的私有成员。在这种情况下就可以将其定义成友元
    * 定义

    ```c++
    class class_name {
      friend std::istream &read(std::isteam &is, class_name &cn);
    }
    ```

    * 友元声明只能定义在类的内部，具体定义在那里没有要求。一般在类定义开始或者结束的地方进行集中声明
    * 友元不是类的成员，类的访问修饰符对它没用
    * 友元的类内声明只是仅仅指定了访问权限，它并不是真正的函数声明。我们还要在类的外面重新生声明一次，尽管有的编译器会自动生成但并不是所有。就很离谱

* 7.3 类的其他特性

  * 定义类型成员

    * 定义

    ```c++
    class class_name {
    public:
    	typedef std::string::size_type pos;
    }
    ```

    * 类型成员是一个成员，受访问修饰符限制
    * 类型成员和普通成员不太一样，必须先定义后使用。因此通常定义在类的最前面

  * 可变数据成员
    * 作用：即使在`cosnt`函数内也能改变它的值，即它永远都不是`const`
    * 声明：`mutable std::string::size_t sz;`

  * 返回`*this`的函数

    * 如果一个函数的返回值是一个左值，则很可能会像构造者模式一样连续调用。但一旦调用对象是一个常量，则问题很大
    * 基于`const`的重载

    ```c++
    class Screen {
    public:
      	Screen &display(std::ostream &os)
        {
          do_display(os);
          return *this;
        }
      	const Screen &display(std::ostream &os) const 
        {
          do_display(os);
          return *this;
        }
    private:
    	void do_display(std::ostream &os) const
      {
        os << contents;
      }
    }
    ```

    * C++真的是离谱
    * 设计良好的C++代码中包含很多类似于`do_display()`的小函数

  * 类的声明
    * 类可以和函数或变量一样先声明后定义
    * 声明之前定义之后的类类型是一个`不完全类型`
    * **不完全类型**的使用场景十分有限
    * 十分复杂不想继续了解了

  * 类之间友元关系

    * 类友元定义

    ```c++
    class Screen {
      friend class Window_mgr;
    }
    ```

    * 类友元作用：`Window_mgr`的所有成员函数都是`Screen`类的友元函数
    * 友元关系不具有传递性，要想友元必须先声明。即每个类控制自己的友元函数
    * 类成员函数友元定义

    ```c++
    class Screen {
      friend void Window_mgr::clear(ScreenIndex);
    }
    ```

    * 顺序说明
      * 先定义`Window_mgr`类
      * 然后声明但不定义`clear`方法
      * 定义`Screen`类，并指明友元访问关系
      * 最后定义`clear`函数

  * 重载和友元：重载的函数也是不同的函数，如果想友元则所有函数都要声明成友元

  * 友元的声明和作用域：类中仅仅是指明了访问关系，真正的函数声明还要重新进行声明

* 7.4 类的作用域

  * 作用域和定义在类外部的成员
    * 编译器在处理参数前已经知道函数的定义位于某个类的作用域中，因此参数不用特意加上`class_name::`
    * 同理函数体中也不需要加
    * 但函数的返回值出现在函数名之前，在解析函数返回值时编译器并不知道函数位于某个类的作用域中。因此函数的返回值必须加上`class_name::reture_type`

  * 名字查找
    * 编译器会先编译成员变量，在编译成员函数。因此成员函数体可以放心使用成员变量而无需关心声明顺序
    * 但成员函数中的参数列表、返回类型则都必须保证使用前先声明

  * 类型名：不能在更小作用域内重复`typedef`
  * 作用域名字查找：尽管会有隐藏，但我们仍然可以通过`::`的形式强制访问外层作用域

* 7.5 构造函数在探

  * 初始值列表：使用构造函数初始值列表还是在函数体中进行赋值是不一样的。一个是初始化，一个是赋值
  * const成员：`const`成员初始化的唯一方式是使用构造函数初始值列表，不能使用构造函数体赋值
  * 初始化和赋值
    * 两者在底层有一定的差异
    * 初始化直接初始化，而赋值一般是先初始化然后在赋值
    * 因为有些情况必须执行初始化，因此建议都采用初始化的方式而不是进行赋值

  * 成员初始化顺序
    * 编译器未对顺序做太多要求
    * 最好初始化顺序和成员变量的声明顺序保持一致
    * 最好不要一个成员的初始化依赖于另一个成员变量的初始值

  * 如果一个构造函数为所有的参数都提供了默认实参，则它实际上也定义了默认构造函数

  * 委托构造函数：和其他语言一样
  * 使用默认构造函数

  ```c++
  Sales_data data; //正确，使用默认构造函数初始化一个对象
  Sales_data data(); //错误，定义了一个data函数，它没有参数，返回类型是Sales_data
  ```

  * 隐式的类类型转换

    * C++语言在内置类型之前定义了隐式转换规则
    * 同样我们可以为类类型定义隐式类型转换规则，我们把这种构造函数叫做转换构造函数
    * 原则：能通过一个实参调用的构造函数定义了一条从构造函数实参类型向类类型的隐式转换规则
    * 如需要使用`Sales_data`的地方，可以使用`std::string`或者`istream`代替，因为我们定义了这样的构造函数

    ```c++
    class book {
    public:
        book() = default;
        book(const std::string name) : name(name) {};
        const std::string get_name() const { return name; }
    private:
        const std::string name{""};
    };
    
    int main() {
        book my_book = std::string ("book_name"); //使用string隐式类型转换
      	//book my_book = "book_name"; //错误，因为连续执行了两次隐式类型转换
        std::cout << my_book.get_name() << std::endl;
        return 0;
    }
    ```

    * 隐式类类型转换只允许执行一次，不能一直向下执行
    * 隐式类类型转换不总是有效的，用完就丢不能存，因为编译器创建的是临时量
    * 抑制构造函数隐式类类型转换：在构造函数前面使用`explicit`关键字
    * `explicit`关键字只对一个实参的构造函数有用
    * `explicit`构造函数只能用于直接初始化

    ```c++
    Sales_data data(null_book);//正确，直接初始化
    Sales_data data = null_book;//错误，这是赋值操作会报类型不匹配
    //本来是可以的，因为声明成了explicit就不行了
    ```

    * `explicit`显示强制类型转换：虽然隐式的不行，但可以强制转，比如使用`static_cast<type>`

  * 聚合类
    * 类似于`data class`
    * 要求
      * 所有成员是public
      * 没有定义任何构造函数
      * 没有类内初始值
      * 没有基类，没有virtual函数
    * 使用：`Data vall = {"1", "2", "3"}`
    * 注意事项
      * 初始化顺序要与声明顺序完全一致
      * 如果初始化数量小于成员数量，则后面的成员默认初始化

  * 字面值常量类
    * 定义：数据成员都是字面值类型的聚合类是字面值常量类。如果一个类不是聚合类，满足下列条件，则它也是字面值常量类
      * 数据成员都是字面值类型
      * 至少有一个constexpr函数
      * 成员的类内初始值必须是常量表达式，或者constexpr构造函数
      * 不能自定义析构函数
    * constexpr函数
      * 函数不能是const，但字面值常量类的构造函数可以是constexpr
      * `constexpr Debug(bool b) : hw(b), io(b), other(b) {};`

  * 静态成员
    * 静态函数不能被声明成`const`，且不能使用`this`指针，因为根本就没有`this`指针
    * 成员函数既可以类内部定义，也可以类外部定义
    * `static`关键字只能存在于类内，不能存在于类外
    * 静态成员(非函数)的声明在类中，我们不能在类内部初始化静态成员，反而应该类外部进行初始化
    * 静态成员(非函数)类内初始化
      * 通常情况下类的静态成员不应该在类内初始化
      * 但如果是常量静态成员则可以在类内部初始化
      * C++不允许非常量静态成员在类内初始


***

## 第十三章

* 前言

  * 函数与运算符
    * `构造函数`：类类型对象在初始化时做什么
    * `拷贝构造函数`：用同类型的另一个对象初始化本对象时做什么
    * `移动构造函数`：用同类型的另一个对象初始化本对象时做什么
    * `拷贝赋值运算符`：将一个对象赋值给同类型的另一个对象时做什么
    * `移动赋值运算符`：将一个对象赋值给同类型的另一个对象时做什么

  * 默认缺省
    * 如果不定义编译器会自动为类型定义默认的操作
    * 但一些时候默认操作是灾难性的

* 13.1 拷贝构造函数

  * 定义：如果一个构造函数的第一个参数是自身类型的引用，且任何额外的参数都有默认值
  * 示例：

  ```c++
  //Book.h
  class Book {
  public:
    	Book(const std::string name);
      Book(const Book &book); //拷贝构造函数
      const std::string get_name() const;
  private:
      const std::string name{""};
  };
  ```

  ```c++
  //Book.cpp
  Book::Book(const Book &book) : name(book.get_name()){}
  ```

  ```c++
  //main.cpp
  int main() {
      Book book_one("book_one");
      Book book_two = book_one;
      std::cout << &book_one << std::endl << &book_two <<std::endl;
      return 0;
  }
  ```

  ```c++
  //output
  0x7ff7b042c630
  0x7ff7b042c610
  ```

  * 非隐式：拷贝构造函数可能会被隐式调用，因此不能加`explicit`
  * 合成拷贝构造函数
    * 如果没有定义则编译器自己生成
    * 可以用来阻止一个对象被拷贝
    * 一般情况下合成拷贝构造函数会逐一拷贝非static成员
      * 类类型：执行类类型的拷贝构造函数
      * 内置类型：直接拷贝
      * 数组：逐一拷贝数组中的元素

  * 拷贝初始化
    * 直接初始化是调用`构造函数`，拷贝初始化是调用`拷贝构造函数`或者`移动构造函数`
    * 发生时机
      * 用一个对象赋值给它同类型的对象时
      * 将一个对象当作实参传递给非引用的形参时
      * 一个返回类型为非引用的函数返回时
      * 用花括号列表初始化一个数组中的元素或者一个聚合类中的成员
    * 拷贝构造函数的参数必须是引用类型：因为如果不是引用类型会使用拷贝构造函数来进行初始化，这样就死循环了

* 13.1拷贝赋值运算符

  * 重载运算符
    * 本质上是一个函数，既有参数列表也有返回值
    * 参数的个数和这个运算符是几元运算符对应
    * 如果一个重载运算符为成员函数，则其最左边的操作数默认绑定到`this`参数上

  * 定义：接收一个与其所在类型相同类型的引用参数。
  * 示例：

  ```c++
  //book.h
  class Book {
  public:
      Book() = default;
      Book(std::string  name);
      Book& operator=(const Book& book);
      std::string name;
      std::string *alloc_space = new std::string("allow_space");
  };
  ```

  ```c++
  //book.cpp
  Book &Book::operator=(const Book &book) {
      std::string content = *book.alloc_space;
      delete this->alloc_space;
      this->alloc_space = new std::string(content);
      this->name = book.name;
      return *this;
  }
  ```

* 13.1析构函数

  * 作用：释放对象所用的资源，销毁对象非static成员
  * 定义：一个波浪号开头。没有返回值，没有参数。由于没参数，不能被重载
  * 示例：

  ```c++
  //Book.h
  class Book {
  public:
      Book() = default;
      Book(std::string  name);
      ~Book();
      std::string *alloc_space;
  };
  ```

  ```c++
  //Book.cpp
  Book::~Book() { delete alloc_space; }
  ```

  * 内容：首先执行函数体，然后销毁成员。销毁顺序和初始化顺序相反。类类型执行自己的析构函数，内置类型不用析构
  * 调用时机
    * 变量离开作用域
    * 当一个对象被销毁时，其成员被销毁
    * 容器被销毁时，其元素被销毁
    * 动态内存分配的对象，执行delete时
    * 临时对象，创建它的完整表达式结束时
  * 合成析构函数
    * 如果没有定义，编译器会自己合成一个默认的
    * 对于某些类，可以用来阻止该类型对象的销毁；除非这种情况，合成析构函数的函数体就为空
    * 析构函数自身不销毁对象，成员是在析构函数调用完成后再进行销毁的

* 13.1

  * 三五法则优先级：`析构` > `拷贝、赋值`。一旦一个类需要`析构函数`，则其就需要`拷贝构造函数、拷贝赋值运算符`，因为默认的`拷贝构造函数、拷贝赋值运算符`是复制指针的值，在`析构函数`中容易被`double delete`

  * `= default` : 显式地让编译器帮忙合成默认的函数

  * ` = delete`

    * 一个被定义成` = delete`的函数可以阻止这个操作的执行，它通知编译器我们不希望生成这个函数
    * 示例：

    ```c++
    //ostream
    basic_ostream(const basic_ostream&) = delete;
    
    basic_ostream& operator=(const basic_ostream&) = delete;
    ```

    * `= default`只能用于编译器能生成的合成函数，但` = delete`可以用于任何函数，有时它用来引导函数的匹配
    * 析构函数不能是删除成员，一旦定义成删除，则该对象不能完成销毁过程

  * 合成的拷贝控制成员可能是删除的
    * 如果一个类有数据成员是不能默认构造，拷贝，复制，销毁的，则对应的操作会被默认定义成`delete`
    * 如果一个类具有引用成员或无法默认构造的const成员，编译器不会为其合成默认构造函数
    * 类有引用成员，合成赋值运算符将被定义成删除的
    * 本质上，当不可能拷贝、赋值、销毁成员时，对应的操作就会定义成删除的。

  * private阻止拷贝
    * 11标准前，通过将函数声明成`private`来阻止禁止某项函数
    * 旧标准了，不推荐使用

* 13.2拷贝控制和资源管理

  * 两种拷贝语义：`值`、`指针`。其中`vector`这种就像值，而`share_ptr`就像指针，`IO`类即不像值也不像指针
  * 行为像值的类的拷贝赋值运算符
    * 通常组合了`构造函数`和`析构函数`的操作
    * 要保证自赋值也是正确的，通常先暂存右操作对象的内容而不是先销毁左操作对象内容

  * 定义行为像指针的类
    * 拷贝的是指针的值而不是指针的内容
    * 析构函数不能直接释放，而是要等到最后一个对象时才能够销毁
    * 使用`share_ptr`来管理资源就是最好的行为像指针

  * 引用计数

    * 原理
      * 构造函数时将计数初始化为1
      * 析构函数时将计数减1，减1后为零即可以清除
      * 拷贝构造函数将右边对象的计数+1，然后把右边计数器的值拷贝给左边的计数器(指针拷贝)
      * 拷贝赋值运算符将右边对象的计数+1，然后把左边对象的计数-1
    * 引用计数器不能是类的成员，只能是动态内存。类的一个成员指向这段动态内存
    * 析构

    ```c++
    if(--*use == 0) {
      delete ps;
      delete use;
    } //同时删除内容和引用计数器
    ```

* 13.3 交换操作

  * 重要性：`swap`还是很重要的，重排算法都要用到`swap`操作
  * 如果一个类自定义了`swap`，那么算法使用类自定义的；否则使用标准库版本
  * 原理：一般`swap`的原理都是一次拷贝，两次赋值
  * 定义不必要性：不是一定要定义`swap`，但分配了资源的类定义`swap`有助于提高性能
  * 内存不必要性：很多时候内存分配是没有必要的。因为`swap`前和后内存是不变的，只是指向所有变化
  * 成员或非成员：作为成员函数只有一个参数；作为非成员有两个参数，且此函数被声明成友元
  * 示例：

  ```c++
  inline void Book::swap(Book &lbook, Book &rbook) {
    useing std::swap;
    swap(lbook.ps, rbook.ps);
    swap(lbook.t, rbook.i);
  }
  ```

  * `inline`: 本来就是小操作肯定定义成`inline`
  * `using std::swap`；放在这里用来重载匹配。一旦`Book::ps, Book::i`有自己版本的`swap`就优先匹配自己版本，否则匹配到`std::swap`
  * 赋值运算符中使用`swap`

  ```c++
  Book& Book::oprator=(const Book book) { //拷贝构造传进来一个临时对象
    swap(*this, book); //交换赋值给*this
    return *this;
  } //离开作用域临时对象直接销毁
  ```

* 13.5 动态内存管理类

  * 运行时分配可变内存大小：
    * 使用标准库容器来保存数据
    * 类自己来决定内存分配
  * `vector`:原理和`java Arraylist`一模一样
  * 移动构造函数和`std::move`
    * `vector`达到阈值销毁重建容量double的过程很适合做`move`而不是`copy`

* 13.6对象移动

  * 移动和拷贝	
    * 某些类不能拷贝，但却能移动。如`IO, unique_ptr`
    * 旧版本C++标准要求标准库容器的对象必须能拷贝，现在要求是只要能移动就行

  * 右值引用
    * 即必须绑定到右值的引用
    * 重要性质：只能绑定到一个马上就要销毁的对象身上
    * 右值引用只能绑定到右值上而不能绑定到左值上
    * 持久与短暂：左值持久而右值短暂。右值要么是字面量或者是临时对象
    * 性质：
      * 该引用马上就要销毁
      * 该对象没有其他用户
      * 可以随时接管右值引用的资源

  * `std::move`
    * 将一个左值变量(变量都是左值)转换成右值引用
    * 告诉编译器：我要强转成右值引用，除了赋值和销毁外我不会做其他的操作了
    * 使用`std::move`而不是`move`，这样可以避免潜在的名字冲突

  * 移动构造函数

    * 示例：

    ```c++
    Book::Book(Book &&book) noexcept : name(book.name),alloc_space(book.alloc_space){ book.alloc_space = nullptr; }
    ```

    * 接受一个同类型的右值引用；不能定义成const；要加`noexpect`；如果有其他参数则必须有默认值；要保证右值可析构，一般将动态内存都置空`nullptr`
    * `noexpect`：通知标准库这个函数不会抛出异常。如果不这样声明则`vector`扩容时依旧会使用拷贝而不是移动，因为有可能抛异常，而标准库容器对异常发生时自身行为提供保障

  * 移动赋值运算符

    * 示例：

    ```c++
    Book &Book::operator=(Book &&book) noexpect {
      if(*this != &book) { //指针地址判断
        free();//释放自身元素
        alloc_space = book.allow_space; //接管资源
        book.alloc_space = nullptr;//置空可销毁
      }
      return *this;
    }
    ```


  * 合成的移动操作
    * `拷贝构造、拷贝赋值`没有定义总会生成合成版本，要么逐拷贝，要么逐赋值，要么删除
    * 一个类一旦定义了`拷贝构造、拷贝赋值、析构`，则编译器不会合成移动操作。而是根据函数匹配使用拷贝
    * 合成条件：没有定义上面所有，且每个非`static`都可以移动。条件比较苛刻
    * 内置成员是可以移动的
    * 定义了移动构造函数或移动赋值运算符的类一定要定义拷贝版本，否则拷贝版本会被编译器定义为删除的
    * 同时有移动和拷贝：根据函数匹配来。右值移动，左值拷贝
    * 只有移动：无论左值还是右值都拷贝

  * 移动的危险性
    * 移动不要随便用
    * 用得好提升性能
    * 用不好出了错都不知道怎么查
    * 性能损失可接受的范围下还是用拷贝更香

  > ***

  * 成员函数的右值引用

  ```c++
  void push_back(const X&);
  void push_back(X&&);
  ```

  * 左值和右值引用成员函数
    * 有时一些迷惑的定义方式使右值也能够被赋值，这是我们正常的函数不希望看到的
    * 可以类似函数末尾加`const`限制
    * `Book& oprator=(const Book&) &;`，限定了只能向可修改的左值进行赋值操作

  * 重载引用函数

    ```c++
    Book sort() &&; //右值引用调用
    Book sort() const &; //任何类型都能调用
    ```

***

## 第十四章

* 14.1基本概念

  * 是一个函数，有返回值，有参数，有函数体

  * 默认参数：除了调用运算符`()`外，其他运算符不能有默认参数

  * 如果是成员函数，则第一个操作数绑定到`this`上面 

  * 不能重定义内置类型的运算符

  * 重载的运算符的优先级和结合律和内置运算符保持一致

  * 直接调用

    ```c++
    //一个非成员运算符重载
    data1 + data2; //调用 + 
    operator+(data1, data2); //直接调用函数
    
    //一个成员函数运算符
    data1 += data2; //调用 +=
    data1.operator+=(data2); //直接调用函数
    ```

  * 某些运算符不应该被重载
    * 带短路性质的元素符不应该被重载，因为重载后无短路性质和原运算符的语义不同
    * 不重载逗号和取地址符，因为C++已经规定了它们作用于类类型的含义

  * 一致性
    * 运算符的行为语义应该与内置一致
    * 返回类型应该与内置一致

  * 作为成员还是非成员
    * `=、[]、()、->`必须作为成员
    * 复合赋值运算符一般来说是成员，但非必需
    * 改变对象状态的运算符或者与给定类型密切相关的运算符应该被声明成成员，如`++、--、*`
    * 具有对称性质的运算符，一般定义成非成员。
      * 原因：`"str" + i;` 和`i + "str";`，如果为成员，前面一种调用是错误的

* 14.2 输入输出运算符

  * 示例：

  ```c++
  //Book.h
  class Book {
      friend std::ostream& operator<<(std::ostream& ostream, const Book& book);
  public:
      Book()  = default;
      explicit Book(std::string name);
  
  private:
      std::string name;
      std::string *alloc_space;
  };
  ```

  ```c++
  //Book.cpp
  std::ostream &operator<<(std::ostream &ostream, const Book &book) {
      ostream << book.name << *book.alloc_space;
      return ostream;
  }
  ```

  * 运算符特点
    * 第一个参数是非const引用，因为会往里面写入值改变状态
    * 第二个参数是const引用，因为不会改变它的状态，且减少复制开销
    * 返回值是引用
    * 输出尽量将是否格式化的权限交给用户而不要擅自做主

  * 必需是非成员函数，一般是友元函数
    * `std::cout << content;`的使用决定了第一个参数肯定必须得是`std::ostream`
    * 而我们无法改变标准库`std::ostream`的定义，因此只能是非成员函数
    * 一般要访问私有成员，因为声明成友元

  * 重载输出运算符：同理

  * 输入时错误

    * 输入流是有可能流出错的，一般要这样写

    ```c++
    if(is) {
      item.value = xxx; //流有效才进行赋值
    } else {
      item = Book(); //流无效就将待输入元素置成原始状态
    }
    ```

* 14.3 算术和关系运算符

  * 经验
    * 定义成非成员函数，因为这样可以允许左右对象进行类型转换
    * 一般定义成常量引用，减少复制且不用改变对象的状态
    * 返回结果是一个临时值，不要定义成引用
    * 一般同时也会定义复合赋值运算符，使用复合赋值运算符来定义算术运算符是最简单的

  * 示例

  ```c++
  Sales_data operator+(const Sales_data &ldata, const Sale_data &rdata) {
    Sale_data sum = ldata; //拷贝构造
    sum += rdata; //使用复合赋值运算符
    return sum; //返回一个临时变量
  }
  ```

  * 相等运算符

  ```c++
  bool operator==(const Book &lbook, const Book &rbook) {
  
  }
  
  bool operator!=(const Book &lbook, const Book &rbook) {
  
  }
  ```

  * 关系运算符
    * 因为算法会用到小于运算符，因此最好还是定义一下
    * 一般是定义`<`号
    * 但是不要为了定义而定义，有些时候就是没有明确的小于关系，就不要定义了

* 14.4赋值运算符

  * 种类
    * 拷贝赋值运算符
    * 移动赋值运算符
    * 其他赋值运算符

  * 示例

  ```c++
  vector&  operator=(initializer_list<value_type> __l) {
    //接受一个initializer_list的参数，这样就可以使用大括号列表初始化了
  }
  ```

  * 特点
    * 必需定义为成员函数
    * 返回值是自己同类型对象的一个引用

* 14.5 下标运算符

  * 特点
    * 必需是成员函数
    * 返回类型是当前索引的引用
    * 最好定义常量和非常量版本，这样常量版本保证了获取后不会改变值

  * 示例

  ```c++
  reference operator[](size_type __n); //非常量普通版本
  const_reference operator[](size_type __n) const; //常量版本
  ```

* 14.6递增递减运算符

  * 特点
    * 建议最好做为成员函数，因为要改变状态；但并没有强制规定
    * 一般要定义两个版本，前置版本和后置版本
    * 前置版本返回值是同类型的一个引用，后置版本返回的是一个值

  * 区分前置版本和后置版本

    * 普通的重载版本无法做区分，因为定义是一样的
    * 因此编译器采取主动添加参数的方式

    ```c++
    Book& operator++();//前置版本
    Book operator++(int);//后置版本
    ```

    * 实参默认传0，但这个值不使用，只是一个标记位
    * 注意后置版本返回的是一个值而非本身的引用
    * 后置版本逻辑
      * 先把当前值存成一个`temp`值
      * 然后调用`前置版本`实现改变值
      * 最后返回`temp`值

* 14.8函数调用运算符

  * 特点
    * 重载函数调用运算符的类能被当作函数使用，他还能存储其他状态，比普通函数更加灵活
    * 重载了`()`的类的对象常常被称作函数对象
    * 函数对象常常用作泛型算法的实参

  * 示例

  ```c++
  //重载()运算符
  int operator()(int val) const {
    return val < 0 ? -val : val;
  }
  
  //调用
  Book book_one; //创建一个对象
  cout << book_one(-10) << endl; //像一个函数一样使用
  ```

  * 用作泛型算法的实参

  ```c++
  for_each(vs.begin(), vs.end(), PrintString(cerr, '\n'));
  //其中把PrintString的一个临时变量用作参数
  ```

* lambda

  * 实质：编译器把lambda表达式编译成一个`未命名类的未命名对象`，该类重载了`()`运算符
  * 引用捕获举例

  ```c++
  //lambda表达式
  [](const std::string &a, const std::string &b) { return a.size() < b.size(); }
  
  //模拟生成的类
  class no_name {
  public:
    bool operator()(const std::string &a, const std::string &b) const { 
      return a.size() < b.size(); 
    }
  }
  ```

  * const : 默认情况下lambda表达式不能改变捕获的变量值，因此生成的类函数一般是const 
  * 等价性：任何地方使用上面lambda表达式的地方都能使用`no_name()`一个临时对象代替
  * 值捕获举例

  ```c++
  //lambda表达式
  [](const std::string &a) { return a.size() < size; }
  
  //模拟生成的类
  class no_name {
  public:
    no_name(int size) : size(size) {}
    bool operator()(const std::string &a) const { 
      return a.size() < size; 
    }
  private:
    int size;
  }
  ```

  * 等价性：任何地方使用上面lambda表达式的地方都可以使用`no_name(size)`来代替，必需提供一个实参

* 标准库定义的函数对象

  * 特点
    * 由标准库定义，一般都是模版类
    * 使用：`plus<int> my_plus; my_plus(1, 2);`

  * 举例

  ```c++
  template<typename _Tp>
      struct less : public binary_function<_Tp, _Tp, bool>
      {
        bool
        operator()(const _Tp& __x, const _Tp& __y) const
        { return __x < __y; }
      };
  ```

  * 这些类型能当作函数对象使用，如`less<string>()`
  * 一般都用作实参传入算法函数

* 可调用对象与fiction

  * 可调用对象
    * 函数
    * 函数指针
    * lambda表达式
    * bind创建的对象
    * 重载了`()`的类

  * 类型：上面可调用的对象它们有自己的类型，而且都是不能直接相互转换的；它们有时类型不同但调用形式是相同的
  * 类型示例：

  ```c++
  //普通函数
  int add(int a, int b);
  //函数指针
  int (*p)(int, int) = add;
  //lambda表达式
  auto my_add = [](int a, int b) { return a + b;}; //lambda的静态类型是一个未命名的类
  //类
  class my_add {
  public:
      int operator()(int a, int b) { return a + b;}
  };
  ```

  * 标准库`function`
    * 作用：能够存静态类型不同但调用方式相同的可调用函数对象
    * 使用：`funtion<int(int, int)>`
    * 函数表：`map<string, funtiong<int(int,int)>> ops;`

* 14.9重载、类型转换与运算符

  * 类型转换运算符
    * 特点
      * 是类的成员函数
      * 负责将一个类类型的值转换成其他类型
    * 形式：`oprator type() const`
    * 既没有参数也没有返回值；`type`必须是函数能返回的类型；因为不改变转换的内容，常定义成const

  * 举例

  ```c++
  class small_int {
  public:
      small_int(int value = 0) : value( value % 255 ){}; //可以隐式由int -> SmallInt
      operator int() const { return value; } //可以隐式由SmallInt -> int
  
  private:
      int value;
  };
  ```

  ```c++
  SmallInt si;
  si = 4; // 首先隐式将 int -> small_int，再调用拷贝赋值运算符
  int j = si + 3; //隐式将 small_int -> int
  SmallInt sb = 4; //直接隐式将 int -> small_int
  ```

  * 类型转换运算符是隐式进行的，无法主动调用，因此无法传递实参
  * 类型转换运算符因为式隐式的，用起来可能会很坑，慎用。但一般要定义向`bool`的转换
  * 布尔转换举例：`explicit operator bool() const { return _M_ok; }`
  * 显式类型转换符
    * 因为隐式的太坑了，因此要定义显示的
    * 定义：`explicit operator int() const { return value; }`
    * 使用：`int j = static_cast<int>(small_int(400));`
    * 即使声明成了`explicit`，但还是可能会被隐式执行。就是在作为条件转换成`bool`时

  * 避免二义性的转换：注意就行，类型转换不要瞎定义
  * 重载函数与用户定义的类型转换
    * 用户自定义类型转换会使函数匹配变得更复杂
    * 当调用重载函数时，如果两个用户定义的类型转换都可以匹配，则认为一样好有二义性

  * 函数匹配与重载运算符
    * 不能通过调用形式来区分是成员函数还是非成员函数
    * 候选函数集即应该包括成员函数，也应该包括非成员函数

***

## 第十五章

* 15.2定义基类和派生类

  * 示例

  ```c++
  //Base.h
  class Base {
  public:
      Base()  = default;
      explicit Base(std::string name) : name(std::move(name)) {};
      virtual int virtual_method(); // 一个虚方法
      virtual ~Base() = default; // 定义虚析构函数
  private:
      std::string name;
  };
  ```

  ```c++
  //Derive.h
  class Derive : public Base {
      int virtual_method() override; //加上override标记
  };
  
  //Derive.cpp
  int Derive::virtual_method() { return 0; }
  ```

  * 基类都必需定义一个析构函数，且该函数还必须是虚函数
  * 成员函数与继承
    * 任何非static的函数都可以是虚函数
    * 虚函数在派生类中默认隐式地也声明成虚函数
    * 虚函数的解析发生在运行时，其他函数的解析都发生在编译时

  * 继承时的`public, protected`
    * 只影响派生类从基类继承而来的成员对派生类对象的可见性
    * `protected`限制最好访问权限为`protected`，以此类推
    * 不影响派生类访问基类的成员，只要不是private就都能访问

  * 派生类中的虚函数
    * 如果要重写，则派生类必需在头文件中声明一下这个函数
    * 如果不重写，甚至不用声明这个函数，直接默认继承基类的版本

  * 类型转换
    * C++没有规定派生类的内存如何分配
    * 因为派生类中有完整的基类成员，因此派生类可以当作基类来使用
    * 编译器会隐式地进行派生类到基类的类型转换

  * 派生类构造函数
    * 每个类控制自己成员的创建与销毁，一个类不能去控制另外的类
    * `Derive(xxx) : Base(xxx), ...`
      * 首先`Base()`的初始化列表
      * 然后`Base()`的函数体，执行完成时基类部分已经完成了初始化
      * 接着`Derived()`的初始化列表
      * 最后`Derived()`的函数体
    * 虽然派生类可以直接访问赋值基类的非私有成员，但尽量不要这么干。做个清晰的职责划分

  * 静态成员
    * 如果基类定义了一个static成员，则无论有多少个派生类，只存在静态成员的唯一实例
    * 如果静态成员可以访问，则既能通过基类访问，又能通过派生类访问

  * 被用作基类的类
    * 一个类如果要被用作基类，则用之前它必须有完整的定义。不然派生类根本不知道有什么基类成员
    * 即类不能派生自己

  * 防止继承：`class final_class final {}`
  * 对象之间不存在类型转换
    * 派生类对象可以赋值给基类的引用或者基类的指针，在运行时执行动态绑定
    * 派生类对象直接赋值给基类对象时，实际上调用的是`拷贝构造函数`
    * `拷贝构造函数`的参数是基类的一个引用，因此派生类对象会类型转换为基类的一个引用
    * 执行的拷贝构造函数也是基类的版本，因为它不是一个虚函数
    * 所以最后的结果就是只有基类的部分被拷贝了
    * 等于对象被`截断`了

  * 总结类型转换规则
    * 派生类对象隐式转换成基类的引用或指针
    * 不存在基类向派生类的转换
    * 派生类对象向基类对象转换会被截断，因为使用的是基类的拷贝成员

* 15.3虚函数

  * 定义：无论我们是否使用到虚函数，都得提供一个定义，因为连编译器都无法确定会使用那个版本的虚函数
  * 动态解析
    * 只有引用和指针才会执行动态解析
    * 普通的对象函数调用版本早在编译时就确定下来了

  * 虚函数与默认实参数
    * 虚函数也可以有默认实参，实参值由本次调用的静态类型决定
    * 因此虚函数要么别定义默认实参，要么基类和派生类的默认实参保持一致

  * 回避虚函数极值：加上作用域符号`::`

* 15.4 抽象基类

  * 纯虚函数：`= 0`
  * 示例：

  ```c++
  virtual int virtual_method(int i) = 0;
  ```

  * 抽象基类：含有(继承但未覆盖)纯虚函数的类是抽象类
  * 抽象类不能创建对象

* 15.5访问控制

  * 友元与继承：不能继承友元关系，每个类控制自己成员的访问权限

  * 改变个别成员的可访问性

    ```c++
    //Base.h
    class Base {
    protected:
     std::string name; // protected
    };
    //Drevie.h
    class Derive final : private Base { //首先是私有继承
    public:
     using Base::name; //然后声明成共有
    }
    ```

    * 派生类只能为那些它能够访问到的成员提供`using`声明

  * 默认的继承保护机制
    * 默认的继承保护级别由定义派生类所用的关键字决定
    * `class`是私有继承，而`struct`是公共继承
    * 这两个关键字的唯一区别就是`默认成员访问说明符`和`默认派生访问说明符`

* 15.6继承中的类作用域

  * 原理
    * 派生类的作用域是`嵌套`在基类的作用域中的
    * 当查找一个名字时首先在派生类中进行查找，如果找不到就在更大一层的作用域即`基类`的作用域进行查找
    * 最后上升到顶层作用域都找不到就报错

  * 在编译时进行名字查找
    * 静态类型决定了那些成员是可见的
    * 我们能使用那些成员也是由静态类型来决定的，即在编译期就决定了

  * 隐藏
    * 内层作用域可以隐藏外层作用域的定义，因此派生类能够隐藏同名的基类成员
    * 但除了覆盖继承而来的虚函数外，建议不要随便覆盖基类的定义

  * 名字查找详细流程
    * 先确定静态类型
    * 在静态类型中查找，找不到依次向上找，最后都找不到就报错
      * 一旦找到，就进行常规类型检查
      * 假设调用合法，根据是否是虚函数产生不同的代码
        * 指针或引用，且是虚函数，进行虚函数调用
        * 否则进行静态解析

  * 名字查找先于类型检查
    * 编译器首先找名字，然后在进行类型检查
    * 即重载所有的名字都必须在同级的作用域内
    * 不能跨作用域进行函数重载

  * 虚函数与作用域：虚函数必须有相同的形式参数，否则名字查找直接结束
  * 覆盖重载虚函数
    * 基类定义了3个虚函数，这三个虚函数是相互重载的
    * 如果派生类要重写从基类继承而来的3个虚函数中的一个，则它必须重写3个。因为名字查找先于类型检查，不允许跨作用域重载
    * 但可以使用`using`声明将基类中的函数都引进来，这样只有重写1一个就行了
    * `using`指定一个名字而不用指定形参列表

* 15.7 拷贝函数与拷贝控制

  * 虚析构函数
    * 析构函数必须定义成虚函数
    * 调用`delete ptr;`的时候动态解析析构函数
    * 类控制自己的析构过程

  * 合成拷贝控制与继承
    * 类似于构造函数，每个类负责自己成员`初始化、赋值、销毁`，然后调用基类的版本向上传递调用
    * 向上调用无论是自定义版本或者合成默认版本都无所谓，只要可以访问且不定义成删除就行了
    * 定义了析构函数，则默认不合成移动函数

  * 派生类析构函数
    * 派生类的析构函数不用调`Base::~Base()`
    * 因为这是自动执行的
    * 对象的销毁顺序和它的创建顺序刚好相反
    * 如果构造函数或者析构函数调用了某个虚函数，则我们应该调用对应的版本。不然容易崩

  * 继承的构造函数

    * 派生类能够重用(继承)基类的构造函数
    * 派生类不能继承拷贝和移动函数，如果没有定义，则由编译器自己合成默认版本
    * 使用方式

    ```c++
    using Base::Base;
    ```

    * 通常情况下，`using`的作用是让名字在当前作用域可见。但当作用于构造函数时，会让编译器生成代码。生成代码如下：`Derive(args) : Base(args) {};`
    * 如果类有自定的成员，使用这种方式时，类成员执行默认初始化

* 15.8 容器与继承

  * 容器的泛型参数不能是静态类型，否则存放派生类时会被截断
    * 在容器中最好存放`(智能)指针`，如`vector<share_ptr<Base>> vec;`
  * 面向对象悖论
    * C++的对象不支持面向对象，只有指针和引用支持
    * 因为指针会增加程序的复杂性，因此经常定义一些辅助类来表示这种情况

****

## 第十六章

* 核心：不像java的泛型那么拉胯，C++的泛型是真正的泛型，在编译期真正地生成了

* 函数模版

* 定义

```c++
template <typename T> T add(const T &a, const T &b) { return a + b; }
```

* 使用
  * 模版实参的值一般是隐式绑定到模版形参上的
  * 比如上面的函数模版传入`5.6`会默认`T`是`double`(只是举例，因为右值不能绑定到左值)

* 实例化函数模版
  * 当调用模版函数时，编译器用推断出来的实参`实例化`一个特殊版本的函数
  * 编译器生成的版本通常称作模版的实例
  * 实例化发生在调用时

* 多个模版参数：

```c++
template <typename T, typename M> T add(T &&a, M &&b) { return a + static_cast<T>(b); }
```

* 非类型模版参数

  * 被用户提供或者编译器推断的值所替代
  * 这些值必须是常量表达式，从而允许编译器在编译时就实例化模版
  * 举例

  ```c++
  template <unsigned M, unsigned N> 
  std::string add(const char (&lp)[M], const char (&rp)[N]) {
      return std::string(lp) + std::string(rp);
  }
  ```

  * 在强调一遍，得是编译期就能确定的常量表达式。如数组大小

* 函数模版同样能声明成`constexpr`或`inline`的
* 模版程序应该尽量减少对实参类型的要求
* 模版编译
  * 定义一个模版时，编译器不生成代码
  * 只有当我们实例化出模版的某一个特定的版本时，编译器才会生成代码
  * 这一个特性影响我们我们如何组织代码，以及错误何时被检测到
  * 为了生成一个实例化版本，编译器需要掌握模版的定义。因此模版的头文件通常既包括声明也包括定义
  * 也因此模版的错误一般只有在实例化的时候才能报出来

类模版

* 函数模版编译器可以进行模版参数推断，但类模版则完全不行，必须手动声明
* 定义：

```c++
template<typename _CharT>
  class basic_string;
```

* 实例化类模版 
  * 一个类模版的每个实例都是一个单独的类
  * 一个模版的两个实例之间没有任何关联

* 在模版作用域中引用模版类型
  * 通常将模版自己的参数当作被使用模版的实参
  * 如：`std::share_ptr<vector<T>> data;`

* 类模版的成员函数

  * 既能定义在类的内部，也能定义在內的外部
  * 类外定义：

  ```c++
  template<typename _FwdIterator>
  void base_string::_M_construct(_FwdIterator __beg, _FwdIterator __end,std::forward_iterator_tag) {};
  ```

  * 类外定义既要用`template`，也要`base_string::`
  * 类内定义：

  ```c++
  template <typename T>
  class Base {
  public:
      T get_T() { return T(); }
  };
  ```

  * 实例化时机：默认情况下，对于一个实例化的类模版，其成员只有在使用时才会进行实例化。这导致即使有一些方法不能正常实例化，我们只要避开这个方法就能正常使用这个类

* 类模版内部简化
  * 类模版的作用域内可以省略模版形参
  * 如直接使用`vector&`，而不用使用`vector<T>&`

* 在类模版外使用类模版名

  * 这种情况下我们并不是在內的作用域中，直到遇到类名时才进入类的作用域

  ```c++
  template <typename T>
  Base<T> Base<T>::get_T() { return Base(); }
  ```

  * 返回值`Base<T>`，因为此时还没进入类作用域，因此编译器根本不知道模版参数的存在，我们必须显示添加
  * 语句`return Base();`时，因为已经进入了类作用域，因此不用加模版参数
  * 即类外要添加，类内不用添加

* 类模版和友元：类与友元是否是模版互相之间没有任何关系
* 模版类型别名
  * 模版不是一个类型，模版的某个实例才是一个类型
  * `typedef Base<T> b_t;`
  * `template<typename T> using t_pair = std::pair<T,T>;`

* 模版参数作用域：声明之后，模版定义结束之前

* 使用模版类的类型

  * 当使用`string::size_type`时，编译器知道`string`的定义，因此它知道`size_type`是一个类型，而且还是一个整型
  * 但使用`T::size_type`时，`T`还没有实例化，编译器根本没有为它生成代码，因此编译器不知道`size_type`到底是一个变量还是一个类型还是一个其他的什么
  * C++一般默认是变量而不是类型，因此当确实是一个类型时，需要我们进行手动声明
  * 举例

  ```c++
  typename T::size_type get_size();
  ```


* 模版默认实参：无论函数模版还是类模版，无论是类型形参还是正常形参(常量表达式)，都可以设置默认实参
* 成员模版
  * 一个类和这个类的成员是否是模版互相没有关系
  * 一个非模版类的虚函数不能是模版的
  * 成员函数模版是`函数模版`，因此成员模版可以隐式传模版实参

* 控制实例化

  * 模版使用时才实例化，因此相同的实例可能出现在多个对象文件中
  * 当两个独立编译时，使用同一个模版，且传的相同的实参时就会实例化相同的实例
  * 解决方案：和变量一样，多处声明一次定义

  ```c++
  extern template class my_class<string>; //声明
  template class my_class<string>; //定义
  ```

  * 一个`extern声明`即告诉程序在其他位置有该实例的一个`非extern定义`
  * 每个实例化的声明，必须在程序中的某个位置有其显示实例化的定义

* 实例化定义会实例化所有成员
  * 普通的类模版实例化，其成员往往不立即实例化，而是等到调用某个成员时编译器才为之生成代码进行实例化
  * 但显示实例化定义不同，它在实例化生成类的代码时就一起生成了该类的所有成员

* 效率与灵活性
  * 运行时绑定删除器
    * `share_ptr`有一个成员，这个成员是一个指针，这个指针指向真正的删除器
    * 类似可以推测这个成员的类型大概类似于`void*`即可以指向任何类型
    * 而真正的类型要直到运行时动态才能知道
    * 这样的优点是用户可以随时动态更改删除器的类型
    * 缺点是在访问或者析构的时候要进行类型判断，动态的效率比较低
  * 编译时绑定删除器
    * `unique_ptr`是一个模版类，在实例化时就必须要显示传递一个类型参数 - 删除器的静态类型
    * 因此`unique_ptr`的删除器类型在静态编译时就是完全可知的
    * 这样的优点是效率高，完全不用动态开销，静态时把一切都处理好了
    * 缺点是如果用户想更换删除器的类型将十分麻烦，扩展性比较弱

16.2模版实参推断

* 类型转换与模版类型参数
  * 模版实例化的过程传参数和函数调用传参数一模一样，都是用实参去初始化形参
  * 参数初始化的过程中一般支持隐式类型转换，但模版参数实例化支持的隐式类型转换十分之少。只支持两种
    * 无视顶层`const`的转换
    * 数组转换为指针，函数类型转换为函数指针

* 使用相同模版参数类型的函数形参

  * 错误示范

  ```c++
  template <typename T> bool compare(const T&, const T&);
  
  long i = 10;
  compare(i, 9);//编译错误(long, int)和(T, T)不匹配
  ```

  * 正确示范

  ```c++
  template <typename T, typename S> bool compare(const T&, const S&);
  
  long i = 10;
  compare(i, 9);//编译正确(long, int)和(T, S)匹配
  ```

  * 此限制仅适用于模版参数，对于模版中的非模版参数，执行正常的参数匹配

* 指定显示模版实参

  * 下面这个例子中`T3`是无法进行推断的

  ```c++
  template <typename T1, typename T2, typename T3> T1 sum(T2, T3)
  ```

  * 使用

  ```c++
  long val = sum<long long>(i, lng);
  //T1 参数显示指定为long long
  //T2 参数由i进行推断
  //T3 参数由lng进行推断
  ```


* 尾置返回类型
  * 代码出现的顺序影响编译至关重要
  * 普通的函数形式的返回值定义不能使用`decltype(arg)`，因为在解析返回值时甚至都没有`arg`的存在
  * 但是`尾置返回类型`就很神奇，它的返回值类型出在参数值之后，因此它就能使用
  * `auto fcn(It begin, It end) -> decltype(*beging)`

* 进行类型转换的标准库模版类
  * 有时候我们只能获得一个引用，根本无法获得真正的静态类型，标准库提供了获取办法
  * `std::remove_reference<decltype(*begin)>::type`即静态类型，要求`decltype(*begin)`是一个引用

* 引用类型绑定
  * 普通非常量左值引用`int &`：普通左值引用只能绑定一个左值，即变量
  * 常量左值引用`const int &`：常量左值引用不仅可以绑定一个左值变量，还能绑定一个右值。即`const int &t = 5;`是合法的
  * 右值即绑定右值，不能绑定左值

* 引用折叠和右值引用参数
  * 正常情况下左值不能绑定到右值引用上，但有两种例外
  * 情况1:模版参数类型为`T&&`，尝试将一个左值`int &`绑定到模版形参上，则这种情况下编译器会推断出`T`的实际类型为`int &`。则模版参数实际类型为`int &`的右值引用
  * 引用的引用会发生引用折叠，两两搭配有4种情况。只有一种情况`右值引用的右值引用`折叠结果是`右值引用`，其他3种情况就都折叠成`左值引用`
  * 因此`T&&`的模版参数我们可以传递任何的参数，既可以是左值也可以是右值

* 编写接受右值引用参数的模版函数
  * 模版参数`T&&`的绑定很离谱
    * 当传递一个左值时，`T`为`int&`，则是引用而非拷贝
    * 当传递一个右值时，如`42`，`T`为`int`则会进行拷贝
  * `T&&`接受右值，`const T&`接受左值。可以用它们两个来进行函数重载，会优先进行匹配

* `std::move`

  * 定义

  ```c++
  template<typename _Tp>
      constexpr typename std::remove_reference<_Tp>::type&&
      move(_Tp&& __t) noexcept
      { return static_cast<typename std::remove_reference<_Tp>::type&&>(__t); }
  ```

  * `参数`为模版参数+右值引用 --> 能匹配任何类型
  * `static_cast`：使用其进行显示的左值到右值的转换是允许且可行的

***

## 第十八章

* 18.1 异常处理

  * 程序的控制权从`throw`转移到与之匹配的`catch`模块

    * 沿着调用链的函数可能会提前退出
    * 沿着调用链的对象将被销毁

  * 栈展开

    * 首先在`catch`语句块里面找
    * 没有则退出函数，在主调函数中找
    * 最后一步一步退出直到最后没有找到执行标准库函数`terminal`

  * 自动销毁

    * 栈帧退出意味着变量离开其作用空间，意味着变量要执行`析构函数`
    * 即使某个对象只构造了一部分，也要确保已构造的函数成员能被正确销毁

  * 析构函数与异常

    * 析构函数肯定会被执行，但可能析构函数中的释放资源代码由于异常而被跳过
    * 因此我们使用类来控制分配资源，就应该保证无论是否发生异常都能够正确释放资源
    * 综上原因，析构函数不应该抛出它自身无法处理的异常。即如果要抛出异常，就必须自己处理掉。否则别人抛出异常执行到此析构函数，自己抛了个自己无法处理的异常，程序就崩溃了
    * 实际常常因为析构函数仅仅是释放资源，因此一般情况是不会抛出异常的

  * throw表达式

    * 可以抛出异常对象，即一个类对象
    * 也可以抛一个表达式

  * 异常对象

    * 异常对象类型必须拥有可以访问的`析构函数和构造函数`，`拷贝或移动构造函数`。因为`catch`那里和函数参数绑定一模一样
    * 异常对象的作用域有点特殊，它位于编译器管理的空间中。一旦处理完成就被销毁掉
    * 因为退出块是执行析构函数销毁对象，因此抛出局部对象肯定是错误的。不允许抛出局部变量表达式
    * 抛出类型是静态类型，不会运行时绑定

  * 捕获异常

    * `catch`的类型可以是左值引用
    * 和异常绑定的过程和函数参数初始化一模一样。非引用可能截断，引用执行函数可能会动态绑定

  * 查找匹配处理代码

    * 越优先匹配的应该放在最前面
    * 允许非常量转常量、允许派生类转基类、允许数组和函数转换成指针
    * 除此之外的转换不允许

  * 重新抛出

    * 一个异常处理后，可以选择将它重新抛出
    * 重新抛出仍然是一条`throw`，只不过不包含任何表达式
    * 此`throw`只能出现在`catch`块内
    * 实际的行为是将该异常继续沿着调用链向上进行传递
    * 只有当`catch`住的是引用时，才会改变异常本身的值，否则不变

  * 捕获所有异常

    * `catch(...)`
    * 一般出现在最后的位置，否则它后面的捕获都没有任何用处

  * `try`与构造函数

    * 因为列表初始化时构造函数体的`try`并未生效，因此构造函数体不能处理列表初始化的异常
    * 如果想要处理，需要这样写

    ```c++
    my_class::my_class() try : memember(memember) {
        //body
    } catch (...) {
        //body
    }
    ```

    * 如果参数初始化的时候出现了异常，那这个异常是主调函数来处理的，与被调函数无关

  * `noexcept`

    * 可以有助于优化
    * 一个`noexcept`也可以抛出异常，但一旦抛出就会调用标准库函数`terminal`来终止程序
    * 因此`noexcept`标识真的不抛出异常，或者表示抛出异常就抛出吧，无法处理

  * 异常说明实参

    * `noexcep(true)`：不会抛出异常
    * `noexcep(false)`：可能抛出异常

  * `noexcep`运算符

    * 使用`noexcep(expression)`，返回一个`bool`值，和`sizeof()`运算符很像

  * 绑定性

    * 指针绑定`noexcept`声明要一直
    * 虚函数的声明要一直
    * 如果都`noexcept`，在编译器合成默认版本时就`noexcept`

***

## 第十九章

* 19.1 控制内存分配

  * `new`的三个步骤

    * 分配一块未命名的内存空间
    * 编译器执行构造函数构造对象，并为对象传入初始值
    * 对象被分配了空间并构造完成，返回一个指针

  * `delete`两个步骤

    * 执行对象的析构函数
    * 释放内存空间

  * `new`和`delete`优先级

    * 类的作用域最高
    * 全局作用域次之
    * 都找不到就用标准库定义版本

  * 版本

    * `new`和`delete`一共定义了8个版本。根据是否分配数组，是否抛出异常做了区分
    * 当运算符函数被定义成类成员时，它是隐式静态的。因为`oprator new`在构造函数之前执行，当时还没有对象
    * 我们可以定义8个版本之外的自定义运算符函数，但`void *operator new(size_t, void*)`不能被重载，这种形式只供标准库使用

  * 重载都的区别

    * 重载`new`和`delete`和其他运算符重载不一样
    * 实际上我们根本没法改变`new`和`delete`的本身行为
    * 我们只是可以改变内存的分配方式而已

  * `malloc`和`free`

    * 返回一个指针或者`0`表示分配失败
    * `free`将内存归还给操作系统。`free(0)`是安全的

  * 自定义举例

    ```c++
    void *operator new(size_t size) {
        if(void *mem = malloc(size)) {
            return mem;
        } else {
            throw bad_alloc();
        }
    }
    
    void operator delete(void *mem) { free(mem); }
    ```

* ,19.2 运行时内存识别

  * `RTTI` - `runtime type identification`

    * `typeid`运算符，用于返回表达式的类型
    * `dynamic_cast`运算符，用于将基类的指针或引用安全转换为派生类的指针或引用

  * `dynamic_cast`使用形式

    * `dynamic_cast<type*>(...)`
    * `dynamic_cast<type&>(...)`
    * `dynamic_cast<type&&>(...)`

  * 转换失败

    * 如果是指针，转换失败返回`0`
    * 如果是引用，抛出`bad_cast`异常

  * 使用

    * 指针类型转换：`if(Deried *dp = dynamic_cast<Derived*>(bp))`

    * 这样既能类型转换，又能进行转换结果的判断

    * 引用类型转换

      ```c++
      try {
          const Derived *r = dynamic_cast<const Derived&>(b);
      } catch (bad_cast) {
          //...
      }
      ```

  * `typeid()`运算符

    * 返回值是`type_info`或子类的常量引用对象
    * 作用于数组和指针返回值不会执行类型转换，而是保留其本身的类型
    * 当无虚函数时，返回提示的类型是静态类型；至少含有一个虚函数时，返回提示的类型要直到运行时才知道

  * 使用

    * `if(typeid(*bp) == typeid(*dp))`，两个类型都不知道
    * `if(typeid(*bp) == typeid(Derived))`，一个类型不知道

  * 是否求值

    * 无虚函数，直接返回静态类型时不会进行求值
    * 有虚函数，运行时才知道要进行求值，因此要保证其求值程序不崩

  * `equal`的实现

    ```c++
    class Base {
        friend bool operator==(const Base &lhs, const Base &rhs) {
            return typeid(lhs) == typeid(rhs) && lhs.equal(rhs);
        }
    public:
        virtual bool equal(const Base&) const;
    }
    
    bool operator==(const Base &lhs, const Base &rhs) {
        return typeid(lhs) == typeid(rhs) && lhs.equal(rhs);
    }
    
    bool Derived::equal(const Base &rhs) {
        if(Derived &r = dynamic_cast<Derived&>(rhs)){
            //...
        } else {
    
        }
    }
    ```

  * `type_info`类

    * 没有默认构造函数，拷贝和移动构造函数定义为删除，赋值运算符被定义成删除
    * 创建`type_info`的唯一途径是通过`typeid`运算符

* 19.8 固有不可移植特性

  * 位域
    * 位域的实现与机器密切相关
    * 最好都存储无符号数
    * `Bit modified: 1; //一个mofify位，宽度位1`
  * `volatile`
    * 对象的值有可能在程序的控制之外被改变时，应该将对象声明成`volatile`
    * 使用方式和`const`很相似
    * 允许拷贝`volatile`对象，但很多时候这是无意义的
  * `extern "C"`
    * 这是一个`C`函数
    * 要求我们必须有权访问该语言的编译器，且和当前`C++`编译器是兼容的

****

## HomeWork

* 1.3

  ```c++
  #include<iostream>
  
  int main()
  {
  std::cout << "Hello World" << std::endl;
  return 0;
  }
  ```

* 1.4

  ```c++
  #include<iostream>
  
  int main()
  {
  double x,y;
  std::cout << "please enter x value, and note that x must be a double value." << std::endl;
  std::cin >> x;
  std::cout << "please enter y value, and note that y must be a double value." << std::endl;
  std::cin >> y;
  std::cout << "x = " << x << ", and y = " << y << ", and x*y = " << x * y << std::endl;
  return 0;
  }
  ```

* 1.16

  ```c++
  #include<iostream>
  
  int main()
  {
  int a,b;
  std::cin >> a >> b;
  std::cout << "a + b = " << a + b << std::endl;
  }
  ```

* 2.6

  ```c++
  #include<iostream>
  
  int main()
  {
   int a = 011;
   int b = 11;
   std::cout << a << std::endl << b << std::endl;
   return 0;
  }
  
  //result
  9
  11
  ```

* 2.11

  ```c++
  extern int ix = 1024; // 定义
  int iy; // 定义
  extern int iz; // 声明
  ```
