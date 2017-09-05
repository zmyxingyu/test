package com.cn21.onekit.core;

/**
 * Created by Administrator on 2017/8/17.
 */
public interface OneKitInitListener {
    void onInitOneKitResult(boolean success);

    void onDownloadOneKitResult(boolean success);

    void onXWalkUpdateStarted();
}
