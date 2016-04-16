package com.yizan.community.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.Hand.InterFace.EVALUATE_STATE;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.OrderRate;
import com.fanwe.seallibrary.model.result.RateListResultInfo;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.RefreshLayoutUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.DateUtil;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.StringUtil;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 评价-好评ListFragment
 * 
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-4-2 上午10:05:32
 */
public class EvaluationUpListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
	private ListView mListView;
	private List<OrderRate> mDatas = new ArrayList<OrderRate>();
	private CommonAdapter<OrderRate> mAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private boolean mLoadMore;

	@Override
	protected View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.common_refresh_empty, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPageTag(TagManage.EVALUATION_UP_LIST_FRAGMENT);
	}

	@Override
	protected void initView() {
		mSwipeRefreshLayout = mViewFinder.find(R.id.swipe_container);
		RefreshLayoutUtils.initSwipeRefreshLayout(mFragmentActivity, mSwipeRefreshLayout, this, true);
		mAdapter = new CommonAdapter<OrderRate>(mFragmentActivity, mDatas, R.layout.item_customer_reviews) {

			@Override
			public void convert(ViewHolder helper, OrderRate item,int position) {
				if (null != item.user) {
					helper.setText(R.id.customer_tv_mobile, StringUtil.mobileHide(item.user.mobile));
				}
				if (EVALUATE_STATE.UP.code.equals(item.result)) {
					((TextView) helper.getView(R.id.customer_tv_reviews)).setTextColor(getResources().getColor(R.color.primary));
				} else if (EVALUATE_STATE.DOWN.code.equals(item.result)) {
					((TextView) helper.getView(R.id.customer_tv_reviews)).setTextColor(getResources().getColor(R.color.primary));
				} else if (EVALUATE_STATE.MIDDLE.code.equals(item.result)) {
					((TextView) helper.getView(R.id.customer_tv_reviews)).setTextColor(getResources().getColor(R.color.primary));
				}
				if (null != item.reply){
					((LinearLayout)helper.getView(R.id.ll_sjhf)).setVisibility(View.VISIBLE);
					helper.setText(R.id.customer_reply_content, item.reply);
					helper.setText(R.id.customer_reply_time,DateUtil.UTC2GMT(Constants.PATTERN_Y_M, item.replyTime * 1000));
				}
				helper.setText(R.id.customer_tv_reviews, EVALUATE_STATE.getType(item.result));
				helper.setText(R.id.customer_tv_content, item.content);
				helper.setText(R.id.customer_tv__time, DateUtil.UTC2GMT(Constants.PATTERN_Y_M, item.createTime * 1000));

				((Button)helper.getView(R.id.customer_btn_reply)).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mFragmentActivity, ReviewsReplyActivity.class);
						startActivity(intent);
					}
				});

			}
		};
		mListView = mViewFinder.find(android.R.id.list);
		mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mDatas.size() >= Constants.PAGE_SIZE && mDatas.size() % Constants.PAGE_SIZE == 0
						&& !mSwipeRefreshLayout.isRefreshing()) {
					loadData(false);
					mLoadMore = true;
				}
			}
		});
		loadData(true);
	}

	private void loadData(final boolean isRefresh) {
		if (NetworkUtils.isNetworkAvaiable(mFragmentActivity)) {
			if (isRefresh)
				mDatas.clear();
			HashMap<String, String> data = new HashMap<String, String>(2);
			data.put(ParamConstants.TYPE, String.valueOf(1));
			data.put(ParamConstants.PAGE, String.valueOf(isRefresh ? 1 : (mDatas.size() / Constants.PAGE_SIZE + 1)));
			ApiUtils.post(mFragmentActivity, URLConstants.EVALUATION_LIST, data, RateListResultInfo.class, new Response.Listener<RateListResultInfo>() {

				@Override
				public void onResponse(RateListResultInfo response) {
					mSwipeRefreshLayout.setRefreshing(false);
					if (!isRefresh)
						mLoadMore = false;
					if (O2OUtils.checkResponse(mFragmentActivity, response)) {
						if (!ArraysUtils.isEmpty(response.data)) {
							mDatas.addAll(response.data);
							mAdapter.notifyDataSetChanged();
						} else {
							ToastUtils.show(mFragmentActivity, R.string.common_not_more);
							if (isRefresh)
								mAdapter.notifyDataSetChanged();
						}
					} else if (isRefresh)
						mAdapter.notifyDataSetChanged();
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					mSwipeRefreshLayout.setRefreshing(false);
					if (isRefresh)
						mAdapter.notifyDataSetChanged();
					else
						mLoadMore = false;
				}
			});
		} else {
			mSwipeRefreshLayout.setRefreshing(false);
			ToastUtils.show(mFragmentActivity, R.string.msg_error_network);
		}
	}

	@Override
	public void onRefresh() {
		loadData(true);
	}

}
