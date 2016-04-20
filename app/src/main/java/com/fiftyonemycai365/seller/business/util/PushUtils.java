package com.fiftyonemycai365.seller.business.util;

import android.content.Context;

import com.fiftyonemycai365.seller.business.BuildConfig;
import com.fanwe.seallibrary.model.UserInfo;
import com.zongyou.library.platform.ZYPushConfig;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/11/16.
 */
public class PushUtils {

    public static void init(Context context){
        ZYPushConfig.setDebugMode(BuildConfig.DEBUG);
        ZYPushConfig.init(context);
        ZYPushConfig.setLatestNotificationNumber(context, 1);
        ZYPushConfig.setDefaultNotificationSound(context, false, true);
        initTagsAndAlias(context);
    }
    public static void initTagsAndAlias(final Context context) {

        initTags(context);
        initAlias(context);

    }
    public static void initTags(Context context){
        /**
         * 标签
         */
        Set<String> list = new HashSet<String>();
        list.add("staff");
        list.add("android");
        ZYPushConfig.initTags(context, list);
    }
    /**
     * 别名
     */
    public static void initAlias(final Context context){
        UserInfo info = PreferenceUtils.getObject(context, UserInfo.class);
        String str = "";
        if (O2OUtils.isLogin(context) && info != null) {
            str = "staff_" + info.id;
        }else{
            ZYPushConfig.clearAllNotifications(context);
        }
        ZYPushConfig.initAlias(context, str);
    }
    public static void isPush(Context context,boolean parameter){
        if(parameter) {
            ZYPushConfig.resumePush(context);
        }else {
            ZYPushConfig.stopPush(context);
        }
    }
}
