package com.yizan.community.business.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yizan.community.business.R;
import com.yizan.community.business.ui.EvaluationAllListFragment;
import com.yizan.community.business.ui.EvaluationDownListFragment;
import com.yizan.community.business.ui.EvaluationMiddleListFragment;
import com.yizan.community.business.ui.EvaluationUpListFragment;

/**
 * 评价列表fragmentpageradapter
 * 
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-4-2 上午9:49:37
 */
public class TabEvaluationListFragmentPagerAdapter extends FragmentPagerAdapter {
	private String[] TITLES ;

	public TabEvaluationListFragmentPagerAdapter(Context mContext,FragmentManager fm) {
		super(fm);
		TITLES = new String[] {mContext.getString(R.string.all_number),
				mContext.getString(R.string.good_evaluate),
				mContext.getString(R.string.middle_evaluate),
				mContext.getString(R.string.bad_evaluate)};
	}


	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new EvaluationAllListFragment();
			break;
		case 1:
			fragment = new EvaluationUpListFragment();
			break;
		case 2:
			fragment = new EvaluationMiddleListFragment();
			break;
		case 3:
			fragment = new EvaluationDownListFragment();
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