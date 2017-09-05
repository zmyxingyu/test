package com.cn21.onekit.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/7/27.
 */
public class AndroidUtils {
    public static String getApplicationMataValue(Context activity, String componentName) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String msg = appInfo.metaData.getString(componentName);
        return msg;
    }

    public static void showShortToast(Context context, CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
