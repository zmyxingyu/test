package com.cn21.onekit.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.cn21.onekit.core.OKViewClient;
import com.cn21.onekit.core.OneKitView;
import com.cn21.onekit.lib.update.ResoucePackageManager;

/**
 * Created by Administrator on 2017/8/10.
 */
public class ResourceActivity extends BaseActivity {
    OneKitView mOneKitView = null;
    private String TAG = ResourceActivity.class.getSimpleName();
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar);
        mOneKitView = (OneKitView) findViewById(R.id.my_onekit);
        mOneKitView.getSettings().setJavaScriptEnabled(true);
        mOneKitView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mOneKitView.setOKViewClient(new MyClient());
        mOneKitView.loadUrl(ResoucePackageManager.getRequestWebviewUrl());

//        FilterManager.getInstance().addUrlMapping("http://k.21cn.com/api/activity/exchange/integral_index.html",
//                "file:///android_asset/test/junitdemo.html");
//        mOneKitView.loadUrl("http://k.21cn.com/api/app/exchange_center/index.html");
    }

    class MyClient extends OKViewClient {
        @Override
        public void onLoadFinished(OneKitView view, String url) {
            super.onLoadFinished(view, url);
            Log.d(TAG, "onLoadFinished:" + url);
            mProgressbar.setVisibility(View.GONE);
        }

//        @Override
//        public boolean WebView_shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            Log.d(TAG, "WebView_shouldOverrideUrlLoading");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            }
//            return true;
//        }
//
        @Override
        public boolean WebView_shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
//
//        @Override
//        public boolean XWalk_shouldOverrideUrlLoading(XWalkView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
    }
}
