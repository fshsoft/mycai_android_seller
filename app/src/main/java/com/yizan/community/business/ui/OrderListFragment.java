package com.yizan.community.business.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.yizan.community.business.adapter.OrderSummaryListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.OrderCount;
import com.fanwe.seallibrary.model.result.OrderPackResult;
import com.fanwe.seallibrary.model.result.OrderSummary;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.RefreshLayoutUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.LogUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 订单列表ListFragment
 */
public class OrderListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = OrderListFragment.class.getName();
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected boolean mLoadMore = false;
    private ListView mListView;
    private OrderSummaryListAdapter mAdapter;
    private List<OrderSummary> mListDatas = new ArrayList<OrderSummary>();
    private String mStatus;
    private String mDate;
    private String mKeywords;
    private OrderRefershListener mOrderRefershListener;

    /**
     * 订单数据刷新Listener
     */
    public interface OrderRefershListener {
        void orderRefresh(OrderCount orderCount);
    }

    public void setOrderRefershListener(OrderRefershListener orderRefershListener) {
        this.mOrderRefershListener = orderRefershListener;
    }

    public static OrderListFragment newInstance(Bundle args) {
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.ORDER_LIST_FRAGMENT);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.common_refresh_empty, container, false);
    }

    @Override
    protected void initView() {
        Bundle args = getArguments();
        if (null != args) {
            mStatus = args.getString(ParamConstants.STATUS);
            mDate = args.getString(ParamConstants.DATE);
            mKeywords = args.getString(ParamConstants.KEYWORDS);
        }
        mListView = mViewFinder.find(android.R.id.list);
        mListView.setDividerHeight(0);
        mSwipeRefreshLayout = mViewFinder.find(R.id.swipe_container);
        mAdapter = new OrderSummaryListAdapter(mFragmentActivity, mListDatas);

        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
        RefreshLayoutUtils.initSwipeRefreshLayout(mFragmentActivity, mSwipeRefreshLayout, this, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListDatas != null && mListDatas.size() > 0 && mListDatas.get(position).id > 0) {
                    Intent intent = new Intent(mFragmentActivity, OrderDetailsActivity.class);
                    intent.putExtra(Constants.ORDERID, mListDatas.get(position).id);
                    mFragmentActivity.startActivity(intent);
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //1.判断是否加载更多
                // 2.第一个可见的item下标加上可见item的总数是否不小于全部item的数量
                // 3.适配器中的item的总数是否不小于page容器的大小（20）
                // 4.适配器中的item总数整除page大小
                if (!mLoadMore &&
                        firstVisibleItem + visibleItemCount >= totalItemCount &&
                        mAdapter.getCount() >= Constants.PAGE_SIZE &&
                        mAdapter.getCount() % Constants.PAGE_SIZE == 0) {
                    loadData(false);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!TextUtils.isEmpty(mStatus) && ParamConstants.ORDER_NEW == Integer.valueOf(mStatus)) {
            mOrderReceiver = new OrderReceiver();
            getActivity().registerReceiver(mOrderReceiver, new IntentFilter("cn.jpush.android.intent.NOTIFICATION_RECEIVED"));
        }
    }

    @Override
    public void onDestroy() {
        if (null != mOrderReceiver)
            mFragmentActivity.unregisterReceiver(mOrderReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mOrderReceiver;

    class OrderReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

            if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                    .getAction())) {
                // 接收到推送下来的通知
                try {
                    JSONObject jsonObject = new JSONObject(extras);
                    String type = "";
                    if (jsonObject.has("type"))
                        type = jsonObject.getString("type");
                    if (type.equals("3")) {
                        //REFRESH order
                        loadData(true);
                    }

                } catch (JSONException e) {
                    LogUtils.e(TAG, e);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(true);
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        loadData(true);
    }

    protected void loadData(final boolean isRefresh) {
        if (!checkLoadState(isRefresh)) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (isRefresh) {
            mListDatas.clear();
        }
        ApiUtils.post(mFragmentActivity, URLConstants.ORDER_LISTS, getLoadParams(), OrderPackResult.class, new Listener<OrderPackResult>() {

            @Override
            public void onResponse(OrderPackResult response) {
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);

                if (O2OUtils.checkResponse(mFragmentActivity, response))
                    if (!ArraysUtils.isEmpty(response.data.orders))
                        mListDatas.addAll(response.data.orders);
                mAdapter.notifyDataSetChanged();
                if (null != mOrderRefershListener)
                    mOrderRefershListener.orderRefresh(new OrderCount(response.data));
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isRefresh) {
                    mListDatas.clear();
                    mAdapter.notifyDataSetChanged();
                }
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }

    protected HashMap<String, Object> getLoadParams() {
        HashMap<String, Object> data = new HashMap<String, Object>(4);
        int page = mListDatas.size() / Constants.PAGE_SIZE + 1;

        data.put(ParamConstants.PAGE, page);
        if (!TextUtils.isEmpty(mStatus))
            data.put(ParamConstants.STATUS, mStatus);
        if (!TextUtils.isEmpty(mDate))
            data.put(ParamConstants.DATE, mDate);
        if (!TextUtils.isEmpty(mKeywords))
            data.put(ParamConstants.KEYWORDS, mKeywords);
        return data;
    }

    protected boolean checkLoadState(boolean isRefresh) {
        if (mLoadMore) {
            mSwipeRefreshLayout.setRefreshing(false);
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(mFragmentActivity)) {
            ToastUtils.show(mFragmentActivity, R.string.msg_error_network);
            mSwipeRefreshLayout.setRefreshing(false);
            return false;
        }
        mLoadMore = true;
        if (isRefresh)
            mSwipeRefreshLayout.setRefreshing(true);

        return true;
    }

}