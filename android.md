# Android开发



## bug记录

* gradle下载不了，进入 **https://services.gradle.org/distributions/**下载完成后拖入 **C:\users\izumi\.gradle\……**
* 自定义的ViewModel不能设置构造函数，否则会出现应用无法进入闪退现象
* 篮球计分板两个队撤销操作不用区分上一次操作的是谁，直接把两个之前的值都存下来，赋值就行。没有变化的项相当于重复赋了一次值
*  **dataBinding.enabled true** 已经过时替换为 **buidFeatures.dataBinding true**



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

  