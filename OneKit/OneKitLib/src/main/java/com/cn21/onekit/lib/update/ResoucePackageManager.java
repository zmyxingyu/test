package com.cn21.onekit.lib.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cn21.onekit.lib.update.net.NetAccessor;
import com.cn21.onekit.lib.utils.Constants;
import com.cn21.onekit.lib.utils.DefaultShared;
import com.cn21.onekit.core.utils.FileUtil;
import com.cn21.onekit.lib.utils.UpdateResourcesUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/5.
 */

public class ResoucePackageManager extends BaseManager {

    public static final String assertAutoLoginUrl = "file:///android_asset/v2.14/index.html";  //assert目录下面免密页面路径


    private static final String TAG = ResoucePackageManager.class.getSimpleName();

    private static String requestWebviewUrl = null;

    public static String getRequestWebviewUrl() {
        if (TextUtils.isEmpty(requestWebviewUrl)) {
            return assertAutoLoginUrl;
        }
        return requestWebviewUrl;
    }

    private ResoucePackageManager() {
    }

    private static ResoucePackageManager instance = null;

    public static ResoucePackageManager getInstance() {
        if (instance == null) {
            synchronized (ResoucePackageManager.class) {
                if (instance == null) {
                    instance = new ResoucePackageManager();
                }
            }
        }
        return instance;
    }

    /**
     * 预校验
     *
     * @return
     */
    private void preLoadResouces(Context context) {
        try {
            String unZipPath = DefaultShared.getString(context, Constants.KEY_UPDATE_UNZIP_PATH, "");
            if (!TextUtils.isEmpty(unZipPath)) {
                File unZipDir = new File(unZipPath);
                if (!unZipDir.exists()) {
                    Log.d(TAG, "preLoadResouces -- > 解压目录不存在");
                    return;
                }

                //验证资源包的每个文件
                String sourceMd5Json = DefaultShared.getString(context, Constants.KEY_UPDATE_SOURCE_MD5_JSON, "");
                Map<String, String> serviceFileMd5Map = UpdateResourcesUtils.parseFileMd5ToMap(sourceMd5Json);
                if (!UpdateResourcesUtils.verifyEveryFile(serviceFileMd5Map, unZipPath)) {
                    Log.d(TAG, "preLoadResouces -- >  校验解压目录下的文件失败");
                    return;
                }
                String startUrl = DefaultShared.getString(context, Constants.KEY_START_URL, "");
                if (TextUtils.isEmpty(startUrl)) {
                    requestWebviewUrl = null;
                } else {
                    requestWebviewUrl = "file://" + unZipPath + startUrl;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新本地化资源文件
     */
    public void init(final Context context, final String appId, final String appSecret) {
        //检查是否需要更新
        execute(new Runnable() {
            @Override
            public void run() {
                preLoadResouces(context);
                String versionCodePara = DefaultShared.getString(context, Constants.KEY_UPDATE_VERSION_CODE, "v0");
                DefaultShared.putString(context, Constants.KEY_UPDATE_OLD_VERSION_CODE, versionCodePara);
                if (UpdateResourcesUtils.isNeedToUpdate(context)) {
                    checkUpdateResource(context, appId, versionCodePara);
                }
                deleteOldResources(context);
            }
        });
    }

    /**
     * 删除旧的zip和解压文件
     *
     * @param context
     */
    private void deleteOldResources(Context context) {
        String older_version_code = DefaultShared.getString(context, Constants.KEY_UPDATE_OLD_VERSION_CODE, "v0");
        String new_version_code = DefaultShared.getString(context, Constants.KEY_UPDATE_VERSION_CODE, "v0");
        if (!TextUtils.equals(older_version_code, "v0") && !TextUtils.equals(new_version_code, "v0") &&
                !TextUtils.equals(older_version_code, new_version_code)) {
            FileUtil.delete(UpdateResourcesUtils.createNewResourceFile(context, older_version_code));
            FileUtil.delete(new File(UpdateResourcesUtils.getUnzipResourcePath(context, older_version_code)));
        }
    }

    private void checkUpdateResource(Context context, String appId, String resourceVersion) {
        UpdateResourceResModel model = NetAccessor.checkUpdateResource(context, appId, resourceVersion);
        if (model != null && model.result == 0 && !TextUtils.isEmpty(model.resourceVersion)) {
            //需要下载资源包
            downloadResourcePackage(context, model);
        }
    }

    private void downloadResourcePackage(Context context, UpdateResourceResModel model) {
        File resourceFile = UpdateResourcesUtils.createNewResourceFile(context, model.resourceVersion);
        if (resourceFile == null) {
            Log.i(TAG, "创建资源文件失败");
            return;
        }
        boolean isSuccess = FileUtil.downLoad(model.resourceUrl, resourceFile);//根据平台返回的资源路径，下载最新的本地资源包
        if (isSuccess) {
            Log.i(TAG, "下载资源文件成功");
            verifyAndUnzipResourceFile(context, model, resourceFile);
        } else {
            Log.d(TAG, "下载资源文件失败");
        }
    }

    private void verifyAndUnzipResourceFile(Context context, UpdateResourceResModel model, File resourceFile) {
        //校验下载的资源包
        boolean verifyResourceFile = UpdateResourcesUtils.verifyFiles(model.md5, resourceFile);
        if (!verifyResourceFile) {
            Log.d(TAG, "校验资源包失败");
            FileUtil.delete(resourceFile);//删除下载的zip资源包
            return;
        }

        //解压下载的资源包
        String unZipPath = UpdateResourcesUtils.getUnzipResourcePath(context, model.resourceVersion);
        Boolean unzipResourcesFlag = UpdateResourcesUtils.unzipResources(resourceFile, unZipPath);
        if (!unzipResourcesFlag) {
            Log.d(TAG, "解压资源包失败");
            FileUtil.delete(resourceFile);//删除下载的zip资源包
            FileUtil.delete(new File(unZipPath));//删除已经解压的文件
            return;
        }


        //读取配置清单的json
        String manifestJson = FileUtil.readFile(unZipPath + "/" + Constants.MANIFEST_FILE_NAME);
        String sourceMd5Json = UpdateResourcesUtils.parseJsonObjectByName(manifestJson, "sdk_source_md5");
        String urlMappingJson = UpdateResourcesUtils.parseJsonObjectByName(manifestJson, "sdk_url_mapping");

        String startUrl = UpdateResourcesUtils.parseStringValueByName(manifestJson, "start_url");

        //验证资源包的每个文件
        Map<String, String> serviceFileMd5Map = UpdateResourcesUtils.parseFileMd5ToMap(sourceMd5Json);
        boolean verifyEveryFile = UpdateResourcesUtils.verifyEveryFile(serviceFileMd5Map, unZipPath);
        if (verifyEveryFile) {
            saveUpdateInfo(context, model.resourceVersion, sourceMd5Json, unZipPath, startUrl);
            UpdateResourcesUtils.saveUrlMapping(context, urlMappingJson);
        } else {
            Log.d(TAG, "校验资源包中每个文件失败");
            FileUtil.delete(resourceFile);//删除下载的zip资源包
            FileUtil.delete(new File(unZipPath));//删除已经解压的文件
        }
    }

    private static void saveUpdateInfo(Context context, String versionCode, String sourceMd5Json, String unZipPath, String startUrl) {

        Map<String, Object> saveSharedParams = new HashMap<>();
        saveSharedParams.put(Constants.KEY_UPDATE_SUCCESS_FLAG, true);
        saveSharedParams.put(Constants.KEY_UPDATE_SUCCESS_TIME, System.currentTimeMillis());
        saveSharedParams.put(Constants.KEY_UPDATE_SOURCE_MD5_JSON, sourceMd5Json);
        saveSharedParams.put(Constants.KEY_UPDATE_VERSION_CODE, versionCode);
        saveSharedParams.put(Constants.KEY_UPDATE_UNZIP_PATH, unZipPath);
        saveSharedParams.put(Constants.KEY_START_URL, startUrl);
        DefaultShared.putMap(context, saveSharedParams);
        requestWebviewUrl = "file://" + unZipPath + startUrl;
        Log.d(TAG, "更新资源包成功");
    }

}
