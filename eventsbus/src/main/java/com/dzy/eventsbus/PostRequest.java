package com.dzy.eventsbus;


import java.lang.reflect.Method;

/**
 * Created by dzysg on 2015/10/25 0025.
 */
public class PostRequest
{
    public Subsciber mSubsciber;
    public Object mParams;
    public int ThreadMode;
    public Method mMethod;
    public PostRequest(Subsciber subsciber, Object params)
    {
        mSubsciber = subsciber;
        mParams = params;
        ThreadMode = subsciber.ThreadMode;
        mMethod = subsciber.mMethod;
    }
}
