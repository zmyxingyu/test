package com.cn21.onekit.core;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 地址映射能力
 * Created by zhangmy on 2017/8/18.
 */
public class FilterManager {
    private static FilterManager instance;

    private Map<String, String> mMappings;

    /**
     * 根据urlmaping ,返回本地地址
     *
     * @param sourceUrl
     * @return mapping 后地址
     */
    public String filter2LocalUrl(String sourceUrl) {
        String[] args = getItemOfUrl(sourceUrl);
        if (args != null && mMappings != null && mMappings.size() > 0) {
            String url = mMappings.get(args[0]);
            if (TextUtils.isEmpty(url)) {
                return sourceUrl;
            }
            return args.length > 1 ? (url + "?" + args[1]) : url;
        }
        return sourceUrl;
    }

    /**
     * 分离参数和地址
     *
     * @param url
     * @return
     */
    public String[] getItemOfUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String[] args = url.split("\\?");
        if (args == null || args.length > 2 || args.length < 1) {
            return null;
        }
        return args;
    }

    private FilterManager() {

    }

    public static FilterManager getInstance() {
        if (instance == null) {
            synchronized (FilterManager.class) {
                instance = new FilterManager();
            }
        }
        return instance;
    }

    private Map<String, String> getmMappings() {
        if (null == mMappings) {
            mMappings = new HashMap<>();
        }
        return mMappings;
    }

    public void addUrlMapping(String source, String mapping) {
        getmMappings().put(source, mapping);
    }

    public void addUrlMapping(Map<String, String> map) {
        getmMappings().putAll(map);
    }

    public void clearUrlMapping() {
        if (null != mMappings) {
            mMappings.clear();
        }
    }


}
