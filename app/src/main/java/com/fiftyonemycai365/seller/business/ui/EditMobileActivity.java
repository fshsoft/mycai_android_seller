package com.fiftyonemycai365.seller.business.ui;

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
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.UserResultInfo;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.CheckUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2015/10/15.
 */
public class EditMobileActivity extends BaseActivity implements BaseActivity.TitleListener,View.OnClickListener {

    private EditText mEditMobile,mEditCode;
    private Button mGetCode,mSureChange;
    private MyCount mc;
    private String oldmobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mobile);
        setTitleListener(this);

        oldmobile = this.getIntent().getExtras().getString(Constants.EXTRA_DATA);
        initViews();
        setPageTag(TagManage.EDIT_MOBILE_ACTIVITY);
    }

    private void initViews() {
        mEditCode = mViewFinder.find(R.id.edit_code);
        mEditMobile = mViewFinder.find(R.id.edit_mobile);
        mGetCode = mViewFinder.find(R.id.edit_code_btn);
        mGetCode.setOnClickListener(this);
        mSureChange = mViewFinder.find(R.id.edit_validation_btn);
        mSureChange.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mc != null) {
                mc.cancel();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_code_btn:
                mc = new MyCount(61000, 1000);
                if (NetworkUtils.isNetworkAvaiable(EditMobileActivity.this)) {
                    if (TextUtils.isEmpty(mEditMobile.getText().toString())){
                        ToastUtils.show(EditMobileActivity.this,getString(R.string.hint_mobile));
                        mEditMobile.requestFocus();
                        return;
                    }
                    if (!CheckUtils.isMobileNO(mEditMobile.getText().toString())){
                        ToastUtils.show(EditMobileActivity.this, R.string.label_legal_mobile);
                        mEditMobile.setSelection(mEditMobile.getText().toString().length());
                        mEditMobile.requestFocus();
                        return;
                    }
                    CustomDialogFragment.getInstance(getSupportFragmentManager(), PhoneLoginActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", mEditMobile.getText().toString().trim());
                    ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, null, new Response.Listener<Object>() {

                        @Override
                        public void onResponse(Object response) {
                            CustomDialogFragment.dismissDialog();
                            ToastUtils.show(EditMobileActivity.this, R.string.label_send_success);
                            mGetCode.setEnabled(false);
                            mc.start();
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtils.show(EditMobileActivity.this, R.string.msg_error_send);
                            CustomDialogFragment.dismissDialog();
                        }
                    });
                } else {
                    ToastUtils.show(EditMobileActivity.this, R.string.msg_error_network);
                }
                break;
            case R.id.edit_validation_btn:
                changeMobile();
                break;
        }
    }

    private void changeMobile(){
        if (TextUtils.isEmpty(mEditMobile.getText().toString())){
            ToastUtils.show(EditMobileActivity.this,getString(R.string.hint_mobile));
            mEditMobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mEditCode.toString())){
            ToastUtils.show(EditMobileActivity.this,getString(R.string.label_identifying_code));
            mEditCode.requestFocus();
            return;
        }
        if (mEditCode.getText().toString().length() < 6){
            ToastUtils.show(EditMobileActivity.this, getString(R.string.tip_import_sex_verification_code));
            mEditCode.setSelection(mEditCode.getText().toString().length());
            mEditCode.requestFocus();
            return;
        }
        if (!CheckUtils.isMobileNO(mEditMobile.getText().toString())){
            ToastUtils.show(EditMobileActivity.this, R.string.label_legal_mobile);
            mEditMobile.setSelection(mEditMobile.getText().toString().length());
            mEditMobile.requestFocus();
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(),EditMobileActivity.class.getName());
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("mobile",mEditMobile.getText().toString().trim());
        data.put("oldmobile",oldmobile);
        data.put("verifyCode",mEditCode.getText().toString().trim());
        ApiUtils.post(EditMobileActivity.this, URLConstants.REFLASHMOBILE, data, UserResultInfo.class, new Response.Listener<UserResultInfo>() {
            @Override
            public void onResponse(UserResultInfo response) {
                    if (response.data != null){
                        UserInfo info = response.data;
                        PreferenceUtils.setObject(EditMobileActivity.this, info);
                        O2OUtils.reidrectLogin(EditMobileActivity.this);

                    }else{
                        ToastUtils.show(EditMobileActivity.this,response.msg);
                    }
                CustomDialogFragment.dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(EditMobileActivity.this,R.string.msg_error);
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_change_phone_number));
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
