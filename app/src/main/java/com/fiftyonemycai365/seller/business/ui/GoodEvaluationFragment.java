package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;

/**
 * Created by zzl on 2015/9/2.
 */
public class GoodEvaluationFragment extends BaseFragment {
    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.GOOD_EVALUATION_FRAGMENT);
    }

    @Override
    protected void initView() {

    }
}
