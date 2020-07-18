package com.example.todolist;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListOfPlanElementsOperator {
    List<PlanElements> elementsList;
    SharedPreferences preferences;

    public void setElementsList(List<PlanElements> elementsList){this.elementsList=elementsList;}

    public ListOfPlanElementsOperator(List<PlanElements> elementsList){this.elementsList=elementsList;}

    public ListOfPlanElementsOperator(){};

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public List<PlanElements> rangeWithoutOldPlan()
    {
        int today=preferences.getInt("DETAIL_DATE",0);
        ArrayList<PlanElements> elementsArrayList=new ArrayList<PlanElements>();
        for(int i=0;i<elementsList.size();i++)
        {
            if(elementsList.get(i).date_all_in>=today) {
                elementsArrayList.add(elementsList.get(i));
            }
        }
        return (List<PlanElements>)elementsArrayList;
    }

    public List<PlanElements> rangeOnlyOldPlan()
    {
        int today=preferences.getInt("DETAIL_DATE",0);
        ArrayList<PlanElements> elementsArrayList=new ArrayList<PlanElements>();
        for(int i=0;i<elementsList.size();i++)
        {
            if(elementsList.get(i).date_all_in<today) {
                elementsArrayList.add(elementsList.get(i));
            }
        }
        return (List<PlanElements>)elementsArrayList;
    }

    public List<PlanElements> rangeWithoutPlan()
    {
        return (List<PlanElements>)(new ArrayList<PlanElements>());
    }

}

