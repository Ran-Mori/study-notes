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
>
> ### 文件结构
>
> * Activity主代码一般是继承自`AppCompatActivity`类，这个类会随着设备的不同自动调整，省去了一些适配的烦恼
> * 一个Activity一般对应者一个布局文件，这个文件一般是`xml`格式。
> * 主代码和布局文件绑定：通过主代码中的`onCreate(savedInstanceState: Bundle?)`中的`setContentView(R.layout.activity_main)`来实现绑定
> * `AndroidManifest.xml`注册文件，所有的activity声明后都要咋这个文件中进行注册，不然无法使用。此注册文件还可以设置activity的诸多属性
>
> ### 增查ID
>
> * 增加ID `android:id="@id+/selfDefineId"`
> * 查找ID `findViewById(R.id.selfDefineId)`
>
> ### AndroidMainifest.xml中的activity属性
>
> * `<action android:name = "android.intent.action.MAIN" />`决定谁是主启动项
> * `<category android:name = "" />` 配合`Intent`实现隐式页面跳转
> * `<activity android:theme = "" />` 决定次activity的主题，如对话框
>
> ### 不使用`findViewById()`
>
> * 频繁使用`findViewById()`这种繁琐代码是kotlin语言不推荐的
> * 在`app/build.gradle`中加入`plugins{ id 'kotlin-android-extensions'}`即可直接在代码中使用ID代表一个组件对象
>
> ### menu
>
> * 就是Android界面上的三个点
> * 逻辑结构
>   * `memu_main.xml` ：一个在res文件夹中的menu类型的xml文件，里面写清了有几个`item`
>   * `onCreateOptionMenu(menu: Menu?) Boolean`方法：返回true代表显示menu，返回false代表不显示。在此方法中设置显示那一个menu
>   * `onOptionsItemSelected(item: MenuItem) Boolean`方法：根据选择的`item`的ID来进行逻辑代码编写，一般使用`when()`语句
>
> ### 销毁activity
>
> * 点击back键或者执行`finsh()`方法
>
> ### Intent
>
> * 用于不同activity之间的切换和数据传递。且分为显示与隐式
>
> * 使用举例
>
>   ```kotlin
>   //显示
>   val intent = Intent(this, SecondActivity::class)
>   startActivity(intent)
>
>   //隐式
>   val intent = Intent("whu.activityTest.ACTION_START")
>   intent.addCategory("whu.activityTest.MY_CATEGORY")
>   startActivity(intent)
>
>   //调用系统浏览器
>   val intent = Intent(Intent.ACTION_VIEW) //一个常量View，代表浏览器
>   intent.data = Uri.parse("https://www.baidu.com") //传入需要解析的data内容
>   startActivity(intent)
>
>   //调用系统拨号界面
>   val intent = Intent(Intent.ACTION_DIAL)
>   intent.data = Uri.parse("tel:10086")
>   startActivity(intent)
>   ```
>
> * 传递数据
>
>   ```kotlin
>   //发送
>   val intent = Intent(this, SecondActivity::class)
>   intent.putExtra("name","IzumiSakai")
>   startActivity(intent)
>   
>   //接收，才onCreate(b:Bundle?) 或者onStart()中
>   val name = intent.getStringExtra("name")
>   ```
>
> ### 返回数据给上一个Activity
>
> * 调用finish()函数进行页面跳转实例
>
>   ```kotlin
>   //接收数据的一方, 进行页面跳转时
>   //一个intent代表想跳转的activity，一个int表示唯一请求码，在后面会用
>   startActivityForResult(intent: Intent, statusCode: Int)
>
>   //发送数据的一方
>   val intent = Intent() //由于不进行跳转，只是一个容纳数据的容器，因此不用进行跳转页面设置
>   intent.putStringExtra("name","IzumiSakai")
>   setResult(RESULT_OK,intent) //RESULT_OK表示返回的状态码，后面要用，可以不唯一
>   finish()
>
>   //接收数据的一方，获取传递的数据时
>   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
>       super.onActivityResult(requestCode, resultCode, data)
>       when(requestCode){
>           1 -> { //会有很多个请求吗，因此要进行区分
>               if (resultCode == Activity.RESULT_OK){//当返回码为成功时，进行实际代码逻辑
>                   //代码逻辑
>               }
>           }
>       }
>   }
>   ```
>
> * 直接点击back返回键进行跳转的实例
>
>   ```kotlin
>   override fun onBackPressed() {
>       super.onBackPressed()
>       val myIntent = Intent()
>       myIntent.putExtra("name","IzumiSakai")
>       setResult(RESULT_OK,myIntent)
>       finish()
>   }
>   ```
>
> ### 生命周期
>
> * 各个生命周期执行的函数
>
>   ```kotlin
>   //创建时，含有一个Bundle参数是为了当这个activity处于stop状态被回收后中间暂存的数据丢失
>   //因此当是因为内存不足被清理掉在重新创建时onCreate的此参数就不会为空
>   override fun onCreate(savedInstanceState: Bundle?) {
>       super.onCreate(savedInstanceState)
>       setContentView(R.layout.activity_main)
>       Log.d("MainActivity","onCreateCalled")
>   }
>
>   //由不可见变成可见
>   override fun onStart() {
>       super.onStart()
>       Log.d("MainActivity","onStartCalled")
>   }
>
>   //由不可交互变成可以交互
>   override fun onResume() {
>       super.onResume()
>       Log.d("MainActivity","onResumeCalled")
>   }
>
>   //由可交互变成不可交互
>   override fun onPause() {
>       super.onPause()
>       Log.d("MainActivity","onPauseCalled")
>   }
>
>   //由可见变成不可见
>   override fun onStop() {
>       super.onStop()
>       Log.d("MainActivity","onStopCalled")
>   }
>
>   override fun onDestroy() {
>       super.onDestroy()
>       Log.d("MainActivity","onDestroyCalled")
>   }
>
>   //当未被销毁非处于栈顶又由于切换处于栈顶时会调用此方法
>   override fun onRestart() {
>       super.onRestart()
>       Log.d("MainActivity","onRestartCalled")
>   }
>   ```
>
> * 注意事项：pause和stop的主要区别之一就是当activity是对话框形式时只会执行pause而不会执行stop，因为对话框即使暂时退出了也是可见的
>
> * 调度形式：栈。activity不停地压栈和出栈就形成了activity的调度
>
> ### activity被清理后恢复数据
>
> * 代码示例
>
>   ```kotlin
>   //发送数据
>   //此方法会保证在activity被回收之前一定调用
>   override fun onSaveInstanceState(outState: Bundle) {
>       super.onSaveInstanceState(outState)
>       outState.putString("name","Izumi Sakai")
>   }
>   
>   //接收数据
>   override fun onCreate(savedInstanceState: Bundle?) {
>       super.onCreate(savedInstanceState)
>       val name = savedInstanceState?.getString("name")
>   }
>   ```
>
> ### 启动模式
>
> * standard：每次当栈顶是它时就无脑重新创建
> * singleTop：当它在栈顶时就不创建，否则就要创建
> * singleTask：如果栈中存在它，就把上面的全部出栈，知道它在栈顶
> * singleInstance：最极端，相当于单例模式。此种情况下此activity在整个Android系统中同时永远只会存在一个实例，且由于其single性，它还单独享有一个调用栈，不能和task共栈，共栈的话无法实现如此极端的单例性
>
> ***

