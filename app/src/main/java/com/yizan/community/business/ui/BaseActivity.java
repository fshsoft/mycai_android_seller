package com.yizan.community.business.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yizan.community.business.R;
import com.zongyou.library.app.AppManager;
import com.zongyou.library.app.AppUtils;
import com.zongyou.library.platform.ZYStatConfig;
import com.zongyou.library.util.LogUtils;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.util.ViewFinder;

/**
 * Activity基类
 *
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午5:23:25
 */
public class BaseActivity extends FragmentActivity {
    public ImageButton mTitleLeft;
    public TextView mTitle;
    public View mTitleRight;
    private TitleListener mTitleSetListener;
    protected ViewFinder mViewFinder;

    protected String mPageTag;

    protected void onResume() {
        super.onResume();
        ZYStatConfig.onPageResume(this, getPageTag());
    }

    protected void onPause() {
        super.onPause();
        ZYStatConfig.onPagePause(this, getPageTag());
    }

    protected void setPageTag(String pageTag){
        mPageTag = pageTag;
    }

    public String getPageTag() {
        if (TextUtils.isEmpty(mPageTag) && ZYStatConfig.isNeedStat()) {
            LogUtils.e("STAT_TAG", "NET SEET TAG ====>>: " + this.getClass().getName());
        }
        return mPageTag;
    }

    interface TitleListener {
        public void setTitle(TextView title, ImageButton left, View right);
    }

    public void setTitleListener(TitleListener listener) {
        setCustomTitle(R.layout.titlebar);
        initTitle();
        mTitleSetListener = listener;

        mTitleSetListener.setTitle(mTitle, mTitleLeft, mTitleRight);
    }

    public void setTitleListener(TitleListener listener, int titleLayoutRes) {
        setCustomTitle(titleLayoutRes);
        initTitle();
        mTitleSetListener = listener;

        mTitleSetListener.setTitle(mTitle, mTitleLeft, mTitleRight);
    }

    public void setCustomTitle(int titleRes) {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, titleRes);
    }

    public void initTitle() {
        mTitleLeft = (ImageButton) findViewById(R.id.title_left);
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = findViewById(R.id.title_right);
        mTitleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				AppManager.getAppManager().finishActivity();
                finishActivity();
            }
        });
    }

    public void initTitle(int titleRes, boolean titleLeftVisible,
                          int titleRightRes) {
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.titlebar);
        mTitleLeft = (ImageButton) findViewById(R.id.title_left);
        mTitle = (TextView) findViewById(R.id.title);
        mTitleRight = (Button) findViewById(R.id.title_right);
        mTitleLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AppUtils.hideSoftInput(BaseActivity.this);
//				AppManager.getAppManager().finishActivity();
                finishActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AppUtils.hideSoftInput(BaseActivity.this);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        AppManager.getAppManager().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mViewFinder = new ViewFinder(this);
    }

    @Override
    protected void onDestroy() {
        mViewFinder = null;
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
    }

    public void finishActivity() {
        AppManager.getAppManager().finishActivity(this);
//		finish();
    }

    public View setViewText(int layoutId, String value) {
        TextView v = (TextView) findViewById(layoutId);
        v.setText(value);
        return v;
    }

    public View setViewText(int layoutId, Spanned value) {
        TextView v = (TextView) findViewById(layoutId);
        v.setText(value);
        return v;
    }

    public View setViewText(int layoutId, int res) {
        TextView v = (TextView) findViewById(layoutId);
        v.setText(res);
        return v;
    }

    public View setViewClickListener(int layoutId, OnClickListener listener) {
        View v = findViewById(layoutId);
        v.setOnClickListener(listener);
        return v;
    }

    public View setViewVisible(int layoutId, int visibility) {
        View v = findViewById(layoutId);
        v.setVisibility(visibility);
        return v;
    }

    public View setViewImage(int layoutId, String url, int defaultImage) {
        NetworkImageView v = (NetworkImageView) findViewById(layoutId);
        if (defaultImage > 0) {
            v.setDefaultImageResId(defaultImage);
            v.setErrorImageResId(defaultImage);
        }
        v.setImageUrl(url, RequestManager.getImageLoader());
        return v;
    }

    public View setViewImage(int layoutId, int imageId) {
        ImageView v = (ImageView) findViewById(layoutId);
        if (imageId > 0) {
            v.setImageResource(imageId);
        }
        return v;
    }

    protected void hideSoftInputView() {
        try {
            if (this.getCurrentFocus() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(this.getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
        }
    }
}