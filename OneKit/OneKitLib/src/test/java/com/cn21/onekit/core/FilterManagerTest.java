package com.cn21.onekit.core;

import android.net.Uri;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by zhangmy on 2017/9/1.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 21)
public class FilterManagerTest {
    FilterManager mFilterManager;
    private String HOST = "www.cn21.com";
    private String NET_SCHEME = "http://";
    private String FILE_SCHEME = "file://";
    private String path_1 = "/main/index.htm";
    private String path_2 = "/js/main.js";
    private String parm_1 = "?account=123456";
    private String parm_2 = "#fragment";


    @Before
    public void setUp() {
        mFilterManager = FilterManager.getInstance();

        String source = NET_SCHEME + HOST + path_1;
        String mapping = FILE_SCHEME + path_1;
        System.out.println(source + " | " + mapping);
        mFilterManager.addUrlMapping(source, mapping);

        source = NET_SCHEME + HOST + path_2;
        mapping = FILE_SCHEME + path_2;
        System.out.println(source + " | " + mapping);
        mFilterManager.addUrlMapping(source, mapping);

//        source = NET_SCHEME + HOST + parm_2;
//        mapping = FILE_SCHEME + path_2;
//        System.out.println(source + " | " + mapping);
//        mFilterManager.addUrlMapping(source, mapping);


    }

    @Test
    public void testAdd() {
        String source = NET_SCHEME + HOST + parm_2;
        Uri uri = Uri.parse(source);
        Assert.assertNotNull(uri);
        System.out.println(source + " | " + uri.toString());
    }

    /*    http://www.cn21.com/main/index.htm | file:///main/index.htm
        http://www.cn21.com/js/main.js | file:///js/main.js*/
    @Test
    public void testFilter2LocalUrl() {
        String source = "http://www.cn21.com/main/index.htm";
        Assert.assertEquals("file:///main/index.htm", mFilterManager.filter2LocalUrl(source));
        source = "http://www.cn21.com/main/index.htm" + parm_1;
        Assert.assertEquals("file:///main/index.htm" + parm_1, mFilterManager.filter2LocalUrl(source));
        source = "http://www.cn21.com/main/index.htm?";
        Assert.assertEquals("file:///main/index.htm", mFilterManager.filter2LocalUrl(source));

        source = "http://www.cn21.com/js/main.js";
        Assert.assertEquals("file:///js/main.js", mFilterManager.filter2LocalUrl(source));
    }
}
