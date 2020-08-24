package com.oralcalculation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

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