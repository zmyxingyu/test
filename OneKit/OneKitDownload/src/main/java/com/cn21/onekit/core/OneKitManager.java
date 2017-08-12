package com.cn21.onekit.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cn21.onekit.core.utils.OkEnvrionmentUtil;

import org.xwalk.core.OneKitEnvironment;
import org.xwalk.core.XWalkInitializer;
import org.xwalk.core.XWalkUpdater;

/**
 * Created by xuxd on 2017/7/11.
 */

public class OneKitManager {
    private static final String TAG = OneKitManager.class.getSimpleName();
    XWalkInitializer mXWalkInitializer;
    XWalkUpdater mXWalkUpdater;
    private Context mContext;

    private OneKitManager(){
    }

    private volatile static OneKitManager instance;

    public static OneKitManager getInstance(){
        if(instance == null){
            synchronized (OneKitManager.class){
                if(instance == null){
                    instance = new OneKitManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context){
        mContext = context;
        if (mXWalkInitializer == null) {
            mXWalkInitializer = new XWalkInitializer(WalkInitListener, context);
        }
        mXWalkInitializer.initAsync();
    }

    public boolean isXWalkReady(){
        if(mXWalkInitializer != null){
            return mXWalkInitializer.isXWalkReady();
        }
        return false;
    }

    public Handler mHandler = new Handler(Looper.getMainLooper());

    XWalkInitializer.XWalkInitListener WalkInitListener = new XWalkInitializer.XWalkInitListener(){
        @Override
        public void onXWalkInitStarted() {
            Log.i(TAG, "onXWalkInitStarted ");
        }

        @Override
        public void onXWalkInitCancelled() {
            Log.i(TAG, "onXWalkInitCancelled ");
        }

        @Override
        public void onXWalkInitFailed() {
            Log.i(TAG, "onXWalkInitFailed ");
            // Initialization failed. Trigger the Crosswalk runtime download
            if(isWifi()){ //wifi网络下才更新
                if (mXWalkUpdater == null) {
                    mXWalkUpdater = new XWalkUpdater(xWalkBackgroundUpdateListener, mContext);
                }

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = OneKitEnvironment.getXWalkApkUrl();
                        Log.i(TAG, "before :" + url);
                        url = OkEnvrionmentUtil.fixApkUrl(mContext, url);
                        Log.i(TAG, "after :" + url);
                        OneKitEnvironment.setXWalkApkUrl(url);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mXWalkUpdater.updateXWalkRuntime();
                            }
                        });
                    }
                });
                thread.start();
            }
        }

        @Override
        public void onXWalkInitCompleted() {
            Log.i(TAG, "onXWalkInitCompleted ");
        }
    };

    XWalkUpdater.XWalkBackgroundUpdateListener xWalkBackgroundUpdateListener = new XWalkUpdater.XWalkBackgroundUpdateListener(){

        @Override
        public void onXWalkUpdateStarted() {
            Log.i(TAG, "onXWalkUpdateStarted ");
        }

        @Override
        public void onXWalkUpdateProgress(int i) {
            Log.i(TAG, "onXWalkUpdateProgress   i ：" + i);
        }

        @Override
        public void onXWalkUpdateCancelled() {
            Log.i(TAG, "onXWalkUpdateCancelled ");
        }

        @Override
        public void onXWalkUpdateFailed() {
            Log.i(TAG, "onXWalkUpdateFailed ");
        }

        @Override
        public void onXWalkUpdateCompleted() {
            Log.i(TAG, "onXWalkUpdateCompleted ");
            mXWalkInitializer.initAsync();
        }
    };

    private boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
