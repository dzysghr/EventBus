package com.dzy.eventsbus;


public class Student
{
	private int age;
	private String name;
	
	public Student(String n)
	{
		name = n;
	}
	public Student()
	{

	}

	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return name;
	}
	
	@Observers(ThreadMode.PostThread)
	public void onEvent(String msg)
	{
		System.out.println(name+"     "+msg);
		System.out.println("event thread "+Thread.currentThread().getName());
	}
	
	public String getName()
	{
		return name;
	}



	private void setName(String name)
	{
		this.name = name;
	}



	private void setAge(int a)
	{
		age = a;
	}
	public int getAge()
	{
		return age;
	}
}
