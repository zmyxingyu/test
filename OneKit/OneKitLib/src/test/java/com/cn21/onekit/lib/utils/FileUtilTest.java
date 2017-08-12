package com.cn21.onekit.lib.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import com.cn21.onekit.core.utils.FileUtil;
import com.cn21.onekit.lib.BuildConfig;

/**
 * Created by Administrator on 2017/8/1.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FileUtilTest {
    @Test
    public void readFile(){
        String path = "F:\\a_temp_work\\test\\unzip\\manifest.json";
        String str = FileUtil.readFile(path);
        Assert.assertNotNull(str);
        String sourceMd5Json = UpdateResourcesUtils.parseJsonObjectByName(str, "sdk_source_md5");
        Assert.assertNotNull(sourceMd5Json);
        System.out.println(sourceMd5Json);
        //��֤��Դ����ÿ���ļ�
        Map<String, String> serviceFileMd5Map = UpdateResourcesUtils.parseFileMd5ToMap(sourceMd5Json);

        String unzipPath = "F:\\a_temp_work\\test\\unzip\\";
        boolean b = UpdateResourcesUtils.verifyEveryFile(serviceFileMd5Map, unzipPath);
        Assert.assertTrue(b);
    }

    @Test
    public void testZipFileRead() {
        String zip = "F:\\a_temp_work\\test\\zip\\h5.zip";
        String unzip = "F:\\a_temp_work\\test\\unzip\\";
        Assert.assertTrue(FileUtil.zipFileRead(zip, unzip));
    }

    @Test
    public void testDelete(){
        File f = new File("F:\\a_temp_work\\test\\unzip\\NTRIPClient");

        String test = "F:\\a_temp_work\\test\\unzip\\NTRIPClient";
        String p = test.replaceAll("\\\\","/");
        System.out.println(p);
//        Assert.assertTrue(FileUtil.delete(f));
    }

    @Test
    public void testDownload() {
        String unzip = "F:\\a_temp_work\\test\\unzip\\XWalkRuntimeLibLzma.apk";
        String url = "http://10.16.32.201:4000/download/XWalkRuntimeLibLzma.apk?arch=arm64-v8a";
        Assert.assertTrue(FileUtil.downLoad(url, new File(unzip)));
    }

    @Test
    public void testGetMd5ByFile() throws FileNotFoundException {
        String unzip = "F:\\a_temp_work\\test\\unzip\\XWalkRuntimeLibLzma.apk";
        String md5 = FileUtil.getMd5ByFile(new File(unzip));
        Assert.assertNotNull(md5);
        Assert.assertEquals("a61d7727e80e1e8c048bdd4ea3717113",md5);
        System.out.println(md5);

    }

    @Test
    public void test() {
        File f = new File("F:\\a_temp_work\\test\\zip\\h5.zip");
        FileUtil.downLoad("http://10.16.32.212:3000/download/DB8CF9F16BDC4DF9B1B0FEA998A0EFD1", f);
    }
}
