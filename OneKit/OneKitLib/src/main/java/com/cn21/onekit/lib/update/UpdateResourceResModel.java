package com.cn21.onekit.lib.update;

/**
 * 本地资源包更新接口返回实体类
 * xuxd
 */
public class UpdateResourceResModel {
    public int result; //result	0表示需要更新，其他的则不需要
    public String msg; //说明信息
    public String md5; //经过RSA加密后的资源包md5字符串
    public String resourceUrl; //新版资源包下载地址
    public String resourceVersion; //新版资源包版本号
}
