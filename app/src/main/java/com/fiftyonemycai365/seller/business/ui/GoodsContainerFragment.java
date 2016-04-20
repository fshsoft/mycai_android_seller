package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.TabGoodsListFragmentPagerAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.GoodsCate;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;


public class GoodsContainerFragment extends BaseFragment {
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private TabGoodsListFragmentPagerAdapter mAdapter;
    private GoodsCate mGoodsCate;
    private String mKeywrods;

    public static GoodsContainerFragment newInstance(GoodsCate goodsCate, String keywords) {
        GoodsContainerFragment fragment = new GoodsContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_DATA, goodsCate);
        bundle.putString(Constants.EXTRA_KEYS, keywords);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.GOODS_CONTAINER_FRAGMENT);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_goods_container, container, false);
    }

    @Override
    protected void initView() {
        mIndicator = mViewFinder.find(R.id.indicator);
        mViewPager = mViewFinder.find(R.id.viewpager);
        mGoodsCate = (GoodsCate)this.getArguments().getParcelable(Constants.EXTRA_DATA);
        mKeywrods = this.getArguments().getString(Constants.EXTRA_KEYS);
        mAdapter = new TabGoodsListFragmentPagerAdapter(getFragmentManager(), mGoodsCate, mKeywrods,getActivity());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    public void reloadData(){
        for(int i=0; i<mAdapter.getCount(); i++){
            Fragment fragment = mAdapter.getFragment(i);
            if(null != fragment) {
                ((GoodsListFragment)fragment).reloadData();
            }

        }

    }

}
