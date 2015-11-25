package com.dzy.eventbustest;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by dzysg on 2015/10/26 0026.
 */
public class app extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        LeakCanary.install(this);
    }
}
