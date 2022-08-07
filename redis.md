# RedisLearning
学习Redis

## NoSQL

* 意义：not only SQL 不只是SQL

* 背景：很多如个人信息、社交网路、地理位置……等数据格式的存储不需要一个固定的额格式！而java中的Map<String,Object>什么数据都能装

* 特点
  * 方便扩展（数据之间没有关系，好扩展）
  * 大数据量高性能
  * 数据类型多样（不用设计数据库，随写随用）
  * 没有固定查询语言
  * 键值对存储、列存储、图形数据库
  * 高性能、高可用、高扩展
  
* 大数据的3V
  * 海量——volume
  * 多样——variety
  * 实时——velocity
  
* 大数据的三高
  * 高并发
  * 高性能
  * 高可扩（随时水平拆分，添加服务器）
  
* 实际：SQL与NoSQL结合使用

***********************
## 一个简单的商品页面技术

* 文档型数据库——MongDB。比如存文字较多商品描述、评论
* 分布式文件系统——FastDBS。比如存商品图片
* 搜索引擎——elastricsearch。比如商品的关键字搜索
* 内存数据库——Redis。比如秒杀商品
**************************
## 大型互联网企业问题

* 数据类型太多
* 数据源太多
* 经常重构
* 一旦改造则改大面积

****************

## NoSQL四大分类

* KV键值对——Redis
* 文档数据库(bson格式)——MongoDB
* 列存储数据库——HBase
* 图关系数据库(不是存图片的，是存关系的)——Neo4j
  * 存社交网络，朋友圈等

*****************

## Redis概述

* remote dictionary server 远程字典服务
* 特点：C语言编写、支持网络、KV存储、基于内存、可持久化、多语言API
* 官网：`www.redis.io`
************************

## Redis安装

* 官网下包

* linux解压安装包

* 安装基本环境

  ```bash
  yum install gcc-c++
  ```

* make(第一次会执行很长时间)

  * redis 6需要gcc版本大于9，然而yum的版本只有4。学习使用建议使用Redis5版本
  * 执行结束后会多出一个src目录
  
  ```bash
  make
  
  make install
  ```
  
* 复制配置文件（以后从这个配置文件启动，原版作为备份）
  
  ```bash
  # 创建一个文件夹
  mkdir /usr/local/bin/myredisconfig
  # 移动配置文件
  cp /usr/local/redis/redis-5.0.4/redis.conf /usr/local/bin/myredisconfig/
  ```
  
* 默认不是配置文件，去修改配置文件
  
  ```bash
  vim /usr/local/bin/myredisconfig/redis.conf
  #设置 daemonize=yes
  ```
  
* 启动。在/usr/local/bin目录下执行语句
  
  * 执行结束后会有redis-cli生成
  
  ```bash
  # redis-server处于/usr/local/bin
  # myredisconfig/redis.conf是当前目录下的配置文件
  redis-server myredisconfig/redis.conf 
  ```
  
* 使用(在/usr/local/bin目录下)

  ```bash
  # 指定配置文件
  redis-server /usr/local/bin/myredisconfig/redis.conf
  # 连接
  redis-cli -p 6379
  # ping
  ping
  ```
  
* 测试

  ```bash
  set name IzumiSkai # set方法
  get name # get 方法
  keys * #查看所有key
  ```
  
* 退出
  
  ```bash
  shutdown
  exit
  ```
  
* 查看
  
  ```bash
  ps -ef | grep redis
  ```
*************************





## Redis性能测试

* 基本测试过程

  ```bash
  redis-benchmark 参数
  ```

* 例子

  ```bash
  redis-benchmark -h 127.0.0.1 -p 6379 -c 50 -n 10000 # 指有50个并发连接，每个连接请求数为10000
  ```

******************





## Redis基本知识

* 数据库

  * Redis一共有16个数据库，默认使用第0个数据库。可以从配置文件中查看以下配置

    ```bash
    databases 16
    ```

  * 操作

    ```bash
    # 切换到第四个数据库
    SELECT 3(index)
    # 查看数据库内容大小
    DBSIZE
    # 清楚当前数据库
    FLUSHDB
    ```

* 线程 
  * Redis 6之后是多线程，之前只支持单线程
  * Redis是很快的，是基于内存的操作。它的性能瓶颈不是CPU，而是内存和带宽
  * 一些误区
    * 高性能的服务器一定是多线程的
    * 多线程（特别是单CPU上下文切换）的性能一定比单线程高

*******************



## Redis-Key的基本操作

* 举例

  ```bash
  # 是否存在一个Key
  EXIST key # 是否存在name键
  
  # 删除一个key
  del key
  
  #设置有效期
  expire key 10(seconds) # 10s后name键就过期
  
  #查看有效期
  ttl key
  
  # 查看类型
  type key # age也是string类型
  ```

***

## string

* 操作

  ```bash
  # 字符串最加 
  # 如果当前字符串不存在，就相当于set key
  append key "append-string"(value) # 其中name是键
  
  # 获取字符串长度
  strlen key 
  
  # 增加1/减少1
  incr/decr views(key) # 其中views是键名
  
  # 增减/减少 步长
  incrby/decrby views(key) 10(offset) # 其中name是key，10是步长
  
  # 截取字符串，当end是-1时代表全部截取
  getrange key 0 3 # 0是begin，3是end
  
  # 替换字符串
  setrange key offset value
  
  # 设置过期时间
  setex key seconds value
  
  # 不存在则创建, 存在就报错不设置
  setnx key value 
  
  # 批量设置(不存在则创建)
  msetnx key value [key value]  # 原子性操作。只要其中一个不存在则其他都不创建。要么都创建要么都不创建
  
  # 批量获取
  mget key [key]
  
  # 设置对象
  setnx user:Izumi {name: "Izumi Sakai",age: 40} # 还是key-value的结构，本质没变
  
  # getset命令，先获取后设置
  getset key value # {不存在：“返回null，在设置新值”，存在：“返回旧值，修改旧值为新值”}
  ```



******************************************



## List

* 操作

  ```bash
  # 新增元素
  lpush/rpush key element [elements ...]
  
  # 获取元素(没有rrange)
  lrange key start end
  
  # 根据index获取值(没有rindex)
  lindex key index
  
  # 删除元素
  lpop/rpop key
  
  # 查看长度
  llen key
  
  # 精确移除元素(list允许元素重复)
  lrem key count element
  
  # 截断list
  ltrim key start end
  
  # rpoplpush命令
  rpoplpush source destination # 相当于list最后一个pop，push到另一个list
  
  # 指定位置添加值
  lset key index value
  
  ```
  
* 注意

  * 只有push和pop分l和r，其他的只有l
  * list的底层是链表
  * 根据入和出的顺序可以玩出栈和队列
  * 里面的值可以重复，但set集合里面不能重复

************************

## 事务

* 基础

  * redis一条指令保证原子性，但是事务不保证原子性
  * redis事务就是一串命令的集合

* 事务过程

  * 开启事务
  * 命令入队
  * 执行事务

* 执行过程

  ```bash
  multi
  setnx k1 v1
  setnx k2 v2
  setnx k3 v3
  get k2
  exec
  ```

*********************

## 锁

* 悲观锁： 认为什么时候都会出问题，都加锁。很影响性能
* 乐观锁： 认为什么时候都不会出问题，都不加锁。只是更新数据的时候去判断检查一下，在此期间这个数据有没有被修改
  * 一般使用version字段实现

**************************************

## Java连接redis

* 一些事情

  * 配置文件中的配置 **bind** 项是配置那个IP地址可以访问。比如配置 **bind 127.0.0.1**就是默认本机启动
  * **redis-server  配置文件** 命令的实际作用在对应IP和对应端口启动redis，暴露端口访问
  * **ps  -ef | grep redis** 可以查看开启的是那个IP和那个端口

* 公网IP和私有IP

  * 只有公网IP才能访问网络，私网IP不能访问网络
  * 一个公网IP地址让很多歌私有IP地址访问
  * 172开头的就是B类私有地址
  * 198开头的就是D类私有地址

* 此次访问条件

  * bind 设置为私有IP地址 **172.18.65.13**
  * 开启 **redis-server**

* 连接过程

  * 导入依赖

    ```html
    <dependency>
         <groupId>redis.clients</groupId>
         <artifactId>jedis</artifactId>
         <version>3.3.0</version>
    </dependency>
    ```

  * 连接

    ```java
    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.113.97.26", 6379);
        System.out.println(jedis.ping());
        jedis.set("age","40");
        System.out.println(jedis.get("name"));
        jedis.close();
    }
    ```

  *******************

## springboot整合redis

* springboot底层不在使用jedis，而是使用 **lettuce**
  * jedis: 采用的直连，当多个线程操作时，是不安全的。为了避免不安全，得使用连接池
  * lettuce： 底层采用的是 **netty** 技术，实例可以在多个线程中共享。

* 整合过程

  * 导入依赖

    ```html
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    ```

  * 配置yaml文件

    ```yaml
    spring:
      redis:
        host: 172.18.65.13
        port: 6379
    ```

  * 代码

    * 一定要写 **@RestController**, 不然默认返回网页

    ```java
    @RestController
    public class Test {
    	//自动注入
        @Autowired
        private RedisTemplate redisTemplate;
    
        @RequestMapping("/")
        public String testConnection(){
            redisTemplate.opsForValue().set("test","testvalue");
            return "result";
        }
    }
    ```

  * 自定义配置
    * **Ctrl + shift + n** 搜索所有的类
    * 搜索后可以查看到RedisTemplate类默认的序列化器是JDK序列化，最好改成json序列化