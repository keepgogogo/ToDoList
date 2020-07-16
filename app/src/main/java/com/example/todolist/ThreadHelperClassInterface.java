package com.example.todolist;

import android.os.Handler;

interface ThreadHelperClassInterface {
    /**
     * func:Get the date of today and write it into a SharedPreferences file named "NormalData"
     * @param handler used to send message to UI thread
     */
    void renewDateOfToday(MainActivity.TimeManager handler);

    /**
     * method for TimeHandler formation
     */
    void ThreadHelperClass();

    void insertPlan(final PlanAddActivity.PlanAddActivityHandler handler,PlanElements planElements,
                    RoomDatabase roomDatabase,int handler_what);

    void loadAllPlan(final PlanAddActivity.PlanAddActivityHandler handler,final RoomDatabase roomDatabase,
                     final int handler_what);
}
