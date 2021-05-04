# 第一行代码 - android

## 第一章 - 概述

> ### 系统架构
>
> * 内核层：提供了底层驱动
> * 运行库层：分为library和runtime
>   * Libary：SQLite、OpenGL、WebKit
>   * runtime：运行时核心库，允许使用Java来编写Android
> * 框架层
>   * 构建应用的各种API
> * 应用层：每个应用都是在应用层
>
> ### 开发特色
>
> * 四大组件
>   * activity：所有能看到的图标画面全在activity上
>   * service
>     * 即使应用退出后依然可以运行
>     * 是看不见的
>   * broadcast receiver：广播消息如短信、电话等
>   * content provider：用于不同应用间共享信息
> * 系统控件丰富
>   * 不满足系统控件还可以自定义系统控件
> * sqlite数据库
>   * 提供了极好的数据库API支持
> * 媒体丰富强大
>
> ### 项目目录结构
>
> * my application
>   * app
>     * src
>       * main
>         * Java：源代码处
>         * res
>           * drawable：存图片
>           * layout：存布局
>           * mipmap：存图
>           * values：存字符串对
>         * AndroidMainfest.xml：整个Android项目的配置文件，四大组件都需要在这里注册
>     * build.gradle：APP模块的gradle构建脚本
>     * proguard-rules.pro：混淆规则文件
>   * gradle
>     * wrapper：Google建议使用wrapper gradle，这样项目会自动从本地缓存找gradle，找不到就从仓库下载
>   * build.gradle：全局gradle构建脚本
>   * gradle.properties：全局gradle配置文件
>
> ### log
>
> * 五个级别
>   * verbose
>   * debug
>   * info
>   * warning
>   * error
>
> * 特点：可以定义过滤器快速查找日志，总之不建议使用`Println()`打印日志
>
> ***

## 第三章 - Activity

> ### 是什么
>
> * 是一种可以包含用户界面组件，主要用于和用户进行交互的activity