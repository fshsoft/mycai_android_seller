package com.fiftyonemycai365.seller.business.service;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.request.ConfigRequestInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.ConfigResultInfo;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.zongyou.library.app.DeviceUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-20 上午11:34:06
 */
public class O2OService {
    static Context mContext;
    static Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mHandler.removeMessages(0);
            if (!PreferenceUtils.getValue(mContext, Constants.PREFERENCE_CONFIG, false))
                initConfig(mContext);
        }

    };

    public static void initConfig(final Context context) {
        mContext = context;
        if (!NetworkUtils.isNetworkAvaiable(context)) {
            mHandler.sendEmptyMessageDelayed(0, 3000);
            return;
        }
        ApiUtils.post(context, URLConstants.INIT,
                new ConfigRequestInfo(DeviceUtils.getDeviceId(context), Constants.DEVICE_TYPE, android.os.Build.VERSION.RELEASE, DeviceUtils.getPackageInfo(context).versionName),
                ConfigResultInfo.class, new Listener<ConfigResultInfo>() {

                    @Override
                    public void onResponse(final ConfigResultInfo response) {
                        new Thread(new Runnable() {
                            public void run() {
                                if (null != response && null != response.data) {
//									if (!ArraysUtils.isEmpty(response.data.citys)) {
//										O2ODBUtils.addCity(context, response.data.citys);
//									}
//									if (!ArraysUtils.isEmpty(response.data.payments)) {
//										FileUtils.writeObjectFile(context, "config", response.data);
//									}
                                    PreferenceUtils.setValue(context, Constants.PREFERENCE_SERVICE_TEL, response.data.serviceTel);
                                    PreferenceUtils.setValue(context, Constants.PREFERENCE_CONFIG, true);
                                    PreferenceUtils.setValue(context, Constants.TOKEN, response.token);
//									PreferenceUtils.setValue(context, Constants.KEY, response.key);
                                    PreferenceUtils.setObject(context, response.data);

                                }
                            }
                        }).start();

                    }
                }, new ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!PreferenceUtils.getValue(context, Constants.PREFERENCE_CONFIG, false))
                            mHandler.sendEmptyMessageDelayed(0, 3000);
                    }
                });
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public static void logout(Context context) {
        PreferenceUtils.clearSettings(context, UserInfo.class.getName());
//		PreferenceUtils.clearSettings(context, UserAddressInfo.class.getName());

        ApiUtils.post(context, URLConstants.LOGOUT, null, BaseResult.class, new Listener() {

            @Override
            public void onResponse(Object response) {

            }
        });
        return;
    }

    /**
     *
     */
    public static boolean isSeller(int role) {
        return (role & Constants.ROLE_SELLER) !=0;
    }
    public static boolean isService(int role) {
        return (role & Constants.ROLE_SERVICE)  !=0;
    }
    public static boolean isStaff(int role) {
        return (role & Constants.ROLE_STAFF)  !=0;
    }

}

