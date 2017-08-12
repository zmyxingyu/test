package com.cn21.onekit.android;

import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.widget.Toast;

import com.cn21.onekit.android.net.HttpConnUtil;
import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.core.OneKitView;
import com.cn21.onekit.lib.jsinter.OkBridgeApiImpl;
import com.cn21.onekit.lib.jsinter.OneKitJsCallback;

public class JsBridgeActivity extends OneKitActivity implements OneKitJsCallback {
    private ActionBar mActionBar;
    private OneKitView mOneKitView;
    private OkBridgeApiImpl mDefOkBridgeApiImpl;

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsbridge);
        initActionBar();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        mOneKitView = (OneKitView) findViewById(R.id.my_onekit);

//        mOneKitView.loadUrl("http://www.baidu.com");
        mOneKitView.loadUrl("file:///android_asset/test/junitdemo.html");
        mDefOkBridgeApiImpl = new OkBridgeApiImpl(this);
        mDefOkBridgeApiImpl.setOneKitProvider(mOneKitView.getOneKitProvider());
        mDefOkBridgeApiImpl.setOneKitJsCallback(this);
        mOneKitView.getSettings().setJavaScriptEnabled(true);
        mOneKitView.addJavascriptInterface(mDefOkBridgeApiImpl, "ok");

    }

    private void initActionBar() {
        //DEMO中使用actionbar作为顶部的导航栏
        mActionBar = getSupportActionBar();
    }

    @Override
    public void onBackPressed() {
        if (mOneKitView.canGoBack()) {
            mOneKitView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDefOkBridgeApiImpl != null) mDefOkBridgeApiImpl.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDefOkBridgeApiImpl != null) mDefOkBridgeApiImpl.onActivityPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDefOkBridgeApiImpl != null) mDefOkBridgeApiImpl.onActivityDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mDefOkBridgeApiImpl != null) {
            mDefOkBridgeApiImpl.onActivityResult(requestCode, resultCode, data);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                OneKitManager.getInstance().init(this);
            } else {
                // Permission Denied
                Toast.makeText(this, "申请写入SD卡权限失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void showNavigationBar() {
        //显示顶部导航栏
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mActionBar != null) {
                        mActionBar.show();
                    }
                }
            });
        }
    }

    @Override
    public void hideNavigationBar() {
        //隐藏顶部导航栏
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mActionBar != null) {
                        mActionBar.hide();
                    }
                }
            });
        }
    }

    @Override
    public void setNavigationBarTitle(final String title) {
        //设置顶部导航栏title
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mActionBar != null) {
                        mActionBar.setTitle(title);
                    }
                }
            });
        }
    }

    @Override
    public void setNavigationBarColor(final String color) {
        //显示顶部导航栏颜色
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mActionBar != null) {
                        try {
                            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void logout() {
        //退出登录
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(JsBridgeActivity.this, "退出登录", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public Map<String, String> getLoginInfo() {
        //获取登录信息
        Map<String, String> map = new HashMap<>();
        map.put("loginState", "true");
        map.put("userId", "00231456789");
        //map.put("openId", "00231456789");
        return map;
    }

    @Override
    public void onSelectMobileData(int result, final String url, final Object network) {
        final Context appCxt = getApplicationContext();
        if (0 == result) {
            //通过数据网络进行请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = "";
                    try {
                        if (Build.VERSION.SDK_INT >= 21 &&
                                network instanceof android.net.Network) {
                            result = HttpConnUtil.doPost(appCxt, url, null, null,
                                    (android.net.Network)network).result;
                        } else {
                            result = HttpConnUtil.doPost(appCxt, url, null, null, null).result;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("okLib", String.format("request %s --> %s", url, result));
                }
            }).start();
        } else {
            Log.w("okLib", "select mobile data failed");
        }
    }
}
