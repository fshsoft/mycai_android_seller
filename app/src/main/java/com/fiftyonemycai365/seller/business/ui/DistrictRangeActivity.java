package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.zongyou.library.app.AppManager;
import com.zongyou.library.widget.ProgressWebView;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-27 上午10:06:32
 */
public class DistrictRangeActivity extends BaseActivity implements
		BaseActivity.TitleListener {
	private ProgressWebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		setTitleListener(this);
		mWebView = (ProgressWebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.setOnTitleChange(new com.zongyou.library.widget.ProgressWebView.OnTitleChange() {

			@Override
			public void updateTitle(String title) {
				mTitleTextView.setText(title);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		String url = getIntent().getStringExtra(Constants.EXTRA_URL);
		if (null != url) {
			String[] us = url.split("\\?");
			if (us.length == 1) {
				url += "?";
			} else
				url += "&";
			url += "agent=m";
		}
		mWebView.loadUrl(url);
	}

	private TextView mTitleTextView;

	@Override
	public void setTitle(TextView title, ImageButton left, View right) {
		mTitleTextView = title;
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if ( mWebView.canGoBack()) {
					mWebView.goBack(); // goBack()表示返回WebView的上一页面
					
				}else {
					AppManager.getAppManager().finishActivity();
				}
				
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
