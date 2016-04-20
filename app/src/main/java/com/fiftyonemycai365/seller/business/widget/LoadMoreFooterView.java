package com.fiftyonemycai365.seller.business.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiftyonemycai365.seller.business.R;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreUIHandler;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-10-22
 * Time: 13:52
 * FIXME
 */
public class LoadMoreFooterView extends RelativeLayout implements LoadMoreUIHandler {
    private TextView mTextView;
    private ProgressBar mProgressBar;

    public LoadMoreFooterView(Context context) {
        this(context, (AttributeSet)null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setupViews();
    }

    private void setupViews() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.layout_load_more, this);
        this.mTextView = (TextView)this.findViewById(R.id.tv_status);
        this.mProgressBar = (ProgressBar)this.findViewById(R.id.pb_loading);
    }

    public void onLoading(LoadMoreContainer container) {
        this.setVisibility(View.VISIBLE);
        this.mTextView.setText(in.srain.cube.R.string.cube_views_load_more_loading);
        this.mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        this.mProgressBar.setVisibility(View.GONE);
        if(!hasMore) {
            this.setVisibility(View.VISIBLE);
            if(empty) {
                this.mTextView.setText(in.srain.cube.R.string.cube_views_load_more_loaded_empty);
            } else {
                this.mTextView.setText(in.srain.cube.R.string.cube_views_load_more_loaded_no_more);
            }
        } else {
            this.setVisibility(View.INVISIBLE);
        }

    }

    public void onWaitToLoadMore(LoadMoreContainer container) {
        this.setVisibility(View.VISIBLE);
        this.mTextView.setText(in.srain.cube.R.string.cube_views_load_more_click_to_load_more);
        this.mProgressBar.setVisibility(View.GONE);
    }

}
