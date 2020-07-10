package com.example.todolist;

import androidx.room.Database;

@Database(entities = {PlanElements.class},version = 1)
public abstract class RoomDatabase {
    public abstract PlanElementsDao planElementsDao();
}
