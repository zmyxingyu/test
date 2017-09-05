package com.cn21.onekit.android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.cn21.onekit.android.util.AndroidUtils;
import com.cn21.onekit.core.OneKitConfig;
import com.cn21.onekit.core.OneKitInitListener;
import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.core.OneKitRuntime;
import com.cn21.onekit.lib.update.CommonApiManager;
import com.cn21.onekit.lib.update.ResoucePackageManager;
import com.cn21.onekit.lib.update.ResourceDownloadListener;
import com.cn21.ued.apm.util.UEDAgent;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lecykx on 2017/7/17.
 */

public class MyApplication extends Application {

    String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        OneKitManager.createInstance(oneKitRuntimeImpl, new OneKitConfig.Builder().build());
        OneKitManager.getInstance().setInitListener(mListener);
        OneKitManager.getInstance().init(this);
        CommonApiManager.getInstance().setmSwitchListener(swichListener);
        ResoucePackageManager.getInstance().setmResourceDowloadLister(mResourceDownloadLister);
        ResoucePackageManager.getInstance().init(this);
        //初始化bugly
        CrashReport.initCrashReport(this, "b7764184c0", BuildConfig.DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        UEDAgent.startTrafficInfoAndNetSpeed(this);
    }

    private OneKitInitListener mListener = new OneKitInitListener() {
        @Override
        public void onInitOneKitResult(boolean success) {
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    success ? UxContants.INIT_ONEKIT_SUCCESS : UxContants.INIT_ONEKIT_FAILED,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, success ? "init onekit success !" : "init onekit fail !");
        }

        @Override
        public void onDownloadOneKitResult(boolean success) {
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    success ? UxContants.DOWNLOAD_ONEKIT_SUCCESS : UxContants.DOWNLOAD_ONEKIT_FAILED,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, success ? "XWalk download success !" : "XWalk download fail !");
        }

        @Override
        public void onXWalkUpdateStarted() {
            AndroidUtils.showShortToast(MyApplication.this, "XWalk update start !");
        }
    };

    private OneKitRuntime oneKitRuntimeImpl = new OneKitRuntime() {
        @Override
        public String getAccount() {
            return "18819364866";
        }

        @Override
        public String getAppSecret() {
            return "aeiou";
        }

        @Override
        public String getAppId() {
            return "test";
        }

        @Override
        public String getPackageSecret() {
            return null;
        }
    };
    private ResourceDownloadListener mResourceDownloadLister = new ResourceDownloadListener() {

        @Override
        public void onResourceDownloadStart() {
            Log.d(TAG, "onResourceDownloadStart");
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    UxContants.resoure_download_start,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, "start download resource !");
        }

        @Override
        public void onResourceDownload(boolean success) {
            Log.d(TAG, "onResourceDownload" + success);
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    success ? UxContants.resoure_download_success : UxContants.resoure_download_Failed,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, "download resource " + (success ? " success !" : "fail !"));
        }

        @Override
        public void onResourceCheckMd5(boolean success) {
            Log.d(TAG, "onResourceCheckMd5" + success);
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    success ? UxContants.resoure_checkMd5_success : UxContants.resource_checkMd5_failed,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, "check resource zip md5" + (success ? " success !" : "fail !"));
        }

        @Override
        public void onResourceUnzip(boolean success) {
            Log.d(TAG, "onResourceUnzip" + success);
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    success ? UxContants.resource_unzip_success : UxContants.resource_unzip_failed,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, "unzip resource " + (success ? " success !" : "fail !"));
        }

        @Override
        public void onResourceCheckFiles(boolean success) {
            Log.d(TAG, "onResourceCheckFiles" + success);
            AndroidUtils.showShortToast(MyApplication.this, "check every file " + (success ? " success !" : "fail !"));
        }

        @Override
        public void onResourceUpdateSuccess() {
            Log.d(TAG, "onResourceUpdateSuccess");
            UEDAgent.trackCustomKVEvent(getApplicationContext(),
                    UxContants.resource_update_success,
                    null, null);
            AndroidUtils.showShortToast(MyApplication.this, "update resource success !");
        }
    };

    private CommonApiManager.SwitchListener swichListener = new CommonApiManager.SwitchListener() {
        @Override
        public void onSwitchErr() {
            AndroidUtils.showShortToast(MyApplication.this, "get xwalk switch status err !");
        }

        @Override
        public void onSwitchSuccess(boolean status) {
            AndroidUtils.showShortToast(MyApplication.this, "switch status :" + (status ? "on" : "off"));
        }
    };
}
