package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class MyAllPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_plan);
        setTitle("所有日程");
    }

    //set the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_of_my_all_plan_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.no_old_plan_in_allPlanActivity:

                break;
            case R.id.delete_old_plan_in_allPlanActivity:
                break;
            case R.id.show_old_plan_in_allPlanActivity:
                break;
            default:
                break;
        }
        return true;
    }


    protected class MyAllPlanActivityHandler extends Handler
    {
        @Override
        public void handleMessage(@NotNull Message message)
        {
            switch (message.what)
            {

            }
        }
    }
}