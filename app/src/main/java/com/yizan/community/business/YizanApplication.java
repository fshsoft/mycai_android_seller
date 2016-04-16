package com.yizan.community.business;

import android.app.Application;

import com.tu.crop.CropHelper;
import com.yizan.community.business.db.DBManager;
import com.yizan.community.business.util.PushUtils;
import com.zongyou.library.platform.ZYStatConfig;
import com.zongyou.library.volley.RequestManager;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 上午11:26:44
 */
public class YizanApplication extends Application {
    private static YizanApplication sMe;

    public static YizanApplication getInstance() {
        return sMe;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sMe = this;
        init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RequestManager.getRequestQueue().stop();
        DBManager.getDBManager(this).closeDB();
        CropHelper.cleanAllCropCache(this);
        sMe = null;
    }

    private void init() {
        // volley
        RequestManager.init(this);

        PushUtils.init(getApplicationContext());

        ZYStatConfig.setDebugMode(BuildConfig.DEBUG);
        ZYStatConfig.setAppKey(getApplicationContext(), BuildConfig.STAT_KEY);
        ZYStatConfig.setInstallChannel(getApplicationContext(), BuildConfig.APP_CHANNEL);
        ZYStatConfig.setAutoExceptionCaught(true);
        ZYStatConfig.setTagCheck(false);
    }
}
