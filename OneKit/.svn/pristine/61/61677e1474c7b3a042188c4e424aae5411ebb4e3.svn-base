package com.cn21.onekit.lib.update;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * Created by xuxd on 2017/4/5.
 */
public class BaseManager {

    public Handler mHandler = new Handler(Looper.getMainLooper());
    private static Executor mTaskExcutor  = Executors.newFixedThreadPool(3);


    public interface ResponseListener<T>{
         void onCallBack(T t);
    }


    public <T> void postData(final T t ,final ResponseListener listener){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if(listener != null){
                    listener.onCallBack(t);
                }
            }
        };
        mHandler.post(task);
    }


    public static void execute(Runnable r){
        mTaskExcutor.execute(r);
    }
}
