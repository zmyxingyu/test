package com.cn21.onekit.core;

import java.util.Map;

import org.xwalk.core.XWalkView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;

/**
 *
 * Created by xuxd on 2017/7/11.
 */
public class OneKitView extends RelativeLayout {

    private static final String TAG = OneKitConstants.ONEKIT_SDK_LOG_PREFIX + "OneKitView";
    private Context mContext;
    private WebView mWebView;
    private XWalkView mXWalkView;
    private boolean isXWalkReady = false;
    private OneKitProvider mOneKitProvider;



    public OneKitView(Context context) {
        this(context ,null);
    }

    public OneKitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public OneKitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        Log.i(TAG, "init() ");
        isXWalkReady = OneKitManager.getInstance().isXWalkReady();
        if(isXWalkReady){
            Log.i(TAG, "use  XWalkView !!!");
            mXWalkView = new XWalkView(mContext);
            mXWalkView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT));
            this.addView(mXWalkView);
        }else {
            Log.i(TAG, "use  WebView !!!");
            mWebView = new WebView(mContext);
            mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT));
            this.addView(mWebView);
        }

        mOneKitProvider = OneKitProviderFactory.createProvider(mXWalkView , mWebView);
    }

    public OneKitProvider getOneKitProvider() {
        return mOneKitProvider;
    }

    public void loadUrl(String url){
        mOneKitProvider.loadUrl(url);
    }

    public void loadUrl(String url ,Map<String,String> additionalHttpHeaders){
        mOneKitProvider.loadUrl(url ,additionalHttpHeaders);
    }

    public void loadData(String data, String mimeType, String encoding) {
        mOneKitProvider.loadData(data, mimeType, encoding);
    }

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        mOneKitProvider.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    public void addJavascriptInterface(Object object, String name) {
        mOneKitProvider.addJavascriptInterface(object, name);
    }

    public void getTitle() {
        mOneKitProvider.getTitle();
    }

    public void getUrl() {
        mOneKitProvider.getUrl();
    }

    public void getOriginalUrl() {
        mOneKitProvider.getOriginalUrl();
    }

    public boolean canGoBack() {
        return mOneKitProvider.canGoBack();
    }

    public void goBack() {
        mOneKitProvider.goBack();
    }

    public void canGoForward() {
        mOneKitProvider.canGoForward();
    }

    public void destroy() {
        mOneKitProvider.destroy();
    }

    public void reload() {
        mOneKitProvider.reload();
    }

    public void scrollTo(int x, int y) {
        mOneKitProvider.scrollTo(x ,y);
    }

    public void scrollBy(int x, int y) {
        mOneKitProvider.scrollBy(x ,y);
    }

    public void setBackgroundColor(int color) {
        mOneKitProvider.setBackgroundColor(color);
    }

    public void setBackgroundResource(int resid)  {
        mOneKitProvider.setBackgroundResource(resid);
    }

    public OKSettings getSettings()  {
        return mOneKitProvider.getSettings();
    }

    public void setOKViewClient(OKViewClient client)  {
        mOneKitProvider.setOKViewClient(this , client);
    }


    public interface PageLoadListener{
        void onLoadFinished(String url);
    }
}
