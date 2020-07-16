package com.example.todolist.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddNewPlanFragment extends Fragment {

    Calendar calendar=Calendar.getInstance();
    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH)+1;
    int day=calendar.get(Calendar.DAY_OF_MONTH);
    DatePicker datePicker;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                month++;

            }
        });



        return inflater.inflate(R.layout.add_plan_fragment,container,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Helper helper=new Helper();
        //datePicker=helper.getDatePicker(R.id.DatePicker);
    }

    public static class Helper extends AppCompatActivity
    {
        DatePicker datePicker;

        public DatePicker getDatePicker(int idOfLayout)
        {
            return (DatePicker)findViewById(idOfLayout);
        }


    }

}
