package com.databinding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

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
