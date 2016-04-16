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
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 回复
 * Created by zzl on 2015/9/6.
 */
public class ReviewsReplyActivity extends BaseActivity implements BaseActivity.TitleListener {

    private EditText edt;

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_comment_reply_succeed));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_reply);
        setTitleListener(this);

        edt = (EditText)findViewById(R.id.edt_reply);
        mViewFinder.find(R.id.btn_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideSoftInput(ReviewsReplyActivity.this);
                reply();
            }
        });
        setPageTag(TagManage.REVIEWS_REPLY_ACTIVITY);
    }

    private void reply() {
        if (TextUtils.isEmpty(edt.getText().toString().trim())) {
            ToastUtils.show(ReviewsReplyActivity.this, R.string.opinion_edi_text);
            return;
        }
        if (NetworkUtils.isNetworkAvaiable(ReviewsReplyActivity.this)){
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", "1");
            map.put("content", edt.getText().toString().trim());
            CustomDialogFragment.getInstance(getSupportFragmentManager(),  ReviewsReplyActivity.class.getName());
                        ApiUtils.post(ReviewsReplyActivity.this, URLConstants.STAFF_REPLY, map, BaseResult.class, new Response.Listener<BaseResult>() {
                            @Override
                            public void onResponse(BaseResult response) {
                                if (O2OUtils.checkResponse(ReviewsReplyActivity.this.getApplicationContext(), response)) {
                                    CustomDialogFragment.dismissDialog();
                                    ToastUtils.show(ReviewsReplyActivity.this, response.msg);
                                    finishActivity();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialogFragment.dismissDialog();
                    ToastUtils.show(ReviewsReplyActivity.this, R.string.label_update_fail);
                }
            });


        }else{
            ToastUtils.show(ReviewsReplyActivity.this, R.string.msg_error_network);
        }

    }
}
