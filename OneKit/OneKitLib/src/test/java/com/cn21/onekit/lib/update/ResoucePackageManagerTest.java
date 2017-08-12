package com.cn21.onekit.lib.update;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import android.content.Context;

import com.cn21.onekit.lib.utils.Constants;
import com.cn21.onekit.lib.utils.DefaultShared;

/**
 * Created by Administrator on 2017/7/26.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE,sdk = 21)
public class ResoucePackageManagerTest {

    ResoucePackageManager rpm;
    Context context;

    @Before
    public void setUp() {
        rpm = ResoucePackageManager.getInstance();
        context = ShadowApplication.getInstance().getApplicationContext();
    }

    @Test
    public void testPreLoadResouces(){
        Assert.assertNotNull(rpm);
        Assert.assertNotNull(context);
        rpm.preLoadResouces(context);
    }
    public static Method getMethod(Class clazz, String methodName,
                                   final Class[] classes) throws Exception {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }
    @Test
    public void testDeleteOldResources() throws Exception {
        String older_version_code = DefaultShared.getString(context, Constants.KEY_UPDATE_OLD_VERSION_CODE, "v0");
        Assert.assertEquals(older_version_code, "v0");
        DefaultShared.putString(context, Constants.KEY_UPDATE_OLD_VERSION_CODE, "V01");
        DefaultShared.putString(context, Constants.KEY_UPDATE_VERSION_CODE, "V02");
        Method method = getMethod(ResoucePackageManager.class, "deleteOldResources", new Class[]{Context.class});
        method.setAccessible(true);
        method.invoke(rpm, context);
    }

    @Test
    public void testDownloadResourcePackage() throws Exception {
        UpdateResourceResModel usm = new UpdateResourceResModel();
        usm.md5 = "VkVk3k4e+C0wveEc6YP9bFTkX01LGK9rr8kM7FrKlmpJo23RjydV9DtPamM0g+FhZqvkoXpPa5Y+4cHb049yUa5C1BsHz7Zk4xzXgCHzwhalbksGp2CjMmWuxhWMTsoCDu4A3BtB50F2L5WQuDb06zTyRPoYP0fEdm0dvVhiqjc=";
        usm.result = 0;
        usm.resourceUrl = "http://10.16.32.212:3000/download/F5FBF9F64B3948319BDD38FA7ABF2583";
        usm.resourceVersion = "2.3";
        Method method = getMethod(ResoucePackageManager.class, "downloadResourcePackage", new Class[]{Context.class, UpdateResourceResModel.class});
        method.setAccessible(true);
        method.invoke(rpm, context, usm);

    }


}
