package com.yizan.community.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ConfigInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 意见反馈
 * Created by zzl on 2015/9/10.
 */
public class SuggestionsActivity extends BaseActivity implements BaseActivity.TitleListener ,View.OnClickListener{

    private TextView mTell;
    private Button mCommit;
    private EditText mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        setTitleListener(this);

        mTell = (TextView)findViewById(R.id.suggestions_tell);
        final ConfigInfo configInfo = PreferenceUtils.getObject(SuggestionsActivity.this,ConfigInfo.class);
        mTell.setText(configInfo.serviceTel);
        mTell.setOnClickListener(this);

        mCommit = (Button)findViewById(R.id.suggestions_commit);
        mCommit.setOnClickListener(this);

        mContent = (EditText)findViewById(R.id.suggestions_content);
        setPageTag(TagManage.SUGGESTIONS_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.opinion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.suggestions_tell:
                try {
                    IntentUtils.dial(SuggestionsActivity.this,((TextView) v).getText().toString().replace(" ", "").replace("-", ""));
                }catch (Exception e){

                }
                break;
            case R.id.suggestions_commit:
                sendSuggestions();
                break;
        }
    }

    private void sendSuggestions() {
        if(("").equals(mContent.getText().toString())){
            ToastUtils.show(SuggestionsActivity.this,R.string.opinion_edi_text);
            return;
        }
        Map<String,String> data = new HashMap<String,String>();
        data.put("content",mContent.getText().toString().trim());
        data.put("deviceType","android");
        CustomDialogFragment.getInstance(getSupportFragmentManager(), SuggestionsActivity.class.getName());
        ApiUtils.post(SuggestionsActivity.this, URLConstants.FEEDBACK_CREATE, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult response) {
                ToastUtils.show(SuggestionsActivity.this,R.string.opinion_finsh);
                CustomDialogFragment.dismissDialog();
                finishActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(SuggestionsActivity.this, R.string.opinion_error);
            }
        });
    }
}
