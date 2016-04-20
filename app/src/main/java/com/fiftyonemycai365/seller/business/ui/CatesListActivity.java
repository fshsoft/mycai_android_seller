package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.CateListAdapter;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.GoodsCate;
import com.fanwe.seallibrary.model.result.CateListResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.fiftyonemycai365.seller.business.widget.dslv.DragSortListView;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatesListActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {

    private CateListAdapter mCateListAdapter;
    private int mGoodsType = Constants.GOODS_TYPE.GOODS; // 1: 商品分类 2: 服务分类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cates_list);
        mViewFinder.find(R.id.tv_edit).setOnClickListener(this);
        mGoodsType = this.getIntent().getIntExtra(Constants.EXTRA_DATA, Constants.GOODS_TYPE.GOODS);
        setTitleListener(this);
        DragSortListView listView = mViewFinder.find(R.id.lv_list);
        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);

        List<GoodsCate> list = new ArrayList<>();

        mCateListAdapter = new CateListAdapter(this, list, listView, mGoodsType);
        listView.setAdapter(mCateListAdapter);
        listView.setDragEnabled(true); //设置是否可拖动。
        listView.setEmptyView(mViewFinder.find(android.R.id.empty));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CatesListActivity.this, GoodsListActivity.class);
                intent.putExtra(Constants.EXTRA_DATA, mCateListAdapter.getItem(i));
                startActivity(intent);
            }
        });
        loadData();
        setPageTag(TagManage.CATES_LIST_ACTIVITY);
    }

    //监听器在手机拖动停下的时候触发
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {//from to 分别表示 被拖动控件原位置 和目标位置
                    if (from != to) {
                        GoodsCate item = mCateListAdapter.getItem(from);//得到listview的适配器
                        mCateListAdapter.remove(from);//在适配器中”原位置“的数据。
                        mCateListAdapter.insert(item, to);//在目标位置中插入被拖动的控件。
                        updateSort(mCateListAdapter.getNewSortIds());
                    }
                }
            };
    //删除监听器，点击左边差号就触发。删除item操作。
    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    mCateListAdapter.remove(which);
                }
            };

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        if(mGoodsType == Constants.GOODS_TYPE.GOODS) {
            title.setText(getString(R.string.store_fun_goods));
        }else{
            title.setText(getString(R.string.store_fun_service));
        }
        right.setVisibility(View.VISIBLE);
        ((Button) right).setText(getString(R.string.title_activity_cate_add));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatesListActivity.this, CateAddActivity.class);
                intent.putExtra(Constants.EXTRA_TYPE, mGoodsType);
                startActivityForResult(intent, 0x103);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:
                mCateListAdapter.setIsEditing(!mCateListAdapter.isEditing());
                if (mCateListAdapter.isEditing()) {
                    mViewFinder.setText(R.id.tv_edit, getString(R.string.complete));
                } else {
                    mViewFinder.setText(R.id.tv_edit, getString(R.string.schedule_edit));
                }

                break;
        }
    }



    private void updateSort(List<Integer> ids) {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("data", ids);
        ApiUtils.post(this, URLConstants.GOODS_CATES_SORT, params,
                CateListResult.class, new Response.Listener<CateListResult>() {

                    @Override
                    public void onResponse(CateListResult response) {
                        if (O2OUtils.checkResponse(
                                CatesListActivity.this,
                                response)) {
//                            initViewData(response.data);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(CatesListActivity.this, R.string.msg_error_update);
                    }
                });

    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("type", mGoodsType);
        CustomDialogFragment.getInstance(this.getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.GOODS_CATES_LIST, params,
                CateListResult.class, new Response.Listener<CateListResult>() {

                    @Override
                    public void onResponse(CateListResult response) {
                        if (O2OUtils.checkResponse(
                                CatesListActivity.this,
                                response)) {
                            initViewData(response.data);
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(CatesListActivity.this, R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }

    private void initViewData(List<GoodsCate> list) {
        if (ArraysUtils.isEmpty(list)) {
            return;
        }
        mCateListAdapter.setList(list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        switch (requestCode){
            case 0x103:
                loadData();
                break;
        }
    }
}
