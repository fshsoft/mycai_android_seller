package com.yizan.community.business.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanwe.seallibrary.model.UserInfo;
import com.zongyou.library.util.storage.PreferenceUtils;

/**
 * Created by zzl on 2015/9/2.
 */
public class EvaluationFragmentAdapetr extends FragmentPagerAdapter {

    UserInfo user = null;


    public EvaluationFragmentAdapetr(FragmentManager fm,Context context) {
        super(fm);
        user = PreferenceUtils.getObject(context, UserInfo.class);
    }


    public static final String[] TITLES = new String[] {"全部","好评","中评","差评"};

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AllEvaluationFragment();
                break;
            case 1:
                fragment = new GoodEvaluationFragment();
                break;
            case 2:
                fragment = new NeutralEvaluationFragment();
                break;
            case 3:
                fragment = new BadEvaluationFragment();
                break;
        }
        return fragment;
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
