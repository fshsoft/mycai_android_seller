package com.fiftyonemycai365.seller.business;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tu.crop.CropHelper;
import com.fiftyonemycai365.seller.business.db.DBManager;
import com.fiftyonemycai365.seller.business.util.PushUtils;
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

    public static String getVersion() {
        PackageManager packageManager = getInstance().getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getInstance().getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
