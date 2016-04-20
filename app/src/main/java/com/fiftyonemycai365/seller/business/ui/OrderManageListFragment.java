package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.TabOrderListFragmentPagerAdapter;
import com.fanwe.seallibrary.model.OrderCount;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;

/**
 * 订单管理ListFragment
 */
public class OrderManageListFragment extends BaseFragment implements OrderListFragment.OrderRefershListener {
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private TabOrderListFragmentPagerAdapter mAdapter;
    public static OrderManageListFragment newInstance() {
        return new OrderManageListFragment();
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_order_manage, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.ORDER_MANAGE_LIST_FRAGMENT);
    }

    @Override
    protected void initView() {
        mIndicator = mViewFinder.find(R.id.indicator);
        mViewPager = mViewFinder.find(R.id.viewpager);
        mAdapter = new TabOrderListFragmentPagerAdapter(getActivity(),getFragmentManager(),this);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }


    @Override
    public void orderRefresh(OrderCount orderCount) {
        mAdapter.setTitles(new String[]{getActivity().getString(R.string.msg_delivering,orderCount.ingCount) , getActivity().getString(R.string.msg_all_order,orderCount.count)});
        mIndicator.notifyDataSetChanged();
    }
}