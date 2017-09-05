package org.xwalk.core;

/**
 * Created by zhangmy on 2017/8/2.
 */
public class OneKitEnvironment {

    /**
     * 获取download模式下，下载runtimeLab地址
     * @return
     */
    public static String getXWalkApkUrl() {
        return XWalkEnvironment.getXWalkApkUrl();
    }

    /**
     * 设置下载runtimeLab地址。（download模式下）
     * @param url
     */
    public static void setXWalkApkUrl(String url) {
        XWalkEnvironment.setXWalkApkUrl(url);
    }
}
