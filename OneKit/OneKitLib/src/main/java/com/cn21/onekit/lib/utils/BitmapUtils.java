package com.cn21.onekit.lib.utils;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2017/7/26.
 */
public class BitmapUtils {

    public static void compressBtimap(String sourcePath, String destPath, int ratio) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(sourcePath, opts);

        opts.inJustDecodeBounds = false;
        opts.inSampleSize = ratio < 1 ? 1 : ratio;//设置缩放比例
        Bitmap bitmap = BitmapFactory.decodeFile(sourcePath, opts);
        // 压缩好比例大小后再进行质量压缩
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(destPath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
