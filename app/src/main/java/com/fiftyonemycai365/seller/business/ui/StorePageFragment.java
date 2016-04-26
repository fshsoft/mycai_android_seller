package com.fiftyonemycai365.seller.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.StoreFuncAdapter;
import com.fiftyonemycai365.seller.business.adapter.StoreFuncAdapter.FuncItem;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺Fragment
 */
public class StorePageFragment extends BaseFragment implements OnClickListener{
    private ShopInfo mShopInfo;
    public static StorePageFragment newInstance() {
        StorePageFragment fragment = new StorePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_store_page, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.STORE_PAGE_FRAGMENT);
    }

    @Override
    protected void initView() {
        GridView gv = mViewFinder.find(R.id.gv_func);
        List<FuncItem> list = new ArrayList<>();
        //list.add(createFunc(getString(R.string.store_fun_goods), R.drawable.ic_store_goods));
        //list.add(createFunc(getString(R.string.store_fun_service), R.drawable.ic_store_service));
        list.add(createFunc(getString(R.string.store_fun_comments), R.drawable.ic_store_comments));
        list.add(createFunc(getString(R.string.store_fun_analyse), R.drawable.ic_store_analyse));
        //list.add(createFunc(getString(R.string.store_fun_detail), R.drawable.ic_store_detail));
        list.add(createFunc(getString(R.string.store_fun_check), R.drawable.ic_store_check));
        StoreFuncAdapter adapter = new StoreFuncAdapter(getActivity(), list);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = null;
                switch (i) {
                    /*
                    case 0:
                        intent = new Intent(getActivity(), CatesListActivity.class);
                        intent.putExtra(Constants.EXTRA_DATA, Constants.GOODS_TYPE.GOODS);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), CatesListActivity.class);
                        intent.putExtra(Constants.EXTRA_DATA, Constants.GOODS_TYPE.SERVICE);
                        startActivity(intent);
                        break;
                    */
                    case 0:
                        startActivity(new Intent(getActivity(),EvluationManagerActivity.class));
                        break;
                    case 1: //经营分析
                        UserInfo userInfo = PreferenceUtils.getObject(getActivity(), UserInfo.class);
                        String token = PreferenceUtils.getValue(getActivity(), Constants.TOKEN_LOGIN, "");
                        if(userInfo != null || !TextUtils.isEmpty(token)){
                            String url = String.format(URLConstants.ORDER_STATISTIC, token, userInfo.id);
                            Intent intent1 = new Intent(getActivity(), WebViewActivity.class);
                            intent1.putExtra(Constants.EXTRA_URL, url);
                            startActivity(intent1);
                        }
                        break;
                        /*
                    case 4:
                        startActivity(new Intent(getActivity(), SHopMessageActivity.class));
                        break;
                       */
                    case 2:
                        ToastUtils.show(getActivity(), R.string.store_not_open);
                        break;

                }

            }
        });

        mViewFinder.onClick(R.id.ll_bill, this);
    }

    FuncItem createFunc(String name, int icon){
        FuncItem item = new StoreFuncAdapter.FuncItem();
        item.name = name;
        item.icon = icon;
        return item;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(mFragmentActivity)) {
            ToastUtils.show(mFragmentActivity, R.string.msg_error_network);
            return ;
        }
        CustomDialogFragment.getInstance(this.getFragmentManager(), this.getClass().getName());
        ApiUtils.post(this.mFragmentActivity, URLConstants.SHOP_INFO, null,
                ShopResult.class, new Response.Listener<ShopResult>() {

                    @Override
                    public void onResponse(ShopResult response) {
                        if (O2OUtils.checkResponse(
                                StorePageFragment.this.mFragmentActivity,
                                response)) {
                            initViewData(response.data);
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(getActivity(), R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }
    private void initViewData(ShopInfo shopInfo){
        if(shopInfo == null){
            return;
        }
        mShopInfo = shopInfo;
        mViewFinder.setText(R.id.tv_balance, getString(R.string.msg_RMB, NumFormat.formatPrice(mShopInfo.balance)));
        mViewFinder.setText(R.id.tv_turn_over, getString(R.string.msg_RMB, NumFormat.formatPrice(mShopInfo.turnover)));
        mViewFinder.setText(R.id.tv_order_nums, String.valueOf(mShopInfo.orderNum));

        PreferenceUtils.setObject(getActivity(), mShopInfo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_bill:
                if(mShopInfo != null){
                    Intent intent = new Intent(getActivity(), UserBillActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, mShopInfo);
                    startActivityForResult(intent, 0x101);
                }

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0x101){
            loadData();
        }
    }
}
