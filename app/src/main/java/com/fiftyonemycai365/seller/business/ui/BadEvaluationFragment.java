package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.EvalutionManagerAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.EvaPackResult;
import com.fanwe.seallibrary.model.OrderRateInfo;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ztl on 2015/9/2.
 */
public class BadEvaluationFragment extends BaseFragment {
    private ListView mListview;
    private View emptyView;
    private boolean mLoadMore;
    private List<OrderRateInfo> listDatas;
    private EvalutionManagerAdapter adapter;

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.all_evluation_manager,container,false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.BAD_EVALUATION_FRAGMENT);
    }

    @Override
    protected void initView() {
        listDatas = new ArrayList<OrderRateInfo>();
        getAllEvluation(true);
        emptyView = mViewFinder.find(android.R.id.empty);
        mListview = mViewFinder.find(R.id.all_evluation_list);
        mListview.setEmptyView(emptyView);
        adapter = new EvalutionManagerAdapter(listDatas,mFragmentActivity);
        mListview.setAdapter(adapter);
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && adapter.getCount() >= Constants.PAGE_SIZE && adapter.getCount() % Constants.PAGE_SIZE == 0) {
                    getAllEvluation(false);
                }
            }
        });
    }

    private void getAllEvluation(final boolean isRefresh){
        if (mLoadMore)
            return;
        if (!NetworkUtils.isNetworkAvaiable(mFragmentActivity)){
            ToastUtils.show(mFragmentActivity, R.string.msg_error_network);
            return;
        }
        CustomDialogFragment.getInstance(getFragmentManager(), mFragmentActivity.getClass().getName());
        Map<String,String> data = new HashMap<>();
        data.put("page",String.valueOf(listDatas.size()/ Constants.PAGE_SIZE+1));
        data.put("type",String.valueOf(2));
        mLoadMore = true;
        ApiUtils.post(mFragmentActivity, URLConstants.SELLER_EVALIST, data, EvaPackResult.class, new Response.Listener<EvaPackResult>() {
            @Override
            public void onResponse(EvaPackResult response) {
                if (isRefresh)
                    listDatas.clear();
                if (O2OUtils.checkResponse(mFragmentActivity, response)) {
                    if (!ArraysUtils.isEmpty(response.data.eva))
                        listDatas.addAll(response.data.eva);
                }
                adapter.notifyDataSetChanged();
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                mLoadMore = false;
            }
        });
    }
}
