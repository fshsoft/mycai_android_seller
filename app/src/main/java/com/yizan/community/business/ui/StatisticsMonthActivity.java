package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
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
import com.yizan.community.business.adapter.StatisticsMonthAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.StatisticsMonth;
import com.fanwe.seallibrary.model.result.StatisticsMonthResult;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.RefreshLayoutUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 按月份统计Activity
 */
public class StatisticsMonthActivity extends BaseActivity implements TitleListener, SwipeRefreshLayout.OnRefreshListener {

	protected SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView statistce_listview;
	protected boolean mLoadMore = false;
	private int mPage = 1;
	private StatisticsMonthAdapter mAdapter;
	private List<StatisticsMonth> list = new ArrayList<StatisticsMonth>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		setTitleListener(this);
		setPageTag(TagManage.STATISTICS_MONTH_ACTIVITY);
	}

	private void initView() {
		setContentView(R.layout.common_refresh_empty);

		statistce_listview = mViewFinder.find(android.R.id.list);
		statistce_listview.setBackgroundColor(getResources().getColor(R.color.white));
		statistce_listview.setPadding(getResources().getDimensionPixelSize(R.dimen.margin),0,0,0);
		mSwipeRefreshLayout = mViewFinder.find(R.id.swipe_container);
		mAdapter = new StatisticsMonthAdapter(StatisticsMonthActivity.this, list);
		statistce_listview.setAdapter(mAdapter);
		statistce_listview.setEmptyView(mViewFinder.find(android.R.id.empty));
		RefreshLayoutUtils.initSwipeRefreshLayout(StatisticsMonthActivity.this, mSwipeRefreshLayout, this, true);
		statistce_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (list != null && list.size() > 0 && list.get(position).month > 0) {
					Intent intent = new Intent(StatisticsMonthActivity.this, StatisticsActivity.class);
					intent.putExtra(Constants.MONTH, String.valueOf(list.get(position).month));
					startActivity(intent);
				}

			}
		});

		statistce_listview.setOnScrollListener(new OnScrollListener() {

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
		statistce_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent =new Intent(StatisticsMonthActivity.this,StatisticsActivity.class);
					intent.putExtra(Constants.MONTH,String.valueOf(list.get(position).month));
					startActivity(intent);
			}
		});
		loadData(true);
	}

	@Override
	public void onRefresh() {
		loadData(true);
	}

	@Override
	public void setTitle(TextView title, ImageButton left, View right) {
		title.setText(R.string.statistics_month_summary);

	}

	protected boolean checkLoadState(boolean isRefresh) {
		if (mLoadMore) {
			return false;
		}
		if (!NetworkUtils.isNetworkAvaiable(StatisticsMonthActivity.this)) {
			ToastUtils.show(StatisticsMonthActivity.this, R.string.msg_error_network);
			return false;
		}
		mLoadMore = true;
		if (isRefresh) {
			mSwipeRefreshLayout.setRefreshing(true);
			mPage = 1;
		} else {
			mPage += 1;
		}
		return true;
	}

	protected HashMap<String, String> getLoadParams() {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("page", String.valueOf(mPage));
		return data;
	}

	protected void loadData(final boolean isRefresh) {
		if (!checkLoadState(isRefresh)) {
			return;
		}

		ApiUtils.post(StatisticsMonthActivity.this, URLConstants.STATISTICSMONTH, getLoadParams(), StatisticsMonthResult.class, new Listener<StatisticsMonthResult>() {

			@Override
			public void onResponse(StatisticsMonthResult response) {
				mLoadMore = false;
				mSwipeRefreshLayout.setRefreshing(false);
				if (O2OUtils.checkResponse(StatisticsMonthActivity.this, response)) {
					if (!ArraysUtils.isEmpty(response.data)) {
						if (isRefresh) {
							list = response.data;
						} else {

							list.addAll(response.data);
						}
						mAdapter.setListData(list);
						mAdapter.notifyDataSetChanged();
					} else {
						ToastUtils.show(StatisticsMonthActivity.this, R.string.common_not_more);
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
