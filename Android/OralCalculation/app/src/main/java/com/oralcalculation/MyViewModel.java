package com.oralcalculation;

import android.app.Activity;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;


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
