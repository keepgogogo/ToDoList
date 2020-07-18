package com.example.todolist;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlanElementsDao {
    @Query("SELECT * FROM PlanElements")
    List<PlanElements> getAll();

    @Query("SELECT * FROM PlanElements WHERE planId IN (:planIds)")
    List<PlanElements> loadAllByPlanId(int[] planIds);

    @Query("SELECT * FROM PlanElements WHERE date_year BETWEEN :startOfTimeSelected AND :endOfTimeSelected")
    List<PlanElements> loadByPlanIdBetweenYear(int startOfTimeSelected,int endOfTimeSelected);

    @Query("SELECT * FROM PlanElements WHERE date_month BETWEEN :startOfTimeSelected AND :endOfTimeSelected")
    List<PlanElements> loadByPlanIdBetweenMonth(int startOfTimeSelected,int endOfTimeSelected);

    @Query("SELECT * FROM PlanElements WHERE date_days BETWEEN :startOfTimeSelected AND :endOfTimeSelected")
    List<PlanElements> loadByPlanIdBetweenDay(int startOfTimeSelected,int endOfTimeSelected);

    @Query("SELECT * FROM PlanElements WHERE date_all_in >= :today")
    List<PlanElements> loadByDateAllInTodayAndAfter(int today);

    @Query("SELECT * FROM PlanElements WHERE importance = 1")
    List<PlanElements> loadByImportance();

    @Insert
    void insert(PlanElements ... planElements);

    @Delete
    void delete(PlanElements planElements);

    @Delete
    void deleteByGroup(PlanElements ... planElements);

    @Query("DELETE FROM PlanElements")
    void deleteAll();
}
