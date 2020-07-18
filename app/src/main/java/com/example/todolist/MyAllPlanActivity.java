package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import com.example.todolist.recycler.PlanShowRecyclerAdapter;
import com.example.todolist.recycler.PlanShowRecyclerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class MyAllPlanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyAllPlanActivityHandler handler=new MyAllPlanActivityHandler();
    private PlanShowRecyclerViewModel viewModel;

    final int LOAD_ALL_PLAN_NO_LIMIT=0;
    final int UPDATE_ALL_PLAN=1;

    List<PlanElements> listOfAllPlanElement;
    ThreadHelperClass threadHelper=new ThreadHelperClass();;
    RoomDatabase roomDatabase;
    SharedPreferences preferences;
    ListOfPlanElementsOperator operator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_plan);
        setTitle("所有日程");
        preferences=getSharedPreferences("NormalData",MODE_PRIVATE);

        viewModel=new ViewModelProvider(this).get(PlanShowRecyclerViewModel.class);
        final Observer<List<PlanElements>> planElementsObserver= new Observer<List<PlanElements>>() {
            @Override
            public void onChanged(List<PlanElements> planElements) {
                PlanElements[] dataForAdapter=new PlanElements[planElements.size()];
                for(int i=0;i<planElements.size();i++)dataForAdapter[i]=planElements.get(i);
                adapter=new PlanShowRecyclerAdapter(dataForAdapter);
                recyclerView.setAdapter(adapter);
            }
        };
        viewModel.getCurrentData().observe(this,planElementsObserver);

        recyclerView=(RecyclerView)findViewById(R.id.RecyclerViewIn_MyAllPlanActivity);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        roomDatabase= Room.databaseBuilder(getApplicationContext(),
                RoomDatabase.class,"database").build();

        threadHelper.loadAllPlan(handler,roomDatabase,LOAD_ALL_PLAN_NO_LIMIT);

        operator=new ListOfPlanElementsOperator();
        handler.setOperator(operator);
    }

    //set the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_of_my_all_plan_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@Nonnull MenuItem item)
    {
        operator.setPreferences(preferences);
        switch (item.getItemId())
        {
            case R.id.show_all_plan_in_allPlanActivity:

                setView(listOfAllPlanElement);

                break;
            case R.id.no_old_plan_in_allPlanActivity:

                setView(operator.rangeWithoutOldPlan());

                break;
            case R.id.delete_old_plan_in_allPlanActivity:

                setView(operator.rangeWithoutOldPlan());
                List<PlanElements> elementsToBeDelete=operator.rangeOnlyOldPlan();
                PlanElements[] elementsTobeCancel= elementsToBeDelete.toArray(new PlanElements[0]);
                threadHelper.deletePlan(roomDatabase,handler,UPDATE_ALL_PLAN,elementsTobeCancel);
                threadHelper.loadAllPlan(handler,roomDatabase,LOAD_ALL_PLAN_NO_LIMIT);
                break;
            case R.id.show_old_plan_in_allPlanActivity:

                setView(operator.rangeOnlyOldPlan());

                break;
            case R.id.delete_all_plan_in_allPlanActivity:

                setView(operator.rangeWithoutPlan());

                PlanElements[] allPlanElement=new PlanElements[listOfAllPlanElement.size()];
                listOfAllPlanElement.toArray(allPlanElement);
                threadHelper.deletePlan(roomDatabase, handler, UPDATE_ALL_PLAN, allPlanElement);
                threadHelper.loadAllPlan(handler,roomDatabase,LOAD_ALL_PLAN_NO_LIMIT);
                break;
            default:
                break;
        }
        return true;
    }


    protected class MyAllPlanActivityHandler extends Handler
    {
        final int LOAD_ALL_PLAN_NO_LIMIT=0;
        final int UPDATE_ALL_PLAN=1;
        @Override
        public void handleMessage(@NotNull Message message)
        {
            switch (message.what)
            {
                case LOAD_ALL_PLAN_NO_LIMIT:
                    List<PlanElements> listOfData=(List<PlanElements>)message.obj;
//                    PlanElements[] dataForAdapter=new PlanElements[listOfData.size()];
//                    for(int i=0;i<listOfData.size();i++)dataForAdapter[i]=listOfData.get(i);
//                    adapter=new PlanShowRecyclerAdapter(dataForAdapter);
//                    recyclerView.setAdapter(adapter);
                    viewModel.getCurrentData().setValue(listOfData);

                    //仅在此处对 listOfAllPlanElement 修改
                    listOfAllPlanElement=listOfData;
                    operator.setElementsList(listOfAllPlanElement);
                    break;
                case UPDATE_ALL_PLAN:
                    listOfAllPlanElement=(List<PlanElements>)message.obj;
                    operator.setElementsList(listOfAllPlanElement);
                    break;
            }
        }

        ListOfPlanElementsOperator operator;

        public void setOperator(ListOfPlanElementsOperator operator){this.operator=operator;}
    }

    public void setView(List<PlanElements> list)
    {
        viewModel.getCurrentData().setValue(list);
    }
}