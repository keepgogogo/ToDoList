package com.example.todolist;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadHelperClass implements ThreadHelperClassInterface {

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

            }
        });
    }

    @Override
    public void insertPlan(final MyAllPlanActivity.MyAllPlanActivityHandler handler,
                           final RoomDatabase roomDatabase, final int handler_what, final PlanElements ... planElements)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
                planElementsDao.insert(planElements);

                Message message=new Message();
                message.what=handler_what;
                handler.sendMessage(message);

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

    @Override
    public void loadTodayPlan(final MyAllPlanActivity.MyAllPlanActivityHandler handler,
                              final RoomDatabase roomDatabase,
                              final int handler_what,
                              final int dateOfToday)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {

                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
                List<PlanElements> planElementsList=planElementsDao.loadByDateAllInToday(dateOfToday);

                Message message=new Message();
                message.obj=(Object)planElementsList;
                message.what=handler_what;
                handler.sendMessage(message);
            }
        });
    }



    public void deletePlan(final RoomDatabase roomDatabase,final PlanElements ... planElements)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                PlanElementsDao planElementsDao=roomDatabase.planElementsDao();
                planElementsDao.deleteByGroup(planElements);
            }
        });
    }

    public void updateListOfPlan(final MyAllPlanActivity.MyAllPlanActivityHandler handler)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                int updateListOfPlan=1;
                Message message=new Message();
                message.what=updateListOfPlan;
                handler.sendMessage(message);
            }
        });
    }

    public void setAlarm(PlanElements planElements,
                         PlanAddActivity.PlanAddActivityHandler handler,
                         int handler_what,
                         Context context) throws ParseException {
        CalendarReminderUtils reminderUtils=new CalendarReminderUtils();
        reminderUtils.addPlanInCalender(context, planElements);

        Message message=new Message();
        message.what=handler_what;
        handler.sendMessage(message);
    }
}
