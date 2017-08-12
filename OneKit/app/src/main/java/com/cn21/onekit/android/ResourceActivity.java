package com.cn21.onekit.android;

import android.os.Bundle;

import com.cn21.onekit.core.OneKitView;
import com.cn21.onekit.lib.update.ResoucePackageManager;

/**
 * Created by Administrator on 2017/8/10.
 */
public class ResourceActivity extends OneKitActivity{
    OneKitView mOneKitView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        mOneKitView = (OneKitView) findViewById(R.id.my_onekit);
        mOneKitView.loadUrl(ResoucePackageManager.getRequestWebviewUrl());
        mOneKitView.getSettings().setJavaScriptEnabled(true);

    }
}
