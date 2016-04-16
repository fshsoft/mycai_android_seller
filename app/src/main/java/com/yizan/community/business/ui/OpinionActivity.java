package com.yizan.community.business.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 意见反馈
 */
public class OpinionActivity extends BaseActivity implements BaseActivity.TitleListener {

	private TextView opinion_comfimation;
	private EditText opinion_edi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		setTitleListener(this);
		setPageTag(TagManage.OPINION_ACTIVITY);
	}

	private void initView() {
		setContentView(R.layout.opinion_layout);
		opinion_comfimation = (TextView) findViewById(R.id.opinion_comfimation);
		opinion_edi = (EditText) findViewById(R.id.opinion_edi);

		opinion_comfimation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				updata();

			}
		});

	}

	@Override
	public void setTitle(TextView title, ImageButton left, View right) {

		title.setText(R.string.opinion);

	}

	private void updata() {
		if (TextUtils.isEmpty(opinion_edi.getText().toString().trim())) {
			ToastUtils.show(OpinionActivity.this, R.string.opinion_edi_text);
			return;
		}
		if (NetworkUtils.isNetworkAvaiable(OpinionActivity.this)) {

			CustomDialogFragment.getInstance(getSupportFragmentManager(), OpinionActivity.class.getName());
			ApiUtils.post(getApplicationContext(), URLConstants.FEEDBACK_CREATE, getParams(), BaseResult.class, new Listener<BaseResult>() {

				@Override
				public void onResponse(BaseResult response) {
					CustomDialogFragment.dismissDialog();
					if (response.code == 0) {
						ToastUtils.show(OpinionActivity.this, R.string.opinion_finsh);
						finish();
					}

				}

			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					CustomDialogFragment.dismissDialog();
				}

			});

		} else {

			ToastUtils.show(OpinionActivity.this, R.string.msg_error_network);
		}

	}

	private Map<String, String> getParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("deviceType", "Android");
		map.put("content", opinion_edi.getText().toString().trim());

		return map;
	}

}
