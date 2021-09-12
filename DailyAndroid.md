# 日常Android学习

## Eventbus

> ### 概述
>
> * 就是一个耦合度极低的观察者模式的框架
>
> ### 引入依赖
>
> * `implementation 'org.greenrobot:eventbus:3.2.0'`
>
> ### 定义一个消息类
>
> * 消息类的作用就是一个消息
>
> ```kotlin
> data class MessageEvent(val msg:String)
> ```
>
> ### 定义观察者Observer的处理事件方法
>
> ```kotlin
> @Subscribe(threadMode = ThreadMode.BACKGROUND)
> fun subscribe(msg:MessageEvent){  
>     Log.d("MainActivity","receive a message")  
>     binding.buttonFirst.text = msg.msg  
>     Toast.makeText(activity,"click one",Toast.LENGTH_LONG).show()
> }
> ```
>
> ### 四种threadMode模式
>
> * `POSTING`：在被观察者(发送消息那个)的线程执行
> * `Main`：观察者方法在`Main`(UI线程)执行
>   * 如果被观察者是在`Main`线程发出post。那么观察者立即执行，导致被观察者被阻塞
>   * 如果被观察者不在`Main`线程发出post，那么所有post构成一个队列，依次执行，被观察者不会被阻塞
> * `MAIN_ORDERED`：post总是在一个队列里，被观察者永远不会被阻塞
> * `BACKGROUND`
>   * 如果被观察者是在`Main`线程发出post。那么任务被**队列化**安排到一条固定的`Backgroud`线程执行，有可能会阻塞`backgroud`线程
>   * 如果被观察者不是在`Main`线程发出post。那么任务队列就直接在发出post的那条线程执行
> * `ASYNC`：既不在`Main`线程执行，也不在被观察者的post线程执行。EventBus有一个线程池
>
> ### 注册与解注册
>
> ```kotlin
> override fun onStart() {  
>   super.onStart()  
>   EventBus.getDefault().register(this)
> }
> override fun onStop() {  
>   super.onStop()  
>   EventBus.getDefault().unregister(this)
> }
> ```
>
> * 使用的是默认的`EventBus`对象
>
> ### 被观察者发送信息
>
> * `EventBus.getDefault().post(Message("一个发出的消息"))`
>
> ***

## LifeCycleOwner

> ### 概述
>
> * 本质上还是一个观察者模式
> * `Observable`是本身拥有生命周期的`Activity`、`Fragment`
> * `Observer`是自定义的
>
> ### 定义`Observer`
>
> ```kotlin
> class MyLifecycleObserver:LifecycleObserver {    
>   @OnLifecycleEvent(Lifecycle.Event.ON_START)    
>   fun onStart(){        
>     Log.d("LifecycleObserver","onStart")    
>   }    
>   @OnLifecycleEvent(Lifecycle.Event.ON_STOP)    
>   fun onStop(){        
>     Log.d("LifecycleObserver","onStop")    
>   }    
>   @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)    
>   fun onDestroy(){        
>     Log.d("LifecycleObserver","onDestroy")    
>   }
> }
> ```
>
> * `LifecycleObserver`接口没有任何方法
> * 使用`@OnLifecycleEvent`注解
>
> ### `Observable`订阅`Observer`
>
> ```kotlin
> class FirstFragment : Fragment() {    
>   private lateinit var myLifecycleObserver: MyLifecycleObserver    
>   override fun onCreate(savedInstanceState: Bundle?) {        
>     super.onCreate(savedInstanceState)        
>     myLifecycleObserver = MyLifecycleObserver(this)        
>     lifecycle.addObserver(myLifecycleObserver)    
>   }
> }
> ```
>
> ### 注
>
> * Android手动杀死进程。依旧会执行`onStop()、onDestroy()`方法
>
> ***

## LiveData

> ### 概述
>
> * 也是一种观察者模式
>
> ### 特点
>
> * `Observer`只能是`LifecycleOwner`，即一般就只能是`Activity、Fragment`。必须有`start、resume、stop`等方法
> * `Observable`必须对应接口的实现`onChanged()`方法
> * 不用手动处理生命周期，默认方式封装了只会在活跃生命周期内观察
> * 如果在不正常生命周期漏观察了变化，则在进入正常生命周期时刻会立即更新
> * 总是就是很好用很方便
>
> ### 观察
>
> ```java
> public void observe(LifecycleOwner owner, Observer<? super T> observer) {}
> ```
>
> ***

## Activity

> ### 概述
>
> * 代表安卓应用中的 **一个屏幕** ，不同的屏幕对应不同的`Activity`，比如电子邮件列表屏幕、电子邮件编辑屏幕
> * `Activity`的存在支持了每次调用应用不是一定从一个固定的屏幕开始(也就是通常所说的主函数)。比如浏览器点击发送邮件按钮应该从编辑邮件按钮界面开始，而不是从一般的邮件列表开始
> * `Activity`提供窗口让应用绘制界面。窗口可能铺满实际物理屏幕，也可能比实际物理屏幕小
> * `Acitivity`之间的依赖耦合很小
>
> ### Intent过滤器
>
> * 当系统询问界面询问使用那个应用来执行接下来的操作时，就是 **隐式Intent** 在起作用
>
> ### Activity权限
>
> * 给`Activity`单独声明权限
>
> ```xml
> <manifest>
>   <activity android:name="...."	
>             android:permission=”com.google.socialapp.permission.SHARE_POST”/>
> </manifest>
> ```
>
> * `父Activity`的权限必须是`子Activty`的真子集时，父才能启动子
>
> ### OnSaveInstanceState()
>
> * 用户自己退出时不会调用
> * 只有因为系统资源紧张，系统自动把它清除掉或者其他原因才会调用此方法
> * 此方法的对应回调在`fun onCreate(savedInstanceState: Bundle?)`中
>
> ### A切换到B的执行顺序
>
> * `A.onPause()`、`B.onCreate()`、`B.onStart()`、`B.onResume()`、`A.onStop()`
> * 两者生命周期是有重叠的
>
> ### 返回键
>
> * `onBackPressed()`
>
> ### 健壮的Activity
>
> * 一个合格稳定的Activity，一定要在意外的情况下也能逻辑正确
> * 其他应用(电话)阻断了此Activity
> * 系统自动回收销毁又创建此Activity
> * 将此Activity放在新的窗口环境中，如画中画、多窗口环境等
>
> ***

## Android系统架构开篇

> ### 架构层
>
> * app
> * java api
> * native、android runtime
> * hal 
> * kernel
>
> ### 调用
>
> * api 和 native C++通过JNI调用
> * native 和 kernel之间通过System call 调用
>
> ### 通信方式
>
> * `Binder`：进程间通信，CS架构
> * `Socket`：主要用于framework层和native层之间的通信
> * `Handler`：同进程的线程间的通信
>
> ***

## Handler

> ### 四个组成
>
> * `Handler`：`post messages and handle messages`
> * `MessageQueue`：`a container of messages`
> * `Looper`：`get messages from MessageQueue`
> * `Message`：`an intermediary and carry real data`
>
> ### 成员及方法信息
>
> * Handler
>
>   * 成员变量
>
>     * `MessageQueue mQueue`
>     * `Looper mLooper`
>
>   * 成员方法
>     * `public void dispatchMessage(@NonNull Message msg)`
>
>       ```java
>       public void dispatchMessage(@NonNull Message msg) {
>         if (msg.callback != null) {
>           handleCallback(msg);//如果message有callback就执行callback
>         } else {
>           if (mCallback != null) {
>             if (mCallback.handleMessage(msg)) { //如果handler自身的mCallback不为空就执行自身的mCallback
>               return;
>             }
>           }
>           handleMessage(msg); //都为空就兜底执行自己的handleMessage()方法，这个方法一般子类自己实现
>         }
>       }
>       ```
>
>     * `public final boolean post(@NonNull Runnable r)`：表面post一个`Runnable`，实际还是会被包装成一个`Message`
>
>     * `public final boolean sendMessage(@NonNull Message msg)`：send一个message
>
>     * `private boolean enqueueMessage(MessageQueue queue, Message msg,long uptimeMillis)`：上面两个方法都会执行它，向链表里面插入消息
>
> * MessageQueue(是一个单链表)
>   * 成员方法
>     * `bool enqueueMessage(Message msg, long when)`
>     * `Message next()`
>
> * Message
>   * 成员变量
>     * `Handler target`：所属的Hanlder
>     * `Object obj`：message携带的数据内容
>   * 成员方法
>     * `public void sendToTarget()`：把这个消息让自己的成员`Handler target`给`post`出去
>   
> * Looper
>   
>   * 静态方法
>     * `private static void prepare(boolean quitAllowed)`：`new and set`当前线程的`looper`，`new and set`当前线程的`MessageQueue`
>     * `public static Looper getMainLooper()`：获取`UI线程`的`looper`
>     * `public static void loop()`：把当前线程的`MessageQueue`跑起来，每个`message`执行`msg.target.dispatchMessage(msg);`
>   * 变量
>     * `MessageQueue mQueue`
>     * `static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();`
>     * `Thread mThread`
>
> ### Looper
>
> * `static prepare()`：给当前线程的threadLocal初始化set一个新的Looper
> * `private Looper()`：给此Looper初始化set一个MessageQueue，因为Looper和线程绑定，传递性可知MessageQueue和线程绑定
> * `statice loop()`：获取当前线程的looper对象，获取当前线程的MessageQueue对象，开始loop
>
> ### Handler
>
> * `Handler()`：绑定`mLooper`，绑定`mQueue`
> * `dispatchMessage(message:Message)`：链式调用，首先`message.callback.run()`，接着`Handler.mCallback.handleMessage(msg)`，最后`Handler.handleMessage(msg)`方法
> * `post(r:Runnalbe)`：把Runnable封装成`Message`丢进MessageQueue
>
> ### Message
>
> * `Handler target`：消息的响应方
> * `Runnable callback`：消息的回调方
>
> ### 消息池
>
> * 消息`Message`是可以进行复用的
>
> ### 总结
>
> * Handler最对外
> * Looper、MessageQueue和线程ThreadLocal绑定
> * Handler发送和分派消息，Looper不断入队消息和出队消息
>
> ### 数量关系
>
> * 一个线程最多一个`Looper`，是由`public static void prepare()`决定的
> * 一个线程最多一个`MessageQueue`，是由`private Looper(boolean quitAllowed)`决定的
> * 一个线程可以有很多个`Handler`，但多个`Handler`绑定的是同一个`Looper`和`MessageQueue`
> * 如何区分不同的消息？
>
> ```java
> //Handler.java
> private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
>   msg.target = this; //每个消息都带上抛自己的handler
>   return queue.enqueueMessage(msg, uptimeMillis);
> }
> ```
>
> ```java
> //Looper.java
> public static void loop() {
>   for (;;) {
>     Message msg = queue.next(); // might block
>     try {
>       msg.target.dispatchMessage(msg);//由每个消息自己的handler处理消息
>     }
>   }
> }
> ```
>
> ***

## MVP、Handler实例

> ### FatherModel.java
>
> ```java
> class FatherModel<T> implements IHandler{
> protected void handleData(T data) {
>     //无实现，子类overwrite它
> }
> 
> //重写WeakHandler的handleMsg方法
> @Overrite
> public void handleMsg(Message msg){
>     //调用自己的handleData方法，处理的数据从Message来。msg.obj就是Resbonse
>     handleData(msg.obj);
>     //成功了调用listner的onSuccess()方法。此处listner为Presenter
>     listener.onSuccess();
> }
> }
> ```
>
> ### SonModel.kt
>
> ```java
> class SonModel extend FatherModel{
> 	private fun fetchData(){
>     use a handler to commit a Runnable
> }
> 	override fun handleData(response: Response?){
>     //重写父类的handleData
> }
> }
> ```
>
> ### FatherPresenter.java
>
> ```java
> class FatherPresenter{
> 	//在父类的构造方法处将Presenter作为观察者，Model作为被观察者
> 	public void bindMyModel(Type myModel) {
>     this.mModel = myModel;
>     this.mModel.addNotifyListener(this);
> }
> }
> ```
>
> ### SonPresenter.java
>
> ```java
> class SonPresenter extend FatherPresenter{
> 	//成功的方法。SonPresenter作为观察者，这是观察者的一个回调方法
> 	override fun onSuccess(){
>     //成功调用View的doSuccess()方法
>     mView.doSuccess()
> 	}
> }
> ```
>
> ### 方法调用顺序
>
> * `View.getData()`
> * `SonPresenter.getData()`
> * `SonModel.getData()`
> * `SonModel.commitARunnalbe()`
> * 用其他线程异步执行网络请求
> * 请求返回后`mMessage.obj = obj; mMessage.sendToTarget();`向UI线程抛一个Message
> * `成功获取到数据,但过程是使用handler模式获得的，在message里面`
> * `FatherModel.handleMsg()`
>   * 这一步是handler机制决定的，且方法名都不能改
>   * 其中设计dispatch机制
> * `FantherModel.handleData()`
>   * 此步不执行，由于动态分派规则让子类执行
> * `SonModel.handleData()`
> * `SonPresenter.onSuccess()`
>   * 这是观察者模式决定的，SonPresenter被添加为了Lisnter
> * `View.doOnSuccess()`
>
> ### handler部分详解
>
> * 创建一个`Runnable`，里面定义好想执行的操作
>
> ```java
> new Callable() {
>   @Override
>   public Object call() throws Exception {
>     return myApi.getData();//这是一个网络请求
>   }
> }
> ```
>
> * 封装另外一个`Runnable`
>
> ```java
> new Callable() {
>   @Override
>   public Object call() throws Exception {
>     Object obj = mCallable.call();//这个mCallable是上面的Runnable，执行会耗时
>     if (mMessage != null) {
>       mMessage.obj = obj; //obj是网络返回的数据
>       mMessage.sendToTarget(); //这一行是调用的handler.sendMessage()
>     }
>   }
> }
> ```
>
> * 异步执行
>
> ```java
> mExecutor.execute(Runnable runnable)//异步执行上面那个Runnable
> ```
>
> ***

## Binder

> ### 重要性
>
> * 四大组件底层的通信都依赖 Binder IPC
>
> ### 原理
>
> * 进程之间的用户空间是不共享的，一般为3G
> * 进程之间的内核空间是共享的，一般为1G
>
> ### Binder原理
>
> * 应用层
>   * Client，Server之间可以**间接**通信
> * Native C++层
>   * Client向ServiceManager申请获取服务
>   * Server向ServiceManager申请注册服务
> * 内核空间
>   * Binder驱动设备(/dev/binder)
>
> ### 总结
>
> * 应用层的Client和Server之间不能直接交互，必须通过ServiceManager间接交互
> * Binder驱动位于内核空间，而Client、Server、ServiceManager位于用户空间
> * Binder和ServiceManager是Android平台的基础架构
> * 开发人员只用自定义实现Client和Server即可实现通信
>
> ### Linux进程通信方式
>
> * `管道`：缓冲区大小比较小，且消息需要复制两次
> * `消息队列`：复制两次
> * `共享内存`：复制零次，效率高。但要自己处理同步问题
> * `套接字`：更通用的接口，但效率低。只适用于不同机器不同网络
> * `信号量`：主要作为一种锁机制，用于进程同步
> * `信号量`：主要用于杀死进程等操作
>
> ### 为什么使用Binder
>
> * `性能`：binder只需要复制一次，性能仅次于共享内存
> * `稳定性`：CS架构比较稳定
> * `安全性`：Linux通信方式在内核态无任何保护措施，完全只看效率。Binder通信可以获得可靠的uid/pid
> * `语言角度`：Binder机制是面向对象的。一个Binder对象在各个进程中都可以有引用
> * `Google战略`：Google让GPL协议止步于Linux内核空间，而binder是实现在用户空间的
>
> ### 复制一次
>
> * 发送进程将数据从用户空间拷贝到内核空间。即一次复制
> * 由于内核缓冲空间和接收进程的用户空间存在内存映射关系，即不用复制就可以直接读取到数据。即零次复制
> * 总就只复制了一次
>
> ### 继承关系
>
> * `Java framework`：作为Server端继承(或间接继承)于Binder类，Client端继承(或间接继承)于BinderProxy类
> * `Native Framework`：这是C++层，作为Server端继承(或间接继承)于BBinder类，Client端继承(或间接继承)于BpBinder
>
> ### 总
>
> * `无Binder不Android`
>
> ***

## ConstrainLayout属性

> ### layout_constraintHorizontal_bias
>
> * 设左右约束布局分别为`left`和`rigth`
> * `bias` = `left` / `(left + right)`
>
> ### layout_constraintHorizontal_chainStyle
>
> * `spread`：左边界、中间、右边界平分所有空间
> * `spread_inside`: 只有中间的平分
> * `packed`:左边界、右边界平分。中间全部聚拢
>
> ***

## .9.png

> ### left
>
> * 决定上下缩放那些区域可以缩放
>
> ### top
>
> * 决定左右缩放那些区域可以缩放
>
> ### right
>
> * 决定上下那些区域可以放置实际内容
>
> ### bottom
>
> * 决定左右那些区域可以放置实际内容
>
> ***

## RxJava

> ### 响应式函数编程
>
> * `命令式编程`：是面向计算机硬件的抽象，有变量、赋值语句、表达式和控制语句
> * `函数式编程`：是面向数学的抽象，将计算描述为一种表达式求值，函数可以在任何地方定义，并且可以对函数进行组合
> * `响应式编程`：是一种面向数据流和变化传播的编程范式，数据的更新是关联的
>
> ### 四个角色
>
> * `Observable`：`produce event`
> * `Observer`：`consume event`
> * `Subscribe`：`a connection between observalbe and observer`
> * `Event`：`carry message`
>
> ### 简略过程
>
> * 创建`Observable`并产生事件
> * 创建`Observer`定义消费事件行为
> * 通过`Subscrib`连接`observable`和`Observer`
>
> ### 创建Observable
>
> * `Observable`的创建是收敛的
> * 无论是`create()`、`just()`、`from()`还是其他什么，都会收敛到传入一个`Observable.onSubscrib(){}`接口的实现对象
> * `Observable.java` - 被观察者
>
> ```java
> public class Observable<T> {
> 		//成员变量
>     final OnSubscribe<T> onSubscribe;
> 
> 		//唯一的构造函数，需要传入一个接口对象
>     protected Observable(OnSubscribe<T> f) {
>         this.onSubscribe = f;
>     }
> 
>     //接口定义，实际就是一个Action1, 参数是Subscriber
>     public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
>         // cover for generics insanity
>     }
> }
> ```
>
> * `Action1.java`
>
> ```java
> public interface Action1<T> extends Action {
>     void call(T t);
> }
> ```
>
> * `Subscriber.java` - 观察者
>
> ```java
> public abstract class Subscriber<T> implements Observer<T>, Subscription {
>     //...
> }
> ```
>
> * `Observer.java` - 观察者
>
> ```java
> public interface Observer<T> {
>     void onCompleted();
>     void onError(Throwable e);
>     void onNext(T t);
> }
> ```
>
> * `Observerable.subscribe()`方法
>
> ```java
> static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
>         // new Subscriber so onStart it
>         subscriber.onStart();
> 
>     	//最后通过Subscriber.java里的接口call()方法开始调用
>         RxJavaHooks.onObservableStart(observable, observable.onSubscribe).call(subscriber);
>         return RxJavaHooks.onObservableReturn(subscriber);
> }
> 
> 
> //OnSubscribeFromArray.java中一个call方法的实现
> public void call(Subscriber<? super T> child) {
>     child.setProducer(new FromArrayProducer<T>(child, array));
> }
> ```
>
> ### 绑定时机性
>
> * 真正开始观察与被观察一定是在`observeralbe.subscible()`后才开始
>
> ### 队列性
>
> * `Observable`发射的是一个一串事件，而不是一个事件。整串事件被抽象成一个队列
> * 事件流未开始时观察者调用`onSubscribe()`
> * 事件流中每一个事件观察者调用`onNext()`
> * 事件流发生错误时观察者调用`onError()`
> * 事件流结束时观察者调用`onComplete()`
> * 互斥性：`onError()`和`onComplete()`是互斥的。两者之一必被调用一次
>
> ### Action
>
> * 定义
>
> ```java
> public interface Action2<T1, T2> extends Action {
>   void call(T1, T2);
> }
> ```
>
> * 不同就是泛型的个数不同
>
> ### 不完全回调
>
> * 定义
>
> ```java
> public Disposable subscribe(Consumer onNext, Consumer onError,Action onComplete, Consumer onSubscribe){};
> ```
>
> ### 核心方法
>
> * `observable.subscribe(observer);`或者`observable.subscribe(subscriber)；`
> * 即相当于`addObserver(observer)`
>
> ### 两个方法
>
> * `subscribeOn(final Scheduler scheduler)`：`specify the Scheduler on which an Observable will operate`
> * `observeOn(final Scheduler scheduler)`：`specify the Scheduler on which an observer will observe this Observable`
>
> ### zip
>
> * `public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> source1, ObservableSource<? extends T2> source2,BiFunction<? super T1, ? super T2, ? extends R> zipper){ ... }`
>
> ***

## RecyclerView优化

> ### ItemView生命周期
>
> * `create`：主要对应`onCreateViewHolder()`
> * `bind`：主要对应`onBindViewHolder()`
> * `addView`
> * `measure`
> * `layout`
> * `draw`
>
> ### onCreatViewHolder
>
> * 只有在`RecyclerView`的多级缓存被击穿时才会调用，一旦调用，卡帧就特别明显
> * 可以采取异步线程调用UI线程获取方式，等于在多级缓存后面又加了一个自己的j异步View缓存池
>
> ### onBindViewHolder
>
> * 精简代码逻辑
> * 预加载
> * 异步
> * 延迟绑定
> * 复用
>
> ### onMeasure耗时优化
>
> * 最核心的思路是禁止`measure`两次
> * `mearsure`两次情形
>   * `RelativeLayout`
>   * `LinearLayout`使用了`weight`属性
>   * `ConstrainttLayout`设置`width = 0`，根据`toLeftOf、toRightOf`确定宽度
> * 禁止`RTL`布局
>
> ### addView、removeView
>
> * 没啥优化的点
> * 注意`View.onAttach()、View.onDetach()`方法调用
>
> ### 通用优化
>
> * 滑动期间规避全局`requestLayout`
>
> ***

## gradle

> ### 分类
>
> * Project的`build.gradle`
> * Module的`build.gradle`
>
> ### Project维度
>
> ```groovy
> // Top-level build file where you can add configuration options common to all sub-projects/modules.
> 
> buildscript {//dependencies to run gradle itself
>     
>     repositories { //config remote repositories
>         google()
>         jcenter()
>     }
>     dependencies { //config build tools
>         classpath 'com.android.tools.build:gradle:3.0.0'//此处是android的插件gradle，gradle是一个强大的项目构建工具
>       	// buildscript itself to use classpath
>         // NOTE: Do not place your application dependencies here; they belong
>         // in the individual module build.gradle files
>     }
> }
> 
> allprojects { //dependencies to this project
>     repositories {
>         google()
>         jcenter()
>     }
> }
> 
> // 运行gradle clean时，执行此处定义的task任务。
> // 该任务继承自Delete，删除根目录中的build目录。
> // 相当于执行Delete.delete(rootProject.buildDir)。
> // gradle使用groovy语言，调用method时可以不用加（）。
> task clean(type: Delete) {
>     delete rootProject.buildDir
> }
> ```
>
> ### classpath 和 implementation
>
> * `classpath`：`buildscript itself needs something to run, use classpath`
> * `implementation`：`your project needs something to run, use implementation`
>
> ### Moudule维度
>
> ```groovy
> apply plugin: 'com.android.application'
> apply plugin: 'kotlin-android-extensions'
> apply from: 'https://xxx.gradle'
> 
> android { //配置项目构建的各种属性
>     compileSdkVersion 27 //设置编译时用的Android版本
>     defaultConfig {
>         applicationId "com.whu.myapplication" //项目的包名
>         minSdkVersion 16 //项目最低兼容的版本,低于此无法安装
>         targetSdkVersion 27 //项目的目标版本，系统会为该应用启动一些对应该目标系统的最新功能特性。如22运行在Android 6上就不会开运行时权限
>         versionCode 1 //版本号
>         versionName "1.0" //版本名称，展示在应用市场上
>         flavorDimensions "versionCode"
>     }
>     buildTypes { // 指定生成安装文件的主要配置
>         release { // 生产环境
>             buildConfigField("boolean", "LOG_DEBUG", "false") //配置Log日志
>             buildConfigField("String", "URL_PERFIX", "\"https://release.cn/\"")// 配置URL前缀
>             minifyEnabled false //是否对代码进行混淆
>             proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro' //指定混淆的规则文件
>             signingConfig signingConfigs.release //设置签名信息
>             pseudoLocalesEnabled false //是否在APK中生成伪语言环境，帮助国际化的东西，一般使用的不多
>             zipAlignEnabled true //是否对APK包执行ZIP对齐优化，减小zip体积，增加运行效率
>             applicationIdSuffix 'test' //在applicationId 中添加了一个后缀，一般使用的不多
>             versionNameSuffix 'test' //在applicationId 中添加了一个后缀，一般使用的不多
>         }
>         //比如还可以打'大包'和'小包'
>     }
> 
>     packagingOptions{ //打包时的相关配置
>         //pickFirsts做用是 当有重复文件时 打包会报错 这样配置会使用第一个匹配的文件打包进入apk
>         //表示当apk中有重复的META-INF目录下有重复的LICENSE文件时  只用第一个 这样打包就不会报错
>         pickFirsts = ['META-INF/LICENSE']
> 
>         //merges合并 当出现重复文件时 合并重复的文件 然后打包入apk
>         //这个是有默认值得 merges = [] 这样会把默默认值去掉  所以我们用下面这种方式 在默认值后添加
>         merge 'META-INF/LICENSE'
> 
>         //重复依赖通过exclude去除重复的文件。
>         exclude 'META-INF/services/javax.annotation.processing.Processor'
>     }
> 
>     productFlavors { //多个渠道配置，为特定的渠道做部分特殊的处理，比如设置不同的包名、应用名等
>       	// 配置后打包出来默认命名格式如app-wandoujia-release-unsigned.apk
>         wandoujia {}
>         xiaomi {}
>         _360 {}
>     }
> 
>     productFlavors.all {
>             //批量修改，类似一个循序遍历
>         flavor -> flavor.manifestPlaceholders = [IFLYTEK_CHANNEL: name]
>     }
> 
>     
>     lintOptions { //程序在编译的时候会检查lint，有任何错误提示会停止build，我们可以关闭这个开关
>         abortOnError false //即使报错也不会停止打包
>         checkReleaseBuilds false //打包release版本的时候进行检测
>     }
> 
> }
> 
> dependencies { //定义此moudule的依赖关系
>     //项目的依赖关系
>     implementation fileTree(include: ['*.jar'], dir: 'libs')
>     //本地jar包依赖
>     implementation 'com.android.support:appcompat-v7:27.1.1'
>     //远程依赖
>     implementation 'com.android.support.constraint:constraint-layout:1.1.2'
>     testImplementation 'junit:junit:4.12'
>     //声明测试用例库
>     androidTestImplementation 'com.android.support.test:runner:1.0.2'
>     androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
> }
> ```
>
> ### apply
>
> * `apply plugin`：表示应用一个插件
> * `apply from : url` ：从URL引入插件
> * `apply plugin: 'com.android.application'`：表示打出来一个`apk`应用
> * `apply plugin: 'com.android.library'`：表示打出来一个`arr`文件，供别人使用
>
> ### dependencies{}
>
> * `implementation fileTree(include: ['*.jar'], dir: 'libs')`：一个本地依赖声明，表示将libs目录下所有.jar后缀的文件都添加到项目的构建路径当中
> * `implementation 'com.android.support:appcompat-v7:27.1.1'`：一个远程依赖
>
> ### api、implementation、compile
>
> * `api`：和`compile`一模一样，只是换了一个名字
> * `compile`：老的用法，已将废弃。引入的库整个项目都可以使用，容易导致重耦合
> * `implementation`：引入的库只有对应的Module能使用，其他Module不能使用
>
> ***

## 组件化

> ### 模块化
>
> * 实现模块化依然耦合严重
> * 模块化的下一步目标是组件化
>
> ### 优势
>
> * 每个模块可以作为独立的App存在
> * 模块间无直接的依赖
> * 基础组件作为业务组件的更低层
>
> ***

## 事件分发

> ### window链接
>
> * [Android全面解析之Window机制](https://juejin.cn/post/6888688477714841608)
>
> ### window概述
>
> * `window`是一个抽象概念
> * `window`是`view`的载体
> * `view`是`window`的表现
>
> ### view树
>
> * 一个`window`就是一颗`view`树
> * 一颗`view`树就是一个`window`
>
> ### Dialog等
>
> * 通过`windowManager`添加的`view`，与当前的`Activity`毫无关系
> * 它是另一个`window`，另一个`view`树
>
> ### type属性
>
> * 决定`window`即`view`树的深度信息，越高越靠前
> * 系统弹窗、Toast的type值就很高
>
> ### 添加window
>
> * 示例代码
>
> ```java
> //在Activity中执行下列代码
> Button button = new Button(this);
> WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
> // 这里对windowParam进行初始化
> windowParam.addFlags...
> // 获得应用PhoneWindow的WindowManager对象进行添加window
> getWindowManager.addView(button,windowParams);
> ```
>
> * `WindowManagerGloabal.addView()`最终执行
>
> ```java
> public void addView(...) {
>   //在此新建了一个ViewRootImpl
>   root = new ViewRootImpl(view.getContext(), display);
>   view.setLayoutParams(wparams);
>   mViews.add(view);
>   mRoots.add(root);
>   mParams.add(wparams);
>   
>   ...
>     
>   root.setView()
> }
> ```
>
> * `WindowManagerGloabal`是一个全局单例对象，管理所有`window`
> * `ViewRootImpl.setView()`会调用`WMS`去创建`window`
>
> ### 事件产生及传递
>
> * 硬件产生触动
> * 触动传递给`InputManagerService`
> * `InputeManagerService`通过`WindowManagerService`将触动传给对应的`window`
> * `window`将触动传送给对应的`ViewRootImpl`
> * `ViewRootImpl`将触动封装成`MotionEvent`对象，传递给下层`View`
>
> ### 事件传递
>
> * `ViewRootImpl`
>
> ```java
> class ViewRootImpl {
>   View mView;
>   public void setView(View view, ...) {
>     if (mView == null) {
>       mView = view;
>     }
>   }
> }
> ```
>
> * `ViewRootImple`向下传递
>
> ```java
> //ViewRootImpl.ViewPostImeInputStage
> final class ViewPostImeInputStage extends InputStage {
>   @Override
>   protected int onProcess(QueuedInputEvent q) {
>     //调用processPointerEvent()
>     return processPointerEvent(q);
>   }
> }
> //ViewRootImpl.processPointerEvent()
> private int processPointerEvent(QueuedInputEvent q) {
>   final MotionEvent event = (MotionEvent)q.mEvent;
>   //调用dispatchPointerEvent()
>   boolean handled = mView.dispatchPointerEvent(event);
>   return handled ? FINISH_HANDLED : FORWARD;
> }
> 
> //View.dispatchPointerEvent(),且ViewGroup未重写此方法
> public final boolean dispatchPointerEvent(MotionEvent event) {
>   if (event.isTouchEvent()) {
>     //调用dispatchTouchEvent()
>     return dispatchTouchEvent(event);
>   } else {
>     return dispatchGenericMotionEvent(event);
>   }
> }
> ```
>
> * 应用布局界面和`Dialog`最顶层的`ViewGroup`为`DecorView`
> * 如果不为`DecorView`，则直接`顶层ViewGroup.dispatchTouchEvent(ev)`
> * `DecorView.dispatchTouchEvent()`
>
> ```java
> public class DecorView extends FrameLayout {
>  	public boolean dispatchTouchEvent(MotionEvent ev) {
>     final Window.Callback cb = mWindow.getCallback();
>     return cb != null && !mWindow.isDestroyed() && mFeatureId < 0
>             ? cb.dispatchTouchEvent(ev) : super.dispatchTouchEvent(ev);
> 	} 
> }
> ```
>
> * `Window.Callback`
>
> ```java
> public interface Callback { 
> 	public boolean dispatchTouchEvent(MotionEvent event);
> }
> ```
>
> * `Activity`定义
>
> ```java
> public class Activity implements Window.Callback {
>   ...
> }
> ```
>
> * `Dialog`定义
>
> ```java
> public class Dialog implements Window.Callback {
>   ...
> }
> ```
>
> * 当`Window.Callback cb = mWindow.getCallback(); cb != null`时，事件传递给了`Activity`或者`Dialog`
> * 当`Window.Callback cb = mWindow.getCallback(); cb == null`时，事件传递给`super.dispatchTouchEvent(ev)`，即`DecorView`的父类`ViewGroup`
>
> ### DecorView叛徒
>
> * 本来事件从`ViewRootImpl.mView.dispatchPointerEvent()`传递好好的
> * 但是如果`ViewRootImpe.mView is DecorView`，则事件则交给了`Activity`或者`Dialog`
>
> ### Activity、PhoneWindow、DecorView、ViewRootImpl关系
>
> * `Activity`持有`PhoneWindow`
>
> ```java
> public class Activity implements Window.Callback {
>   private Window mWindow
>     
>   final void attach(Window window) {
>     mWindow = new PhoneWindow(this, window, activityConfigCallback);
>   }
> }
> ```
>
> * `PhoneWindow`持有`DecorView`
>
> ```java
> public class PhoneWindow extends Window {
>   private DecorView mDecor;
> 
> 	public PhoneWindow(Context context, Window preservedWindow,
>             ActivityConfigCallback activityConfigCallback) {
>     mDecor = (DecorView) preservedWindow.getDecorView();
>   }
> }
> ```
>
> * `DecorView`持有`PhoneWindow`,且可以通过父类`View.getViewRootImpl()`获取`ViewRootImpl`
>
> ```java
> public class DecorView extends FrameLayout {
>   DecorView(Context context, PhoneWindow window) {
>     setWindow(window);
>   }
>   void setWindow(PhoneWindow phoneWindow) {
>     mWindow = phoneWindow;
>   }
> }
> ```
>
> ### Activity事件分发
>
> ```java
> public boolean dispatchTouchEvent(MotionEvent ev) {
>     // 这个方法是个空实现，给开发者去重写
>     if (ev.getAction() == MotionEvent.ACTION_DOWN) {
>         onUserInteraction();
>     }
>     // getWindow返回的就是PhoneWindow实例
>     // 直接调用PhoneWindow的方法
>     if (getWindow().superDispatchTouchEvent(ev)) {
>         return true;
>     }
>     // 如果前面分发过程中事件没有被处理，那么调用Activity自身的方法对事件进行处理
>     return onTouchEvent(ev);
> }
> ```
>
> ### PhoneWindow事件分发
>
> ```java
> public boolean superDispatchTouchEvent(MotionEvent event) {
>   //交给DecorView去分发
>   return mDecor.superDispatchTouchEvent(event);
> }
> ```
>
> ### DecorView分发
>
> ```java
> public class DecorView extends FrameLayout {
> 	public boolean superDispatchTouchEvent(MotionEvent event) {
>     //即ViewGroup事件分发
>     return super.dispatchTouchEvent(event);
>   }
> }
> ```
>
> ### 结论
>
> * 兜兜绕绕一圈，即使`ViewRootImpl.mView is DecorView `事件交给了`Activity`，但最终又绕回了`DecorView的父类ViewGroup.dispatchTouchEvent()`
>
> ### 注意
>
> * `ViewRootImpl`不是一个`View`，它有一个`View mView`成员变量