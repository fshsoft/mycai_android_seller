package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.FetchBillRecordListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.Account;
import com.fanwe.seallibrary.model.result.AccountListResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.fiftyonemycai365.seller.business.widget.LoadMoreFooterView;
import com.zongyou.library.app.CustomDialogFragment;
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
 * 提现记录Activity
 */
public class FetchRecordActivity extends BaseActivity implements BaseActivity.TitleListener {
    private LoadMoreListViewContainer mLoadMoreListViewContainer;
    private FetchBillRecordListAdapter mBillListAdapter;
    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_record);
        setTitleListener(this);

        initView();
        setPageTag(TagManage.FETCH_RECORD_ACTIVITY);
    }


    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_fetch_record);
    }

    private void initView() {
        mLoadMoreListViewContainer = mViewFinder.find(R.id.load_more_list_view_container);
        LoadMoreFooterView footerView = new LoadMoreFooterView(this);
        footerView.setVisibility(View.GONE);
        mLoadMoreListViewContainer.setLoadMoreView(footerView);
        mLoadMoreListViewContainer.setLoadMoreUIHandler(footerView);
        mBillListAdapter = new FetchBillRecordListAdapter(this, new ArrayList<Account>());
        ListView lv = mViewFinder.find(android.R.id.list);
        lv.setAdapter(mBillListAdapter);
        lv.setEmptyView(mViewFinder.find(android.R.id.empty));

        mLoadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadData();
            }
        });
        loadFirst();

    }

    private void loadFirst() {
        mPage = 0;
        loadData();
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        if (mPage == 0) {
            CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        }
        Map<String, Object> parmas = new HashMap<>();
        parmas.put("page", mPage);
        parmas.put("type", 2);
        parmas.put("status", 0);
        ApiUtils.post(this, URLConstants.SHOP_ACCOUNT, parmas,
                AccountListResult.class, new Response.Listener<AccountListResult>() {

                    @Override
                    public void onResponse(AccountListResult response) {
                        if (mPage == 0) {
                            CustomDialogFragment.dismissDialog();
                            mBillListAdapter.clear();
                        }
                        if (O2OUtils.checkResponse(
                                FetchRecordActivity.this,
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
                        ToastUtils.show(FetchRecordActivity.this, R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                        mLoadMoreListViewContainer.loadMoreFinish(mBillListAdapter.getCount() <= 0, true);
                    }
                });

    }
}
