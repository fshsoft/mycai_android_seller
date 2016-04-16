package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.common.Constants;
import com.yizan.community.business.R;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.util.ToastUtils;

/**
 * 充值Activity
 */
public class RechargeActivity extends BaseActivity implements BaseActivity.TitleListener {

    public static RechargeActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        activity = this;
        setTitleListener(this);
        mViewFinder.onClick(R.id.btn_next, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = mViewFinder.find(R.id.et_price);
                if (TextUtils.isEmpty(et.getText())) {
                    ToastUtils.show(RechargeActivity.this, "请输入有效金额");
                    return;
                }
                try {
                    int money = Integer.parseInt(et.getText().toString());
                    if (money <= 0) {
                        ToastUtils.show(RechargeActivity.this, "充值金额必须大于0");
                        return;
                    }
                    Intent intent = new Intent(RechargeActivity.this, PayWayActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, money);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setPageTag(TagManage.RECHARGE_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("我要充值");
    }
}
