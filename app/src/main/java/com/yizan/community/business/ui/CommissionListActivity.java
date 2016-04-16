package com.yizan.community.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.Commission;
import com.fanwe.seallibrary.model.result.CommissionListResult;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
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
 * 佣金ListActivity
 */
public class CommissionListActivity extends BaseActivity implements TitleListener {

    protected boolean mLoadMore = false;
    private ListView mListView;
    private CommonAdapter<Commission> mAdapter;
    private List<Commission> list = new ArrayList<Commission>();
    private TextView mAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission_list);
        initView();
        setTitleListener(this);
        setPageTag(TagManage.COMMISSION_LIST_ACTIVITY);
    }

    private void initView() {
        mAmountTextView = mViewFinder.find(R.id.commission_ammount);
        mListView = mViewFinder.find(android.R.id.list);
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

        mListView.setOnScrollListener(new OnScrollListener() {

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

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.my_commission);
    }

    protected HashMap<String, String> getLoadParams() {
        HashMap<String, String> data = new HashMap<String, String>(1);
        data.put("page", String.valueOf(list.size() / Constants.PAGE_SIZE + 1));
        data.put("month",String.valueOf(0));
        return data;
    }

    protected boolean checkLoadState() {
        if (mLoadMore) {
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(CommissionListActivity.this)) {
            ToastUtils.show(CommissionListActivity.this, R.string.msg_error_network);
            return false;
        }
        mLoadMore = true;
        return true;
    }

    protected void loadData() {
        if (!checkLoadState()) {
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), CommissionListActivity.class.getName());
        ApiUtils.post(CommissionListActivity.this, URLConstants.STATISTICS_DETAIL, getLoadParams(), CommissionListResult.class, new Listener<CommissionListResult>() {

            @Override
            public void onResponse(CommissionListResult response) {
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
                if (O2OUtils.checkResponse(CommissionListActivity.this, response)) {
                    if (response.data.total != null){
                        mAmountTextView.setText(getString(R.string.yuan, response.data.total));
                    }else{
                        mAmountTextView.setText(getString(R.string.yuan,"0"));
                    }

                    if (!ArraysUtils.isEmpty(response.data.commisssions)) {
                        list.addAll(response.data.commisssions);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(CommissionListActivity.this, R.string.msg_error);
                mLoadMore = false;
            }

        });
    }
}
