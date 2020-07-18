package com.example.todolist;

import android.os.Message;
import android.util.Log;

import androidx.room.Room;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ThreadHelperClass implements ThreadHelperClassInterface {

    /**
     * message.what
     */
    private final static int UPDATE_DATE =1;

    /**
     * have a threadpool built
     */
    private ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("poolForTime_%d").build();

    /**
     * initialization of parameters of ThreadPoolExecutor
     */
    private final static int CORE_POOL_SIZE =1;
    private final static int MAXIMUM_POOL_SIZE =1;
    private final static long KEEP_ALIVE_TIME =0L;
    private final static int CAPACITY_OF_BLOCKING_QUEUE =128;

    /**
     * have a single thread built based on a threadpool
     */
    private ExecutorService thread=new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(CAPACITY_OF_BLOCKING_QUEUE),
            threadFactory,new ThreadPoolExecutor.AbortPolicy());


    @Override
    public void ThreadHelperClass(){}

    @Override
    public void renewDateOfToday(final MainActivity.TimeManager handler) {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                Calendar calendar=Calendar.getInstance();
                int[] dateForReturn=new int[3];

                dateForReturn[0]=calendar.get(Calendar.YEAR);
                dateForReturn[1]=calendar.get(Calendar.MONTH)+1;
                dateForReturn[2]=calendar.get(Calendar.DAY_OF_MONTH);
//                if(calendar.get(Calendar.MONTH)<9)dateForReturn[0]*=10;
//                dateForReturn[0]*=10;
//                dateForReturn[0]+=(calendar.get(Calendar.MONTH)+1);
//
//                if(calendar.get(Calendar.DAY_OF_MONTH)<10)dateForReturn[0]*=10;
//                dateForReturn[0]*=10;
//                dateForReturn[0]+=calendar.get(Calendar.DAY_OF_MONTH);
//
//                dateForReturn[1]=calendar.get(Calendar.HOUR_OF_DAY);
//                dateForReturn[2]=calendar.get(Calendar.MINUTE);

                Message message=new Message();
                message.what= UPDATE_DATE;
                message.obj=dateForReturn;
                handler.sendMessage(message);
                String tag ="ThreadHelperClass";
                Log.d(tag, "run:message has been sent ");
            }
        });
    }

    @Override
    public void insertPlan(final PlanAddActivity.PlanAddActivityHandler handler, final PlanElements planElements,
                           final RoomDatabase roomDatabase,final int handler_what)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
                planElementsDao.insert(planElements);

                Message message=new Message();
                message.what=handler_what;
                handler.sendMessage(message);
//                planMessage.what=SAVE_PLAN_COMPLETE;
//                planAddActivityHandler.sendMessage(planMessage);
            }
        });
    }

    @Override
    public void loadAllPlan(final MyAllPlanActivity.MyAllPlanActivityHandler handler, final RoomDatabase roomDatabase,
                            final int handler_what)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {

                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
                List<PlanElements> planElementsList=planElementsDao.getAll();

                Message message=new Message();
                message.obj=(Object)planElementsList;
                message.what=handler_what;
                handler.sendMessage(message);

            }
        });
    }



    public void deletePlan(final RoomDatabase roomDatabase,final MyAllPlanActivity.MyAllPlanActivityHandler handler,
                           final int handler_what,final PlanElements ... planElements)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
                planElementsDao.deleteByGroup(planElements);
                Message message=new Message();
                message.what=handler_what;

            }
        });
    }
}
