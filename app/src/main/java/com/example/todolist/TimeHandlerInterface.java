package com.example.todolist;

import android.os.Handler;

interface TimeHandlerInterface {
    /**
     * func:Get the date of today and write it into a SharedPreferences file named "NormalData"
     * @param handler used to send message to UI thread
     */
    void renewDateOfToday(MainActivity.TimeManager handler);

    /**
     * method for TimeHandler formation
     */
    void timeHandlerClass();
}
