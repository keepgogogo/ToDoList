package com.example.todolist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Dust
 *
 * A schedule set by user is named a PlanElement
 * Inside the PlanElement are
 * int date : the date of the schedule,with the formation 20200709
 *  time : the concrete time of schedule with int hour and int mintue
 */

@Entity(tableName = "PlanElements")
public class PlanElements{
    @PrimaryKey(autoGenerate = true)
    public int planId;

    @ColumnInfo(name = "date_days")
    public int date_days;

    @ColumnInfo(name = "date_year")
    public int year;

    @ColumnInfo(name = "date_month")
    public int month;

    @ColumnInfo(name = "date_hours")
    public int hour;

    @ColumnInfo(name = "date_minutes")
    public int minute;

    @ColumnInfo(name = "date_all_in")
    public int date_all_in;

    @ColumnInfo(name = "plan")
    public String plan;

    @ColumnInfo(name = "importance")
    public int importance;

}
