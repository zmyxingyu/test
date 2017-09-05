package com.cn21.onekit.android;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/14.
 */
public class WebViewTestUtil {

    private String TAG = null;
    private static HashMap<String, WebViewTestUtil> map = new HashMap<>();

    public static WebViewTestUtil getInstance(String tag) {
        WebViewTestUtil instance;
        instance = map.get(tag);
        if (instance == null) {
            synchronized (WebViewTestUtil.class) {
                instance = new WebViewTestUtil(tag);
                map.put(tag, instance);
            }
        }
        return instance;
    }

    private WebViewTestUtil(String tag) {
        this.TAG = tag;
    }

    private long startTime;

    public void start() {
        Log.e(TAG,"before =========");
        startTime = System.currentTimeMillis();
    }

    private String toTime(long start) {
        long millisecond = start % 1000;
        millisecond /= 100;
        start /= 1000;
        long minute = start / 60;
        long second = start % 60;
        minute %= 60;
        return String.format("end %02d:%02d:%01d ======", minute, second, millisecond);
    }

    public void end() {
        long end = System.currentTimeMillis() - startTime;
        Log.e(TAG, "end :"+end +"ms ======");
    }
}
