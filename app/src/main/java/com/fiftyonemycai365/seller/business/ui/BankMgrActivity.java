package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.BankInfo;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.BankResultInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class BankMgrActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    private ShopInfo mShopInfo;
    private BankInfo mBankInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_mgr);
        setTitleListener(this);
        mViewFinder.onClick(R.id.btn_add_bank, this);
        mViewFinder.onClick(R.id.iv_edit, this);
        mViewFinder.onClick(R.id.iv_del, this);
        mShopInfo = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        setPageTag(TagManage.BANK_MGR_ACTIVITY);
        loadData();
    }

    private void initViewData(BankInfo bankInfo) {
        mBankInfo = bankInfo;
        if(bankInfo == null){
            mViewFinder.find(R.id.cv_bank).setVisibility(View.INVISIBLE);
            mViewFinder.find(R.id.ll_add_bank).setVisibility(View.VISIBLE);
            return;
        }
        mViewFinder.find(R.id.cv_bank).setVisibility(View.VISIBLE);
        mViewFinder.find(R.id.ll_add_bank).setVisibility(View.INVISIBLE);
        mViewFinder.setText(R.id.tv_bank_name, mBankInfo.bank);
        mViewFinder.setText(R.id.tv_bank_no, mBankInfo.bankNo);
        mViewFinder.setText(R.id.tv_user_name, mBankInfo.name);
    }

    protected void loadData() {

        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), BankMgrActivity.class.getName());

        ApiUtils.post(getApplicationContext(), URLConstants.USER_BANK_INFO, null, BankResultInfo.class, new Response.Listener<BankResultInfo>() {
            @Override
            public void onResponse(BankResultInfo response) {
                CustomDialogFragment.dismissDialog();
                initViewData(response.data);
                if (!O2OUtils.checkResponse(getApplicationContext(), response)) {
                    return;
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(BankMgrActivity.this, R.string.label_load_fail);
            }
        });

    }

    protected void removeBank() {

        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        if(mBankInfo == null){
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), BankMgrActivity.class.getName());
        Map<String, String> map = new HashMap<String, String>(1);
        map.put("id", String.valueOf(mBankInfo.id));
        ApiUtils.post(getApplicationContext(), URLConstants.USER_BANK_DEL, map, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult response) {
                CustomDialogFragment.dismissDialog();
                if (!O2OUtils.checkResponse(getApplicationContext(), response)) {
                    return;
                }
                initViewData(null);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(BankMgrActivity.this, R.string.msg_error_del);
            }
        });

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_bank_mgr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_bank:
            case R.id.iv_edit:
                eidtBankInfo();
                break;
            case R.id.iv_del:
                Dialog alertDialog = new AlertDialog.Builder(this).
                        setMessage(R.string.hint_bank_remove).
                        setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeBank();
                            }
                        }).
                        setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).
                        create();
                alertDialog.show();
                break;
        }
    }

    private void eidtBankInfo(){
        Intent intent = new Intent(this, AddBankActivity.class);
        if(mBankInfo != null) {
            intent.putExtra(Constants.EXTRA_DATA, mBankInfo);
        }
        intent.putExtra(Constants.EXTRA_SHOP, mShopInfo);
        startActivityForResult(intent, AddBankActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case AddBankActivity.REQUEST_CODE:
//                if(data != null) {
//                    BankInfo bankInfo = data.getParcelableExtra(Constants.EXTRA_DATA);
//                    initViewData(bankInfo);
//                }
                loadData();
                break;
        }
    }
}
