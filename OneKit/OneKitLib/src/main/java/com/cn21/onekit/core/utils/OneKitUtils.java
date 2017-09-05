package com.cn21.onekit.core.utils;

import android.net.Uri;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/25.
 */
public class OneKitUtils {
    public static String getMime(String url) {
        String mime = "text/html";
        Uri currentUri = Uri.parse(url);
        String path = currentUri.getPath();
        if (path.contains(".css")) {
            mime = "text/css";
        } else if (path.contains(".js")) {
            mime = "application/x-javascript";
        } else if (path.contains(".jpg") || path.contains(".gif") ||
                path.contains(".png") || path.contains(".jpeg")) {
            mime = "image/*";
        }
        return mime;
    }

    public static String buildLocalUri(String unZipPath, String fileName) {
        Log.d("OneKitUtils", "unZipPath:" + unZipPath + ",name:" + fileName);
        return "file://" + unZipPath + "/" + fileName;
    }


    public static String getLocalPath(String uri) {
        if (uri.startsWith("file://")) {
            return uri.substring(7, uri.length());
        }
        return uri;
    }


}
