package com.dzy.eventsbus;


import java.lang.reflect.Method;

/**
 * Created by dzysg on 2015/10/25 0025.
 */
public class PostRequest
{
    public Subscriber mSubscriber;
    public Object mParams;
    public int ThreadMode;
    public Method mMethod;
    public PostRequest(Subscriber subscriber, Object params)
    {
        mSubscriber = subscriber;
        mParams = params;
        ThreadMode = subscriber.ThreadMode;
        mMethod = subscriber.mMethod;
    }
}
