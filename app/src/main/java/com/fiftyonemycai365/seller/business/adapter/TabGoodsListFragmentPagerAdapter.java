package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanwe.seallibrary.model.GoodsCate;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.ui.GoodsListFragment;

/**
 * 商品列表
 */
public class TabGoodsListFragmentPagerAdapter extends FragmentPagerAdapter {
    private GoodsCate mGoodsCate;
    private String mKeywrods;
    private Fragment[] mListFragmentList;
    private String[] TITLES ;
    public TabGoodsListFragmentPagerAdapter(FragmentManager fm, GoodsCate goodsCate, String keywords,Context mContext) {
        super(fm);
        mGoodsCate = goodsCate;
        mKeywrods = keywords;
        mListFragmentList = new Fragment[2];
        TITLES = new String[]{mContext.getString(R.string.msg_putaway_goods), mContext.getString(R.string.msg_sold_out_goods)};
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = GoodsListFragment.getInstance(1, mGoodsCate, mKeywrods);
                mListFragmentList[position] = fragment;
                break;
            case 1:
                fragment = GoodsListFragment.getInstance(2, mGoodsCate, mKeywrods);
                mListFragmentList[position] = fragment;
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

    public Fragment getFragment(int position){
        return mListFragmentList[position];
    }
}