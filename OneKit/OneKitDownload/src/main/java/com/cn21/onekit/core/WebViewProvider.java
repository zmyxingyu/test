package com.cn21.onekit.core;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

/**
 *  WebView
 * Created by xuxd on 2017/7/13.
 */

public class WebViewProvider implements OneKitProvider {
    private WebView mWebView;
    private OneKitView.PageLoadListener mPageLoadListener;
    private OneKitView oneKitView = null;
    private OKViewClient client = null;

    public WebViewProvider(WebView mWebView) {
        this.mWebView = mWebView;

        mWebView.setWebViewClient(mWebViewClient);

    }

    @Override
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        mWebView.loadUrl(url,additionalHttpHeaders);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        mWebView.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void addJavascriptInterface(Object object, String name) {
        mWebView.addJavascriptInterface(object, name);
    }

    @Override
    public void getTitle() {
        mWebView.getTitle();
    }

    @Override
    public void getUrl() {
        mWebView.getUrl();
    }

    @Override
    public void getOriginalUrl() {
        mWebView.getOriginalUrl();
    }

    @Override
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    @Override
    public void goBack() {
        mWebView.goBack();
    }

    @Override
    public void canGoForward() {
        mWebView.canGoForward();
    }

    @Override
    public void destroy() {
        mWebView.destroy();
    }

    @Override
    public void reload() {
        mWebView.reload();
    }

    @Override
    public void scrollTo(int x, int y) {
        mWebView.scrollTo(x ,y);
    }

    @Override
    public void scrollBy(int x, int y) {
        mWebView.scrollBy(x ,y);
    }

    @Override
    public void setBackgroundColor(int color) {
        mWebView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mWebView.setBackgroundResource(resid);
    }

    @Override
    public OKSettings getSettings() {
        WebSettings webSettings = mWebView.getSettings();
        return new OKSettings(webSettings);
    }



    @Override
    public void setOKViewClient(final OneKitView oneKitView , final OKViewClient client) {

        this.oneKitView = oneKitView;
        this.client = client;
        //由于需要监听onPageFinished事件，所以WebChromeClient在初始化时设置
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    @Override
    public void setPageLoadListener(OneKitView.PageLoadListener listener) {
        mPageLoadListener = listener;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(client != null){
                client.onProgressChanged(oneKitView  ,newProgress);
            }
        }
    };
    private WebViewClient mWebViewClient = new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(client != null){
                client.onLoadStarted(oneKitView  ,url);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(client != null){
                client.onLoadFinished(oneKitView  ,url);
            }
            if(mPageLoadListener != null){
                mPageLoadListener.onLoadFinished(url);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if(client != null){
                client.WebView_onReceivedError(view  ,request, error);
            }
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(client != null){
                return client.WebView_shouldOverrideUrlLoading(view  ,request);
            }
            return false;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if(client != null){
                return client.WebView_shouldInterceptRequest(view , request);
            }
            return null;

        }
    };
}
