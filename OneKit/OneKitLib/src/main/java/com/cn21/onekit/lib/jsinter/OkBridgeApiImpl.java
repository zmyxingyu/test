package com.cn21.onekit.lib.jsinter;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.cn21.onekit.core.OneKitProvider;
import com.cn21.onekit.lib.common.ActivityCallback;
import com.cn21.onekit.lib.update.net.HttpConnUtil;
import com.cn21.onekit.lib.update.net.ResponseInfo;
import com.cn21.onekit.lib.utils.BitmapUtils;
import com.cn21.onekit.lib.utils.GetFilePathUtils;
import com.cn21.onekit.lib.utils.LogUtil;
import com.cn21.onekit.lib.utils.MobileNetManager;
import com.cn21.onekit.lib.utils.NetworkUtils;
import com.cn21.onekit.lib.utils.PermissionManager;
import com.cn21.onekit.lib.utils.SharePreferenceUtil;
import com.cn21.onekit.lib.utils.ThreadUtils;
import com.cn21.onekit.lib.utils.TransportUtils;

/**
 * Created by xuxd on 2017/7/24.
 */

public final class OkBridgeApiImpl implements OkBridgeApi, ActivityCallback {
    private static final int CHOOSE_IMG_FROM_ALBUM = 1010;
    private static final int CHOOSE_IMG_FROM_CAMERA = CHOOSE_IMG_FROM_ALBUM + 1;
    private static final int WRITE_SETTINGS_REQUEST_CODE = CHOOSE_IMG_FROM_ALBUM + 2;

    private volatile Activity mContext;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private volatile OneKitJsCallback mOneKitJsCallback;
    private volatile OneKitProvider mOneKitProvider;

    private String mCameraImgPath;
    private String mSizeType;
    private int mCompressRatio = 1;

    public OkBridgeApiImpl(Activity context) {
        this.mContext = context;
    }

    public void setOneKitJsCallback(OneKitJsCallback OneKitJsCallback) {
        this.mOneKitJsCallback = OneKitJsCallback;
    }

    public void setOneKitProvider(OneKitProvider oneKitProvider) {
        this.mOneKitProvider = oneKitProvider;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public String checkJsApi() {
        Method[] methods = OkBridgeApi.class.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject array = new JSONObject();
                for (Method m : methods) {
                    array.put(m.getName(), true);
                }
                jsonObject.put("checkResult", array);
                jsonObject.put("errMsg", "");
                return jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "{\"checkResult\":{},\"errMsg\":\"no js api\"}";
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public String getNetworkType() {
        if (!PermissionManager.checkPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE)) {
            return null;
        }
        return NetworkUtils.getNetworkType(mContext);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void logout() {
        if (mOneKitJsCallback != null) mOneKitJsCallback.logout();
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void chooseImage(final int compressRatio, final String sizeType, final String sourceType) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mContext.isFinishing()) {
                    return;
                }
                mCompressRatio = compressRatio;
                mSizeType = sizeType;
                if ("album".equals(sourceType)) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    try {
                        mContext.startActivityForResult(intent, CHOOSE_IMG_FROM_ALBUM); // 打开相册
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if ("camera".equals(sourceType)) {
                    chooseImgFromCamera();
                }
            }
        });
    }

    //打开相机
    public void chooseImgFromCamera() {
        String saveDir = "";
        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(storageState)) {
            saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath();
            new File(saveDir).mkdirs();
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(saveDir)) {
            LogUtil.d("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //照片命名
        String fileName = timeStamp + ".jpg";
        File out = new File(saveDir, fileName);
        Uri uri = Uri.fromFile(out);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        try {
            mContext.startActivityForResult(intent, CHOOSE_IMG_FROM_CAMERA);
            mCameraImgPath = out.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void previewImage(final String imagePath) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                File file = new File(imagePath);
                if (file.exists()) {
                    try {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "image/*");
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void showNavigationBar() {
        if (mOneKitJsCallback != null) mOneKitJsCallback.showNavigationBar();
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void hideNavigationBar() {
        if (mOneKitJsCallback != null) mOneKitJsCallback.hideNavigationBar();
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setNavigationBarTitle(String title) {
        if (mOneKitJsCallback != null) mOneKitJsCallback.setNavigationBarTitle(title);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setNavigationBarColor(String color) {
        if (mOneKitJsCallback != null) mOneKitJsCallback.setNavigationBarColor(color);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setViewBgColor(final String color) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOneKitProvider != null) {
                    try {
                        mOneKitProvider.setBackgroundColor(Color.parseColor(color));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setScreenBrightness(final int brightness) {
        if(Build.VERSION.SDK_INT >= 23 && !Settings.System.canWrite(mContext.getApplicationContext())){
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + mContext.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        mContext.startActivityForResult(intent, WRITE_SETTINGS_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        try {
            //设置为手动调节亮度
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            //设置亮度
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightness < 0 ? 0 : ((brightness > 255) ? 255 : brightness));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public int getScreenBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setKeepScreenOn() {
        if (mContext instanceof Activity) {
            final Activity activity = (Activity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!activity.isFinishing()
                            && activity.getWindow() != null) {
                        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }
            });
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void vibrate(int param) {
        if (!PermissionManager.checkPermission(mContext, Manifest.permission.VIBRATE)) {
            return;
        }
        Vibrator vibrator = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null &&
            vibrator.hasVibrator()) {
            vibrator.cancel();
            vibrator.vibrate(param);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setClipboardData(String content) {
        ClipboardManager clipboardManager =
            (ClipboardManager) mContext.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText(content, content);
            clipboardManager.setPrimaryClip(clipData);
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public String getClipboardData() {
        ClipboardManager clipboardManager =
            (ClipboardManager) mContext.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null) {
                final int count = clipData.getItemCount();
                return count > 0? String.valueOf(clipData.getItemAt(count - 1).getText()): null;
            }
        }
        return null;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void makePhoneCall(final String phoneNum) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void setStorage(String key, String obj) {
        SharePreferenceUtil.saveKeyValue(mContext, key, obj);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public String getStorage(String key) {
        return SharePreferenceUtil.getString(mContext, key);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void removeStorage(String key) {
        SharePreferenceUtil.removeKey(mContext, key);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void clearStorage() {
        SharePreferenceUtil.clearAll(mContext);
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void showShortToast(final String msg) {
        mMainHandler.post(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void showLongToast(final String msg) {
        mMainHandler.post(new Runnable(){
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void systemShare(final String title, final String content, final String imageUrl) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_SEND);
                if (!TextUtils.isEmpty(content)) {
                    intent.putExtra(Intent.EXTRA_TEXT, content);
                    intent.setType("text/plain");
                    try {
                        mContext.startActivity(Intent.createChooser(intent, title == null ? "Share" : title));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (!TextUtils.isEmpty(imageUrl)) {
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl));
                    intent.setType("image/*");
                    try {
                        mContext.startActivity(Intent.createChooser(intent, title == null ? "Share" : title));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public String getSystemInfo() {
        final Context appCxt = mContext.getApplicationContext();
        final Point point = new Point();
        WindowManager wm = (WindowManager) appCxt.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        Locale defLocale = Locale.getDefault();
        final String language = defLocale.getLanguage();
        final String country = defLocale.getCountry();
        String result = null;
        try {
            JSONObject object = new JSONObject();
            object.put("model", Build.MODEL);
            object.put("screenWidth", point.x);
            object.put("screenHeight", point.y);
            object.put("language", language + (TextUtils.isEmpty(country) ? "" : ("-" + country)));
            object.put("version", appCxt.getPackageManager().getPackageInfo(appCxt.getPackageName(), 0).versionName);
            object.put("system", Build.VERSION.RELEASE);
            object.put("platform", "Android");
            result = object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public String getLoginInfo() {
        if (mOneKitJsCallback != null) {
            Map<String, String> map = mOneKitJsCallback.getLoginInfo();
            if (map != null && !map.isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        jsonObject.put(entry.getKey(), entry.getValue());
                    }
                    return jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void request(String method, final String url, final String data) {
        final Context context = mContext.getApplicationContext();
        if ("get".equalsIgnoreCase(method)) {
            ThreadUtils.createThread("http_get", new Runnable() {
                @Override
                public void run() {
                    final String result = HttpConnUtil.doGet(context, url);
                    if (TextUtils.isEmpty(result)) {
                        callJsHttpRequestResult(false, null, "unknown error");
                    } else {
                        callJsHttpRequestResult(true, result, null);
                    }
                }
            }).start();
        } else if ("post".equalsIgnoreCase(method)) {
            ThreadUtils.createThread("http_post", new Runnable() {
                @Override
                public void run() {
                    final ResponseInfo result = HttpConnUtil.doPost(context, url, null, data, null);
                    if (result != null && result.responseCode == 200) {
                        callJsHttpRequestResult(true, result.result, null);
                    } else {
                        callJsHttpRequestResult(false, null, "unknown error");
                    }
                }
            }).start();
        }
    }

    private void callJsHttpRequestResult(final boolean success, final String data, final String errMsg) {
        if (mOneKitProvider != null) {
            String json = "{}";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result", success ? 0 : -1);
                jsonObject.put("data", data);
                jsonObject.put("errMsg", success ? "" : errMsg);
                json = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            callJsMethod(String.format("javascript:onRequestResult(%s)", json));
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void uploadFile(final String url, final String filePath) {
        ThreadUtils.createThread("uploadFile", new Runnable() {
            @Override
            public void run() {
                //当出错时，resp才有值
                final String resp = TransportUtils.uploadFile(url, new File(filePath), null);
                callJsUploadResult(resp == null, url, filePath, resp != null ? resp : "success");
            }
        }).start();
    }

    private void callJsUploadResult(final boolean success, final String url, final String filePath, final String errMsg) {
        if (mOneKitProvider != null) {
            String json = "{}";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result", success ? 0 : -1);
                jsonObject.put("url", url);
                jsonObject.put("filePath", filePath);
                jsonObject.put("errMsg", success ? "" : errMsg);
                json = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            callJsMethod(String.format("javascript:onUploadFileResult(%s)", json));
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void downloadFile(final String url, final String fileName) {
        final Context context = mContext.getApplicationContext();
        ThreadUtils.createThread("download", new Runnable() {
            @Override
            public void run() {
                String name = TextUtils.isEmpty(fileName) ?
                        new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".oneKit":
                        fileName;
                String path = null;
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    dir.mkdirs();
                    path = new File(dir, name).getAbsolutePath();
                } else {
                    context.getCacheDir().mkdirs();
                    path = new File(context.getCacheDir(), name).getAbsolutePath();
                }
                //当出错时，resp才有值
                final String resp = TransportUtils.doDownloadFile(url, path);
                callJsDownloadResult(resp == null, url, path, name,
                        resp != null ? resp : "success");
            }
        }).start();
    }

    private void callJsDownloadResult(final boolean success, final String url,
                                      final String filePath, final String fileName, final String errMsg) {
        if (mOneKitProvider != null) {
            String json = "{}";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result", success ? 0 : -1);
                jsonObject.put("url", url);
                jsonObject.put("filePath", filePath);
                jsonObject.put("fileName", fileName);
                jsonObject.put("errMsg", success ? "" : errMsg);
                json = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            callJsMethod(String.format("javascript:onDownloadFileResult(%s)", json));
        }
    }

    @JavascriptInterface
    @org.xwalk.core.JavascriptInterface
    @org.chromium.content.browser.JavascriptInterface
    @Override
    public void selectMobileDataToRequest(final String url) {
        final Context appCxt = mContext.getApplicationContext();
        MobileNetManager.forceMobileData(appCxt, url, new MobileNetManager.OnMobileSelectCallback() {
            @Override
            public void onSuccess(String url, Object network) {
                callJsSelectMobileDataResult(true, url, "");
                if (mOneKitJsCallback != null) {
                    mOneKitJsCallback.onSelectMobileData(0, url, network);
                }
            }

            @Override
            public void onFailed() {
                callJsSelectMobileDataResult(false, url, "select mobile data failed");
                if (mOneKitJsCallback != null) {
                    mOneKitJsCallback.onSelectMobileData(-1, url, null);
                }
            }
        });
    }

    private void callJsSelectMobileDataResult(final boolean success, final String url, final String errMsg) {
        if (mOneKitProvider != null) {
            String json = "{}";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result", success ? 0 : -1);
                jsonObject.put("url", url);
                jsonObject.put("errMsg", success ? "" : errMsg);
                json = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            callJsMethod(String.format("javascript:onSelectMobileDataResult(%s)", json));
        }
    }

    //******************Activity 相关回调 start***********************
    @Override
    public void onActivityResume() {

    }

    @Override
    public void onActivityPause() {

    }

    @Override
    public void onActivityDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CHOOSE_IMG_FROM_ALBUM == requestCode) {
            //从相册选择图片
            if (data != null && resultCode == Activity.RESULT_OK) {
                getImgFromAlbum(data);
            }
            resetChooseImgParams();
        } else if (CHOOSE_IMG_FROM_CAMERA == requestCode) {
            //从相机拍照选择图片
            if (!TextUtils.isEmpty(mCameraImgPath) && resultCode == Activity.RESULT_OK) {
                if ("compressed".equals(mSizeType) && mCompressRatio > 1) {
                    compressImg("camera", mCameraImgPath, mCompressRatio);
                } else {
                    callJsChooseImgResult(true, mCameraImgPath);
                }
            }
            resetChooseImgParams();
        } else if (WRITE_SETTINGS_REQUEST_CODE == requestCode) {
            boolean success = Build.VERSION.SDK_INT >= 23 && Settings.System.canWrite(mContext);
            LogUtil.d("request write_settings permission result : %s",
                    success ? "success" : "failed");
        }
    }
    //******************Activity 相关回调 end***********************

    private void callJsMethod(final String jsMethod) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mOneKitProvider != null) {
                    mOneKitProvider.loadUrl(jsMethod);
                }
            }
        });
    }

    private void resetChooseImgParams() {
        mCameraImgPath = null;
        mSizeType = null;
        mCompressRatio = 1;
    }

    private void callJsChooseImgResult(boolean success, String path) {
        if (mOneKitProvider != null) {
            String json = "{}";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result", success ? 0 : -1);
                jsonObject.put("path", path);
                jsonObject.put("errMsg", success ? "" : "choose img failed");
                json = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            callJsMethod(String.format("javascript:onChooseImageResult(%s)", json));
        }
    }

    private void compressImg(final String tag, final String sourcePath, final int compressRatio) {
        ThreadUtils.createThread("compressImg_" + tag, new Runnable() {
            @Override
            public void run() {
                String path = null;
                final String name = System.currentTimeMillis() + "_ok_compressed_" + compressRatio + ".jpg";
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    dir.mkdirs();
                    path = new File(dir, name).getAbsolutePath();
                } else {
                    path = new File(new File(sourcePath).getParent(), name).getAbsolutePath();
                }
                BitmapUtils.compressBtimap(sourcePath, path, compressRatio);
                callJsChooseImgResult(true, path);
            }
        }).start();
    }

    private void getImgFromAlbum(Intent data) {
        Uri originalUri = data.getData();//获得图片的uri
        LogUtil.d("get img uri : " + originalUri);
        if (originalUri == null) {
            callJsChooseImgResult(false, null);
            return;
        }
        String photoPath = GetFilePathUtils.getPath(mContext, originalUri);
        if (TextUtils.isEmpty(photoPath)) {
            callJsChooseImgResult(false, null);
            return;
        }

        if ("compressed".equals(mSizeType) && mCompressRatio > 1) {
            compressImg("album", photoPath, mCompressRatio);
        } else {
            callJsChooseImgResult(true, photoPath);
        }
    }
}
