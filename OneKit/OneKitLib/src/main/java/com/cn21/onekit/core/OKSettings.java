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

    public void setAllowUniversalAccessFromFileURLs(boolean flag) {
        if (xWalkSettings != null) {
            xWalkSettings.setAllowUniversalAccessFromFileURLs(flag);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowUniversalAccessFromFileURLs(flag);
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

    public void setBuiltInZoomControls(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setBuiltInZoomControls(enable);
        } else {
            webSettings.setBuiltInZoomControls(enable);
        }
    }

    public void setDisplayZoomControls(boolean enable) {
        if (webSettings != null) {
            webSettings.setDisplayZoomControls(enable);
        }
    }

    public void setSaveFormData(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setSaveFormData(enable);
        } else {
            webSettings.setSaveFormData(enable);
        }
    }

    public void setTextZoom(int textZoom) {
        if (xWalkSettings != null) {
            xWalkSettings.setTextZoom(textZoom);
        } else {
            webSettings.setTextZoom(textZoom);
        }
    }

    public void setLoadsImagesAutomatically(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setLoadsImagesAutomatically(enable);
        } else {
            webSettings.setLoadsImagesAutomatically(enable);
        }
    }

    public void setBlockNetworkImage(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setBlockNetworkImage(enable);
        } else {
            webSettings.setBlockNetworkImage(enable);
        }
    }

    public void setBlockNetworkLoads(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setBlockNetworkLoads(enable);
        } else {
            webSettings.setBlockNetworkLoads(enable);
        }
    }

    public void setDatabaseEnabled(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setDatabaseEnabled(enable);
        } else {
            webSettings.setDatabaseEnabled(enable);
        }
    }

    public void setGeolocationEnabled(boolean enable) {
        if (webSettings != null) {
            webSettings.setGeolocationEnabled(enable);
        }
    }

    public void setJavaScriptCanOpenWindowsAutomatically(boolean enable) {
        if (xWalkSettings != null) {
            xWalkSettings.setJavaScriptCanOpenWindowsAutomatically(enable);
        } else {
            webSettings.setJavaScriptCanOpenWindowsAutomatically(enable);
        }
    }

    public void setDefaultTextEncodingName(String encoding) {
        if (webSettings != null) {
            webSettings.setDefaultTextEncodingName(encoding);
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
