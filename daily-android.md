# 日常Android学习

***

## Eventbus

* 概述

  * 就是一个耦合度极低的观察者模式的框架

* [Analysis of EventBus principle](https://programmer.group/analysis-of-eventbus-principle.html)

* 引入依赖

  * `implementation 'org.greenrobot:eventbus:3.3.0'`

* 定义一个消息类

  * 消息类的作用就是一个消息

  ```kotlin
  data class EventBusMessage(val msg: String)
  ```

* 定义Observer的处理事件方法

  ```kotlin
  class MainActivity {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      //注册
      EventBus.getDefault().register(this)
    }
    
    override fun onDestroy() {
      super.onDestroy()
      //解注册
      EventBus.getDefault().unregister(this)
    }
    
    override fun onClick(view: View?) {
      view ?: return
      when (view.id) {
        R.id.button -> {
          // post a event
          EventBus.getDefault().post(EventBusMessage("post a event bug message"))
        }
      }
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleMessage(msg: EventBusMessage) {
      //do something when receive a msg
    }
  }
  ```

* 四种threadMode模式

  * `POSTING`：在`Observable`的线程执行
  * `Main`：`Observer`方法在`Main`(UI线程)执行
    * 如果`Observable`是在`Main`线程发出post。那么`Observer`立即执行，导致`Observerable`被阻塞
    * 如果`Observable`不在`Main`线程发出post，那么所有post构成一个队列，依次执行，`Observable`不会被阻塞
  * `MAIN_ORDERED`：post总是在一个队列里，`Observable`永远不会被阻塞
  * `BACKGROUND`
    * 如果`Observable`是在`Main`线程发出post。那么事件被**队列化**安排到一条固定的`Backgroud`线程执行，有可能会阻塞`backgroud`线程
    * 如果被`Observable`不是在`Main`线程发出post。那么任务队列就直接在发出post的那条线程执行
  * `ASYNC`：既不在`Main`线程执行，也不在`Observable`的post线程执行。EventBus有一个线程池

* 源码解析

  ```java
  public class EventBus {
    public void register(Object subscriber) {
      //查找订阅方法
      List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
      //将方法进行订阅
      synchronized (this) {
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
          subscribe(subscriber, subscriberMethod);
        }
      }
    }
    
    public void post(Object event) {
      //将事件抛出，调用postSingleEvent方法
      postSingleEvent(eventQueue.remove(0), postingState);
    }
    
    private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
      synchronized (this) {
        //通过抛出的事件找到订阅方法
        subscriptions = subscriptionsByEventType.get(eventClass);
      }
      for (Subscription subscription : subscriptions) {
        //将事件处理传递给postToSubscription方法
        postToSubscription(subscription, event, postingState.isMainThread); 
      }
    }
    
    private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
      switch (subscription.subscriberMethod.threadMode) {
          //区分注解threadMode
          case MAIN:
          	//将事件处理传递给invokeSubscriber
          	invokeSubscriber(subscription, event);
          	break;
      }
    }
    
    void invokeSubscriber(Subscription subscription, Object event) {
      //直接反射开始执行
      subscription.subscriberMethod.method.invoke(subscription.subscriber, event);
    }
  }
  ```

* `sticky event`

  1. 参考链接：[Sticky Events](https://greenrobot.org/eventbus/documentation/configuration/sticky-events/)
  2. 概念：当使用`EventBus.getDefault().postSticky()`抛出一个事件时，这个事件就是`sticky event`。内存中始终会存储最近抛出的一个`sticky event`
  3. 作用：一旦注册了EventBus，即`EventBus.getDefault().register(this)`，它`@Subscribe(sticky = true)`的方法就会看是否有这个已经抛出过被存储在内存中的`sticky event`，一旦有则立刻执行`@Subscribe`的方法体
  4. 关键API
     * `EventBus.getDefault().postSticky(stickyEvent)`
     * `@Subscribe(sticky = true)`
     * `EventBus.getDefault().getStickyEvent(class)`
     * `EventBus.getDefault().removeStickyEvent(stickyEvent)`

***

## LifeCycleOwner

* 概述

  * 本质上还是一个观察者模式
  * `Observable`是本身拥有生命周期的`Activity`、`Fragment`
  * `Observer`是自定义的

* 定义`Observer`

  ```kotlin
  class MyLifecycleObserver: LifecycleObserver {    
    
    @OnLifecycleEvent(Lifecycle.Event.ON_START)    
    fun onStart(){        
      Log.d("LifecycleObserver","onStart")    
    }    
    
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)    
    fun onStop(){        
      Log.d("LifecycleObserver","onStop")    
    }    
    
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)    
    fun onDestroy(){        
      Log.d("LifecycleObserver","onDestroy")    
    }
    
  }
  ```

  * `LifecycleObserver`接口没有任何方法
  * 使用`@OnLifecycleEvent`注解

* `Observable`订阅`Observer`

  ```kotlin
  class FirstFragment : Fragment() {    
    private lateinit var myLifecycleObserver: MyLifecycleObserver  
    
    override fun onCreate(savedInstanceState: Bundle?) {        
      super.onCreate(savedInstanceState)        
      myLifecycleObserver = MyLifecycleObserver(this)        
      lifecycle.addObserver(myLifecycleObserver)    
    }
  }
  ```
  
  * 首先实例化出一个`Observer`对象
  * 再将`Observer`对象添加进去
  
* 注

  * Android手动杀死进程。依旧会执行`onStop()、onDestroy()`方法

* 实现原理

  * `LifecycleOwner`接口的实现类在`androidx.activity.ComponentActivity`

    ```java
    public class ComponentActivity implements LifecycleOwner {
      //声明一个LifecycleRegistry
      private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
      
      protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将生命周期的变化委托到ReportFragment
        ReportFragment.injectIfNeededIn(this);
      }
    }
    ```

  * `ReportFragment`生命周期依附`Activity`，这样可以无侵入`Activity`即可观察`Activity`的生命周期

    ```java
    public class ReportFragment extends android.app.Fragment {
      public static void injectIfNeededIn(Activity activity) {
        LifecycleCallbacks.registerIn(activity);
      }
      
      static class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        static void registerIn(Activity activity) {
          //给Activity注册一个ActivityLifecycleCallback，而这个Callback的实现如下所示
          activity.registerActivityLifecycleCallbacks(new LifecycleCallbacks());
        }
        
        @Override
        public void onActivityPostStarted(@NonNull Activity activity) {
          //将start事件diapatch出去
          dispatch(activity, Lifecycle.Event.ON_START);
        }
        
        @Override
        public void onActivityPostResumed(@NonNull Activity activity) {
          dispatch(activity, Lifecycle.Event.ON_RESUME);
        }
        
        @Override
        public void onActivityPreDestroyed(@NonNull Activity activity) {
          dispatch(activity, Lifecycle.Event.ON_DESTROY);
        }
      }
      
      static void dispatch(@NonNull Activity activity, @NonNull Lifecycle.Event event) {
        Lifecycle lifecycle = ((LifecycleOwner) activity).getLifecycle();
        //将事件交给了LifecycleRegistry，调用handleLifecycleEvent方法来处理
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
      }
    }
    ```

    * 综上`ReportFragment`只是一个没有界面的`Fragment`，它的作用就是将`Activity`的生命周期引借出来。通过注册一个监听器来监听`Activity`的生命周期，在监听的处理中又将生命周期处理交还给了`Activity.mLifecycleRegistry`

  * `LifecycleRegistry`

    ```java
    public class LifecycleRegistry extends Lifecycle { 
      
      //mLifecycleOwner在这里是一个Activity
      private final WeakReference<LifecycleOwner> mLifecycleOwner;
      private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap<>();
      
      public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        //处理生命周期只是调用了一下moveToState
        moveToState(event.getTargetState());
      }
      
      private void moveToState(State next) {
        mState = next;
        mHandlingEvent = true;
        //调用核心的sync方法
        sync();
        mHandlingEvent = false;
      }
      
      private void sync() {
        LifecycleOwner lifecycleOwner = mLifecycleOwner.get();
        backwardPass(lifecycleOwner);
      }
      
      private void backwardPass(LifecycleOwner lifecycleOwner) {
        //observer is get from `mObserverMap`, and it is an ObserverWithState
        Event event = Event.downFrom(observer.mState);
        observer.dispatchEvent(lifecycleOwner, event);
      }
      
      static class ObserverWithState {
        State mState;
        LifecycleEventObserver mLifecycleObserver;
        
        ObserverWithState(LifecycleObserver observer, State initialState) {
          //通过Lifecycling.lifecycleEventObserver()将LifecycleObserver转换为ReflectiveGenericLifecycleObserver
          mLifecycleObserver = Lifecycling.lifecycleEventObserver(observer);
          mState = initialState;
        }
        
        void dispatchEvent(LifecycleOwner owner, Event event) {
          mLifecycleObserver.onStateChanged(owner, event);
        }
      }
    }
    ```
  
  * `ReflectiveGenericLifecycleObserver`
  
    ```java
    //public interface LifecycleEventObserver extends LifecycleObserver
    class ReflectiveGenericLifecycleObserver implements LifecycleEventObserver {
      //这个mWrapped是LifecycleObserver
      private final Object mWrapped;
      private final CallbackInfo mInfo;
    
      ReflectiveGenericLifecycleObserver(Object wrapped) {
        mWrapped = wrapped;
        mInfo = ClassesInfoCache.sInstance.getInfo(mWrapped.getClass());
      }
    
      @Override
      public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Event event) {
        //这一行会通过反射执行目标方法
        mInfo.invokeCallbacks(source, event, mWrapped);
      }
    }
    ```
  
    * 还是同样一个思路，尽量不侵入用户自定义的`class MyLifeCycle: LifecycleObserver`类。而是通过继承的方式将方法引出来。


***

## LiveData

* 概述

  * 也是一种观察者模式
  * 除了`观察者、被观察者`外多了一个第三者——`LifeCycleOwner`，用于控制观察者模式的时间域范围

* 特点

  * `LifecycleOwner`，一般就只能是`Activity`、`Fragment`，必须有`start、resume、stop`等方法
  * `Observer`必须实现`public interface Observer<T>`接口的`onChanged(T t)`方法
  * 不用手动处理生命周期，默认方式封装了只会在活跃生命周期内观察
  * 如果在不正常生命周期漏观察了变化，则在进入正常生命周期时刻会立即更新
  * 总是就是很好用很方便

* 观察

  ```java
  //LiveData.observe()
  public void observe(LifecycleOwner owner, Observer<? super T> observer) {}
  ```

* `LiveData`

  ```java
  public abstract class LiveData<T> {
    
    //observer容器
    private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap<>();
    
    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
  
      //包一层，将LifecycleOwner生命周期观察引借出来
      LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
      //存放入容器
      ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
      //LifecycleOwner观察wrapper
      owner.getLifecycle().addObserver(wrapper);
    }
    
    @MainThread
    protected void setValue(T value) {
      mVersion++;
      mData = value;
      //setValue调用到dispatchValue()
      dispatchingValue(null);
    }
    
    void dispatchingValue(@Nullable ObserverWrapper initiator) {
      for (Iterator<Map.Entry<Observer<? super T>, ObserverWrapper>> iterator =
           mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
        //调用considerNotify
        considerNotify(iterator.next().getValue());
      }
    }
    
    private void considerNotify(ObserverWrapper observer) {
      //调用observer的方法
      observer.mObserver.onChanged((T) mData);
    }
    
    class LifecycleBoundObserver extends ObserverWrapper implements LifecycleEventObserver {
      //在lifecycleOwner的生命周期前提下进行观察者模式
      @Override
      public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        Lifecycle.State currentState = mOwner.getLifecycle().getCurrentState();
        if (currentState == DESTROYED) {
          removeObserver(mObserver);
          return;
        }
        Lifecycle.State prevState = null;
        while (prevState != currentState) {
          prevState = currentState;
          //因lifecycle生命周期发生改变而进行观察改变,实际调用ObserverWrapper.activeStateChanged
          activeStateChanged(shouldBeActive());
          currentState = mOwner.getLifecycle().getCurrentState();
        }
      }
    }
    
    private abstract class ObserverWrapper {
      void activeStateChanged(boolean newActive) {
        //调用dispatchValue
        dispatchingValue(this);
      }
    }
  }
  ```

***

## Activity

* 概述

  * 代表安卓应用中的**一个屏幕** ，不同的屏幕对应不同的`Activity`，比如电子邮件列表屏幕、电子邮件编辑屏幕
  * 之所以能够理解成代表一个`屏幕`和后续的`window`机制有关，一个`window`因为持有`ViewRootImpl`是一颗`view`树
  * `Activity`的存在支持了每次调用应用不是一定从一个固定的屏幕开始(也就是通常所说的主函数)。比如浏览器点击发送邮件按钮应该从编辑邮件按钮界面开始，而不是从一般的邮件列表开始
  * `Activity`提供窗口让应用绘制界面。窗口可能铺满实际物理屏幕，也可能比实际物理屏幕小
  * `Acitivity`之间的依赖耦合很小

* Intent过滤器

  * 当系统询问界面询问使用那个应用来执行接下来的操作时，就是 **隐式Intent** 在起作用

* Activity权限

  * 给`Activity`单独声明权限

  ```xml
  <manifest>
    <activity android:name="...."	
              android:permission=”com.google.socialapp.permission.SHARE_POST”/>
  </manifest>
  ```

  * `父Activity`的权限必须是`子Activty`的真子集时，父才能启动子

* `OnSaveInstanceState()`

  * 用户自己退出时不会调用
  * 只有因为系统资源紧张，系统自动把它清除掉或者其他原因才会调用此方法
  * 此方法的对应回调在`fun onCreate(savedInstanceState: Bundle?)`中

* A切换到B的执行顺序

  * `A.onPause()`、`B.onCreate()`、`B.onStart()`、`B.onResume()`、`A.onStop()`
  * 两者生命周期是有重叠的

* 返回键

  * `onBackPressed()`

* 健壮的Activity

  * 一个合格稳定的Activity，一定要在意外的情况下也能逻辑正确
  * 其他应用(电话)阻断了此Activity
  * 系统自动回收销毁又创建此Activity
  * 将此Activity放在新的窗口环境中，如画中画、多窗口环境等

***

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

  * binder驱动 - 路由器
  * ServiceManager - DNS
  * Client - client
  * Server - server

* 原理简介

  * binder是一个设备，但其没有对应的硬件实体，而是内核态的一段内存。这个设备对外有`open、ioctl、mmap`等接口操作
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

* `binder`的使用

  * 创建一个`IMyService.aidl`

    ```java
    package com.example;
    import com.example.ComplexType;
    
    interface IMyService {
      //write some methods
      void sayhello();
    }
    ```

    * `aidl`作用：直接写一个类过于复杂，`aidl`等于是一个中间过程，我们只需在`aidl`中写入核心的内容，其余一切交给系统自动生成久好了。以下是系统自动生成的一个比较复杂的类

  * `IInterface.java`

    ```java
    package android.os;
    
    public interface IInterface
    {
        /**
         * Retrieve the Binder object associated with this interface.
         * You must use this instead of a plain cast, so that proxy objects
         * can return the correct result.
         */
        public IBinder asBinder();
    }
    ```

  * 系统自动生成一个`IMyService.java`

    ```java
    package com.example;
    
    //继承了IInterface，因此需要实现`asBinder`方法
    public interface IMyService extends android.os.IInterface {
      
      /** Default implementation for IMyService. */
      public static class Default implements com.example.IMyService {
        
        //default implementation for some methods
        public void sayHello(){}
        
        //default implementation for asBindler()
        @Override
        public android.os.IBinder asBinder() {
          return null;
        }
        
      }
      
      /** Local-side IPC implementation stub class. */
      //继承了anddroid.os.Binder，实现了IMyService，Stub是一个抽象类，具体方法由服务端实现
      public static abstract class Stub extends android.os.Binder implements com.example.IMyService {
        
        //binder的唯一标志符，一般用全类名
        private static final java.lang.String DESCRIPTOR = "com.example.IMyService";
        
        /** Construct the stub at attach it to the interface. */
        public Stub() {
          this.attachInterface(this, DESCRIPTOR);
        }
        
        /**
         * Cast an IBinder object into an com.example.IMyService interface,
         * generating a proxy if needed.
         */
        //用于将服务端的Binder对象转换成客户端所需的AIDL接口类型对象，这种类型转换过程是区分进程的，如果客户端和服务端位于同一进程，那么此方法返回的就是服务端的Stub对象本身，否则返回的是系统封装后的Stub.proxy对象
        public static com.example.IMyService asInterface(android.os.IBinder obj) {
          if ((obj==null)) {
            return null;
          }
          //查询本地接口
          android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
          //如果存在直接返回该对象（代表服务端与客户端在同一进程）
          if (((iin!=null)&&(iin instanceof com.example.IMyService))) {
            return ((com.example.IMyService)iin);
          }
          //返回系统封装后的Stub.proxy对象（代表服务端客户端不在同一进程）
          return new com.example.IMyService.Stub.Proxy(obj);
        }
        
        //返回当前的Binder对象
        @Override 
        public android.os.IBinder asBinder() {
          return this;
        }
        
        //该方法运行在服务端的Binder线程池中，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法处理
        @Override 
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
          java.lang.String descriptor = DESCRIPTOR;
          switch (code) {
            case INTERFACE_TRANSACTION: {
              reply.writeString(descriptor);
              return true;
            }
            //自己定义的方法
            case TRANSACTION_sayHello: {
              this.sayHello(...);
              reply.writeNoException();
              return true;
            }
            default: {
              return super.onTransact(code, data, reply, flags);
            }
          }
        }
    
        //Stub类里面的私有静态类，实现了IMyInterface
        private static class Proxy implements com.example.IMyService {
          private android.os.IBinder mRemote;
          
          //构造方法，将参数存为成员变量
          Proxy(android.os.IBinder remote) {
            mRemote = remote;
          }
          
          @Override 
          public android.os.IBinder asBinder() {
            return mRemote;
          }
          
          public java.lang.String getInterfaceDescriptor() {
            return DESCRIPTOR;
          }
          
          //implementation for some methods
          //此方法运行在服务端
          @Override 
          public void sayHello() throws android.os.RemoteException {
            //创建输入类型Parcel对象
            android.os.Parcel _data = android.os.Parcel.obtain();
            //创建输出类型Parcel对象
            android.os.Parcel _reply = android.os.Parcel.obtain();
            
            try {
              //发起RPC（远程过程调用请求），同时当前线程挂起；然后服务端的onTransact方法会被调用，直到RPC过程返回后，当前线程继续执行
              boolean _status = mRemote.transact(Stub.TRANSACTION_basicTypes, _data, _reply, 0);
              if (!_status && getDefaultImpl() != null) {
                getDefaultImpl().basicTypes(anInt, aLong, aBoolean, aFloat, aDouble, aString);
                return;
              }
              _reply.readException();
            }
            finally {
              _reply.recycle();
              _data.recycle();
            }
          }
         
          public static com.example.IMyService sDefaultImpl;
        }
        
        //自定义方法的标志数
        static final int TRANSACTION_sayhello = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        
        public static boolean setDefaultImpl(com.example.IMyService impl) {
          if (Stub.Proxy.sDefaultImpl == null && impl != null) {
            Stub.Proxy.sDefaultImpl = impl;
            return true;
          }
          return false;
        }
        
        public static com.example.IMyService getDefaultImpl() {
          return Stub.Proxy.sDefaultImpl;
        }
        
      }
      
      //写在`IMyService.aidl`中的方法
      public void sayhello() throws android.os.RemoteException;
    }
    ```

  * `ServiceConnection.java`

    ```java
    package android.content;
    
    import android.os.IBinder;
    
    public interface ServiceConnection {
        void onServiceConnected(ComponentName name, IBinder service);
        void onServiceDisconnected(ComponentName name);
    }
    ```

  * `client`端

    ```java
    package com.example.javabinderclient;
    
    import android.os.IBinder;
    import android.os.RemoteException;
    import com.example.IMyService;
    
    //实现ServiceConnection接口
    public class MainActivity extends AppCompatActivity implements ServiceConnection
    {
        private IMyService mService = null;
        private volatile boolean mIsServiceConnected = false;
    
        @Override
        protected void onResume()
        {
            super.onResume();
            Intent intent = new Intent();
          	//构造Intent，这是server端的一个service实现类
            intent.setClassName("com.example.javabinderservice",
                    "com.example.javabinderservice.MyService");
    
            bindService(intent, this, BIND_AUTO_CREATE);
    
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
    										//调用自定义的方法
                        mService.sayHello();
                    }
                }
            }).start();
        }
    
        @Override
        protected void onPause() {
            unbindService(this);
            mService = null;
            super.onPause();
        }
    
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
          	//获取service
            mService = IMyService.Stub.asInterface(iBinder);
        }
    
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
          	//service置空
            mService = null;
        }
    }
    ```

  * `server`端

    ```java
    package com.example.javabinderservice;
    
    import android.app.Service;
    import android.os.IBinder;
    import com.example.IMyService;
    
    public class MyService extends Service {
        private IBinder mBinder;
    
        @Override
        public void onCreate() {
            super.onCreate();
    				//赋值binder
            mBinder = new MyServiceBinder();
        }
    
        @Override
        public IBinder onBind(Intent intent) {
            return mBinder;
        }
    
      	//自己定义一个真正的server端实现类，继承的是IMyService.Stub
        private static class MyServiceBinder extends IMyService.Stub {
            @Override
            public void sayHello() throws RemoteException {
                //真正的实现
            }
        }
    }
    ```

* 调用顺序

  * `MainActivity.java`

    ```java
    public class MainActivity extends AppCompatActivity implements ServiceConnection {
      @Override
      protected void onResume() {
        super.onResume();
        //调用ContextWrapper.bindService()
        bindService(intent, this, BIND_AUTO_CREATE); 
      }
    }
    ```

  * `ContextImpl.java`

    ```java
    class ReceiverRestrictedContext extends ContextWrapper {
      @Override
      public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        //调用bindServiceCommon
        return bindServiceCommon(service, conn, flags, ...);
      }
      
      private boolean bindServiceCommon(Intent service, ServiceConnection conn, int flags,...) {
        // Keep this in sync with DevicePolicyManager.bindDeviceAdminServiceAsUser.
        IServiceConnection sd;
        //将ServiceConnection conn封装成IServiceConnection sd
        sd = mPackageInfo.getServiceDispatcher(conn, ..., flags);
        IBinder token = getActivityToken();
        //调用ActivityManager
        int res = ActivityManager.getService().bindIsolatedService(service, sd, flags);
      }
    }
    ```

  * `ActivityManager.java`

    ```java
    public class ActivityManager {
      
      private static final Singleton<IActivityManager> IActivityManagerSingleton =
        new Singleton<IActivityManager>() {
        @Override
        protected IActivityManager create() {
          //通过ServiceManager获取ActivityManagerService服务
          final IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
          final IActivityManager am = IActivityManager.Stub.asInterface(b);
          return am;
        }
      };
      
      public static IActivityManager getService() {
        return IActivityManagerSingleton.get();
      }
    }
    ```

  * `ActivityManagerService.java`

    ```java
    public class ActivityManagerService extends IActivityManager.Stub {
      
      final ActiveServices mServices;
      
      public int bindIsolatedService(Intent service, IServiceConnection connection) {
        synchronized(this) {
          return mServices.bindServiceLocked(service, connection);
        }
      }
      
    }
    ```

  * `ActiveServices.java`

    ```java
    public final class ActiveServices {
      int bindServiceLocked(Intent service, final IServiceConnection connection) {
        ServiceLookupResult res = retrieveServiceLocked(service, ...);
        //Intent service 封装成s
        ServiceRecord s = res.record;
        
        ConnectionRecord c = new ConnectionRecord(connection);
        IBinder binder = connection.asBinder();
        //connection直接被加到了s里面
        s.addConnection(binder, c);
        
        requestServiceBindingLocked(s, b.intent, callerFg, false);
      }
      
      //thread实际为IApplicationThread thread, 跨进程跨到ActivityThread.java
      private final boolean requestServiceBindingLocked(ServiceRecord r) {
        r.app.thread.scheduleBindService(r);
      }
    }
    ```

  * `ActivityThread.java`

    ```java
    public final class ActivityThread extends ClientTransactionHandler {
      
      final ArrayMap<IBinder, Service> mServices = new ArrayMap<>();
      
      public final void scheduleBindService(IBinder token, Intent intent) {
        sendMessage(H.BIND_SERVICE, s);
      }
      
      public void handleMessage(Message msg) {
        switch (msg.what) {
            case BIND_SERVICE:
              handleBindService((BindServiceData)msg.obj);
              break;
        }
      }
      
      private void handleBindService(BindServiceData data) {
        Service s = mServices.get(data.token);
        //调用onBind
        IBinder binder = s.onBind(data.intent);
        ActivityManager.getService().publishService(data.token, data.intent, binder);
      }
    }
    ```

  * `ActivityManagerService.java`

    ```java
    public class ActivityManagerService extends IActivityManager.Stub {
      
      public void publishService(IBinder token, Intent intent, IBinder service) {
        synchronized(this) {
          mServices.publishServiceLocked((ServiceRecord)token, intent, service);
        }
      }
    }
    ```

  * `ActiveServices.java`

    ```java
    public final class ActiveServices {
      void publishServiceLocked(ServiceRecord r, Intent intent, IBinder service) {
        //conn是IServiceConnection conn; 
        c.conn.connected(r.name, service, false);
      }
    }
    ```

  * `IServiceConnection`由何而来

    * `ContextImpl.java`

      ```java
      class ReceiverRestrictedContext extends ContextWrapper {
        final @NonNull LoadedApk mPackageInfo;
      
      	private boolean bindServiceCommon(ServiceConnection conn) {
         IServiceConnection sd;
         sd = mPackageInfo.getServiceDispatcher(conn, getOuterContext(), executor, flags);
       }
      }
      ```

    * `LoadeApk.java`

      ```java
      public final class LoadedApk {
        public final IServiceConnection getServiceDispatcher(ServiceConnection c) {
          return getServiceDispatcherCommon(c, context, null, executor, flags);
        }
        
        private IServiceConnection getServiceDispatcherCommon(ServiceConnection c) {
          sd = new ServiceDispatcher(c, context, executor, flags);
        }
        
        //继承Stub，又是一个跨进程通信
        private static class InnerConnection extends IServiceConnection.Stub {
          
          private final ServiceConnection mConnection;
          
          ServiceDispatcher(ServiceConnection conn) {
            mConnection = conn;
          }
          
          public void connected(ComponentName name, IBinder service, boolean dead) {
            mActivityThread.post(new RunConnection(name, service, 0, dead));
          }
          
          private final class RunConnection implements Runnable {
            public void run() {
              doConnected(mName, mService, mDead);
            }
          }
           
          public void doConnected(ComponentName name, IBinder service, boolean dead) {
            if (service != null) {
              //调用onServiceConnected
              mConnection.onServiceConnected(name, service);
            }
          }
        }
      }
      ```


***

## ServiceManager

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

## RxJava

* 响应式函数编程

  * `命令式编程`：是面向计算机硬件的抽象，有变量、赋值语句、表达式和控制语句
  * `函数式编程`：是面向数学的抽象，将计算描述为一种表达式求值，函数可以在任何地方定义，并且可以对函数进行组合
  * `响应式编程`：是一种面向数据流和变化传播的编程范式，数据的更新是关联的

* `callback`式的缺点

  * 消费者需要关注的点太多，比如什么时候IO、线程切换、异步操作
  * 消费者职责不单一，数据处理应该是生产者的事情，但有时消费者还要负责数据处理
  * 多次异步时，就会有回调地狱

* ReactiveX响应式

  * 迭代器设计

    ```kotlin
    public interface Iterable<out T> {
        public operator fun iterator(): Iterator<T>
    }
    
    public interface Iterator<out T> {
        public operator fun next(): T
        public operator fun hasNext(): Boolean
    }
    ```

  * `Observable`和`Observer`设计

    ```kotlin
    public interface ObservableSource<T> {
        void subscribe(@NonNull Observer<? super T> observer);
    }
    
    public interface Observer<T> {
      void onSubscribe(@NonNull Disposable d);
      void onNext(@NonNull T t); //对应onNext()
      void onError(@NonNull Throwable e); //对应throw Exception
      void onComplete();//对应!hasNext()
    }
    ```

* 函数式

  * 按照道理是不能函数式的，因为有异常有副作用

  * 原理是使用了`monad`。即`Observable`是`Monad`，rxjava中操作符起了`monad`的作用

    ```kotlin
    class ObservableMap<T, U> (
      private val source: ObservableSource<T>, 
      private val mapper: (T) -> U,
    ) : Observable<U>() {} //两个入参一个返回值的结构完全符合monad
    
    
    ```

  * 由于monad的函数组合能力，最终成为一个简洁明了的链式调用

* 四个角色

  * `Observable`：`produce event`
  * `Observer`：`consume event`
  * `Subscribe`：`a connection between observalbe and observer`
  * `Event`：`carry message`

* 简略过程

  * 创建`Observable`并产生事件
  * 创建`Observer`定义消费事件行为
  * 通过`Subscribe`连接`Observable`和`Observer`

* 创建Observable

  * `Observable`的创建是收敛的
  * 无论是`create()`、`just()`、`from()`还是其他什么，都会收敛到传入一个`Observable.onSubscrib(){}`接口的实现对象
  * `Observable.java`

  ```java
  public class Observable<T> {
  		//成员变量
      final OnSubscribe<T> onSubscribe;
  
  		//唯一的构造函数，需要传入一个接口对象
      protected Observable(OnSubscribe<T> f) {
          this.onSubscribe = f;
      }
  
      //接口定义，实际就是一个Action1, 参数是Subscriber
      public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
          // cover for generics insanity
      }
  }
  ```

  * `Action1.java`

  ```java
  public interface Action1<T> extends Action {
      void call(T t);
  }
  ```

  * `Subscriber.java`

  ```java
  public abstract class Subscriber<T> implements Observer<T>, Subscription {
      //...
  }
  ```

  * `Observer.java` - 观察者

  ```java
  public interface Observer<T> {
      void onCompleted();
      void onError(Throwable e);
      void onNext(T t);
  }
  ```

  * `Observerable.subscribe()`方法

  ```java
  static <T> Subscription subscribe(Subscriber<? super T> subscriber, Observable<T> observable) {
    // new Subscriber so onStart it
    subscriber.onStart();
  
    //最后通过Subscriber.java里的接口call()方法开始调用
    RxJavaHooks.onObservableStart(observable, observable.onSubscribe).call(subscriber);
    return RxJavaHooks.onObservableReturn(subscriber);
  }
  
  
  //OnSubscribeFromArray.java中一个call方法的实现
  public void call(Subscriber<? super T> child) {
      child.setProducer(new FromArrayProducer<T>(child, array));
  }
  ```

* 绑定时机性

  * 真正开始观察与被观察一定是在`observeralbe.subscible()`后才开始

* 队列性

  * `Observable`发射的是一串事件，而不是一个事件。整串事件被抽象成一个队列
  * 事件流未开始时观察者调用`onSubscribe()`
  * 事件流中每一个事件观察者调用`onNext()`
  * 事件流发生错误时观察者调用`onError()`
  * 事件流结束时观察者调用`onComplete()`
  * 互斥性：`onError()`和`onComplete()`是互斥的。两者之一必被调用一次

* Action

  * 定义

  ```java
  public interface Action2<T1, T2> extends Action {
    void call(T1, T2);
  }
  ```

  * 不同就是泛型的个数不同

* 不完全回调

  * 定义

  ```java
  public Disposable subscribe(Consumer onNext, Consumer onError, Action onComplete, Consumer onSubscribe){};
  ```

* 核心方法

  * `observable.subscribe(observer);`或者`observable.subscribe(subscriber)；`
  * 即相当于`addObserver(observer)`

* 两个方法

  * `subscribeOn(final Scheduler scheduler)`：`specify the Scheduler on which an Observable will operate`
  * `observeOn(final Scheduler scheduler)`：`specify the Scheduler on which an observer will observe this Observable`

* 操作符

  * `zip`
    *  `combine the emissions of multiple Observables together via a specified function and emit single items for each combination based on the results of this function.`
    * 适用场景 - 同时发多个请求，等多个请求都返回才处理。（粉丝通知的粉丝和推人同时发）
  * `concat`
    * `emit the emissions from two or more`
    * 适用场景 - 同时发多个请求，但请求处理要求次序。（同时读缓存和进行网络请求，但要求先处理缓存）

* `Disposable`

  * [When and How to Use RxJava Disposable](https://cupsofcode.com/post/when_how_use_rxjava_disposable_serialdisposable_compositedisposable/)
  * 为什么建议要在`onDestory`进行`dispose`？因为流的发射通常是网络请求，耗时事件很长，有时结果还没有返回但页面已经退出了，此时应该终止流的进行与订阅，否则可能出现内存泄漏问题


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

* window链接

  * [Android全面解析之Window机制](https://juejin.cn/post/6888688477714841608)

* window概述

  * `window`是一个抽象概念
  * `window`是`view`的载体
  * `view`是`window`的表现

* view树

  * 一个`window`就是一颗`view`树
  * 一颗`view`树就是一个`window`

* Dialog

  * 通过`windowManager`添加的`view`，与当前的`Activity`毫无关系
  * 它是另一个`window`，另一个`view`树

* type属性

  * 决定`window`即`view`树的深度信息，越高越靠前
  * 系统弹窗、Toast的type值就很高

* 添加window

  * 示例代码

  ```java
  //在Activity中执行下列代码
  Button button = new Button(this);
  WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
  // 这里对windowParam进行初始化
  windowParam.addFlags...
  // 获得应用PhoneWindow的WindowManager对象进行添加window
  getWindowManager.addView(button, windowParams);
  ```

  * `WindowManagerGloabal.addView()`最终执行

  ```java
  public void addView(...) {
    //在此新建了一个ViewRootImpl
    root = new ViewRootImpl(view.getContext(), display);
    view.setLayoutParams(wparams);
    mViews.add(view);
    mRoots.add(root);
    mParams.add(wparams);
  
    ...
  
    root.setView()
  }
  ```

  * `WindowManagerGloabal`是一个全局单例对象，管理所有`window`
  * `ViewRootImpl.setView()`会调用`WMS`去创建`window`

* 事件产生及传递

  * 硬件产生触动

  * 触动传递给`InputManagerService`

  * `InputeManagerService`通过`WindowManagerService`将触动传给对应的`window`

  * `window`将触动传送给对应的`ViewRootImpl`
  
  * `ViewRootImpl`将触动封装成`MotionEvent`对象，传递给下层`View`
  
* 事件传递

  * `ViewRootImpl`

  ```java
  class ViewRootImpl {
    View mView;
    public void setView(View view, ...) {
      if (mView == null) {
        mView = view;
      }
    }
  }
  ```

  * `ViewRootImpl`向下传递

  ```java
  //ViewRootImpl.ViewPostImeInputStage
  final class ViewPostImeInputStage extends InputStage {
    @Override
    protected int onProcess(QueuedInputEvent q) {
      //调用processPointerEvent()
      return processPointerEvent(q);
    }
  }
  //ViewRootImpl.processPointerEvent()
  private int processPointerEvent(QueuedInputEvent q) {
    final MotionEvent event = (MotionEvent)q.mEvent;
    //调用dispatchPointerEvent()
    boolean handled = mView.dispatchPointerEvent(event);
    return handled ? FINISH_HANDLED : FORWARD;
  }
  
  //View.dispatchPointerEvent(),且ViewGroup未重写此方法
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
      final Window.Callback cb = mWindow.getCallback();
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

  * `Activity`定义

  ```java
  public class Activity implements Window.Callback {
    ...
  }
  ```

  * `Dialog`定义

  ```java
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

* 注意

  * `ViewRootImpl`不是一个`View`，它有一个`View mView`成员变量

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

## Fresco

* 参考文档
  * [Fresco架构设计赏析](https://juejin.cn/post/6844903784460582926)

* 重要构成
  * `DraweeView`

    1. 官方解释 -> `View that displays a DraweeHierarchy.`

    2. 继承`ImageView`，但它的接口别用，未来会考虑直接继承`View`。唯一交集: 利用`ImageView`来显示`Drawable`

    3. 持有`DraweeHolder`对象

       ```java
       public class DraweeView extends ImageView {
         private DraweeHolder<DH> mDraweeHolder;
       }
       ```

  * `DraweeHolder`

    * 官方解释 -> `A holder class for Drawee controller and hierarchy.`

    * 持有`DraweeController`和`DraweeHierarchy`

      ```java
      public class DraweeHolder {
        @Nullable private DH mHierarchy;
        private DraweeController mController = null;
      }
      ```

  * `DraweeHierachy`

    * `Draweable`的容器，从`BACKGROUND -> OVERLAY`一共包含7层`Drawable`

      ```java
      public class GenericDraweeHierarchy {
        private static final int BACKGROUND_IMAGE_INDEX = 0;
        private static final int PLACEHOLDER_IMAGE_INDEX = 1;
        private static final int ACTUAL_IMAGE_INDEX = 2;
        private static final int PROGRESS_BAR_IMAGE_INDEX = 3;
        private static final int RETRY_IMAGE_INDEX = 4;
        private static final int FAILURE_IMAGE_INDEX = 5;
        private static final int OVERLAY_IMAGES_INDEX = 6;
      }
      ```

  * `DraweeController` 

    * 控制图片的加载，请求，并根据不同事件控制`Hierarchy`

    * 持有`DraweeHierarchy`

      ```java
      public abstract class AbstractDraweeController {
        private SettableDraweeHierarchy mSettableDraweeHierarchy;
      }
      ```

  * `ImagePipline` - 顾名思义

* 图片加载简要流程

  1. `Controller`将请求任务委托给`DataSource`，在`DataSource`内注册一个请求结果的回调 -> `DataSubscriber`

  2. `DataSource`通过经过一系列`Producer`委托责任链处理最终获得`result`，调用到`DataSubscriber`的方法

  3. 将`result`传递给`Hierachy`

  4. `DraweeView`将`Hierachy`的`topLevelDrawable`取出来展示

* 与加载相关的一些关键接口

  1. `DataSource`

     ```java
     public interface DataSource<T> {
       //获取结果
       T getResult();
       //查询状态
       boolean isFinished();
       boolean hasFailed();
       //注册回调，类似于RxJava
       void subscribe(DataSubscriber<T> dataSubscriber, Executor executor);
     }
     ```

  2. `DataSubscirber`

     ```java
     public interface DataSubscriber<T> {
       //成功回调
       void onNewResult(@Nonnull DataSource<T> dataSource);
       //失败回调
       void onFailure(@Nonnull DataSource<T> dataSource);
       //取消回调
       void onFailure(@Nonnull DataSource<T> dataSource);
     }
     ```

  3. `Producer`

     ```java
     public interface Producer<T> {
       // 产生数据，并通知consumer消费
       void produceResults(Consumer<T> consumer, ProducerContext context);
     }
     ```

  4. `Consumer`

     ```java
     public interface Consumer<T> {
       //实际上就是一个回调
       void onNewResult(@Nullable T newResult, @Status int status);
       void onFailure(Throwable t);
       void onCancellation();
     }
     ```

* 图片加载详细流程

  1. 设置`controller`，订阅`dataSourceSubsciber`

     ```java
     //DraweeView#setController
     public void setController(draweeController) {
       //将设置controller委托给mDraweeHolder
       mDraweeHolder.setController(draweeController);
     }
     
     //DraweeHolder#setController
     public void setController(draweeController) {
       mController = draweeController;
       if (wasAttached) {
         //尝试进行attach
         attachController();
       }
     }
     private void attachController() {
       //调用controller的attach
       mController.onAttach();
     }
     
     //AbstractDraweeController
     public void onAttach() {
       if (!mIsRequestSubmitted) {
         submitRequest();
       }
     }
     protected void submitRequest() {
       final T closeableImage = getCachedImage();
       
       if (closeableImage != null) {
         //有缓存(代码只找了内存缓存)，根本不用请求，直接return
         return
       }
       
       //内存缓存没有就通过mDataSource进行请求
       DataSubscriber<T> dataSubscriber = new BaseDataSubscriber<T>() {
         public void onNewResultImpl(DataSource<T> dataSource) {
           //...
         }
         public void onFailureImpl(DataSource<T> dataSource) {
           //...
         }
         public void onProgressUpdate(DataSource<T> dataSource) {
           //...
         }
       }
       mDataSource.subscribe(dataSubscriber, mUiThreadImmediateExecutor);
     }
     ```

     * `controller`进行`attach`有两条路径
       1. 当进行赋值设置`controller`时会把`controller`给`attach`
       2. 当`DraweeView#onAttachedToWindow()`时也会尝试将当前已赋值的`controller`进行`attach`
       3. `detach`同理

  2. `DataSource`获取、首个`Producer`获取并注入`DataSource`

     ```java
     //AbstractDraweeController
     protected void submitRequest() {
       //非常核心的一行，去获取dataSource
     	mDataSource = getDataSource();
     }
     
     //从AbstractDraweeController#getDataSource()开始调用，会调到ImagePipeline#fetchDecodedImage()
     //ImagePipeline
     public DataSource<CloseableReference<CloseableImage>> fetchDecodedImage() {
       //首个Producer获取，一般是BitmapMemoryCacheProducer
       Producer<CloseableReference<CloseableImage>> producerSequence =
               mProducerSequenceFactory.getDecodedImageProducerSequence(imageRequest);
       return submitFetchRequest(producerSequence, ...)
     }
     
     private <T> DataSource<CloseableReference<T>> submitFetchRequest() {
       //将首个Producer给传进去，new一个CloseableProducerToDataSourceAdapter
       return CloseableProducerToDataSourceAdapter.create(producerSequence, settableProducerContext);
     }
     ```

  3. 首个`Producer`开始执行`produceResults`，并注册`Consumer`

     ```java
     //CloseableProducerToDataSourceAdapter
     private CloseableProducerToDataSourceAdapter() {
       //构造函数直接走到super，super是AbstractProducerToDataSourceAdapter
       super(producer, settableProducerContext, listener);
     }
     
     //AbstractProducerToDataSourceAdapter
     protected AbstractProducerToDataSourceAdapter() {
       //开始调用producer.produceResults了，并且注册了回调createConsumer()
       //这里的producer是BitmapMemoryCacheProducer
       producer.produceResults(createConsumer(), settableProducerContext);
     }
     
     private Consumer<T> createConsumer() {
       return new BaseConsumer<T>() {
         protected void onNewResultImpl(T newResult) {
           //注册的回调就是给dataSource的实现类result赋值，这样接口方法`T getResult();`才能返回
           AbstractProducerToDataSourceAdapter.this.onNewResultImpl(newResult);
         }
       }
     }
     ```

* 各种各种的`Producer`

  1. `BitmapMemoryCacheProducer`

     * 尝试从内存缓存中找`Bitmap`

     * 包装`consumer`，将`result`存入`mMemoryCache`

       ```java
       //内存缓存容器
       private final MemoryCache<CacheKey, CloseableImage> mMemoryCache;
       //cacheKey容器
       private final CacheKeyFactory mCacheKeyFactory;
       //下一个Producer -> ThreadHandoffProducer
       private final Producer<CloseableReference<CloseableImage>> mInputProducer;
       
       public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext context) {
         //...代码太多了，讲一下代码的逻辑
         
         //看下isBitmapCacheEnabled，开启就从根据key从内存缓存map里找，cacheKey默认是图片url，找到的话就执行回调然后返回，如下图
         if(cachedReference != null) {
           consumer.onNewResult(cachedReference);
           return;
         }
         
         //没找到就将请求委托给mInputProducer
         mInputProducer.produceResults(wrappedConsumer, producerContext);
       }
       
       //包装consumer
       protected Consumer<CloseableReference<CloseableImage>> wrapConsumer() {
         return new DelegatingConsumer {
           public void onNewResultImpl() {
             //是否支持写入缓存
             if (isBitmapCacheEnabledForWrite) {
               //写入缓存
               newCachedResult = mMemoryCache.cache(cacheKey, newResult);
             }
             //写入缓存后才执行原来的consumer的onNewResult()
             getConsumer().onNewResult()
           }
         }
       }
       ```

  2. `ThreadHandoffProducer` -> 不找，将任务委托到非UI线程

     ```java
     //下一个Producer -> BitmapMemoryCacheKeyMultiplexProducer(父类是MultiplexProducer)
     private final Producer<T> mInputProducer;
     //一个queue，里面有ThreadPoolExecutor
     private final ThreadHandoffProducerQueue mThreadHandoffProducerQueue;
     
     public void produceResults(Consumer<T> consumer, ProducerContext context) {
       // new了一个runnable出来，在这个runnable内会将图片请求委托给mInputProducer
     	Runnable<T> runnable = new StatefulProducerRunnable {
         protected void onSuccess(@Nullable T ignored) {
           mInputProducer.produceResults(consumer, context);
         }
       }
       //将runnable添加到异步线程池里面等待执行
       mThreadHandoffProducerQueue.addToQueueOrExecute(runnable);
     }
     ```

  3. `MultiplexProducer` -> 不找，Producer for combining multiple identical requests into a single request.

     ```java
     //下一个Producer -> BitmapMemoryCacheProducer
     private final Producer<T> mInputProducer;
     
     public void produceResults(Consumer<T> consumer, ProducerContext context) {
       //组合的过程比较复杂，与请求过程关系不大，先跳过不看了
       mInputProducer.produceResults(forwardingConsumer, multiplexProducerContext);
     }
     ```

  4. `BitmapMemoryCacheProducer` -> 又找了一次`Bitmap`内存缓存，简直离谱

     ```java
     //下一个Producer -> DecodeProducer
     private final Producer<CloseableReference<CloseableImage>> mInputProducer;
     
     public void produceResults(Consumer<CloseableReference<CloseableImage>> consumer, ProducerContext context) {
       //没找到就将请求委托给mInputProducer
       mInputProducer.produceResults(wrappedConsumer, producerContext);
     }
     ```

  5. `DecodeProducer` -> 将解码任务封在`consumer`里往下传递

     ```java
     public class DecodeProducer {
       public void produceResults() {
         ProgressiveJpegParser jpegParser = new ProgressiveJpegParser(mByteArrayPool);
         progressiveDecoder =
                 new NetworkImagesProgressiveDecoder(consumer,jpegParser);
         mInputProducer.produceResults(progressiveDecoder)
       }
     }

  6. `ResizeAndRotateProducer` 

     * Resizes and rotates images according to the EXIF orientation data or a specified rotation angle. 

     * 包一层`consumer`进行`resize、rotate`

       ```java
       public class ResizeAndRotateProducer {
         public void produceResults(final Consumer<EncodedImage> consumer, final ProducerContext context) {
           mInputProducer.produceResults(
               new TransformingConsumer(consumer, context, mIsResizingEnabled, mImageTranscoderFactory),
               context);
         }
       }
       ```

  7. `AddImageTransformMetaDataProducer` ->

     * Add image transform meta data producer. 

     * 包一层`consumer`

       ```java
       public class AddImageTransformMetaDataProducer {
         public void produceResults(Consumer<EncodedImage> consumer, ProducerContext context) {
           mInputProducer.produceResults(new AddImageTransformMetaDataConsumer(consumer), context);
         }
       }
       ```

  8. `EncodedMemoryCacheProducer` 

     * 从内存缓存中找`encoded image`
     * 包一层`consumer`将结果写入`mMemoryCache`

  9. `DiskCacheReadProducer` -> 从磁盘中找

  10. `DiskCacheWriteProducer` -> 仅仅是包了个`consumer`用于存磁盘缓存

  11. `NetworkFetchProducer`

* 网络数据转成图片

  1. 把网络流转换为`EncodeImage`

     ```java
     public class NetworkFetchProducer {
       protected static void notifyConsumer(PooledByteBufferOutputStream pooledOutputStream) {
         encodedImage = new EncodedImage(result);
       }
     }
     ```

  2. 决定图片的类型

     ```java
     public class DefaultImageFormatChecker {
       public final ImageFormat determineFormat(byte[] headerBytes, int headerSize) {
         if (isJpegHeader(headerBytes, headerSize)) {
           return DefaultImageFormats.JPEG;
         }
     
         if (isPngHeader(headerBytes, headerSize)) {
           return DefaultImageFormats.PNG;
         }
       }
     }
     ```

  3. 将`EncodeImage`变为`Bitmap`

     ```java
     public abstract class DefaultDecoder {
       private CloseableReference<Bitmap> decodeFromStream() {
         //EncodedImage只是包含所有信息，没有被解码。可以从中获取流
         InputStream in = encodedImage.getInputStream();
         //调用系统的方法，将流变成Bitmap。系统方法会调用到native
         Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream, null, options);
       }
     }
     ```

  4. 将`Bitmap`变成`CloseableStaticBitmap`

     ```java
     public class DefaultImageDecoder {
       public CloseableStaticBitmap decodeJpeg() {
         //先从`EncodeImage`变为`Bitmap`
         CloseableReference<Bitmap> bitmapReference = ...;
         //在从`Bitmap`变成`CloseableStaticBitmap`
          CloseableStaticBitmap closeableStaticBitmap = new CloseableStaticBitmap(bitmapReference);
       }
     }
     ```

  5. 将`CloseableStaticBitmap`变成`BitmapDrawable`

     ```java
     public class DefaultDrawableFactory {
       public Drawable createDrawable(CloseableImage closeableImage) {
         Drawable bitmapDrawable =
                 new BitmapDrawable(mResources, closeableStaticBitmap.getUnderlyingBitmap());
       }
     }
     ```

  6. 不同的中间物意义

    1. `PooledByteBufferOutputStream` - 这是网络流
    2. `EncodeImage` 
       * it is implemented a lightweight wrapper around an encoded byte stream.
       * Encoded image data is a compressed representation of an image that has been prepared for storage or transmission. 
       * Encoded images are generally smaller in size than decoded images because they have been compressed to reduce their file size. 
       * it cannot be directly displayed on a screen without first being decoded.
       * it takes up very little memory compared to the decoded image data. This allows Fresco to load and cache multiple images at once without using excessive memory.
    3. `DecodeImage`
       * Decoded image data  is the uncompressed representation of an image that can be directly displayed on a screen.
       * Decoded images are generally larger in size than their encoded counterparts because they contain all of the image data necessary for display, such as pixel values and color information.
       * Decoding an encoded image involves unpacking the compressed data and reconstructing the original image.
       * The process of decoding an encoded image typically involves reading the encoded image data from disk or network, decompressing the data, and constructing a DecodedImage object that represents the resulting bitmap or other format suitable for display.
    4. `Bitmap`
       * DecodedImage is a higher-level abstraction used by Fresco to manipulate and manage the image data, while Bitmap is a lower-level representation of the actual pixel data that can be displayed on a screen.

* 不同level drawable显示原理

  * `DraweeView`始终都只设置了一个`Drawable`

    ```java
    public void setHierarchy(DH hierarchy) {
      mDraweeHolder.setHierarchy(hierarchy);
      //设置drawable
      super.setImageDrawable(mDraweeHolder.getTopLevelDrawable());
    }
    
    public void setController(@Nullable DraweeController draweeController) {
      mDraweeHolder.setController(draweeController);
      //设置drawable
      super.setImageDrawable(mDraweeHolder.getTopLevelDrawable());
    }
    
    @Deprecated
    public void setImageDrawable(@Nullable Drawable drawable) {
      init(getContext());
      mDraweeHolder.setController(null);
      //过期的方式，别去用
      super.setImageDrawable(drawable);
    }
    ```

  * `ImageView#Drawable` = `getTopLevelDrawable()` = `RootDrawable` = `FadeDrawable`

    ```java
    public class GenericDraweeHierarchy {
      private final RootDrawable mTopLevelDrawable;
      
      GenericDraweeHierarchy() {
        mTopLevelDrawable = new RootDrawable(mFadeDrawable); //将fadeDrawable给包了一层
      }
      
      public Drawable getTopLevelDrawable() {
        return mTopLevelDrawable; //对外暴露的方法
      }
    }
    ```

  * `FadeDrawble`原理

    ```java
    //一个drawable容器
    private final Drawable[] mLayers;
    //动画时间
    int mDurationMs;
    //每个level(index)的透明度
    int[] mAlphas;
    //Determines whether to fade-out a layer to zero opacity (false) or to fade-in to the full opacity (true)
    boolean[] mIsLayerOn;
    //The index of the layer that contains the actual image 
    private final int mActualImageLayer;
    
    public FadeDrawable(Drawable[] layers, int actualImageLayer) {
      //赋一个值
      mLayers = layers;
      //初始化每一层的alpha
      mAlphas = new int[layers.length];
      //把真正显示的层给设置好
      mActualImageLayer = actualImageLayer;
    }
    
    //设置某一层的drawable可见
    public void fadeInLayer(int index) {
      mTransitionState = TRANSITION_STARTING; //设置drawing的state
      mIsLayerOn[index] = true; //将isOn设置成true
      invalidateSelf(); //请求redraw
    }
    
    //原理同上
    public void fadeOutLayer(int index) {
      mTransitionState = TRANSITION_STARTING;
      mIsLayerOn[index] = false;
      invalidateSelf();
    }
    
    //专门只显示某一层
    public void fadeToLayer(int index) {
      mTransitionState = TRANSITION_STARTING;
      Arrays.fill(mIsLayerOn, false);
      mIsLayerOn[index] = true;
      invalidateSelf();
    }
    
    //核心的draw
    public void draw(Canvas canvas) {
      switch (mTransitionState) {
        case TRANSITION_RUNNING:
          done = updateAlphas(ratio); //核心语句，draw时更新alpha
          //更新状态
          mTransitionState = done ? TRANSITION_NONE : TRANSITION_RUNNING;
          break;
      }
      for (int i = 0; i < mLayers.length; i++) {
        //上面alpha数组更新好后，遍历更新每一层drawable的alpha
        drawDrawableWithAlpha(canvas, mLayers[i], (int) Math.ceil(mAlphas[i] * mAlpha / 255.0));
      }
    }
    
    private boolean updateAlphas(float ratio) {
      for (int i = 0; i < mLayers.length; i++) {
        //更新数组内的alpha
        mAlphas[i] = (int) (mStartAlphas[i] + dir * 255 * ratio);
      }
    }
    ```

  * 总结

    1. 只有一个`RootDrawable`，但这个`RootDrawable`包装了`FadeDrawable`，`FadeDrawable`是一个`Drawable`数组容器
    2. 显示不同的层是通过设置`alpha`来控制`draw`实现的

* `PostProcessor`原理

  1. `new`一个 `PostprocessorProducer`，并将`BaseProducer`传进去当作下一个`inputProducer`

     ```java
     //ImagePipeline
     public DataSource<CloseableReference<CloseableImage>> fetchDecodedImage() {
       //这个入口会去获取首个producer
       Producer<CloseableReference<CloseableImage>> producerSequence =
               mProducerSequenceFactory.getDecodedImageProducerSequence(imageRequest);
       return submitFetchRequest(...)
     }
     
     //ProducerSequenceFactory
     public Producer getDecodedImageProducerSequence(imageRequest) {
       //基础的Producer
       Producer<> pipelineSequence = getBasicDecodedImageSequence(imageRequest);
       //如果有Postprocessor，就将producer包一层，将pipelineSequence作为下一个Producer
       if (imageRequest.getPostprocessor() != null) {
         //开始包一层
         pipelineSequence = getPostprocessorSequence(pipelineSequence);
       }
       //返回包了一层的Producer
       return pipelineSequence;
     }
     private Producer getPostprocessorSequence() {
       //将刚才的Producer当成inputProducer传进去
       PostprocessorProducer postprocessorProducer = mProducerFactory.newPostprocessorProducer(inputProducer);
     }
     ```

  2. `PostprocessorProducer`的处理实际上是把`Consumer`给包一层

     ```java
     //PostprocessorProducer
     public void produceResults() {
       //从ImageRequest中获取Processor
       Postprocessor postprocessor = context.getImageRequest().getPostprocessor();
       //将原来的Consumer用PostprocessorConsumer包一层
       PostprocessorConsumer postprocessorConsumer = new PostprocessorConsumer(consumer, listener, postprocessor, context);
       //啥都不干，直接让mInputProducer去produce图片。把包好的consumer给传进去
       mInputProducer.produceResults(postprocessorConsumer, context);
     }
     ```

  3. `PostprocessorConsumer`处理

     1. `DelegatingConsumer`

        * 以下代码示例如何将`Consumer`给包一层
     
        * 泛型表示这个`DelegateConsumer`的`输入`和`输出`
     
          ```java
          public abstract class DelegatingConsumer<I, O> extends BaseConsumer<I> {
            private final Consumer<O> mConsumer;
            public DelegatingConsumer(Consumer<O> consumer) {
              mConsumer = consumer;
            }
            public Consumer<O> getConsumer() {
              return mConsumer;
            }
            
            protected void onFailureImpl(Throwable t) { mConsumer.onFailure(t);}
          }
          ```
     
     2. `PostprocessorConsumer` -> 将内层`Consumer`给拦截住，处理结束后在通知内层`Consumer`
     
        ```java
        //PostprocessorConsumer
        private final Postprocessor mPostprocessor;
        //涉及异步线程操作了
        private final Executor mExecutor;
        
        //缓存、网络等producer返回结果了
        protected void onNewResultImpl(CloseableReference<CloseableImage> newResult, @Status int status) {
          //开始准备Postprocessing
          submitPostprocessing();
        }
        
        private void submitPostprocessing() {
          mExecutor.execute(
            new Runnable() {
              @Override
              public void run() {
                //在异步线程中执行Postprocessing
                doPostprocessing(closeableImageRef, status);
              }
            }
        }
        
        private void doPostprocessing(CloseableReference<CloseableImage> sourceImageRef) {
          //核心处理的三行代码
          CloseableStaticBitmap staticBitmap = (CloseableStaticBitmap) sourceImage;
          Bitmap sourceBitmap = staticBitmap.getUnderlyingBitmap();
          CloseableReference<Bitmap> bitmapRef = mPostprocessor.process(sourceBitmap, mBitmapFactory);
          //处理后的Bitmap
          destImageRef = new CloseableStaticBitmap(bitmapRef);
          //获取内容的consumer，然后将新图片通知给内层consumer
          getConsumer().onNewResult(destImageRef, status);
        }
        ```

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
