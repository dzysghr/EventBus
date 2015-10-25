package com.dzy.eventsbus;

import java.lang.reflect.Method;

public class Subsciber
{
	public Object mObject;
	public int priority;
	public Method mMethod;
	public int ThreadMode = 1;
	
	
	
	public Subsciber(Object o)
	{
		mObject = o;
	}
	
	public Subsciber(Object o,int p)
	{
		mObject = o;
		priority = p;
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
