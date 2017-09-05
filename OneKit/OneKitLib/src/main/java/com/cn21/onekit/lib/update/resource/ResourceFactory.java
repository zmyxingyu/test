package com.cn21.onekit.lib.update.resource;

/**
 * Created by Administrator on 2017/8/16.
 */
public class ResourceFactory {

    public static IResource createResource(String url, String md5) {
        ResoucePackage rp = new ResoucePackage();
        rp.setMd5(md5);
        rp.setUrl(url);
        return rp;
    }

    static class ResoucePackage implements IResource{

        String mUrl;
        String mMd5;
        @Override
        public String getUrl() {
            return mUrl;
        }

        @Override
        public void setUrl(String url) {
            this.mUrl = url;
        }

        @Override
        public String getMd5() {
            return mMd5;
        }

        @Override
        public void setMd5(String md5) {
            this.mMd5 = md5;
        }
    }
}
