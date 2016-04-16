package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.Commission;
import com.fanwe.seallibrary.model.result.CommissionsListResult;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.RefreshLayoutUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 统计明细Activity
 */
public class StatisticsActivity extends BaseActivity implements TitleListener, SwipeRefreshLayout.OnRefreshListener, OnClickListener {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected boolean mLoadMore = false;
    private CommonAdapter<Commission> mAdapter;
    private List<Commission> list = new ArrayList<Commission>();
    private ListView mListView;
    private String month = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_refresh_empty);
        month = getIntent().getStringExtra(Constants.MONTH);
        initView();
        setTitleListener(this);
        setPageTag(TagManage.STATISTICS_ACTIVITY);
    }

    private void initView() {
        mListView = mViewFinder.find(android.R.id.list);
        mListView.setBackgroundColor(getResources().getColor(R.color.white));
        mListView.setPadding(getResources().getDimensionPixelSize(R.dimen.margin),0,0,0);
        mSwipeRefreshLayout = mViewFinder.find(R.id.swipe_container);
        mAdapter = new CommonAdapter<Commission>(this, list, R.layout.item_commission) {
            @Override
            public void convert(ViewHolder helper, Commission item, int position) {
                helper.setText(R.id.commission_date, item.createTime);
                helper.setText(R.id.commission_type, item.content);
                helper.setText(R.id.commission_content, getString(R.string.order_sn, item.orderSn));
                helper.setText(R.id.commission_amount, getString(R.string.yuan, item.money));
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
        RefreshLayoutUtils.initSwipeRefreshLayout(StatisticsActivity.this, mSwipeRefreshLayout, this, true);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(StatisticsActivity.this, StatisticsMonthActivity.class));
            }
        });

        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mAdapter.getCount() >= Constants.PAGE_SIZE
                        && mAdapter.getCount() % Constants.PAGE_SIZE == 0) {
                    loadData(false);
                }
            }
        });
        loadData(true);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_right:
                intent = new Intent(StatisticsActivity.this, StatisticsMonthActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        if (TextUtils.isEmpty(month)) {
            title.setText(R.string.income_statistics);
            ((TextView) right).setText(R.string.statistics_month);
            right.setOnClickListener(this);
        } else {
            if (month.length() == 6) {
                title.setText(month.substring(0, 4) + getString(R.string.year) + month.substring(4) + getString(R.string.month));
            }
        }

    }

    protected HashMap<String, String> getLoadParams() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(ParamConstants.PAGE, String.valueOf(list.size() / Constants.PAGE_SIZE + 1));
        if (!TextUtils.isEmpty(month)) {
            data.put("month", month);
        }
        return data;
    }

    protected boolean checkLoadState(boolean isRefresh) {
        if (mLoadMore) {
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(StatisticsActivity.this)) {
            ToastUtils.show(StatisticsActivity.this, R.string.msg_error_network);
            return false;
        }
        mLoadMore = true;
        if (isRefresh) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        return true;
    }

    protected void loadData(final boolean isRefresh) {
        if (!checkLoadState(isRefresh)) {
            return;
        }

        ApiUtils.post(StatisticsActivity.this, URLConstants.STATISTICSDETAIL, getLoadParams(), CommissionsListResult.class, new Listener<CommissionsListResult>() {

            @Override
            public void onResponse(CommissionsListResult response) {
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
                if (O2OUtils.checkResponse(StatisticsActivity.this, response)) {
                    if (!ArraysUtils.isEmpty(response.data)) {
                        if (isRefresh)
                            list.clear();
                        list.addAll(response.data);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(StatisticsActivity.this, R.string.common_not_more);
                    }
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }
}
