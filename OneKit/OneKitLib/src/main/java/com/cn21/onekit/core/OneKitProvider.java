package com.cn21.onekit.core;

import java.util.Map;

/**
 * Created by Dong on 2017/7/13.
 */

public interface OneKitProvider {

    public void loadUrl(String url);

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders);


    public void loadData(String data, String mimeType, String encoding) ;

    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) ;

    public void addJavascriptInterface(Object object, String name) ;

    public void getTitle() ;

    public void getUrl() ;

    public void getOriginalUrl() ;

    public boolean canGoBack() ;

    public void goBack();

    public void canGoForward() ;

    public void destroy() ;

    public void reload() ;

    public void scrollTo(int x, int y) ;


    public void scrollBy(int x, int y) ;


    public void setBackgroundColor(int color) ;

    public void setBackgroundResource(int resid) ;

    public OKSettings getSettings() ;

    public void setOKViewClient(final OneKitView oneKitView, final OKViewClient client)  ;

    public void setPageLoadListener(OneKitView.PageLoadListener listener);
}
