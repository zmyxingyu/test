package com.cn21.onekit.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Administrator on 2017/8/3.
 */
public class OkEnvrionmentUtil {
    /**
     * 未知运行环境
     **/
    public static final int RUNTIME_UNKNOWN = 0;
    /**
     * 32位运行环境
     **/
    public static final int RUNTIME_32_BIT = 1;
    /**
     * 64位运行环境
     **/
    public static final int RUNTIME_64_BIT = 2;
    private static int sRuntimeBit = RUNTIME_UNKNOWN;

    /**
     * @return 返回值为：{@link #RUNTIME_32_BIT}, {@link #RUNTIME_UNKNOWN}
     */
    public static int is32BitRuntime() {
        if (sRuntimeBit != RUNTIME_UNKNOWN) {
            return sRuntimeBit;
        }
        boolean runtime32 = FileUtil.findTxtInFile(String.format("/proc/%d/maps", android.os.Process.myPid()),
                "/system/lib/libc.so");
        sRuntimeBit = runtime32 ? RUNTIME_32_BIT : RUNTIME_UNKNOWN;
        return sRuntimeBit;
    }

    /**
     * @return 返回值为：{@link #RUNTIME_64_BIT}, {@link #RUNTIME_UNKNOWN}
     */
    public static int is64BitRuntime() {
        if (sRuntimeBit != RUNTIME_UNKNOWN) {
            return sRuntimeBit;
        }
        boolean runtime64 = FileUtil.findTxtInFile(String.format("/proc/%d/maps", android.os.Process.myPid()),
                "/system/lib64/libc.so");
        sRuntimeBit = runtime64 ? RUNTIME_64_BIT : RUNTIME_UNKNOWN;
        return sRuntimeBit;
    }

    public static String fixApkUrl(Context context, String url) {
        return fixApkUrl(getAppRuntimeBit(context), url);
    }

    public static String fixApkUrl(int runtimeType, String url) {
        boolean is32 = false;
        if (runtimeType == RUNTIME_32_BIT) {
            is32 = true;
        } else if (runtimeType == RUNTIME_64_BIT) {
            is32 = false;
        } else {
            return url;
        }
        //http://10.16.33.15:3000/XWalkRuntimeLib.apk?arch=arm64-v8a  x86  armeabi-v7a
        String key = "arch";
        String parms = Uri.parse(url).getQueryParameter(key);
        String value = null;
        if (!TextUtils.isEmpty(parms)) {
            if (is32) {
                if (parms.equals("arm64-v8a")) {
                    value = "armeabi-v7a";
                } else if (parms.equals("x86_64")) {
                    value = "x86";
                }

            } else {
                if (parms.equals("armeabi-v7a")) {
                    value = "arm64-v8a";
                } else if (parms.equals("x86")) {
                    value = "x86_64";
                }
            }
        }
        if (!TextUtils.isEmpty(value)) {
            url = url.replace(parms, value);
        }
        return url;
    }

    public static List<String> getCommodResult(Process ex) {
        List<String> lists = new ArrayList<>();
        InputStreamReader ir = new InputStreamReader(ex.getInputStream());
        BufferedReader input = new BufferedReader(ir);
        String line = null;
        try {
            while ((line = input.readLine()) != null) {
                lists.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (ir != null) {
                    ir.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }


    public static final String ZYGOTE64 = "zygote64";
    public static final String ZYGOTE = "zygote";

    private static List<String> filterProcessInfo(Context context, List<String> lists) {
        if (lists == null || lists.size() < 2) {
            return null;
        }
        List<String> filters = new ArrayList<>();
        filters.add(lists.get(0));
        String name = context.getPackageName();
        for (int i = 1; i < lists.size(); i++) {
            String item = lists.get(i);
            if (!TextUtils.isEmpty(item)) {
                String[] strs = item.split("\\s+|\\n+|\\t+|\\r+");
                if (getIndex(name, strs) > 0 || getIndex(ZYGOTE64, strs) > 0 || getIndex(ZYGOTE, strs) > 0) {
                    filters.add(item);
                }
            }
        }
        return filters;
    }

    public static String getPPID(int index, String process) {
        if (TextUtils.isEmpty(process)) {
            return null;
        }
        String[] strs = process.split("\\s+|\\n+|\\t+|\\r+");
        if (index > 0) {
            return strs[index];
        } else {
            //// FIXME: 2017/8/3
            return strs[2];
        }
    }

    public static int getIndex(String key, String[] keys) {
        if (TextUtils.isEmpty(key) || keys == null) {
            throw new IllegalArgumentException("key or keys can not null ");
        }
        for (int i = 0; i < keys.length; i++) {
            if (key.equalsIgnoreCase(keys[i])) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isLineContain(String line, String[] keys) {
        if (TextUtils.isEmpty(line)) {
            return false;
        }
        if (keys == null || keys.length < 1) {
            return true;
        }
        String[] strs = line.split("\\s+|\\n+|\\t+|\\r+");
        if (strs == null || strs.length < 1) {
            return false;
        }
        for (String key : keys) {
            int index = getIndex(key, strs);
            if (index < 0) {
                return false;
            }
        }
        return true;
    }


    //64 or 32
    public static int getAppRuntimeBit(Context context) {
        String packageName = context.getPackageName();
        String ppid = null;
        Process pcApp = null;
        try {
            pcApp = Runtime.getRuntime().exec("ps");
            List<String> lists = filterProcessInfo(context, getCommodResult(pcApp));

            if (lists != null && !lists.isEmpty()) {
                String pcInfo1 = lists.get(0);
                boolean isTitle = isLineContain(pcInfo1, new String[]{"ppid", "pid"});
                int index = -1;
                if (isTitle) {
                    index = getIndex("ppid", pcInfo1.split("\\s+|\\n+|\\t+|\\r+"));
                }
                String pcInfo = null;
                for (String item : lists) {
                    if (isLineContain(item, new String[]{packageName})) {
                        pcInfo = item;
                        break;
                    }
                }
                ppid = getPPID(index, pcInfo);
            }

            if (TextUtils.isEmpty(ppid)) {
                return RUNTIME_UNKNOWN;
            }

            for (String item : lists) {
                if (isLineContain(item, new String[]{ppid, "zygote64"})) {
                    return RUNTIME_64_BIT;
                }
                if (isLineContain(item, new String[]{ppid, "zygote"})) {
                    return RUNTIME_32_BIT;
                }
            }
            return RUNTIME_UNKNOWN;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
}
