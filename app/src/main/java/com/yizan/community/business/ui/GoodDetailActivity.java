package com.yizan.community.business.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.Goods;
import com.fanwe.seallibrary.model.ShopInfo;
import com.yizan.community.business.util.NumFormat;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.DateUtil;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.io.File;
import java.util.Date;

public class GoodDetailActivity extends BaseActivity implements BaseActivity.TitleListener {
    private Goods mGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        mGoods =  this.getIntent().getParcelableExtra(Constants.EXTRA_DATA);
        if (mGoods == null) {
            finish();
            return;
        }
        setTitleListener(this);
        initView();
        setPageTag(TagManage.GOOD_DETAIL_ACTIVITY);
    }


    private void initView() {
        NetworkImageView iv = mViewFinder.find(R.id.iv_img);
        if(!ArraysUtils.isEmpty(mGoods.imgs)){
            String img = mGoods.imgs.get(0);
            if(img.contains("http")){
                iv.setImageUrl(img, RequestManager.getImageLoader());
            }else{
                iv.setImageURI(Uri.fromFile(new File(img)));
            }
        }

        mViewFinder.setText(R.id.tv_good_name, mGoods.name);
        mViewFinder.setText(R.id.tv_price, getString(R.string.msg_before_price, NumFormat.formatPrice(mGoods.price)));
        ShopInfo shopInfo = PreferenceUtils.getObject(this, ShopInfo.class);
        if (shopInfo != null) {
            mViewFinder.setText(R.id.tv_seller_name, shopInfo.name);
        }
        if (ArraysUtils.isEmpty(mGoods.norms)) {
            mViewFinder.find(R.id.ll_norms).setVisibility(View.GONE);
            mViewFinder.find(R.id.line_norms).setVisibility(View.GONE);
            if(mGoods.type == 1) {
                mViewFinder.find(R.id.line_cell).setVisibility(View.GONE);
            }
        } else {
            mViewFinder.setText(R.id.tv_norms, mGoods.norms.get(0).name);
        }

        if (mGoods.type == 1) {
            mViewFinder.find(R.id.ll_service_time).setVisibility(View.GONE);
            mViewFinder.find(R.id.line_service_time).setVisibility(View.GONE);
        } else {
            Date date = new Date();
            date.setTime(date.getTime() + 3600 * 1000);
            mViewFinder.setText(R.id.tv_service_time, DateUtil.formatDate(getString(R.string.date_fomat_5), date.getTime()));
        }

        mViewFinder.onClick(R.id.rl_preview, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
        initGoodBrief(mGoods.brief);
    }

    private void initGoodBrief(String html){
        String value = TextUtils.isEmpty(html) ? "":html;
        WebView webView = mViewFinder.find(R.id.wv_brief);
        webView.loadData(value, "text/html; charset=UTF-8", null);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(mGoods.name);
    }
}
