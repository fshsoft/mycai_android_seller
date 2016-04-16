package com.yizan.community.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.Commission;
import com.fanwe.seallibrary.model.result.CommissionListResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 月详情Activity
 * Created by admin on 2015/10/30.
 */
public class MonthDetailActivity extends BaseActivity implements BaseActivity.TitleListener {

    protected boolean mLoadMore = false;
    private ListView mListView;
    private CommonAdapter<Commission> mAdapter;
    private List<Commission> list = new ArrayList<Commission>();
    private String month,yearMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_statictas);
        month = this.getIntent().getStringExtra("month");
        yearMonth = this.getIntent().getStringExtra("yearMonth");
        setTitleListener(this);
        initViews();
        setPageTag(TagManage.MONTH_DETAIL_ACTIVITY);
    }

    private void initViews() {
        mListView = mViewFinder.find(R.id.list_business_statictas);
        mAdapter = new CommonAdapter<Commission>(this, list, R.layout.item_commission) {
            @Override
            public void convert(ViewHolder helper, Commission item, int position) {
                helper.setText(R.id.commission_date, item.createTime);
                helper.setText(R.id.commission_type, item.content);
                helper.setText(R.id.commission_content, getString(R.string.order_sn,item.orderSn));
                helper.setText(R.id.commission_amount, getString(R.string.yuan,item.money));
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mAdapter.getCount() >= Constants.PAGE_SIZE
                        && mAdapter.getCount() % Constants.PAGE_SIZE == 0) {
                    loadData();
                }
            }
        });
        loadData();
    }

    protected HashMap<String, String> getLoadParams() {
        HashMap<String, String> data = new HashMap<String, String>(1);
        data.put("page", String.valueOf(list.size() / Constants.PAGE_SIZE + 1));
        data.put("month", yearMonth);
        return data;
    }

    protected boolean checkLoadState() {
        if (mLoadMore) {
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(MonthDetailActivity.this)) {
            ToastUtils.show(MonthDetailActivity.this, R.string.msg_error_network);
            return false;
        }
        mLoadMore = true;
        return true;
    }

    protected void loadData() {
        if (!checkLoadState()) {
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), MonthDetailActivity.class.getName());
        ApiUtils.post(MonthDetailActivity.this, URLConstants.STATISTICS_DETAIL, getLoadParams(), CommissionListResult.class, new Response.Listener<CommissionListResult>() {

            @Override
            public void onResponse(CommissionListResult response) {
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
                if (O2OUtils.checkResponse(MonthDetailActivity.this, response)) {
                    if (!ArraysUtils.isEmpty(response.data.commisssions)) {
                        list.addAll(response.data.commisssions);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(MonthDetailActivity.this, R.string.msg_error);
                mLoadMore = false;
            }

        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(month+getString(R.string.msg_month_detail));
    }
}
