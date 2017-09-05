package com.cn21.onekit.core;

import org.xwalk.core.XWalkView;

import android.webkit.WebView;

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
