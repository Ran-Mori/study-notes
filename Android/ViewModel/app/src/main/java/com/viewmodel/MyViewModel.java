package com.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
