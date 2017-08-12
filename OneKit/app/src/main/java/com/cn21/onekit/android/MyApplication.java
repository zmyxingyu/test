package com.cn21.onekit.android;

import android.app.Application;

import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.lib.update.ResoucePackageManager;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lecykx on 2017/7/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OneKitManager.getInstance().init(this);
        ResoucePackageManager.getInstance().init(this, "test", "");
        //初始化bugly
        CrashReport.initCrashReport(this, "b7764184c0", BuildConfig.DEBUG);
    }
}
