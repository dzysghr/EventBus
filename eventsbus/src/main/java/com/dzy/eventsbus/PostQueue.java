package com.dzy.eventsbus;

import java.util.concurrent.LinkedBlockingDeque;

public class PostQueue
{
	private LinkedBlockingDeque<PostRequest> mQueue;

    private PostProcessThread mProcessThread;


	public PostQueue()
	{
		mQueue = new LinkedBlockingDeque<PostRequest>();
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
