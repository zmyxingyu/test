package com.cn21.onekit.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cn21.ued.apm.util.UEDAgent;

/**
 * Created by Administrator on 2017/7/26.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UEDAgent.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UEDAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UEDAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UEDAgent.onStop(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        UEDAgent.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        UEDAgent.onHomeKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }
}
