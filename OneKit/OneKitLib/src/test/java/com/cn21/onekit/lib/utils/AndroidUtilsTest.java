package com.cn21.onekit.lib.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import android.content.Context;

import com.cn21.onekit.lib.BuildConfig;

/**
 * Created by Administrator on 2017/7/28.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AndroidUtilsTest {
    static Context context;
    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
    }
    @Test
    public void testGetApplicationMataValue(){
        String expect  = "http://10.16.33.15:3000/XWalkRuntimeLib.apk";//"xwalk_apk_url";
        String value = AndroidUtils.getApplicationMataValue(context,"xwalk_apk_url");
        Assert.assertEquals(expect,value);
    }
}
