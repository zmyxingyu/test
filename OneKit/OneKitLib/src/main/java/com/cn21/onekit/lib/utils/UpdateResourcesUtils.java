package com.cn21.onekit.lib.utils;

import android.content.Context;
import android.text.TextUtils;

import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.core.utils.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 主要是更新资源包模块相关方法
 * 作者：${fuyanan} on 2017/4/10 18:13
 */

public class UpdateResourcesUtils {
    private static final String TAG = UpdateResourcesUtils.class.getSimpleName();

    private static final String RESOURCE_ZIP_DIR_NAME = "/onekit/zip/";
    private static final String RESOURCE_UNZIP_DIR_NAME = "/onekit/h5/";

    /**
     * 检查是否需要去更新最新的资源包
     *
     * @return
     */
    public static boolean isNeedToUpdate(Context context) {
        boolean updateSuccessFlag = DefaultShared.getBoolean(context, Constants.KEY_UPDATE_SUCCESS_FLAG, false);
        long updateSuccessTime = DefaultShared.getLong(context, Constants.KEY_UPDATE_SUCCESS_TIME, 0L);
        if (!updateSuccessFlag || (updateSuccessFlag && (System.currentTimeMillis() - updateSuccessTime > OneKitManager.getInstance().getOneKitConfig().getOneKitResourceInvalidTime()))) {
            return true;
        }
        return false;
    }

    public static File createNewResourceFile(Context context,String versionCode){
        try{
            String resourceFileName =  versionCode + ".zip";
            File resourceDir = new File(context.getFilesDir() + RESOURCE_ZIP_DIR_NAME);
            if(!resourceDir.exists()){
                resourceDir.mkdirs();
            }
            File resourceFile = new File(resourceDir , resourceFileName);
            if(resourceFile.exists()){
                resourceFile.delete();
            }
            resourceFile.createNewFile();
            return resourceFile;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验资源包中的每一个资源文件
     *
     * @param serviceFileMd5Map
     * @param unZipPath
     * @return
     */
    public static boolean verifyEveryFile(Map<String, String> serviceFileMd5Map, String unZipPath) {
        try {
            Map<String, String> localMap = getLocalMap(unZipPath);
            return compareMap(serviceFileMd5Map, localMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 校验整个资源包的MD5值
     *
     * @param serviceMd5
     * @param file
     * @return true：校验成功，false:校验失败
     */
    public static Boolean verifyFiles(String serviceMd5, File file) {
        try {
            String decryserverMd5 = RSAUtils.decryptWithBase64(serviceMd5, RSAUtils.getRSAPrivateKey());
            String downloadMd5 = FileUtil.getMd5ByFile(file);
            if (downloadMd5.equals(decryserverMd5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解压整个资源包
     *
     * @param resourceFile
     * @param unZipPath
     * @return true：解压成功，false:解压失败
     */
    public static Boolean unzipResources(File resourceFile, String unZipPath) {
        if (FileUtil.zipFileRead(resourceFile.getAbsolutePath() , unZipPath)) {
            return true;
        }
        return false;
    }

    /**
     * 根据versionCode 获得解压路径
     * @param context
     * @param versionCode
     * @return
     */
    public static String getUnzipResourcePath(Context context , String versionCode){
        return context.getFilesDir().getPath() + RESOURCE_UNZIP_DIR_NAME + versionCode +"/";
    }

    /**
     * 获得下载后解压的资源的文件对应的路径名和对应路径的Md5的Map
     *
     * @param unZipPath
     * @return
     */
    public static Map<String, String> getLocalMap(String unZipPath) {
        List list = new ArrayList<String>();
        Map<String, String> localMap = new HashMap();
        try {
            FileUtil.traverseFolder2(new File(unZipPath),list);
            for (int i = 0; i < list.size(); i++) {
                String path = list.get(i).toString();
                localMap.put(path.substring(unZipPath.length(), path.length()), FileUtil.getMd5ByFile(new File(list.get(i).toString())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localMap;
    }
    
    /**
     * 比较两个map相同key所对应的value是否相等
     *
     * @param txtMap
     * @param localMap
     * @return
     */
    public static Boolean compareMap(Map<String, String> txtMap, Map<String, String> localMap) {
        if (txtMap == null || localMap == null) {
            return false;
        }
        if (txtMap != null && localMap != null && txtMap.size() != localMap.size()) {
            return false;
        }
        for (Map.Entry<String, String> entry1 : txtMap.entrySet()) {
            String txtMapValue = entry1.getValue() == null ? "" : entry1.getValue();
            String localMapValue = localMap.get(entry1.getKey()) == null ? "" : localMap.get(entry1.getKey());
            if (!txtMapValue.equals(localMapValue)) {
                return false;
            }
        }
        return true;
    }


    public static String parseJsonObjectByName(String json, String name) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            JSONObject obj = jsonObject.optJSONObject(name);
            if (obj != null) {
                return obj.toString();
            }
        }
        return null;
    }

    public static String parseStringValueByName(String json, String name) {
        if (!TextUtils.isEmpty(json)) {

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonObject != null) {
                return jsonObject.optString(name);
            }
        }
        return null;
    }

    public static void saveUrlMapping(Context context ,String urlMappingJson){
        UrlMappingSharedPref.clear(context); //更新新的资源包必须清除旧映射关系
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(urlMappingJson);
            if(jsonObject == null){
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Iterator iterator = jsonObject.keys();
        Map<String, String> map = new HashMap();
        while (iterator.hasNext()) {
            String key_url = (String) iterator.next();
            String value_localPath = jsonObject.optString(key_url);
            map.put(key_url, value_localPath);
        }
        if(map.size() > 0){
            UrlMappingSharedPref.putMap(context , map);
        }
    }

    /**
     * 读取sdk_source_md5中的键和RSA解压后的值保存到Map中
     * @param json
     * @return
     */
    public static Map<String, String> parseFileMd5ToMap(String json) {
        JSONObject sourceMd5Obj = null;
        try {
            sourceMd5Obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(sourceMd5Obj != null){
            Iterator iterator = sourceMd5Obj.keys();
            Map<String, String> map = new HashMap();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                try {
                    map.put(key, RSAUtils.decryptWithBase64(sourceMd5Obj.optString(key), RSAUtils.getRSAPrivateKey()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return map;
        }
        return null;
    }
}
