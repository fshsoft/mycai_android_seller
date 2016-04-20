package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.BillListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.Account;
import com.fanwe.seallibrary.model.result.AccountListResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.fiftyonemycai365.seller.business.widget.LoadMoreFooterView;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;

/**
 * 收入ListFragment
 */
public class BillListFragment extends BaseFragment {
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private BillListAdapter mBillListAdapter;
    private boolean mHasLoadedOnce = false;
    private int mPage = 0;
    private int mStatus = 0;

    public static BillListFragment newInstance(int status) {
        BillListFragment fragment = new BillListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DATA, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.BILL_LIST_FRAGMENT);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_bill_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFirst();
    }

    @Override
    protected void initView() {
        if (this.getArguments() != null) {
            mStatus = this.getArguments().getInt(Constants.EXTRA_DATA, 0);
        }
// 获取view的引用
        mLoadMoreListViewContainer = mViewFinder.find(R.id.load_more_list_view_container);
        LoadMoreFooterView footerView = new LoadMoreFooterView(getActivity());
        footerView.setVisibility(View.GONE);
        mLoadMoreListViewContainer.setLoadMoreView(footerView);
        mLoadMoreListViewContainer.setLoadMoreUIHandler(footerView);
        mBillListAdapter = new BillListAdapter(getActivity(), new ArrayList<Account>());
        ListView lv = mViewFinder.find(android.R.id.list);
        lv.setAdapter(mBillListAdapter);
        lv.setEmptyView(mViewFinder.find(android.R.id.empty));

        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadData();
            }
        });


    }

    private void loadFirst() {
        mPage = 0;
        loadData();
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(getActivity())) {
            ToastUtils.show(getActivity(), R.string.msg_error_network);
            return;
        }
        Map<String, Object> parmas = new HashMap<>();
        parmas.put("page", mPage);
        parmas.put("type", 1);
        parmas.put("status", mStatus);
        ApiUtils.post(getActivity(), URLConstants.SHOP_ACCOUNT, parmas,
                AccountListResult.class, new Response.Listener<AccountListResult>() {

                    @Override
                    public void onResponse(AccountListResult response) {
                        if (mPage == 0) {
                            mBillListAdapter.clear();
                        }
                        if (O2OUtils.checkResponse(
                                getActivity(),
                                response)) {
                            boolean haveMore = false;

                            mPage += 1;
                            if (!ArraysUtils.isEmpty(response.data)) {
                                if (response.data.size() >= Constants.PAGE_SIZE) {
                                    haveMore = true;
                                }

                            }
                            mBillListAdapter.addAll(response.data);
                            mLoadMoreListViewContainer.loadMoreFinish(mBillListAdapter.getCount() <= 0, haveMore);

                        } else {
                            mLoadMoreListViewContainer.loadMoreFinish(mBillListAdapter.getCount() <= 0, true);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(getActivity(), R.string.msg_error);
                        mLoadMoreListViewContainer.loadMoreFinish(mBillListAdapter.getCount() <= 0, true);
                    }
                });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            mStatus = this.getArguments().getInt(Constants.EXTRA_DATA, 0);
            if(mStatus == 0){
                setUserVisibleHint(true);
            }
        }

        super.onActivityCreated(savedInstanceState);
    }
}
