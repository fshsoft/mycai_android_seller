package com.fiftyonemycai365.seller.business.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.fiftyonemycai365.seller.business.ui.BaseActivity.TitleListener;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.fiftyonemycai365.seller.business.view.DateTimePickDialogUtil;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.DateUtil;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 请假添加Activity
 */
public class LeaveTimeActivity extends BaseActivity implements TitleListener {

	private RelativeLayout leave_start_layout, leave_end_layout;
	private TextView leave_start_text, leave_end_text, remark_text;
	private String startime, endtime;

	private Button time_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leave_time);
		setTitleListener(this);
		initView();
		setPageTag(TagManage.LEAVE_TIME_ACTIVITY);
	}

	private void initView() {
		leave_start_layout = (RelativeLayout) findViewById(R.id.leave_start_layout);
		leave_end_layout = (RelativeLayout) findViewById(R.id.leave_end_layout);
		leave_start_text = (TextView) findViewById(R.id.leave_start_text);
		leave_end_text = (TextView) findViewById(R.id.leave_end_text);
		time_confirm = (Button) findViewById(R.id.time_confirm);

		remark_text = (TextView) findViewById(R.id.remark_text);

		startime = DateUtil.formatDate(Constants.PATTERN_Y_M_H_M,
				System.currentTimeMillis());
		leave_start_text.setText(startime);
		endtime = DateUtil.formatDate(Constants.PATTERN_Y_M_H_M,
				System.currentTimeMillis() + 3600*1000);
		leave_end_text.setText(endtime);
		leave_start_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						LeaveTimeActivity.this, startime);
				dateTimePicKDialog.dateTimePicKDialog(leave_start_text);
				startime = leave_start_text.getText().toString();
			}
		});

		leave_end_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						LeaveTimeActivity.this, endtime);
				dateTimePicKDialog.dateTimePicKDialog(leave_end_text);
				endtime = leave_end_text.getText().toString();
			}
		});

		time_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (remark_text.getText().length() > 0) {
					loaddata();
				} else {
					ToastUtils.show(LeaveTimeActivity.this,
							R.string.time_leave_text);
				}
			}
		});

	}

	public long date2longtime(String pattern,String type) {
		SimpleDateFormat format = new SimpleDateFormat( type );
//		format.setTimeZone(TimeZone.getDefault());
		long  datetime = 0;
		try {
			Date date = format.parse(pattern);

			datetime=date.getTime() - 8 * 3600 * 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return datetime;
	}
	private Map<String, String> getParams() {

		Map<String, String> map = new HashMap<String, String>();
		map.put("beginTime", date2longtime((String) leave_start_text.getText(),
				Constants.PATTERN_Y_M_H_M)/1000 + "");

		map.put("endTime",date2longtime((String) leave_end_text.getText(),
				Constants.PATTERN_Y_M_H_M)/1000 + "");
		map.put("remark",remark_text.getText().toString());
		return map;
	}

	private void loaddata() {

		if (NetworkUtils.isNetworkAvaiable(LeaveTimeActivity.this)) {

			CustomDialogFragment.getInstance(getSupportFragmentManager(),LeaveTimeActivity.class.getName());
			ApiUtils.post(LeaveTimeActivity.this,
					URLConstants.STAFFLEAVE_CREATE, getParams(),
					BaseResult.class, new Listener<BaseResult>() {

						@Override
						public void onResponse(BaseResult response) {
							CustomDialogFragment.dismissDialog();
							if (O2OUtils.checkResponse(LeaveTimeActivity.this,
									response)) {
								ToastUtils.show(LeaveTimeActivity.this,
										R.string.msg_succ_add);
								startActivity(new Intent(LeaveTimeActivity.this, LeaveHistoryListActivity.class));
								finishActivity();
							}

						}

					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							CustomDialogFragment.dismissDialog();
							ToastUtils.show(LeaveTimeActivity.this,
									R.string.msg_failed_add);
						}

					});

		} else {

			ToastUtils
					.show(LeaveTimeActivity.this, R.string.msg_error_network);
		}

	}

	@Override
	public void setTitle(TextView title, ImageButton left, View right) {
		title.setText(R.string.time_leave_txt);
		((TextView)right).setText(R.string.time_leave_history_txt);
		right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LeaveTimeActivity.this, LeaveHistoryListActivity.class));
			}
		});

	}
}
