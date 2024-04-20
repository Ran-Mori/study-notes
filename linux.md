# Linux学习

****************

## Linux学习方向

* 运维工程师：管理和维护成千上万台服务器
* 嵌入式工程师：需求较小，一般要会C语言
* 用得最多，java EE、Python、C++、大数据等等

******************

## Linux应用领域

* 个人领域：一直被Windows压着
* 服务器领域：最强，没有之一
* 嵌入式系统：因为Linux内核可改可变小，适合嵌入式

*****************

## 虚拟机网络模式

* 桥接模式：Linux可以和外部主机通信，但要占用一个局域网地址
* NAT模式：主机之外有一个局域网，Linux不占用本身的局域网
* 主机模式：是一个独立的主机，不能访问外网

*****************

## Linux系统分区

文章介绍 -> [The Linux Directory Structure, Explained](https://www.howtogeek.com/117435/htg-explains-the-linux-directory-structure-explained/)

* `/`  根分区
* `/bin`  存放核心指令
* `/sbin`  超级管理员存放指令的目录
* `/swap`  虚拟内存，一般为真实内存的1.5倍
* `/boot`  启动linux使用的一些中心文件，包括内核文件、启动菜单配置文件等
* `/etc`  系统主要的配置文件，例如人员的账号密码文件、各种服务的起始文件，防火墙
* `/home`  所有普通用户默认的工作目录
* `/media`   可移除的媒体设备，比如插入CD时，会自动生成一个子文件夹
* `/lib`   存放`/bin`和`/sbin`下命令需要的动态链接库
* `/root`  root用户的home目录
* `/usr`  Unix Software Resource  安装软件的目录
* `/proc`   存储的是当前内核运行状态的一系列特殊文件，用户可以通过这些文件查看有关系统硬件及当前正在运行进程的信息，甚至可以通过更改其中某些文件来改变内核的运行状态。
* `/mnt`  作为挂载点使用。通常包括系统引导后被挂载的文件系统的挂载点，如挂载Windows下的某个分区
* `/opt`  可选的package
* `/var`  存放系统中经常需要变化的一些文件
* **其他变色有个箭头指向一个文件夹的是软连接**，不是一个真正文件夹

****************

## 关机重启指令

* `reboot`  重启
* `shutdown -h now`   立刻关机

*****************

## vim编辑器

- 三个模式
  - 正常模式：执行vim后进入的模式，在此模式可以使用快捷键
  - 插入模式：正常模式下按`i`后进入的模式
  - 命令行模式：正常模式下按`:`进入的模式
- 保存命令
  - `wq`  保存退出
  - `q`  不保存退出
  - `q!`  强制退出 
- 操作指令
  - 复制    **3yy**表示复制从此时光标向下的三行，**p**表示粘贴复制内容到当前光标处。剪贴板是系统级的
  - 删除    **3dd**，逻辑同上
  - 查找    命令行模式下**/hello**代表搜索hello关键字
  - 替换    `:%s/fromWord/toWord/g` 将全局的`fromWord`替换为`toWord` 
  - 查找下一个 正常模式**n**
  - 查找上一个 正常模式**N**
  - 顶行    正常模式下**gg**
  - 底行    正常模式**G**
  - 上一页 正常模式**ctrl + f**即forward
  - 下一页 正常模式**ctrl + b**即backward
  - 撤销    正常模式**u**

****************

## 用户管理

* 添加用户   **useradd  IzumiSakai**
* 设置用户密码   **passwd IzumiSakai**
* 切换用户   **su IzumiSakai**
* 删除用户   **userdel IzumiSakai** 或者**userdel -r IzumiSakai**。区别是后面的会删除home下对应的用户目录
* 查看用户信息  **id IzumiSakai**
* 查看所有用户信息   **vim /etc/passwd**

****************

## 用户组管理

* 添加组   **groupadd  IzumiSakaiFan**
* 删除组   **groupdel  IzumiSakaiFan**
* 添加用户到指定组   **groupadd  -g IzumiSakaiFan  IzumiSakai**
* 修改用户的用户组   **groupmod  -g  IzumiSakaiFan  IzumiSakai**、

***************************

## 常用指令

* 查看帮助    **man ls**或者**help  ls**
* 显示绝对路径  **pwd**
* 显示包括隐藏的目录  **ls  -al**
* 创建文件夹   **mkdir**   当加上**-p**参数可以创建多级目录
* 创建文件  **touch   test.json**
* 复制文件  **cp**  带上**-r**可以递归复制文件夹
* 删除文件和文件夹   **rm -rf**  其中**-f**表示屏蔽显示
* 移动文件  **mv**
* 只读查看文件  **cat**

******************

## 进阶指令

* 动态显示大文本文件 **less**  并非一次加载完毕
* 输出重定向 **ls  -al  >  test.json **  会覆盖原本内容
* 追加   **cat  hello.txt  >>  test.json**  只是追加不会覆盖
* 查看环境变量 **echo  $PATH**
* 追加指定内容  **echo  "addedContent"  >>  test.json**
* 查看命令行历史记录  **history**
* 查找内容  
  * **find  /home  -name  hello.txt**
  * **find  /  -user  IzumiSakai**
  * **find  /  -size  +20M**
  * **find  /  -name  ***google*****   相当于模糊查询
* 查找 **grep**指令  **grep  "root"  test.json**  在test.json中查找root关键字
* 解压  **tar  -zxvf  jdk1.8.tar.gz  -C  /usr/local**  解压到指定目录。其中 **-C**参数必须大写。如果拓展名只有 **tar**不是 **tar.gz**就不用加 **z**
* 查看进程 **ps  -ef  |  grep  java **  ps是process status的缩写
* 查看所有进程 **ps  -aux**
* 强制停止进程 **kill  -9  2442**

******************

## 文件夹信息结构

* 基本结构
  * 文件类型(1) - 用户权限(3) - 组权限(3) - 其他组权限(3) - 底下目录数目 - 所有者 - 所有组 - 文件大小
* 详细信息
  * 文件类型 **d**为文件夹
  * 权限为rwx三个，分别代表写，读，可执行(可打开)  
    * rwx=7
  * 文件大小如果是文件夹则默认4096

*****************

## 授权

* 改变文件的拥有者 **chown  IzumiSakai  test.json**   加上**-R**参数表示递归文件夹
*  改变文件的组别  **chgrp  IzumiSakai  test.json**   加上**-R**参数表示递归文件夹
* 改变所有者文件权限  **chmod  u [+, -]  [r, w, x ]  test,json**   加上**-R**参数表示递归文件夹
* 改变所属组的权限  **chmod  g [+, -]  [r, w, x ]  test,json**   加上**-R**参数表示递归文件夹
* 改变其他组的权限  **chmod  o [+, -]  [r, w, x ]  test,json **  加上**-R**参数表示递归文件夹
* 简写 **chmod -R  753  IzumiSakai**

*****************

## 软件安装

* 主流三种方式
  * **tar**  解压安装
  * **rpm**   直接安装
    * `rpm -ivh --prefix=/usr/local/java jdk1.8.rpm`  指定安装目录
  * **yum**  在线安装
* 注意事项
  * linux系统下的可执行文件是 **.sh** 文件，在目录下执行 **./startup.sh**就可以执行这个shell脚本可执行文件

******************

## rpm安装MySQL

* 查看rpm已经安装的软件 **rpm  -qa  |  grep  mariadb**  q表示查询，a表示所用
* 删除已经安装的应用 **rpm  -e  mariadb-libs-5.5.60-1.el7_5.x86_64  --nodeps**。其中 **--nodeps**表示深度卸载
* 用yum安装依赖 **yum install libaio**
* 解压压缩包 **tar  -xvf  mysql.8.0.19.tar**  。只以 **tar** 结尾的就不用 **-z**命令
* 安装 **rpm  -ivh  mysql -community-server.rpm**  之间有依赖关系，先安装那个不一定
* 初始化  **mysqld --initialize**
* 打开mysql服务 **service  mysqld  start**
* 查看mysql服务状态 **service  mysqld  status**
* 后续步骤
  * 查看初始密码
  * 登录mysql
  * 改复杂密码
  * 设置密码等级为LOW
  * 改简单密码
  * 修改密码为永不过期
  * 设置可以远程访问
  * 安全组打开3306端口
  * 远程登录使用

***

## Linux原理及应用上课

* 开源软件协议

  * Apache

  * BSD

  * LGPL
* Linux和Linux发行版的区别

  * Linux：几百万行代码编译成几百k的 **linux-xxx.tar.gz**的文件

  * Linux发行版：Linux的Kernel和一些其他GNU或其他Free形式分发的应用程序结合起来的发行版

***

## 进程状态

* 可执行状态
  * 包括正在执行中或者ready能够执行
  * 对应的`tast_strcut`会被放进内核的可执行队列中，由内核调度执行
  * 其他操作系统有`running`和`ready`之说，但`linux`统称为可执行状态
* 可中断的睡眠状态
  * 通常是在等待某某事件发生(比如socket连接，信号量等)
  * 对应的`task_strcut`被放进内核的等待队列中。当这些事件发生时(外部中断触或其他进程触发等)，进程被唤醒
  * 一个操作系统中的绝大部分进程都处于这个状态
* 不可中断睡眠状态
  * 不可中断并不是`cpu`不响应外部硬件中断，而是指这个进程不响应异步信号量
  * 这种情况下使用信号量如`kill -9`杀不死进程
  * 这种状态存在的意义是内核某些处理是不能被打断的，比如`read`内核通过设备驱动操作硬件，这个时候去响应信号处理会有大问题
  * 因此这种状态大部分作用是用来保护进程不被打断。通常持续事件很短，一般使用`ps -ef`都看不到这种状态的进程
* 暂停状态
  * 向进程发送`SIGSTOP`信号，一个进程就会进入暂停态
  * 向暂停进程发送`kill -18`即`SIGCONT`信号会恢复
* 僵尸状态
  * 进程退出时，进程占有的资源都会被回收。但`task_strcut`及其他少数资源不会回收，因为`task_strcut`保存了很多信息，而这个信息是父进程关心的
  * 父进程一般使用`wait`系列调用或许存储于`task_struct`中它关心的信息，然后`wait`系列系统调用会自动清除子进程所有资源
  * 两种情况
    * 僵尸状态
      * 概述：父进程回收出现问题
      * 子进程退出后父进程没有使用`wait`系列函数获取子进程信息并将子进程释放
      * 子进程退出时，内核会向父进程发送一个信号来让父进程回收。但父进程由于信号太多或者回收异常导致没有回收，此时就会有僵尸进程
    * 孤儿进程
      * 父进程比子进程先退出

***

## SIGCHLD信号

* 性质
  * 是LInux进程信号通信的一种信号
* 产生条件
  * 子进程终止时。向父进程发送此信号，但父进程的默认处理是忽略此信号
  * 子进程收到`SIGSTOP`信号停止时
  * 子进程在停止状态，收到`SIGCONT`信号被唤醒时
  * 综上所述，子进程状态改变就会给父进程发送这个信号，让父进程知道自己的状态

***

## IPC机制

* 管道
  * 实质
    * 操作系统内核管理的一个缓冲区(一段内存)
  * 特点
    * 半双工。如果想数据双向传输，就需要打开两个管道
    * 容量有限制，可能会读阻塞或写阻塞
    * 消息需要在内核内存和用户内存间复制**两**次
  * 分类
    * 匿名管道
      * 几乎只能用于`Linux`的父子进程，即配合`fork`使用
      * 即父进程创建一个管道，通过`fork`创建出一个子进程。由于父子进程的`文件符表`共享，即父子进程都知道`pipe_fd`。此时父进程关闭管道的`读端`，子进程关闭管道的`写端`，这样父子进程就能实现`IPC`了
    * 实名管道
      * 实名管道的存在就是为了解决匿名管道的不足，匿名管道几乎就只能用在`fork`的父子进程共享这种情况，因为`pipe_fd`是未知的。即只要让这个管道有个可以访问的方式，就能够实现任意进程的`IPC`了，这就是实名管道
  * how it works?
    1. When a sender writes data to a pipe, the kernel copies the data from the sender's address space to the shared-memory buffer.
    2. When a receiver reads data from the pipe, the kernel copies the data from the shared-memory buffer to the receiver's address space.
* 信号
  * 定义
    * 这里的信号量指的是异常控制流的那个信号量，而不是同步访问临界区的那个信号量
  * 使用
    * 具体使用方式可以参考`CSAPP.md`的`ECF`异常控制流章节
    * 即一个进程通过操作系统内核向自己或者其他进程发送信号量。另一个进程接收到信号量后，会直接跳转到预先设置好的异常处理程序进行信号量处理，即`ECF`跳转。一般可以通过`signal()`函数定义一个进程对信号量的响应方式
* 信号量
  * 定义
    * 这就是那个`PV`操作的信号量
  * 实质
    * 是在内核中的一个共享变量，用于控制同步访问临界区，常常用作互斥锁的实现
* 消息队列
  * 实质
    * 内核中的一个链表数据结构
  * 特点
    * 消息需要在内核内存和用户内存间复制两次
    * 消息队列可以有多个，而不是整个操作系统内核只维护一个消息队列，所有的进程都使用这一个消息队列
    * 消息队列是独立于进程的存在，它的生命周期与进程生命周期无关
    * 消息队列中的消息可以设置一个标志符，来标识这个消息的类型及一些其他信息
  * 相比于命名管道的优势
    * 独立进程而存在
    * 不需要进程自己来提供同步方法
    * 接收程序可以通过`消息类型`有选择的接收数据，而不是像命名管道一样全盘接收
* 共享内存
  * 特点
    * 无同步机制，需要程序员自己控制同步
    * 消息需要在内核内存和用户内存间复制1次
  * 过程
    * 创建共享内存 —— `shmget()`
    * 映射到进程A —— `shmat()`
    * 映射到进程B —— `shmat()`
    * 进程间通信 
    * 撤销内存映射区 ——`shmdt()`
    * 释放共享内存 —— `shctl()`
* 内存映射
  * 和共享内存有点类似，但它是映射一个本地磁盘文件，效率比较低下
  * 消息需要在用户内存和本次磁盘间复制两次
* socket
  * 常用于网络中不同主机的两个进程间通信，效率相比于上面的方式较低
* RPC
  * 客户端调用`Stub`接口
  * `Stub`根据操作系统的要求进行打包，并执行相应的系统调用
  * 由内核来完成与服务端的具体交互，它负责将客户端的数据包发送给服务端的内核
  * 服务端`Stub`解包并调用与数据包匹配的进程
  * 服务器以上述步骤的逆向过程将结果返回给客户端

***

## 同步机制

* 信号量
  * 就是`PV`操作
* Mutex
  * 就是信号量为1的`PV`操作
* 管程
  * 出现原因
    * 使用`PV`看起来很OK。但类比`C++`中的`new/delete`，在大型应用程序中复杂度高，而且是由多人合作完成，因此难免会出问题
    * 鉴于上述原因，有了管程。管程可以看做是对信号量的又一层封装
  * 定义
    * 是可以被多个进程/线程安全访问的对象或者模块
  * 特点
    * 管程中的方法都是受`mutual exclusion`保护的，同一个时刻只允许一个访问者使用它
  * 性质
    * 安全性，互斥性，共享性

***

## epoll

* 详情请看blog
* 名词解释
  * IO指的是文件(套接字)读写
  * 多路指的是网络连接
  * 复用指的是只有一个线程

***

## 驱动与内核

* 驱动开发也是内核开发，只有应用层和内核层，没有驱动层这种说法
* 驱动代码的体积占整个内核的比重十分大
* 内核支持动态卸载和装载驱动模块

***

## 抢占式与非抢占式

* 抢占式
  * 资源(CPU周期)是按照时间片分配给每一个进程
  * 高优先度的进程可以在低优先度进程正在执行且时间片还未用完时，就强行获取CPU周期直接执行，此时低优先级进程进入等待状态
* 非抢占式
  * 资源(CPU周期)整个分配给进程，即不按时间片来分配
  * 正在执行的进程直到执行结束或退出才释放CPU资源，且其不能被中断
  * 基于此，多用户操作系统必须是抢占式操作系统

***

## 内核体系结构

* 宏内核
  * Linux操作系统是使用的宏内核
* 微内核
  * 内核只需要有一个很小的函数集
    * 同步原语
    * 一个简单的调度程序
    * 进程间通信机制
    * ……
  * 运行在微内核之上的几个系统**进程**实现之前操作系统的功能
    * 内存分配
    * 设备驱动
    * 系统调用
* 微内核优缺点
  * 缺点
    * 操作系统不同层次调用有开销
  * 优点
    * 操作系统的每个层都是一个独立的程序，上下交互的接口已经定义好了。因此非常容易移植，且硬件相关的代码已经封装进了程序内部，对外暴露的是简洁的接口
    * 更能充分利用内存，因为不需要的系统进程可以不加载
* 模块
  * 出现原因
    * 为了达到微内核的优势，但又不想影响性能
  * 原理
    * 模块是一个目标文件，可以在运行时动态链接到内核或者从内核解除链接
  * 功能举例
    * 文件系统
    * 驱动程序
    * ……
  * 实质
    * 模块不是像微内核一样作为一个单独的进程来运行。它与其他任何静态函数一样，它代表当前的进程在内核态下执行
  * 优点
    * 清晰定义的接口，优秀的移植性
    * 节省内存使用
    * 无性能损失，因为链接和就和普通的内核代码一样了

***

## dlopen

### what

* it is not a system call.
* It's a function provided by the C library, specifically from the `libdl` library.

### signature

``` c
#include <dlfcn.h>

void *dlopen(const char *filename, int flags);
int dlclose(void *handle);
```

### how it works

1. `dlopen` uses internal system calls(`open`) to open the library file on disk.
2. It then uses system calls like `mmap` to map the library's contents into the memory space of your program.
3. `dlopen` resolves symbols (function names) within the library and returns a handle to the loaded library.

### example

```c
#include <stdio.h>
#include <dlfcn.h>

// Function prototype to match the expected function from the library
int add(int a, int b);

int main() {
  void* handle;
  char* error;

  // Path to the library (replace "mylib.so" with your actual library filename)
  const char* lib_path = "mylib.so";

  // Load the dynamic library
  handle = dlopen(lib_path, RTLD_LAZY);
  if (!handle) {
    error = dlerror();
    fprintf(stderr, "Error loading library: %s\n", error);
    return 1;
  }

  // Get the function pointer using dlsym
  add = (int(*)(int, int))dlsym(handle, "add");
  if (!add) {
    error = dlerror();
    fprintf(stderr, "Error getting function pointer: %s\n", error);
    dlclose(handle); // Close the library even on error
    return 1;
  }

  // Call the function from the loaded library
  int result = add(5, 3);
  printf("Result: %d\n", result);

  // Close the library when finished
  dlclose(handle);

  return 0;
}
```

