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
  			[-e] # 环境配置，比如MySQL的密码
  			
  docker ps # 查看运行容器
  			[-a] # 查看所有，包括其他
  			
  docker rm # 删除容器，-f 强制删除
  docker rm -f $(docker ps -aq) # 删除所有
  docker start 镜像ID # 运行一个之前停止的容器
  docker stop 镜像ID # 停止容器
  ```
```
  
  * docker run
    * **docker  run  centos  -it  /bin/bash**  运行并进入
    *  **exit**  退出并停止容器  **Ctrl+P+Q** 退出不停止 

**************



## 常用的其他命令

* 后台运行 

  ```bash
  docker run -it -d centos 
  # 常见的坑，docker后台启动时，必须要有一个前台的进程，docker发现没有其他应用，就会自动停止
```

* 查看日志

  ```bash
  docker logs -ft 容器ID
  ```

* 查看容器里面的进程信息

  ```bash
  docker top 容器ID
  ```

* 查看容器的所有信息

  ```bash
  docker inspect 容器ID
  ```

* 进入当前正在运行的容器

  ```bash
  docker exec -it 容器ID 默认bash  # 进入容器后开启一个新的终端，可以在里面操作
  docker attach 容器ID # 进入容器正在执行的终端，不会启动新的进程
  ```

* 从容器内拷贝文件到容器外

  ```bash
  docker cp 容器ID: 容器内路径 容器外路径
  ```

***************

## 练习Nginx和其他

* 首先先search和pull

* 运行

  ```bash
  docker run -d -p:8001:80 --name nginx01 nginx # 把Nginx的80端口映射到主机实际的8001端口
  ```

* 查看

  * 外部Windows主机访问 **47.113.97.26:8001**就能看到Nginx主页

* 进入Nginx

  ```bash
  docker exec -it 容器ID /bin/bash
  ```

* 其他如何启动运行
  
   * 进入 **https://hub.docker.com/** 搜索相应的应用，然后下面会有 **docker  run** 的指令，直接CV就能运行

  

******************



## 联合文件系统

* 英文名： UnionFS
* 思想：分层，对文件的修改是一层一层的叠加。类似于git的版本控制
* docker镜像
  * docker镜像实际上就是一层一层的文件系统组成的
  * docker镜像的最底层就类似于操作系统镜像从关机到开机，是每一个镜像都要执行的过程。这层可以理解成操作系统层
  * docker镜像中重点负责的是命令，内核启动加载就不负责了
  * 每个docker镜像都类似一台linux主机，只不过非常的精简
  * 举例：一个镜像最底层是Ubuntu，上面加一层java环境，在上面套一个Tomcat服务器环境……不断的加层
  * docker镜像一般将很多个文件统一计算做一层。如centos、jdk、redis统一算作一层，因为很多镜像都要公用这一层
  * 下载镜像的时候就分层下载，如果此层已经下载就不用下载了
  * docker镜像默认都是只读的，但镜像启动成容器时实际上就是在镜像上面在加一层。自己的操作是完全不影响原本的镜像文件
  * 你自己的操作添加后又多了一层，此时可以把这个打包，就成了一个新的镜像



**************

## 提交镜像

* 命令

  ``` bash
  docker commit -a="author" -m="message" 容器ID 自定义镜像名: tag
  ```

*************



## 容器数据卷

* 命令

  ```shell
  docker run -itd -v 本机路径 : 容器路径 镜像名 # 执行挂载
  docker inspect 镜像ID # 查看挂在，在Mouts节点可以看到详细信息
  ```

* 特点

> 双向绑定，类似于Vue
* 具名和匿名挂载

  * 具名 ：**-v  mymysql:/var/lib/data**
  * 匿名：**-v /var/lib/data**

> 具名和匿名都挂载到 /var/lib/docker/volumes/***下

****************

## 使用MySQL

* 命令

  ```shell
  docker run   -itd  # 后台交互运行
  			-v /home/mysql/config:/etc/mysql/conf.d # 挂载配置文件目录
  			-v /home/mysql/data:/var/lib/mysql # 挂载数据目录
  			-p 3306:3306  # 主机端口映射
  			-e MYSQL_ROOT_PASSWORD=542270191MSzyl # 配置环境(密码) 
  			--name mysqlwithpw # 起别名
  			mysql:5.7 # 运行的镜像
  ```

* 连接

> 适应DataGrip输入h,p,u,p直接就可以连接

***************



## 数据卷容器

* 作用

> 使两个或多个本来相互隔离的容器之间实现数据同步。比如两个MySQL数据同步

* 命令

  ```shell
  docker run 
  			-itd 
  			--name centos02 
  			--volumes-from centos01 # 挂载
  			centos
  ```

* 效果

> centos1和centos2中的挂载目录实现了双向绑定
>
> 其中centos01是父容器
>
> 其中挂载目录是通过dockerfile里面编写的shell实现的，两个容器使用同样dockerfile构建的镜像，因此挂载目录相同
>
> 只有当全部的容器删除了挂载目录里面的内容才会删除，只要还有一个容器在使用挂载目录，那么里面的内容就不会删除
>
> --volumes-from 挂载和 -v挂载不同。一种是两个容器之间，另一种是一个容器和宿主机之间
>
> 