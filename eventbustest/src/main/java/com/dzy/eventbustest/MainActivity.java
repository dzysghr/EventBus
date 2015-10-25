package com.dzy.eventbustest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dzy.eventsbus.EventBus;
import com.dzy.eventsbus.Observers;
import com.dzy.eventsbus.ThreadMode;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getInstant().registerByAnnotation(this);

    }

    public void onClick(View v)
    {
       EventBus.getInstant().post("this is a msg");
    }

    @Observers(ThreadMode.MainThread)
    public void onevent(String msg)
    {
        Log.i("tag","thread   "+Thread.currentThread().getName());
    }

}
