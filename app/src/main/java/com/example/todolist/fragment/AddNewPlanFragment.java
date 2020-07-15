package com.example.todolist.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.todolist.R;

import org.jetbrains.annotations.NotNull;

public class AddNewPlanFragment extends Fragment {
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.add_plan_fragment,container,false);
    }
}
