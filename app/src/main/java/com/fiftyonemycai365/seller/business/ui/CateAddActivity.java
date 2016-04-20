package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import com.fanwe.seallibrary.model.GoodsCate;
import com.fanwe.seallibrary.model.Trade;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.TradeListResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CateAddActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private GoodsCate mGoodsCate;
    private List<Trade> mTradeList;
    private int mCurrTradeId = -1;
    private int mType = Constants.GOODS_TYPE.GOODS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_add);
        setTitleListener(this);

        mGoodsCate = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        mType = this.getIntent().getIntExtra(Constants.EXTRA_TYPE, Constants.GOODS_TYPE.GOODS);
        if (mGoodsCate != null) {
            EditText et = mViewFinder.find(R.id.et_cate_name);
            et.setText(mGoodsCate.name);
        }

        if(BuildConfig.MULTI_CATES){
            mViewFinder.find(R.id.ll_trade_cate).setVisibility(View.VISIBLE);
            loadData();
        }else{
            mViewFinder.find(R.id.ll_trade_cate).setVisibility(View.GONE);
        }
        setPageTag(TagManage.CATEADD_ACTIVITY);
    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_cate_add);
        right.setVisibility(View.VISIBLE);
        ((Button) right).setText(getString(R.string.save));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.SELLER_TRADE, null,
                TradeListResult.class, new Response.Listener<TradeListResult>() {

                    @Override
                    public void onResponse(TradeListResult response) {
                        if (O2OUtils.checkResponse(
                                CateAddActivity.this,
                                response)) {
                            initViewData(response.data);
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(CateAddActivity.this, R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }

    private void initViewData(List<Trade> list) {
        if (ArraysUtils.isEmpty(list)) {
            return;
        }
        mTradeList = list;
        mViewFinder.find(R.id.tv_trade_cate).setOnClickListener(this);
        if (mGoodsCate == null) {
            updateTrade(mTradeList.get(0));
        } else {
            for (Trade item : mTradeList) {
                if (item.cateId == mGoodsCate.tradeId) {
                    updateTrade(item);
                    return;
                }
            }
        }
        updateTrade(mTradeList.get(0));
    }

    private void saveData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }

        if (BuildConfig.MULTI_CATES && (ArraysUtils.isEmpty(mTradeList) || mCurrTradeId < 0)) {
            ToastUtils.show(this, getString(R.string.msg_select_business_classfiy));
            return;
        }

        EditText et = mViewFinder.find(R.id.et_cate_name);
        if (et.getText().toString().trim().length() < 2) {
            ToastUtils.show(this, getString(R.string.msg_error_classfiy_name));
            return;
        }
        Map<String, String> parmas = new HashMap<>();
        if(mGoodsCate != null) {
            parmas.put("id", String.valueOf(mGoodsCate.id));
        }
        if(BuildConfig.MULTI_CATES) {
            parmas.put("tradeId", String.valueOf(mCurrTradeId));
        }
        parmas.put("name", et.getText().toString().trim());
        parmas.put("type", String.valueOf(mType));
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.GOODS_CATES_EDIT, parmas,
                BaseResult.class, new Response.Listener<BaseResult>() {

                    @Override
                    public void onResponse(BaseResult response) {
                        if (O2OUtils.checkResponse(
                                CateAddActivity.this,
                                response)) {
                            if (mGoodsCate == null) {
                                ToastUtils.show(CateAddActivity.this, getString(R.string.msg_succ_add));
                            } else {
                                ToastUtils.show(CateAddActivity.this, getString(R.string.msg_update_scceed));
                            }
                            setResult(Activity.RESULT_OK);
                            finishActivity();
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(CateAddActivity.this, R.string.msg_error_update);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_trade_cate:
                showTradeList();
                break;
        }
    }

    private void updateTrade(Trade trade) {
        mCurrTradeId = trade.cateId;
        mViewFinder.setText(R.id.tv_trade_cate, trade.name);
    }

    private void showTradeList() {
        if (ArraysUtils.isEmpty(mTradeList)) {
            return;
        }
        ArrayList<String> list = new ArrayList<>();
        for (Trade trade : mTradeList) {
            list.add(trade.name);
        }
        String[] values = (String[])list.toArray(new String[list.size()]);
        Dialog alertDialog = new AlertDialog.Builder(this)
                .setItems( values, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateTrade(mTradeList.get(which));

                    }
                }).
                        setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).
                        create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }
}
