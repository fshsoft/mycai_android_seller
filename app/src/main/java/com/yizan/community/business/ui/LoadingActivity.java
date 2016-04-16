package com.yizan.community.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.yizan.community.business.R;
import com.yizan.community.business.service.O2OService;
import com.yizan.community.business.util.O2OUtils;

public class LoadingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ImageView iv = new ImageView(this);
		iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		iv.setBackgroundResource(R.drawable.fullscreen);
		setContentView(iv);

		O2OService.initConfig(getApplicationContext());

		new CountDownTimer(1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
//				if (PreferenceUtils.getValue(LoadingActivity.this, "isfirst",
//						true)) {
//					startActivity(new Intent(LoadingActivity.this,
//							NewbieGuideActivity.class));
//				} else {
//					startActivity(new Intent(getApplicationContext(),
//							LoginActivity.class));
					if (!O2OUtils.isLogin(getApplicationContext())) {
						startActivity(new Intent(getApplicationContext(),
								LoginActivity.class));
					} else {
						startActivity(new Intent(LoadingActivity.this,
								MainActivity.class));
					}
				//}
				finish();
			}
		}.start();
	}
}