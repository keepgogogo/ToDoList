package com.example.todolist;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanElementsDao {
    @Query("SELECT * FROM PlanElements")
    List<PlanElements> getAll();
}
