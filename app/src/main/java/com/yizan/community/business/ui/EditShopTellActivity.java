package com.yizan.community.business.ui;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.yizan.community.business.util.CheckUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by admin on 2015/11/2.
 * 修改店铺电话
 */
public class EditShopTellActivity extends BaseActivity implements BaseActivity.TitleListener {

    private TextView mText;
    private EditText mEdit;
    private ShopInfo shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_edit_number);
        setTitleListener(this);
        shop = this.getIntent().getExtras().getParcelable("text");
        initViews();
        setPageTag(TagManage.EDIT_SHOP_TELL_ACTIVITY);
    }

    private void initViews() {
        mText = mViewFinder.find(R.id.common_edit_number_tv);
        mEdit = mViewFinder.find(R.id.common_edit_number_edt);

        mText.setText(getString(R.string.msg_store_mobile));
        if (shop != null){
            mEdit.setText(shop.tel);
        }else{
            mEdit.setText("");
        }

    }

    private void editShop(){
        if (TextUtils.isEmpty(mEdit.getText().toString())) {
            ToastUtils.show(EditShopTellActivity.this, R.string.hint_mobile);
            mEdit.requestFocus();
            return;
        }
        if (!CheckUtils.isMobileNO(mEdit.getText().toString())) {
            ToastUtils.show(EditShopTellActivity.this, R.string.label_legal_mobile);
            mEdit.setSelection(mEdit.getText().toString().length());
            mEdit.requestFocus();
            return;
        }
        shop.tel = mEdit.getText().toString();
        HashMap<String,Object> data = new HashMap<>();
        data.put("shopdatas", shop);
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopTellActivity.class.getName());
        ApiUtils.post(EditShopTellActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
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
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(EditShopTellActivity.this, R.string.msg_error_update);
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_store_mobile_2));
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
