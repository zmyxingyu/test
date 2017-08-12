package com.cn21.onekit.core;

import org.xwalk.core.XWalkNavigationHistory;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkWebResourceRequest;
import org.xwalk.core.XWalkWebResourceResponse;

import java.util.Map;

/**
 * Created by Dong on 2017/7/13.
 */

public class OneKitViewProvider implements OneKitProvider{

    private XWalkView mXWalkView;
    private XWalkResourceClient mXWalkResourceClient = null;
    private OneKitView oneKitView = null;
    private OKViewClient client = null;
    private OneKitView.PageLoadListener mPageLoadListener;

    public OneKitViewProvider(XWalkView mXWalkView) {
        this.mXWalkView = mXWalkView;
        initXWalkResourceClient(mXWalkView);


    }

    @Override
    public void loadUrl(String url) {
        mXWalkView.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        mXWalkView.loadUrl(url, additionalHttpHeaders);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        mXWalkView.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mXWalkView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void addJavascriptInterface(Object object, String name) {
        mXWalkView.addJavascriptInterface(object, name);
    }

    @Override
    public void getTitle() {
        mXWalkView.getTitle();
    }

    @Override
    public void getUrl() {
        mXWalkView.getUrl();
    }

    @Override
    public void getOriginalUrl() {
        mXWalkView.getOriginalUrl();
    }

    @Override
    public boolean canGoBack() {
        return mXWalkView.getNavigationHistory().canGoBack();
    }

    @Override
    public void goBack() {
        mXWalkView.getNavigationHistory().navigate(XWalkNavigationHistory.Direction.BACKWARD, 1);
    }

    @Override
    public void canGoForward() {
        mXWalkView.getNavigationHistory().canGoForward();
    }

    @Override
    public void destroy() {
        mXWalkView.onDestroy();
    }

    @Override
    public void reload() {
        mXWalkView.reload(XWalkView.RELOAD_NORMAL);
    }

    @Override
    public void scrollTo(int x, int y) {
        mXWalkView.scrollTo(x ,y);
    }

    @Override
    public void scrollBy(int x, int y) {
        mXWalkView.scrollBy(x ,y);
    }


    @Override
    public void setBackgroundColor(int color) {
        mXWalkView.setBackgroundColor(color);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mXWalkView.setBackgroundResource(resid);
    }

    @Override
    public OKSettings getSettings() {
        XWalkSettings xWalkSettings = mXWalkView.getSettings();
        return new OKSettings(xWalkSettings);
    }


    @Override
    public void setOKViewClient(final OneKitView oneKitView , final OKViewClient client) {
        this.oneKitView = oneKitView;
        this.client = client;
    }

    @Override
    public void setPageLoadListener(OneKitView.PageLoadListener listener) {
        mPageLoadListener = listener;
    }

    private void initXWalkResourceClient(XWalkView mXWalkView){
        mXWalkResourceClient = new  XWalkResourceClient(mXWalkView){
            @Override
            public void onLoadStarted(XWalkView view, String url) {
                super.onLoadStarted(view, url);
                if(client != null){
                    client.onLoadStarted(oneKitView  ,url);
                }
            }

            @Override
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
                if(client != null){
                    client.onLoadFinished(oneKitView  ,url);
                }
                if(mPageLoadListener != null){
                    mPageLoadListener.onLoadFinished(url);
                }
            }
            @Override
            public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                super.onReceivedLoadError(view, errorCode, description, failingUrl);
                if(client != null){
                    client.XWalk_onReceivedLoadError(view  ,errorCode, description, failingUrl);
                }
            }

            @Override
            public void onProgressChanged(XWalkView view, int progressInPercent) {
                super.onProgressChanged(view, progressInPercent);

                if(client != null){
                    client.onProgressChanged(oneKitView  ,progressInPercent);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
                if(client != null){
                    return client.XWalk_shouldOverrideUrlLoading(view  ,url);
                }
                return false;
            }

            @Override
            public XWalkWebResourceResponse shouldInterceptLoadRequest(XWalkView view, XWalkWebResourceRequest request) {
                if(client != null){
                    return client.XWalk_shouldInterceptLoadRequest(view  ,request);
                }
                return null;
            }
        };
        mXWalkView.setResourceClient(mXWalkResourceClient);
    }
}
