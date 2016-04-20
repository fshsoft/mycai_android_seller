package com.fiftyonemycai365.seller.business.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.GoodsCate;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.util.ToastUtils;

public class GoodsListActivity extends BaseActivity implements View.OnClickListener {
    private GoodsCate mGoodsCate;
    private GoodsContainerFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);
        mGoodsCate = this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        initTitleBar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mFragment = GoodsContainerFragment.newInstance(mGoodsCate, "");
        fragmentTransaction.add(R.id.fl_container, mFragment);
        fragmentTransaction.commit();
        setPageTag(TagManage.GOODS_LIST_ACTIVITY);
    }

    private void initTitleBar(){
        setCustomTitle(R.layout.titlebar_goods_list);
        setViewClickListener(R.id.title_left, this);
        setViewClickListener(R.id.title_add, this);
        setViewText(R.id.title, mGoodsCate.name);

        SearchView searView = (SearchView) findViewById(R.id.title_right);
        searView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewVisible(R.id.title, View.GONE);
                setViewVisible(R.id.title_left, View.GONE);
                setViewVisible(R.id.title_add, View.GONE);
            }
        });
        searView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setViewVisible(R.id.title, View.VISIBLE);
                setViewVisible(R.id.title_left, View.VISIBLE);
                setViewVisible(R.id.title_add, View.VISIBLE);
                return false;
            }
        });
        searView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query.trim())) {
                    ToastUtils.show(GoodsListActivity.this, getString(R.string.msg_import_goods_name));
                    return false;
                }
                Intent intent = new Intent(GoodsListActivity.this, GoodsSearchActivity.class);
                intent.putExtra(Constants.EXTRA_KEYS, query.trim());
                intent.putExtra(Constants.EXTRA_DATA, mGoodsCate);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_left:
                AppUtils.hideSoftInput(this);
                finishActivity();
                break;

            case R.id.title_add:
                Intent intent = null;
                if(mGoodsCate.type == Constants.GOODS_TYPE.GOODS) {
                    intent = new Intent(this, GoodAddActivity.class);
                }else{
                    intent = new Intent(this, ServiceAddActivity.class);
                }
                intent.putExtra(Constants.EXTRA_DATA, mGoodsCate);
                startActivity(intent);
                break;
        }
    }


}
