package com.yizan.community.business.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ConfigInfo;
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
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午9:15:07
 */
public class PhoneLoginActivity extends BaseActivity implements TitleListener, OnClickListener, OnTouchListener {
    public static final int REQUEST_CODE = 0x101;
    private EditText mEtLoginMobile, mEtLoginIdentifyCode;
    private String mStrMobileNum, mStrIdentifyCode;
    private TextView mTvGetIndentifyCode;
    private SpannableString spanText;
    private MyCount mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        setTitleListener(this);
        mEtLoginMobile = (EditText) findViewById(R.id.login_mobile);
        mEtLoginIdentifyCode = (EditText) findViewById(R.id.identify_code_et);
        setViewClickListener(R.id.login_btn, this);
        setViewClickListener(R.id.tv_forget_pwd, this);
//        setProtocol();

        TextView mProtocol = (TextView)findViewById(R.id.phone_login_protocol);
        final String tempStr = getString(R.string.login_protocol_pink);
        spanText = new SpannableString(getString(R.string.login_protocol,tempStr));
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(PhoneLoginActivity.this, WebViewActivity.class);
                String url = PreferenceUtils.getObject(PhoneLoginActivity.this, ConfigInfo.class).protocolUrl;
                intent.putExtra(Constants.EXTRA_URL, url);
                startActivity(intent);
            }
        },spanText.length()-tempStr.length(), spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)), spanText.length() - tempStr.length(), spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mProtocol.setText(spanText);
        mProtocol.setMovementMethod(LinkMovementMethod.getInstance());

        mTvGetIndentifyCode = (TextView) findViewById(R.id.get_identifying);
        mTvGetIndentifyCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AppUtils.hideSoftInput(PhoneLoginActivity.this);
                String mobileNumber = mEtLoginMobile.getText().toString();
                if (mobileNumber == null || mobileNumber.length() == 0) {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.label_empty_mobile);
                    mEtLoginMobile.requestFocus();
                    return;
                }
                if (!CheckUtils.isMobileNO(mobileNumber)) {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.label_legal_mobile);
                    mEtLoginMobile.setSelection(mobileNumber.length());
                    mEtLoginMobile.requestFocus();
                    return;
                }
                mc = new MyCount(61000, 1000);
                // mc.start();
                if (NetworkUtils.isNetworkAvaiable(PhoneLoginActivity.this)) {
                    CustomDialogFragment.getInstance(getSupportFragmentManager(),  PhoneLoginActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", mobileNumber);
                    ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, BaseResult.class, new Listener<BaseResult>() {

                        @Override
                        public void onResponse(BaseResult response) {
                            CustomDialogFragment.dismissDialog();
                            if(O2OUtils.checkResponse(getApplicationContext(), response)) {
                                ToastUtils.show(PhoneLoginActivity.this, R.string.label_send_success);
                                mTvGetIndentifyCode.setEnabled(false);
                                mc.start();
                            }
                        }

                    }, new ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CustomDialogFragment.dismissDialog();
                        }
                    });
                } else {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.msg_error_network);
                }
            }
        });

        String phone = this.getIntent().getStringExtra(Constants.DATA);
        if (!TextUtils.isEmpty(phone)) {
            this.mEtLoginMobile.setText(phone);
        }
        setPageTag(TagManage.PHONE_LOGIN_ACTIVITY);
//        test();
    }

//    private void setProtocol() {
//        TextView view = (TextView) findViewById(R.id.login_protocol);
//        SpannableString spanText = new SpannableString("点击“登录”，即表示您同意《易赞O2O免责声明》");
//        spanText.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Intent intent = new Intent(PhoneLoginActivity.this, WebViewActivity.class);
//                intent.putExtra("url", URLConstants.URL_DISCLAIMER);
//                startActivity(intent);
//            }
//        }, 13, spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), 13, spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        view.setText(spanText);
//        view.setMovementMethod(LinkMovementMethod.getInstance());
//
//        findViewById(R.id.ll_container).setOnTouchListener(this);
//    }

    private void test() {
        // FIXME 测试数据
        mEtLoginMobile.setText("13201236549");
        mEtLoginIdentifyCode.setText("123456");
    }

    @Override
    public void onClick(View v) {
        AppUtils.hideSoftInput(PhoneLoginActivity.this);
        switch (v.getId()) {
            case R.id.login_btn:
                mStrMobileNum = mEtLoginMobile.getText().toString();
                mStrIdentifyCode = mEtLoginIdentifyCode.getText().toString();
                if (TextUtils.isEmpty(mStrMobileNum)) {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.hint_mobile);
                    mEtLoginMobile.requestFocus();
                    return;
                }
                if (!CheckUtils.isMobileNO(mStrMobileNum)) {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.label_legal_mobile);
                    mEtLoginMobile.setSelection(mStrMobileNum.length());
                    mEtLoginMobile.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mStrIdentifyCode)) {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.label_identifying_code);
                    mEtLoginIdentifyCode.requestFocus();
                    return;
                }

                if (NetworkUtils.isNetworkAvaiable(PhoneLoginActivity.this)) {
                    CustomDialogFragment.getInstance(getSupportFragmentManager(),PhoneLoginActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>(2);
                    map.put("mobile", mStrMobileNum);
                    map.put("verifyCode", mStrIdentifyCode);
                    ApiUtils.post(getApplicationContext(), URLConstants.QUICK_LOGIN, map, UserResultInfo.class, new Listener<UserResultInfo>() {
                        @Override
                        public void onResponse(UserResultInfo response) {
                            CustomDialogFragment.dismissDialog();
                            if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                                if (null != response.data) {
                                    ToastUtils.show(PhoneLoginActivity.this, R.string.label_loging_success);
                                    O2OUtils.cacheUserData(PhoneLoginActivity.this, response);
                                    Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    setResult(RESULT_OK);
                                    finishActivity();
                                } else
                                    ToastUtils.show(PhoneLoginActivity.this, R.string.label_login_fail);

                            }
                        }

                    }, new ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CustomDialogFragment.dismissDialog();
                            ToastUtils.show(PhoneLoginActivity.this, R.string.label_login_fail);
                        }
                    });
                } else {
                    ToastUtils.show(PhoneLoginActivity.this, R.string.msg_error_network);
                }

                break;
            case R.id.tv_forget_pwd:
                Intent intent = new Intent(PhoneLoginActivity.this, ChangedPasswordActivity.class);
                String mobile = mEtLoginMobile.getText().toString();
                if (!TextUtils.isEmpty(mobile)) {
                    intent.putExtra(Constants.DATA, mobile);
                }
                startActivityForResult(intent, ChangedPasswordActivity.REQUEST_CODE);
                break;

            case R.id.title_right:
                Intent intent2 = new Intent(PhoneLoginActivity.this, LoginActivity.class);
                startActivityForResult(intent2, LoginActivity.REQUEST_CODE);
                break;
        }

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        left.setVisibility(View.GONE);
        right.setOnClickListener(this);
        title.setText(R.string.title_phone_login);
        ((TextView)right).setText(R.string.title_pwd_login);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            this.hideSoftInputView();
        }
        return false;
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            mTvGetIndentifyCode.setTextColor(Color.WHITE);
            mTvGetIndentifyCode.setText(R.string.lable_get_identifying);
            mTvGetIndentifyCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTvGetIndentifyCode.setTextColor(Color.GRAY);
            mTvGetIndentifyCode.setText(getString(R.string.code_again,String.valueOf( millisUntilFinished / 1000)));
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

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 != RESULT_OK) {
            return;
        }
        switch (arg0) {
            case LoginActivity.REQUEST_CODE:
            case ChangedPasswordActivity.REQUEST_CODE:
                Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
