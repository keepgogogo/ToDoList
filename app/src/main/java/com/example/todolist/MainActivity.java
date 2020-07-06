package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * @author Dust
 */
public class MainActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    /**
     *  message.what Date
     */
    final static int updateDate=1;
    private static final String TAG ="MAINACTIVITY" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *  get a SharedPreferences file named "NormalData" to save some fundamental data
         */
        final SharedPreferences preferences=getSharedPreferences("NormalData",MODE_PRIVATE);
        final SharedPreferences.Editor preferencesEditor=getSharedPreferences("NormalData",MODE_PRIVATE).edit();



        //get date of today
        TimeManager handler=new TimeManager();
        handler.setPreferencesEditor(preferencesEditor);
        getDate(handler);

        Button button=findViewById(R.id.buttonForToday);
        Log.d(TAG, "Button was working");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TextView textView=findViewById(R.id.forTest);
//                textView.setText(preferences.getInt("Year",0));
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(preferences.getInt("Year",0));
                stringBuilder.append(preferences.getInt("Month",0));
                stringBuilder.append(preferences.getInt("Day",0));
                Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDate(TimeManager handler)
    {
        TimeHandlerClass timeHandlerClass=new TimeHandlerClass();
        timeHandlerClass.renewDateOfToday(handler);
    }


    /**
     * build a Handler to process data from get date thread
     */

    protected static class TimeManager extends Handler{

        final static int DATE_YEAR=0;
        final static int DATE_MONTH=1;
        final static int DATE_DAY_OF_MONTH=2;
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
                case updateDate:
                    Log.d(TAG, "handleMessage: Handler has gained back data");
                    int[] date=(int[])msg.obj;
                    preferencesEditor.putInt("Year",date[DATE_YEAR]);
                    preferencesEditor.putInt("Month",date[DATE_MONTH]);
                    preferencesEditor.putInt("Day",date[DATE_DAY_OF_MONTH]);
                    preferencesEditor.apply();
                    Log.d(TAG, "handleMessage: SharedPreference has been set");
                    break;
                default:
                    break;
            }
        }
    }

}
