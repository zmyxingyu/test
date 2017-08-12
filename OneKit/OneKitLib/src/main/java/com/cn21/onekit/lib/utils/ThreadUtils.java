package com.cn21.onekit.lib.utils;

import android.os.Process;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/7/26.
 */
public class ThreadUtils {
    private static final AtomicInteger COUNT = new AtomicInteger(1);

    public static Thread createThread(String threadName, Runnable r) {
        return new Thread(r, threadName + "_" + COUNT.getAndIncrement()) {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                super.run();
            }
        };
    }

    public static Thread createThread(Runnable r) {
        return new Thread(r, "ok_thread_" + COUNT.getAndIncrement()) {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                super.run();
            }
        };
    }
}
