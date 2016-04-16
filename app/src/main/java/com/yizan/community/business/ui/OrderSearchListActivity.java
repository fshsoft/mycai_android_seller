package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.common.ParamConstants;
import com.yizan.community.business.R;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.FragmentUtil;

/**
 * 订单搜索结果列表ListActivity
 */
public class OrderSearchListActivity extends BaseActivity implements BaseActivity.TitleListener {
    private String keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        Intent data = getIntent();
        Bundle args = new Bundle(1);
        keywords = data.getStringExtra(ParamConstants.DATE);
        if (TextUtils.isEmpty(keywords)) {
            keywords = data.getStringExtra(ParamConstants.KEYWORDS);
            args.putString(ParamConstants.KEYWORDS, keywords);
        }else
            args.putString(ParamConstants.DATE, keywords);
        setTitleListener(this);
        OrderListFragment orderListFragment = OrderListFragment.newInstance(args);
        FragmentUtil.turnToFragment(getSupportFragmentManager(), R.id.frame_content, orderListFragment, false);
        setPageTag(TagManage.ORDER_SEARCH_LIST_ACTIVITY);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(keywords);
    }
}