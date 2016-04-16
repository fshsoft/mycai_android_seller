package com.yizan.community.business.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yizan.community.business.R;
import com.yizan.community.business.ui.BillListFragment;
import com.yizan.community.business.ui.GoodsListFragment;

/**
 * 商品列表
 */
public class TabBillListFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] TITLES;
    public TabBillListFragmentPagerAdapter(Context mContext,FragmentManager fm) {
        super(fm);
        TITLES = new String[]{mContext.getString(R.string.all), mContext.getString(R.string.income), mContext.getString(R.string.withdraw_deposit), mContext.getString(R.string.user_bill_tab_recharge)};
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                fragment = BillListFragment.newInstance(position);
                break;
        }
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