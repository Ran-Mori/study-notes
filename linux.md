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

* "/"  根分区
* "/bin"  存放核心指令
* "/sbin"  超级管理员存放指令的目录
* "/swap"  虚拟内存，一般为真实内存的1.5倍
* "/boot"  启动linux使用的一些中心文件，包括内核文件、启动菜单配置文件等
* "/etc"  系统主要的配置文件，例如人员的账号密码文件、各种服务的起始文件，防火墙
* "/home"  所有普通用户默认的工作目录
* "/media"   设备目录，比如U盘
* "/lib"   存放系统最根本的动态链接库
* "/root"  root用户的home目录
* "/usr"  Unix Software Resource  安装软件的目录
* "/proc"   存储的是当前内核运行状态的一系列特殊文件，用户可以通过这些文件查看有关系统硬件及当前正在运行进程的信息，甚至可以通过更改其中某些文件来改变内核的运行状态。
* "/mnt"  作为挂载点使用。通常包括系统引导后被挂载的文件系统的挂载点。如挂载Windows下的某个分区
* "/opt"   第三方软件安装的地方
* "/var"  存放系统中经常需要变化的一些文件
* **其他变色有个箭头指向一个文件夹的是软连接**，不是一个真正文件夹



****************

## 关机重启指令

* "reboot"  重启
* "shutdown -h now"   立刻关机

*****************

## vim编辑器

- 三个模式
  - 正常模式：执行vim后进入的模式，在此模式可以使用快捷键
  - 插入模式：正常模式下按"i"后进入的模式
  - 命令行模式：正常模式下按":"进入的模式
- 保存命令
  - "wq"  保存退出
  - "q"  不保存退出
  - "q!"  强制退出 
- 操作指令(**下面操作注意是在正常模式下还是在命令行模式**)
  - 复制    **3yy**表示复制从此时光标向下的三行，**p**表示粘贴复制内容到当前光标处。剪贴板是系统级的
  - 删除    **3dd**，逻辑同上
  - 查找    命令行模式下**/hello**代表搜索hello关键字
  - 顶行    正常模式下**gg**
  - 底行    正常模式**G**
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
* 输出重定向 **ls  /  -l  >  test.json **  会覆盖原本内容
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























# Linux原理及应用

## 1 第一节课

### 1.1  开源软件协议

* Apache
* BSD
* LGPL

### 1.2 Linux和Linux发行版的区别

* Linux：几百万行代码编译成几百k的 **linux-xxx.tar.gz**的文件
* Linux发行版：Linux的Kernel和一些其他GNU或其他Free形式分发的应用程序结合起来的发行版