package com.oralcalculation;

import android.app.Activity;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;


public class MyViewModel extends ViewModel {

    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public MutableLiveData<Integer> firstAdd=new MutableLiveData<>();
    public MutableLiveData<Integer> secondAdd=new MutableLiveData<>();
    public MutableLiveData<String> operator=new MutableLiveData<>();
    public MutableLiveData<String> result=new MutableLiveData<>();

    public void initAll(){
        firstAdd.setValue((int)(Math.random()*100));
        secondAdd.setValue((int)((Math.random()*100)));

        if ((int)(Math.random()*2)==0)
            operator.setValue("+");
        else
            operator.setValue("-");

        result.setValue("");
    }

    public void append(Integer i){
        result.setValue(result.getValue()+i);
    }
    public void append(Character i){
        result.setValue(result.getValue()+i);
    }

    public void judge(){
        Integer correctResult;
        if ("+".equals(operator.getValue()))
            correctResult=firstAdd.getValue()+secondAdd.getValue();
        else
            correctResult=firstAdd.getValue()-secondAdd.getValue();

        System.out.println("正确结果="+correctResult);

        Integer currentResult=Integer.parseInt(result.getValue());

        System.out.println("我计算的="+currentResult);
        if (currentResult.equals(correctResult))
            initAll();
        else {
            Navigation.findNavController(activity,R.id.fragment).navigate(R.id.action_calculationPage_to_failPage);
        }
    }
}
