package com.dzy.eventsbus;

import java.lang.reflect.Method;

public class Subscriber
{
	public Object mObject;
	public int priority;
	public Method mMethod;
	public int ThreadMode = 1;
	
	public Subscriber(Object o)
	{
		mObject = o;
	}
	
	public Subscriber(Object o, int p)
	{
		mObject = o;
		priority = p;
	}



    public Subscriber Clone(Object object,int p)
    {
        Subscriber sb = new Subscriber(object,p);
        sb.mMethod = this.mMethod;
        sb.ThreadMode = this.ThreadMode;
        return sb;

    }

    @Override
	public boolean equals(Object obj)
	{
		return mObject.equals(obj);
	}

	@Override
	public int hashCode()
	{
		return super.hashCode()+mObject.hashCode();
	}
}
