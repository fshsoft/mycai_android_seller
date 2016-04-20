package com.fiftyonemycai365.seller.business.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2015/10/29.
 */
public class CheckOldMobileActivity extends BaseActivity implements View.OnClickListener,BaseActivity.TitleListener {

    private EditText mEditCode;
    private Button mGetCode,mSureChange;
    private MyCount mc;
    private UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_old_mobile);
        setTitleListener(this);
        user = PreferenceUtils.getObject(CheckOldMobileActivity.this,UserInfo.class);
        initViews();
        setPageTag(TagManage.CHECK_OLD_MOBILE_ACTIVITY);
    }

    private void initViews() {
        mEditCode = mViewFinder.find(R.id.check_old_mobile_edit_code);
        mGetCode = mViewFinder.find(R.id.check_old_mobile_get_code);
        mGetCode.setOnClickListener(this);
        mSureChange = mViewFinder.find(R.id.check_new_mobile);
        mSureChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.check_old_mobile_get_code:
                mc = new MyCount(61000, 1000);
                if (NetworkUtils.isNetworkAvaiable(CheckOldMobileActivity.this)) {
                    if (user.mobile != null){
                        CustomDialogFragment.getInstance(getSupportFragmentManager(), PhoneLoginActivity.class.getName());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("mobile", user.mobile);
                        ApiUtils.post(getApplicationContext(), URLConstants.INENTIFYING, map, null, new Response.Listener<Object>() {

                            @Override
                            public void onResponse(Object response) {
                                CustomDialogFragment.dismissDialog();
                                ToastUtils.show(CheckOldMobileActivity.this, R.string.label_send_success);
                                mGetCode.setEnabled(false);
                                mc.start();
                            }

                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                CustomDialogFragment.dismissDialog();
                            }
                        });
                    }

                } else {
                    ToastUtils.show(CheckOldMobileActivity.this, R.string.msg_error_network);
                }
               break;
            case R.id.check_new_mobile:
                if (("").equals(user.mobile)){
                    return;
                }
                Intent intent = new Intent(CheckOldMobileActivity.this,ChangedMobileActivity.class);
                intent.putExtra("oldMobile",user.mobile);
                startActivity(intent);
                finishActivity();
                break;
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_check_old_phone_number));
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
