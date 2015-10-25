package com.dzy.eventsbus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus
{

	private Map<Class<?>, List<Subsciber>> eventTypeMap = new ConcurrentHashMap<Class<?>, List<Subsciber>>();

	private Map<Object, List<Class<?>>> mObserversMap = new ConcurrentHashMap<Object, List<Class<?>>>();

	private Set<Object> mObservers = new HashSet<Object>();

	private PostQueue mQueue;

	private static EventBus mEventBus = new EventBus();

    public EventBus()
    {
        mQueue = new PostQueue();
        mQueue.start();
    }

	public static EventBus getInstant()
	{
		return mEventBus;
	}

	public void unRegister(Object object)
	{
		List<Class<?>> types = mObserversMap.remove(object);
		if (types == null)
			return;
		for (Class<?> type : types)
		{
			List<Subsciber> list = eventTypeMap.get(type);
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).equals(object))
				{
					list.remove(i);
					break;
				}
			}
		}

	}

	public void register(Object o)
	{
		register(o, 0);
	}

	public synchronized void register(Object object, int priority)
	{

		Class<?> type = object.getClass();
		Method[] methods = type.getDeclaredMethods();
		List<Class<?>> typelist = null;
		try
		{
			for (int i = 0; i < methods.length; i++)
			{
				Method method = methods[i];
				if (method.getName().equals("onEvent"))
				{
					if (!Modifier.isPublic(method.getModifiers()))// if is not
																	// public
						throw new Exception("onEvent method is not public");

					Class<?>[] parasTypes = method.getParameterTypes();
					if (parasTypes.length != 1)// if is has 0 or more than 1 //
												// params;
					{
						throw new Exception("can not support the parameter");
					}
					Class<?> parastype = parasTypes[0];

					if (typelist == null)
					{
						typelist = new CopyOnWriteArrayList<Class<?>>();
					}
					typelist.add(parastype);

					List<Subsciber> observerList = eventTypeMap.get(parastype);

					if (observerList == null)
					{
						observerList = new CopyOnWriteArrayList<Subsciber>();
					}

					Subsciber sb = new Subsciber(object, priority);
					sb.mMethod = method;

					if (observerList.size() == 0)
						observerList.add(sb);
					else
                        // add to list in order by priority
						for (int j = 0; j < observerList.size(); j++)
						{
							if (observerList.get(j).priority >= sb.priority)
							{
								observerList.add(j, sb);
								break;
							}
						}

					eventTypeMap.put(parastype, observerList);
					mObserversMap.put(object, typelist);

				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void registerByAnnotation(Object object)
	{
		registerByAnnotation(object, 0);
	}

	public synchronized void registerByAnnotation(Object object, int priority)
	{
		Class<?> type = object.getClass();
		Method[] methods = type.getDeclaredMethods();
		List<Class<?>> typelist = null;
		int threadmode = 1;
		try
		{
			for (int i = 0; i < methods.length; i++)
			{
				Method method = methods[i];
				Observers observers = method.getAnnotation(Observers.class);
				if (observers != null)
				{
					// if is not public	
					if (!Modifier.isPublic(method.getModifiers()))																// public
						throw new Exception("onEvent method is not public");
					threadmode = observers.value();

					Class<?>[] parasTypes = method.getParameterTypes();
					// if is has 0 or more than 1 params;
					if (parasTypes.length != 1)
					{
						throw new Exception("can not support the parameter");
					}
					Class<?> parastype = parasTypes[0];

					if (typelist == null)
					{
						typelist = new CopyOnWriteArrayList<Class<?>>();
					}
					typelist.add(parastype);

					List<Subsciber> observerList = eventTypeMap.get(parastype);

					if (observerList == null)
					{
						observerList = new CopyOnWriteArrayList<Subsciber>();
					}

					Subsciber sb = new Subsciber(object, priority);
					sb.mMethod = method;
					sb.ThreadMode = threadmode;

					if (observerList.size() == 0)
						observerList.add(sb);
					else
						for (int j = 0; j < observerList.size(); j++)
						{
							//add to the list in order by priority
							if (observerList.get(j).priority >= sb.priority)
							{
								observerList.add(j, sb);
								break;
							}
						}

					eventTypeMap.put(parastype, observerList);
					mObserversMap.put(object, typelist);

				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void post(Object param)
	{
		List<Subsciber> queue = eventTypeMap.get(param.getClass());
		if (queue == null)
			return;
		try
		{
			for (Subsciber sb : queue)
			{
				if (sb.ThreadMode == ThreadMode.PostThread)
					sb.mMethod.invoke(sb.mObject, param);
				else
					mQueue.add(new PostRequest(sb,param));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
