package com.cn21.onekit.lib.utils;

import java.io.File;
import java.util.Map;

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
 * Created by Administrator on 2017/7/31.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestUpdateResourcesUtils {

    static Context context;
    @Before
    public void setUp() {
        context = ShadowApplication.getInstance().getApplicationContext();
    }

    @Test
    public void testParseJsonObjectByName(){
        String json = "{\n" +
                "  \"sdk_source_md5\": {\n" +
                "    \"css/stylecss\": \"lW4jt4WWl74GKqOTTPMigbhTgia7haNW8Leh7WTUWrJSupklP46ZWH6HrnM1tVhoQjWyQIXQqO+ePRRVXaxKsjhmYo0HGpYniWbIA3rIUSlRE9PqVwpB4RZ6vxfJAJvomhBxkeiJBdfz0dHt3fiAgKliCmSjkBJ1p0eUfdIrGx0=\",\n" +
                "    \"js/appjs\": \"W29GiGvZvkMsgA+XHIeaHO4fh51EeuQrMLwPbRN4OUnapA9v5ubfIUy9YtU4Y+oM14RGkpNaGbqw7b/Lh0WlSOtu/32i0gwRHkhi4+vxzCRo4oQULH2QDKcxxnl/zkv7Z8KtTWWD2aD21tVPx4N1fP8i6/c248uGlB1ppPMRm0U=\",\n" +
                "    \"reviewhtml\": \"VSaeyheKJUJhEMcOfY0Gdla1RODQSbBaVoLkgVr0w6oy2Fy2vpk+IGK6jWif84rEx9MsmpAOzebTKZLFQeBgqXiMdqJYmLf6nMBiOyA8ExETJ3qlGAkwOXApLstEX6FPMqodTt0BpDHoRKzrDXS2f3F0zPdyp9Lz7riT/8tSxyM=\",\n" +
                "    \"indexhtml\": \"j21h8RxJYJDsbaWtBrVKnJLUh9SxM4GY9i18TiqTpkzK7AxB4x2afG122GI5CyfNBx9b4mg64P+dv3HGWCTb/zrGHE+ViqsYGSK8q0ElYkckPn0f4YEuCjxwicPH2TVsLPSrRlNvyIE+83VwlDgpuDS28/IgkC62FCgD4wCCy+0=\"\n" +
                "  },\n" +
                "  \"theme_color\": \"#2F3BA2\",\n" +
                "  \"background_color\": \"#3E4EB8\",\n" +
                "  \"display\": \"standalone\",\n" +
                "  \"start_url\": \"/index.html\",\n" +
                "  \"icons\": [\n" +
                "    {\n" +
                "      \"type\": \"image/png\",\n" +
                "      \"sizes\": \"128x128\",\n" +
                "      \"src\": \"images/icons/icon-128x128.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"image/png\",\n" +
                "      \"sizes\": \"144x144\",\n" +
                "      \"src\": \"images/icons/icon-144x144.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"image/png\",\n" +
                "      \"sizes\": \"152x152\",\n" +
                "      \"src\": \"images/icons/icon-152x152.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"image/png\",\n" +
                "      \"sizes\": \"192x192\",\n" +
                "      \"src\": \"images/icons/icon-192x192.png\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"image/png\",\n" +
                "      \"sizes\": \"256x256\",\n" +
                "      \"src\": \"images/icons/icon-256x256.png\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"short_name\": \"Weather\",\n" +
                "  \"name\": \"Weather\",\n" +
                "  \"sdk_url_mapping\": {\n" +
                "    \"http://domaincom/js/appjs\": \"js/app.js\",\n" +
                "    \"http://domaincom/css/stylecss\": \"css/style.css\",\n" +
                "    \"http://domaincom/reviewhtml\": \"review.html\",\n" +
                "    \"http://domaincom/indexhtml\": \"index.html\"\n" +
                "  }\n" +
                "}";
        String sourceMd5Json = UpdateResourcesUtils.parseJsonObjectByName(json, "sdk_source_md5");
        Assert.assertNotNull(sourceMd5Json);
        Map<String, String> serviceFileMd5Map = UpdateResourcesUtils.parseFileMd5ToMap(sourceMd5Json);
        Assert.assertNotNull(serviceFileMd5Map);
        System.out.println(serviceFileMd5Map.get("js/appjs"));
        String urlMappingJson = UpdateResourcesUtils.parseJsonObjectByName(json, "sdk_url_mapping");
        Assert.assertNotNull(urlMappingJson);
        System.out.println(urlMappingJson);


    }

    @Test
    public void testParseFileMd5ToMap(){
        String jsonStr = "{\"css\\/stylecss\":\"lW4jt4WWl74GKqOTTPMigbhTgia7haNW8Leh7WTUWrJSupklP46ZWH6HrnM1tVhoQjWyQIXQqO+ePRRVXaxKsjhmYo0HGpYniWbIA3rIUSlRE9PqVwpB4RZ6vxfJAJvomhBxkeiJBdfz0dHt3fiAgKliCmSjkBJ1p0eUfdIrGx0=\",\"js\\/appjs\":\"W29GiGvZvkMsgA+XHIeaHO4fh51EeuQrMLwPbRN4OUnapA9v5ubfIUy9YtU4Y+oM14RGkpNaGbqw7b\\/Lh0WlSOtu\\/32i0gwRHkhi4+vxzCRo4oQULH2QDKcxxnl\\/zkv7Z8KtTWWD2aD21tVPx4N1fP8i6\\/c248uGlB1ppPMRm0U=\",\"reviewhtml\":\"VSaeyheKJUJhEMcOfY0Gdla1RODQSbBaVoLkgVr0w6oy2Fy2vpk+IGK6jWif84rEx9MsmpAOzebTKZLFQeBgqXiMdqJYmLf6nMBiOyA8ExETJ3qlGAkwOXApLstEX6FPMqodTt0BpDHoRKzrDXS2f3F0zPdyp9Lz7riT\\/8tSxyM=\",\"indexhtml\":\"j21h8RxJYJDsbaWtBrVKnJLUh9SxM4GY9i18TiqTpkzK7AxB4x2afG122GI5CyfNBx9b4mg64P+dv3HGWCTb\\/zrGHE+ViqsYGSK8q0ElYkckPn0f4YEuCjxwicPH2TVsLPSrRlNvyIE+83VwlDgpuDS28\\/IgkC62FCgD4wCCy+0=\"}\n";
        Map<String, String> serviceFileMd5Map = UpdateResourcesUtils.parseFileMd5ToMap(jsonStr);
        Assert.assertNotNull(serviceFileMd5Map);
        Assert.assertEquals(serviceFileMd5Map.get("css/stylecss"),"lW4jt4WWl74GKqOTTPMigbhTgia7haNW8Leh7WTUWrJSupklP46ZWH6HrnM1tVhoQjWyQIXQqO+ePRRVXaxKsjhmYo0HGpYniWbIA3rIUSlRE9PqVwpB4RZ6vxfJAJvomhBxkeiJBdfz0dHt3fiAgKliCmSjkBJ1p0eUfdIrGx0=");
    }

    @Test
    public void testSaveUrlMapping() {
        String urlMapping = "{\"http:\\/\\/domaincom\\/js\\/appjs\":\"js\\/app.js\",\"http:\\/\\/domaincom\\/css\\/stylecss\":\"css\\/style.css\",\"http:\\/\\/domaincom\\/reviewhtml\":\"review.html\",\"http:\\/\\/domaincom\\/indexhtml\":\"index.html\"}\n";
        UpdateResourcesUtils.saveUrlMapping(context, urlMapping);
    }

    @Test
    public void testCreateNewResourceFile(){
        File f = UpdateResourcesUtils.createNewResourceFile(context,"v2");
        Assert.assertNotNull(f);
        System.out.println(f.getAbsolutePath());
    }

    @Test
    public void tesVerifyFiles() {
        String expect = "DaOvVaVw6bAo6LOBUEeB+GxtVErHI+06ZVLzpSXubjlnG6FfojwxUBntS9y8p7Aot/wKMeiX1GDRjfFxPagr+3ZzVSJHrtmSQEznGegxtthnCDAFXsyayK41pRKWRvc2x2vTwfe130afNZpyBtRErH+NWZlN0+qSWVXhhjQRHD0=";
        File f = new File("F:\\a_temp_work\\test\\zip\\h5.zip");
        UpdateResourcesUtils.verifyFiles(expect, f);
    }

    @Test
    public void testGetLocalMap() {
        String str = "F:\\a_temp_work\\test\\unzip\\";
        Map<String, String> map = UpdateResourcesUtils.getLocalMap(str);
        Assert.assertNotNull(map);
    }

}
