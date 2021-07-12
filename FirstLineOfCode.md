

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
>   val intent = Intent(this, SecondActivity::class.java)
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
> ### 隐式Intent
>
> * `action`：`xml`文件中一个`activity`可以指定多个`action`，但隐式intent中只能设置一个`action`，即在构造函数中设置如`val intent = Intent("MyAction")`
> * `category`：`xml`文件中一个`activity`可以使用多个`category`，隐式Intent也可以设置多个category。当且仅当`xml`中的category大于等于隐式Intent中的category才能匹配成功
> * `data`：`xml`文件中可以用多个`data`标签来设置URL的属性诸如`schema,host,path`，一旦`xml`中设置了`data`属性。则当且仅当隐式Intent的data值和`xml`中的匹配才行。如`intent.data = Uri.parse("https://47.108.63.126/songs")`
> * 匹配规则是先匹配`action`，保证`data`是匹配的，再匹配`category`
> * 隐式Intent必须有`android.intent.category.DEFAULT`的`categroy`，否则只能进行显示匹配
> * 其中`action`和`category`的名字可以随便自己乱取，不过最好以`包.类名`来命名知道实际指那个实体Activity
>
> ### 协调合作确定好某一个Activity需要什么数据
>
> ```kotlin
> class SecondActivity : BaseActivity() {
>     companion object {
>         fun actionStart(context: Context, data1: String, data2: String) {
>             val intent = Intent(context, SecondActivity::class.java).apply {
>                 putExtra("param1", "data1")
>                 putExtra("param2", "data2")
>             }
>             context.startActivity(intent)
>         }
>     }
> }
> ```
>
> * 写一个伴生对象
> * 这样无论是谁跳转到`SecondActivity`，都知道了这个Activity需要两个数据，这样就很好地做到了数据传递的规范性
> * 在`FirstActivity`中实现跳转`SecondActivity.actionStart(this, "data1", "data2")`
>
> ### 退出并返回数据
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
> * 此种结束返回数据的方式要注意结束的方式一共有两种，一种是调用`finish`方法，一种是返回键。因此在这两个地方都要写上返回回传数据的逻辑
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
> ### Android:Theme
>
> * 在`AndroidManifest.xml`中可以设置此属性，可以设置`Activity`的主题样式
> * 如`android:theme="@style/Theme.AppCompat.Dialog`
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
>       val name = savedInstanceState?.getString("name")?:""
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

## 第三章 - UI

> ### dp sp px
>
> * dp 和 sp是一个概念，只不过dp一般用于表示长宽大小，而sp一般用于表示字体大小
> * dp 和 sp是一个于密度无关的单位，不会因为ppi很高而显得非常小，也不会因为ppi很小而显得非常大
> * 转换公式为`px = dp * ppi / 160`，即160的ppi下`1 dp = 1 px`，随着ppi的增长，视觉上同样dp的组件大小不变
>
> ### 居中
>
> * 内容居中：`android:gravity = ""`
> * 布局居中：`android:layout_gravity = ""`
> * 指定多个值；`android:gravity="left|center_vertical"`
>
> ### Button
>
> * Android Studio默认会把button上的字母都改成大写，只需要加上`android:textAllCaps="false"`就行
>
> ### ImageView
>
> * 现在主流手机的分辨率都是`xxhdpi`，因此图片文件放在`drawable-xxhdpi`文件夹下面
> * 引用src：`android:src = "@drawable/img_1"`
> * 注意：Android Studio不以文件拓展名区分图片，即不允许有重名图片，即使拓展名不同
>
> ### 可见性
>
> * 可见：`android:visibility="visible"`
> * 不可见但是占据空间：`android:visibility="invisible"`
> * 完全隐藏：`android:visibility="gone"`
>
> ### ProgressBar
>
> * 默认是一个圆圈在那里转，修改style可以变成水平加载的进度条
> * `style="?android:attr/progressBarStyleHorizontal"`
>
> ### 警告框AlertDialog
>
> ```kotlin
> val alertDialog = AlertDialog.Builder(this).apply {
>     setCancelable(false)
>     setPositiveButton("Ok"){
>         alertDialog, which -> Toast.makeText(this@MainActivity,"you click ok",Toast.LENGTH_SHORT).show()
>     }
>     setNegativeButton("refuse"){
>         alertDialog, which -> Toast.makeText(this@MainActivity,"you click refuse",Toast.LENGTH_SHORT).show()
>     }
>     setTitle("title")
>     setMessage("message")
>     create()
> }.show()
> ```
>
> * 最后一定不要忘了加上`show()`，经常忘加
> * `setPositiveButton()`函数接收两个参数，一个是`AlertDialog`警告对象，一个是(int -> unit)的高阶函数
>
> ### LinearLayout
>
> * 有垂直和水平两种可以选择
> * 水平的layout不能把`layout_height`设置成`match_parent`；同理可得垂直方向的`layout`
> * 水平的layout的`layout_gravity`只有垂直方向上的设置才会生效，水平方向上的设置会被忽略
>
> ### 比例布局
>
> * 核心参数`layout_weight`
> * 且通常把想要指定的宽度或高度方向设置为`0dp`来表示使用weight
> * 一个`warp_content`，一个`layout_weigth = "1"`可以实现非常美观的布局
>
> ### RelativeLayout
>
> * 相对于父布局：`layout_alignParentBottom`等
> * 相对于某组件：`layout_above="@id/button"`，通过`@id`来指定相对的组件
>
> ### 自定义布局复用
>
> * 首先创建一个布局的`layout.xml`文件，自己设计样式
>
>   ```xml
>   <?xml version="1.0" encoding="utf-8"?><LinearLayout    xmlns:android="http://schemas.android.com/apk/res/android"    android:layout_width="match_parent"    android:layout_height="wrap_content"    android:orientation="horizontal">   	//...</LinearLayout>
>   ```
>
> * 创建一个kotlin类即代表这个布局的实体类
>
>   ```kotlin
>   class TitleLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {    init {        LayoutInflater.from(context).inflate(R.layout.title,this)        backButton.setOnClickListener {            val activity = context as Activity            activity.finish()        }        toastButton.setOnClickListener {            Toast.makeText(context,"toast",Toast.LENGTH_SHORT).show()        }    }}
>   ```
>
> * 在另外的`xml`布局文件中引入这个布局
>
>   ```xml
>   <whu.uilearning.TitleLayout        android:id="@+id/customer"        android:layout_width="wrap_content"        android:layout_height="wrap_content" />
>   ```
>
> * 注：虽然`LinearLayout`有众多的构造函数，但只传入一个`context`参数会构造失败，还是要传`(context: Context, attrs: AttributeSet)`
>
> ### 隐藏最上面的的`ActionBar`
>
> * `supportActionBar?.hide()`
>
> ### 自定义样式无逻辑布局
>
> * 直接include就行`<include layout = "@layout/slef_define_layout"`
>
> ### 自定义带逻辑布局
>
> * 首先要创建一个类继承系统中的如`LinearLayout`
> * 然后再布局xml文件中使用全限定类名引入`<whu.layout.SelfDefineLayout />`
>
> ***

## RecycleView

> ### 引入依赖
>
> * `implementation 'androidx.recyclerview:recyclerview:1.1.0'`
>
> ### SongAdapter
>
> ```kotlin
> class SongAdapter(val songList: List<Song>): RecyclerView.Adapter<SongAdapter.ViewHolder>() {    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){        val songIdTextView: TextView = view.findViewById(R.id.songId)        val songNameTextView: TextView = view.findViewById(R.id.songName)        val songSingerTextView: TextView = view.findViewById(R.id.songSinger)    }    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.song_item,parent,false)        val viewHolder = ViewHolder(view)        viewHolder.songNameTextView.setOnClickListener {            Toast.makeText(parent.context,"songname = ${viewHolder.songNameTextView.text}",Toast.LENGTH_SHORT).show()        }        return viewHolder    }    override fun onBindViewHolder(holder: ViewHolder, position: Int) {        val song: Song = songList[position]        holder.songIdTextView.text = song.id.toString()        holder.songNameTextView.text = song.songName        holder.songSingerTextView.text = song.songSinger    }    override fun getItemCount() = songList.size}
> ```
>
> ### Main
>
> ```kotlin
> recyclerView.layoutManager = LinearLayoutManager(this)recyclerView.adapter = SongAdapter(songList)
> ```
>
> ### 垂直其他布局
>
> * RecycleView实现其他的布局主要是通过`LayoutManager`来进行实现，如果要实现垂直的瀑布流布局。可以对`Manager`进行响应的一些设置
> * 即`StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)`即是实现3列垂直瀑布布局
>
> ### 当有多种View，但还是使用ViewHodler情况
>
> * 思路：定义一个基类`ViewHodler`，不同的View就继承实现不同的`ViewHolder`
>
> * 此处还可以使用密封类的概念，在进行判断时就不用写`else`，且会做是否覆盖覆盖所有case的检查
>
> * `ViewHolder定义`
>
>   ```kotlin
>   sealed class MsgViewHolder(view: View) : RecyclerView.ViewHolder(view)class LeftViewHolder(view: View) : MsgViewHolder(view) {    val leftMsg: TextView = view.findViewById(R.id.leftMsg)}class RightViewHolder(view: View) : MsgViewHolder(view) {    val rightMsg: TextView = view.findViewById(R.id.rightMsg)}
>   ```
>
> * `onCreateViewHolder使用`
>
>   ```kotlin
>   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Msg.TYPE_RECEIVED) {        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_left_item, parent, false)        LeftViewHolder(view)    } else {        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_right_item, parent, false)        RightViewHolder(view)    }
>   ```
>
> ***

## 第四章 - Fragment

> ### 特点
>
> * 有自己的生命周期
> * 必须依附于`Activity`而存在，自己不能单独存在
>
> ### 创建一个Fragment
>
> * 首先先自定义一个`xml`的布局
> * 最简单的一行代码就可以构建一个Fragment如`class ExampleFragment : Fragment()`
>
> ### 将Fragment添加到Activity中显示
>
> * Android官方强烈推荐使用`FragmentContainerView`进行Fragment的添加和显示
>
> * 方法一：静态在`xml`中指明全限定类名，这样一加载时就可以添加
>
>   ```xml
>   <androidx.fragment.app.FragmentContainerView    xmlns:android="http://schemas.android.com/apk/res/android"    android:id="@+id/fragment_container_view"    android:layout_width="match_parent"    android:layout_height="match_parent"    android:name="com.example.ExampleFragment" />
>   ```
>
>   * 其中`name`属性就是全限定类名
>
> * 方法二：动态在代码中进行添加
>
>   ```kotlin
>   supportFragmentManager.commit {    setReorderingAllowed(true)//开启事务优化    add<LeftFragment>(R.id.fragment_container_view_tag,"left",bundle)}
>   ```
>
>   * 其中`commit`和`add`方法都是kotlin的拓展函数，方便了程序员进行api的调用
>
> * 动态加载的一般写法
>
>   ```kotlin
>   Log.d("MainActivity","开始事物")val transaction = supportFragmentManager.beginTransaction()transaction.replace(R.id.linearLayout,RightFragment())transaction.commit()Log.d("MainActivity","事物结束")
>   ```
>
>   * 动态加载`Fragment`的基本操作是事物
>   * `R.id.linearLayout`是`MainActivity`中的一个容器，这个容器随便是啥
>   * 但`RightFragment()`必须是一个`Fragment`
>   * 增加和替换的操作一般使用`relace()`函数完成
>
> ### 在Fragment创建时从Activity传入数据
>
> * Activity发送数据
>
>   ```kotlin
>   supportFragmentManager.commit {    val bundle = Bundle()    bundle.putString("key","value")    replace<LeftFragment>(R.id.fragment_container_view_tag,"tag",bundle)}
>   ```
>
>   * 实际上就是自定义一个Bundle然后作为参数传过去
>
> * Fragment接收数据
>
>   ```kotlin
>   class LeftFragment: Fragment(R.layout.msg_left){    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState)        Log.d("MainActivity",requireArguments().getString("key").toString())    }}
>   ```
>
>   * 在`onViewCreated()`方法中调用`requireArguments()`获取之前传过来的`Buddle`
>
> ### FragmentManager
>
> * 只要涉及到`Fragment`必然涉及到`FragmentManager`
> * 只是看是实际使用它还是使用高层的api屏蔽掉它，比如使用`jetpack navigation`就几乎感觉不到使用`FragmentManager`，但底层实际上还是在使用
>
> ### 获取FragmentManager
>
> * `Activity`中：`getSurrportFragmentManager()`
> * `Fragment`中：
>   * 获取管理此`Fragment`的manager：`getParentFragmentManager()`
>   * 获取此`Fragment`管理的子Fragment的manager：`getChildFragmentManager()`
> * 注意：按照Fragment层级的嵌套关系，每个层级都有管理它下一个层级的`FragmentManager`，即最底层的Fragment没有`FragmentManager`，它只能被管理
>
> ### 栈式存储
>
> * 使用`FragmentManager`开始事务后的整个操作被算作是一次原子操作。
> * 如果最后提交前调用了`addToBackStack("name")`，其中name可以为空
> * 就可以点击`back`键或者调用`popBackStack()`方法就可以进行回退操作
> * 不添加进栈就直接`onDestroy()`，添加了就是`onStop()`
>
> ### 找到一个Fragment
>
> * 通过`android:id`找：`supportFragmentManager.findFragmentById(R.id.fragment_container) as ExampleFragment`
> * 通过`android:tag`或者`replace<ExampleFragment>(R.id.fragment_container, "tag")`设置的tag找：`supportFragmentManager.findFragmentByTag("tag") as ExampleFragment`
>
> ### 同级多个只有一个能当主导航栈
>
> * `Note that when two or more fragments are displayed at the same time, only one of them can be the primary navigation fragment`
>
> ### 自定义构造函数
>
> * 首先定义自己的Fragment
>
>   ```kotlin
>   class SongsFragment(val songs:List<String>):Fragment() {}
>   ```
>
> * 然后创建一个`FragmentFactory`的子类来重写方法
>
>   ```kotlin
>   class SongsFactory(val songs:List<String>):FragmentFactory() {    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {        when(loadFragmentClass(classLoader, className)){            SongsFragment::class.java -> return SongsFragment(songs)            else -> return super.instantiate(classLoader, className)        }    }}
>   ```
>
>   * 其实`when`里面的逻辑还可以加，这样就完全成了自己自定义的构造工厂了
>
> #### 生命周期
>
> * 注意：`Fragment`是一个对象，它依赖于一个`Activity`或者父`Fragment`，它最后呈现在页面上是一个view。这三者都有它们各自的生命周期
> * `onAttach`：在Fragment和FragmentManger绑定时触发
> * `onCreate`
> * `onCreateView`
> * `onViewCreated`
> * `onStart`
> * `onResume`
> * `onPaused`
> * `onStop`
> * `onDestroyView`
> * `onDestroy`
> * `onDetach`

## 第六章 - 广播

> ### IP广播
>
> * 一句网段中最大的IP地址为广播地址
> * 如192.168.0.0网段，子网掩码为255.255.255.0，则广播地址为192.168.0.255
>
> ### 广播分类
>
> * 标准广播
>   * 全异步广播
>   * 同一时刻所有注册的接收器都会收到
>   * 不可阻断
> * 有序广播
>   * 同步广播
>   * 接收顺序有先后，只有等前面接收到了处理完逻辑以后后面才能接收
>   * 也就是说前面可以把后面给阻断
>
> ### 接收广播举例
>
> ```kotlin
> class MainActivity : AppCompatActivity() {        private lateinit var broadcastReceiver:TimeChangeBroadCastReceiver        private var count: Int = 0        inner class TimeChangeBroadCastReceiver: BroadcastReceiver(){                override fun onReceive(context: Context?, intent: Intent?) {                        Log.d("MainActivityChange","${count++}")                }        }        override fun onCreate(savedInstanceState: Bundle?) {                super.onCreate(savedInstanceState)                setContentView(R.layout.activity_main)                val intentFilter = IntentFilter()                intentFilter.addAction("android.intent.action.TIME_TICK")                broadcastReceiver = TimeChangeBroadCastReceiver()                registerReceiver(broadcastReceiver,intentFilter)        }        override fun onDestroy() {                super.onDestroy()                unregisterReceiver(broadcastReceiver)        }}
> ```
>
> * 程序每隔一分钟会接收到一条系统时间变化的广播，然后可以进行相应的逻辑处理
> * 注册后一定要在`onDestroy()`方法中取消注册才行，不然会重复注册
>
> ### 两种注册方式
>
> * 静态注册：在`AndroidManifest.xml`中进行注册
> * 动态注册：在activity的代码中使用`Intent registerReceiver(BroadcastReceiver receiver,IntentFilter filter)`进行注册
>
> ### 静态注册特点
>
> * 之前的注册都是动态注册。动态注册要求接收广播的activity一定要处于运行的状态，这对于那些未启动但依然想要接收广播的activity来说是不友好的
> * 静态注册允许某个activity未启动依然可以接收广播
> * 但这产生了一个新的问题，即很多应用滥用静态注册这个功能，当其处于未启动状态时频繁监听全局的广播信息，造成电量消耗过快，运行内存紧张，系统卡顿的问题
> * 因此Android一直都在限制静态注册的使用，但依然还是可以使用的
>
> ### 静态注册示例
>
> * 创建一个接收器
>
>   ```kotlin
>   class MyReceiver: BroadcastReceiver() {      override fun onReceive(context: Context?, intent: Intent?) {            Toast.makeText(context,"静态注册消息",Toast.LENGTH_SHORT).show()      }}
>   ```
>
> * 静态注册都是在`AndroidManifest.xml`中进行注册的
>
>   ```xml
>   <receiver android:name=".MyReceiver"                      android:enabled="true"                      android:exported="true" >      <intent-filter>            <action android:name="android.intent.action.BOOT_COMPLETED" ></action>      </intent-filter></receiver>
>   ```
>
>   * `android:exported`表示是否允许它接收本程序以外的广播。此处肯定要设置为true，因此系统开机广播不是本程序的广播
>   * 在`intent-filter`中声明接收那种广播
>
> * 使用权限
>
>   ```xml
>   <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
>   ```
>
>   * android做了很多权限管理，某些通知想要使用必须显示地声明拥有这些权限才能使用
>
> ### 发送自定义广播
>
> * 创建一个接收器
>
>   ```kotlin
>   class MyReceiver: BroadcastReceiver() {        override fun onReceive(context: Context?, intent: Intent?) {                Toast.makeText(context,"静态注册消息",Toast.LENGTH_SHORT).show()        }}
>   ```
>
> * 在`AndroidManifest.xml`中进行注册
>
>   ```xml
>   <receiver android:name=".MyReceiver"                      android:enabled="true"                      android:exported="true" >        <intent-filter>                <action android:name="android.intent.action.MY_BROADCAST" ></action>        </intent-filter></receiver>
>   ```
>
> * 发送广播
>
>   ```kotlin
>   val intent = Intent("com.whu.MY_BROADCAST")intent.setPackage(packageName)sendBroadcast(intent)
>   ```
>
>   * 发送过程实际上就是发送一个`Intent`
>
> * 隐式广播和显示广播
>
>   * 几乎所有的系统广播都是隐式广播，隐式广播有一个特点就是所有的接收器都能接收到，只是看它是否进行响应
>   * 显示广播指的是限定了只有那个部分的接收器才能收到
>   * 基于安卓权限混乱，频繁唤醒的现况。静态注册的接收器是无法接收到隐式广播的，如果要接收到就要进行权限声明(和开机权限声明类似)
>   * 因此此处的`setPackage`方法必须进行调用，因为调用次方法相当于是把隐式广播转换成了显示广播
>
> ### 发送有序广播
>
> * 发送处
>
>   ```kotlin
>   val intent = Intent("com.whu.MY_BROADCAST")intent.setPackage(packageName)sendOrderedBroadcast(intent,null)
>   ```
>
>   * 仅仅变了一处即`sendOrderedBroadcast(intent,null)`
>
> * 优先级声明
>
>   ```xml
>   <receiver android:name=".MyReceiver"                    android:enabled="true"                    android:exported="true">      <intent-filter android:priority="100">            <action android:name="android.intent.action.BOOT_COMPLETED" ></action>    </intent-filter></receiver>
>   ```
>
>   * 仅仅加了一个`android:priority="100"`
>
> * 截断广播
>
>   ```kotlin
>   abortBroadcast()
>   ```
>
>   * 直接在`MyBroadcastRecevier`中调用此方法
>
> ### 强制下线逻辑
>
> * 总逻辑
>
>   * 发送一个广播通知，让栈顶的activity接收到通知
>   * 接收到通知的逻辑是弹出一个警告框，此警告框不能返回，不能干其他，只能点确定
>   * 确定的逻辑是结束所有的activity并最后压入一个LoginActivity
>
> * 实现原理
>
>   * `结束所有`：创一个含有List的单例类，并创一个基类，`onCreate()`和`onDestroy()`时执行增删
>   * `让栈顶接到通知`：使用动态注册，每次`resume`注册接收器，每次`pause`就取消注册接收器。这样就能保证只有栈顶的activity接收到通知。注意`create`时没有必要进行注册
>   * `不能所有的类都写`：那就把逻辑都写在自己创建的`BaseActivity`中
>
> * 单例类
>
>   ```kotlin
>   object ActivityUtil {        private val list = ArrayList<Activity>()        fun add(activity: Activity){                list.add(activity)        }        fun remove(activity: Activity){                list.remove(activity)        }        fun finishAll(){                for (activity in list){                        if (!activity.isFinishing)                            activity.finish()                }                list.clear()        }}
>   ```
>
>   * 因为finish一个activity有可能需要时间比较久，因此要做一个判断，不然多线程时感觉会炸
>   * finish后按照逻辑栈是没有activity了，因此要把list给清空
>
> * 登录成功要调用一遍`finish()`。因为总不可能让用户登录成功后按返回又是登录吧
>
> * 禁用返回取消。`setCancelabe(false)`，这样用户就迫不得已只能点确定
>
> ***

## 第七章 - 持久化

> ### 持久化为本地文件
>
> * 一般适合持久化二进制文件，不需要自定义存储结构
> * 操作过程就是使用Java的输入输出流
>
> ### SharedPreferences简介
>
> * 使用键值对来进行存储
> * 支持多种不同的数据格式存储
>
> ### 获取对象
>
> * `Context`类的`getSharedPreferences(val name:String,val mode:Int)`方法。
> * `Activity`类的`getPreferences(val mode:Int)`方法
> * 两者的区别就是`activity`的方法的`name`参数是固定的，固定为activity的类名，即每一个activity都有一个自己的preference
> * `mode`参数就默认是0，其他的模式已经被Android取消了
>
> ### 对象的重要方法
>
> * `edit()`：获取一个editor对象
> * `apply()`：提交修改
>
> ### 使用举例
>
> ```kotlin
> override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_main)        val sharedPreference = getSharedPreferences("song", Context.MODE_PRIVATE)        val value = sharedPreference.getString("key", "默认值")        editText.setText(value)}override fun onDestroy() {        super.onDestroy()        val editor = getSharedPreferences("song", Context.MODE_PRIVATE).edit()        editor.putString("key","${editText.text.toString()}")        editor.apply()}
> ```
>
> ### ROOM
>
> * jetpack下面的一个组件
> * 一个成熟的ORM框架
>
> ### 导入依赖
>
> ```json
> plugins {        id 'kotlin-android-extensions'        id 'kotlin-kapt'}dependencies {        implementation 'androidx.room:room-runtime:2.3.0'        kapt 'androidx.room:room-compiler:2.3.0'}
> ```
>
> * kapt是kotlin的一个注解处理器
> * 注解处理器就是类似于javac的过程中对注解进行解析的处理器
>
> ### entity
>
> ```kotlin
> @Entitydata class Song(var songName:String,var songSinger:String) {        @PrimaryKey(autoGenerate = true)        var id: Long = 0}
> ```
>
> ### dao
>
> ```kotlin
> @Daointerface SongDao {        @Insert        fun insert(song: Song):Long        @Query("delete from song where id = :id")        fun deleteById(id: Long):Int        @Update        fun update(song:Song):Int        @Query("select * from song")        fun queryAll(): List<Song>        @Query("select * from song where id = :id")        fun queryOneById(id: Long):List<Song>}
> ```
>
> * sqlite规定了ID必须是`long`类型，因此无论是entity还是dao的ID类型都要设置成long
> * 而update和delete返回的修改行数，sqlite规定了只能是Int类型，因此dao的返回值只能是Int
> * 具体定义在`SupportSQLiteDatabase`
> * 这是一个接口，在编译时Room会自动生成一个它的实现类
>
> ```kotlin
> @Database(version = 1,entities = [Song::class])abstract class NeteaseDatabase: RoomDatabase() {        abstract fun songDao():SongDao        companion object{                private var instance: NeteaseDatabase? =null                @Synchronized                fun getDatabase(context:Context):NeteaseDatabase{                        instance?.let {                                return it                        }                        return Room.databaseBuilder(                                context.applicationContext,                                NeteaseDatabase::class.java,                                "netease_database").                        build().apply {                               instance = this                        }                }        }}
> ```
>
> * 注解的entitys选项是一个数组，可以填很多类
> * 一定要有抽象方法获取`songDao`
> * 使用单例模式获取数据库对象
>
> ### 使用
>
> ```kotlin
> val songDao = NeteaseDatabase.getDatabase(this).songDao()var foreverYou = Song("Izumi Sakai","Forever You")insert.setOnClickListener {        thread {                foreverYou.id = songDao.insert(foreverYou)        }}
> ```
>
> * 主要是要先获取`SongDao`
> * 然后数据库的操作一定要在另外一个线程，Android规定了必须在另外一个线程，不然数据库卡太久影响体验
>
> ### 数据库升级
>
> * sqlite数据库是可以进行升级的
>
> * 首先要明确一点就是数据库不是存放在云端的，数据库是每个用户所私有的，存放在每个用户的本地
>
> * 最简单粗暴的方式升级数据库就是把用户本地的数据库删除了，然后重新创建。这样最严重也是最致命的问题就是用户的数据全部丢失，就像重新安装一样，是绝对不允许的
>
> * 因此现在都是通过定义当前数据库版本号，每次版本迁移都设定好特定的函数，这样每个之前版本的数据库就都能根据自己所处的数据版本执行相应的升级方法升级到最新版本的数据库
>
> * 示例
>
>   ```kotlin
>   @Database(version = 2,entities = [Song::class])abstract class NeteaseDatabase: RoomDatabase() {        companion object{                @Synchronized                fun getDatabase(context:Context):NeteaseDatabase{                        val version1_to_2 = object : Migration(1,2){                                override fun migrate(database: SupportSQLiteDatabase) {                                       database.execSQL("")                                }                        }                }        }}
>   ```
>
> * 首先是头部注解的`version`字段给设置为2
>
> * 其此是使用匿名类实现了从version1 - 2的升级逻辑
>
> ***

## 第八章 - ContentProvider

> ### 申请权限
>
> * 要申请运行时权限就要在`AndoirdManifest.xml`中进行权限声明
> * 但为何只要进行了一个声明就能保证权限的安全呢？
>
> ### 声明的作用
>
> * 声明了权限过后，在用户安装时和安装后进入权限管理页面就能看到这个程序申请了那些权限。
> * 如果有用户不想给的权限，那么用户就可以拒绝安装
> * 但这种想法只是表面上很美好，但很多巨型APP，比如微信QQ这类，你明知道它申请了过多的权限，但你却必须安装它，因此很多时候用户都是被逼无奈只能点击允许
>
> ### 运行时权限
>
> * 为了解决这个问题，提出了一种运行时权限的概念
> * 即在运行过程中当程序需要申请某权限时，需要得到用户的允许才可以，否则就不行
>
> ### 权限分类
>
> * 普通权限：指对用户没有什么影响和危害或者隐私性很弱的权限。这类权限的特点是系统会自动进行授权，不用用户手动操作
> * 危险权限：指对用户隐私性和安全性影响极大的权限。这类权限的特点是必须进行用户手动授权
>   * 危险权限一共有11组共30个。其他的就都是普通权限
>   * 一般同意了某一组的某一个权限后，此组的其他权限会默认进行授权
>
> ### 处理逻辑改变
>
> * 普通权限申请就在`AndroidManifest.xml`中进行申请，这部分一般都是允许的，因为这些普通权限无伤大雅
> * 运行时权限就需要在运行时动态处理，分为允许和拒绝的情况。注意：运行时权限也要在`AndroidManifest.xml`中声明使用权限，不声明直接是永远拒绝
>
> ### 代码逻辑
>
> ```kotlin
> override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_main)        makeCall.setOnClickListener {                if (ContextCompat.checkSelfPermission(                        this,                         Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){                        ActivityCompat.requestPermissions(                                this,                                 arrayOf(Manifest.permission.CALL_PHONE),                                1)                }else{                        call()                }        }}override fun onRequestPermissionsResult(        requestCode: Int,        permissions: Array<out String>,        grantResults: IntArray) {        super.onRequestPermissionsResult(requestCode, permissions, grantResults)        when(requestCode){                1 ->{                        if (grantResults.isNotEmpty() &&                                grantResults[0] == PackageManager.PERMISSION_GRANTED){                                call()                        }else{                                Toast.makeText(this,"apply for permission denied",Toast.LENGTH_SHORT).show()               }                }        }}private fun call(){        try {                val intent = Intent(Intent.ACTION_CALL)                intent.data = Uri.parse("tel:10086")                startActivity(intent)        }catch (error: Exception){                Log.e("MainActivity","${error.printStackTrace()}")        }}
> ```
>
> ### ContentProvider两种用法
>
> * 一是使用ContentProvider读取和操作其他程序的数据
> * 一是自定义此程序的ContentProvider，供其他用户进行访问
>
> ### 读其他程序数据示例
>
> ```kotlin
> class  MainActivity : AppCompatActivity() {    var bookId: String? = null    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_main)        addData.setOnClickListener {            // 添加数据            val uri = Uri.parse("content://com.example.databasetest.provider/book")            val values = contentValuesOf("name" to "A Clash of Kings", "author" to "George Martin", "pages" to 1040, "price" to 22.85)            val newUri = contentResolver.insert(uri, values)            bookId = newUri?.pathSegments?.get(1)        }        queryData.setOnClickListener {            // 查询数据            val uri = Uri.parse("content://com.example.databasetest.provider/book")            contentResolver.query(uri, null, null, null, null)?.build {                while (moveToNext()) {                    val name = getString(getColumnIndex("name"))                    val author = getString(getColumnIndex("author"))                    val pages = getInt(getColumnIndex("pages"))                    val price = getDouble(getColumnIndex("price"))                    Log.d("MainActivity", "book name is $name")                    Log.d("MainActivity", "book author is $author")                    Log.d("MainActivity", "book pages is $pages")                    Log.d("MainActivity", "book price is $price")                }                close()            }        }        updateData.setOnClickListener {            // 更新数据            bookId?.let {                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")                val values = contentValuesOf("name" to "A Storm of Swords", "pages" to 1216, "price" to 24.05)                contentResolver.update(uri, values, null, null)            }        }        deleteData.setOnClickListener {            // 删除数据            bookId?.let {                val uri = Uri.parse("content://com.example.databasetest.provider/book/$it")                contentResolver.delete(uri, null, null)            }        }    }}
> ```
>
> ### 读其他基本用法
>
> * 使用`ContentResolver`类作为接口操作
> * 一般就是增删改查
> * 此处注意定位的唯一标志符换成了URI，如`content://whu.example.app.provider/songTable`
> * 然后增删改查逻辑和之前低配版的`sqlite`一样，总之不能用SQL就特别蠢
>
> ### 自定义基本用法
>
> * 创建一个类继承`ContentProvider`，然后重写6个方法
> * 然后在`AndroidManifest.xml`中进行provider的注册
>
> ***

## 第九章 - 丰富的多媒体

> ### 通知
>
> * 通知是Android一开始就有的功能，iOS最开始是没有通知功能的，直到后面意识到了通知的重要性才加入了通知功能
> * 通知一开始的设计是非常好的。但开发者为了让自己的APP保持活跃，不断向用户发送源源不断的通知，导致用户十分厌恶
>
> ### 通知渠道
>
> * 之前某个应用的通知要么全部查看要么全部关闭，粒度很粗
> * 而通知渠道就是为了细化通知的粒度。即一个应用的通知分为很多个渠道，用户可以自定义选择关闭某个或者打开某些通知渠道，收看自己感兴趣的通知
>
> ### 通知渠道创建
>
> ```kotlin
> if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){    val channel = NotificationChannel("id", "music comes", NotificationManager.IMPORTANCE_HIGH)    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager    manager.createNotificationChannel(channel)}
> ```
>
> ### 通知创建
>
> ```kotlin
> val notice = NotificationCompat.Builder(this,"id")	.setContentTitle("contentTitle")	.setContentText("contentText")	.setSmallIcon(R.drawable.ic_launcher_background)	.build()manager.notify(1,notice)
> ```
>
> ### 设置通知点击事件
>
> ```kotlin
> button.setOnClickListener {    //新增的两行代码    val intent = Intent(this,SecondActivity::class.java)    val pendingIntent = PendingIntent.getActivity(this,0,intent,0)    val notice = NotificationCompat.Builder(this,"id")    .setContentTitle("contentTitle")    .setContentText("contentText")    .setSmallIcon(R.drawable.ic_launcher_background)    .setContentIntent(pendingIntent)//新增的一行代码    .setAutoCancel(true)    .build()    manager.notify(1,notice)}
> ```
>
> * PendingIntent可以理解成延迟执行的Intent
>
> ***

## 第十章 - Service

> ### 后台功能
>
> * Android是一开始就支持丰富的后台功能，也正是由于其后台功能过于开发，以至于后面的版本都在限制其后台的功能
> * iOS一开始是没有后台功能的，它是在后面意识到了后后台的重要性才加入了后台功能
>
> ### service是什么
>
> * service并不是独立存在并运行的一个进程
> * 一个service必须依赖于某一个特定的进程之中，当这个进程被杀死时，对应的service也会终止
> * service并不会自己主动去开启多线程，当需要时比我我们自己去创建多线程，否则就有可能阻塞掉主线程
>
> ### kotlin中的线程写法
>
> ```kotlin
> thread{      //代码逻辑}
> ```
>
> ### 多线程异步更新UI
>
> ```kotlin
> val toChangeText = 1var isChanged = falseval handler = object : Handler(Looper.getMainLooper()){        override fun handleMessage(msg: Message) {                super.handleMessage(msg)                when(msg.what){                        toChangeText -> textView.text = if (isChanged){                isChanged = false;                "true"            }else{                isChanged = true;                "false"            }                }        }}override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_main)        changeText.setOnClickListener {                thread {                        val msg = Message()                        msg.what = toChangeText                        handler.sendMessage(msg)                }        }}
> ```
>
> * Android中更新UI只能在主线程中进行，在子线程中更新被认为是不安全的行为
> * 首先创建一个消息处理器handler，这个handler有点类似于epoll的感觉，会传入一个循环器来一直查询可以处理的任务
> * 然后在通过这个处理器来发送消息
>
> ### 异步消息的四个部分
>
> * `Handler`：最后所用的消息处理逻辑都会回调到`handlerMessage()`当中，根据不同的消息值进行不同的处理
> * `Message`：可以理解成一个唯一的定位ID
> * `MessageQueue`
> * `Looper`：管家作用，会循环查看有无任务，如果有就从`MessageQueue`取出
>
> ### Service创建
>
> * 创建了过后一定要在`AndroidManifest.xml`中进行注册
> * 只不过Android Studio已经自动化帮助我们完成了注册工作
>
> ### Service方法
>
> * `onBind(intent:Intent):IBinder`
> * `onCreate()`：调用`context.startServie()`第一次未创建时调用
> * `onStartCommand(intent:Intent,flags:Int,startId:Int):Int`：每次`context.startServie()`进行调用
> * `onDestroy()`：每次`context.stopServie()`调用
>
> ### 启动停止Service
>
> ```kotlin
> val intent = Intent(this,MainService::class)startService(intent)stopService(intent)
> ```
>
> ### 不稳定性
>
> * 只有当service对应的activity处于前台时，才能保证service的稳定运行
> * 否则由于很多滥用后台，service随时可能会被Android系统给回收掉
>
> ### 与Activity绑定
>
> * 首先在Service中要重写`onBind()`方法，重写之前还要自定义绑定后可以做的事情的`Binder`对象
>
>   ```kotlin
>   class MainService : Service() {        class DownLoadBinder: Binder() {                fun startDownLoad(){                        Log.d("MainService","startDownload called")                }                fun getProgress(){                        Log.d("MainService","getProgress called")                }        }        override fun onBind(intent: Intent): IBinder {                Log.d("MainService","onBind called")                return DownLoadBinder()        }}
>   ```
>
> * 在Activity中创建一个用于实现绑定的连接connection。并在连接里面调用自定义的方法
>
>   ```kotlin
>   lateinit var downloadBinder: MainService.DownLoadBinderprivate val connection = object : ServiceConnection{        override fun onServiceDisconnected(name: ComponentName?) {                Log.d("MainService","disconect called")        }        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {                downloadBinder = service as MainService.DownLoadBinder                downloadBinder.startDownLoad()                downloadBinder.getProgress()        }}
>   ```
>
> * 最后绑定和解绑
>
>   ```kotlin
>   val intent = Intent(this,MainService::class.java)startBind.setOnClickListener {    bindService(intent,connection,Context.BIND_AUTO_CREATE)}stopBind.setOnClickListener {    unbindService(connection)}
>   ```
>
>   * 其中`Context.BIND_AUTO_CREATE`表示绑定时如果service未创建会自动创建
>
> ### 绑定的生命周期
>
> * 当`bindService()`调用时，首先执行`onCreate()`方法，然后执行`onBind()`方法，然后用户就可以执行Binder里面自定义的逻辑
> * 当`unBindService()`执行时，首先执行`onUnBind()`，然后执行`onDestroy()`方法
>
> ### 执行的生命周期
>
> * 当`startService()`执行时，如果未创建就首先执行`onCreat()`方法，然后执行`onStartCommand()`方法
> * 当`stopService()`执行时，执行`onDestroy()`方法
>
> ### 前台Service
>
> * 即一直运行在前台的service，例如下滑网易云的播放暂停那个
> * 总之就非常类似于通知的效果，系统一般不会回收前台的service
> * 创建前台service的方法和通知的方法非常类似
> * 注意Android 9.0后面的版本，前台service必须要在`AndroidManifest.xml`中进行声明
>
> ### 线程
>
> * service中的所有逻辑默认都是运行在主线程中的
> * 因此service中耗时的逻辑应该放在其他的线程
>
> ### IntentService
>
> * IntentService就是带有多线程处理耗时逻辑，进行了高层封装的一个service
>
>   ```kotlin
>   class MyIntentService : IntentService("intentService") {    override fun onHandleIntent(intent: Intent?) {        //处理耗时逻辑        //处理完毕会自动调用stopItselt()方法    }}
>   ```
>
> ***

## 第十一章 - 网络技术

> ### 使用webview
>
> * 添加控件，webview实际上就是一个控件
>
>   ```xml
>   <WebView    android:layout_width="match_parent"    android:layout_height="match_parent"    android:id="@+id/webView" />
>   ```
>
> * `AndroidManifest.xml`中声明网络权限和`usesCleartextTraffic`
>
>   ```xml
>   <uses-permission android:name="android.permission.INTERNET" />    <application        android:usesCleartextTraffic="true" />
>   ```
>
> * 使用
>
>   ```kotlin
>   webView.settings.javaScriptEnabled = truewebView.webViewClient = WebViewClient()webView.loadUrl("http://47.108.63.126")
>   ```
>
>   * 其中`webViewClient`作用是当进行网页跳转时依然使用webview，而不是打开系统的浏览器
>
> ### 导入依赖
>
> * `implementation 'com.squareup.okhttp3:okhttp:4.9.1'`
>
> ***

## 第十二章 - Jetpack

> ### ViewModel创建
>
> ```kotlin
> class MainViewModel(countReserved: Int) : ViewModel() { val counter: LiveData<Int>     get() = _counter private val _counter = MutableLiveData<Int>() init {     _counter.value = countReserved } fun plusOne() {     val count = _counter.value ?: 0     _counter.value = count + 1 } fun clear() {     _counter.value = 0 }}
> ```
>
> ### 构造工厂创建
>
> ```kotlin
> class MainViewModelFactory(private val countReserved: Int) : ViewModelProvider.Factory { override fun <T : ViewModel> create(modelClass: Class<T>): T {     return MainViewModel(countReserved) as T }}
> ```
>
> ### 使用
>
> ```kotlin
> lateinit var viewModel: MainViewModellateinit var sp: SharedPreferencesoverride fun onCreate(savedInstanceState: Bundle?) {     super.onCreate(savedInstanceState)     setContentView(R.layout.activity_main)     sp = getPreferences(Context.MODE_PRIVATE)     val countReserved = sp.getInt("count_reserved", 0)     viewModel = ViewModelProviders.of(this, MainViewModelFactory(countReserved)).get(MainViewModel::class.java)     plusOneBtn.setOnClickListener {         viewModel.plusOne()     }     clearBtn.setOnClickListener {         viewModel.clear()     }     viewModel.counter.observe(this, Observer{ count ->         infoText.text = count.toString()     })}
> ```
>
> ***

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
> class MyLifecycleObserver:LifecycleObserver {    @OnLifecycleEvent(Lifecycle.Event.ON_START)    fun onStart(){        Log.d("LifecycleObserver","onStart")    }    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)    fun onStop(){        Log.d("LifecycleObserver","onStop")    }    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)    fun onDestroy(){        Log.d("LifecycleObserver","onDestroy")    }}
> ```
>
> * 实现`LifecycleObserver`
> * 使用`@OnLifecycleEvent`注解
>
> ### `Obserable`订阅`Observer`
>
> ```kotlin
> class FirstFragment : Fragment() {    private lateinit var myLifecycleObserver: MyLifecycleObserver    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        myLifecycleObserver = MyLifecycleObserver(this)        lifecycle.addObserver(myLifecycleObserver)    }}
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