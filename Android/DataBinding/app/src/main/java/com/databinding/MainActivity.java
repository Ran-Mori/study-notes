package com.databinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.databinding.databinding.ActivityMainBinding;

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