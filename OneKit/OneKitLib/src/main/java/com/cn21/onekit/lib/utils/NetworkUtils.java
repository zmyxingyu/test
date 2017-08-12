package com.cn21.onekit.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Administrator on 2017/7/25.
 */
public class NetworkUtils {
    private static final String TAG = "net_work_util";

    public static String getNetworkType(final Context context) {
        String strNetworkType = null;

        NetworkInfo networkInfo =
            ((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null &&
            networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_BLUETOOTH) {
                strNetworkType = "bluetooth";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                strNetworkType = "ethernet";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "wifi";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                Log.d(TAG, "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
                        strNetworkType = "2g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
                        strNetworkType = "3g";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
                        strNetworkType = "4g";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if ("TD-SCDMA".equalsIgnoreCase(_strSubTypeName) ||
                                "WCDMA".equalsIgnoreCase(_strSubTypeName) ||
                                "CDMA2000".equalsIgnoreCase(_strSubTypeName)) {
                            strNetworkType = "3g";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }

                Log.d(TAG, "Network getSubtype : " + networkType);
            }
        }
        Log.d(TAG, "Network Type : " + strNetworkType);
        return strNetworkType;
    }
}
