package com.cn21.onekit.lib.update;

/**
 * 监听下载资源包，过程。需在ResoucePackageManager init前设置
 * Created by zhangmy on 2017/8/17.
 */
public interface ResourceDownloadListener {
    void onResourceDownloadStart();

    void onResourceDownload(boolean success);

    void onResourceCheckMd5(boolean success);

    void onResourceUnzip(boolean success);

    void onResourceCheckFiles(boolean success);

    void onResourceUpdateSuccess();
}
