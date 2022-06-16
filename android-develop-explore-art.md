# Android开发探索艺术

## 第一章

* onPause()和onResume()执行顺序
  * `ActivityA.onPause()`先执行，`ActivityB.onResume()`后执行
  * 这是源码决定的
  * 因此`onPause()`中不能执行重量操作，因为这样会影响到下一个`Activity`的启动速度

* 异常情况
  * 资源相关的配置文件发生改变
  * 系统内存不足

* 资源配置改变
  * 横竖屏切换有可能加载不同的 **图片** 资源文件

* onSaveInstanceState()
  * 异常情况销毁才会调用
  * 正常退出是不会调用的

* View数据保存和恢复
  * 异常退出系统会自动保存和恢复一定的View数据
  * 如文本框用户输入的数据、LIstView滚动位置
  * 保存那些数据取决于每个View的`onSaveInstane()、onRestoreInstanceState()`方法的实现
  * 保存的逻辑是从`Activity`一步步委托

* 恢复位置
  * 一般都放在`onCreate()`中判断不为null进行恢复
  * 但有时候恢复放在`onRestoreInstanceState()`更方便

* 系统资源不足被异常退出
  * 如果一个进程中没有任何四大组件运行，那么这个进程很容易被杀死
  * 因此后台的耗时任务不能直接挂在后台，最好绑定一个`Service`，这样才不会被杀死

* 活动栈和任务栈
  * `ActivityStack`只有一个，用`ActivityRecord`记录对应的`Activity`信息
  * `ActivityTask`有很多个，用`TaskRecord`记录对应的`Activity`信息
  * 一个应用一般就是一个任务栈，`taskAffinity`一般就是app的包名

* SingleTask
  * 系统会创建新任务，并实例化新任务的根 Activity。
  * 但是，如果另外的任务中已存在该 Activity 的实例，则系统会通过调用其 `onNewIntent()` 方法将 intent 转送到该现有实例，而不是创建新实例。
  * Activity 一次只能有一个实例存在。

* SingleInstance
  * 系统不会将任何其他 Activity 启动到包含该实例的任务中
  * 该 Activity 始终是其任务唯一的成员；由该 Activity 启动的任何 Activity 都会在其他的任务中打开

***

## 第二章

* windows进程间通信

  * 剪贴板
  * 管道
  * 邮槽

* 单应用多进程情况

  * 某模块因为各种原因必须要运行在单独的进程
  * 申请更多的内存

* 如何开启多进程

  * `AndroidMenifest.xml`中声明

  ```xml
  <activity
      android:name = ""
      android:process =":remote" />
  ```

  * 有且仅有这一种方法，不支持运行时动态设置

* 进程命名

  * `:remote`：实际进程名为`包名:remote`
  * 带有`:`的是私有进程，不带`:`的是公有进程
  * 公有进程可以通过`ShareUID`方式共享

* 进程-应用-虚拟机关系

  * 一个`process`对应一个`Application`对应一个`Virtual Machine`
  * 因此静态变量、单例模式、线程同步机制会因为多进程而全部失效

* SP

  * SP底层实际上是通过修改`xml`文件来实现数据共享

* IPC

  * Bundle
  * 文件共享。总体可行，但要适当同步控制并发
  * SP。在内存有缓存策略，并发多进程容易拿到脏数据
  * Messenger。对AIDL的一个封装，方便使用
  * Binder
  * ContentProvider
  * Socket

* Serializable

  * 是一个空接口
  * 通过`ObjectOutputStream`和`ObjectInputStream`来实现序列化
  * 静态成员不属于对象不参与序列化
  * `transient`标记的也不参与序列化

* serialVersionUID

  * 最好加上，虽然不加也可以
  * 如果类中定义有就用定义的，如果没有就用类的`hashCode`
  * 在序列化时会将此值存入序列化文件；在反序列化时会将文件的值和对象类的值进行比较，如果不一样就序列化失败
  * 因此建议最好加上这个值

* Parcelable接口

  * 用于`Android`的序列化和反序列化
  * 比较复杂，里面待实现方法和变量很多
  * 有很多实现类可以直接用，如`Intent、Bundle、Bitmap`，集合也可以序列化，但要求集合内的元素都可序列化

* 两者对比

  * S简单但开销大，P复杂但开销小
  * Android建议使用P

* Binder

  * 实现了`IBinder`接口
  * 虚拟的物理设备，设备驱动是`/dev/binder`
  * 是连接`Manager`和`Service`的桥梁
  * 是客户端、服务端进行通信的媒介

* Binder核心

  * `boolean onTransact(int code, Parcel data, Parcel reply, int flags)`
  * `boolean transact(int code, Parcel data, Parcel reply, int flags)`

* binder连接池

  * `Service`资源宝贵，且会显示在前台，不能随便多创建
  * 因此服务端只有一个Service，不同的AIDL都要转发到这个Service中去
  * 服务端有一个`queryBinder()`接口，能将不同的AIDL映射到对应的Binder
  * 然后不同的binder都绑定相同的Service

***

## 第三章

* View类

  * 三万行，离谱
  * `ViewGroup`继承自`View`，它即是一个`View`，也是一个`View Container`
  * `LinearLayout`即是一个`View`，也是一个`View容器`
  * `View`树和`DOM`树不能说毫无差别，只能说一模一样

* 真实位置和看到位置

  * View动画不改变View的真实位置，只改变View看到的位置
  * 即动画过后看到新的位置不能点击，但原来的位置是可以点击的。因为它的真实位置根本就没有变化

* 位置参数

  * `left`：父容器最左边到此View最左边的宽度 - 真实位置
  * `right`：父容器最左边到此View最右边的宽度 - 真实位置
  * `top`：父容器最上面到此View最上面的宽度 - 真实位置
  * `bottom`：父容器最上面到此View最下面的宽度 - 真实位置
  * `x`：可视位置的left - 可视位置
  * `y`：可视位置的top - 可视位置
  * `translationX`：可视位置相对于真实位置的left - 当两位置相同时为零
  * `translationY`：可视位置相对于真实位置的top - 当两位置相同时为零
  * `x、y`变化，`left、top`不变

* Motion相关

  * `getX()、getY()`：触摸位置相对于当前View的位置
  * `getRawX()、getRawY()`：触摸位置相对于物理屏幕左上角的位置
  * `TouchSlop`：被认为是滑动的最小距离。单位是`dp`
  * `VelocityTracker`：滑动速度相关
  * `GestureDetector`：手势检测相关

* scrollTo()、scrollBy()

  * 只改变View内的内容位置，而不能改变View本身的布局位置

* 事件分发研究对象

  * `MotionEvent`事件的分发过程，即当一个`MotionEvent`产生以后，系统需要把这个事件传递给一个具体的View，而这个传递就是分发过程

* 事件机制三方法

  * `View.dispatchTouchEvent(MotionEvent event)`：Pass the touch screen motion event down to the target view, or this view if it is the target.
  * `View.onTouchEvent(MotionEvent event)`：Implement this method to handle touch screen motion events.
  * `ViewGroup.onInterceptTouchEvent(MotionEvent ev)`：This allows you to watch events as they are dispatched to your children, and take ownership of the current gesture at any point.

* 三方法伪代码

  ```kotlin
  fun dispatchTouchEvent(val ev:MotionEvent){
    var consume = false
    if(onInterceptTouchEvent(ev)){
      consume = onTouchEvent(ev)
    } else {
      comsume = child.dispatchTouchEvent(ev)
    }
    return consume;
  }
  ```

  * 是一个递归函数

* 返回值true、false

  * `true`代表消耗了事件，`false`代表未消耗事件

* 传递优先级

  * `onTouchListner`最高，`onTouchEvent`次之，`onClickListner`最低

* 暂时结论

  * 事件序列 - 从`ACTION_DOWN`开始，到`ACTION_UP`结束，中间有很多的`ACTION_MOVE`
  * 一个事件序列正常情况下只能被一个`View`拦截并消耗
  * `View`如果要处理事件，就必修消耗`ACTION_DOWN`事件，否则就向上抛；`View`一旦消耗`ACTION_DOWN`事件，即消耗整个事件
  * `ViewGroup`默认不拦截任何事件，它的`onInterceptTouchEvent`方法默认返回false

* 分发流程

  * 首先传递给`Activity`
  * `Activity`将事件传递给根容器`root view`，一般是`View Group`
  * `View Group`将事件传递给它的`下一层View`
  * `下一层View`传递给`再下一层View`
  * 都没处理就让`Activity`处理

* 滑动冲突场景

  * 场景一 —— 类似于`ViewPager`，一个左右滑，一个上下滑
  * 场景二 —— 两层`View`，且滑动方向还相同
  * 场景三 —— 场景一和场景二复合

* 解决方式

  * 场景一 —— 根据滑动的方向来判断
  * 场景二 —— 自定义规则来判断
  * 场景三 —— 拆分成场景一和场景二解决

* 处理规则

  * 父容器拦截点击事件，如果父容器要处理就处理，不处理就将事件交给子容器

***

## 第四章

* 三大流程

  * `measure`：决定`View`的测量宽和高
  * `layout`：决定`View`四个顶点的位置和实际的宽和高
  * `draw`：将`View`显示绘制在屏幕上

* 三大核心方法

  * `setMeasuredDimension`
  * `setFrame`
  * `convas?.drawxxx()`

* 递归调用

  * 如果是`ViewGroup`，三大流程就如事件分发机制一样做递归调用

* MeasureSpec

  * 高两位是`mode`，低30位是`size`

* 三种`mode`

  * `UNSPECIFIED`：父对子无任何限制，要多大给多大。一般不用
  * `EXACTLY`：父已经检测出了子的精确大小，就给那么大。对应`dp、match_parent`
  * `AT_MOST`：父指定了一个最大值，最大不能超过这个值。对应`wrap_content`

* MeasureSpec和LayoutParams对应关系

  * 系统会将`LayoutParams`在父容器的约束下转换成对应的`MeasureSpec`，然后再根据这个`MeasureSpec`确定`View`的测量宽高 
  * 对于`DecorView`，`MeasureSpec`由窗口尺寸和自身的`LayoutParams`决定
  * 对于其他普通`View`，`MeasureSpec`由父容器的`MeasureSpec`和自身的`LayoutParam`决定

* 父MeasueSpec和子LayoutParams决定子MeasureSpec

  > 横轴为parent，众轴为child
  >
  > 详情在`ViewGrour$getChildMeasureSpec`

  |              | EXACTLY | AT_MOST |
  | ------------ | ------- | ------- |
  | dp           | EXACTLY | EXACTLY |
  | match_parent | EXACTLY | AT_MOST |
  | wrap_content | AT_MOST | AT_MOST |

* Measure

  * 测量`View`的宽度高度
  * `ViewGroup`涉及递归调用
  * `Measure`和`Activity`的生命周期不匹配，不一定取得到。如果没测完，则宽高都返回零

* 测量宽高和实际宽高

  * 测量宽高在measure时获得，实际宽高在layout时候获得
  * 它们俩的值百分之九十九都是一样的
  * 赋值时机measure偏前一点

* 普通View的`onMeasure`过程

  > `ViewGruoup$getChildMeasureSpec` 确定了`View`的`MeasureSpec`是多少
  >
  > `View$getDefaultSize`根据`MeasureSpec`最终确定`View`的宽高

  ```java
  //View$getDefaultSize
  public static int getDefaultSize(int size, int measureSpec) {
    //size是getSuggestedMinimumWidth()
    int result = size;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
  
    switch (specMode) {
      case MeasureSpec.UNSPECIFIED:
        result = size;
        break;
      case MeasureSpec.AT_MOST:
      case MeasureSpec.EXACTLY:
        result = specSize;
        break;
    }
    return result;
  }
  ```

  * `specMode`是`此View`的属性，`specSize`是`此View`设置的具体大小值或父容器能提供的最大值
  * 当`specMode`是`EXACTILY`时，`result = specSize`是无问题的
  * 当`specMode`是`AT_MOST`，此`View`设置为`match_parent`时无问题
  * 当`specMode`是`AT_MOST`，此`View`设置为`wrap_content`就有大问题。因为它的实际效果和`match_parent`一样了，因此自定义必须重写`onMeasure`，否则不支持`wrap_content`

* `ViewGroup`的`onMeasure`过程

  * `ViewGroup$onMeasure`是虚方法，需要每个`layout`自己去实现
  * 但`ViewGroup`有`measureChild`和`measureChildren`方法
  * `LinearLayout`这种先测出所有子View，最后根据子View最后测量自己

* `meseasure`结果获取

  * `measure`结束后就可以获取`View`的宽度和高度了，但`Measure`的过程和`Activity`生命周期不同步，因此常常获取到`View`的宽高为`0`
  * 常常使用`View.post()`来获取`View`的宽和高

* `layout()`方法

  > `public void layout(int l, int t, int r, int b)`

  * `layout`方法确定`View`本身的位置，它定义于`View`类中
  * 主要工作是调用`setFrame()`方法来确定4个点的位置(相对于父容器)，如果布局有改变，则调用`onLayout()`方法来递归`layout`

* `onLayout()`方法

  > `protected void onLayout(boolean changed, int left, int top, int right, int bottom)`

  * 用于确定所有`子View`的位置，定义于`View`类中，不过是个空方法，需要`View`和`ViewGroup`去自己实现
  * `onLayout()`的逻辑是调用每一个`子View`的`layout`方法，不同的布局逻辑是通过不同的`l, t, r, b`参数来实现的

* `draw()`方法

  * 这个方法一般不会`override`，一般只会重写`onDraw()`
  * 四个步骤
    * Step 1, draw the background, if need -  `(drawBackground(canvas))`
    * Step 3, draw the content - (`onDraw(canvas)`)
    * Step 4, draw the children - (`dispatchDraw(canvas)`)
    * Step 6, draw decorations (foreground, scrollbars) - (`onDrawForeground(canvas)`)

* `onDraw()`方法

  * 定义在`View`里面，但是一个空方法，需要不同的`View`和`ViewGroup`来自己实现
  * 这个方法负责如何`draw`自己

* `dispatchDraw()`方法

  * 定义在`View`里面，但是一个空方法，需要不同的`View`和`ViewGroup`来自己实现
  * 一般逻辑是调用每一个`子View`的`draw()`方法

* 自定义View

  * 比较难
  * 是一个综合技术体系，涉及View的层次结构、事件分发机制和View的工作原理细节

* 自定义View分类

  * 继承`View`：需重写`onDraw()`，重写`onMeasure()`支持`wrap_content`，`padding`也要自己处理
  * 继承`ViewGroup`：自己处理`onMeasure()、onLayout()`，并还要顺带处理子元素的测量和布局
  * 继承特定的`View`：简单
  * 继承特定的`Layout`：简单

* 自定义View须知

  * 继承自View类不支持`wrap_content`
  * 继承自View类不支持`padding`
  * 继承自ViewGroup类需要考虑padding和子元素margin的影响
  * 不要在View中使用handler，因为View有post方法
  * View有线程或者动画要及时停止，一般在`onDetachFromWindow()`中处理，不然内存泄漏
  * 有嵌套处理要自己解决滑动冲突

* `padding`和`margin`

  * `margin`是父容器控制的，一般`ViewGroup`要在`onMeasure()`和`onLayout`中使`margin`生效
  * `padding`是每个`View`自己控制的，一般要在`onMeasure()`和`onDraw()`中使`padding`生效

* `View.post()`和`Handler.post()`

  * 如果view已经attach到window了，那么View#post和Handler#post作用一样，都是往调用UI主线程的MessageQueue中扔Runnable
  * 如果view还未attach到window中，则需要通过一个缓存队列将Runnable暂时先缓存起来，等到view attach到window上之后，再将缓存队列中的Runnable取出来，再扔到UI线程的MessageQueue中

* 自定义 - 直接继承`View`

  * [详情看代码]()
  * 


***

## 第五章

* RemoteView
  * 在其他进程中显示
  * 有一套api支持跨进程更新它的页面
  * 主要用于通知栏和桌面小部件
  * 其他进程一般就是指系统的SystemServer进程

***

## 第七章

* 三种动画
  * View动画
  * 帧动画
  * 属性动画

* View动画
  * 写在`res/anime`下
  * 是一个`xml`文件
  * 通过`R.anim.xmlname`引入使用

* 帧动画
  * 原理：连续的放图片，容易OOM
  * 通过xml定义一个AnimationDrawable
  * 把这个Drawable作为图像的background即可实现动画

* LayoutAnimation
  * 是一种特殊的View动画
  * 作用于`ViewGroup`，这样它的子元素出场就有动画效果
  * 常用于`RecycleView`
  * 还是通过xml文件来指定

* Activity的切换效果
  * 重写`Activity.overridePendingTransition(int enterAnim, int exitAnim)`
  * 动画id实际上也是一个xml文件

* 属性动画
  * 动态改变图像的属性，只支持API 11以上。低级的需要用兼容库
  * 只要图像有这个属性，就能够使用属性动画
  * 需要图像提供对应属性值的`get`和`set`方法

* ObjectAnimator
  * 继承自`ValueAniamtor`
  * `public static ObjectAnimator ofInt(Object target, String propertyName, int... values)`
  * 比`ValueAnimator`封装度更高一点
  * 透明度这种可以使用，但无`onEndListener`这种就比较不方便

* Interpolator
  * 根据时间流逝百分比计算当前属性值改变的百分比
  * 注意核心是计算出百分比，真正的值为多少与它无关
  * 线性插值器
  * 加速插值器
  * 减速插值器

* Evaluator
  * 根据当前属性改变的百分比来计算改变后的属性值
  * `public Integer evaluate(float fraction, Integer startValue, Integer endValue)`
  * 得到百分比估算出实际的属性值

* Button不能动画改变其width
  * `Button`继承了`TextView`
  * `Button.getWidth()`是`View.getWidth()`
  * `Button.setWidth()`是`TextView.setWidth()`
  * 因此button的宽度不能使用属性动画

* 使用动画注意事项
  * 帧动画OOM
  * 兼容性问题，低版本不支持属性动画
  * View动画是对View影像做动画，并不能改变View的状态

***

## 第八章

* Window

  * `WindowManager`和`WindowManagerService`交互是IPC过程，通过`binder`
  * Android所有视图，如`Acticity、Dialog、Toast`视图都是通过`Window`来呈现的
  * `Window`是`View`的直接管理者
  * 事件由`Window`传给`DecorView`

* Window的三种类型

  * 应用Window：对应一个Activity
  * 子Window：必须存在于父Window中
  * 系统WIndow：比如Toast和系统状态栏。声明系统Window是需要`AndroidManifest.xml`中声明权限

* Window的分层

  * window显示和图层的覆盖一样
  * 图层高低用`层级范围`表示

* WindowManager方法

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

* 理解

  * `Window`是一个抽象概念，且其自身就是一个抽象类
  * `WindowManger`是外界访问`Window`的入口
  * 真正的实现在`WMS`。`WM、WMS`通过`binder`机制IPC通信
  * 在创建视图并显示出来时，首先是通过创建一个Window对象，然后通过WindowManager对象的 `addView(View view, ViewGroup.LayoutParams params)`; 方法将 contentView 添加到Window中，完成添加和显示视图这两个过程
  * 在关闭视图时，通过WindowManager来移除DecorView， mWindowManager.removeViewImmediate( view)
  * Toast比较特殊，具有定时取消功能，所以系统采用了Handler

***

## 第九章

* 例外特点
  * 只有`BroadcastReceiver`不需要在`AndroidManifest.xml`中注册
  * 调用方式上，只有`ContentProvider`不需要借助`intent`

* Activity
  * 唯一一个对用户可感知的组件，其他三个都感知不到
  * 展示一个界面和用户交互，扮演前台角色

* Service
  * 一种计算型组件，用于在后台执行一系列任务
  * 默认运行在主线程，需要手动设置才能进入异步模式
  * 启动状态：后台异步进行计算操作，不需要和外面直接交互
  * 绑定状态：同样可以进行计算操作，但可以很方便和外面交互

* BroadCaseReciver
  * 一种消息型组件，用于在不用组件甚至不同应用之间传递消息
  * 发送接收过程通过`intent-filter`来过滤
  * 可以实现低耦合的观察者模式
  * 不适合做耗时操作

* ContentProvider
  * 内部维持着一份数据集合
  * 数据集合的实现没有要求，可以使用数据库，也可以使用集合

* 剩余内容
  * 全是源码分析
  * 不想看了

***

## 第十章

* 概述

  * `handdler`是android消息机制的上层接口，开发人员只要和它打交道就行了
  * `handler`经常拿来做异步处理耗时任务，UI线程处理UI。但并不代表handler只用来更新UI
  * `UI线程` = `主线程` = `ActivityThread`

* 限制UI线程更新UI

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

* ThreadLocal

  * 是一个线程内部的数据存储类
  * 当某些数据是以线程为作用域且不同线程需要不同的副本时，就可以采取`ThreadLocal`
  * 虽然在不同线程中访问的是同一个`ThreadLocal`对象，但是获取到的值确实不一样的，这就做到了线程隔离

* 主函数

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

* 三种另外形式
  * `AsynsTask`：封装了线程池和`Handler`
  * `HandlerThread`：内部可以使用`Handler`
  * `IntentService`：
  * 上面三者虽然表现形式不同，但本质上依然是传统的线程

* 调度方式
  * 线程数量不能无限多
  * 除非线程数量小于CPU核心数，否则采用时间片轮转法进行调度，无法做到真正的并行

* AsyncTask
  * 使用方式：继承实现几个特定方法
  * 参数：`AsyncTask<Params, Progress, Result>`，不需要使用可以设置为`void`
  * 核心：`Handler`、`ThreadPoolExcutor`
  * `SERIAL_EXECUTOR`：把任务排队
  * `THREAD_POOL_EXECUTOR`：真正执行并行任务
  * `InternalHandler`：用于异步线程向主线程抛事件，即线程切换

* HandlerThread
  * 继承自`Thread`
  * 里面有一个`Handler`成员变量
  * 且已经`Looper`做了初始化

* IntentService
  * `Service`不会没用过，后面再看

* Java线程池
  * `Excutor`是一个接口，真正的线程池实现在`ThreadPoolExecutor`
  * `ThreadPoolExecutor`通过一系列参数来配置线程池

***

