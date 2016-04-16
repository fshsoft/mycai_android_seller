package com.yizan.community.business.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.widget.ProgressWebView;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-27 上午10:06:32
 */
public class WebViewActivity extends BaseActivity implements TitleListener {
	private ProgressWebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		setTitleListener(this);

		mWebView = (ProgressWebView) findViewById(R.id.webview);
		mWebView.setProgressWebView(android.R.attr.progressBarStyleHorizontal);
		mWebView.setOnProgressTypeLinterner(new ProgressWebView.OnProgressTypeLinterner() {

			@Override
			public void endType(WebView view, ProgressBar progressbar) {

			}

			@Override
			public void startType(WebView view, ProgressBar progressbar) {

			}

		});
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
			url += "agent=m&token="+ PreferenceUtils.getValue(WebViewActivity.this,Constants.TOKEN_LOGIN,"");


		}
		mWebView.loadUrl(url);
	}

	private TextView mTitleTextView;


	@Override
	public void setTitle(TextView title, ImageButton left, View right) {
		mTitleTextView = title;
	}
}

