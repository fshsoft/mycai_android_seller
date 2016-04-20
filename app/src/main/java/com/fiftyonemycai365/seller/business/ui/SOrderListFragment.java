package com.fiftyonemycai365.seller.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.OrderSummaryListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.result.OrderSummary;
import com.zongyou.library.app.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class SOrderListFragment extends BaseFragment  {

	private ListView order_listview;
	private OrderSummaryListAdapter mAdapter;
	private List<OrderSummary> list ;


	@Override
	protected View inflateView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_sorder_list, container, false);
	}

	@Override
	protected void initView() {

		list = (ArrayList<OrderSummary>)this.getArguments().getSerializable(Constants.EXTRA_DATA);
		if(list == null){
			list = new ArrayList<OrderSummary>();
		}
		order_listview = mViewFinder.find(R.id.order_listview);
		mAdapter = new OrderSummaryListAdapter((BaseActivity) mFragmentActivity, list);

		order_listview.setAdapter(mAdapter);
		order_listview.setEmptyView(mViewFinder.find(android.R.id.empty));
		order_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (list != null && list.size() > 0 && list.get(position).id > 0) {

					Intent intent = new Intent(mFragmentActivity, OrderDetailsActivity.class);

					intent.putExtra(Constants.ORDERID, mAdapter.getItem(position).id);
					mFragmentActivity.startActivity(intent);
				}
			}
		});


	}

	public static SOrderListFragment newInstance(ArrayList<OrderSummary> list) {
		SOrderListFragment fragment = new SOrderListFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.EXTRA_DATA, list);
		fragment.setArguments(bundle);
		return fragment;
	}


}
