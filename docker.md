# Docker学习

*****************



## docker前置

* 核心
  * 项目绑定上 **环境**
  * 类似集装箱相互隔离，互相不影响
* 容器
  * 容器化技术不是模拟一个完整的操作系统
  * 容器没有自己的内核，也没有虚拟的硬件
  * 每个容器互相隔离，有自己的文件系统

***************



## docker基本名词

* 镜像： 一个模板，容器是由镜像创建的
* 容器： 镜像创建出容器
* 仓库： 存放镜像的地方。用国内的不然太慢



***********************

## docker安装与加速

* 安装

  ```bash
  https://www.cnblogs.com/walker-lin/p/11214127.html
  ```

* 加速

  ```bash
  https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
  ```

  

********************

## 工作原理

* docker是怎样工作的

> docker是一个client-server的结构系统，docker守护进程运行在主机上。通过socket从客户端访问
>
> server接收到client的命令后就执行命令
>
> docker的容器之间是相互隔离的，容器端口和linux本机的端口没有关系

************



## docker常用命令

* 帮助命令

  ```bash
  docker version  # 查看版本
  docker info  # 查看信息，包括镜像数量和各种状态容器的数量
  docker 命令 --help # 查看帮助
  ```

* 镜像命令

  ```bash
  docker images # 查看所用镜像
  docker search 镜像名 # 搜索镜像
  docker pull 镜像名 # 下载镜像
  docker rmi 镜像名/镜像ID # 删除镜像，-f 强制删除
  ```

  * docker pull 详解

  > **docker pull mysql** = **docker pull docker.io/libirary/mysql:latest**
  >
  > 没有指定tag就默认下载latest
  >
  > 指定版本下载命令 ——  **docker pull mysql:5.7**

* 容器命令

  ```bash
  docker run 
  			[--name "方便人识别的名字"] 
  			[-d] # 后台运行
  			[-it] # 使用交互方式运行，进入容器查看
  			[-P] # 指定端口
  			[-p] # 随机端口
  			
  docker ps # 查看运行容器
  			[-a] # 查看所有，包括其他
  			
  docker rm # 删除容器，-f 强制删除
  docker start 镜像ID # 运行一个之前停止的容器
  docker stop 镜像ID # 停止容器
  ```

  * docker run
    * **docker  run  centos  -it  /bin/bash**  运行并进入
    *  **exit**  退出并停止容器  **Ctrl+P+Q** 退出不停止 

