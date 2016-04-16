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
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by admin on 2015/10/28.
 * 意见反馈 activity
 */
public class FeedBackActivity extends BaseActivity implements BaseActivity.TitleListener{

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        setTitleListener(this);
        mEditText = mViewFinder.find(R.id.feed_back_text);
        setPageTag(TagManage.FEED_BACK_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.opinion);
        ((TextView)right).setText(getString(R.string.commit));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subMitFeedback();
            }
        });
    }

    private void subMitFeedback(){
        if (("").equals(mEditText.getText().toString())){
            ToastUtils.show(FeedBackActivity.this,getString(R.string.msg_opinion_not_null));
            return;
        }
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("content",mEditText.getText().toString());
        data.put("deviceType","Android");
        ApiUtils.post(FeedBackActivity.this, URLConstants.FEEDBACK_CREATE, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult response) {
                ToastUtils.show(FeedBackActivity.this,getString(R.string.opinion_finsh));
                finishActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(FeedBackActivity.this,getString(R.string.msg_opinion_err));
            }
        });
    }
}
