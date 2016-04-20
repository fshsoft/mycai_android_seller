package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;

public class EditShopDeliveryFeeActivity extends BaseActivity implements BaseActivity.TitleListener {
    private ShopInfo mShopInfo;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_delivery_fee);
        setTitleListener(this);
        mShopInfo = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        mEditText = mViewFinder.find(R.id.et_value);
        mEditText.setFilters(new InputFilter[]{lengthfilter});
        if (mShopInfo == null) {
            finishActivity();
            return;
        }
        mEditText.setText( String.valueOf(mShopInfo.deliveryFee));
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                mEditText.requestFocus();
//
//            }
//        }, 200);
        setPageTag(TagManage.EDIT_SHOP_DELIVERY_FEE_ACTIVITY);
    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_edit_shop_delivery_fee);
        ((Button) right).setText(R.string.save);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editShop();
            }
        });
    }

    private void editShop() {
        String value = mEditText.getText().toString();
        if (TextUtils.isEmpty(value)) {
            mShopInfo.deliveryFee = 0;
        } else {
            try {
                mShopInfo.deliveryFee = Double.parseDouble(value);
            } catch (Exception e) {

            }
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopIntroductionActivity.class.getName());
        HashMap<String, Object> data = new HashMap<>();
        data.put("shopdatas", mShopInfo);
        ApiUtils.post(this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    ToastUtils.show(getApplicationContext(), getString(R.string.msg_update_scceed));
                    setResult(Activity.RESULT_OK);
                    finishActivity();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(EditShopDeliveryFeeActivity.this, R.string.msg_error_update);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    /**
     * 设置小数位数控制
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
