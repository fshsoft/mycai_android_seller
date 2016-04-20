package com.fiftyonemycai365.seller.business.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
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
import com.fanwe.seallibrary.model.StaffInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.BooleanResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 改绑手机号，校验旧手机Activity
 */
public class BindCheckMobileActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private Button mGetCodeButton;
    private EditText mCodeEditText;
    private MyCount mc;
    private TextView mTextMobile;
    private boolean code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_check_mobile);
        setTitleListener(this);


        mGetCodeButton = (Button) findViewById(R.id.bind_code_btn);
        mGetCodeButton.setOnClickListener(this);
        mViewFinder.find(R.id.bind_validation_btn).setOnClickListener(this);

        initViews();
        initDatas();
        setPageTag(TagManage.BIND_CHECK_MOBILE_ACTIVITY);
    }

    private void initDatas() {
        UserInfo user = PreferenceUtils.getObject(BindCheckMobileActivity.this,UserInfo.class);
        SpannableString sp = new SpannableString(getString(R.string.label_new_mobile,user.mobile));
        mTextMobile.setText(sp);
    }

    private void initViews() {
        mTextMobile = mViewFinder.find(R.id.bind_mobile);
        mCodeEditText = mViewFinder.find(R.id.bind_code);

    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.new_mobile_check);
    }

    @Override
    public void onClick(View v) {
        AppUtils.hideSoftInput(BindCheckMobileActivity.this);
        switch (v.getId()) {
            case R.id.bind_validation_btn:
                checkMobile();
                break;
            case R.id.bind_code_btn:
                mc = new MyCount(61000, 1000);
                if (NetworkUtils.isNetworkAvaiable(BindCheckMobileActivity.this)) {
                    CustomDialogFragment.getInstance(getSupportFragmentManager(), PhoneLoginActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", PreferenceUtils.getObject(BindCheckMobileActivity.this, StaffInfo.class).mobile);
                    ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, null, new Response.Listener<Object>() {

                        @Override
                        public void onResponse(Object response) {
                            CustomDialogFragment.dismissDialog();
                            ToastUtils.show(BindCheckMobileActivity.this, R.string.label_send_success);
                            mGetCodeButton.setEnabled(false);
                            mc.start();
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CustomDialogFragment.dismissDialog();
                        }
                    });
                } else {
                    ToastUtils.show(BindCheckMobileActivity.this, R.string.msg_error_network);
                }
                break;
        }
    }

    private void checkMobile(){
        if (NetworkUtils.isNetworkAvaiable(BindCheckMobileActivity.this)){
            if (("").equals(mCodeEditText.getText().toString().trim())){
                ToastUtils.show(BindCheckMobileActivity.this,R.string.tip_import_verification_code);
                mCodeEditText.requestFocus();
                return ;
            }
            if (mCodeEditText.getText().toString().trim().length() < 6){
                ToastUtils.show(BindCheckMobileActivity.this,R.string.tip_import_sex_verification_code);
                mCodeEditText.setSelection(mCodeEditText.getText().toString().length());
                mCodeEditText.requestFocus();
                return;
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("mobile", PreferenceUtils.getObject(BindCheckMobileActivity.this, UserInfo.class).mobile);
            map.put("verifyCode",mCodeEditText.getText().toString().trim());
            ApiUtils.post(BindCheckMobileActivity.this, URLConstants.CHECKMOBILE, map, BooleanResult.class, new Response.Listener<BooleanResult>() {
                @Override
                public void onResponse(BooleanResult response) {
                        if (!("").equals(response.data)){
                            if (response.data){
                                Intent intent = new Intent(BindCheckMobileActivity.this,EditMobileActivity.class);
                                Bundle b = new Bundle();
                                b.putString(Constants.EXTRA_DATA,PreferenceUtils.getObject(BindCheckMobileActivity.this,UserInfo.class).mobile);
                                intent.putExtras(b);
                                startActivity(intent);
                            }else{
                                ToastUtils.show(BindCheckMobileActivity.this,R.string.msg_error_afresh_obtain);
                            }
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ToastUtils.show(BindCheckMobileActivity.this,R.string.msg_error);
                }
            });
        }
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

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            mGetCodeButton.setTextColor(Color.WHITE);
            mGetCodeButton.setText(R.string.lable_get_identifying);
            mGetCodeButton.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetCodeButton.setTextColor(Color.GRAY);
            mGetCodeButton.setText(getString(R.string.code_again, String.valueOf(millisUntilFinished / 1000)));
        }
    }
}
