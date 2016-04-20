package com.fiftyonemycai365.seller.business.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.GoodsListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fiftyonemycai365.seller.business.event.GoodOpEvent;
import com.fanwe.seallibrary.model.Goods;
import com.fanwe.seallibrary.model.GoodsCate;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fanwe.seallibrary.model.result.GoodListResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.RefreshLayoutUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.fiftyonemycai365.seller.business.widget.dslv.DragSortListView;
import com.ypy.eventbus.EventBus;
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
 * 商品ListFragment
 */
public class GoodsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, GoodsListAdapter.ISelNotify {
    private DragSortListView mListView;
    private GoodsListAdapter mGoodsListAdapter;
    private CheckBox mCbSelAll;
    private GoodsCate mGoodsCate;
    private int mStatus;
    private String mKeywords;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected boolean mLoadMore = false;
    private boolean mHasLoadedOnce = false;
    private boolean mNeedReload = false;

    public static GoodsListFragment getInstance(int status, GoodsCate goodsCate, String keywords) {
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_DATA, goodsCate);
        bundle.putInt(Constants.EXTRA_STATUS, status);
        bundle.putString(Constants.EXTRA_KEYS, keywords);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.GOODS_LIST_FRAGMENT);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_goods_list, container, false);
    }

    @Override
    protected void initView() {
        mStatus = this.getArguments().getInt(Constants.EXTRA_STATUS);
        mGoodsCate = this.getArguments().getParcelable(Constants.EXTRA_DATA);
        mKeywords = this.getArguments().getString(Constants.EXTRA_KEYS);


        mViewFinder.onClick(R.id.cb_sel_all, this);
        mViewFinder.onClick(R.id.tv_edit, this);
        mCbSelAll = mViewFinder.find(R.id.cb_sel_all);
        mViewFinder.onClick(R.id.rl_pull, this);
        mViewFinder.onClick(R.id.rl_remove, this);

        TextView tv = mViewFinder.find(R.id.tv_pull);
        Drawable drawable;
        if (mStatus == 1) {
            tv.setText(getString(R.string.msg_sold_out_text));
            drawable = getResources().getDrawable(R.drawable.ic_pull_off);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
        } else {
            tv.setText(getString(R.string.msg_putaway_text));
            drawable = getResources().getDrawable(R.drawable.ic_pull_on);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv.setCompoundDrawables(drawable, null, null, null);
        }
        initListView();
        initViewState();

        EventBus.getDefault().register(this);
    }

    private void initListView() {
        mSwipeRefreshLayout = mViewFinder.find(R.id.swipe_container);
        mListView = mViewFinder.find(R.id.lv_list);
        List<Goods> ls = new ArrayList<>();

        mGoodsListAdapter = new GoodsListAdapter(getActivity(), ls, mListView);
        mGoodsListAdapter.setISelNotify(this);
        mListView.setAdapter(mGoodsListAdapter);
        mListView.setEmptyView(mViewFinder.find(android.R.id.empty));
        RefreshLayoutUtils.initSwipeRefreshLayout(mFragmentActivity, mSwipeRefreshLayout, this, true);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (!mLoadMore && firstVisibleItem + visibleItemCount >= totalItemCount && mGoodsListAdapter.getCount() >= Constants.PAGE_SIZE && mGoodsListAdapter.getCount() % Constants.PAGE_SIZE == 0) {
                    loadData(false);
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    protected void loadData(final boolean isRefresh) {
        if (!checkLoadState(isRefresh)) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }

        ApiUtils.post(mFragmentActivity, URLConstants.GOODS_LIST, getLoadParams(), GoodListResult.class, new Response.Listener<GoodListResult>() {

            @Override
            public void onResponse(GoodListResult response) {
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
                if (isRefresh) {
                    mGoodsListAdapter.clear();
                    mCbSelAll.setChecked(false);
                }
                if (O2OUtils.checkResponse(mFragmentActivity, response)) {
                    if (!ArraysUtils.isEmpty(response.data)) {
                        mGoodsListAdapter.addAll(response.data);
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isRefresh) {
                    mGoodsListAdapter.clear();
                }
                mLoadMore = false;
                mSwipeRefreshLayout.setRefreshing(false);
            }

        });
    }

    protected HashMap<String, Object> getLoadParams() {
        HashMap<String, Object> data = new HashMap<String, Object>(4);
        data.put(ParamConstants.PAGE, mGoodsListAdapter.getCount() / Constants.PAGE_SIZE + 1);
        data.put(ParamConstants.STATUS, mStatus);
        data.put(ParamConstants.ID, mGoodsCate.id);
        data.put(ParamConstants.KEYWORDS, mKeywords);
        return data;
    }

    protected boolean checkLoadState(boolean isRefresh) {
        if (mLoadMore) {
            mSwipeRefreshLayout.setRefreshing(false);
            return false;
        }
        if (!NetworkUtils.isNetworkAvaiable(mFragmentActivity)) {
            ToastUtils.show(mFragmentActivity, R.string.msg_error_network);
            mSwipeRefreshLayout.setRefreshing(false);
            return false;
        }
        mLoadMore = true;
        if (isRefresh)
            mSwipeRefreshLayout.setRefreshing(true);

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_sel_all:
                mGoodsListAdapter.selAll(mCbSelAll.isChecked());
                break;
            case R.id.tv_edit:
                mGoodsListAdapter.setIsEditing(!mGoodsListAdapter.isEditing());
                initViewState();
                break;
            case R.id.rl_pull:
                goodsOp(mStatus == 1 ? 2:1);
                break;
            case R.id.rl_remove:
                goodsOp(3);
                break;
        }
    }

    private void initViewState() {
        boolean isEdit = mGoodsListAdapter.isEditing();
        mCbSelAll.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mViewFinder.setText(R.id.tv_edit, isEdit ? getString(R.string.complete) : getString(R.string.schedule_edit));
        mViewFinder.find(R.id.tv_desc).setVisibility(isEdit ? View.GONE : View.VISIBLE);
        mViewFinder.find(R.id.ll_bottom_container).setVisibility(isEdit ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemSelect(Goods goods) {
        if (!goods.checked) {
            mCbSelAll.setChecked(false);
        } else {
            mCbSelAll.setChecked(mGoodsListAdapter.isAllSel());
        }
    }



    private void goodsOp(final int op) {

        final List<Integer> ids = mGoodsListAdapter.getSelIds();
        if (ArraysUtils.isEmpty(ids)) {
            ToastUtils.show(getActivity(), getString(R.string.msg_select_goods));
            return;
        }
        if (!NetworkUtils.isNetworkAvaiable(getActivity())) {
            ToastUtils.show(getActivity(), R.string.msg_error_network);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        params.put("type", op);
        CustomDialogFragment.getInstance(getFragmentManager(), this.getClass().getName());
        ApiUtils.post(getActivity(), URLConstants.GOODS_OP, params,
                BaseResult.class, new Response.Listener<BaseResult>() {

                    @Override
                    public void onResponse(BaseResult response) {
                        if (O2OUtils.checkResponse(
                                getActivity(),
                                response)) {
                            mGoodsListAdapter.removeAll(ids);
                            EventBus.getDefault().post(new GoodOpEvent(GoodsListFragment.this, op));
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(getActivity(), R.string.msg_error_update);
                        CustomDialogFragment.dismissDialog();
                    }
                });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (this.isVisible()) {
            if (isVisibleToUser) {
                if(!mHasLoadedOnce) {
                    mHasLoadedOnce = true;
                    loadData(true);
                }else if(mNeedReload){
                    reloadData();
                }
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            if(mStatus == 1){
                setUserVisibleHint(true);
            }
        }

        super.onActivityCreated(savedInstanceState);
    }


    public void reloadData() {
        if(mHasLoadedOnce){
            loadData(true);
            mNeedReload = false;
        }
    }

    public void onEventMainThread(GoodOpEvent event) {
        if(event.viewObj == null || !event.viewObj.equals(this)){
            if(mHasLoadedOnce) {
                mNeedReload = true;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mNeedReload && this.isVisible()){
            reloadData();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
