package com.dzy.eventbustest;

import android.util.Log;

import com.dzy.eventsbus.Observers;
import com.dzy.eventsbus.ThreadMode;

/**
 *
 * Created by dzysg on 2015/11/24 0024.
 */
public class testobject
{
    //在主线程调用
    public void onMainEvent(String msg)
    {
        Log.i("tag","Thread :"+Thread.currentThread().getId()+"  Content: "+msg);
    }

    //在后台线程调用
    public void onBackgroundEvent(String msg)
    {
        Log.i("tag","Thread :"+Thread.currentThread().getId()+"  Content: "+msg);
    }

    //在事件发布线程调用
    public void onPostEvent(String msg)
    {
        Log.i("tag","Thread :"+Thread.currentThread().getId()+"  Content :"+msg);
    }


    @Observers(ThreadMode.MainThread)
    public void onMainAnnotation(String msg)
    {
        Log.i("tag","Thread :"+Thread.currentThread().getId()+"  Content :"+msg);
    }

    @Observers(ThreadMode.BackgroudThread)
    public void onBackgroudAnnotation(String msg)
    {
        Log.i("tag", "Thread :" + Thread.currentThread().getId() + "  Content :" + msg);
    }

    @Observers(ThreadMode.PostThread)
    public void onPostAnnotation(String msg)
    {
        Log.i("tag","Thread :"+Thread.currentThread().getId()+"  Content :"+msg);
    }
}
