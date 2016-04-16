package com.yizan.community.business.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.TagManage;

/**
 * 消息详情Activity
 *
 * @author atlas
 * @email atlas.tufei@gmail.com
 * @time 2015-5-15 下午8:22:59
 */
public class InformDetailsActivity extends BaseActivity implements TitleListener {
    private TextView mDateTv, mContentDetailTv;
    private String titlesss;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_details);
        initView();
        setTitleListener(this);
        setPageTag(TagManage.INFORM_DETAILS_ACTIVITY);
    }

    private void initView() {
        mDateTv = (TextView) findViewById(R.id.msg_date_text);
        mContentDetailTv = (TextView) findViewById(R.id.msg_content_text);
        Bundle b = getIntent().getExtras();
        titlesss = b.getString("title");
        mContentDetailTv.setText(Html.fromHtml(b.getString("content")));
        mDateTv.setText(b.getString("date"));
    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(titlesss);
    }

}
