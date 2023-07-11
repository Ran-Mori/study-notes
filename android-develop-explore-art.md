# Android开发探索艺术

## 第一章

### 生命周期

* `onStart()` 和 `onResume()` 的区别

  1. onStart() -> It is called when the activity becomes visible to the user, but it may not have the input focus. It is typically called after the `onCreate()` method has completed execution. This method is often used to initialize components that are required for the activity to be visible to the user.
  2. onResume() -> It is called when the activity starts interacting with the user. It is called after `onStart()` and it indicates that the activity is now in the foreground and has the user's input focus. This is the point where the activity is considered active and the user can start interacting with it, such as entering text or tapping on buttons.

* A启动B，`A#onPause()`和`B#onResume()`谁先执行

  * 日志

    ```bash
    IzumiSakai: MainActivity onCreate
    IzumiSakai: MainActivity onStart
    IzumiSakai: MainActivity onResume
    # Main跳Second
    IzumiSakai: MainActivity onPause
    IzumiSakai: SecondActivity onCreate
    IzumiSakai: SecondActivity onStart
    IzumiSakai: SecondActivity onResume
    IzumiSakai: MainActivity onStop
    # Second返回Main
    IzumiSakai: SecondActivity onPause
    IzumiSakai: MainActivity onStart
    IzumiSakai: MainActivity onResume
    IzumiSakai: SecondActivity onStop
    IzumiSakai: SecondActivity onDestroy
    ```

  * 分析

    * 先执行`A#onPause()`，因为`onResume()`代表已经可以和用户交互，此Activity已在前台，只有放弃这些才能让下一个Activity具有这些特性
    * `B#onResume()`执行完后才执行`A#onStop()`，这样的调度更有利于`B`快速启动
    * 按下返回键同理

  * 原因

    * 核心的原因还是调度代码就是这样写的

  * 结论

    * `onPause()`中不能执行重量操作，因为这样会影响到下一个`Activity`的启动速度；重量耗时操作放到`onStop()`中进行

### onSaveInstanceState()

* 谁的方法 -> Activity&View

* 调用时机 - 异常退出，如配置改变，内存压力大被回收

* 配套成对方法 -> `onRestoreInstanceState(Bundle)`

* Activity的实现

  ```java
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    // 通过window，从根节点遍历整棵View树，调用View#onSaveInstanceState()
    outState.putBundle(WINDOW_HIERARCHY_TAG, mWindow.saveHierarchyState());
  	// 保存Fragment的状态
    Parcelable p = mFragments.saveAllState();
    getAutofillClientController().onSaveInstanceState(outState);
    dispatchActivitySaveInstanceState(outState);
  }
  ```

* 特点

  * View只保存有`id`的，因为恢复的时候要用id做key
  * 在`onStop()`后进行

* View#onSaveInstanceState()

  * 系统已自动保存了一些内容，如果觉得不够可以复写新增
  * for example -> the current cursor position in a text view (but usually not the text itself since that is stored in a content provider or other persistent storage), the currently selected item in a list view


### 活动优先级

* 优先级
  * 前台Activity最高
  * 可见但非前台，比如`onPause()`后Activity 中弹出了一个对话框，导致Activity可见但是位 于后台无法和用户直接交互
  * 后台，onStop()的优先级最低
* 作用
  * 系统资源不足时，会按照优先级回收Activity或其他四大组件
* 经验
  * 如果一个进程中没有任何四大组件运行，那么这个进程很容易被杀死
  * 因此后台的耗时任务不能直接挂在后台，最好绑定一个`Service`，这样才不会被杀死

### 启动模式

* taskAffinity

  * 怎么用

    ```xml
    <activity
        android:name=".SecondActivity"
        android:taskAffinity="com.example.task1"
        android:exported="false" />
    ```

  * 作用

    1. 指定一个Activity启动的任务栈
    2. 配合`singleTask` 和 `singleInstance`使用

  * 特点

    1. 如果不指定，一个应用所有的Activity的taskAffinity是一样的，就是应用包名
    2. 如果要自己指定taskAffinity，那一定不能是包名，不然等于没有指定

* 四种模式

  * standard

    * 无论什么情况，都新建一个`Activity`
    * 谁启动了这个Activity，那这个Activity就在启动它的Activity的栈中

  * singleTop

    * 和standard差不多，有且仅在栈顶重复启动时不会新建

    * 栈顶启动同一个，会调用`onNewIntent()`方法

    * 日志

      ```java
      # 栈顶startActivity()
      IzumiSakai: MainActivity onPause
      IzumiSakai: MainActivity onNewIntent
      IzumiSakai: MainActivity onResume
      ```

  * singleTask

    * 具有singleTop的所有属性
    * 当启动Activity  A时，系统首先会寻找是否存在A想要的任务栈，如果不存在，就重新创建一 个任务栈，然后创建A的实例后把A放到栈中。如果存在A 所需的任务栈，这时要看A 是否在栈中有实例存在，如果有实例存在， 那么系统就会把 A 上面的Activity全部出栈，让A为栈顶，并调用它的onNewIntent()方法，如果实例不存在，就创建A的实例并把A压入栈中。

  * singleInstance

    * 具有singleTask的所有属性
    * 具有此种模式的Activity只能单独地位于一个任务栈中

* `adb shell dump activity activities`

  * 输出示例

    ```java
    // 一个Task，Task的名字是com.example.task1
    * Task{ type=standard com.example.task1 }
        topResumedActivity=ActivityRecord{com.activity/.SecondActivity} 
        * Hist  #0: ActivityRecord{com.activity/.SecondActivity}
    			packageName=com.activity
          launchedFromPackage=com.activity // 由谁启动
        	Intent { flg=0x10000000 cmp=com.activity/.SecondActivity }
    			taskAffinity=com.example.task1 // 指定了taskAffinity
    // 另一个Task，名字是com.activity
    * Task{ type=standard com.activity }
        mLastPausedActivity: ActivityRecord{ com.activity/.MainActivity }
      	* Hist  #0: ActivityRecord com.activity/.MainActivity }
    			packageName=com.activity
          launchedFromPackage=com.google.android.apps.nexuslauncher // 由launcher启动
          taskAffinity=com.activity //默认taskAffinity，和包名一样
    // launcher对应Task，type为home
    * Task{ type=home }
    	mLastPausedActivity: ActivityRecord{ com.google.android.apps.nexuslauncher/.NexusLauncherActivity }
    	* Hist  #0: ActivityRecord{ com.google.android.apps.nexuslauncher/.NexusLauncherActivity }
    ```

  * 解读

    * 一个三个Task，前面两个是应用自定义的，第三个是默认`Launcher`

* 几个类

  * `ActivityRecord` -> It represents an instance of an activity in the Android system.

    ```java
    final class ActivityRecord {
      ActivityInfo info;
    }
    ```

  * `TaskRecord` -> It represents the details of a specific task and maintains information about the activities associated with it.

    ```java
    final class TaskRecord {
      ActivityStack stack; // 持有Stack变量
      ArrayList<ActivityRecord> mActivities;
    }
    ```

  * `ActivityTask` -> It represents a collection of activities that are related to a specific task.

    ```java
    final class ActivityStack {
      ArrayList<TaskRecord> mTaskHistory; // 一个Stack可以对应多个Task
    }

* 任务栈

  * 一个应用一般就是一个任务栈，`taskAffinity`一般就是app的包名
  * 一个应用也可以多个任务栈，当多个任务栈时，向上滑动查看后台任务时可以发现一个应用有多张应用卡片


***

## 第二章

### windows进程间通信

* 剪贴板
* 管道
* 邮槽

### 单应用多进程情况

* 某模块因为各种原因必须要运行在单独的进程
* 申请更多的内存

### 如何开启多进程

* `AndroidMenifest.xml`中声明

```xml
<activity
    android:name = ""
    android:process =":remote" />
```

* 有且仅有这一种方法，不支持运行时动态设置

### 进程命名

* `:remote`：实际进程名为`包名:remote`
* 带有`:`的是私有进程，不带`:`的是公有进程
* 公有进程可以通过`ShareUID`方式共享

### 进程-应用-虚拟机关系

* 一个`process`对应一个`Application`对应一个`Virtual Machine`
* 因此静态变量、单例模式、线程同步机制会因为多进程而全部失效

### SP

* SP底层实际上是通过修改`xml`文件来实现数据共享

### IPC

* Bundle
* 文件共享。总体可行，但要适当同步控制并发
* SP。在内存有缓存策略，并发多进程容易拿到脏数据
* Messenger。对AIDL的一个封装，方便使用
* Binder
* ContentProvider
* Socket

### Serializable

* 是一个空接口
* 通过`ObjectOutputStream`和`ObjectInputStream`来实现序列化
* 静态成员不属于对象不参与序列化
* `transient`标记的也不参与序列化

### serialVersionUID

* 最好加上，虽然不加也可以
* 如果类中定义有就用定义的，如果没有就用类的`hashCode`
* 在序列化时会将此值存入序列化文件；在反序列化时会将文件的值和对象类的值进行比较，如果不一样就序列化失败
* 因此建议最好加上这个值

### Parcelable接口

* 用于`Android`的序列化和反序列化
* 比较复杂，里面待实现方法和变量很多
* 有很多实现类可以直接用，如`Intent、Bundle、Bitmap`，集合也可以序列化，但要求集合内的元素都可序列化

### 两者对比

* S简单但开销大，P复杂但开销小
* Android建议使用P

### Binder

* 实现了`IBinder`接口
* 虚拟的物理设备，设备驱动是`/dev/binder`
* 是连接`Manager`和`Service`的桥梁
* 是客户端、服务端进行通信的媒介

### Binder核心

* `boolean onTransact(int code, Parcel data, Parcel reply, int flags)`
* `boolean transact(int code, Parcel data, Parcel reply, int flags)`

### binder连接池

* `Service`资源宝贵，且会显示在前台，不能随便多创建
* 因此服务端只有一个Service，不同的AIDL都要转发到这个Service中去
* 服务端有一个`queryBinder()`接口，能将不同的AIDL映射到对应的Binder
* 然后不同的binder都绑定相同的Service

***

## 第三章

### View类

* 三万行，离谱
* `ViewGroup`继承自`View`，它即是一个`View`，也是一个`View Container`
* `LinearLayout`即是一个`View`，也是一个`View容器`
* `View`树和`DOM`树不能说毫无差别，只能说一模一样

### 真实位置和看到位置

* View动画不改变View的真实位置，只改变View看到的位置
* 即动画过后看到新的位置不能点击，但原来的位置是可以点击的。因为它的真实位置根本就没有变化

### 位置参数

* `left`：父容器最左边到此View最左边的宽度 - 真实位置
* `right`：父容器最左边到此View最右边的宽度 - 真实位置
* `top`：父容器最上面到此View最上面的宽度 - 真实位置
* `bottom`：父容器最上面到此View最下面的宽度 - 真实位置
* `x`：可视位置的left - 可视位置
* `y`：可视位置的top - 可视位置
* `translationX`：可视位置相对于真实位置的left - 当两位置相同时为零
* `translationY`：可视位置相对于真实位置的top - 当两位置相同时为零
* `x、y`变化，`left、top`不变

### 滑动冲突场景

* 场景一 —— 类似于`ViewPager`，一个左右滑，一个上下滑
* 场景二 —— 两层`View`，且滑动方向还相同
* 场景三 —— 场景一和场景二复合

### 解决方式

* 场景一 —— 根据滑动的方向来判断
* 场景二 —— 自定义规则来判断
* 场景三 —— 拆分成场景一和场景二解决

### 处理规则

* 父容器拦截点击事件，如果父容器要处理就处理，不处理就将事件交给子容器

***

## 第四章

### 自定义View

1. 概述

   * 比较难

   * 是一个综合技术体系，涉及View的层次结构、事件分发机制和View的工作原理细节

2. 分类

   * 继承`View`：需重写`onDraw()`，重写`onMeasure()`支持`wrap_content`，`padding`也要自己处理

   * 继承`ViewGroup`：自己处理`onMeasure()、onLayout()`，并还要顺带处理子元素的测量和布局

   * 继承特定的`View`：简单

   * 继承特定的`Layout`：简单

3. 须知

   * 继承自View类不支持`wrap_content`

   * 继承自View类不支持`padding`

   * 继承自ViewGroup类需要考虑padding和子元素margin的影响

   * 不要在View中使用handler，因为View有post方法

   * View有线程或者动画要及时停止，一般在`onDetachFromWindow()`中处理，不然内存泄漏

   * 有嵌套处理要自己解决滑动冲突


### padding和margin

* `margin`是父容器控制的，一般`ViewGroup`要在`onMeasure()`和`onLayout`中使`margin`生效
* `padding`是每个`View`自己控制的，一般要在`onMeasure()`和`onDraw()`中使`padding`生效

### View.post()和Handler.post()

* 如果view已经attach到window了，那么View#post和Handler#post作用一样，都是往调用UI主线程的MessageQueue中扔Runnable
* 如果view还未attach到window中，则需要通过一个缓存队列将Runnable暂时先缓存起来，等到view attach到window上之后，再将缓存队列中的Runnable取出来，再扔到UI线程的MessageQueue中

### 参考文档

* [Android自定义ViewGroup的OnMeasure和onLayout详解](https://blog.csdn.net/tuke_tuke/article/details/73379123)

***

## 第五章

### RemoteView

* 在其他进程中显示
* 有一套api支持跨进程更新它的页面
* 主要用于通知栏和桌面小部件
* 其他进程一般就是指系统的SystemServer进程

***

## 第七章

### 三种动画

* View动画
* 帧动画
* 属性动画

### View动画

* 写在`res/anime`下
* 是一个`xml`文件
* 通过`R.anim.xmlname`引入使用

### 帧动画

* 原理：连续的放图片，容易OOM
* 通过xml定义一个AnimationDrawable
* 把这个Drawable作为图像的background即可实现动画

### LayoutAnimation

* 是一种特殊的View动画
* 作用于`ViewGroup`，这样它的子元素出场就有动画效果
* 常用于`RecycleView`
* 还是通过xml文件来指定

### Activity的切换效果

* 重写`Activity.overridePendingTransition(int enterAnim, int exitAnim)`
* 动画id实际上也是一个xml文件

### 属性动画

* 动态改变图像的属性，只支持API 11以上。低级的需要用兼容库
* 只要图像有这个属性，就能够使用属性动画
* 需要图像提供对应属性值的`get`和`set`方法

### ObjectAnimator

* 继承自`ValueAniamtor`
* `public static ObjectAnimator ofInt(Object target, String propertyName, int... values)`
* 比`ValueAnimator`封装度更高一点
* 透明度这种可以使用，但无`onEndListener`这种就比较不方便

### Interpolator

* 根据时间流逝百分比计算当前属性值改变的百分比
* 注意核心是计算出百分比，真正的值为多少与它无关
* 线性插值器
* 加速插值器
* 减速插值器

### Evaluator

* 根据当前属性改变的百分比来计算改变后的属性值
* `public Integer evaluate(float fraction, Integer startValue, Integer endValue)`
* 得到百分比估算出实际的属性值

### Button不能动画改变其width

* `Button`继承了`TextView`
* `Button.getWidth()`是`View.getWidth()`
* `Button.setWidth()`是`TextView.setWidth()`
* 因此button的宽度不能使用属性动画

### 使用动画注意事项

* 帧动画OOM
* 兼容性问题，低版本不支持属性动画
* View动画是对View影像做动画，并不能改变View的状态

***

## 第八章

### Window

* `WindowManager`和`WindowManagerService`交互是IPC过程，通过`binder`
* Android所有视图，如`Acticity、Dialog、Toast`视图都是通过`Window`来呈现的
* `Window`是`View`的直接管理者
* 事件由`Window`传给`DecorView`

### Window的三种类型

* 应用Window：对应一个Activity
* 子Window：必须存在于父Window中
* 系统WIndow：比如Toast和系统状态栏。声明系统Window是需要`AndroidManifest.xml`中声明权限

### Window的分层

* window显示和图层的覆盖一样
* 图层高低用`层级范围`表示

### WindowManager方法

* `WindowManager`继承自`ViewManager`

```java
public interface ViewManager
{
    public void addView(View view, ViewGroup.LayoutParams params);
    public void updateViewLayout(View view, ViewGroup.LayoutParams params);
    public void removeView(View view);
}
```

* 三个方法 —— 增、删、改
* 平时拖动Window的效果：根据手指位置设置`LayoutParams`的值

```java
mLayoutParams.x = rawX;
mLayoutParams.y = rawY;
mWindowManager.updateViewLayout(view, layoutParam);
```

### 理解

* `Window`是一个抽象概念，且其自身就是一个抽象类
* `WindowManger`是外界访问`Window`的入口
* 真正的实现在`WMS`。`WM、WMS`通过`binder`机制IPC通信
* 在创建视图并显示出来时，首先是通过创建一个Window对象，然后通过WindowManager对象的 `addView(View view, ViewGroup.LayoutParams params)`; 方法将 contentView 添加到Window中，完成添加和显示视图这两个过程
* 在关闭视图时，通过WindowManager来移除DecorView， mWindowManager.removeViewImmediate( view)
* Toast比较特殊，具有定时取消功能，所以系统采用了Handler

***

## 第九章

### 例外特点

* 只有`BroadcastReceiver`不需要在`AndroidManifest.xml`中注册
* 调用方式上，只有`ContentProvider`不需要借助`intent`

### Activity

* 唯一一个对用户可感知的组件，其他三个都感知不到
* 展示一个界面和用户交互，扮演前台角色

### Service

* 一种计算型组件，用于在后台执行一系列任务
* 默认运行在主线程，需要手动设置才能进入异步模式
* 启动状态：后台异步进行计算操作，不需要和外面直接交互
* 绑定状态：同样可以进行计算操作，但可以很方便和外面交互

### BroadCaseReciver

* 一种消息型组件，用于在不用组件甚至不同应用之间传递消息
* 发送接收过程通过`intent-filter`来过滤
* 可以实现低耦合的观察者模式
* 不适合做耗时操作

### ContentProvider

* 内部维持着一份数据集合
* 数据集合的实现没有要求，可以使用数据库，也可以使用集合

### 剩余内容

* 全是源码分析
* 不想看了

***

## 第十章

### 概述

* `handdler`是android消息机制的上层接口，开发人员只要和它打交道就行了
* `handler`经常拿来做异步处理耗时任务，UI线程处理UI。但并不代表handler只用来更新UI
* `UI线程` = `主线程` = `ActivityThread`

### 限制UI线程更新UI

* 代码逻辑

```java
class ViewRootImpl() {
  if(mThread != Thread.currentThread) {
    throw new CalledFromWrongThreadException("only the original thread can ...")
  }
}
```

* Android的UI控件是线程不安全的，但没有采取`加锁同步机制`而是采取`单线程模型处理机制`
* 加锁会使用UI控件的访问更加复杂，且加锁会降低UI控件访问操作的效率
* 因此Android最终选择单线程处理模型，配合handler机制

### ThreadLocal

* 是一个线程内部的数据存储类
* 当某些数据是以线程为作用域且不同线程需要不同的副本时，就可以采取`ThreadLocal`
* 虽然在不同线程中访问的是同一个`ThreadLocal`对象，但是获取到的值确实不一样的，这就做到了线程隔离

### 主函数

* Android是一个java程序，当然有`public static main(String[] args)`方法

* 这个方法在`ActivityThread`里面

  ```java
  public static void main(String[] args) {
    //初始化主线程的looper
    Looper.prepareMainLooper();
  
    //创建UI线程
    ActivityThread thread = new ActivityThread();
    thread.attach(false, startSeq);
  
    //获取主线程的handler
    if (sMainThreadHandler == null) {
      sMainThreadHandler = thread.getHandler();
    }
  
    //至此整个App永远卡在这一行执行
    Looper.loop();
  
    //能走到这一行说明有问题就抛出异常
    throw new RuntimeException("Main thread loop unexpectedly exited");
  }
  ```


***

## 第十一章

### 三种另外形式

* `AsynsTask`：封装了线程池和`Handler`
* `HandlerThread`：内部可以使用`Handler`
* `IntentService`：
* 上面三者虽然表现形式不同，但本质上依然是传统的线程

### 调度方式

* 线程数量不能无限多
* 除非线程数量小于CPU核心数，否则采用时间片轮转法进行调度，无法做到真正的并行

### AsyncTask

* 使用方式：继承实现几个特定方法
* 参数：`AsyncTask<Params, Progress, Result>`，不需要使用可以设置为`void`
* 核心：`Handler`、`ThreadPoolExcutor`
* `SERIAL_EXECUTOR`：把任务排队
* `THREAD_POOL_EXECUTOR`：真正执行并行任务
* `InternalHandler`：用于异步线程向主线程抛事件，即线程切换

### HandlerThread

* 继承自`Thread`
* 里面有一个`Handler`成员变量
* 且已经`Looper`做了初始化

### IntentService

* `Service`不会没用过，后面再看

### Java线程池

* `Excutor`是一个接口，真正的线程池实现在`ThreadPoolExecutor`
* `ThreadPoolExecutor`通过一系列参数来配置线程池

***

