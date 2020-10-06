# linux第一次作业说明文档

## 环境

* busybox(且已配置环境变量)
* g++(用于编译.cpp)文件
* 其他基本环境

## 运行步骤

* 首先使用g++编译 **call_busybox.cpp**文件

> g++ call_busybox.cpp -o output

* 修改运行权限

> chmod +x output

* bash执行

> ./output

* 输入指令，比如以下

> ps -ef
>
> ls -l
>
> cat call_busybox.cpp
>
> ……

## 其他事项

* 某些不符合规定指令可能会导致程序故障卡死，请ctrl +c重启进入

