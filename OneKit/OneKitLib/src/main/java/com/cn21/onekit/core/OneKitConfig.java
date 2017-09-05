package com.cn21.onekit.core;

/**
 * 接入的一些配置项
 * Created by zhangmy on 2017/9/4.
 */
public class OneKitConfig {

    public long getOneKitResourceInvalidTime() {
        return ONEKIT_RESOURCE_INTERVAL_TIME;
    }

    /**
     * 资源包有效时间，超过该时间才要去检查版本更新
     * {@code UpdateResourcesUtils.isNeedToUpdate()}
     */
    long ONEKIT_RESOURCE_INTERVAL_TIME = 0 * 60 * 60 * 1000L;

    private OneKitConfig() {

    }

    public static class Builder {
        private final OneKitConfig target;

        public Builder() {
            target = new OneKitConfig();
        }

        public Builder setResourceInvalidTime(long invalidTime) {
            target.ONEKIT_RESOURCE_INTERVAL_TIME = invalidTime;
            return this;
        }

        public OneKitConfig build() {
            return target;
        }
    }
}
