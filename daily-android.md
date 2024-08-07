# 日常Android学习

## Android系统架构开篇

* 架构层
  * app
  * java api
  * native、android runtime - 很多实际的系统运行库都是成熟的开源项目，如`WebKit、OpenGL、SQLite`等，都是由`C++`实现
  * hal - 最开始没有，一部分原因是因为开源协议问题而迫不得已添加此层
  * kernel
* 调用
  * java api 和 native C++通过JNI调用
  * native 和 kernel之间通过System call 调用
* 通信方式
  * `Binder`：进程间通信，CS架构
  * `Socket`：主要用于framework层和native层之间的通信
  * `Handler`：同进程的线程间的通信

***

## Handler

* 四个组成

  * `Handler`：`post messages and handle messages`
  * `MessageQueue`：`a container of messages`
  * `Looper`：`get messages from MessageQueue`
  * `Message`：`an intermediary and carry real data`

* 成员及方法信息

  * Handler

    * 成员变量

      * `MessageQueue mQueue`
      * `Looper mLooper`

    * 成员变量初始化

      ```java
      //Handler.java
      public Handler() { this(null, false); }
      
      public Handler(Callback callback, boolean async) {
        mLooper = Looper.myLooper();
        mQueue = mLooper.mQueue;
      }
      
      //Looper.java
      public static Looper myLooper() {
        return sThreadLocal.get();
      }
      ```

    * 成员方法

      * `public void dispatchMessage(@NonNull Message msg)`

        ```java
        public void dispatchMessage(@NonNull Message msg) {
          if (msg.callback != null) {
            handleCallback(msg);//如果message有callback就执行callback
          } else {
            if (mCallback != null) {
              if (mCallback.handleMessage(msg)) { //如果handler自身的mCallback不为空就执行自身的mCallback
                return;
              }
            }
            handleMessage(msg); //都为空就兜底执行自己的handleMessage()方法，这个方法一般子类自己实现
          }
        }
        ```

      * `public final boolean post(@NonNull Runnable r)`：表面post一个`Runnable`，实际还是会被包装成一个`Message`

      * `public final boolean sendMessage(@NonNull Message msg)`：send一个message

      * `private boolean enqueueMessage(MessageQueue queue, Message msg,long uptimeMillis)`：上面两个方法都会执行它，向链表里面插入消息

  * MessageQueue(是一个单链表)

    * 成员方法
      * `bool enqueueMessage(Message msg, long when)`
      * `Message next()`

  * Message

    * 成员变量
      * `Handler target`：所属的Handler
      * `Object obj`：message携带的数据内容
    * 成员方法
      * `public void sendToTarget()`：把这个消息让自己的成员`Handler target`给`post`出去

  * Looper

    * 静态方法
      * `private static void prepare(boolean quitAllowed)`：`new and set`当前线程的`looper`，`new and set`当前线程的`MessageQueue`
      * `public static Looper getMainLooper()`：获取`UI线程`的`looper`
      * `public static void prepareMainLooper()`：`ActivityThread.java`的`main()`方法会调用
      * `public static void loop()`：把当前线程的`MessageQueue`跑起来，每个`message`执行`msg.target.dispatchMessage(msg);`
    * 变量
      * `MessageQueue mQueue`
      * `static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();`
      * `Thread mThread`

* Looper

  * `static prepare()`：给当前线程的threadLocal初始化set一个新的Looper
  * `private Looper()`：给此Looper初始化set一个MessageQueue，因为Looper和线程绑定，传递性可知MessageQueue和线程绑定
  * `statice loop()`：获取当前线程的looper对象，获取当前线程的MessageQueue对象，开始loop

* Handler

  * `Handler()`：绑定`mLooper`，绑定`mQueue`
  * `dispatchMessage(message:Message)`：链式调用，首先`message.callback.run()`，接着`Handler.mCallback.handleMessage(msg)`，最后`Handler.handleMessage(msg)`方法
  * `post(r:Runnalbe)`：把Runnable封装成`Message`丢进MessageQueue

* Message

  * `Handler target`：消息的响应方
  * `Runnable callback`：消息的回调方

* 消息池

  * 消息`Message`是可以进行复用的

* 总结

  * Handler最对外
  * Looper、MessageQueue和线程ThreadLocal绑定
  * Handler发送和分派消息，Looper不断入队消息和出队消息

* 数量关系

  * 一个线程最多一个`Looper`，是由`public static void prepare()`决定的
  * 一个线程最多一个`MessageQueue`，是由`private Looper(boolean quitAllowed)`决定的
  * 一个线程可以有很多个`Handler`，但多个`Handler`绑定的是同一个`Looper`和`MessageQueue`
  * 如何区分不同的消息？

  ```java
  //Handler.java
  private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
    msg.target = this; //每个消息都带上抛自己的handler
    return queue.enqueueMessage(msg, uptimeMillis);
  }
  ```

  ```java
  //Looper.java
  public static void loop() {
    for (;;) {
      Message msg = queue.next(); // might block
      try {
        msg.target.dispatchMessage(msg);//由每个消息自己的handler处理消息
      }
    }
  }
  ```


***

## MVP、Handler实例

* FatherModel.java

  ```java
  class FatherModel<T> implements IHandler{
    protected void handleData(T data) {
      //无实现，子类override它
    }
  
    //重写WeakHandler的handleMsg方法
    @Overrite
    public void handleMsg(Message msg){
      //调用自己的handleData方法(实际上子类会复写此方法)，处理的数据从Message来。msg.obj就是Response
      handleData(msg.obj);
      //成功了调用listner的onSuccess()方法。此处listner为Presenter
      listener.onSuccess();
    }
  }
  ```

* SonModel.kt

  ```java
  class SonModel extend FatherModel{
  	private fun fetchData(){
        use a handler to commit a Runnable
    }
    override fun handleData(response: Response?){
     	//重写父类的handleData
    }
  }
  ```

* FatherPresenter.java

  ```java
  class FatherPresenter{
  	//在父类的构造方法处将Presenter作为Observer，Model作为Observable
  	public void bindMyModel(Type myModel) {
      this.mModel = myModel;
      this.mModel.addNotifyListener(this);
    }
  }
  ```

* SonPresenter.java

  ```java
  class SonPresenter extend FatherPresenter{
  	//成功的方法。SonPresenter作为观察者，这是观察者的一个回调方法
  	override fun onSuccess(){
     //成功调用View的doSuccess()方法
     mView.doSuccess()
    }
  }
  ```

* 方法调用顺序

  * `View.refresh()`
  * `DerivedPresenter.refresh()`
  * `DerivedModel.getData()`
  * `DerivedModel.userExecutorToPostARunnalbe()`//构造一个`Runnable`抛到线程池里面
  * `mMessage.obj = obj; mMessage.sendToTarget();`//异步请求返回，向主线程抛一个Message
  * `mainLooper`处理`message`，交给对应的`DerivedModel`的成员`mHandler`处理
  * `mHandler.handleMessage(Message msg)`
    * 此方法`Override`自`Handler`
    * 其中涉及`mHandler`的`dispatchMessage(Message msg)`
  * `BaseModel.handleMsg()`//`mHandler`弱引用，上一个方法体内调此方法
  * `DrivedModel.handleData()`//上一个方法体内调用，通过Java多态动态绑定到子类此方法
  * `DrivedPresenter.onSuccess()`//上上个方法体内调用
    * 这是观察者模式决定的，`DrivedPresenter`在构造时被添加为了`DrivedModel`的`listener`
  * `View.doOnSuccess()`

* handler部分详解

  * 创建一个`Runnable`，里面定义好想执行的操作

  ```java
  new Callable() {
   @Override
    public Object call() throws Exception {
      return myApi.getData();//这是一个网络请求
    }
  }
  ```

  * 封装另外一个`Runnable`

  ```java
  new Callable() {
   @Override
    public Object call() throws Exception {
      Object obj = mCallable.call();//这个mCallable是上面的Runnable，执行会耗时
      if (mMessage != null) {
        mMessage.obj = obj; //obj是网络返回的数据
        mMessage.sendToTarget(); //这一行是调用的handler.sendMessage()
      }
    }
  }
  ```

  * 线程池异步执行

  ```java
  mExecutor.execute(Runnable runnable)//异步执行上面那个Runnable
  ```

***

## Binder

* 理解类比

  * binder驱动 - 路由器转发内容
  * ServiceManager - DNS
  * Client - client
  * Server - server

* 原理简介

  * binder是一个设备，但其没有对应的硬件实体，而是内核态的一段内存。可以通过`open、ioctl、mmap`等系统调用对这个设备进行操作
  * `ioctl`指令 - 这是binder接口中工作量最大的一个，它承担了Binder驱动的大部分业务。常规的`read、write`就是通过`ioctl`来实现的
  * binder接口`ioctl`提供的指令非常非常的多，关于`ioctl`指令详解参考`os.md`

* 总结

  * 应用层的Client和Server之间不能直接交互，必须通过ServiceManager间接交互
  * Binder驱动位于内核空间，而Client、Server、ServiceManager位于用户空间
  * Binder和ServiceManager是Android平台的基础架构
  * 开发人员只用自定义实现Client和Server即可实现通信

* Linux进程通信方式
  * `管道`：缓冲区大小比较小，且消息需要复制两次
  * `消息队列`：复制两次
  * `共享内存`：复制1次，效率高。但要自己处理同步问题
  * `套接字`：更通用的接口，但效率低。只适用于不同机器不同网络
  * `信号量`：主要作为一种锁机制，用于进程同步
  * `信号`：类似于杀死进程等操作，使用软件形式的异常，即`ECF`

* Binder优势
  * `性能`：binder只需要复制一次，性能仅次于共享内存
    * 发送进程将数据从用户空间拷贝到内核空间。即一次复制
    * 由于内核缓冲空间和接收进程的用户空间存在内存映射关系，即不用复制就可以直接读取到数据。即零次复制
    * 总就只复制了一次
  * `稳定性`：CS架构比较稳定
  * `安全性`：Linux通信方式在内核态无任何保护措施，完全只看效率。Binder通信可以获得可靠的uid/pid
  * `语言角度`：Binder机制是面向对象的。一个Binder对象在各个进程中都可以有引用
  * `Google战略`：Google让GPL协议止步于Linux内核空间，而binder是实现在用户空间的


***

## ServiceManager

* What?

  * It is a system-level service in Android that maintains a registry of available services, and provides a way for processes to look up and communicate with these services.

* features

  * It is implemented as a singleton instance within a single process.
    * Each Android application has its own instance of `ServiceManager` for security reasons and to enforce process isolation.
    *  By having separate instances of `ServiceManager` for each application, the system ensures that one application cannot directly access or modify the service registry of another application.
    * If each application shared the same instance of `ServiceManager`, it would be possible for one application to cause a crash or other issue that affects all the other applications using the same instance.
    * Most Android applications run in a single process, which includes the implementation of business logic and the `ServiceManager`
    * If two different applications try to get a reference to the Activity Manager Service (`AMS`), they will both be accessing the same instance of the service. 
  * It provides a number of system-level services, such as the telephony service, the power manager service, the package manager service, and so on.
  * Each of these services is identified by a unique name, and can be accessed by other processes using the `Binder` mechanism.

* how it works

  * hen a process wants to communicate with a service, it sends a message to the `ServiceManager` over the `Binder` mechanism, requesting the service by name. The `ServiceManager` then looks up the appropriate service in its registry of available services, and returns a `Binder` object that can be used by the requesting process to communicate with the service.

* like DNS?

  * DNS - domain names to IP addresses
  * ServiceManager - unique names to services

* 介绍

  * SM同样是一个binder服务，它相当于DNS的作用。它启动时机非常早，保证了在有人使用binder服务前它已经启动。
  * 它单独运行在一个进程中

* 工作原理

  * `Service_manger.c`的`main`函数

    ```c
    int main(int argc, char **argv){
      bs = binder_open(128*1024);//调用binder_open打开驱动
    	binder_become_context_manager(bs);//将自己注册成DNS
      bidner_loop(bs, handler);//使用消息循环模式开始服务
    }
    ```

    * 打开binder驱动，做好初始化
      * 调用`open("/dev/binder", O_RDWR)`打开驱动节点
      * 调用`mmap`映射内存
    * 设置自己为DNS
      * 通过`ioctl`发送`BINDER_SET_CONTEXT_MGR`指令实现
    * 进入主循环
      * 主循环采用`handler`一模一样的消息循环机制
      * 读取消息 - 通过`ioctl`发送`BINDER_WRITE_READ`消息
      * 处理消息

  * 设计思考

    * 使用`native`实现还是`java`实现并没有任何关系，无论那种方式的本质都是围绕`binder`驱动展开的
    * 由于发起binder请求的可能是Android APK，而请求binder一般都是通过SM间接请求，因此SM必须有java层的接口
    * 如果每次访问都需要打开binder设备，执行mmap操作就太费时了，因此一个进程的所有线程共享binder设备，使得binder只需要打开一次

  * 访问一个binder server的步骤

    * 打开binder设备
    * 执行mmap
    * 通过binder驱动向SM发送请求
    * 获取结果

***

## Android启动过程

* 启动三个阶段

  * Boot loader
  * Linux kernel
  * Android System service

* 介绍

  * Android系统实际上是运行于`linux`内核之上的一系列`“服务进程”`，并不算一个完整意义上的`操作系统`
  * 进程的老祖宗0号进程是`init`进程

* `init.rc`中启动的进程

  * `ServiceManager`进程

    * 它在一个独立的进程中

    ```bash
    service servicemanager /system/bin/servicemanager
    	class core
    	user system
    	group system
    	critical
    	onrestart restart zygote
    	onrestart restart media
    	onrestart restart surfaceflinger
    	onrestart restart drm
    ```

  * `Zygote`进程

    * 它所在的程序名为`app_process`。`app_process`充当一个壳的角色，它会启动虚拟机
    * `app_process`就是系统服务的根据地。它在`init`进程的帮助下通过`zygote`逐步建立起各`SystemServer`的运行环境

  * `SystemServer`进程

    * 是由`java`语言编写的系统服务
    * 由`ZygoteInit`最终调用到`fork`系统调用生成一个新的进程
    * 它会接着启动如`SurfaceFlinger、AudioFlinger`等native层服务启动
    * 还会新建一个线程来启动java层服务，如`PowerManagerService、ActivityManagerService`

  * `adbd`进程

***

## .9.png

* left
  * 决定上下缩放那些区域可以缩放

* top
  * 决定左右缩放那些区域可以缩放

* right
  * 决定上下那些区域可以放置实际内容

* bottom
  * 决定左右那些区域可以放置实际内容


***

## RecyclerView优化

* ItemView生命周期
  * `create`：主要对应`onCreateViewHolder()`
  * `bind`：主要对应`onBindViewHolder()`
  * `addView`
  * `measure`
  * `layout`
  * `draw`

* onCreatViewHolder
  * 只有在`RecyclerView`的多级缓存被击穿时才会调用，一旦调用，卡帧就特别明显
  * 可以采取异步线程调用UI线程获取方式，等于在多级缓存后面又加了一个自己的异步View缓存池

* onBindViewHolder
  * 精简代码逻辑
  * 预加载
  * 异步
  * 延迟绑定
  * 复用

* onMeasure耗时优化
  * 最核心的思路是禁止`measure`两次
  * `mearsure`两次情形
    * `RelativeLayout`
    * `LinearLayout`使用了`weight`属性
    * `ConstrainttLayout`设置`width = 0`，根据`toLeftOf、toRightOf`确定宽度
  * 禁止`RTL`布局

* addView、removeView
  * 没啥优化的点
  * 注意`View.onAttach()、View.onDetach()`方法调用

* 通用优化
  * 滑动期间规避全局`requestLayout`

***

## gradle

* 分类

  * Project的`build.gradle`
  * Module的`build.gradle`

* Project维度

  ```groovy
  // Top-level build file where you can add configuration options common to all sub-projects/modules.
  
  buildscript {//dependencies to run gradle itself
  
      repositories { //config remote repositories
          google()
          jcenter()
      }
      dependencies { //config build tools
          classpath 'com.android.tools.build:gradle:3.0.0'//此处是android的插件gradle，gradle是一个强大的项目构建工具
        	// buildscript itself to use classpath
          // NOTE: Do not place your application dependencies here; they belong
          // in the individual module build.gradle files
      }
  }
  
  allprojects { //dependencies to this project
      repositories {
          google()
          jcenter()
      }
  }
  
  // 运行gradle clean时，执行此处定义的task任务。
  // 该任务继承自Delete，删除根目录中的build目录。
  // 相当于执行Delete.delete(rootProject.buildDir)。
  // gradle使用groovy语言，调用method时可以不用加（）。
  task clean(type: Delete) {
      delete rootProject.buildDir
  }
  ```

* classpath 和 implementation

  * `classpath`：`buildscript itself needs something to run, use classpath`
  * `implementation`：`your project needs something to run, use implementation`

* Moudule维度

  ```groovy
  apply plugin: 'com.android.application'
  apply plugin: 'kotlin-android-extensions'
  apply from: 'https://xxx.gradle'
  
  android { //配置项目构建的各种属性
      compileSdkVersion 27 //设置编译时用的Android版本
      defaultConfig {
          applicationId "com.whu.myapplication" //项目的包名
          minSdkVersion 16 //项目最低兼容的版本,低于此无法安装
          targetSdkVersion 27 //项目的目标版本，系统会为该应用启动一些对应该目标系统的最新功能特性。如22运行在Android 6上就不会开运行时权限
          versionCode 1 //版本号
          versionName "1.0" //版本名称，展示在应用市场上
          flavorDimensions "versionCode"
      }
      buildTypes { // 指定生成安装文件的主要配置
          release { // 生产环境
              buildConfigField("boolean", "LOG_DEBUG", "false") //配置Log日志
              buildConfigField("String", "URL_PERFIX", "\"https://release.cn/\"")// 配置URL前缀
              minifyEnabled false //是否对代码进行混淆
              proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro' //指定混淆的规则文件
              signingConfig signingConfigs.release //设置签名信息
              pseudoLocalesEnabled false //是否在APK中生成伪语言环境，帮助国际化的东西，一般使用的不多
              zipAlignEnabled true //是否对APK包执行ZIP对齐优化，减小zip体积，增加运行效率
              applicationIdSuffix 'test' //在applicationId 中添加了一个后缀，一般使用的不多
              versionNameSuffix 'test' //在applicationId 中添加了一个后缀，一般使用的不多
          }
          //比如还可以打'大包'和'小包'
      }
  
      packagingOptions{ //打包时的相关配置
          //pickFirsts做用是 当有重复文件时 打包会报错 这样配置会使用第一个匹配的文件打包进入apk
          //表示当apk中有重复的META-INF目录下有重复的LICENSE文件时  只用第一个 这样打包就不会报错
          pickFirsts = ['META-INF/LICENSE']
  
          //merges合并 当出现重复文件时 合并重复的文件 然后打包入apk
          //这个是有默认值得 merges = [] 这样会把默默认值去掉  所以我们用下面这种方式 在默认值后添加
          merge 'META-INF/LICENSE'
  
          //重复依赖通过exclude去除重复的文件。
          exclude 'META-INF/services/javax.annotation.processing.Processor'
      }
  
      productFlavors { //多个渠道配置，为特定的渠道做部分特殊的处理，比如设置不同的包名、应用名等
        	// 配置后打包出来默认命名格式如app-wandoujia-release-unsigned.apk
          wandoujia {}
          xiaomi {}
          _360 {}
      }
  
      productFlavors.all {
              //批量修改，类似一个循序遍历
          flavor -> flavor.manifestPlaceholders = [IFLYTEK_CHANNEL: name]
      }
  
  
      lintOptions { //程序在编译的时候会检查lint，有任何错误提示会停止build，我们可以关闭这个开关
          abortOnError false //即使报错也不会停止打包
          checkReleaseBuilds false //打包release版本的时候进行检测
      }
  
  }
  
  dependencies { //定义此moudule的依赖关系
      //项目的依赖关系
    
      implementation fileTree(include: ['*.jar'], dir: 'libs')
      //本地jar包依赖
    
      implementation 'com.android.support:appcompat-v7:27.1.1'
      //远程依赖
    
      implementation 'com.android.support.constraint:constraint-layout:1.1.2'
      testImplementation 'junit:junit:4.12'
      //声明测试用例库
    
      androidTestImplementation 'com.android.support.test:runner:1.0.2'
      androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
  }
  ```

* apply

  * `apply plugin`：表示应用一个插件
  * `apply from : url` ：从URL引入插件
  * `apply plugin: 'com.android.application'`：表示打出来一个`apk`应用
  * `apply plugin: 'com.android.library'`：表示打出来一个`arr`文件，供别人使用

* dependencies{}

  * `implementation fileTree(include: ['*.jar'], dir: 'libs')`：一个本地依赖声明，表示将libs目录下所有.jar后缀的文件都添加到项目的构建路径当中
  * `implementation 'com.android.support:appcompat-v7:27.1.1'`：一个远程依赖

* api、implementation、compile

  * `api`：和`compile`一模一样，只是换了一个名字
  * `compile`：老的用法，已将废弃。引入的库整个项目都可以使用，容易导致重耦合
  * `implementation`：引入的库只有对应的Module能使用，其他Module不能使用

***

## 事件分发

* 硬件产生触动

* 触动传递给`InputManagerService`

* `InputeManagerService`通过`WindowManagerService`将触动传给对应的`window`

  ```java
  public class WindowManagerService extends IWindowManager.Stub {
    final InputManagerService mInputManager;
  }
  ```

* `window`将触动传送给对应的`ViewRootImpl`

  ```java
  public final class ViewRootImpl {
  	WindowInputEventReceiver mInputEventReceiver;
    public void setView(View view, WindowManager.LayoutParams attrs) {
      InputChannel inputChannel = new InputChannel();
      mWindowSession.addToDisplayAsUser(mWindow, inputChannel); // 添加Window进WMS通过传入inputChannel建立关联
      mInputEventReceiver = new WindowInputEventReceiver(inputChannel, Looper.myLooper()); //事件传递到mInputEventReceiver
    }
  }
  ```

* `ViewRootImpl`将触动封装成`MotionEvent`对象，传递给下层`View`

  ```java
  //ViewRootImpl#ViewPostImeInputStage
  final class ViewPostImeInputStage extends InputStage {
    @Override
    protected int onProcess(QueuedInputEvent q) {
      //调用processPointerEvent()
      return processPointerEvent(q);
    }
  }
  
  //ViewRootImpl#processPointerEvent()
  private int processPointerEvent(QueuedInputEvent q) {
    final MotionEvent event = (MotionEvent)q.mEvent;
    //调用dispatchPointerEvent()
    boolean handled = mView.dispatchPointerEvent(event);
    return handled ? FINISH_HANDLED : FORWARD;
  }
  
  //View#dispatchPointerEvent()，且ViewGroup未重写此方法
  public final boolean dispatchPointerEvent(MotionEvent event) {
    if (event.isTouchEvent()) {
      //调用dispatchTouchEvent()
      return dispatchTouchEvent(event);
    } else {
      return dispatchGenericMotionEvent(event);
    }
  }
  ```

* 应用布局界面和`Dialog`最顶层的`ViewGroup`为`DecorView`，奇怪的是`DecorView`对`dispatchTouchEvent`进行了重写

* 如果不为`DecorView`，则直接顶层`ViewGroup.dispatchTouchEvent(ev)`

* `DecorView.dispatchTouchEvent()`

  ```java
  public class DecorView extends FrameLayout {
   	public boolean dispatchTouchEvent(MotionEvent ev) {
      final Window.Callback cb = mWindow.getCallback(); // 这个callback实际是Activity
      return cb != null && !mWindow.isDestroyed() && mFeatureId < 0
              ? cb.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
  	} 
  }
  ```

* `Window.Callback`

  ```java
  public interface Callback { 
  	public boolean dispatchTouchEvent(MotionEvent event);
  }
  ```

* `Activity/Dialog`定义

  ```java
  public class Activity implements Window.Callback {
    ...
  }
  public class Dialog implements Window.Callback {
    ...
  }
  ```

* 当`Window.Callback cb = mWindow.getCallback(); cb != null`时，事件传递给了`Activity`或者`Dialog`

* 当`Window.Callback cb = mWindow.getCallback(); cb == null`时，事件传递给`super.dispatchTouchEvent(ev)`，即`DecorView`的父类`ViewGroup`

* DecorView叛徒

  * 本来事件从`ViewRootImpl.mView.dispatchPointerEvent()`传递好好的
  * 但是如果`ViewRootImpl.mView is DecorView`，则事件则交给了`Activity`或者`Dialog`

* Activity、PhoneWindow、DecorView、ViewRootImpl关系

  * `Activity`持有`PhoneWindow`

  ```java
  public class Activity implements Window.Callback {
    private Window mWindow
  
    final void attach(Window window) {
      mWindow = new PhoneWindow(this, window, activityConfigCallback);
      mWindow.setCallback(this); // 把自己设置为callback
    }
  }
  ```

  * `PhoneWindow`持有`DecorView`

  ```java
  public class PhoneWindow extends Window {
    private DecorView mDecor;
  
  	public PhoneWindow(Context context, Window preservedWindow,
              ActivityConfigCallback activityConfigCallback) {
      mDecor = (DecorView) preservedWindow.getDecorView();
    }
  }
  ```

  * `DecorView`持有`PhoneWindow`, 且可以通过父类`View.getViewRootImpl()`获取`ViewRootImpl`

  ```java
  public class DecorView extends FrameLayout {
    DecorView(Context context, PhoneWindow window) {
      setWindow(window);
    }
    void setWindow(PhoneWindow phoneWindow) {
      mWindow = phoneWindow;
    }
  }
  ```

* Activity事件分发

  ```java
  public boolean dispatchTouchEvent(MotionEvent ev) {
      // 这个方法是个空实现，给开发者去重写
      if (ev.getAction() == MotionEvent.ACTION_DOWN) {
          onUserInteraction();
      }
      // getWindow返回的就是PhoneWindow实例
      // 直接调用PhoneWindow的方法
      if (getWindow().superDispatchTouchEvent(ev)) {
          return true;
      }
      // 如果前面分发过程中事件没有被处理，那么调用Activity自身的方法对事件进行处理
      return onTouchEvent(ev);
  }
  ```

* PhoneWindow事件分发

  ```java
  public boolean superDispatchTouchEvent(MotionEvent event) {
    //交给DecorView去分发
    return mDecor.superDispatchTouchEvent(event);
  }
  ```

* DecorView分发

  ```java
  public class DecorView extends FrameLayout {
  	public boolean superDispatchTouchEvent(MotionEvent event) {
      //即ViewGroup事件分发
      return super.dispatchTouchEvent(event);
    }
  }
  ```

* 结论

  * 兜兜绕绕一圈，即使`ViewRootImpl.mView is DecorView `事件交给了`Activity`，但最终又绕回了`DecorView的父类ViewGroup.dispatchTouchEvent()`

***

## Context

* 链接
  * [context](https://juejin.cn/post/6887499574383116302)

* 定义
  * 应用程序与系统之间的桥梁
  * 应用程序访问系统资源的接口
  * Android系统给应用的一张凭证
  * 一个Java类有了context才能被称为组件

* 创建
  * 通过`AMS`创建
  * `AMS`会把一个`凭证`通过跨进程通信给到应用程序
  * 应用程序会把这个`凭证`封装成`context`，并提供一系列的接口
  * 优势：系统可以对程序访问系统做访问权限控制

* 实现类
  * `ContextWrapper`不是真正的实现类，真正的实现类是`ContextImpl`
  * 使用`装饰者模式`，`ContextWapper`持有`ContextImpl`对象
  * 真正的实现类只有三个：`Application`、`Service`、`Activity`

* Application
  * 全局单例，只有一个
  * 不能因为是单例而把`Application`当工具类用
  * 没有界面的context，就不应该拥有操作界面的权利

* Activity
  * 常用于`UI`的更新，如弹出`Toast`，添加`window`
  * 一直持有一个`activity`的`context`不释放可能会导致内存泄漏

* 总结
  * `Application`虽然是全局单例`context`，但是别乱用

***

## 性能优化总述篇

* 性能优化架构总图
  * 度量
    * 标准
    * 场景
  * 分析
    * trace
    * 慢函数
    * 卡顿树
  * 优化
    * 思路方法
    * 业务层
    * 框架层
    * 系统层
  * 防劣化
    * 日志
    * 人工分析
    * 文件隔离
    * 性能监控

* 标准
  * 启动度量定义：从点击`icon`到首页加载完成的间隔
  * 卡顿度量场景：启动一分钟内，因为一分钟后基本就不卡了

* 分析
  * `systrace`：一个分析工具

* 优化六板斧
  * 不执行：能不能删，能不能下掉
  * 懒加载：不用的话先不加载
  * 延迟：能往后延就往后延
  * 异步：用得最多
  * 预加载：和异步连在一起用
  * 减负：编写高效代码

* 启动拆解阶段
  * 主线程
    * `Application`
    * `MainActivty`
    * `doFrame`

* `Application`阶段
  * 此阶段有些任务必须要求在`Application`初始化结束前完成初始化
  * 即`主线程等待子线程`
  * 优化方法
    * 任务越早执行越好
    * 任务越小越好
    * 是不是真的必须在`Application`初始化结束前？可不可以延长结束时间
    * 增加子线程的数量和优先级
      * 数量：现在都是8核，可以适当增加(有可能资源竞争锁住)
      * 优先级：多分配一些时间片

* `MainActivity`阶段
  * 首页`View`异步预加载
  * 异步预加载问题：由于`inflate`源码当主和子同时`inflate`一个`View`时会有锁，解决方式——`x2c`直接硬写
  * 合并广告页和首页的`Activity`为一个
    * 原理：减少了`IPC`

* `MainActivity`和`doFrame`之间
  * 现状：理想情况数据一到就直接`doFrame`，但现实情况是这里面还夹杂着很多其他消息要处理
  * 解决方式：直接操作`MainLooper`的`MessageQueue`，通过反射将`SyncBarrier`插到队列(实际是链表)的最开始，优先保证`doFrame`执行

* `doFrame`阶段
  * 逻辑：读取首页内容返回时，才会`doFrame`
  * 两个思路
    * 首页内容耗时
    * 首页渲染耗时

* 首页内容耗时
  * 预加载
    * 传统思路都是`MainActivity`阶段才会异步发请求获取首页数据
    * 直接更极端一点`异步预加载`，放到`Application`阶段就进行
    * 就这样简简单单提个前就能带来很大的优化
  * 不执行
    * 分批上屏：屏幕以外的内容不先加载
  * 异步预加载：多线程解析
    * 但容易竞争

* 启动场景识别
  * 场景举例
    * `icon`冷启动
    * `back`退出后启动：`Activity`完成`finish`但`Application`还在
    * `home`后台在启动：`Activity`都在
    * 通知栏push：跳转页面未知
    * 第三方App跳转
    * 静默拉活
  * 结论：只有`icon`冷启动才用预加载提前到`Application`阶段
  * 如何识别：`Application`阶段开始遍历`MessageQueue`，找到`MainActivity Intent`说明是`icon`启动

* 渲染优化
  * 缓存机制：`mAttacedScrap` -> `mCacheViews` -> `mRecyclePool` -> `createViewHolder` -> `bindViewHolder`
  * 优化思路：异步预创建`ViewHodler`，然后把`mRecyclePool`给替换掉
  * 步骤
    * 主线程正常创建`RecycleView`
    * 子线程提前`new RecyclePool`，然后根据上次`ViewHolder`的`type`不停地创建
    * 然后把`RecycleView`的`mRecyclePool`给替换掉

* 卡顿是什么
  * case1:某一帧任务超过`16ms`但不超过很多。这其实还好，三缓缓存修复，只卡一帧用户感知不出来
  * case2:某一帧任务严重超时。用户感觉卡在那里
  * case2:频繁很多任务超过`16ms`，用户能明显感觉出来一下流畅一下卡

* 卡顿根因
  * 耗时任务穿插执行：不该抛到主线程执行任务抛到主线程执行，导致渲染时间超时
  * `RecycleView`列表卡片自身损耗

* 系统优化
* 框架优化

***

## 列表卡片优化

* 多type
  * 当`RecycleView`的`View type`种类很多时，会导致缓存命中率下降严重
  * 就会导致频繁创建`ViewHolder`而导致卡顿

* `createViewHolder`
  * `inflate xml`比较耗时，涉及`IO、解析、生成View`
  * 优化方法
    * 异步`inflate` + `x2c`：用java代码替换`xml`，在异步`inflate`

* `measure、layout、draw`
  * 优化布局层级
  * 减少过度绘制

* 提高缓存命中率
  * `View type`多，但并不是千差万别完全不一样
  * 可以更细粒度的拆分`View`，这样能复用的就多了

* `RecycleView`的`preFetch`机制
  * 第一帧执行的时间小于`16ms`，则就可以在第一帧绘制结束到第二帧显示之前开始第二帧内容的`create`和`bind`
  * 缺点：是在`主线程`执行的，如果时间太长加起来超过`16ms`就不会用

* 类似`prefetch`原理的一个框架
  * 提前预加载马上要显示滑到屏幕内的`View`
  * 如果要创建`ViewHolder`则异步创建，甚至进行异步绑定
  * 最后把它放到`ViewCacheExtension`里面
  * `mViewCacheExtension`的缓存优先级高于`mRecycleViewPool`
  * 且`mViewCacheExtension`就是拿给开发者自己定义的

* 异步渲染问题
  * 安卓原生组件线程不安全
  * 异步需要处理的问题很多
  * 多线程渲染会导致渲染的不确定性 

* 异步渲染框架
  * 细粒度组件：将原生`安卓View`做拆分
    * 属性集`Component`——`measure、layout`，它的属性不能改，一旦要改属性，就要重新创建`Component`，从而根本上杜绝了多线程更新UI属性
    * 画布——`draw`
  * 异步排版
    * 属性集即`layout、measure`，在异步执行。执行`模版解析、创建组件树、计算布局`
      * 模版解析：框架组件树映射到原生的`ViewTree`
    * 画布绘制`draw`在UI线程执行
  * 细粒度复用
    * 复用的级别不是`原生View`而是`Component`
  * 问题
    * `draw`之前一定要等`layout`好，即主线程要等异步线程，不能异步线程未完成布局主线程就开始绘制
    * 不然最开始绘制没有属性信息高度是0，然后布局计算完毕更新绘制进行跳变

***

## 卡顿归因

* 发现卡顿方式
  * 日志
  * 埋点：埋点是分析的基石

* 指标
  * 进程启动信息
  * activity切换
  * 插件事件
  * 页面活动轨迹
  * 主线程looper消息采样
    * 可以采样耗时的`message`，定位什么导致耗时处理
  * 主线程使用率
    * 如果它比较低，则可能被子线程抢占(如插件下载)，导致不能高效完成绘制任务，导致绘制超时，导致卡顿
    * 执行某个超级耗时任务，或者泄漏，会导致一个`message`执行长导致使用率高
  * 主线程休眠率
  * 主线程io_wait率
  * 主线程wait率
  * 主进程使用速率(tick/ms)
    * 每毫秒进程分配到的时间片，反映整个应用的繁忙程度
    * 一个应用可以多进程，其他进程可能抢占主进程使用
    * 设备功耗、发热问题也会导致使用率低
  * 进程内核态占比
    * IO导致占比不正常
    * fork
  * 线程数量
  * 内存
    * 看那个页面导致内存大规模上涨
  * 功耗

* 埋点上报策略
  * 底层监控如`CPU`放到`native`层进行，效率更高
  * `looper`队列执行时间过长上报一下
  * 消息处理超过某个阈值抓下堆栈上报一下

* 卡顿的类型
  * 绘制卡顿：任务超过`16ms`
  * 网络慢
  * 页面加载慢
  * 崩溃/卡死

* 分析思路
  * 主进程CPU使用率
  * 主线程使用率
  * 内核IO
  * 内存高
  * 设备发热，电量低

* 业务卡顿
  * 统计各阶段耗时
  * 分析`Looper`消息、`Activity`切换、`插件`等

* 插件
  * 下载慢抢流量资源、安装IO负载高、加载即使异步也抢资源

***

## 屏幕刷新机制

* [VSync、Choreographer 全面理解](https://juejin.cn/post/6863756420380196877)
* 显示系统基础知识
  * 三部分各司其职
    * `cpu`:计算帧数据
    * `gpu`:将帧数据进行处理渲染，处理好后放到`buffer`存起来
    * `display`:将`buffer`数据取出来，显示在屏幕上
  * 基本概念
    * `屏幕刷新率`:是一个硬件参数，固定不变。一般为`60hz`
    * `逐行扫描`:显示器从左到右，从上到下显示像素点。因为太快了人类感受不到
    * `帧率`:`gpu`一秒内绘制的帧数，帧率是动态变化的。当屏幕静止时，`gpu`不工作不绘制，但屏幕依旧在刷新
    * `画面撕裂(tearing)`:一个画面内的数据来自两个不同的帧，就会画面撕裂
* 双缓存

  * 画面撕裂原因
    * 屏幕16ms固定扫描进行刷新，刷新的数据就从`buffer`里面取
    * 如果`gpu`绘制的速度太快，显示器画第一帧还没有画完，但`buffer`内容已经更新成了第二帧
    * 这样画面上的内容就是一般是第一帧，一般是第二帧，这样就会画面撕裂
  * 双缓存
    * `gpu`绘制存在一个`后缓存`
    * `display`取内容在另一个`前缓存`
    * 当且仅当`gpu`绘制完成后，才将前后缓存进行交换，交换是内存地址交换不涉及拷贝，因此可以看成是瞬间完成的
  * `VSync`
    * 屏幕间隔16ms扫描两帧之间有个空隙，就用这个空隙来进行缓存交换
    * 即达到这个空隙就交换，垂直同步`VSnc`就是这个时间点
* 双缓存 + VSync

  * 一旦一个`VSync`信息发出，`cpu`立刻开始计算准备下一帧数据，`cpu`计算完成后`gpu`也立刻开始渲染这一帧数据
  * 缓存交换有两个条件
    * 达到临界点发出`VSync`信号
    * 当前屏幕显示的帧的下一帧内容`gpu`已经渲染好了
  * 即如果计算时间过长，当下一帧没有准备好时，`display`将一直显示同一帧，在视觉上就是一个卡顿
  * 以上是计算不足的缺点，但也有计算太足的缺点。即`cpu`当且仅当`VSync`信号发出后它才会开始进行计算，如果上一帧用不了多少时间，这个时候`cpu`时间就是浪费的
* 三缓存 + VSync

  * 即`cpu`、`gpu`、`display`各自一个缓存
  * `VSync`时候进行交换
  * 缓存多了一个就能够充分利用`cpu`和`gpu`的性能
  * 缓存交换条件就只有一个：达到临界点发出`VSync`信号。不用等`gpu`把当前屏幕显示的帧的下一帧内容渲染好就可以直接开始下下帧的计算
  * 但如果计算时间太长该卡还是会卡
* Choreographer

  * 作用：实现收到`VSync`信号立刻开始`cpu`计算下一帧的类
  * 核心方法：`doFrame()`
  * `mHander.getLooper().getQueue().postSyncBarrier()`，插入同步屏障，保证收到`VSync`信号时立即执行`doFrame()`开始绘制

* 疑问解答
  * `丢帧`：某一帧延迟显示，因为有其他帧多次显示
  * `布局复杂/主线程耗时是如何造成丢帧的？`：计算时间超过`16.6ms`就会丢帧
  * `是不是16.6ms走一次measure/layout/draw?`：不是，只有有绘制任务才三部曲，屏幕静止不三步曲，且绘制时间间隔取决于布局复杂度及主线程耗时
  * `measure/layout/draw走完，界面就立刻刷新了吗?`：不是，要等`VSync`
  * `Choreographer是干啥的？`：用于实现`VSync`到来时开始绘制

***

## Looper不死循环

* ANR

  * 定义：应用在一定的时间内没有得到响应或者响应时间太长

* android主要的流程

  * `Looper.java`

  ```java
  public static void loop() {
    for (;;) {
      Message msg = queue.next(); // might block
      try {
        msg.target.dispatchMessage(msg);
        if (observer != null) {
          observer.messageDispatched(token, msg);
        }
        dispatchEnd = needEndTime ? SystemClock.uptimeMillis() : 0;
      } catch () {
  
      }
    }
  }
  ```

  * 安卓大部分都是从`mainLooper`中取消息执行`msg.target.dispatchMessage(msg);`

* 消息太多

  * 进`MessageQueue`，慢慢等待执行就行了

* 消息太少

  * 执行`queue.next()`，会停在`native`方法`private native void nativePollOnce(long ptr, int timeoutMillis);`
  * 阻塞在这里，这是主线程会释放`cpu`资源，根本不会ANR

* 综上

  * `android`的机制是不会出现`ANR`的
  * `ANR`都是因为处理`Message`任务过重

***

## View相关

* LayoutInflater.inflate()
  * 函数签名：`public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)`
  * `@LayoutRes int resource`：想要初始化创造的View
  * `@Nullable ViewGroup root`
    * `attachToRoot = true`：调用`root.addView(view)`
    * `@Nullable`注解说明可以为空
* view操作必须在动画完成后
  * 不能对View进行操作，不然当最后的动画`onAnimationEnd()`调用时，导致预期和实际差异巨大
  * 要对View进行操作一定要停止动画`animator.clear()`或者`animator.end()`

***

## ViewGroup相关

* `ViewGroup`

  * `ViewGroup.addView(View child)`：向一个`ViewGroup`中添加`View`
  * `ViewGroup.removeAllViews()`：删除所有View
  * `ViewGroup.getChildCount()`：获取child个数

* `LinearLayout`

  * 是`ViewGroup`
  * `xml`里面定义的是编译期`childen`
  * 运行时可以调用父类`ViewGroup`的方法进行修改

* LayoutParams

  * `View.getLayoutParams()`：每个View都有这个方法，既每个View都有`mLayoutParams`属性
  * `ViewGroup.LayoutParams`决定了父容器怎么arrange这个View
  * `LayoutParams`有很多子类，如`LinearLayout.LayoutParams`、`FrameLayout.LayoutParams`、`RelativeLayout.LayoutParams`。不同的子类对应父容器不同，即如果父容器是`FrameLayout`则子View的`mLayoutParams`应该是`FrameLayout.LayoutParams`

* 动态修改margin

  ```kotlin
  (mApproveBtn.layoutParams as? LinearLayout.LayoutParams)?.apply {
      leftMargin = UIUtils.dip2Px(mItemView.context, 8F).toInt()
      mApproveBtn.layoutParams = this
  }
  ```

* FrameLayout

  * `xml`中最上面的是栈底，最下面最后写的是栈顶

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools"
      android:layout_width="match_parent"
      android:layout_height="match_parent">
  
    <!--在栈底会被掩盖-->
    <View
          android:layout_width="match_parent"
          android:layout_height="106dp" />
  	<!--在栈顶不会被掩盖-->
    <View
          android:layout_width="match_parent"
          android:layout_height="106dp" />
  
  </FrameLayout>
  ```

* RelativeLayout

  * 和`FrameLayout`一样，支持栈式View重叠。最下面写的是栈顶，最上面写的是堆栈底

* `merge`标签

  ```kotlin
  //建三个标准构造函数，直接inflate并且最后一个参数为true
  class SelfDefineView: FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    
    init{
      LayoutInflater.from(context).inflate(R.layout.self_define_view, this, true)
    }
  }
  ```

  ```xml
  <!--首标签是merge-->
  <?xml version="1.0" encoding="utf-8"?>
  <merge xmlns:android="http://schemas.android.com/apk/res/android"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:fresco="http://schemas.android.com/apk/res-auto">
  
      <com.facebook.drawee.view.SimpleDraweeView
          android:id="@+id/bg_view"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          app:actualImageScaleType="centerCrop"
          app:actualImageResource="@drawable/back_ground"/>
  
  </merge>`
  ```

***

## NDK与so

* JNI简介
  * `java`劣势
    * 虽然`java`号称是跨平台，但那是牺牲一部分运行效率换来的，真正的跨平台非`C/C++`莫属，因为当前世上90%的系统都是基于`C/C++`编写的
  * `C++`优势
    * 系统几乎都是由`C/C++`编写而成
    * `C++`代码更加安全，因为`java`可能会被反编译
    * 一些核心的实现部分或者算法的实现，基于效率和安全考虑，使用`C++`来实现更优
  * 定义
    * `JNI`是一门技术，是`Java`与`C/C++`沟通的一门技术
* 定义
  * `NDK`(`Native Development Kit`缩写)是一种基于原生程序接口的软件开发工具包，可以让我们在`Android`中使用`C/C++`语言来实现应用程序的一部分
* 作用
  * 在`Android`中，`NDK`是一系列工具的集合，主要用于扩展`Android SDK`。`NDK`提供了一系列的工具可以帮助开发者快速的开发`C/C++`的动态库，并能自动将`so`和`Java`应用一起打包成`apk`
* 与`JNI`关系
  * `JNI`是想达到的目的，而`ndk`是达到这个目的的一种工具
  * `NDK`只是构建`so`链接文件的其中一种工具，还可以使用其他的工具如`make,ninja`等等
* `so`动态链接库体积优化手段
  * [美团文章](https://mp.weixin.qq.com/s/7YVuouHAq2OfrowhoHVmnQ)

* 几种方式
  * 精简动态符号表
    * 原理：对外暴露的必要的符号就行，越少越好

  * 移除无用代码
    * 方式：使用`LTO`扩大编译优化

  * 优化指令长度
    * 方式：使用`clang`编译器的优化级别达到最少量的体积

  * 其他措施
    * 禁用`C++异常`、`RTTI`
    * 合并`so`


***

## android多进程

* 现状：很多Android应用并不是只有一个进程，高级的安卓应用一般都是多进程
* 优点
  * 安全
    * `WebView`，图片加载等较危险逻辑放在一个进程。这样出现意外这个进程闪退挂掉不会影响主进程
    * 微信小程序是第三方开发，它单独运行在一个进程之中。一旦挂掉不会影响微信本身

  * 大内存
    * 一个进程能够申请的内存是有一定限度的，多进程有助于申请更大的内存

***

## android打包

* [Android应用程序资源的编译和打包过程分析](https://blog.csdn.net/luoshengyang/article/details/8744683)

* 名词解释

  * `aapt` - `Android Asset Packaging Tool`

* 概要

  * 除了`assets`(实际日常根本没看到)和`res/raw`(实际日常也没看到)外，其余资源都会被编译或处理，`png、jpg`的`drawable`都有可能会被处理和压缩。那两个不会被处理而是原封不动打进apk
  * 除了`assets`外其他都有一个`id`。`res/raw`下会原封不动进apk但还是有`id`
  * `aapt`处理完成最后会生成一个资源索引表-`resources.arsc`和资源ID常量类`R.java`
  * 所有的`xml`都会被编译成`二进制的xml`，然后打入`apk`
    * 体积小 - 所有标签和值都收到字符串常量池并去重
    * 解析快 - 不用解析字符串，而是解析`id`
  * 应用程序运行时通过`AssetManager`、`资源id`、`文件名`来访问资源。其中`资源id`最常用

* 打包三最重要过程

  * `xml`资源的编译
  * 资源id文件`R.java`的生成
  * 资源索引文件`resources.arsc`的生成

* 资源id结构

  * 最高字节 - `package id`。系统资源是`0x01`，应用程序自身是`0x7f`。我们引用的`android:orientation、vertical`等就是在系统资源`0x01`中
  * 次高字节 - `type id`。如`animator、anim、color、drawable、layout`
  * 最低两字节 - `entry id`

* 打包流程

  * 解析AndroidManifest.xml - 获取包名

  * 添加被引用包资源 - 比如添加`package id `为`0x01`的系统资源包进来

  * 收集资源文件 - 收集当前需要编译的资源文件保存到一个对象中

  * 将收集到的资源(不包括value)增加到资源表 - 将上一步收集到增加到另一个对象中，这个对象最终会生成`resources.arsc`

  * 编译`values`类资源 - 将`values`都编成很多item

  * 给Bag资源分配id

  * 编译`xml`文件

    * 解析`xml`文件 - 将文件解析成内存中的树
    * 赋予标签和值资源id - `@+[package:]id/button_text`，其中`@`表示引用类型，`+`表示如果没有就新建，`package`表示包名(如无则是当前包)
    * 压平`xml` - 从文本格式的文件转变成二进制格式的文件

  * 生成资源符号 - 为生成`R.java`做准备

  * 生成资源索引表 - 生成`resources.arsc`

  * 编译`AndroidManifest.xml` - 之所以后面编是因为要用到前面编译`xml`的值

  * 生成`R.java`文件

    ```java
    public final class R {
        public static final class layout {
            public static final int main=0x7f030000;
            public static final int sub=0x7f030001;
        }
    }
    ```

  * 打包apk

* 资源加载流程

  * 资源的load过程都是从`ActivityThread -> ContextImpl -> AssetManager -> native AssetManager`

* 资源寻找过程

  * 首先通过`R.java`中的`id`，最终`java`层会调到`AssetManager`的一个方法，然后嵌入到`native`层，通过缓存和各种锁机制读取`resoureces.arsc`文件保存到`Asset`对象中，最后加载文件到内存中

***

## SmartRouter

* 基本实现原理
  * 将所有的`url -> page`映射存放在一个`map`仓库内，需要跳转的时候就进行查找然后跳转
* 优势
  * 极大地降低耦合
  * 多端`iOS、Android、web`可以统一协议
* 协议制定
  * `<schema>://<host>:<authority>/<path>?<query>`
  * `schema -> 产品`，`host -> 模块`，`authority -> 权限`，`path -> 页面`，`query -> 参数`
* 实现流程
  * 写代码时在类上添加自定义注解
  * 编译`.kotlin`文件时注解处理器处理注解，生成一个类名相关的map
  * 所有`.class`文件编译完成后，使用`gradle`的`transform`能力接管所有`.class`，遍历找出所有的类名相关map合成一个大map(整体过程类似于`R.java`类的生成)
* 运行时过程
  * 指定url调用跳转方法
  * 从大路由表仓库找到目标页
  * 进行跳转
* 拦截器 - 类似于`ViewGroup onInterceptTouchEvent`
* 参数传递 - 底层还是使用的`Intent`，将url的quecy参数映射到`Intent`里面就行

***

## Overlay

* see this link [ViewOverlay: When, How and for What Purpose?](http://old.flavienlaurent.com/blog/2013/08/14/viewoverlay-when-how-and-for-what-purpose/)

***

## Parcel

* what is ?

  * A `Parcel` is essentially a flattened buffer of bytes that contains a serialized version of Java objects and primitive types. It is used for marshalling and unmarshalling data across process boundaries, which is necessary for Inter-Process Communication (IPC) in Android.

* Parcel and Bundle

  * A `Parcel` is used for IPC between different processes.
  * A `Bundle` is used for passing data between different components within a single process.
  * Bundle implements `Parcelable` interface, so itself can be transfered across processed by `Parcel#writeBundle()`

* code

  * `User.java`

    ```kotlin
    class User: Parcelable {
      private var Long uid;
    	private var String userName;
      private var Boolean isMale;
    
      override fun describeContents(): Int = 0
    
      override fun writeToParcel(out: Parcel, flags: Int) {
      	out.writeLong(uid) // 写入Parcel
        out.writeString(userName)
        out.writeBoolean(isMale)
      }
    
      companion object CREATOR: Parcelable.Creator<User?> {
      	override fun createFromParcel(in: Parcel): User? {
          // 从Parcel读取
          return User().apply {
          	uid = in.readLong()
            userName = in.readString()
            isMale = in.readBoolean()
          }
      	}
    
      	override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
     	}
    }
    ```

  * subclass

    ```java
    public final class Bitmap implements Parcelable
    public class Intent implements Parcelable
    public final class Bundle implements Parcelable
    ```

* How to IPC

  * Parcel object will finally be copied to kernel driver space (`/dev/binder`).
  * use `ioctl()` system call to read and write for `/dev/binder`

* supported types

  * Binder object

    ```java
    public final void writeStrongBinder(IBinder val) {
      nativeWriteStrongBinder(mNativePtr, val);
    }
    ```

  * primitive data types

  * Parcelables which implement `Parcelable` interface

* use case

  * `protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags)`

***

## ProcessState

* both exits

  * there is a `ProcessState.java` class in Android Java framework
  * there is also a `ProcessState.cpp` class in Android Native Development Kit

* ProcessState.java

  * It is used by the Java runtime environment to track the memory usage of Java processes on the Android platform. It provides an interface for monitoring the memory usage of a Java process, determining when it is under memory pressure, and making decisions about which components of the process should be prioritized or deprioritized when memory is scarce. The Java `ProcessState` class is not intended to be used directly by applications, but rather is used by the Android system and its various components and services, such as the ActivityManagerService

* ProcessState.cpp

  * It is used by the Android Native runtime environment to track the memory usage of native C and C++ processes on the Android platform. It provides a low-level interface for managing the memory usage of native processes, including tracking the memory usage of individual components of a process, such as activity stacks and services. The C++ `ProcessState` class is also not intended to be used directly by applications, but is used extensively by the Android system and its various components and services.

* Binder

  * `ProcessState` is responsible for initializing and managing the `Binder` IPC mechanism within a process.
  * When a process starts, `ProcessState` creates a new instance of the `ProcessState` class, which is responsible for managing the memory usage of the process, as well as initializing and managing the `Binder` mechanism.
  * The `ProcessState` class opens the `/dev/binder` device.
  * When a process needs to communicate with another process using a `Binder`, it uses the `ProcessState` class to initialize the `Binder` mechanism and open a channel to the remote process through the `/dev/binder` device.

* code

  ```cpp
  ProcessState::ProcessState()
      : mDriverFD(open_driver()) //打开 binder 设备
      , mVMStart(MAP_FAILED) //初始化为 MAP_FAILED，映射成功后会变更
      , mThreadCountLock(PTHREAD_MUTEX_INITIALIZER)
      , mThreadCountDecrement(PTHREAD_COND_INITIALIZER)
      , mExecutingThreadsCount(0)
      , mMaxThreads(DEFAULT_MAX_BINDER_THREADS) //binder 线程最大数量
      , mStarvationStartTimeMs(0)
      , mManagesContexts(false)
      , mBinderContextCheckFunc(NULL)
      , mBinderContextUserData(NULL)
      , mThreadPoolStarted(false)
      , mThreadPoolSeq(1){
         if (mDriverFD >= 0) { //已经成功打开 binder 驱动设备
             // 将应用进程一块虚拟内存空间与 binder 驱动映射，在此内存块上进行数据通信
             mVMStart = mmap(0, BINDER_VM_SIZE, PROT_READ, MAP_PRIVATE | MAP_NORESERVE, mDriverFD, 0);
             if (mVMStart == MAP_FAILED) { //映射失败处理
                 ALOGE("Using /dev/binder failed: unable to mmap transaction memory.\n");
                 close(mDriverFD);
                 mDriverFD = -1;
             }
         }
  }
  ```

***

## IPCThreadState

* what is?

  * `IPCThreadState` is a low-level system class that is used to manage the `Binder` IPC mechanism for a thread. It provides an interface for communicating with the `Binder` kernel driver and is primarily used by system-level background threads and services.

* how it works?

  * `IPCThreadState` is a system-level class in the Android OS framework that is responsible for managing the `Binder` IPC mechanism for a thread.
  * When a new thread is created by the system, an instance of `IPCThreadState` is automatically created for that thread, which is used to manage all `Binder` communication on that thread.
  * `IPCThreadState` provides an interface for receiving incoming `Binder` transactions and dispatching them to the appropriate handler threads or services.
  * `IPCThreadState` is used by various system-level components and services, such as the `ActivityManagerService`, `PackageManagerService`, and `PowerManagerService`, to communicate with each other using the `Binder` mechanism.
  * `IPCThreadState` is an internal system class and is not intended to be used directly by applications.

* features

  * It is in Android Native Development Kit.

* code

  ```java
  // 真正的transact执行的地方
  status_t IPCThreadState::transact(int32_t handle,
                                    uint32_t code, const Parcel& data,
                                    Parcel* reply, uint32_t flags){
      //将数据打包塞到 mOut 里
      err = writeTransactionData(BC_TRANSACTION, flags, handle, code, data, NULL);
      err = waitForResponse(&fakeReply);
      return err;
  }
  
  status_t IPCThreadState::waitForResponse(Parcel *reply, status_t *acquireResult){
      uint32_t cmd;
      int32_t err;
      while (1) {
        	//执行ioctl
          cmd = (uint32_t)mIn.readInt32(); //拿到 binder 驱动发过来的命令
          switch (cmd) {
              //处理命令
              case BR_TRANSACTION_COMPLETE:{...}
              case BR_DEAD_REPLY:{...}
              case BR_FAILED_REPLY:{...}
              case BR_ACQUIRE_RESULT:{...}
              case BR_REPLY:{...}
              default:
                  //其他命令在 executeCommand 方法中处理
                  err = executeCommand(cmd);
              }
      }
      return err;
  }
  ```


***

## SPI

* what is? - Service provider interface(SPI) is an API intended to be implemented or extended by a third party. It can be used to enable framework extension and replaceable components
* how to implement it
  1. use an annotation to mark a class as interface, and then use annother annotation to mark a class as implementation.
  2. collect mapping relationships between them when compiling, it may involve annotation processing and asm
  3. register all mapping information to a data center.
  4. try get an implemention from data cener with an interface `class::java` at runtime.
* features
  * there is a cache when calling `findService` method, so it doesn't create impl instances all the time, but it may lead to memory leaking.
  * it doesn't need reflection to create an implementation class at runtime, as the implementation is in the host. while retrofit generates the impl class at runtime with asm.
  * it firstly needs to load a plugin when it tries to find a service impl which is in a plugin.

***
