package com.cn21.onekit.core;

import android.webkit.WebView;

import org.xwalk.core.XWalkView;

/**
 * Created by xuxd on 2017/7/13.
 */

public class OneKitProviderFactory {

    public static OneKitProvider createProvider(XWalkView mXWalkView , WebView mWebView){
        if(mXWalkView != null){
            return new OneKitViewProvider(mXWalkView);
        }else{
            return new WebViewProvider(mWebView);
        }
    }
}
