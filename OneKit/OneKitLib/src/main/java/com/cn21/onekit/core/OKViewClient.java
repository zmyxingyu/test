package com.cn21.onekit.core;


import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

/**
 * Created by xuxd on 2017/7/12.
 */

public class OKViewClient {


    public void onLoadStarted(OneKitView view, String url) {

    }

    public void onLoadFinished(OneKitView view, String url) {

    }

    public void onProgressChanged(OneKitView view, int progressInPercent) {

    }

    /**
     * XWalk
     */
    public void XWalk_onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {

    }

    public boolean XWalk_shouldOverrideUrlLoading(XWalkView view, String url) {
        return false;
    }

    public XWalkWebResourceResponse XWalk_shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
        return null;
    }


    /**
     * WebView
     */
    public void WebView_onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }

    public boolean WebView_shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
    }

    public boolean WebView_shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    public WebResourceResponse WebView_shouldInterceptRequest(WebView view, WebResourceRequest request) {

        return null;
    }


    //    public void onPageStarted(OneKitView view, String url, Bitmap favicon) {
//
//
//    }
//
//    public void onPageFinished(OneKitView view, String url) {
//
//    }

//    public void onReceivedError(OneKitView view, WebResourceRequest request, WebResourceError error) {
//    }
//
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//    }
//
//    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//    }
}
