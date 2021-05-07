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
> AlertDialog.Builder(this).apply {
>     setTitle("测试警告框标题")
>     setMessage("测试警告框信息")
>     setPositiveButton("确定"){
>         dialog, which -> Log.d("警告框","点击确定")
>     }
>     setNegativeButton("取消"){
>         dialog, which -> Log.d("警告框","点击取消")
>     }
>     show()
> }
> ```
>
> * 最后一定不要忘了加上`show()`，经常忘加
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
> class SongAdapter(val songList: List<Song>): RecyclerView.Adapter<SongAdapter.ViewHolder>() {
> 
>     inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
>         val songIdTextView: TextView = view.findViewById(R.id.songId)
>         val songNameTextView: TextView = view.findViewById(R.id.songName)
>         val songSingerTextView: TextView = view.findViewById(R.id.songSinger)
>     }
> 
>     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
>         val view: View = LayoutInflater.from(parent.context).inflate(R.layout.song_item,parent,false)
>         val viewHolder = ViewHolder(view)
>         viewHolder.songNameTextView.setOnClickListener {
>             Toast.makeText(parent.context,"songname = ${viewHolder.songNameTextView.text}",Toast.LENGTH_SHORT).show()
>         }
>         return viewHolder
>     }
> 
>     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
>         val song: Song = songList[position]
>         holder.songIdTextView.text = song.id.toString()
>         holder.songNameTextView.text = song.songName
>         holder.songSingerTextView.text = song.songSinger
>     }
> 
>     override fun getItemCount() = songList.size
> }
> ```
>
> ### Main
>
> ```kotlin
> recyclerView.layoutManager = LinearLayoutManager(this)
> recyclerView.adapter = SongAdapter(songList)
> ```
>
> ***

