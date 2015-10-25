package com.dzy.eventsbus;

import java.util.concurrent.ArrayBlockingQueue;

public class PostQueue
{
	private ArrayBlockingQueue<PostRequest> mQueue;

    private PostProcessThread mProcessThread;



	public PostQueue()
	{
		mQueue = new ArrayBlockingQueue<PostRequest>(16);
	}


	public void add(PostRequest s)
	{
		mQueue.add(s);
	}

	
	public void start()
    {
        mProcessThread = new PostProcessThread(mQueue);
        mProcessThread.start();
    }
	
	public void stop()
    {
       mProcessThread.quit();
    }


}
