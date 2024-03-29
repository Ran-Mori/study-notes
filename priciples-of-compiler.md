# 编译原理

## 第一次课

* 程序执行步骤

  * 代码源文件编译成二进制文件 - **编译原理**

  * 操作系统分配资源成为进程 - **操作系统**

  * 进程跑在某一个特定的体系结构内 - **计算机体系与结构**

  * 最后跑出程序


* 1.2 程序的编译器优化举例

  * 加了 **const **后说明指针str的值不会变，可以做编译器优化

    ```c
    int atoi(const char *str)

  * 加了 **volatile** 说明这个变量经常会变，编译器千万别对这个变量做任何优化，按部就班的算就最好

    ```c
    volatile double x; //比如嵌入式传回来温度传感器的值 
    ```

  * Java 的 String 不能append，多次使用追加性能大降


* 1.3 编译器原理概述

  * 建立人与机器的桥梁

  * 把人看懂的高级程序代码编译成机器能看懂的二进制代码

    > 广义编译器： 一种语言翻译成另一种语言，实际上就是翻译


* 1.4 编译过程

  > a.c -> frontEnd -> IR(LLVM IR) -> backEnd -> nativeCode

  * 一个语言一个前端

  * 都向中间LLVM IR转化

  * 一个平台一个后端，如x86,ARM


* 1.5 编译器前端

  * 预处理 - 比如展开 “#include<iostream>”

  * 词法分析器 - 拆分单词

  * 语法分析器 - 分析逻辑结构信息，如 if-else,while，得到一个语法树

  * ……


* 1.6 语言

  * 语言是一个符号系统

  * 语言完成定义包括 **语法** 和 **语义**

*************************

## 编译器概述

* 什么是编译器

  * 编译器是一个 **程序** ，把源代码翻译成目标代码

    > **源代码**： C++, Java, Python, HTML, C#
    >
    > **目标代码**： x86, IA64, ARM, MIPS


* 编译特点
  * 语义相同，不改变源代码原意


* 编译器与解释器区别

  * 编译器输入源代码产生的是 **可执行程序**，是一种离线 **offline**

  * 解释器输入源代码产生的是 **结果**， 是一种在线 **online**

  * 尽管有不同，但这两个相同点特别多，核心原理都一样

********************

## 编译器结构

* 编译器的高层结构

  * 编译器是具有非常 **模块化** 的高层结构

  * 编译器分成前端和后端

  * 前端： 词法分析，语法分析

  * 后端： 指令集成， 指令优化

  * 编译器可以看成是一个流水线结构

  * 使语言抽象程度依次降低，并不是一次性直接让高级语言编译成目标语言


* 编译程序1+2+3到栈式计算机

  * 首先进行词法语法分析成一棵语法树

  * 然后进行后序遍历，进行语法生成

  * 遇到数字就压栈

  * 遇到符号就出栈两次，计算后在压栈


* 编译器简要结构

  * 前端从输入到语法树

  * 后端从语法树到目标代码


* 代码优化

  * 输入是语法树，输出也是语法树

  * 优化后在交给后端处理进行代码生成 

**************

## 编译器实例

* Sum语言到Stack计算机

  * 输入

    > 整形数字：n
    >
    > 加法：a+b

  * 输出

    > push n
    >
    > add


* 1+2+3编译简述

  * 词法分析：分析出5个字符

  * 语法分析：分析5个字符是否符合Sum的语法定义

  * 语法树构建：分析后构成一棵语法树

************

## 词法分析任务

* 任务
  * 读入 **字符流** 进行切分，输入出**记号流**


* 举例

  ```java
  if (x > 5)
  ```

  ```bash
  IF LPAREN IDENT(x) GT INT(5) RPAREN \N
  ```

* 记号集

  * 字符是一个集合

  * 比如说包含 

    > IF,  LPAREN,  IDENT,  GT,  INT,  RPAREN,  \N


* 记号的数据结构定义
  * 一个结构体，包含至少两个属性
    * 属性一： 记号的类型
    * 属性二： 记号的值
    * ……


* 字符流和记号流

  * 字符流： 和被编译的语言密切相关，如ASCII，Unicode，Java，Python

  * 记号流： 编译器内部定义的数据结构，编码所识别出来的词法单元

**************

## 词法分析之手工构造法

* 词法分析器的两种方案

  * 手工生成： 相对复杂，容器出错，但是非常主流

  * 生成器生成： 可快速成型，代码量较少，但难以控制细节


* 状态图
  * 使用状态图来进行词法分析


* 识别关键字

  * 方法一： 在状态图中出分支，比如出一条单独判断 **if** 的分支

  * 方法二： 建一个关键字**哈希表**。所有输入都进行普通识别，然后在查表判断是不是关键字

****************

## 词法分析之正则表达式

利用正则表达式可以表示关键字，整数，字符串等

* 语法糖
  * 汇编语言只要有赋值和跳转两种指令就可以表示所有指令，其他指令都可以算作是语法糖

********************

## 自动机

* 组成部分

  * 字母表

  * 状态集

  * 初始状态

  * 终止状态集

  * 转移函数


* 有限状态自动机

  * 英文名称： DFA 确定有限自动机

  * 特点： 转移函数的值集合有且仅有一个元素


* 非确定有限状态自动机

  * 英文名称：NFA  非确定有限自动机

  * 特点：转移函数的值集合不一定只有一个元素


* 小结

  * 自动机分为DFA和NFA

  * DFA转移状态确定唯一，NFA转移状态不确定

  * NFA不好分析，一般会向DFA转化

**************

## RE -> NFA -> DFA -> 词法分析器代码

* RE -> NFA
  * Thompson算法


* NFA -> DFA
  * 子集构造算法


* DFA -> 词法分析器代码
  * Hopcroft算法

***************

## RE -> NFA

* 如何转化

  * 使用递归算法

  * 从复杂结构递归到原子结构就是最终的NFA


* 基本规则 P101

  * 规则简介

    *  -e-> 代表空串
    *  |s ---a--- t| 代表s是源，t是目的地，符号是a

  * 对于空串e

    ```bash
    start -e-> end
    ```

  * 对于 r=s | t

    ```bash
               status1 -s-> status3
    start -e->                      -e->status5 -e->end
    	       status2 -t-> status4
    ```

  * 对于r =st

    ```bash
    start -s-> status1 -t-> end
    ```

  * 对于r = s*

    ```bash
     |s--------------------e------------------------t|
    start -e-> status1 -s->status2 -e->status3 -e-> end
    			|t-----------e----------s|
    ```


* 注意
  * 两台自动机做与操作合并成一台自动机，一般是前面的终结状态通过空字符串跳转到后面的起始状态，不会直接省略后者自动机的起始状态直接开始连接

************

## NFA -> DFA

* 算法及特点

  * 子集构造算法

  * 是一个不动点算法，算法一定会终止


* 实现过程 P98

  * 首先计算起始状态start的闭包集，作为状态A

  * 从状态A开始，分别走a和b得到两个直接状态集合，在计算这两个直接状态集合的空字符闭包集合得到B和C

  * 从B和C开始再分别走a和b得到新的或者旧的空字符闭包集合状态

  * 重复第二三步，直到没有新的空字符串闭包集合状态产生为止

  * 包含NFA终止状态的集合状态就是DFA的终止状态

************

## x的空字符串闭包计算

* 计算方法

  * x本身是闭包集合的元素

  * 使用DFS或者BFS遍历空转移状态并加入集合

  * 最后得到x的空字符串闭包集

********************

## DFA最小化

* 算法
  * Hopcroft算法


* 实现过程

  * 首先把所有的状态切分为两个状态，接收集A和非接收集N

  * 对于任意一个字符，如果集合内所有元素的跳转状态都在同一个集合中(包括本身集合和**其他集合**)，那么这个元素不能区分这个集合。否则把跳转状态不同的一些元素提出来做区分

  * 重复第二步直到集合不在变化为止


* 注意

  * 只用考虑状态的出边而不用考虑状态的入边

  * 可以跳转到其他集合状态，且做划分的时候跳转到其他集合状态的所有元素要被看作新的同一集合元素

****************

## 语法分析

* 概述

  * 输入：记号流

  * 输出：语法树

  * 目的：判断输入是否符合某一门语言的语法规则

  * 体现：IDE控制台报错，比如无结尾分号，少括号

*************

## 上下文无关文法

* 组成

  * G=（T, N, P, S）

    > T：终结符
    >
    > N：非终结符
    >
    > P：产生式
    >
    > S：开始符


* 特点
  * 最后一定要推导到无非终结符为止


* 最左(右)推导
  * 概念：每次总是选择最左(右)侧的非终结符号来进行替换，不能从中间选择非终结符来推导


* 最后结果
  * 文法G拒绝或者接受一个记号流


* 分析树

  * 和推导的顺序无关

  * 树中每个内部结点代表非终结符

  * 树种叶子结点代表终结符

  * 分析树的含义取决于树的后序遍历(左右中父)顺序


* 二义性文法

  * 概念：一个句子有两棵或者以上的分析树

  * 解决方案：重写文法G


* 重写二义性文法

  * term -> factor -> atom

  * 一步一步地递归

****************

## First集

* 定义
  * First(N)表示由非终结符N能够推出所有的终结符集合


* 计算过程

  * 对于N -> a …

    > First(N) U= {a}

  * 对于N -> M …

    > First(N) U= First(M)

  * 对于空符号

    > 根据情况考虑


  * 一直重复第一第二第三步，直到没有任何集合改变为止


* 算法特点
  * 是一个不动点算法


* 多字符的First(ABC…)

  * 如果A是非终结符，First(ABC…)=First(A)

  * 如果A是终结符，First(ABC…)={A}


* 注意事项
  * 空符号也算终结符，因此要把它放到first集合里面


* LL(1)的冲突
  * 由非终结符N到终结符S分析表中有两条或者以上的产生式可以选择



## Follow集合

* 概念
  * Follow(N)指能够紧跟在非终结符N之后终结符集合


* 计算方法

  * $在Follow(S)中

  * 对 S --> Ab ，有 Follow(A) U= {b}

  * 对S -->AB，有 Follow(B) U= Follow(S)，Follow(A) U= First(B)

  * 空字符串分情况讨论


* 注意事项

  * 终止$符号一开始就要放在Follow(S)中

  * 空符号不能放在follow集合中

****

## 自顶向下分析算法

* 概念

  * 判断文法G是否接受句子s

  * 文法G从起始符号随意推导，如果推导出来就接受

  * 如果推导不出来就回溯换表达式继续推导


* 特点
  * 使用最左匹配规则


* 算法实现

  * 使用栈

  * 文法G的推导进行右侧优先压栈，如S -> ABC，压栈顺序为CBA。保证最左在栈顶


* 算法缺点

  * 需要回溯，时间复杂度太高

  * 编译器一般要求线性时间的算法

***************

## 递归下降分析算法

* 算法基本思想

  * 预测分析

  * 每个终结符构造一个分析函数

  * 用前看符号指导产生式规则的选择

****************

## LL(1)算法

* 概述

  * 从左向右读入，最左推导

  * 1个前看符号

  * 是一个基于表驱动的算法


* 表驱动的LL(1)架构

  * 由文法G生成一个分析表

  * 读入记号流使用分析栈基于分析表进行判断文法是否接受输入s


* 自顶向下算法缺陷
  * 它在确定选择那条产生式时根本不知道第一个字符是什么，就是盲选	


* 分析表的结构

  * 横轴是终结符，纵轴是非终结符

  * 相交坐标值表示从此非终结符要产生此终结符需要采用那一条产生式或者error

  * 从0开始对文法每条产生式标号


* 算法执行过程

  * 首先压入一个开始符S，读入第一个记号s

  * 根据分析表选择正确产生式，S出栈右逆序压入产生内容

  * 如果栈顶是终结符且与s相当，就出栈且读入下一个记号

  * 如果栈顶依然是非终结符就重复第二步

************

## 预测分析表构建

* 文法

  ```assembly
  E --> TE`          1
  E` --> +TE`        2
  E` --> e           3
  T --> FT`          4
  T` --> *FT`        5
  T` --> e           6
  F --> (E)          7
  F --> id           8
  ```

* First集和Follow集

  ```assembly
  First(E)={(, id}   Follow(E)={), $}
  First(E`)={+, e}   Follow(E`)={), $}
  First(T)={(, id}   Follow(T)={+, ), $}
  First(T`)={*, e}   Follow(T`)={+, ), $}
  First(F)={(, id}   Follow(F)={*, +, ), $}
  ```

* 预测分析表

  |      | id   | +    | *    | (    | )    | $    |
  | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
  | E    | 1    |      |      | 1    |      |      |
  | E`   |      | 2    |      |      | 3    | 3    |
  | T    | 4    |      |      | 4    |      |      |
  | T`   |      | 6    | 5    |      | 6    | 6    |
  | F    | 8    |      |      | 7    |      |      |

* 填入规则

  * 对于产生式 A --> Ba，将 First(A) 填入 A -> 终结符中，当A有多条产生式时根据究竟是由那条产生式得到终结符决定

  * 对于产生式A --> Ba ，如果 First(A) 含有 e，则将Follow(A) 填入 A -> 终结符中，就选产生空串那条产生式

***

## 一般条件下LL(1)的构造

* 特殊情况
  * First(ABC)如果A, AB, ABC能推导出空串，则First(ABC)会有所改变


* NULLABLE集合

  * 能够最终推导出空字符串的非终结符集合 

  * 计算方法仍然是不动点算法


* 最终的First集计算方法

  * 和上面的类似

  * 就是增加考虑一个NULLABLE集合

***

## LL(1)冲突分析处理

* 消除左递归 P135

  ```assembly
  # before modify
  A --> Ac | Aad | bd | e
  
  # after modify
  A --> bdA` | A`
  A` --> cA` | adA` | e
  
  # 注意
  时刻注意原A是否能推导出空串
  ```

* 提取左公因子 P136

  ```assembly
  # before modify
  S --> iEtS | iEtSeS | a
  E --> id
  
  # after modify
  S --> iEtSS` | a
  S` --> e | eS
  E --> id
  ```

  > 使用消除左递归和提取公因式可以将很大一部分有冲突的文法变成LL(1)文法

**************

## LR(0)算法

* LL(1)缺点

  * 能分析的文本类型有限

  * 往往需要改写文法


* 算法特点

  * 从左向右读入，最右推导，0个前看符号

  * 自底向上看是最右推导

  * 自顶向下看是从左边读入的归约


* 推导与归约

  * 推导：产生式从左侧到右侧

  * 归约：产生式从右侧到左侧


* 过程特点

  * 和递归下降对栈的操作刚好相反

  * 递归下降是把产生式的左部弹开，压入右部

  * LR(0)是把产生式的右部弹开，压入左部

***************

## LR算法名词介绍

* 点符号

  * 点的左边：已经读入的

  * 点的右边：剩余的输入


* 项

  * 一条产生式加上产生式中间的一个点

  * 项可以表示为一对整数，第一个是产生式编号，第二个点的位置


* 项集

  * 项的集合

  * 可以用数对的列表来表示


* DFA
  * 由项集加上跳转关系构成


* LR(0)

  * 最普通的LR算法，归约的时候啥都不管就归约。

  * 当一个项集同时有已读完的项和未读完的项就有 **移入规约** 冲突


* SLR
  * 本质是LR(0)，但是当且仅当待读入符号属于Follow(A)才归约


* LALR
  * 把SLR进行状态合并

*************

## LR(0)算法思想

* 首先要添加' S` -->S$ '这条产生式

* DFA构建 P155

  * 首先构建集合状态

  * 第一个集合状态是{S` -> .S$ , S -> .a… }，当输入为S或者a会发生状态转移，转移后如果点的右边是非终结字符，要把那个非终结字符的产生式加入进来

  * 一直重复第二步，直到构建完所有的集合状态


* 输入读入

  * 首先由一个状态栈，栈顶是0代表状态0。一个符号栈，栈底符号是$

  * 依次读入输入，如果是读入字符就压入字符和跳转的状态序号

  * 栈变成一个状态跳转的路径

  * 如果遇到归约，状态栈首先一步步弹出状态，在弹出后根据归约后的状态压入新的状态。符号栈同上

  * 重复上述步骤


* 状态表

  * 由状态自动机可以编码构成一张状态表，两者完全等架

  * 分为action表和goto表



## SLR语法分析表构造

* 文法

  ```assembly
  E` --> E     0
  E --> E+T    1
  E --> T      2
  T --> T*F    3
  T --> F      4
  F --> (E)    5 
  F --> id     6
  ```

* First集和Follow集

  ```assembly
  First(E)={(, id}  Follow(E)={), +, $}
  First(T)={(, id}  Follow(T)={), +, *, $}
  First(F)={(, id}  Follow(F)={), +, *, $}
  ```

* 状态表结构

  * 纵轴是DFA自动机的状态
  * 横轴 part 1是终结符 —— ACTION
  * 横轴 part 2是非终结符 —— GOTO

* 语法分析表

  |      | id   | +    | *    | (    | )    | $    | E    | T    | F    |
  | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
  | 0    | s5   |      |      | s4   |      |      | s1   | s2   | s3   |
  | 1    |      | s6   |      |      |      | acc  |      |      |      |
  | 2    |      | r2   | s7   |      | r2   | r2   |      |      |      |
  | 3    |      | r4   | r4   |      | r4   | r4   |      |      |      |
  | 4    | s5   |      |      | s4   |      |      |      |      |      |
  | 5    |      | r6   | r6   |      | r6   | r6   | 8    | 2    | 3    |
  | 6    | s5   |      |      | s4   |      |      |      | 9    | 3    |
  | 7    | s5   |      |      | s4   |      |      |      |      | 10   |
  | 8    |      | s6   |      |      | s11  |      |      |      |      |
  | 9    |      | r1   | s7   |      | r1   | r1   |      |      |      |
  | 10   |      | r3   | r3   |      | r3   | r3   |      |      |      |
  | 11   |      | r5   | r5   |      | r5   | r5   |      |      |      |

* 规则

  * 首先根据项集写出DFA
  * 对于某一个状态，如果有跳转到其他状态，就填写ACTION和GOTO表
  * 对于某一个含有项' A-->aB. ' 的状态，对Follow(A)中的元素进行归约处理
  * 注意Follow集里面有$也要进行归约

***

## SLR算法

* LR(0)的缺点
  * 可能过延误报错的时机，导致很久才报错
  * 归约无限制，会有很多冲突

* SLR算法思想
  * X -> a. 进行归约，当且仅当 y 属于 Follow(X)

***

## LR(1)算法

* SLR算法缺点

  * 虽然减少了移入归约冲突的数量但仍然存在移入归约冲突。冲突如下：

    ```assembly
    # Follow集
    Follow(T) = {a}
    
    # 项集
    T --> bB.
    R --> F + .a 
    
    # 冲突
    上述情况当待读入符号为a时会发生移入归约冲突	
    ```

* 算法特点

  * 用一个元祖表示
  * 元祖第二个属性为向前看期待的内容
  * 类似于SLR采取Follow集作为前看内容，但LR(1)元组的内容有可能比Follow集更小，更精确
  * 对于产生式相同，点位置相同，但前看符号不完全相同的状态被认为是不同的状态

* 注意事项

  * 同一条产生式点儿位置不同算两个项

    ```java
    E -> E+.E  //1
    E -> .E+E  //2
    E -> .n    //3
    /*1和2算两个项*/
    ```


*************

## LR(1) 项集闭包计算过程

* 文法

  ```java
  S` --> S$
  S --> L=R
  S --> R
  L --> *R
  L --> id
  R --> L
  ```

* so项集合

  ```java
  S` --> .S   ,{$}    //初始只有这一句，$为读入S后的前看符号
  S --> .L=R  ,{$}    //S后面为空，因此前看符号为First($)
  S --> .R    ,{$}    //S后面为空，因此前看符号为First($)
  L --> .*R   ,{=,$}  //L后面是 '=R',因此前看符号为First(=R$)
  L --> .id   ,{=,$}  //L后面是 '=R',因此前看符号为First(=R$)
  R --> .L    ,{$}   /*R后面为空，因此前看符号为First($)
  					且又有L，把L重新产生，就会有$符号*/
  ```

* 计算规则

  * 对于 'S` --> S' 。把 $ 添加入前看符号
  * 对于 'A --> a.Bc  , {d, E, F}'。'B --> … , {}' 前看内容为First(Bcd) U First(BcE) U First(BcF)

***

## LALR算法

* 算法特点
  * 就是把LR(1)算法中产生式相同，但前看符号不同的状态进行合并
  * 合并后就是LALR

* 注意事项
  * 合并不一定好
  * 合并有可能交叉导致移入归约冲突

***

## 优先性和结合性消除LR冲突

* 项集如下

  ```java
  E --> E+E.
  E --> E.+E
  E --> E.*E
  ```

* 结合性

  * 如果规定是左结合，则应该进行归约，因为左边已经完成一则运算

* 优先性

  * 规定右结合，乘法优先，输入为乘号
  * 进行移入操作

***

## 语法分析总结

* 手工方式
  * 递推下降分析

* 语法分析器生成
  * LL(1)
  * LR(1)

* LR系列算法递进
  * LR(0) --> SLR --> LR(1) --> LALR

**********************

## YACC

* 简介
  * 一个生成编译器的编译器

* compiler - compiler构想
  * 人们一直想构造一个这样的编译器，规定一门语言语法就能自动生成这个语言的编译器
  * 但这个宏大的愿景没有实现
  * 但具体细分领域如词法分析，语法分析已经实现了上述目标
  * YACC就是实现上述目的的工具

* Bison
  * 由YACC为基础改进而来

***

## yacc语言

* 语言规则

  ```assembly
  用户代码和yacc声明：可以在接下来使用的部分
  
  %%
  
  语法规则：CFG和相应的语义动作
  
  %%
  
  用户代码：用户提供的代码 

*******************

## 四则运算bison实例

* 代码

  ```c
  //第一部分为用户代码和yacc声明
  %{
  #include<stdio.h>
  #include<stdlib.h>
  int yylex();
  void yyerror();
  %}//此部分为用户C代码的声明
  
  %left '+' //这是bison的CFG声明，对于 '+' 采用左结合
  
  %% //隔离第一和第二部分
  
  //第二部分写CFG和语义动作    
  lines: line lines | line;
  line: exp '\n';  //实现很多行
  exp: n | exp op exp | '('exp')'; //字符要写成ASCII码才行 
  op: '+' | '-' | '*' | '/';
  num: '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' | '0';
  n: '1'n | '2'n | '3'n | '4'n | '5'n | '6'n | '7'n | '8'n | '9'n | '0'n | num;
  
  %% //隔离第二和第三部分
  
  //用户的代码，此处为C代码
  int yylex(){ return getchar(); } //必须实现bison文档的yylex()函数
  void yyerror(char *s){ 
  fprintf(stderr, "%s\n",s);
  return;
  } //必须实现bison文档的yyerror()函数
  int main(int argc, char **argv){
  yyparse(); //调用词法解析函数
  return 0;
  }
  ```

* shell执行过程

  * 首先编写一个 '.y' 的文本文件，因为yacc默认结尾是 '.y'

    ```shell
    vim calculator.y
    ```

  * 编写完成后使用bison编译器进行编译，会生成一个 'calculator.tab.c' 的c代码文件。此c代码就是自动生成的语法分析器。

    ```shell
    bison caculator.y
    ```

  * 使用gcc编译 'calculator.tab.c' 文件，生成可执行程序 calculator.out

    ```shell
    gcc calculator.tab.c -o calculator.out
    ```

  * 执行程序

    ```shell
    ./calculator.out
    ```


***

## 语法制导翻译

* LR中的语法制导翻译
  * 在归约的时候进行，归约是关键
  * 遍历顺序是后序遍历，先遍历孩子结点算出值在遍历父亲结点

* 基本思想
  * 给每条产生式附加一个语义动作，一般是一条代码片段
  * 语义动作在归约的时候执行  

************

## Bison语法制导翻译

* 文法+执行语句

  ```java
  lines: line lines
  | line;
  
  line: exp '\n' {printf("value=%d\n",$1);};
  
  exp: n {$$=$1;}
  | exp '+' exp {$$=$1 + $3;}
  | exp '-' exp {$$=$1 - $3;}
  | exp '*' exp {$$=$1 * $3;}
  | exp '/' exp {$$=$1 / $3;}
  | '('exp')' {$$ = ($2);};
  
  num: '1' {$$=1;}
  | '2' {$$=2;}
  | '3' {$$=3;}
  | '4' {$$=4;} 
  | '5' {$$=5;} 
  | '6' {$$=6;} 
  | '7' {$$=7;} 
  | '8' {$$=8;} 
  | '9' {$$=9;} 
  | '0' {$$=0;};
  n: '1'n {$$=10+$2;}
  | '2'n {$$=20+$2;}
  | '3'n {$$=30+$2;}
  | '4'n {$$=40+$2;}
  | '5'n {$$=50+$2;}
  | '6'n {$$=60+$2;}
  | '7'n {$$=70+$2;}
  | '8'n {$$=80+$2;}
  | '9'n {$$=90+$2;}
  | '0'n {$$=$2;}
  | num {$$=$1;};
  ```

* 代码简介

  *  '$$'指产生式左边符号
  *  '$2'指产生式右边第二个符号

* 分析

  * 在每一条产生式后面加上一条执行代码语句，在归约时执行就是翻译

* 执行过程

  * 其实就是在之前LR算法的归约前面执行产生式的代码片段
  * 在分析栈上维护三元组<symbol, value, state>。相比之前就是多了一个value属性用于保存结果

***

## 抽象语法树

* 语法树简介
  * 抽象语法树是语法分析的输出结果，是前端的输出结果。也是后端的输入

* 过程简介
  * 字符流(文本文件) -> 词法分析 -> 记号流 -> 词法分析 -> 抽象语法树

***

## 分析树

* 分析树简介定义

  ```java
  // 15 * ( 3 + 4) 的分析树
  E
  ---------------    
  |      |      |
  E      *      E
  |       -------------      
  |       |     |     |
  15      (     E     )
    -------
    |  |  |       
    E  +  E
    |     |    
    3     4
  ```

* 注意事项

  * 对于表达式而言，编译只关心 **运算符** 和 **操作数** 。因为优先性结合性已经在语法分析的时候处理掉了
  * 对 **语句、函数** 也是一样。不关心赋值号到底是 '=' 还是 '->' 只关心要进行赋值这一个操作。
  * 语法树已经包含了很多内容，我们不必在语法分析结束后进行树的数据结构存储时还保存不必要的符号及其他信息

* 分析树的优缺点

  * 分析树编码了句子的推导过程，清晰直观
  * 但有些结点占用了额外的存储空间，是不必要的

* 分析树到抽象树

  * 分析树含有太多的无关结点，进行省略删除浓缩可以得到抽象语法树

* 抽象语法树数据结构示例

  ```java
     *
  15    + 
     3     4
  ```

  * 使用不同的语言就要用不同的语言定义抽象语法树的 **数据结构**

* 具体语法与抽象语法

  * 具体语法是语法分析器使用的语法，包含去出分隔符，消除左递归，提取左公因子等等
  * 抽象语法是用来表达语法内部的结构，现代编译器前端后端的接口一般都采用抽象语法树

***

## 抽象语法树C语言定义

* 文法

  ```java
  E -> n
  | E + E
  | E * E
  ```

* C 数据结构定义

  ```c
  //定义三条产生式枚举类型
  enum Kind {E_INT, E_Add, E_TIMES}
  
  struct Exp{
  enum Kind kind;
  }
  
  //整数产生式
  struct Exp_Int{
  enum Kind kind;
  int n;
  }
  
  //加法产生式
  struct Exp_Add{
  enum Kind kind;
  struct Exp *left;
  struct Exp *right;
  }
  
  //乘法产生式
  struct Exp_Times{
  enum Kind kind;
  struct Exp *left;
  struct Exp *right;
  }
  ```

* 构造函数定义

  ```c
  struct Exp_Int *Exp_Int_New(int n);
  struct Exp_Add *Exp_Add_New(struct Exp* left, struct Exp *right);
  struct Exp_Add *Exp_Times_New(struct Exp* left, struct Exp *right);
  ```


***

## LR自动生成抽象语法树

* 原理简介

  * 在产生式的语法动作中插入生成抽象语法树的代码。此代码一般是语法树的构造函数
  * 在产生式归约时，会自底向上构造语法树

* 示例

  ```c
  E -> n {$$ = Exp_Int_New($1);}
     | E + E {$$ = Exp_Add_New($1, $3);}
     | E * E {$$ = Exp_Times_New($1, $3);};
  ```


***

## 源代码信息保留和传播

* 抽象语法树是前后端的接口
* 程序一旦转换为抽象语法树，源代码就被丢弃，后续阶段只处理抽象语法树
* 抽象语法树数据结构必须编码足够多的源代码信息，如行号，文件等
* 抽象语法树必须仔细设计

****

## 位置信息示例

```c
struct positin_t{
char *file;
int column;
int row;
}

struct Exp_Add{
struct Kind kind;
struct Exp *left;
struct Exp *right;
//新增position属性
struct position_t *from;
struct position_t *to;
}
```

***

## 语义分析

* 语义分析定义
  * 语义分析是指在语法分析完成的基础上，对语言的合法性做更精确的检查
  * 如对于 '+' 运算符，语法分析只取两边的子树构成一棵语法分析树
  * 而语义分析还要判断两边是否都是整数类型，要求只有两边都是整数类型才合法。

* 语义分析任务
  * 语义分析也被称为类型检查、上下文相关分析
  * 负责检查抽象语法树的上下文相关属性
    * 变量在使用前进行声明
    * 每个表达式都有合适的类型
    * 函数调用与函数定义一致
    * ……

* 输入与输出
  * 输入是抽象语法树
  * 输出是中间代码

***

## 程序语言的语义

* 表达语义
  * 大多数程序设计语言采用 **自然语言** 来表达程序的语义。就比如编程书籍上对操作符的解释

* 编译器实现者理解语义
  * 编译器实现者要对程序语义有完整清晰地认识

***************************

## C--语言

* 文法

  ```java
  E ->  n
  | true
  | flase
  | E + E
  | E && E
  ```

* 类型检查

  * ' 3 + 3'  合法
  * ' true && 3' 非法

* 类型检查函数

  ```c
  Type check_type(Expression e) //判断表达式e是否类型合法
  ```

  * 实现过程：递归判断

***

## 变量声明

* 文法

  ```java
  P ->  D E
  D -> T id; D
  |
  T ->  int
  | boolean
  E ->  n
  | id
  | true
  | flase
  | E + E
  | E && E
  ```

* 类型检查举例

  * ' int x; x+5' 合法

* 类型检查算法

  * 设置一张全局的符号表
  * 递归调用进行判断

***

## 符号表

* 作用
  * 存储程序中变量相关的信息

* 相关信息
  * 类型、作用域、访问控制信息等

* 特点
  * 符号表的处理必须非常非常高效

* 符号表高效实现
  * 哈希：O(1)时间，但空间浪费大
  * 红黑树: O(log n)，比较节约空间

## Clang

> copy from `man clang` results

* it is a driver
  * The clang executable is actually a small driver which controls the overall execution of other tools such as the compiler, assembler and linker.
* stages
  * `clang -E source.c -o result.i`
    * preprocess
  * `clang -S source.c -o result.s`
    * parsing
    * semantic analysis
    * code gen - translates an AST into low-level intermediate code (known as "LLVM IR")
    * optimazation 
  * `clang -c source.c -o result.o`
    * assemble
  * `clang source.c -o source.out`
    * link
* Driver Options
  * `-Wa,<args>` - Pass the comma separated arguments in args to the assembler.
  * `-Wl,<args>` - Pass the comma separated arguments in args to the linker.
  * `-Wp,<args>` - Pass the comma separated arguments in args to the preprocessor.
  * `-Xassembler <arg>` - Pass arg to the assembler.
  * `-Xlinker <arg>` - Pass arg to the linker.
* Preprocessor Options
  * `-D<macroname>=<value>` - Adds an implicit #define into the predefines buffer which is read before the source file is preprocessed.
  * `-include <filename>` - Adds an implicit #include into the predefines buffer which is read before the source file is preprocessed.
  * `-I<directory>` - Add the specified directory to the search path for include files.
  * `-F<directory>` - Add the specified directory to the search path for framework include files.
