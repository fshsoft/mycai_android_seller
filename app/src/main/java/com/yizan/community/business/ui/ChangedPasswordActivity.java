package com.yizan.community.business.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.UserResultInfo;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.CheckUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 *
 * @author haojiahe
 * @time 2015-3-23上午10:20:19
 */
public class ChangedPasswordActivity extends BaseActivity implements TitleListener, OnClickListener {
    public static final int REQUEST_CODE = 0x102;
    private EditText mEtIdentifyNumber, mEtInputPassword, mEtMobile;
    private Button mBtnChangePwd;
    private TextView mTvGetIndentifyCode, mTvContactCustomerService, mTextNowPhone;
    private String identifyNumber, mobileNumber, password;
    private MyCount mc;
    private UserInfo user;
    private int biaozhi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitleListener(this);
        biaozhi = this.getIntent().getIntExtra("biaozhi", 0);
        initView();
        setPageTag(TagManage.CHANGED_PASSWORD_ACTIVITY);
    }

    private void initView() {
        mTextNowPhone = mViewFinder.find(R.id.text_now_phone);
        user = PreferenceUtils.getObject(ChangedPasswordActivity.this, UserInfo.class);
        mEtMobile = mViewFinder.find(R.id.edit_mobile_cp);
        if (biaozhi == 1) {
            mTextNowPhone.setVisibility(View.VISIBLE);
            mEtMobile.setVisibility(View.GONE);
            SpannableString sp = new SpannableString(getString(R.string.now_phone, user.mobile));
            if (user != null && user.mobile != null) {
                mTextNowPhone.setText(sp);
            } else {
                ToastUtils.show(ChangedPasswordActivity.this, getString(R.string.msg_err_phone_number_is_null));
            }
        } else if (biaozhi == 2) {
            mTextNowPhone.setVisibility(View.GONE);
            mEtMobile.setVisibility(View.VISIBLE);
        }

        mEtIdentifyNumber = (EditText) findViewById(R.id.identify_code_et);
        mEtInputPassword = (EditText) findViewById(R.id.input_pwd_et);
        mTvGetIndentifyCode = (TextView) findViewById(R.id.get_identifying);
        mBtnChangePwd = (Button) findViewById(R.id.changed_btn);
        mTvContactCustomerService = (TextView) findViewById(R.id.contactcustomerservice_tv);
        mTvContactCustomerService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4006-822-181"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mTvGetIndentifyCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AppUtils.hideSoftInput(ChangedPasswordActivity.this);
                if (biaozhi == 1) {
                    mobileNumber = user.mobile;
                } else if (biaozhi == 2) {
                    mobileNumber = mEtMobile.getText().toString();
                }

                identifyNumber = mEtIdentifyNumber.getText().toString();
                if (mobileNumber == null || mobileNumber.length() == 0) {
                    ToastUtils.show(ChangedPasswordActivity.this, R.string.label_empty_mobile);
                    return;
                }

                mc = new MyCount(61000, 1000);
                // mc.start();
                if (NetworkUtils.isNetworkAvaiable(ChangedPasswordActivity.this)) {
                    CustomDialogFragment.getInstance(getSupportFragmentManager(), ChangedPasswordActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", mobileNumber);
                    ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, BaseResult.class, new Listener<BaseResult>() {

                        @Override
                        public void onResponse(BaseResult response) {

                            CustomDialogFragment.dismissDialog();
                            if (O2OUtils.checkResponse(ChangedPasswordActivity.this, response)) {
                                ToastUtils.show(ChangedPasswordActivity.this, R.string.label_send_success);
                                mTvGetIndentifyCode.setEnabled(false);
                                mc.start();
                            }
                        }

                    }, new ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtils.show(ChangedPasswordActivity.this, R.string.msg_error_network);
                            CustomDialogFragment.dismissDialog();
                        }
                    });
                } else {
                    ToastUtils.show(ChangedPasswordActivity.this, R.string.msg_error_network);
                }
            }
        });
        mBtnChangePwd.setOnClickListener(this);

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.label_change_password);

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
        switch (view.getId()) {
            case R.id.changed_btn:
                if (biaozhi == 1) {
                    changedPassWord(user.mobile);
                } else if (biaozhi == 2) {
                    if (TextUtils.isEmpty(mEtMobile.getText().toString())) {
                        ToastUtils.show(ChangedPasswordActivity.this, R.string.hint_mobile);
                        mEtMobile.requestFocus();
                        return;
                    }
                    changedPassWord(mEtMobile.getText().toString());
                }

                break;
        }
    }

    private void changedPassWord(String mobile) {
        mobileNumber = mobile;
        identifyNumber = mEtIdentifyNumber.getText().toString();
        password = mEtInputPassword.getText().toString();
        if (!CheckUtils.isMobileNO(mobileNumber)) {
            if (!TextUtils.isEmpty(mobileNumber)) {
                ToastUtils.show(ChangedPasswordActivity.this, R.string.label_legal_mobile);
            }
            return;
        }
        if (TextUtils.isEmpty(identifyNumber)) {
            ToastUtils.show(ChangedPasswordActivity.this, R.string.label_empty_identify);
            mEtIdentifyNumber.requestFocus();
            return;
        }
        if (identifyNumber.length() != 6) {
            ToastUtils.show(ChangedPasswordActivity.this, R.string.label_six_identify);
            mEtIdentifyNumber.setSelection(identifyNumber.length());
            mEtIdentifyNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(ChangedPasswordActivity.this, R.string.label_empty_password);
            mEtInputPassword.requestFocus();
            return;
        }
        if (password.length() < 6 || password.length() > 20) {
            ToastUtils.show(ChangedPasswordActivity.this, R.string.label_six_twenty_identify);
            mEtInputPassword.setSelection(password.length());
            mEtInputPassword.requestFocus();
            return;
        }
        if (NetworkUtils.isNetworkAvaiable(ChangedPasswordActivity.this)) {
            CustomDialogFragment.getInstance(getSupportFragmentManager(), ChangedPasswordActivity.class.getName());
            Map<String, String> map = new HashMap<String, String>();
            map.put("mobile", mobileNumber);
            map.put("verifyCode", identifyNumber);
            map.put("pwd", password);
            ApiUtils.post(getApplicationContext(), URLConstants.RE_PWD, map, UserResultInfo.class, new Listener<UserResultInfo>() {
                @Override
                public void onResponse(UserResultInfo response) {
                    CustomDialogFragment.dismissDialog();
                    if (O2OUtils.checkResponse(ChangedPasswordActivity.this, response)) {
                        ToastUtils.show(ChangedPasswordActivity.this, R.string.label_changedpassword_success);
                        O2OUtils.cacheUserData(ChangedPasswordActivity.this, response);
                        setResult(RESULT_OK);
                        finishActivity();
                    }
                }

            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialogFragment.dismissDialog();
                    ToastUtils.show(ChangedPasswordActivity.this, R.string.label_change_fail);
                }
            });
        } else {
            ToastUtils.show(ChangedPasswordActivity.this, R.string.msg_error_network);
        }
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mTvGetIndentifyCode.setText(getString(R.string.lable_get_identifying));
            mTvGetIndentifyCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String value = getString(R.string.code_again, String.valueOf(millisUntilFinished / 1000));
            mTvGetIndentifyCode.setText(value);
        }
    }

}
