package com.cn21.onekit.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/7/25.
 */
public class SharePreferenceUtil {


    private SharePreferenceUtil() {
    }

    public static SharedPreferences getSharePreFerence(Context cxt) {
        return cxt.getSharedPreferences("one_kit_cn21", Context.MODE_PRIVATE);
    }

    public static void saveKeyValue(Context cxt, String key, Object value) {
        SharedPreferences.Editor editor = getSharePreFerence(cxt).edit();
        saveKV(editor, key, value);
    }

    public static String getString(Context cxt, String key) {
        return getSharePreFerence(cxt)
                .getString(key, null);
    }

    public static void removeKey(Context cxt, String key) {
        getSharePreFerence(cxt)
                .edit()
                .remove(key)
                .commit();
    }

    public static void clearAll(Context cxt) {
        getSharePreFerence(cxt)
                .edit()
                .clear()
                .commit();
    }

    private static void saveKV(SharedPreferences.Editor editor, String key, Object value) {
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value).commit();
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value).commit();
        } else if (value instanceof String) {
            editor.putString(key, (String) value).commit();
        }
    }
}
