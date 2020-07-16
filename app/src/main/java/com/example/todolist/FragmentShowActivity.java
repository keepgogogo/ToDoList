package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.todolist.fragment.AddNewPlanFragment;

import java.util.Objects;

public class FragmentShowActivity extends AppCompatActivity {

    FragmentManager fragmentManager=getSupportFragmentManager();
    FragmentTransaction transaction=fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_show);
        switch (Objects.requireNonNull(getIntent().getStringExtra("FRAGMENT_TO_START")))
        {
            case "ADD_NEW_PLAN" :
                startFragment(new AddNewPlanFragment(),R.id.fragment_container);
                break;
        }
    }


    /**
     * start a fragment, 使用需启动的碎片的实例作为参数
     * @param fragment fragment to be start
     * @param idOfLayout    layout to be loaded
     */
    private void startFragment(Fragment fragment, int idOfLayout)
    {
        transaction.add(idOfLayout,fragment)
                .commit();
    }





}