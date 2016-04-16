package com.yizan.community.business.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.UserResultInfo;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.CheckUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2015/10/29.
 */
public class ChangedMobileActivity extends BaseActivity implements BaseActivity.TitleListener,View.OnClickListener {
    private EditText mEditNewMobile,mEditCode;
    private Button mGetCode,mSureChange;
    private MyCount mc;
    private String oldMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changed_mobile);
        setTitleListener(this);
        oldMobile = this.getIntent().getStringExtra("oldMobile");
        initeViews();
        setPageTag(TagManage.CHANGED_MOBILE_ACTIVITY);
    }

    private void initeViews() {
        mGetCode = mViewFinder.find(R.id.get_code);
        mGetCode.setOnClickListener(this);
        mSureChange = mViewFinder.find(R.id.save_new_mobile);
        mSureChange.setOnClickListener(this);
        mEditNewMobile = mViewFinder.find(R.id.edit_new_mobile);
//        mEditOldMobile = mViewFinder.find(R.id.edit_old_mobile);
        mEditCode = mViewFinder.find(R.id.new_edit_code);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.check_new_mobile));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_new_mobile:
                changeMobile();
                break;
            case R.id.get_code:
                mc = new MyCount(61000, 1000);
                if (NetworkUtils.isNetworkAvaiable(ChangedMobileActivity.this)) {
                    if (TextUtils.isEmpty(mEditNewMobile.getText().toString())){
                        ToastUtils.show(ChangedMobileActivity.this, getString(R.string.msg_import_new_phone_number));
                        mEditNewMobile.requestFocus();
                        return;
                    }
                    if (!CheckUtils.isMobileNO(mEditNewMobile.getText().toString())){
                        ToastUtils.show(ChangedMobileActivity.this, R.string.label_legal_mobile);
                        mEditNewMobile.setSelection(mEditNewMobile.getText().toString().length());
                        mEditNewMobile.requestFocus();
                        return;
                    }
                    CustomDialogFragment.getInstance(getSupportFragmentManager(), PhoneLoginActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", mEditNewMobile.getText().toString().trim());
                    ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, null, new Response.Listener<Object>() {

                        @Override
                        public void onResponse(Object response) {
                            CustomDialogFragment.dismissDialog();
                            ToastUtils.show(ChangedMobileActivity.this, R.string.label_send_success);
                            mGetCode.setEnabled(false);
                            mc.start();
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CustomDialogFragment.dismissDialog();
                        }
                    });
                } else {
                    ToastUtils.show(ChangedMobileActivity.this, R.string.msg_error_network);
                }
                break;
        }
    }

    private void changeMobile(){
        if (TextUtils.isEmpty(oldMobile)){
            ToastUtils.show(ChangedMobileActivity.this,getString(R.string.msg_import_old_phone_number));
            return;
        }
        if (TextUtils.isEmpty(mEditNewMobile.getText().toString())){
            ToastUtils.show(ChangedMobileActivity.this,getString(R.string.msg_import_new_phone_number));
            mEditNewMobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mEditCode.toString())){
            ToastUtils.show(ChangedMobileActivity.this,getString(R.string.tip_import_verification_code));
            mEditCode.requestFocus();
            return;
        }
        if (mEditCode.getText().toString().length() < 6){
            ToastUtils.show(ChangedMobileActivity.this, getString(R.string.tip_import_sex_verification_code));
            mEditCode.setSelection(mEditCode.getText().toString().length());
            mEditCode.requestFocus();
            return;
        }
        if (!CheckUtils.isMobileNO(oldMobile)){
            ToastUtils.show(ChangedMobileActivity.this, R.string.label_legal_mobile);
            return;
        }
        if (!CheckUtils.isMobileNO(mEditNewMobile.getText().toString())){
            ToastUtils.show(ChangedMobileActivity.this, R.string.label_legal_mobile);
            mEditNewMobile.setSelection(mEditNewMobile.getText().toString().length());
            mEditNewMobile.requestFocus();
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditMobileActivity.class.getName());
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("mobile",mEditNewMobile.getText().toString().trim());
        data.put("oldmobile",oldMobile);
        data.put("verifyCode",mEditCode.getText().toString().trim());
        ApiUtils.post(ChangedMobileActivity.this, URLConstants.REFLASHMOBILE, data, UserResultInfo.class, new Response.Listener<UserResultInfo>() {
            @Override
            public void onResponse(UserResultInfo response) {
                if (response.data != null){
                    UserInfo info = response.data;
                    PreferenceUtils.setObject(ChangedMobileActivity.this, info);
                    O2OUtils.reidrectLogin(ChangedMobileActivity.this);

                }else{
                    ToastUtils.show(ChangedMobileActivity.this,response.msg);
                }
                CustomDialogFragment.dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(ChangedMobileActivity.this,R.string.msg_error);
            }
        });
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            mGetCode.setTextColor(Color.WHITE);
            mGetCode.setText(R.string.lable_get_identifying);
            mGetCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetCode.setTextColor(Color.GRAY);
            mGetCode.setText(getString(R.string.code_again, String.valueOf(millisUntilFinished / 1000)));
        }
    }
}
