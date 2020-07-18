package com.example.todolist.recycler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.PlanElements;

import java.util.List;

public class PlanShowRecyclerViewModel extends ViewModel {

    private MutableLiveData<List<PlanElements>> currentData;

    public MutableLiveData<List<PlanElements>> getCurrentData()
    {
        if(currentData==null)
        {
            currentData=new MutableLiveData<List<PlanElements>>();
        }
        return currentData;
    }
}
