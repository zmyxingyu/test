package com.cn21.onekit.lib.jsinter;

import java.util.Map;

/**
 * js回调app的相关接口
 */
public interface OneKitJsCallback {
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
     * 在页面中退出登录同步到APP也退出登录，同样在APP里面退出同步到页面页退出登录
     */
    void logout();

    /**
     * 获取app的登录信息，使用map返回
     * <pre class="prettyprint">
     *     Map<String, String> map = new HashMap();
     *     map.put("loginState", true);
     *     map.put("userId", "023154dfs");//或者map.put("openId", "242swe");
     * </pre>
     */
    Map<String, String> getLoginInfo();

    /**
     * 选择数据网络通道
     * @param result 0 表示成功，其他值为失败
     * @param url 网页透传过来的地址（未做任何修改）
     * @param network 当 选择数据网络通道成功 且 Build.VERSION.SDK_INT >= 21 时才可能有值
     */
    void onSelectMobileData(int result, String url, Object network);
}
