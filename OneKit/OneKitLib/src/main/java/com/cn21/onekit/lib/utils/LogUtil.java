package com.cn21.onekit.lib.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/28.
 */
public class LogUtil {
    public static final String TAG = "okLib";

    public static void v(String msg, Object... args) {
        Log.v(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg);
    }

    public static void v(Throwable throwable, String msg, Object... args) {
        Log.v(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg, throwable);
    }

    public static void d(String msg, Object... args) {
        Log.d(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg);
    }

    public static void d(Throwable throwable, String msg, Object... args) {
        Log.d(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg, throwable);
    }

    public static void i(String msg, Object... args) {
        Log.i(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg);
    }

    public static void i(Throwable throwable, String msg, Object... args) {
        Log.i(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg, throwable);
    }

    public static void w(String msg, Object... args) {
        Log.w(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg);
    }

    public static void w(Throwable throwable, String msg, Object... args) {
        Log.w(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg, throwable);
    }

    public static void e(String msg, Object... args) {
        Log.e(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg);
    }

    public static void e(Throwable throwable, String msg, Object... args) {
        Log.e(TAG, (args != null && args.length > 0) ? String.format(msg, args) : msg, throwable);
    }
}
