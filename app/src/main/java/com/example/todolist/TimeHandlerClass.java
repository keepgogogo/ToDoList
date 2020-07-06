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
    final static int updateDate=1;

    /**
     * have a threadpool built
     */
    private ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("poolForTime_%d").build();

    /**
     * initialization of parameters of ThreadPoolExecutor
     */
    private final static int corePoolSize=1;
    private final static int maximumPoolSize=1;
    private final static long keepAliveTime=0L;
    private final static int capacityOfBlockingQueue=128;

    /**
     * have a single thread built based on a threadpool
     */
    ExecutorService thread=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,
            TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(capacityOfBlockingQueue),
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
                dateForReturn[1]=calendar.get(Calendar.MONTH)+1;
                dateForReturn[2]=calendar.get(Calendar.DAY_OF_MONTH);

                Message message=new Message();
                message.what=updateDate;
                message.obj=(Object)dateForReturn;
                handler.sendMessage(message);
                String TAG="TimeHandlerClass";
                Log.d(TAG, "run:message has been sent ");
            }
        });

    }
}
