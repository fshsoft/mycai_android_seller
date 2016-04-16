package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.yizan.community.business.BuildConfig;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ConfigInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.MessageHasNewResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.PushUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.HashMap;

/**
 * 更多
 */
public class MoreFragment extends BaseFragment implements OnClickListener {
    private ImageView mPoint;
    private LinearLayout mNoBusiness,mJg;
    private Button msgSettings;

    public void setViewClickListener(int layoutId) {
        mViewFinder.find(layoutId).setOnClickListener(this);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_more, container,
                false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.MORE_FRAGMENT);
    }

    @Override
    protected void initView() {
        UserInfo user = PreferenceUtils.getObject(mFragmentActivity, UserInfo.class);
        mNoBusiness = mViewFinder.find(R.id.is_no_business);
        msgSettings = mViewFinder.find(R.id.personal_msg_right);
        msgSettings.setOnClickListener(this);
        mJg = mViewFinder.find(R.id.jian_ge);
        if (user.role == 1){
            mNoBusiness.setVisibility(View.GONE);
            mJg.setVisibility(View.GONE);
        }else{
            mNoBusiness.setVisibility(View.VISIBLE);
            mJg.setVisibility(View.VISIBLE);
        }
        if (BuildConfig.isTestAndDemo){
            mViewFinder.find(R.id.business_statistics).setVisibility(View.GONE);
            mViewFinder.find(R.id.personal_commission).setVisibility(View.GONE);
        }else{
            mViewFinder.find(R.id.business_statistics).setVisibility(View.VISIBLE);
            mViewFinder.find(R.id.personal_commission).setVisibility(View.VISIBLE);
        }
        mPoint = mViewFinder.find(R.id.personal_msg_new);
        setViewClickListener(R.id.personal_commission);
        setViewClickListener(R.id.personal_msg);
        setViewClickListener(R.id.personal_setting);
        setViewClickListener(R.id.personal_container);
        setViewClickListener(R.id.business_statistics);
        setViewClickListener(R.id.ask_for_leave);
        setViewClickListener(R.id.useing_help);

        ConfigInfo configInfo = PreferenceUtils.getObject(getActivity(), ConfigInfo.class);
        if(configInfo != null) {
            mViewFinder.find(R.id.ll_tel_container).setVisibility(View.VISIBLE);
            TextView tv = mViewFinder.find(R.id.tv_service_tel);
            tv.getPaint().setUnderlineText(true);
            tv.setText(configInfo.serviceTel);
            setViewClickListener(R.id.tv_service_tel);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.useing_help:
                Intent intent = new Intent(mFragmentActivity, WebViewActivity.class);
                String url = PreferenceUtils.getObject(mFragmentActivity, ConfigInfo.class).helpUrl;
                intent.putExtra(Constants.EXTRA_URL, url);
                startActivity(intent);
                break;
            case R.id.personal_container:
                startActivity(new Intent(mFragmentActivity,
                        UserEditActivity.class));
                break;
            case R.id.personal_commission:
                startActivity(new Intent(mFragmentActivity, CommissionListActivity.class));
                break;
            case R.id.personal_setting:
                startActivity(new Intent(mFragmentActivity, SettingActivity.class));
                break;
            case R.id.personal_msg:
                startActivity(new Intent(mFragmentActivity, InformCenterActivity.class));
                break;
            case R.id.business_statistics:
                startActivity(new Intent(mFragmentActivity,BusinessStatictasActivity.class));
                break;
            case R.id.ask_for_leave:
                startActivity(new Intent(mFragmentActivity,LeaveTimeActivity.class));
                break;
            case R.id.tv_service_tel:
                try {
                    ConfigInfo configInfo = PreferenceUtils.getObject(getActivity(), ConfigInfo.class);
                    String tel = configInfo.serviceTel.replace("-", "");
                    IntentUtils.dial(getActivity(), tel);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.personal_msg_right:
                boolean isPushOpen = isPushOpen();
                PushUtils.isPush(getActivity(), !isPushOpen);
                initPushSwitchView(!isPushOpen);
                break;
        }

    }

    private void loadMessageFlag() {
        HashMap<String,String> data = new HashMap<>();
        ApiUtils.post(this.mFragmentActivity, URLConstants.MESSAGE_STATUS, data,
                MessageHasNewResult.class, new Listener<MessageHasNewResult>() {

                    @Override
                    public void onResponse(MessageHasNewResult response) {
                        if (O2OUtils.checkResponse(
                                MoreFragment.this.mFragmentActivity,
                                response)) {
                            mPoint.setVisibility(response.data.hasNewMessage ? View.VISIBLE : View.GONE);
                        }
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkImageView iv = (NetworkImageView) mViewFinder
                .find(R.id.personal_img);
        UserInfo user = PreferenceUtils.getObject(this.getActivity(),
                UserInfo.class);
        if (user != null && user.avatar != null){
            iv.setImageUrl(user.avatar,
                    RequestManager.getImageLoader());
            iv.setDefaultImageResId(R.drawable.ic_default_head);
            iv.setErrorImageResId(R.drawable.ic_default_head);
        }


        TextView tv = mViewFinder.find(R.id.personal_name);

        if (user != null && user.name != null){
            tv.setText(user.name);
        }else{
            tv.setText("");
        }
        loadMessageFlag();
    }
    private void initPushSwitchView(boolean bOpen){
        PreferenceUtils.setValue(getActivity(), "push_open", bOpen);
        if (!bOpen){
            msgSettings.setBackgroundResource(R.drawable.btn_off);
        }else {
            msgSettings.setBackgroundResource(R.drawable.btn_on);
        }
    }

    private boolean isPushOpen(){
        return PreferenceUtils.getValue(getActivity(), "push_open", true);
    }
}
