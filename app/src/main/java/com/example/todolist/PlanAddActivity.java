package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PlanAddActivity extends AppCompatActivity {

    Calendar calendar=Calendar.getInstance();
    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH)+1;
    int day=calendar.get(Calendar.DAY_OF_MONTH);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_add);
        //DatePicker datePicker=(DatePicker)findViewById(R.id.DatePicker);
        TextView textView=(TextView)findViewById(R.id.success);

        final SharedPreferences sharedPreferences=getSharedPreferences("TempFile",MODE_PRIVATE);
        final SharedPreferences.Editor spEditor=sharedPreferences.edit();

        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                spEditor.putInt("year",year);
                spEditor.putInt("month",month);
                spEditor.putInt("day",day);
                spEditor.apply();
                Toast.makeText(PlanAddActivity.this,year+"/"+month+"/"+day,Toast.LENGTH_SHORT).show();
            }
        },year,month-1,day).show();

    }
}