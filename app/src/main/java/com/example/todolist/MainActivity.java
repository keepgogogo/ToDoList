package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    final static int WRITE_LOCATION=3;
    final static int WRITE_WEATHER=4;
    final static int WRITE_WEATHER_FORECAST=5;
    final static int START_GET_WEATHER=6;

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
        else {
            getPermission();
            getLocation();
        }


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
            SharedPreferences.Editor editor = getSharedPreferences("NormalData", MODE_PRIVATE).edit();
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
     * @param fileName name of the file
     * @param assetManager assets manager
     * @return string of file
     */
    public String textFileGet(String fileName,AssetManager assetManager)
    {
        StringBuilder message=new StringBuilder();

        try {
            InputStream promptMessage=assetManager.open(fileName);
            BufferedReader reader=new BufferedReader(new InputStreamReader(promptMessage));
            String line;
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
                Intent intent=new Intent(MainActivity.this,MyAllPlanActivity.class);
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

    protected class TimeManager extends Handler{

        final static int YEAR =0;
        final static int MONTH =1;
        final static int DAY =2;
        private static final String TAG =".TimeManager" ;
        SharedPreferences.Editor preferencesEditor;

        void setPreferencesEditor(SharedPreferences.Editor preferencesEditor)
        {
            this.preferencesEditor=preferencesEditor;
        }

        TimeManager()
        {
            super();
            preferencesEditor=getSharedPreferences("NormalData", MODE_PRIVATE).edit();
            preferencesEditor.apply();
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
                    getPermission();
                    getLocation();
                    break;

                case WRITE_LOCATION:
                    String[] location=(String[])msg.obj;
                    preferencesEditor.putString("province",location[0]);
                    preferencesEditor.putString("city",location[1]);
                    preferencesEditor.putString("district",location[2]);
                    preferencesEditor.apply();

                    TimeManager handler=new TimeManager();
                    Message message=new Message();
                    message.what=START_GET_WEATHER;
                    handler.sendMessage(message);

                    break;

                case WRITE_WEATHER:
                    String[] weather=(String[])msg.obj;
                    preferencesEditor.putString("TodayWeather",weather[0]);
                    preferencesEditor.putString("TodayTemperature",weather[1]);
                    preferencesEditor.apply();
                    break;

                case WRITE_WEATHER_FORECAST:
                    String[] weatherForecast=(String[])msg.obj;
                    preferencesEditor.putString("tomorrowDayWeather",weatherForecast[0]);
                    preferencesEditor.putString("tomorrowNightWeather",weatherForecast[1]);
                    preferencesEditor.putString("tomorrowDayTemperature",weatherForecast[2]);
                    preferencesEditor.putString("tomorrowNightTemperature",weatherForecast[3]);
                    preferencesEditor.apply();
                    break;
                case START_GET_WEATHER:
                    getWeather();
                    break;
                default:
                    break;
            }
        }
    }

    public void getWeather()
    {
        SharedPreferences preferences=getSharedPreferences("NormalData",MODE_PRIVATE);


        WeatherSearchQuery mQuery=new WeatherSearchQuery(preferences.getString("city"," "),
                WeatherSearchQuery.WEATHER_TYPE_LIVE);

        WeatherSearch mWeatherSearch=new WeatherSearch(MainActivity.this);
        mWeatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
                LocalWeatherLive liveResult=localWeatherLiveResult.getLiveResult();
                String[] weather=new String[2];
                weather[0]=liveResult.getWeather();
                weather[1]=liveResult.getTemperature();


                Log.d("FORCHECK", "onWeatherLiveSearched: "+weather[0]+"   "+weather[1]);

                TimeManager handler=new TimeManager();
                Message message=new Message();
                message.what=WRITE_WEATHER;
                message.obj=weather;
                handler.sendMessage(message);
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

            }
        });


        mWeatherSearch.setQuery(mQuery);
        mWeatherSearch.searchWeatherAsyn();



        mQuery=new WeatherSearchQuery(preferences.getString("city"," "),
                WeatherSearchQuery.WEATHER_TYPE_FORECAST);
        WeatherSearch weatherSearchForForecast=new WeatherSearch(MainActivity.this);
        weatherSearchForForecast.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {

            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                LocalWeatherForecast forecastResult=localWeatherForecastResult.getForecastResult();
                List<LocalDayWeatherForecast> listOfWeatherForecast=forecastResult.getWeatherForecast();
                LocalDayWeatherForecast forecast=listOfWeatherForecast.get(0);
                ArrayList<String> weatherData=new ArrayList<>();
                weatherData.add(forecast.getDayWeather());
                weatherData.add(forecast.getNightWeather());
                weatherData.add(forecast.getDayTemp());
                weatherData.add(forecast.getNightTemp());

                Log.d("FORCHECK", "onWeatherForecast: "+forecast.getDayWeather()+"   "+forecast.getDayTemp());

                TimeManager handler=new TimeManager();
                Message message=new Message();
                message.what=WRITE_WEATHER_FORECAST;
                message.obj=weatherData.toArray(new String[0]);
                handler.sendMessage(message);
            }
        });

        weatherSearchForForecast.setQuery(mQuery);
        weatherSearchForForecast.searchWeatherAsyn();
    }

    public void getLocation()
    {
        AMapLocationClient mLocationClient=new AMapLocationClient(MainActivity.this);
        AMapLocationClientOption mLocationOption=new AMapLocationClientOption();
        AMapLocationListener mLocationListener=new MyAMapLocationListener();

        mLocationClient.setLocationListener(mLocationListener);

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setMockEnable(false);
        mLocationOption.setLocationCacheEnable(false);

        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.stopLocation();
        mLocationClient.startLocation();


    }

    public class MyAMapLocationListener implements AMapLocationListener
    {
        public String province;
        public String city;
        public String district;

        public TimeManager handler=new TimeManager();



        public void onLocationChanged(AMapLocation aMapLocation){
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //此处获得成功，可以参照返回值表取需要的参数，我只要了省市县
                    Log.e("位置：", aMapLocation.getAddress());
                    province = aMapLocation.getProvince();
                    city = aMapLocation.getCity();
                    district = aMapLocation.getDistrict();

                    Message message=new Message();
                    String[] obj=new String[3];
                    obj[0]=province;
                    obj[1]=city;
                    obj[2]=district;

                    Log.d("FORCHECK", "onLocationChanged: "+province+"   "+city+"   "+district);

                    message.obj=obj;
                    message.what=WRITE_LOCATION;
                    handler.sendMessage(message);

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }


    /**
     * 动态获取网络、定位等权限
     */
    public void getPermission()
    {
        //获取网络权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.INTERNET)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},1);
        }

        //获取网络状态
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_NETWORK_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
        }

        //获取WIFI网络信息
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_WIFI_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_WIFI_STATE},1);
        }

        //获取粗略位置
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        //获取位置
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        //获取A-GPS位置
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_LOCATION_EXTRA_COMMANDS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS},1);
        }

        //获取日历权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_CALENDAR)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_CALENDAR},1);
        }

        //获取存储权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

        //获取电话权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }





    }
}
