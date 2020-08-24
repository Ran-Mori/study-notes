package com.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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