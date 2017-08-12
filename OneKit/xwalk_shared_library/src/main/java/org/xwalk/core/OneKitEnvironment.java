package org.xwalk.core;

/**
 * Created by Administrator on 2017/8/2.
 */
public class OneKitEnvironment {

    public static String getXWalkApkUrl() {
        return XWalkEnvironment.getXWalkApkUrl();
    }

    public static void setXWalkApkUrl(String url) {
        XWalkEnvironment.setXWalkApkUrl(url);
    }
}
