package com.fiftyonemycai365.seller.business.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.Hand.InterFace.ResponseCode;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.PointInfo;
import com.fanwe.seallibrary.model.StaffInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.UserResultInfo;
import com.fiftyonemycai365.seller.business.ui.LoginActivity;
import com.fiftyonemycai365.seller.business.ui.ViewImageActivity;
import com.zongyou.library.app.AppManager;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;
import java.util.List;

public final class O2OUtils {
    /**
     * 是否已登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        UserInfo user = PreferenceUtils.getObject(context, UserInfo.class);
        return null != user && 0 != user.id;
    }

    /**
     * 重新登录
     *
     * @param context
     */
    public static void reidrectLogin(Context context) {

        ApiUtils.post(context, URLConstants.LOGOUT, null, BaseResult.class, new Listener() {

            @Override
            public void onResponse(Object response) {

            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        PreferenceUtils.clearSettings(context, UserInfo.class.getName());
        PreferenceUtils.clearSettings(context, StaffInfo.class.getName());
        AppManager.getAppManager().finishAllActivity();
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    public static void cacheUserData(Context context, UserResultInfo info) {
        if (null != info && null != info.data) {
            PreferenceUtils.setObject(context, info.data);
            PreferenceUtils.setValue(context, Constants.TOKEN_LOGIN, info.token);
            PreferenceUtils.setValue(context, Constants.STAFF_ID, info.data.id);
        }
    }

    /**
     * 检查response，并提示
     *
     * @param context
     * @param response
     * @return true ,false
     */
    public static boolean checkResponse(Context context, BaseResult response) {
        if (null != response) {
            if (ResponseCode.SUCCESS.code != response.code) {
                switch (response.code) {
                    case 99996:
                        ToastUtils.show(context, ResponseCode.ERROR_UNLOGIN.msg);
                        // reidrectLogin(context);
                        break;
                    case 99997:
                        if(TextUtils.isEmpty(response.msg)) {
                            ToastUtils.show(context, ResponseCode.ERROR_TOKEN.msg);
                        }else{
                            ToastUtils.show(context, response.msg);
                        }
                        // reidrectLogin(context);
                        break;
                    case 99998:
                    case 99999:
                        ToastUtils.show(context, R.string.msg_error);
                        break;
                    default:
                        if (!TextUtils.isEmpty(response.msg))
                            ToastUtils.show(context, response.msg);
                        break;
                }
            } else
                return true;

        } else
            ToastUtils.show(context, R.string.msg_error);
        return false;
    }

    public static void setImageurl(final Context context, final List<String> images, NetworkImageView... imageViews) {
        int index = 0;
        for (NetworkImageView image : imageViews) {
            if (images.size() == 0 || images.size() == 1 && TextUtils.isEmpty(images.get(0))) {
                image.setVisibility(View.GONE);
                index++;
                continue;
            }
            if (images.size() > index && !TextUtils.isEmpty(images.get(index))) {
                image.setDefaultImageResId(R.drawable.ic_default);
                image.setErrorImageResId(R.drawable.ic_default);
                image.setImageUrl(images.get(index), RequestManager.getImageLoader());
                image.setVisibility(View.VISIBLE);
                final int i = index;
                image.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ViewImageActivity.class);
                        intent.putStringArrayListExtra(Constants.EXTRA_DATA, (ArrayList<String>) images);
                        intent.putExtra(Constants.EXTRA_INDEX, i);
                        context.startActivity(intent);
                    }
                });
            } else
                image.setVisibility(View.INVISIBLE);
            index++;
        }
    }

    public static String numberLengthFormat(String source, int length) {
        if (null != source) {
            int offset = length - source.length();
            if (offset > 0) {
                StringBuilder sb = new StringBuilder(length);
                while (offset > 0) {
                    sb.append("  ");
                    offset--;
                }
                sb.append(" ");
                sb.append(source);
                return sb.toString();
            }
        }
        return source;
    }

    public static void openSysMap(Activity activity, PointInfo point) {

        if (point == null) {
            ToastUtils.show(activity, activity.getResources().getString(R.string.err_map_info));
            return;
        }
        IntentUtils.openSysMap(activity, Uri.parse("geo:" + point.y + "," + point.x + "?q=" + point.address));
    }

    public static SpannableString formatTextPrimary(Context context, String src, int offset) {
        SpannableString spanText = new SpannableString(src);
        spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.textPrimary)), src.length() - offset, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }

    public interface OKCallback {
        void callback(String text);
    }

    public static void initPop(View parent, final OKCallback callback) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_pop, null);
        RelativeLayout cancel = (RelativeLayout) view.findViewById(R.id.rl_pop_cancel);
        RelativeLayout sure = (RelativeLayout) view.findViewById(R.id.rl_pop_sure);
        RelativeLayout top = (RelativeLayout) view.findViewById(R.id.rl_top);
        LinearLayout bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        bottom.setBackgroundResource(R.drawable.text_white_half_bottom_border);
        top.setBackgroundResource(R.drawable.text_red_half_border);
        ((TextView)view.findViewById(R.id.pop_title)).setText(R.string.remark);
        final EditText reply_content = (EditText) view.findViewById(R.id.reply_content);
        reply_content.setHint(R.string.opinion_hint);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reply_content.getText().length() != 0) {
                    popupWindow.dismiss();
                    callback.callback(reply_content.getText().toString());
                } else
                    ToastUtils.show(view.getContext(), R.string.msg_error_content_empty);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int height = view.findViewById(R.id.pop_ll).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }
}
