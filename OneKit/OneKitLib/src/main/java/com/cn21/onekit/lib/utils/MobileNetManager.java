package com.cn21.onekit.lib.utils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by mango on 2017/8/4.
 */

public class MobileNetManager {

    private static final String TAG = "MobileNetManager";
    public static final int NO_NETWORK = -1;
    public static final int NETWORK_WITH_MOBILE_DATA = 2;
    public static final int NETWORK_WITHOUT_MOBILE_DATA = 1;
    public static final int NETWORK_ONLY_MOBILE_DATA = 0;

    private static volatile boolean hasCallback;

    public interface OnMobileSelectCallback {
        void onSuccess(String url, Object network);

        void onFailed();
    }

    public static void forceMobileData(final Context appCxt, final String url, final OnMobileSelectCallback callback) {
        final TelephonyManager telephonyManager = (TelephonyManager) appCxt.getSystemService(Context.TELEPHONY_SERVICE);
        boolean isSimReady = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        if (!isSimReady) {
            if (callback != null) {
                callback.onFailed();
            }
            return;
        }
        final ConnectivityManager connectivityManager = (ConnectivityManager) appCxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        final int checkResult = checkIfMobileDataEnabled(appCxt, connectivityManager);
        switch (checkResult) {
            case NO_NETWORK:
            case NETWORK_WITHOUT_MOBILE_DATA:
                if (callback != null) {
                    callback.onFailed();
                }
                return;
            case NETWORK_ONLY_MOBILE_DATA:
                if (callback != null) {
                    callback.onSuccess(url, null);
                }
                return;
            default:
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
                NetworkRequest networkRequest = builder.build();
                ConnectivityManager.NetworkCallback myNetCallback = new ConnectivityManager.NetworkCallback(){

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAvailable(Network network) {
                        connectivityManager.unregisterNetworkCallback(this);
                        if (hasCallback) {
                            return;
                        }
                        if (callback != null) {
                            callback.onSuccess(url, network);
                        }
                        hasCallback = true;
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        connectivityManager.unregisterNetworkCallback(this);
                        if (hasCallback) {
                            return;
                        }
                        if (callback != null) {
                            callback.onFailed();
                        }
                        hasCallback = true;
                    }
                };
                connectivityManager.requestNetwork(networkRequest, myNetCallback);
                hasCallback = false;
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onFailed();
                }
            }
        } else {
            ThreadUtils.createThread("use_mobile_data", new Runnable(){
                @Override
                public void run() {

                    // 4.4及其以下版本不能按次控制哪个网卡访问url而是按时间段(路由表更新时间)来控制的.
                    // 重点在第一次访问时的路由,新配路由会在一段时间后(20-30s)失效又会回归默认,此特性不影响数据网络取号.

                    if (!PermissionManager.checkPermission(appCxt, Manifest.permission.ACCESS_NETWORK_STATE)
                        || !PermissionManager.checkPermission(appCxt, Manifest.permission.CHANGE_NETWORK_STATE)) {
                        Log.d(TAG, "no access network state or change network state permission");
                        if (callback != null) {
                            callback.onFailed();
                        }
                        return;
                    }
                    // enableHIPRI,开启数据网卡
                    connectivityManager.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "enableHIPRI");
                    // wait some time needed to connection manager for waking up
                    try {
                        for (int counter = 0; counter < 30; counter++) {
                            NetworkInfo.State checkState =
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE_HIPRI).getState();
                            if (0 == checkState.compareTo(NetworkInfo.State.CONNECTED))
                                break;
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "check hipri failed");
                    }
                    int hostAddress = lookupHost(extractAddressFromUrl(url));
                    // 新增路由
                    boolean resultBool1 =
                        connectivityManager.requestRouteToHost(ConnectivityManager.TYPE_MOBILE_HIPRI, hostAddress);
                    // 新增域则需要新增路由,比如有302跳转到其他域下,就需要把location的路由加入.

                    Log.e(TAG, "switch to mobile data result >>> " +
                               resultBool1);

                    if (resultBool1) {
                        if (callback != null) {
                            callback.onSuccess(url, null);
                        }
                    } else {
                        Log.d(TAG, "切换网络失败or无数据网络");
                        if (callback != null) {
                            callback.onFailed();
                        }
                    }
                }
            }).start();
        }
    }

    public static int checkIfMobileDataEnabled(Context appCxt, ConnectivityManager connectivityManager) {
        if (PermissionManager.checkPermission(appCxt, Manifest.permission.ACCESS_NETWORK_STATE)) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (null == networkInfo) {
                //当前无网络连接
                return NO_NETWORK;
            }
            if (networkInfo.isConnected()
                    && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE) {
                //当前已连接网络，但网络类型不是mobile data，需判断mobile data是否已打开
                boolean mobileDataEnable = false;
                try {
                    Method getMobileDataEnabled = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
                    getMobileDataEnabled.setAccessible(true);
                    mobileDataEnable = (boolean) getMobileDataEnabled.invoke(connectivityManager);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mobileDataEnable ? NETWORK_WITH_MOBILE_DATA : NETWORK_WITHOUT_MOBILE_DATA;
            } else if (networkInfo.isConnected()
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                //当前已连接网络，且网络类型为mobile data
                return NETWORK_ONLY_MOBILE_DATA;
            }
        }
        return NO_NETWORK;
    }

    /**
     * method
     *
     * @param hostname
     * @return -1 if the host doesn't exists, elsewhere its translation to an integer
     */
    public static int lookupHost(String hostname) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            return -1;
        }
        byte[] addrBytes;
        int addr;
        addrBytes = inetAddress.getAddress();
        addr = ((addrBytes[3] & 0xff) << 24)
               | ((addrBytes[2] & 0xff) << 16)
               | ((addrBytes[1] & 0xff) << 8)
               | (addrBytes[0] & 0xff);
        return addr;
    }

    /**
     * This method extracts from address the hostname
     *
     * @param url eg. http://some.where.com:8080/sync
     * @return some.where.com
     */
    public static String extractAddressFromUrl(String url) {
        String urlToProcess = null;

        // find protocol
        int protocolEndIndex = url.indexOf("://");
        if (protocolEndIndex > 0) {
            urlToProcess = url.substring(protocolEndIndex + 3);
        } else {
            urlToProcess = url;
        }

        // If we have port number in the address we strip everything
        // after the port number
        int pos = urlToProcess.indexOf(':');
        if (pos >= 0) {
            urlToProcess = urlToProcess.substring(0, pos);
        }

        // If we have resource location in the address then we strip
        // everything after the '/'
        pos = urlToProcess.indexOf('/');
        if (pos >= 0) {
            urlToProcess = urlToProcess.substring(0, pos);
        }

        // If we have ? in the address then we strip
        // everything after the '?'
        pos = urlToProcess.indexOf('?');
        if (pos >= 0) {
            urlToProcess = urlToProcess.substring(0, pos);
        }
        return urlToProcess;
    }
}
