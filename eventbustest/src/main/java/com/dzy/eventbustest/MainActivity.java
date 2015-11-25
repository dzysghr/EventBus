package com.dzy.eventbustest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dzy.eventsbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/* bug
 * it's not available if you post int type beacause the number is considered as integer
 */
public class MainActivity extends AppCompatActivity
{
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);


    }

    public void onClick(View v)
    {

        long start = System.currentTimeMillis();
        List<testobject> list = new ArrayList<>();
        for (int i=0;i<100;i++)
        {
            testobject ob = new testobject();
            EventBus.getInstant().registerOnEvent(ob,0);
            list.add(ob);
        }
        long end = System.currentTimeMillis();

        Log.i("tag","register time"+(end-start));
    }


    public void onPost(View v)
    {
        long start = System.currentTimeMillis();
        EventBus.getInstant().post("this is a msg");

        long end = System.currentTimeMillis();

        Log.i("post", "post time" + (end - start));
    }

    public void onShow(View v)
    {
        EventBus.getInstant().showinfo();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
