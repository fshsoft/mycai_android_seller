package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;

/**
 * 店铺详细地址修改Activity
 */
public class EditShopAddressDetailActivity extends BaseActivity implements BaseActivity.TitleListener {

    private TextView mText;
    private EditText mEdit;
    private ShopInfo shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_edit_text);
        setTitleListener(this);
        shop = this.getIntent().getParcelableExtra("text");
        initViews();
        setPageTag(TagManage.EDIT_SHOP_ADDRESS_DETAIL_ACTIVITY);
    }

    private void initViews() {
        mText = mViewFinder.find(R.id.common_edit_text_tv);
        mEdit = mViewFinder.find(R.id.common_edit_text_edt);

        mText.setText(R.string.title_address_detail);
        mEdit.setText(shop.addressDetail);
    }

    private void editShop(){
        shop.addressDetail = mEdit.getText().toString();
        HashMap<String,Object> data = new HashMap<>();
        data.put("shopdatas",shop);
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopAddressDetailActivity.class.getName());
        ApiUtils.post(EditShopAddressDetailActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    ToastUtils.show(getApplicationContext(),getString(R.string.msg_update_scceed));
                    finishActivity();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(EditShopAddressDetailActivity.this,R.string.msg_error_update);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_address_detail);
        ((TextView)right).setText(R.string.save);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editShop();
            }
        });
    }
}
