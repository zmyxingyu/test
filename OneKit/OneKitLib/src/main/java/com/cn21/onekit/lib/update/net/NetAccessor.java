package com.cn21.onekit.lib.update.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.lib.update.ResoucePackageManager;
import com.cn21.onekit.lib.update.UpdateResourceResModel;
import com.cn21.onekit.lib.utils.AndroidUtils;
import com.cn21.onekit.lib.utils.Constants;
import com.cn21.onekit.lib.utils.DefaultShared;
import com.cn21.onekit.lib.utils.XXTeaUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Administrator on 2017/4/5.
 */

public class NetAccessor {

    private static final String TAG = NetAccessor.class.getSimpleName();

    public static final String TYPE_H5 = "H5";



    /**
     * 检查本地资源包是否有更新
     * @param context
     * @param appId
     * @return
     */
    public static UpdateResourceResModel checkUpdateResource(Context context, @NonNull String url, @NonNull String appId, String resourceVersion) {
        String params = getUpdateParms(context, appId, resourceVersion); //本地资源包更新接口参数
        return postUpdateResource(context, url + "?", params);
    }

    public static UpdateResourceResModel checkUpdateResource(Context context, String requestUrl) {
        String result = HttpConnUtil.doGet(context, requestUrl);
        UpdateResourceResModel updateResourceResModel = parseUpdateResourceRes(result);
        return updateResourceResModel;
    }

    public static UpdateResourceResModel postUpdateResource(Context context, String requestUrl, String parms) {
        UpdateResourceResModel updateResourceResModel = null;
        ResponseInfo rinfo = HttpConnUtil.doPost(context, requestUrl, null, parms, null);
        if (rinfo != null && rinfo.responseCode == 200) {
            updateResourceResModel = parseUpdateResourceRes(rinfo.result);
        } else {
            Log.i(TAG, "请求" + requestUrl + "出错！");
        }
        return updateResourceResModel;
    }
    //目前服务端不支持get
    public static UpdateResourceResModel getUpdateResource(Context context, String requestUrl) {
        String result = HttpConnUtil.doGet(context, requestUrl);
        UpdateResourceResModel updateResourceResModel = parseUpdateResourceRes(result);
        return updateResourceResModel;
    }

    /**
     * 服务端开关，开取和关闭使用onekit
     *
     * @param context
     * @param requestUrl
     * @return
     */
    public static boolean checkOpenOneKit(Context context, String requestUrl) throws Exception {
        String str = HttpConnUtil.doGet(context, requestUrl);
        return parseIsOpen(str);
    }

    /**
     * 解析服务端开关返回json
     * @param str
     * @return
     */
    public static boolean parseIsOpen(String str) throws Exception {
        boolean isOpen = true;
        if (!TextUtils.isEmpty(str)) {
            JSONObject job = null;
            try {
                job = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
                throw new Exception("can not resolve response,status of use xwalkview ");
            }
            if (job != null) {
                return job.optBoolean("status");
            }
        } else {
            throw new Exception("null response,status of use xwalkview ");
        }
        return isOpen;
    }
    /**
     * 生成更新资源包接口参数
     *
     * @return
     */
    private static String getUpdateMapParas(Context mContext, String appId, String resourceVersion) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("appVersion", getVersion(mContext));
        params.put("sysVersion", Build.VERSION.SDK_INT + "");
        params.put("resourceVersion", resourceVersion);
        params.put("resourceType", TYPE_H5);
        params.put("system", "android");
        params.put("deviceId", getDeviceId(mContext));
        params.put("sign", "");

        return getUrlParamsByMap(params);
    }

    /**
     * 按特定顺序构造请求参数
     * @param mContext
     * @param appId
     * @param resourceVersion
     * @return
     */
    private static String getUpdateParms(Context mContext, String appId, String resourceVersion) {
        //TODO account should fix
        StringBuffer sb = new StringBuffer();
        sb.append("appId=").append(appId).
                append("&appVersion=").append("1.1.1").//getVersion(mContext)
                append("&sysVersion=").append("6.0.0").//Build.VERSION.SDK_INT
                append("&resourceVersion=").append(resourceVersion).//resourceVersion
                append("&resourceType=").append(TYPE_H5).
                append("&system=").append("android").
                append("&deviceId=").append(getDeviceId(mContext)).
                append("&account=").append(OneKitManager.getInstance().getOnekitRuntime().getAccount());

        String sign = null;
        try {
            sign = XXTeaUtil.encryptStr(sb.toString(), "UTF-8", OneKitManager.getInstance().getOnekitRuntime().getAppSecret());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        sb.append("&sign=").append(sign);
        return sb.toString();
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 获取DeviceId
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String sn = null;
        try {
            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(androidId)) {
                sn =  androidId;
            } else {
                sn = DefaultShared.getString(context ,Constants.KEY_RANDOM_DEVICE_ID, "");
                if(TextUtils.isEmpty(sn)){
                    sn = UUID.randomUUID().toString();
                    DefaultShared.putString(context ,Constants.KEY_RANDOM_DEVICE_ID,sn);
                }
            }
            return sn;
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解析本地资源包更新接口的返回值
     * @return
     */
    public static UpdateResourceResModel parseUpdateResourceRes(String result) {

        if (!TextUtils.isEmpty(result)) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                UpdateResourceResModel updateResourceResModel = new UpdateResourceResModel();
                updateResourceResModel.result = jsonObject.optInt("result");
                updateResourceResModel.msg = jsonObject.optString("msg");
                updateResourceResModel.md5 = jsonObject.optString("md5");
                updateResourceResModel.resourceUrl = jsonObject.optString("resourceUrl");
                updateResourceResModel.resourceVersion = jsonObject.optString("resourceVersion");
                return updateResourceResModel;
            }
        }
        return null;
    }

}

