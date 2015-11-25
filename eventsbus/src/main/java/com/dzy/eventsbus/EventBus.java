package com.dzy.eventsbus;

import android.support.annotation.IntRange;
import android.util.Log;
import android.util.LruCache;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventBus
{
	//eventtype - subsriber
	private Map<Class<?>, List<Subscriber>> eventTypeMap = new ConcurrentHashMap<>();

	//object - eventtype
	private Map<Object,List<Class<?>>> mObserversMap = new ConcurrentHashMap<>();

	private Set<Object> mObservers = new CopyOnWriteArraySet<>(); //prevent to register twice times;

	private LruCache<Class<?>,Subscriber> mSbCache = new LruCache<>(10);

	private PostQueue mQueue;

	private static EventBus mEventBus = new EventBus();

    public EventBus()
    {
        mQueue = new PostQueue();
        mQueue.start();
    }

	public void showinfo()
    {
        Log.i("tag","eventtypemap  "+eventTypeMap.size());
        Log.i("tag", "mobservermap  " + mObserversMap.size());

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
			List<Subscriber> list = eventTypeMap.get(type);
			int len  = list.size();
			for (int i = 0; i < len; i++)
			{
				if (list.get(i).equals(object))
				{
					list.remove(i);
                    Log.i("EventBus","remove");
					break;
				}
			}
		}
		mObservers.remove(object);
	}


	public void register(Object object)
	{
		register(object, 0);
	}

    public synchronized void registerOnEvent(Object ob)
    {
        this.registerOnEvent(ob,0);
    }

    public synchronized  void registerOnEvent(Object object,@IntRange(from = 0,to = 100) int priority)
    {
        if (mObservers.contains(object))
            return;
        Class<?> type = object.getClass();
        Method[] methods = type.getDeclaredMethods();
        List<Class<?>> typelist = null;
        int DefaultMode = 1; //post thread

        try
        {
            for (int i = 0; i < methods.length; i++)
            {
                Method method = methods[i];
                String name = method.getName();

                if (name.startsWith("on")&&name.endsWith("Event"))
                {
                    // check the modifiers
                    if (!Modifier.isPublic(method.getModifiers()))																// public
                        throw new Exception("method is not public");
                    if (Modifier.isAbstract(method.getModifiers())||Modifier.isStatic(method.getModifiers()))
                        throw new Exception("method is not abstract or static");


                    if (name.equals("onMainEvent"))
                        DefaultMode = 2;
                    else if (name.equals("onBackgroundEvent"))
                        DefaultMode = 3;

                    Class<?>[] parasTypes = method.getParameterTypes();
                    // the method only support 1 paramter;
                    if (parasTypes.length != 1)
                    {
                        throw new Exception("the method can only have one parameter");
                    }
                    Class<?> parastype = parasTypes[0];

                    if (typelist == null)
                    {
                        typelist = new CopyOnWriteArrayList<>();
                    }
                    typelist.add(parastype);

                    List<Subscriber> observerList = eventTypeMap.get(parastype);

                    if (observerList == null)
                    {
                        observerList = new CopyOnWriteArrayList<>();
                    }

                    Subscriber sb = new Subscriber(object,priority);
                    sb.mMethod = method;
                    sb.ThreadMode = DefaultMode;

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
                    mObserversMap.put(object,typelist);


                    mObservers.add(object);

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


	public synchronized void register(Object object,@IntRange(from = 0,to = 100)  int priority)
	{

		if (mObservers.contains(object))
			return;
		Class<?> type = object.getClass();
		Method[] methods = type.getDeclaredMethods();
		List<Class<?>> typelist = null;
		int DefaultMode = 1;

		try
		{
			for (int i = 0; i < methods.length; i++)
			{
				Method method = methods[i];
				Observers observers = method.getAnnotation(Observers.class);
				if (observers != null)
				{
					// if it is not public
					if (!Modifier.isPublic(method.getModifiers()))																// public
						throw new Exception("method is not public");

					DefaultMode = observers.value();

					Class<?>[] parasTypes = method.getParameterTypes();
					// the method only support 1 paramter;
					if (parasTypes.length != 1)
					{
						throw new Exception("the method can only have one parameter");
					}
					Class<?> parastype = parasTypes[0];

					if (typelist == null)
					{
						typelist = new CopyOnWriteArrayList<>();
					}
					typelist.add(parastype);

					List<Subscriber> observerList = eventTypeMap.get(parastype);

					if (observerList == null)
					{
						observerList = new CopyOnWriteArrayList<>();
					}

					Subscriber sb = new Subscriber(object, priority);
					sb.mMethod = method;
					sb.ThreadMode = DefaultMode;

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
					mObserversMap.put(object,typelist);


					mObservers.add(object);

				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void post(Object param)
	{
		List<Subscriber> queue = eventTypeMap.get(param.getClass());
		if (queue == null)
			return;
		try
		{
			for (Subscriber sb : queue)
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
