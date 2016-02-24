package com.dzy.eventbustest;

import android.util.Log;

import com.dzy.eventsbus.Observers;

/**
 *
 * Created by dzysg on 2016/2/24 0024.
 */
public class EventTestObject
{

    @Observers
    public void onRecive(BaseEvent e)
    {
        Log.d("tag",e.msg);
    }
}
