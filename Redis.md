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
  # 连接
  redis -cli -p 6379
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









