package com.example.todolist;

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

}
