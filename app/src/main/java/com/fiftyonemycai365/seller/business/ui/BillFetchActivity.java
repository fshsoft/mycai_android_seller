package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.BuildConfig;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.WithDraw;
import com.fanwe.seallibrary.model.result.FetchBillResult;
import com.fanwe.seallibrary.model.result.WithDrawResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现Activity
 */
public class BillFetchActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private ShopInfo mShopInfo;
    private TextView tv_money;
    private EditText et_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_fetch);
        setTitleListener(this);
        initView();
        setPageTag(TagManage.BIL_FETCH_ACTIVITY);
    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_bill_fetch);
        right.setVisibility(View.VISIBLE);
        ((Button)right).setText(R.string.title_activity_fetch_record);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BillFetchActivity.this, FetchRecordActivity.class));
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXTRA_DATA, mShopInfo.balance);
                setResult(Activity.RESULT_OK, intent);
                finishActivity();
            }
        });
    }

    private void initView() {

        if(!BuildConfig.BANK_CARD_NAME) {
            mViewFinder.find(R.id.tv_bank_name).setVisibility(View.GONE);
            mViewFinder.find(R.id.tv_user_name).setPadding((int)getResources().getDimension(R.dimen.margin), (int)getResources().getDimension(R.dimen.margin), 0, 0);
        }
        tv_money = mViewFinder.find(R.id.tv_money);
        EditText et = mViewFinder.find(R.id.et_money);
        et.setFilters(new InputFilter[] { lengthfilter });

        mShopInfo = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if(mShopInfo != null){
            mViewFinder.setText(R.id.tv_money, getString(R.string.msg_before_price, NumFormat.formatPrice(mShopInfo.balance)));
            mViewFinder.onClick(R.id.btn_fetch, this);
            mViewFinder.onClick(R.id.tv_fetch_all, this);
        }
        loadData();
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return ;
        }
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.USER_BANKINFO, null,
                WithDrawResult.class, new Response.Listener<WithDrawResult>() {

                    @Override
                    public void onResponse(WithDrawResult response) {
                        if (O2OUtils.checkResponse(
                                BillFetchActivity.this,
                                response)) {
                            initViewData(response.data);
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(BillFetchActivity.this, R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }
    private void initViewData(WithDraw withDraw){
        if(withDraw == null){
            return;
        }
        mViewFinder.setText(R.id.tv_user_name, getString(R.string.msg_opening_bank_name) + withDraw.name);
        mViewFinder.setText(R.id.tv_bank_no, getString(R.string.msg_card_number) + withDraw.bankNo);
        mViewFinder.setText(R.id.tv_bank_name, getString(R.string.msg_opening_bank) + withDraw.bankName);
        TextView tv = mViewFinder.find(R.id.tv_remark);
        tv.setText(Html.fromHtml(withDraw.notice));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fetch:
                EditText et = mViewFinder.find(R.id.et_money);
                double money = 0;
                try{
                    money = Double.parseDouble(et.getText().toString());
                }catch (Exception e){

                }
                fetchBill(money);
                break;
            case R.id.tv_fetch_all:
//                fetchBill(mShopInfo.balance);
                mViewFinder.setText(R.id.et_money, String.valueOf(mShopInfo.balance));
                break;
        }
    }

    private void fetchBill(double money) {

        if(money <= 0.0 ){
            ToastUtils.show(this, getString(R.string.tip_sum_not_zero));
            return ;
        }
        if(money > mShopInfo.balance ){
            ToastUtils.show(this, getString(R.string.tip_sum_not_exceed));
            return ;
        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return ;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("amount", money);
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.USER_WIDTHDRAW, params,
                FetchBillResult.class, new Response.Listener<FetchBillResult>() {

                    @Override
                    public void onResponse(FetchBillResult response) {
                        CustomDialogFragment.dismissDialog();
                        if (O2OUtils.checkResponse(
                                BillFetchActivity.this,
                                response)) {
                            ToastUtils.show(BillFetchActivity.this, R.string.msg_success_bill);
                            tv_money.setText(getString(R.string.msg_before_price, NumFormat.formatPrice(response.data.money)));
                            mShopInfo.balance = response.data.money;
                            Intent intent = new Intent();
                            intent.putExtra(Constants.EXTRA_DATA,response.data.money);
                            setResult(RESULT_OK,intent);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(BillFetchActivity.this, R.string.msg_error_update);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }

    /**
     *  设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String spilt = "\\.";
            String[] splitArray = dValue.split(spilt);
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - 2;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };
}
