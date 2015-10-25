package com.dzy.eventsbus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostProcessThread extends Thread
{
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private Executor mExecutor = Executors.newFixedThreadPool(3);

    ArrayBlockingQueue<PostRequest> mQueue;
    private boolean mQuit = false;

    public PostProcessThread(ArrayBlockingQueue<PostRequest> queue)
    {
        mQueue = queue;
    }

    @Override
    public void run()
    {
        PostRequest request;
        for (; ; ) {
            request = null;
            try {
                request = mQueue.take();
            }
            catch (Exception e) {
                if (mQuit)
                    return;
                continue;
            }
            if (mQuit)
                return;
            if (request.ThreadMode==ThreadMode.MainThread)
                mMainHandler.post(new InvokeRunable(request));
            else if (request.ThreadMode==ThreadMode.BackgroudThread)
                mExecutor.execute(new InvokeRunable(request));
            else
                Log.e("EventBus","illegal thread mode");

        }
    }

    public void quit()
    {
        mQuit = true;
        interrupt();
    }

    public final static class InvokeRunable implements Runnable
    {

        PostRequest mPostRequest;

        public InvokeRunable(PostRequest request)
        {
            mPostRequest = request;
        }

        @Override
        public void run()
        {
            try {
                mPostRequest.mMethod.invoke(mPostRequest.mSubsciber.mObject, mPostRequest.mParams);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}