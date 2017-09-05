package com.cn21.onekit.core;

/**
 * 运行的一些信息
 * Created by zhangmy on 2017/9/4.
 */
public interface OneKitRuntime {
    /**
     * 获取客户端账号信息
     *
     * @return 账号信息 （天翼账号）
     */
    String getAccount();

    /**
     * appSecret
     *
     * @return 产品秘钥
     */
    String getAppSecret();

    /**
     * 产品appId
     *
     * @return 产品id
     */
    String getAppId();

    /**
     *
     * @return 资源包秘钥
     */
    String getPackageSecret();

}
