package com.cn21.onekit.core;

import org.xwalk.core.XWalkSettings;

import android.os.Build;
import android.webkit.WebSettings;

/**
 * Created by lecykx on 2017/7/11.
 */

public class OKSettings {

    XWalkSettings xWalkSettings;

    WebSettings webSettings;

    public OKSettings(XWalkSettings xWalkSettings){
        this.xWalkSettings = xWalkSettings;
    }

    public OKSettings(WebSettings webSettings){
        this.webSettings = webSettings;
    }

    public boolean getJavaScriptEnabled() {
        if (xWalkSettings != null) {
            return xWalkSettings.getJavaScriptEnabled();
        } else {
            return webSettings.getJavaScriptEnabled();
        }
    }

    public void setJavaScriptEnabled(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setJavaScriptEnabled(flag);
        } else {
            webSettings.setJavaScriptEnabled(flag);
        }
    }

    public void setUserAgentString(String userAgent) {
        if (xWalkSettings != null) {
            xWalkSettings.setUserAgentString(userAgent);
        } else {
            webSettings.setUserAgentString(userAgent);
        }
    }

    public void setSupportZoom(boolean support) {
        if (xWalkSettings != null) {
            xWalkSettings.setSupportZoom(support);
        } else {
            webSettings.setSupportZoom(support);
        }
    }

    public void setSupportMultipleWindows(boolean support) {
        if (xWalkSettings != null) {
            xWalkSettings.setSupportMultipleWindows(support);
        } else {
            webSettings.setSupportMultipleWindows(support);
        }
    }

    public void setDomStorageEnabled(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setDomStorageEnabled(flag);
        } else {
            webSettings.setDomStorageEnabled(flag);
        }
    }

    public void setAllowContentAccess(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setAllowContentAccess(flag);
        } else {
            webSettings.setAllowContentAccess(flag);
        }
    }

    public void setAllowFileAccessFromFileURLs(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setAllowFileAccessFromFileURLs(flag);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowFileAccessFromFileURLs(flag);
            }
        }
    }

    public void setLoadWithOverviewMode(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setLoadWithOverviewMode(flag);
        } else {
            webSettings.setLoadWithOverviewMode(flag);
        }
    }

    public void setUseWideViewPort(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setUseWideViewPort(flag);
        } else {
            webSettings.setUseWideViewPort(flag);
        }
    }

    /**
     *
     * @param mode //todo 查找mode的值
     */
    public void setCacheMode(int mode) {
        if (xWalkSettings != null) {
            xWalkSettings.setCacheMode(mode);
        } else {
            webSettings.setCacheMode(mode);
        }
    }

    // webSettings.getJavaScriptEnabled();
    // webSettings.setJavaScriptEnabled();
    // webSettings.setUserAgentString();
    // webSettings.setSupportZoom();
    // webSettings.setSupportMultipleWindows();
    // webSettings.setDomStorageEnabled();
    // webSettings.setCacheMode();

}
