package com.lifecycle;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;



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
