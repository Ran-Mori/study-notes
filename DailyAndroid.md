# 日常Android学习

## Eventbus

> ### 概述
>
> * 就是一个观察者模式的框架
> * 方便使用观察者模式
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
> ### 定义接收者Obserer的update方法
>
> ```kotlin
> @Subscribe(threadMode = ThreadMode.BACKGROUND)fun subscribe(msg:MessageEvent){  Log.d("MainActivity","receive a message")  binding.buttonFirst.text = msg.msg  Toast.makeText(activity,"click one",Toast.LENGTH_LONG).show()}
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
> override fun onStart() {  super.onStart()  EventBus.getDefault().register(this)}override fun onStop() {  super.onStop()  EventBus.getDefault().unregister(this)}
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
> * `Observable`是本身拥有生命周期的`Activity、Fragment`
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
> * 实现`LifecycleObserver`
> * 使用`@OnLifecycleEvent`注解
>
> ### `Obserable`订阅`Observer`
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
> public void observe(LifecycleOwner owner,Observer<? super T> observer) {}
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
> <manifest><activity android:name="...."	android:permission=”com.google.socialapp.permission.SHARE_POST”/>
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
> ### 测试Activity的
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

## Handler

> ### 四个组成
>
> * Handler
> * MessageQueue
> * Looper
> * Message
>
> ### 成员及方法信息
>
> * Handler
>   * 成员变量
>     * `MessageQueue`
>     * `Looper`
>   * 方法
>     * 发送消息
>     * 派发消息
>
> * MessageQueue
>   * 方法
>     * 入队消息
>     * 出队消息
>
> * Message
>   * 成员变量
>   * `Handler`：所属的Hanlder
>   * `Message`：下一条消息
>
> * Looper
>   * 成员变量
>     * `MessageQueue`
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
> * `dispatchMessage(message:Message)`：链式调用，首先自己`message.callback.run()`，接着`Handler.mCallback.handleMessage(msg)`，最后`Handler.handleMessage(msg)`方法
> * `post(r:Runnalbe)`：把Runnable丢进MessageQueue
>
> ### Message
>
> * `Handler target`：消息的相应方
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
> ***

## MVP、Handler实例

> ### FatherModel.java
>
> ```java
> class FatherModel<T> implements IHandler{
> 	protected void handleData(T data) {
>      //无实现，子类overwrite它
>  }
> 
> 	//重写WeakHandler的handleMsg方法
> 	@Overrite
> 	public void handleMsg(Message msg){
>    	//调用自己的handleData方法，处理的数据从Message来。msg.obj就是Resbonse
>    	handleData(msg.obj);
>    	//成功了调用listner的onSuccess()方法。此处listner为Presenter
>      listener.onSuccess();
>  }
> }
> ```
>
> ### SonModel.kt
>
> ```java
> class SonModel extend FatherModel{
> 	private fun fetchData(){
>    	use a handler to commit a Runnable
>  }
> 	override fun handleData(response: Response?){
>    	//重写父类的handleData
>  }
> }
> ```
>
> ### FatherPresenter.java
>
> ```java
> class FatherPresenter{
> 	//在父类的构造方法处将Presenter作为观察者，Model作为被观察者
> 	public void bindMyModel(Type myModel) {
>      this.mModel = myModel;
>      this.mModel.addNotifyListener(this);
>  }
> }
> ```
>
> ### SonPresenter.java
>
> ```java
> class SonPresenter extend FatherPresenter{
> 	//成功的方法。SonPresenter作为观察者，这是观察者的一个回调方法
> 	override fun onSuccess(){
>    	//成功调用View的doSuccess()方法
>    	mView.doSuccess()
>  }
> }
> ```
>
> ### 方法调用顺序
>
> * `View.getData()`
> * `SonPresenter.getData()`
> * `SonModel.getData()`
> * `SonModel.commitARunnalbe()`
> * `成功获取到数据,但过程是使用handler模式获得的，在message里面`
> * `FatherModel.handleMsg()`
>   * 这一步是handler机制决定的，且方法名都不能改
>   * 其中设计dispatch机制
> * `FantherModel.handleData()`
>   * 此步不执行，由于动态分派规则让子类执行
> * `SonModel.handleData()`
> * `SonPresenter.onSuccess()`
>   * 这是观察者模式决定的，SonPresenter被添加为了Lisnter
> * `View.doSuccess`
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
> * 但进程之间的内核空间是共享的，一般为1G
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

