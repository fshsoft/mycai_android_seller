package com.yizan.community.business.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.GoodsCate;
import com.yizan.community.business.util.TagManage;

public class GoodsSearchActivity extends BaseActivity implements View.OnClickListener, BaseActivity.TitleListener {
    private String mKeywords;
    private GoodsCate mGoodsCate;
    private GoodsContainerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        mKeywords = getIntent().getStringExtra(Constants.EXTRA_KEYS);
        mGoodsCate = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        setTitleListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mFragment = GoodsContainerFragment.newInstance(mGoodsCate, mKeywords);
        fragmentTransaction.add(R.id.fl_container, mFragment);
        fragmentTransaction.commit();
        setPageTag(TagManage.GOODS_SEARCH_ACTIVITY);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        if(TextUtils.isEmpty(mKeywords)){
            mKeywords = getString(R.string.app_name);
        }
        title.setText(mKeywords);
    }

}
