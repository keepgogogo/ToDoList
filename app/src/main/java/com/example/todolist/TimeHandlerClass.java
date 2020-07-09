package com.example.todolist;

import android.os.Message;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class TimeHandlerClass implements TimeHandlerInterface {

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
    public void timeHandlerClass(){}

    @Override
    public void renewDateOfToday(final MainActivity.TimeManager handler) {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                Calendar calendar=Calendar.getInstance();
                int[] dateForReturn=new int[3];

                dateForReturn[0]=calendar.get(Calendar.YEAR);

                if(calendar.get(Calendar.MONTH)<9)dateForReturn[0]*=10;
                dateForReturn[0]*=10;
                dateForReturn[0]+=(calendar.get(Calendar.MONTH)+1);

                if(calendar.get(Calendar.DAY_OF_MONTH)<10)dateForReturn[0]*=10;
                dateForReturn[0]*=10;
                dateForReturn[0]+=calendar.get(Calendar.DAY_OF_MONTH);

                dateForReturn[1]=calendar.get(Calendar.HOUR_OF_DAY);
                dateForReturn[2]=calendar.get(Calendar.MINUTE);

                Message message=new Message();
                message.what= UPDATE_DATE;
                message.obj=dateForReturn;
                handler.sendMessage(message);
                String tag ="TimeHandlerClass";
                Log.d(tag, "run:message has been sent ");
            }
        });

    }
}
