package com.yizan.community.business.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yizan.community.business.R;

public abstract class TextInputActivity extends BaseActivity implements View.OnClickListener, BaseActivity.TitleListener{
    protected Button mBtnCommit;
    protected EditText mEtContent;
    protected TextView mTvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input);

        mBtnCommit = mViewFinder.find(R.id.btn_commit);
        mEtContent = mViewFinder.find(R.id.et_content);
        setTitleListener(this);

        initView();
    }

    protected void setTitle(String title){
        if(mTvTitle != null){
            mTvTitle.setText(title);
        }
    }

    protected String getContent(){
        if(mEtContent != null){
            return mEtContent.getText().toString();
        }
        return "";
    }

    protected void setContentHint(String hint){
        if(mEtContent != null){
            mEtContent.setHint(hint);
        }
    }


    protected abstract void initView();
    protected abstract void onCommit();
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                onCommit();
                break;
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        mTvTitle = title;
    }
}
