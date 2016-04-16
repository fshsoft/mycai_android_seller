package com.yizan.community.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by admin on 2015/11/2.
 * 修改店铺介绍
 */
public class EditShopIntroductionActivity extends BaseActivity implements BaseActivity.TitleListener {

    private TextView mText;
    private EditText mEdit;
    private ShopInfo shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_edit_text);
        setTitleListener(this);
        shop = this.getIntent().getExtras().getParcelable("text");
        initViews();
        setPageTag(TagManage.EDIT_SHOP_INTRODUCTION_ACTIVITY);
    }

    private void initViews() {
        mText = mViewFinder.find(R.id.common_edit_text_tv);
        mEdit = mViewFinder.find(R.id.common_edit_text_edt);

        mText.setText(getString(R.string.msg_store_introduce));
        mEdit.setText(shop.brief);
    }

    private void editShop(){
        shop.brief = mEdit.getText().toString();
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopIntroductionActivity.class.getName());
        HashMap<String,Object> data = new HashMap<>();
        data.put("shopdatas", shop);
        ApiUtils.post(EditShopIntroductionActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
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
                ToastUtils.show(EditShopIntroductionActivity.this, R.string.msg_error_update);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_store_introduce_2));
        ((TextView)right).setText(getString(R.string.save));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editShop();
//                Intent intent = new Intent();
//                intent.putExtra("text",mEdit.getText().toString());
//                setResult(RESULT_OK, intent);
//                finish();
//                finishActivity();
            }
        });
    }
}
