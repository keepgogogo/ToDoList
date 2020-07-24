package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Nonnull;

/**
 * @author Dust
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     *  message.what Date
     */
    final static int UPDATE_DATE =1;
    final static int UPDATE_WEATHER=2;
    private static final String TAG ="MAINACTIVITY" ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("日程");

        final SharedPreferences preferences=getSharedPreferences("NormalData",MODE_PRIVATE);
        final SharedPreferences.Editor preferencesEditor=getSharedPreferences("NormalData",MODE_PRIVATE).edit();

        //get date of today
        TimeManager handler=new TimeManager();
        handler.setPreferencesEditor(preferencesEditor);

        //如果是第一次打开应用，显示提示
        if (firstOrNot(preferences))promptShow();


        getDate(handler);

        Button button=findViewById(R.id.buttonForToday);
        button.setOnClickListener(this);
        Button button1=findViewById(R.id.buttonForStartMyAllPlanActivityIn_MainActivity);
        button1.setOnClickListener(this);

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
                Intent intent=new Intent(MainActivity.this,PlanAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 检查是否是第一次打开app，以决定是否显示新手提示
     */
    private boolean firstOrNot(SharedPreferences preferences)
    {
        int defValue=-1;
        int firstCheck=preferences.getInt("firstOrNot",defValue);
        boolean state=(defValue==firstCheck);
        if(state) {
            SharedPreferences.Editor editor = getSharedPreferences("history", MODE_PRIVATE).edit();
            editor.putInt("firstOrNot", 1);
            editor.apply();
        }
        return state;
    }

    /**
     * 显示注意事项
     */
    private void promptShow()
    {

        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("提示");
        AssetManager assetManager=getAssets();
        dialog.setMessage(textFileGet("promptMessage.txt",assetManager));
        dialog.setCancelable(false);
        dialog.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TimeManager handler=new TimeManager();
                Message message=new Message();
                message.what=UPDATE_WEATHER;
                handler.sendMessage(message);
            }
        });

        dialog.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        dialog.show();
    }

    /**
     * 读取assets文件夹中文件的内容
     * @param fileName
     * @param assetManager
     * @return
     */
    public String textFileGet(String fileName,AssetManager assetManager)
    {
        StringBuilder message=new StringBuilder();

        try {
            InputStream promptMessage=assetManager.open(fileName);
            BufferedReader reader=new BufferedReader(new InputStreamReader(promptMessage));
            String line="";
            while((line=reader.readLine())!=null)
            {
                message.append(line);
                message.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message.toString();
    }


    //Get the date
    private void getDate(TimeManager handler)
    {
        ThreadHelperClass threadHelperClass =new ThreadHelperClass();
        threadHelperClass.renewDateOfToday(handler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonForToday:
                final SharedPreferences preferences=getSharedPreferences("NormalData",MODE_PRIVATE);
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(preferences.getInt("YEAR",0));
                stringBuilder.append("\\");
                stringBuilder.append(preferences.getInt("MONTH",0));
                stringBuilder.append("\\");
                stringBuilder.append(preferences.getInt("DAY",0));
                Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "onClick: end"+stringBuilder.toString());
                break;
            case R.id.buttonForStartMyAllPlanActivityIn_MainActivity:
                Intent intent=new Intent(MainActivity.this,MyAllPlanActivity.class);;
                startActivity(intent);
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

        final static int YEAR =0;
        final static int MONTH =1;
        final static int DAY =2;
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
                    preferencesEditor.putInt("YEAR",date[YEAR]);
                    preferencesEditor.putInt("MONTH",date[MONTH]);
                    preferencesEditor.putInt("DAY",date[DAY]);

                    int detailDate=date[YEAR];
                    detailDate=detailDate*100+date[MONTH];
                    detailDate=detailDate*100+date[DAY];

                    preferencesEditor.putInt("DETAIL_DATE",detailDate);

                    preferencesEditor.apply();
                    Log.d(TAG, "handleMessage: SharedPreference has been set");
                    break;
                case UPDATE_WEATHER:
                    WeatherGetter weatherGetter=new WeatherGetter();
                    weatherGetter.getWeather();
                default:
                    break;
            }
        }
    }

}
