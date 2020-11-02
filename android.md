# Android开发



## bug记录

> * gradle下载不了，进入 **https://services.gradle.org/distributions/**下载完成后拖入 **C:\users\izumi\.gradle\……**
> * 自定义的ViewModel不能设置构造函数，否则会出现应用无法进入闪退现象
> * 篮球计分板两个队撤销操作不用区分上一次操作的是谁，直接把两个之前的值都存下来，赋值就行。没有变化的项相当于重复赋了一次值
> * **dataBinding.enabled true** 已经过时替换为 **buidFeatures.dataBinding true**
> * **"+".equals(operator.getValue())** 的 **getValue()不能省略**
> * **GET** 请求不能带请求体，不然绝对访问失败
> * **Volley框架** 有时返回的json数据默认不是使用UTF-8进行编码，可能会乱码。乱码可以重新Request

## ViewModle入门

```java
public class MainActivity extends AppCompatActivity {
    //创建自定义ViewModel,文本框，按钮的引用
    public MyViewModel myViewModel;
    private TextView textView1;
    private TextView textView2;
    private Button add;
    private Button sub;
    private Button live;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定文本框，按钮
        textView1=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        add=findViewById(R.id.button);
        sub=findViewById(R.id.button2);
        live=findViewById(R.id.button3);

        //设置自定义ViewModel的类格式
        myViewModel=ViewModelProviders.of(this).get(MyViewModel.class);

        //让每次加载的时候文本框都有值，不会突然为0
        textView1.setText(String.valueOf(myViewModel.getI()));

        //通过按钮来改变自定义ViewModel里属性的值，并改变文本框的值
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentI = myViewModel.getI();
                myViewModel.setI(++currentI);
                textView1.setText(String.valueOf(myViewModel.getI()));
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentI=myViewModel.getI();
                myViewModel.setI(--currentI);
                textView1.setText(String.valueOf(myViewModel.getI()));
            }
        });

        //使用观察者模式，当值改变时就立刻改变另一个文本框的值
        myViewModel.getJ().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textView2.setText(String.valueOf(integer));
            }
        });

        //通过按钮调用自定义ViewModel内的接口改变liveData的值
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.increase();
            }
        });
    }
}
```

```java
public class MyViewModel extends ViewModel {
    //定义普通变量
    private int i=0;

    //定义live变量
    private MutableLiveData<Integer> j=new MutableLiveData<>(0);

    public void increase(){
        j.setValue(j.getValue()+1);
    }

    public MutableLiveData<Integer> getJ() {
        return j;
    }


    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
```





## DataBinding-xml绑定到MainActivity

* gradle配置文件打开databinding

```json
defaultConfig {
    dataBinding {
        enabled true
    }
}
```

* layout的XML文件切换为dataBinding模式
* MainActivity部分

```java 
public class MainActivity extends AppCompatActivity {
    private MyViewModel myViewModel;

    //当切换成DataBinding模式后会自动创建一个以那个XML文件名为类名的类
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注释掉本身的这句话
        //setContentView(R.layout.activity_main);

        //设置binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //和上一个项目一样
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        myViewModel.getMutableLiveDataInteger().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.textView.setText(String.valueOf(integer));
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewModel.increase();
            }
        });
      }
}
```



## DataBinding-全部绑定到XML

* xml文件

  ```XML
  <?xml version="1.0" encoding="utf-8"?>
  <layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools">
  
      <data>
          <variable
              name="myViewModel"
              type="com.databinding.MyViewModel" />
      </data>
  
  
          <TextView
              android:text="@{String.valueOf(myViewModel.getMutableLiveDataInteger().getValue())}"/>
  
          <Button
              android:onClick="@{()->myViewModel.increase()}" />
  
      </androidx.constraintlayout.widget.ConstraintLayout>
  </layout>
  ```

* MainActivity文件

  ```java
  public class MainActivity extends AppCompatActivity {
      private MyViewModel myViewModel;
  
      //当切换成DataBinding模式后会自动创建一个以那个XML文件名为类名的类
      private ActivityMainBinding binding;
  
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
  
          //注释掉本身的这句话
          //setContentView(R.layout.activity_main);
  
          //设置binding
          binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
  
          //和上一个项目一样
          myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
  
          //进行必要的设置
          binding.setMyViewModel(myViewModel);
          binding.setLifecycleOwner(this);
  
        }
  }
  ```



## 保存ViewModel状态，非持久化

* 引入依赖

  ```json
  implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0'
  ```

* ViewModel

  ```java
  public class MyViewModel extends ViewModel {
      private SavedStateHandle handle;
      public MyViewModel(SavedStateHandle handle){
          this.handle=handle;
      }
      public MutableLiveData<Integer> getMutableLiveDataInteger() {
          if (!handle.contains("number"))
              handle.set("number",0);
          return handle.getLiveData("number");
      }
  
  
      public void increase(){
          getMutableLiveDataInteger().setValue(getMutableLiveDataInteger().getValue()+1);
      }
  }
  ```

* MainActivity

  ```java
  public class MainActivity extends AppCompatActivity {
      private MyViewModel myViewModel;
  
      //当切换成DataBinding模式后会自动创建一个以那个XML文件名为类名的类
      private ActivityMainBinding binding;
  
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
  
          //设置binding
          binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
  
          //可以使用，但过时了
          //myViewModel = ViewModelProviders.of(this,new SavedStateViewModelFactory(getApplication(),this)).get(MyViewModel.class);
  
          //无法创建，但是最新的写法
          //myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
  
          //可以使用
          myViewModel = new SavedStateViewModelFactory(getApplication(), this).create(MyViewModel.class);
  
          //进行必要的设置
          binding.setMyViewModel(this.myViewModel);
          binding.setLifecycleOwner(this);
        }
  
  }
  ```



## 按键导航Navigator

* start页面的控制类‘

  ```java
  @Override
      public void onActivityCreated(@Nullable Bundle savedInstanceState) {
          super.onActivityCreated(savedInstanceState);
  
          //先锁定所属的View，在通过View获取控件
          Button courses=getView().findViewById(button);
          Button login=getView().findViewById(button2);
  
          //设置点击事件
          courses.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //通过view所属的导航控件找到导航控件
                  NavController navController = Navigation.findNavController(view);
                  //进行路由导航
                  navController.navigate(action_main_to_courses);
              }
          });
  
          login.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  NavController navController = Navigation.findNavController(view);
                  navController.navigate(action_main_to_login2);
              }
          });
      }
  ```

* MainActivity类

  ```java
  public class MainActivity extends AppCompatActivity {
  
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
  
          //通过activity和导航host找到导航控制器
          NavController navController = Navigation.findNavController(this, R.id.fragment5);
          //设置返回键的UI
          NavigationUI.setupActionBarWithNavController(this,navController);
      }
  
  
      @Override
      public boolean onSupportNavigateUp() {
          //设置开启返回
          //label标签名可以在nav.xml中自定义
          return Navigation.findNavController(this,R.id.fragment5).navigateUp();
      }
  }
  ```



## 口算挑战应用

* gradle配置

  ```json
  apply plugin: 'com.android.application'
  
  android {
      defaultConfig {
          buildFeatures.dataBinding true
      }
  }
  
  dependencies {
      implementation 'androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0'
  }
  ```

* 计算核心页面xml文件

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <layout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:tools="http://schemas.android.com/tools">
  
      <data>
          <variable
              name="data"
              type="com.oralcalculation.MyViewModel" />
      </data>
  
      <androidx.constraintlayout.widget.ConstraintLayout
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          tools:context=".CalculationPage">
  
          <Button
              android:onClick="@{()->data.append(0)}"
              android:text="0" />
  
  
          <TextView
              android:text="@{String.valueOf(data.firstAdd)+' '+data.operator+' '+String.valueOf(data.secondAdd)+' '+'='+' '+'?'}"/>
  
  
          <TextView
              android:text="@{String.valueOf(data.result)}" />
  
          <Button
              android:onClick="@{()->data.append(1)}"
              android:text="1" />
  
          <Button
              android:onClick="@{()->data.append('-')}"
              android:text="-"/>
  
          <Button
              android:onClick="@{()->data.judge()}"
              android:text="提交"/>
      </androidx.constraintlayout.widget.ConstraintLayout>
  </layout>
  ```

* 管理数据的ViewModel

  ```java
  public class MyViewModel extends ViewModel {
      //因为要在管理ViewModel里面实现导航，所以需要一个Activity
      private Activity activity;
      public Activity getActivity() {
          return activity;
      }
      public void setActivity(Activity activity) {
          this.activity = activity;
      }
      
      //设置的共享数据
      public MutableLiveData<Integer> firstAdd=new MutableLiveData<>();
      public MutableLiveData<Integer> secondAdd=new MutableLiveData<>();
      public MutableLiveData<String> operator=new MutableLiveData<>();
      public MutableLiveData<String> result=new MutableLiveData<>();
      
      //把所有数据都初始化，两个运算数、一个运算符随机自定义。结果设置为空
      public void initAll(){
          firstAdd.setValue((int)(Math.random()*100));
          secondAdd.setValue((int)((Math.random()*100)));
  
          if ((int)(Math.random()*2)==0)
              operator.setValue("+");
          else
              operator.setValue("-");
  
          result.setValue("");
      }
      
      //按下按键在结果后面追加
      public void append(Integer i){
          result.setValue(result.getValue()+i);
      }
      public void append(Character i){
          result.setValue(result.getValue()+i);
      }
      
      //判断结果是对还是错
      public void judge(){
          //获取系统运算的正确结果
          Integer correctResult;
          if ("+".equals(operator.getValue()))
              correctResult=firstAdd.getValue()+secondAdd.getValue();
          else
              correctResult=firstAdd.getValue()-secondAdd.getValue();
          
          //获取人工挑战者计算的人工结果
          Integer currentResult=Integer.parseInt(result.getValue());
          
          
          if (currentResult.equals(correctResult))
              //运算正确就把所有数据刷新继续算
              initAll();
          else 
              //错误就导航到失败页面
          Navigation.findNavController(activity,R.id.fragment).navigate(R.id.action_calculationPage_to_failPage);
      }
  }
  ```

* MainActivity

  ```java
  public class MainActivity extends AppCompatActivity {
  
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          
          //把ViewModel装入Activity
          new ViewModelProvider(this).get(MyViewModel.class).setActivity(this);
          
          //开启导航返回键
          NavController navController = Navigation.findNavController(this, R.id.fragment);
          NavigationUI.setupActionBarWithNavController(this,navController);
      }
      
      //开启导航返回键
      @Override
      public boolean onSupportNavigateUp() {
          return Navigation.findNavController(this,R.id.failPage).navigateUp();
      }
  }
  ```

* HomePage

  ```java
  public class HomePage extends Fragment {
      @Override
      public void onActivityCreated(@Nullable Bundle savedInstanceState) {
          super.onActivityCreated(savedInstanceState);
          //fragment获取控件需要先获取View
          getView().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Navigation.findNavController(view).navigate(R.id.action_homePage_to_calculationPage);
              }
          });
      }
  }
  ```

* 核心CaculatePage

  ```java
  public class CalculationPage extends Fragment {
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          //初始化ViewModel，并传入核心参数activity
          MyViewModel myViewModel = new ViewModelProvider(getActivity()).get(MyViewModel.class);
          myViewModel.setActivity(getActivity());
          
          //与XML绑定
          FragmentCalculationPageBinding binding= DataBindingUtil.inflate(inflater,R.layout.fragment_calculation_page,container,false);
          binding.setData(myViewModel);
          binding.setLifecycleOwner(getActivity());
          
          //一上来就先初始化一次
          myViewModel.initAll();
          
          //返回根View
          return binding.getRoot();
      }
  }
  ```

  



## LifeCycle和自定义组件

* 自定义组件类

  ```java 
  public class MyChronometer extends Chronometer implements LifecycleObserver {
  
      //因为组件有参数，因此选择带AttributeSet的构造函数
      public MyChronometer(Context context, AttributeSet attrs) {
          super(context, attrs);
      }
  
      @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
      public void onStart(){
          setBase(SystemClock.elapsedRealtime());
          System.out.println("调用了start");
          start();
      }
  
      @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
      public void onPause(){
          System.out.println("调用了pause");
          stop();
      }
  
      @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
      public void onResume(){
          System.out.println("调用了resume");
          start();
      }
  }
  ```

* MainActivity类

  ```java
  public class MainActivity extends AppCompatActivity {
      //设置自定义组件的引用
      MyChronometer myChronometer;
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
  
          //进行组件的绑定
          myChronometer=findViewById(R.id.clock);
          //让组件观察MainActivity的生命周期
          getLifecycle().addObserver(myChronometer);
      }
  }
  ```





## Room

* 导入依赖

  ```json
  dependencies {
      def room_version = "2.2.5"
  
      implementation "androidx.room:room-runtime:$room_version"
      annotationProcessor "androidx.room:room-compiler:$room_version" // For Kotlin use kapt instead of annotationProcessor
      
      // Test helpers
      testImplementation "androidx.room:room-testing:$room_version"
  
  }
  ```



## json序列化 （不区分是否数组）

* 导入GSON依赖

  ```json 
  implementation 'com.google.code.gson:gson:2.8.6'
  ```

* 对象转json

  ```java
  new Gson().toJson(student)
  ```

* json转对象

  ```java
  Student fromJson = new Gson().fromJson(s, Student.class);
  ```



## http请求组件Volley

* 导入依赖

  ```json 
  implementation 'com.android.volley:volley:1.1.1'
  ```

* 给安卓应用网络权限，在AndroirdMianfest.xml里面

  ```xml
  <uses-permission android:name="android.permission.INTERNET"/>
  ```

* StringRequest GET请求案例

  ```java 
  //创建一个队列，用于请求排队，方便异步处理
  RequestQueue requestQueue = Volley.newRequestQueue(this);
          
  //创建一个请求，这个请求有4个参数
  StringRequest request=new StringRequest(
       StringRequest.Method.GET,
       "http://47.113.97.26:8002/course?userId=13",
       new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 textView.setText(response.toString());
            }
       },
       new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                  textView.setText("访问失败");
             }
       }
  );
          
  //把请求放进队列里面等待执行
  requestQueue.add(request);
  ```

* JsonObjectRequest的POST请求

  ```java
  //创建一个队列，用于请求排队，方便异步处理
  RequestQueue requestQueue = Volley.newRequestQueue(this);
  //put一些键值对进去
  Map<String,Object> map= new HashMap();
  map.put("account","123456");
  map.put("password","123456");
  //设置请求需要用的请求体JSONObject格式数据
  JSONObject jsonObject=new JSONObject(map);
  
  //开始发送请求
  JsonObjectRequest objectRequest=new JsonObjectRequest(
       //方法是POST
       JsonRequest.Method.POST,
       //设置访问的URL
       "http://47.113.97.26:8001/user/register",
       //传入之前的JSONObject对象
       jsonObject,
       //设置成功的处理
       new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
                 textView.setText(response.toString());
           }
       },
       //设置失败的处理
       new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
                 textView.setText("访问失败");
           }
       }
  );
  
  //把请求放进队列里面等待执行
  requestQueue.add(objectRequest);
  ```

  