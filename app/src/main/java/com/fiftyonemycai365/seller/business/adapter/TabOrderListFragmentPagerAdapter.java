package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanwe.seallibrary.common.ParamConstants;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.ui.OrderListFragment;

/**
 * 订单列表fragmentpageradapter
 *
 * @author tu
 */
public class TabOrderListFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES ;
    private OrderListFragment.OrderRefershListener mOrderRefreshListener;
    public TabOrderListFragmentPagerAdapter(Context context,FragmentManager fm,OrderListFragment.OrderRefershListener listener) {
        super(fm);
        TITLES = new String[]{context.getString(R.string.msg_delivering, 0),context.getString(R.string.msg_all_order,0)};
        mOrderRefreshListener = listener;
    }



    @Override
    public Fragment getItem(int position) {
        OrderListFragment fragment = null;
        Bundle args = new Bundle(1);

        switch (position) {
            case 0:
                args.putString(ParamConstants.STATUS, String.valueOf(ParamConstants.ORDER_PROCESS));
                fragment = OrderListFragment.newInstance(args);
                break;
            case 1:
                args.putString(ParamConstants.STATUS, String.valueOf(ParamConstants.ORDER_ALL));
                fragment = OrderListFragment.newInstance(args);
                break;
        }
        fragment.setOrderRefershListener(mOrderRefreshListener);
        return fragment;
    }

    public void setTitles(String[] ts) {
        TITLES = ts;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position % TITLES.length];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}