package com.cn21.onekit.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Toast;

import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.lib.update.ResoucePackageManager;

public class MainActivity extends OneKitActivity {
    private ActionBar mActionBar;

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    private void initActionBar() {
        //DEMO中使用actionbar作为顶部的导航栏
        mActionBar = getSupportActionBar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
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

    public void startJsBridge(View view) {
        Intent i = new Intent(this, JsBridgeActivity.class);
        startActivity(i);
    }

    public void startResourcePage(View view) {
        Intent i = new Intent(this, ResourceActivity.class);
        startActivity(i);
    }

}
