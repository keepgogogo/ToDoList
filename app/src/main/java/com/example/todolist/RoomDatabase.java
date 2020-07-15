package com.example.todolist;

import androidx.room.Database;

@Database(entities = {PlanElements.class},version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
    public abstract PlanElementsDao planElementsDao();
}
