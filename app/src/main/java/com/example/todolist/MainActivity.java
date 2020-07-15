package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.todolist.fragment.AddNewPlanFragment;

import javax.annotation.Nonnull;

/**
 * @author Dust
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     *  message.what Date
     */
    final static int UPDATE_DATE =1;
    private static final String TAG ="MAINACTIVITY" ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences preferences=getSharedPreferences("NormalData",MODE_PRIVATE);
        final SharedPreferences.Editor preferencesEditor=getSharedPreferences("NormalData",MODE_PRIVATE).edit();

        //get date of today
        TimeManager handler=new TimeManager();
        handler.setPreferencesEditor(preferencesEditor);
        getDate(handler);

        Button button=findViewById(R.id.buttonForToday);
        button.setOnClickListener(this);
        Log.d(TAG, "Button was working");

    }

    //set the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_of_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_new_plan:
                startFragment(new AddNewPlanFragment(),R.layout.add_plan_fragment);
                break;
            default:
                startFragment(new AddNewPlanFragment(),R.layout.activity_main);
                break;
        }
        return true;
    }

    /**
     * start a fragment, 使用需启动的碎片的实例作为参数
     * @param fragment
     * @param idOfLayout
     */
    private void startFragment(Fragment fragment,int idOfLayout)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(idOfLayout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }








    //Get the date
    private void getDate(TimeManager handler)
    {
        TimeHandlerClass timeHandlerClass=new TimeHandlerClass();
        timeHandlerClass.renewDateOfToday(handler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonForToday:
                final SharedPreferences preferences=getSharedPreferences("NormalData",MODE_PRIVATE);
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(preferences.getInt("Date",0));
                stringBuilder.append('\n');
                stringBuilder.append(preferences.getInt("Hour",0));
                stringBuilder.append(":");
                stringBuilder.append(preferences.getInt("Minute",0));
                Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "onClick: end"+stringBuilder.toString());
                break;
            default:
                break;
        }
    }


    /**
     * build a Handler to process data from get date thread
     * It was built as a inner class of MainActivity
     */

    protected static class TimeManager extends Handler{

        final static int DATE=0;
        final static int DATE_HOUR_24=1;
        final static int DATE_MINUTES=2;
        private static final String TAG =".TimeManager" ;
        SharedPreferences.Editor preferencesEditor=null;

        void setPreferencesEditor(SharedPreferences.Editor preferencesEditor)
        {
            this.preferencesEditor=preferencesEditor;
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPDATE_DATE:
                    Log.d(TAG, "handleMessage: Handler has gained back data");
                    int[] date=(int[])msg.obj;
                    preferencesEditor.putInt("Date",date[DATE]);
                    preferencesEditor.putInt("Hour",date[DATE_HOUR_24]);
                    preferencesEditor.putInt("Minute",date[DATE_MINUTES]);
                    preferencesEditor.apply();
                    Log.d(TAG, "handleMessage: SharedPreference has been set");
                    break;
                default:
                    break;
            }
        }
    }

}
