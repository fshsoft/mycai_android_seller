package com.yizan.community.business.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.zongyou.library.util.json.JSONHelper;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.VolleyUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 业务处理工具类
 *
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午7:22:20
 */
public class ApiUtils {
    public static <T> void get(String url, Map<String, String> data, final Class<T> clazz, Response.Listener<T> listener,Response.ErrorListener errorListener) {
        VolleyUtils.get(url, data, clazz, listener, errorListener);
    }
    public static <T> void post(Context context, String url, Object data, final Class<T> clazz, Listener<T> listener) {
        VolleyUtils.post(url, getParams(context, data, url), clazz, listener, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO toast error

            }

        });
    }

    public static <T> void post(Context context, String url, Object data, final Class<T> clazz, Listener<T> listener, ErrorListener el) {

        VolleyUtils.post(context, url, getParams(context, data, url), clazz, listener, el, URLConstants.ENUM == URLConstants.URLEnum.Encryption ? true : false);
        // FIXME url debug
        StringBuilder sb = new StringBuilder(url);
        HashMap<String, String> pa = getParams(context, data, url);
        Set<Entry<String, String>> en = pa.entrySet();
        Iterator<Entry<String, String>> i = en.iterator();
        int index = 0;
        if (i.hasNext()) {
            sb.append("?");
            index++;
            while (i.hasNext()) {
                Entry<String, String> e = i.next();
                sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
            }
        }
        Log.e("http", index > 0 ? sb.replace(sb.length() - 1, sb.length(), "").toString() : sb.toString());
    }

    private static HashMap<String, String> getParams(Context context,
                                                     Object data, String url) {
        HashMap<String, String> params = new HashMap<String, String>(2);
        String token = PreferenceUtils.getValue(context, Constants.TOKEN_LOGIN, "");
        final UserInfo info = PreferenceUtils.getObject(context, UserInfo.class);
        if (null != info) {
            if (!TextUtils.isEmpty(token))
                params.put(Constants.TOKEN, token);
            else
                params.put(Constants.TOKEN,
                        PreferenceUtils.getValue(context, Constants.TOKEN, ""));
            if (0 != info.id)
                params.put(Constants.USER_ID, String.valueOf(info.id));
        }
        if (null != data) {
            String paramData = JSONHelper.toJSON(data);
//		if (!url.equals(URLConstants.LOGIN)) {
//			AESUtils aesUtils = new AESUtils(token, PreferenceUtils.getValue(
//					context, Constants.KEY, ""), info.user.id);
//			paramData = aesUtils.encrypt(JSONHelper.toJSON(data));
//		}
            params.put(Constants.DATA, paramData);
        }
        return params;
    }
}
