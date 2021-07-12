# Android开发探索艺术

## 第一章

> ### onPause()和onResume()执行顺序
>
> * `ActivityA.onPause()`先执行，`ActivityB.onResume()`后执行
> * 这是源码决定的
> * 因此`onPause()`中不能执行重量操作，因为这样会影响到下一个`Activity`的启动速度
>
> ### 异常情况
>
> * 资源相关的配置文件发生改变
> * 系统内存不足
>
> ### 资源配置改变
>
> * 横竖屏切换有可能加载不同的 **图片** 资源文件
>
> ### onSaveInstanceState()
>
> * 异常情况销毁才会调用
> * 正常退出是不会调用的
>
> ### View数据保存和恢复
>
> * 异常退出系统会自动保存和恢复一定的View数据
> * 如文本框用户输入的数据、LIstView滚动位置
> * 保存那些数据取决于每个View的`onSaveInstane()、onRestoreInstanceState()`方法的实现
> * 保存的逻辑是从`Activity`一步步委托
>
> ### 恢复位置
>
> * 一般都放在`onCreate()`中判断不为null进行恢复
> * 但有时候恢复放在`onRestoreInstanceState()`更方便
>
> ### 系统资源不足被异常退出
>
> * 如果一个进程中没有任何四大组件运行，那么这个进程很容易被杀死
> * 因此后台的耗时任务不能直接挂在后台，最好绑定一个`Service`，这样才不会被杀死
>
> ### 活动栈和任务栈
>
> * `ActivityStack`只有一个，用`ActivityRecord`记录对应的`Activity`信息
> * `ActivityTask`有很多个，用`TaskRecord`记录对应的`Activity`信息
> * 一个应用一般就是一个任务栈，`taskAffinity`一般就是app的包名
>
> ### SingleTask
>
> * 系统会创建新任务，并实例化新任务的根 Activity。
> * 但是，如果另外的任务中已存在该 Activity 的实例，则系统会通过调用其 `onNewIntent()` 方法将 intent 转送到该现有实例，而不是创建新实例。
> * Activity 一次只能有一个实例存在。
>
> ### SingleInstance
>
> * 系统不会将任何其他 Activity 启动到包含该实例的任务中
> * 该 Activity 始终是其任务唯一的成员；由该 Activity 启动的任何 Activity 都会在其他的任务中打开
>
> ***

## 第二章

> ### windows进程间通信
>
> * 剪贴板
> * 管道
> * 邮槽
>
> ### 单应用多进程情况
>
> * 某模块因为各种原因必须要运行在单独的进程
> * 申请更多的内存
>
> ### 如何开启多进程
>
> * `AndroidMenifest.xml`中声明
>
> ```xml
> <activity
>     android:name = ""
>     android:process =":remote" />
> ```
>
> * 有且仅有这一种方法，不支持运行时动态设置
>
> ### 进程命名
>
> * `:remote`：实际进程名为`包名:remote`
> * 带有`:`的是私有进程，不带`:`的是公有进程
> * 公有进程可以通过`ShareUID`方式共享
>
> ### 进程-应用-虚拟机关系
>
> * 一个`process`对应一个`Application`对应一个`Virtual Machine`
> * 因此静态变量、单例模式、线程同步机制会因为多进程而全部失效
>
> ### SP
>
> * SP底层实际上是通过修改`xml`文件来实现数据共享
>
> ### IPC
>
> * Intent
> * SP
> * Binder
> * Socket
>
> ### Serializable
>
> * 是一个空接口
> * 通过`ObjectOutputStream`和`ObjectInputStream`来实现序列化
> * 静态成员不属于对象不参与序列化
> * `transient`标记的也不参与序列化
>
> ### serialVersionUID
>
> * 最好加上，虽然不加也可以
> * 如果类中定义有就用定义的，如果没有就用类的`hashCode`
> * 在序列化时会将此值存入序列化文件；在反序列化时会将文件的值和对象类的值进行比较，如果不一样就序列化失败
> * 因此建议最好加上这个值
>
> ### Parcelable接口
>
> * 用于`Android`的序列化和反序列化
> * 比较复杂，里面待实现方法和变量很多
> * 有很多实现类可以直接用，如`Intent、Bundle、Bitmap`，集合也可以序列化，但要求集合内的元素都可序列化
>
> ### 两者对比
>
> * S简单但开销大，P复杂但开销小
> * Android建议使用P
>
> ### Binder
>
> * 实现了`IBinder`接口
> * 虚拟的物理设备，设备驱动是`/dev/binder`
> * 是连接`Manager`和`Service`的桥梁
> * 是客户端、服务端进行通信的媒介
