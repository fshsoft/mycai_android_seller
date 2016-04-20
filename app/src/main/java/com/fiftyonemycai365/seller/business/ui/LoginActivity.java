package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
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
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.result.UserResultInfo;
import com.fiftyonemycai365.seller.business.ui.BaseActivity.TitleListener;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.CheckUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录Activity
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午9:15:07
 */
public class LoginActivity extends BaseActivity implements TitleListener, OnClickListener, OnTouchListener {
    private EditText mEtLoginMobile, mEtLoginPassword;
    private String mStrMobile, mStrPassword;
    public static final int REQUEST_CODE = 0x103;
    private SpannableString spanText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitleListener(this);
        mEtLoginMobile = (EditText) findViewById(R.id.login_mobile);
        mEtLoginPassword = (EditText) findViewById(R.id.login_pwd);
        setPageTag(TagManage.LOGIN_ACTIVITY);
        

//        TextView mProtocol = (TextView)findViewById(R.id.login_protocol);
//        final String tempStr = getString(R.string.login_protocol_pink);
//        spanText = new SpannableString(getString(R.string.login_protocol,tempStr));
//        spanText.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
//                String url = PreferenceUtils.getObject(LoginActivity.this, ConfigInfo.class).protocolUrl;
//                intent.putExtra(Constants.EXTRA_URL, url);
//                startActivity(intent);
//            }
//        },spanText.length()-tempStr.length(), spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)), spanText.length()-tempStr.length(), spanText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mProtocol.setText(spanText);
//        mProtocol.setMovementMethod(LinkMovementMethod.getInstance());


        setViewClickListener(R.id.login_btn, this);
        setViewClickListener(R.id.tv_forget_pwd, this);
       // test();
    }

//    private void setProtocol() {
//        TextView view = (TextView) findViewById(R.id.login_protocol);
//        SpannableString spanText = new SpannableString("点击“登录”，即表示您同意《易赞O2O免责声明》");
//        spanText.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
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
        mEtLoginMobile.setText("18696594097");
        mEtLoginPassword.setText("123123");
    }

    @Override
    public void onClick(View v) {
        AppUtils.hideSoftInput(LoginActivity.this);
        switch (v.getId()) {
            case R.id.login_btn:
                mStrMobile = mEtLoginMobile.getText().toString();
                mStrPassword = mEtLoginPassword.getText().toString();
                if (TextUtils.isEmpty(mStrMobile)) {
                    ToastUtils.show(LoginActivity.this, R.string.hint_mobile);
                    mEtLoginMobile.requestFocus();
                    return;
                }
                if (!CheckUtils.isMobileNO(mStrMobile)) {
                    ToastUtils.show(LoginActivity.this, R.string.label_legal_mobile);
                    mEtLoginMobile.setSelection(mStrMobile.length());
                    mEtLoginMobile.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mStrPassword)) {
                    ToastUtils.show(LoginActivity.this, R.string.hint_password);
                    mEtLoginPassword.requestFocus();
                    return;
                }

                if (mStrPassword.length() < 6 || mStrPassword.length() > 20) {
                    ToastUtils.show(LoginActivity.this, R.string.label_six_twenty_identify);
                    mEtLoginPassword.setSelection(mStrPassword.length());
                    mEtLoginPassword.requestFocus();
                    return;
                }
                if (NetworkUtils.isNetworkAvaiable(LoginActivity.this)) {
                    CustomDialogFragment.getInstance(getSupportFragmentManager(),  LoginActivity.class.getName());
                    Map<String, String> map = new HashMap<String, String>(2);
                    map.put("mobile", mStrMobile);
                    map.put("pwd", mStrPassword);
                    ApiUtils.post(getApplicationContext(), URLConstants.LOGIN, map, UserResultInfo.class, new Listener<UserResultInfo>() {
                        @Override
                        public void onResponse(UserResultInfo response) {
                            CustomDialogFragment.dismissDialog();
                            if (!O2OUtils.checkResponse(getApplicationContext(), response)) {
                                return;
                            }
                            if (null == response.data) {
//                                ToastUtils.show(LoginActivity.this, R.string.label_login_fail);
                                return;
                            }
                            ToastUtils.show(LoginActivity.this, R.string.label_loging_success);
                            O2OUtils.cacheUserData(LoginActivity.this, response);

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finishActivity();

                        }

                    }, new ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CustomDialogFragment.dismissDialog();
                            ToastUtils.show(LoginActivity.this, R.string.label_login_fail);
                        }
                    });
                } else {
                    ToastUtils.show(LoginActivity.this, R.string.msg_error_network);
                }

                break;
            case R.id.tv_forget_pwd:
                Intent intent = new Intent(LoginActivity.this, ChangedPasswordActivity.class);
                mStrMobile = mEtLoginMobile.getText().toString();
                if (!TextUtils.isEmpty(mStrMobile)) {
                    intent.putExtra(Constants.DATA, mStrMobile);
                }
                intent.putExtra("biaozhi",2);
                startActivityForResult(intent, ChangedPasswordActivity.REQUEST_CODE);
                break;

        }

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
		left.setVisibility(View.GONE);
        title.setText(R.string.login);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            this.hideSoftInputView();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 != RESULT_OK) {
            return;
        }
        switch (arg0) {
            case ChangedPasswordActivity.REQUEST_CODE:
                setResult(Activity.RESULT_OK);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finishActivity();
                break;
        }
    }

}
