package com.cn21.onekit.core;

import org.xwalk.core.OneKitEnvironment;
import org.xwalk.core.XWalkInitializer;
import org.xwalk.core.XWalkUpdater;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cn21.onekit.core.utils.OkEnvrionmentUtil;
import com.cn21.onekit.lib.BuildConfig;

/**
 * Created by xuxd on 2017/7/11.
 */

public class OneKitManager {
    private static final String TAG = OneKitConstants.ONEKIT_SDK_LOG_PREFIX + "OneKitManger";
    private XWalkInitializer mXWalkInitializer;
    private XWalkUpdater mXWalkUpdater;
    private Context mContext;
    private OneKitInitListener mListener;

    public boolean isXWalkStatus() {
        return XWalkStatus;
    }

    public void setXWalkStatus(boolean XWalkStatus) {
        this.XWalkStatus = XWalkStatus;
    }

    public CheckXWalkStatusListener getmCheckXWalkStatusListener() {
        return mCheckXWalkStatusListener;
    }

    public void setmCheckXWalkStatusListener(CheckXWalkStatusListener mCheckXWalkStatusListener) {
        this.mCheckXWalkStatusListener = mCheckXWalkStatusListener;
    }

    private CheckXWalkStatusListener mCheckXWalkStatusListener;

    private boolean XWalkStatus = true;//xwalkview开关

    private final OneKitConfig mConfig;
    private final OneKitRuntime mRuntime;

    private OneKitManager(OneKitConfig oneKitConfig, OneKitRuntime oneKitRuntime) {
        this.mConfig = oneKitConfig;
        this.mRuntime = oneKitRuntime;
    }

    public OneKitRuntime getOnekitRuntime() {
        return mRuntime;
    }

    public OneKitConfig getOneKitConfig() {
        return mConfig;
    }

    private volatile static OneKitManager instance;

    /**
     * 调用前必须确保已经创建过实例createInstance
     *
     * @return
     */
    public static synchronized OneKitManager getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("need to be createInstance(@NonNull OneKitRuntime runtime, @NonNull OneKitConfig config) before getInstance()");
        }
        return instance;
    }

    public static synchronized OneKitManager createInstance(@NonNull OneKitRuntime runtime, @NonNull OneKitConfig config) {
        if (null == instance) {
            instance = new OneKitManager(config, runtime);
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        if (mXWalkInitializer == null) {
            mXWalkInitializer = new XWalkInitializer(WalkInitListener, context);
        }
        mXWalkInitializer.initAsync();
    }

    public boolean isXWalkReady() {
        if (null != getmCheckXWalkStatusListener()) {
            setXWalkStatus(getmCheckXWalkStatusListener().onCheckStatus());
            Log.i(TAG, "set XWalkView status :" + isXWalkStatus());
        }
        if (!XWalkStatus) {
            Log.i(TAG, "XWalkView set enable");
            return false;
        }
        if (mXWalkInitializer != null) {
            return mXWalkInitializer.isXWalkReady();
        }
        return false;
    }


    public void setInitListener(OneKitInitListener listener) {
        mListener = listener;
    }

    public Handler mHandler = new Handler(Looper.getMainLooper());

    XWalkInitializer.XWalkInitListener WalkInitListener = new XWalkInitializer.XWalkInitListener() {
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
            if (mListener != null) mListener.onInitOneKitResult(false);
            // Initialization failed. Trigger the Crosswalk runtime download
            if (BuildConfig.DOWLOAD_MODE && isWifi()) { //wifi网络下才更新
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
            if (mListener != null) mListener.onInitOneKitResult(true);
        }
    };

    XWalkUpdater.XWalkBackgroundUpdateListener xWalkBackgroundUpdateListener = new XWalkUpdater.XWalkBackgroundUpdateListener() {

        @Override
        public void onXWalkUpdateStarted() {
            Log.i(TAG, "onXWalkUpdateStarted ");
            if (mListener != null) mListener.onXWalkUpdateStarted();
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
            if (mListener != null) mListener.onDownloadOneKitResult(false);
        }

        @Override
        public void onXWalkUpdateCompleted() {
            Log.i(TAG, "onXWalkUpdateCompleted ");
            if (mListener != null) mListener.onDownloadOneKitResult(true);
            mXWalkInitializer.initAsync();
        }
    };

    private boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public interface CheckXWalkStatusListener {
        boolean onCheckStatus();
    }

}
