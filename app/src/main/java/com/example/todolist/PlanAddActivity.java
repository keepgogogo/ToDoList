package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

public class PlanAddActivity extends AppCompatActivity implements View.OnClickListener{

    final private int RESET_DATE_TextView=1;
    final private int RESET_TIME_TextView=2;
    final private int RESET_IMPORTANCE_TextView=3;
    final private int SAVE_THE_PLAN=4;
    final private int SAVE_PLAN_COMPLETE=5;
    final private int SET_ALARM_SUCCESS=6;

    Calendar calendar=Calendar.getInstance();
    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH)+1;
    int day=calendar.get(Calendar.DAY_OF_MONTH);

    private SharedPreferences.Editor spEditor=null;
    private PlanAddActivityHandler planAddActivityHandler=null;
    private EditText editText;
    private ProgressBar progressBar;
    private TextView textViewOfIfImportance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);
        planAddActivityHandler=new PlanAddActivityHandler();
        setTitle("添加新日程");

        final Button buttonOfSetDate=findViewById(R.id.ButtonForSetDate_PlanAddActivity);
        buttonOfSetDate.setOnClickListener(this);
        final Button buttonOfSetTime=findViewById(R.id.ButtonForSetTime_PlanAddActivity);
        buttonOfSetTime.setOnClickListener(this);
        final Button buttonOfSetImportance=findViewById(R.id.ButtonForSetImportanceIn_PlanAddActivity);
        buttonOfSetImportance.setOnClickListener(this);
        final Button buttonOfTempSavingThePlan=findViewById(R.id.ButtonForTempSaveDetailOfThePlanIn_PlanAddActivity);
        buttonOfTempSavingThePlan.setOnClickListener(this);
        final Button buttonOfSaveThePlan=findViewById(R.id.ButtonForSaveTheWholePlanIn_PlanAddActivity);
        buttonOfSaveThePlan.setOnClickListener(this);

        final Switch switchForAlarm=findViewById(R.id.SwitchForAlarm);


        editText=findViewById(R.id.EditTextForDetailOfThePlanIn_PlanAddActivity);
        textViewOfIfImportance=findViewById(R.id.TextViewForImportanceSetIn_PlanAddActivity);
        progressBar=findViewById(R.id.progress_Bar);
        progressBar.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getSharedPreferences("TempFile", MODE_PRIVATE);
        spEditor=sharedPreferences.edit();
        spEditor.clear();
        spEditor.putInt("importance",0);
        spEditor.putBoolean("alarm",false);
        spEditor.apply();

        switchForAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        spEditor.putBoolean("alarm",true);
                    }
                    else
                    {
                        spEditor.putBoolean("alarm",false);
                    }
            }
        });
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ButtonForSetDate_PlanAddActivity:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        spEditor.putInt("year",year);
                        spEditor.putInt("month",month);
                        spEditor.putInt("day",day);
                        spEditor.apply();
                        String forTestPrint=year+"/"+month+"/"+day;
                        Toast.makeText(PlanAddActivity.this,forTestPrint,Toast.LENGTH_SHORT).show();

                        Message message=new Message();
                        message.what=RESET_DATE_TextView;
                        message.obj=forTestPrint;
                        planAddActivityHandler.sendMessage(message);

                    }
                },year,month-1,day).show();
                break;


            case R.id.ButtonForSetTime_PlanAddActivity:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String forTestPrint=hour+":"+minute;
                        Toast.makeText(PlanAddActivity.this,forTestPrint,Toast.LENGTH_SHORT).show();
                        spEditor.putInt("hour",hour);
                        spEditor.putInt("minute",minute);
                        spEditor.apply();

                        Message message=new Message();
                        message.what=RESET_TIME_TextView;
                        message.obj=forTestPrint;
                        planAddActivityHandler.sendMessage(message);

                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
                break;


            case R.id.ButtonForSetImportanceIn_PlanAddActivity:
                if("否".equals(textViewOfIfImportance.getText()))
                {
                    Toast.makeText(PlanAddActivity.this,"已将本事项设为重要事项",
                            Toast.LENGTH_SHORT).show();
                    spEditor.putInt("importance",1);
                }
                else
                {
                    Toast.makeText(PlanAddActivity.this,"已将本事项设为非重要事项",
                            Toast.LENGTH_SHORT).show();
                    spEditor.putInt("importance",0);
                }
                spEditor.apply();
                Message message=new Message();
                message.what=RESET_IMPORTANCE_TextView;
                message.obj=getSharedPreferences("TempFile", MODE_PRIVATE).getInt("importance",-1);
                planAddActivityHandler.sendMessage(message);
                break;

            case R.id.ButtonForTempSaveDetailOfThePlanIn_PlanAddActivity:
                String inputText=editText.getText().toString();
                spEditor.putString("plan",inputText);
                spEditor.apply();
                Toast.makeText(PlanAddActivity.this,"输入成功",Toast.LENGTH_SHORT).show();
                break;



            case R.id.ButtonForSaveTheWholePlanIn_PlanAddActivity:
                PlanElements planElements=new PlanElements();
                SharedPreferences sharedPreferences=getSharedPreferences("TempFile",MODE_PRIVATE);
                if(sharedPreferences.getString("plan","null").equals("null"))
                {
                    Toast.makeText(PlanAddActivity.this,"日程内容不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                planElements.date_days=sharedPreferences.getInt("day",-1);
                planElements.hour=sharedPreferences.getInt("hour",-1);
                planElements.minute=sharedPreferences.getInt("minute",-1);
                planElements.month=sharedPreferences.getInt("month",-1);
                planElements.year=sharedPreferences.getInt("year",-1);
                if (planElements.year==-1||planElements.month==-1||planElements.minute==-1
                        ||planElements.hour==-1||planElements.date_days==-1)
                {
                    Toast.makeText(PlanAddActivity.this,"请正确设置时间",Toast.LENGTH_SHORT).show();
                    break;
                }

                planElements.importance=sharedPreferences.getInt("importance",-1);
                planElements.plan=sharedPreferences.getString("plan","null");

                Message planMessage=new Message();

                planMessage.what=SAVE_THE_PLAN;
                planAddActivityHandler.sendMessage(planMessage);

                planElements.date_all_in=getDetailDate(planElements);

                RoomDatabase roomDatabase= Room.databaseBuilder(getApplicationContext(),
                        RoomDatabase.class,"database").build();
                ThreadHelperClass threadHelper=new ThreadHelperClass();
                threadHelper.insertPlan(planAddActivityHandler,planElements,roomDatabase,SAVE_PLAN_COMPLETE);

//                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
//                planElementsDao.insert(planElements);
//
//                planMessage.what=SAVE_PLAN_COMPLETE;
//                planAddActivityHandler.sendMessage(planMessage);

                if(sharedPreferences.getBoolean("alarm",false))
                {
                    try {
                        threadHelper.setAlarm(planElements,planAddActivityHandler,SET_ALARM_SUCCESS,this);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }




                Toast.makeText(PlanAddActivity.this,"新日程添加成功",Toast.LENGTH_SHORT).show();
                finish();
                break;


            default:
                break;
        }


    }

    private int getDetailDate(PlanElements planElements)
    {
        int backData=planElements.year;
        backData=backData*100+planElements.month;
        backData=backData*100+planElements.date_days;

        return backData;
    }

    protected class PlanAddActivityHandler extends Handler
    {

        @Override
        public void handleMessage(Message message)
        {

            switch (message.what)
            {
                case RESET_DATE_TextView:
                    TextView textViewOfDateSelection=findViewById(R.id.TextViewForDateSetIn_PlanAddActivity);
                    textViewOfDateSelection.setText((String)message.obj);
                    break;
                case RESET_TIME_TextView:
                    TextView textViewOfTimeSelection=findViewById(R.id.TextViewForTimeSetIn_PlanAddActivity);
                    textViewOfTimeSelection.setText((String)message.obj);
                    break;
                case RESET_IMPORTANCE_TextView:
                    TextView textViewOfImportanceSelection=findViewById(R.id.TextViewForImportanceSetIn_PlanAddActivity);
                    String output="否";
                    if((int)message.obj==1)output="是";
                    textViewOfImportanceSelection.setText(output);
                    break;
                case SAVE_THE_PLAN:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SAVE_PLAN_COMPLETE:
                    progressBar.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }
}