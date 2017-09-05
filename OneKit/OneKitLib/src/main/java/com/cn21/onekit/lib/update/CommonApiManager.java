package com.cn21.onekit.lib.update;

import android.content.Context;
import android.util.Log;

import com.cn21.onekit.core.OneKitManager;
import com.cn21.onekit.lib.update.net.NetAccessor;
import com.cn21.onekit.lib.utils.Constants;
import com.cn21.onekit.lib.utils.DefaultShared;

/**
 * Created by zhangmy on 2017/4/5.
 */
public class CommonApiManager extends BaseManager {
    private static final String CHECK_OPEN_REQUEST = "/api/getSwitch";
    private static final long INTERVAL_TIME = 0 * 60 * 60 * 1000L; //請求服務端接口狀態時間間隔
    private String TAG = CommonApiManager.class.getSimpleName();

    public SwitchListener getmSwitchListener() {
        return mSwitchListener;
    }

    public void setmSwitchListener(SwitchListener mSwitchListener) {
        this.mSwitchListener = mSwitchListener;
    }

    private SwitchListener mSwitchListener;

    private CommonApiManager() {
    }

    private static CommonApiManager instance = null;

    public static CommonApiManager getInstance() {
        if (instance == null) {
            synchronized (CommonApiManager.class) {
                if (instance == null) {
                    instance = new CommonApiManager();
                }
            }
        }
        return instance;
    }

    public void init(final Context context) {
        OneKitManager.getInstance().setmCheckXWalkStatusListener(new OneKitManager.CheckXWalkStatusListener() {
            @Override
            public boolean onCheckStatus() {
                return getXwalkViewStatus(context);
            }
        });
    }

    public boolean getXwalkViewStatus(final Context context) {
        boolean status = DefaultShared.getBoolean(context, Constants.KEY_CHECK_USE_XWALKVIEW_STATUS, true);
        if (isNeedRequestStatus(context)) {
            execute(new Runnable() {
                @Override
                public void run() {
                    requestStatus(context, getBaseUrl(context) + CHECK_OPEN_REQUEST);
                }
            });
        }
        return status;
    }

    private void requestStatus(Context context, String url) {
        try {
            boolean status = NetAccessor.checkOpenOneKit(context, url);
            saveStatus(context, status);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "requestStatus err");
            if (getmSwitchListener() != null) {
                postData(null, new ResponseListener() {
                    @Override
                    public void onCallBack(Object o) {
                        getmSwitchListener().onSwitchErr();
                    }
                });
            }
        }
    }

    private void saveStatus(Context context, boolean status) {
        DefaultShared.putBoolean(context, Constants.KEY_CHECK_USE_XWALKVIEW_STATUS, status);
        DefaultShared.putLong(context, Constants.KEY_CHECK_USE_XWALKVIEW_TIME, System.currentTimeMillis());
        postData(new Boolean(status), new ResponseListener() {
            @Override
            public void onCallBack(Object o) {
                if (getmSwitchListener() != null) {
                    getmSwitchListener().onSwitchSuccess((Boolean) o);
                }
            }
        });
    }

    /**
     * 是否需求向服务端请求状态
     *
     * @return
     */
    private boolean isNeedRequestStatus(Context context) {
        long updateSuccessTime = DefaultShared.getLong(context, Constants.KEY_CHECK_USE_XWALKVIEW_TIME, 0L);
        if ((System.currentTimeMillis() - updateSuccessTime > INTERVAL_TIME)) {
            return true;
        }
        return false;
    }


    /**
     * 监听获取开关的状态信息
     */
    public interface SwitchListener {
        void onSwitchErr();

        void onSwitchSuccess(boolean status);
    }

}
