package com.cn21.onekit.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;


import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by kaidi.wu on 2015/12/2. <br>e-mail:kaid123@163.com
 */
public class DeviceInfoUtil {
    private final static String tag = DeviceInfoUtil.class.getCanonicalName();
    private final static String key = "Dev8k03JiceI1OQPLiNfoi2kenm281x2qw";
    private static String userAgent = "";

    public synchronized static String getUserAgentInstance(final Context mContext) {
        if (TextUtils.isEmpty(userAgent) && null != mContext) {
            try {
                if (Build.VERSION.SDK_INT < 19) {
                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(mContext!=null) {
                                WebSettings settings = new WebView(mContext).getSettings();
                                userAgent = settings.getUserAgentString();
                            }
                        }
                    });
                } else {
                    userAgent = WebSettings.getDefaultUserAgent(mContext);
                }
            } catch (Exception e) {
                Log.w(tag, "getUserAgent throw Exception:", e);
            }
        }
        return userAgent;
    }

    public static String getIMEI(Context mContext) {
        try {
            if (!PermissionManager.checkPhoneStatePermission(mContext)) {
                return "";
            } else {
                return ((TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            }
        } catch (Exception e) {
            Log.w(tag, "getIMEI throw Exception:", e);
            return "";
        }
    }

    public static String getIMSI(Context mContext) {
        try {
            if (!PermissionManager.checkPhoneStatePermission(mContext)) {
                return "";
            } else {
                return ((TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE)).getSubscriberId();
            }
        } catch (Exception e) {
            Log.w(tag, "getIMSI throw Exception:", e);
            return "";
        }
    }

    /**
    /**
     * 根据imsi判断运营商 cm-移动, ct-电信, cu-联通, ""-无法判断
     */
    public static String getISPType(String imsi) {
        if (!TextUtils.isEmpty(imsi)) {
            if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46004") || imsi.startsWith("46007"))
                return "cm";//移动
            else if (imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011"))
                return "ct";//电信
            else if (imsi.startsWith("46001") || imsi.startsWith("46006") || imsi.startsWith("46009"))
                return "cu";//联通
        }
        return "";
    }

    public static String getSysVersion() {
        try {
            return "android" + Build.VERSION.RELEASE;
        } catch (Exception e) {
            Log.w(tag, "getSysVersion throw Exception:", e);
            return "";
        }
    }

    /**
     * 获取本地IP地址
     */
    public static String getLocalIpAddr() {
        String ipAddr = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddr = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.w(tag, "getLocalIpAddr throw Exception:", ex);
        }
        return ipAddr;
    }

    /**
     * 返回网络类型networkInfo.getSubtypeName(),如果是WIFI则返回WIFI，如果无网络则返回""
     */
    public static String getNetworkType(Context mContext) {
        ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                    return networkInfo.getTypeName();
                }
                return networkInfo.getSubtypeName();
            }
        }
        return "";
    }

    /**
     * 判断网络是否在连状态
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo[] networkInfos = connectMgr.getAllNetworkInfo();//This method was deprecated in API level 23
            if (networkInfos != null && networkInfos.length > 0) {
                for (NetworkInfo networkInfo : networkInfos) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * 根据包名获取PackageInfo,若不存在则返回空
     */
    public static PackageInfo getPackageInfo(Context mContext, String packageName) {
        if (!TextUtils.isEmpty(packageName) || mContext != null) {
            try {
                return mContext.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            } catch (NameNotFoundException e) {
                Log.w(tag, "getPackageInfo throw Exception:", e);
            }
        }
        return null;
    }


    /**
     * 返回当前APP的versionName
     */
    public static String getAppSelfVersion(Context mContext) {
        return getAppSelfVersion(getPackageInfo(mContext, mContext.getPackageName()));
    }

    /**
     * 返回当前APP的versionName
     */
    public static String getAppSelfVersion(PackageInfo mPackageInfo) {
        if (mPackageInfo != null) {
            String versionName = mPackageInfo.versionName;
            if (!TextUtils.isEmpty(versionName)) return versionName;
        }
        return "";
    }

    /**
     * 获取当前APP的应用名
     */
    public static String getAppName(Context mContext) {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            String appName = pi.applicationInfo.loadIcon(pm).toString();
            if (!TextUtils.isEmpty(appName)) return appName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取application下的 meta-data预先定义好的String数据
     */
    public static String getApplicationMetaData(Context mContext, String metaDataKey) {
        try {
            Bundle mBundle =  mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA).metaData;
            if(mBundle != null && mBundle.containsKey(metaDataKey)){
                return mBundle.getString(metaDataKey);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取综合平台下发的appId
     */
    public static String getE189cnAppKey(Context mContext) {
        return getApplicationMetaData(mContext, "E189CN_APPKEY");
    }

    /**
     * 获取密钥（appId+IMEI->>MD5）
     */
    public static String getCipherKey(Context mContext) {
        try {
            return StringUtil.getMD5Bit32(key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 获取密钥（appId+IMEI->>MD5）
     */
    public static String getCipherKeyOld(Context context) {
        try {
            return StringUtil.getMD5Bit32(getE189cnAppKey(context) + getIMEI(context));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Android_ID是设备首次启动时，系统生成的一个唯一串号，长16字节(例：70560687d711af97)，由com.android.providers.settings 这个系统程序所管理，
     * Android6.0 以下储存在/data/data/com.android.providers.settings/databases/settings.db中的secure 表
     * 系统升级可能会变，同品牌不同手机有可能拿到一样值
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        String androidId = getSystemString(context, Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 获取System.getString的数据
     *
     * @param mContext
     * @param key      保存的数据对应的key
     * @return 返回对应数据（没有则空串）
     */
    public static String getSystemString(Context mContext, String key) {
        if (null != mContext && !TextUtils.isEmpty(key)) {
            try {
                return Settings.System.getString(mContext.getContentResolver(), key);
            } catch (Exception e) {
                Log.w(tag, "getSystemString throw Exception:", e);
            }
        }
        return "";
    }


    public static String getImeiAndImsi(Context context) {
        String phoneIm = "";
        try {
            if (!PermissionManager.checkPhoneStatePermission(context)) {
                return phoneIm;
            } else {
                TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                Class clazz = manager.getClass();
                Method getImei = clazz.getDeclaredMethod("getImei", int.class);//(int slotId)
                //获得IMEI 1的信息：
                String imei1 = (String) getImei.invoke(manager, 0);
                String imei2 = (String) getImei.invoke(manager, 1);
                Method getImsi = clazz.getDeclaredMethod("getSubscriberId", int.class);
                String imsi1 = (String) getImsi.invoke(manager, 0);
                String imsi2 = (String) getImsi.invoke(manager, 1);
                imei1 = (imei1 == null) ? "" : imei1;
                imei2 = (imei2 == null) ? "" : imei2;
                imsi1 = (imsi1 == null) ? "" : imsi1;
                imsi2 = (imsi2 == null) ? "" : imsi2;
                if(TextUtils.isEmpty(imei1)){
                    imei1 = getIMEI(context) ;
                }
                if(TextUtils.isEmpty(imsi1)){
                    imsi1 = getIMSI(context) ;
                }
                phoneIm = imei1 + "-" + imei2 + "-" + imsi1 + "-" + imsi2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e){
            if(e != null)e.printStackTrace();
        }
        return phoneIm;
    }

    /**
     * 获取手机卡运营商信息
     * @param context
     * @return
     */
    public static String getOperatorType(Context context) {
        if (context == null)
            return "";
        String operatorType;
        String phoneIms = getImeiAndImsi(context);
        String imsi1 = "";
        String imsi2 = "";
        try {
            String[] ims = phoneIms.split("-", 4);
            if (ims.length == 4) {
                imsi1 = ims[2];
                imsi2 = ims[3];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(imsi1)) {
            imsi1 = getIMSI(context);
        }
        String operatorType1 = getISPType(imsi1);
        String operatorType2 = getISPType(imsi2);
        operatorType = operatorType1 + "," + operatorType2;
        return operatorType;
    }

}
