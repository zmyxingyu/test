package com.cn21.onekit.lib.common;

import android.content.Intent;

/**
 * Created by Administrator on 2017/7/26.
 */
public interface ActivityCallback {
    void onActivityResume();

    void onActivityPause();

    void onActivityDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
