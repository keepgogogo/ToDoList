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
public class PlanElements {
    @PrimaryKey(autoGenerate = true)
    int planId;

    @ColumnInfo(name = "date_days")
    int date_days;

    @ColumnInfo(name = "date_year")
    int year;

    @ColumnInfo(name = "date_month")
    int month;

    @ColumnInfo(name = "date_hours")
    int hour;

    @ColumnInfo(name = "date_minutes")
    int minute;

    @ColumnInfo(name = "plan")
    String plan;

    @ColumnInfo(name = "importance")
    int importance;

    public int getPlanId() {
        return planId;
    }

    public int getDate_days() {
        return date_days;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getPlan() {
        return plan;
    }

    public int getImportance() {
        return importance;
    }

    //    @ColumnInfo(name = "")

}
