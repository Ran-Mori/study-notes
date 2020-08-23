package com.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

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