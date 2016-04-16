package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.BankInfo;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.BankResultInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.R;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

public class AddBankActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {

    public static final int REQUEST_CODE = 0x401;
    private BankInfo mBankInfo;
    private Button mBtnIdentifyingCode;
    private String mMobileNum;
    private ShopInfo mShopInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        setTitleListener(this);

        mBankInfo = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        mShopInfo = this.getIntent().getParcelableExtra(Constants.EXTRA_SHOP);
        initViewData();
        mViewFinder.onClick(R.id.btn_identifying_code, this);
        mViewFinder.onClick(R.id.btn_commit, this);
        mBtnIdentifyingCode = mViewFinder.find(R.id.btn_identifying_code);

        UserInfo userInfo = PreferenceUtils.getObject(this, UserInfo.class);
        mMobileNum = userInfo.mobile;
        mViewFinder.setText(R.id.tv_phone, getString(R.string.bank_add_send_identifying_code) + mMobileNum);
        mViewFinder.setText(R.id.tv_user_name, mShopInfo.contacts);
        setPageTag(TagManage.ADDBANK_ACTIVITY);
    }

    private void initViewData() {
        if (mBankInfo == null) {
            return;
        }
        EditText et = mViewFinder.find(R.id.et_bank_name);
        et.setText(mBankInfo.bank);

        et = mViewFinder.find(R.id.et_bank_num);
        et.setText(mBankInfo.bankNo);
        TextView tv = mViewFinder.find(R.id.tv_user_name);
        tv.setText(mBankInfo.name);

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_add_bank);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_identifying_code:
                loadIdentifyingCode();
                break;
            case R.id.btn_commit:
                commitData();
                break;
        }
    }

    protected void commitData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        Map<String, String> map = new HashMap<String, String>();

        map.put("name", mShopInfo.contacts);
        EditText et = mViewFinder.find(R.id.et_bank_name);
        String value = et.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            ToastUtils.show(this, R.string.err_bank_name);
            return;
        }
        map.put("bank", value);
        et = mViewFinder.find(R.id.et_bank_num);
        value = et.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            ToastUtils.show(this, R.string.err_bank_num);
            return;
        }
        map.put("bankNo", value);

        et = mViewFinder.find(R.id.et_identifying_code);
        value = et.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            ToastUtils.show(this, R.string.label_identifying_code);
            return;
        }
        map.put("verifyCode", value);
        map.put("mobile", mMobileNum);
        if (mBankInfo != null) {
            map.put("id", String.valueOf(mBankInfo.id));
        }

        CustomDialogFragment.getInstance(getSupportFragmentManager(), AddBankActivity.class.getName());

        ApiUtils.post(getApplicationContext(), URLConstants.USER_BANK_EDIT, map, BankResultInfo.class, new Response.Listener<BankResultInfo>() {

            @Override
            public void onResponse(BankResultInfo response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_DATA, response.data);
                    setResult(Activity.RESULT_OK);
                    finishActivity();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    MyCount mMyCount;

    protected void loadIdentifyingCode() {
        mMyCount = new MyCount(61000, 1000);
        // mc.start();
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), AddBankActivity.class.getName());
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mMobileNum);
        ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, BaseResult.class, new Response.Listener<BaseResult>() {

            @Override
            public void onResponse(BaseResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    ToastUtils.show(AddBankActivity.this, R.string.label_send_success);
                    mBtnIdentifyingCode.setEnabled(false);
                    mMyCount.start();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            mBtnIdentifyingCode.setText(R.string.lable_get_identifying);
            mBtnIdentifyingCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mBtnIdentifyingCode.setText(getString(R.string.code_again, String.valueOf(millisUntilFinished / 1000)));
        }
    }
}
