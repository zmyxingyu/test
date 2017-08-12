package com.cn21.onekit.lib.jsinter;

/**
 * Created by xuxd on 2017/7/24.
 */

public interface OkBridgeApi {

    /**
     * 判断当前客户端版本是否支持指定JS接口
     * @return 以键值对的形式返回，可用的api值true，不可用为false
     *         如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
     */
    String checkJsApi();

    /***
     * 获取网络状态接口
     * @return 2g，3g，4g，wifi，none
     */
    String getNetworkType();

    /**
     * 在页面中退出登录同步到APP也退出登录，同样在APP里面退出同步到页面页退出登录
     */
    void logout();

    /**
     *  拍照或从手机相册中选图接口
     * @param compressRatio 压缩比例，仅当sizeType为'compressed'时有效，例：compressRatio = 2则压缩为原大小的二分之一；compressRatio = 4则压缩为原大小的四分之一
     * @param sizeType 可以指定是原图还是压缩图  ['original', 'compressed']
     * @param sourceType  可以指定来源是相册还是相机 ['album', 'camera'],
     *
     * 备注：下载完成，调用js的onChooseImageResult(String data)方法。
     *      data : 结果信息 ，json格式:result为0表示成功， {"result": 0, "path": "","errMsg": "从相机选择图片失败"}
     */
    void chooseImage(int compressRatio , String sizeType , String sourceType);

    /**
     * 预览图片接口
     * @param imagePath
     */
    void previewImage(String imagePath);

    /**
     * 显示导航
     */
    void showNavigationBar();

    /**
     * 隐藏导航
     */
    void hideNavigationBar();


    /**
     * 动态设置当前页面的标题
     */
    void setNavigationBarTitle(String title);



    /**
     * 设置导航bar的颜色
     */
    void setNavigationBarColor(String color);

    /**
     * 设置容器view的背景颜色
     * @param color
     */
    void setViewBgColor(String color);

    /**
     * 设置屏幕亮度
     * @param brightness 取值范围0 到 255
     */
    void setScreenBrightness(int brightness);

    /**
     * 获取屏幕亮度
     */
    int getScreenBrightness();

    /**
     * 设置是否保持常亮状态
     * @param  //参数待定义
     */
    void setKeepScreenOn();

    /**
     * 使手机发生较长时间的振动（400ms）
     * @param //震动强度
     */
    void vibrate(int param);

    /**
     * 设置系统剪贴板的内容
     * @param content
     */
    void setClipboardData(String content);

    /**
     * 获取系统剪贴板的内容
     */
    String getClipboardData();


    /**
     * 拨打电话接口
     */
    void makePhoneCall(String phoneNum);


    /**
     * 监听用户主动截屏事件，用户使用系统截屏按键截屏时触发此事件
     */
//    void onUserCaptureScreen();

    /**
     * 将数据存储在本地缓存中指定的 key 中，会覆盖掉原来该 key 对应的内容
     * @param key
     * @param obj
     */
    void setStorage(String key , String obj);

    /**
     * 从本地缓存中异步获取指定 key 对应的内容
     * @param key
     * @return
     */
    String getStorage(String key);

    /**
     * 从本地缓存中异步移除指定 key 
     * @param key
     */
    void removeStorage(String key);


    /**
     * 清理本地数据缓存
     */
    void clearStorage();


    /**
     * 调用系统的toast提示
     * @param msg
     */
    void showShortToast(String msg);

    /**
     * 调用系统的toast提示
     * @param msg
     */
    void showLongToast(String msg);

    /**
     * 调用系统默认的分享  （参数待定）
     * @param title
     * @param content
     * @param imageUrl
     */
    void systemShare(String title ,String content, String imageUrl);

    /**
     * 获取系统信息，使用json格式返回
     * @return {"model":"vivo x7", "screenWidth" :720, "screenHeight":1200, "language":"zh-CN", "version": "1.0", "system":"5.0", "platform":"Android"}
     */
    String getSystemInfo();

    /**
     * 获取app的登录信息，使用json格式返回
     * @return {"loginState":true , "userId/openId" :"xxxxx"}
     */
    String getLoginInfo();

    /**
     * 封装http请求
     * @param method 请求方法get post
     * @param url 请求url
     * @param data 请求参数  （post请求时 可以传入json格式的请求参数）
     *
     *
     * 备注：请求完成，调用js的onRequestResult(int code ，String result)方法。
     *      resp : 结果信息 ，json格式，例如：result为0则表示成功，其他为失败， {"result": 0, "data": %s, "errMsg": ""}
     */
    void request(String method , String url , String data);

    /**
     * 上传文件
     *<p></p>
     * 备注：下载完成，调用js的onUploadFileResult(String jsonResult)方法。
     *      resp : 结果信息 ，json格式，例如：result为0则表示成功，其他为失败， {"result": 0, "url": "xxx", "filePath": "dsdfs", "errMsg": ""}
     */
    void uploadFile(String url ,String filePath);

    /**
     * 下载文件
     * @param url
     *
     * 备注：下载完成，调用js的onDownloadFileResult(String jsonResult)方法。
     *      resp : 结果信息 ，json格式，例如：result为0则表示成功，其他为失败， {"result": 0, "url": "xxx", "fileName": "dsdfs", "errMsg": ""}
     */
    void downloadFile(String url, String fileName);

    /**
     *
     *备注：选定数据网络通道后，调用js的onUseMobileDataResult(String jsonResult)方法。
     *      resp : 结果信息 ，json格式，例如：result为0则表示成功，其他为失败， {"result": 0, "url": "xxx", "errMsg": ""}
     */
    void selectMobileDataToRequest(String url);
}
