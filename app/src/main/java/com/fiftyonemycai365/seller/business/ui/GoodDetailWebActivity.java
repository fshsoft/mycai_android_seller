package com.fiftyonemycai365.seller.business.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.UserInfo;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.Date;

public class GoodDetailWebActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    public static final int REQUEST_CODE = 0xF10;
    private WebView mWebView;
    private Button mRightButton;

    private boolean mIsLoadOK = false;
    private int mGoodId;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_good_detail_web);
        setContentView(R.layout.activity_webview);
        setTitleListener(this);

        mGoodId = getIntent().getIntExtra(Constants.EXTRA_ID, 0);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);


        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CustomDialogFragment.dismissDialog();
                mIsLoadOK = true;
            }
        });
        UserInfo info = PreferenceUtils.getObject(this,
                UserInfo.class);
        String token = PreferenceUtils.getValue(this, Constants.TOKEN_LOGIN, "");
        String url = String.format(URLConstants.GOOD_DETAIL_EDIT, token, info.id, mGoodId);

        if (null != url) {
            String[] us = url.split("\\?");
            if (us.length == 1) {
                url += "?";
            } else
                url += "&";
            url += "agent=m";
            url += "&tx=";
            url += (new Date()).getTime();
        }
        mJsToJava = new JsToJava(this);
        mWebView.addJavascriptInterface(mJsToJava, "stub");

        CustomDialogFragment.getInstance(getSupportFragmentManager(), this.getClass().getName());
        mWebView.loadUrl(url);
        setPageTag(TagManage.GOOD_DETAIL_WEB_ACTIVITY);

    }
    private JsToJava mJsToJava;

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.title_activity_good_detail_web);
        mRightButton = (Button) right;
        mRightButton.setText(R.string.save);
        mRightButton.setOnClickListener(this);
    }

    public void finishOk(String result){
        if(TextUtils.isEmpty(result)){
            result = "";
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_DATA, result);
        setResult(Activity.RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void onClick(View v) {

        if(mIsLoadOK) {
            mWebView.loadUrl("javascript:getBrief()");
        }
    }

    private class JsToJava
    {
        private GoodDetailWebActivity mContext;
        public JsToJava(GoodDetailWebActivity context){
            mContext = context;
        }

        @JavascriptInterface
        public void jsMethod(String paramFromJS)
        {
            finishOk(paramFromJS);
        }
    }
}
