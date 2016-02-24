package com.dzy.eventsbus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class PostProcessThread extends Thread
{
    final static private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private Executor mExecutor = Executors.newFixedThreadPool(ThreadPoorCount);

    public static final int ThreadPoorCount = 3;

    LinkedBlockingDeque<PostRequest> mQueue;
    private boolean mQuit = false;

    public PostProcessThread(LinkedBlockingDeque<PostRequest> queue)
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
                mPostRequest.mMethod.invoke(mPostRequest.mSubscriber.mObject, mPostRequest.mParams);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                mPostRequest = null;
            }
        }
    }
}