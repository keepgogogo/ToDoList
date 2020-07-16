package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class PlanAddActivity extends AppCompatActivity implements View.OnClickListener{

    final private int RESET_DATE_TextView=1;
    final private int RESET_TIME_TextView=2;

    Calendar calendar=Calendar.getInstance();
    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH)+1;
    int day=calendar.get(Calendar.DAY_OF_MONTH);

    private SharedPreferences.Editor spEditor=null;
    private PlanAddActivityHandler planAddActivityHandler=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);
        planAddActivityHandler=new PlanAddActivityHandler();
        setTitle("添加新日程");

        final TextView textViewOfDateSelection=(TextView)findViewById(R.id.TextViewForDateSetIn_PlanAddActivity);
        final Button buttonOfSetDate=(Button)findViewById(R.id.ButtonForSetDate_PlanAddActivity);
        buttonOfSetDate.setOnClickListener(this);
        final Button buttonOfSetTime=(Button)findViewById(R.id.ButtonForSetTime_PlanAddActivity);
        buttonOfSetTime.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("TempFile", MODE_PRIVATE);
        spEditor=sharedPreferences.edit();
        spEditor.putBoolean("importance",false);
        spEditor.apply();


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
            case R.id.ButtonForSetImportanceIn_PlanAddActivity:
                AlertDialog.Builder dialog=new AlertDialog.Builder(PlanAddActivity.this);
                dialog.setTitle("设为重要事项");
                dialog.setMessage("是否将本事项设为重要事项？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PlanAddActivity.this,"已将本事项设为重要事项",
                                Toast.LENGTH_SHORT).show();
                        spEditor.putBoolean("importance",true);
                        spEditor.apply();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(PlanAddActivity.this,"已将本事项设为非重要事项",
                                Toast.LENGTH_SHORT).show();
                        spEditor.putBoolean("importance",false);
                        spEditor.apply();
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    protected class PlanAddActivityHandler extends Handler
    {
        final private int RESET_DATE_TextView=1;
        final private int RESET_TIME_TextView=2;
        @Override
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case RESET_DATE_TextView:
                    TextView textViewOfDateSelection=(TextView)findViewById(R.id.TextViewForDateSetIn_PlanAddActivity);
                    textViewOfDateSelection.setText((String)message.obj);
                    break;
                case RESET_TIME_TextView:
                    TextView textViewOfTimeSelection=(TextView)findViewById(R.id.TextViewForTimeSetIn_PlanAddActivity);
                    textViewOfTimeSelection.setText((String)message.obj);
                    break;
                default:
                    break;
            }
        }
    }
}