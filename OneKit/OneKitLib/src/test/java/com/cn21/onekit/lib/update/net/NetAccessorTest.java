package com.cn21.onekit.lib.update.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import android.content.Context;
import android.os.Build;

import com.cn21.onekit.lib.BuildConfig;
import com.cn21.onekit.lib.update.UpdateResourceResModel;
import com.cn21.onekit.lib.utils.XXTeaUtil;

/**
 * Created by Administrator on 2017/7/26.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NetAccessorTest {
    Context context;
    public static final String TYPE_H5 = "H5";

    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
    }

    @Test
    public void testGetVersion() {
        String version = NetAccessor.getVersion(context);
        System.out.println(version);
        Assert.assertNotNull(version);
    }

    @Test
    public void testGetDeviceId() {
        String version = NetAccessor.getDeviceId(context);
        System.out.println(version);
        Assert.assertNotNull(version);
    }

    @Test
    public void testGetParms() {
        String experct = "sysVersion=21&appVersion=1.0&system=android&appId=aaaaaaaaaaaa&resourceVersion=1&" +
                "sign=&deviceId=dd9a3a37-cfd5-487e-881b-cdec5e106a74&resourceType=H5";
        Map<String, Object> params = new HashMap<>();
        params.put("appId", "aaaaaaaaaaaa");
        params.put("appVersion", "1.0");
        params.put("sysVersion", Build.VERSION.SDK_INT + "");
        params.put("resourceVersion", "1");
        params.put("resourceType", TYPE_H5);
        params.put("system", "android");
        params.put("deviceId", "dd9a3a37-cfd5-487e-881b-cdec5e106a74");
        params.put("sign", "");
        String s = NetAccessor.getUrlParamsByMap(params);
        System.out.println(s);
        Assert.assertEquals(s, experct);
    }

    @Test
    public void testParseUpdateResourceRes() {
        String json = "{\n" +
                "  \"versionCode\": \"2.3.4\",\n" +
                "  \"resourceUrl\": \"http://download.hotfix.com/9daf78f7f0b084ea912dc59995991778/2.3.4/package.zip\",\n" +
                "  \"md5\": \"T/sZsOoIMNLvPxhqF/wBJFuakAh6auLgnvgzqUPbaL8x76wh33A7N4X2tTK1y5+sKA5Ww5eJRwXrfqu5xgc3MCrMhNq8PClxiHBFsLs3zddWRcDMGbH8DK6WqgfNudlLXqm3oY3VPtJPdnu+O5kbLSaAET49IhvSPS46dPu8sI4=\",\n" +
                "  \"msg\": \"Resouce should be update to version 2.3.4\",\n" +
                "  \"result\": 0\n" +
                "}";
        UpdateResourceResModel rm = NetAccessor.parseUpdateResourceRes(json);
        Assert.assertEquals(rm.md5, "T/sZsOoIMNLvPxhqF/wBJFuakAh6auLgnvgzqUPbaL8x76wh33A7N4X2tTK1y5+sKA5Ww5eJRwXrfqu5xgc3MCrMhNq8PClxiHBFsLs3zddWRcDMGbH8DK6WqgfNudlLXqm3oY3VPtJPdnu+O5kbLSaAET49IhvSPS46dPu8sI4=");
        Assert.assertEquals(rm.resourceUrl, "http://download.hotfix.com/9daf78f7f0b084ea912dc59995991778/2.3.4/package.zip");
        Assert.assertEquals(rm.result, 0);
        Assert.assertEquals(rm.resourceVersion, "2.3.4");
    }

    @Test
    public void testCheckUpdateResource() throws UnsupportedEncodingException {
        String UPDATE_RESOURCE_URL = "http://10.20.16.25:3000/api/updateResource?"; //使用本地资源包更新接口
        String parm = "appId=test&appVersion=1.0&sysVersion=6.0&resourceVersion=1.0&resourceType=H5&system=android&deviceId=undefined&account=18148910925";
        String sign = XXTeaUtil.encryptStr(parm, "UTF-8", XXTeaUtil.KEY);
        parm = parm + "&sign=" + sign;

        UpdateResourceResModel urm = NetAccessor.postUpdateResource(context, "http://10.16.32.212:3000/api/updateResource", parm);
        Assert.assertNotNull(urm);
        System.out.println(urm);
    }

    @Test
    public void testCheckUseOneKit(){
        boolean b = false;
        try {
            b = NetAccessor.checkOpenOneKit(context,"http://10.16.33.150:3000/api/getSwitch");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertTrue(b);
    }




}
