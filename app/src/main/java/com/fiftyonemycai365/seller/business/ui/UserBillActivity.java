package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.viewpagerindicator.TabPageIndicator;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.TabBillListFragmentPagerAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

/**
 * 我的账单Activity
 */
public class UserBillActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private TabBillListFragmentPagerAdapter mAdapter;
    private ShopInfo mShopInfo;
    public static UserBillActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bill);
        setTitleListener(this);
        activity = this;
        initView();
        setPageTag(TagManage.USER_BILL_ACTIVITY);
    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.title_activity_user_bill));
    }

    private void initView() {
        mIndicator = mViewFinder.find(R.id.indicator);
        mViewPager = mViewFinder.find(R.id.viewpager);
        mAdapter = new TabBillListFragmentPagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setVisibility(View.VISIBLE);

        mViewFinder.onClick(R.id.btn_fetch, this);
        mViewFinder.onClick(R.id.btn_recharge, this);
        mShopInfo = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);

        updateBalance();
    }

    /**
     * 刷新余额
     */
    private void updateBalance() {
        if (mShopInfo != null) {
            mViewFinder.setText(R.id.tv_all_bill, getString(R.string.msg_RMB, NumFormat.formatPrice(mShopInfo.balance)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fetch:
                if (mShopInfo != null) {
                    Intent intent = new Intent(this, BillFetchActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, mShopInfo);
                    startActivityForResult(intent, 12);
                }
                break;
            case R.id.btn_recharge:
                startActivityForResult(new Intent(this, RechargeActivity.class), 13);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 13){
            loadData();
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (null != mShopInfo) {
                mShopInfo.balance = data.getDoubleExtra(Constants.EXTRA_DATA, 0);
                updateBalance();
            }
        }
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return ;
        }
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.SHOP_INFO, null,
                ShopResult.class, new Response.Listener<ShopResult>() {

                    @Override
                    public void onResponse(ShopResult response) {
                        if (O2OUtils.checkResponse(
                                UserBillActivity.this,
                                response)) {
                            mShopInfo = response.data;
                            updateBalance();
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(UserBillActivity.this, R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }
}
