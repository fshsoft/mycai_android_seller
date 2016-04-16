package com.yizan.community.business.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.LeaveTimeInfo;
import com.yizan.community.business.ui.BaseActivity.TitleListener;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单详情Activity
 * 
 * @author gyli
 * @time 2015-6-12 下午8:20:38
 */
public class LeaveDetailsActivity extends BaseActivity implements OnClickListener, TitleListener {


	private LeaveTimeInfo mLeaveTimeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		setTitleListener(this);
		setPageTag(TagManage.LEAVE_DETAILS_ACTIVITY);
	}

	private void initView() {
		setContentView(R.layout.activity_leave_detail);
		mLeaveTimeInfo = (LeaveTimeInfo)this.getIntent().getSerializableExtra(Constants.EXTRA_DATA);
		if(mLeaveTimeInfo == null){
			finish();
			return;
		}
		initViewData(mLeaveTimeInfo);

	}

	private void initViewData(LeaveTimeInfo orderInfo) {
		mLeaveTimeInfo = orderInfo;

		SimpleDateFormat format = new SimpleDateFormat(getString(R.string.date_fomat_6));
		SimpleDateFormat oldFormat = new SimpleDateFormat(getString(R.string.date_fomat_2));
		try {
			Date begin = oldFormat.parse(orderInfo.beginTime);
			Date end = oldFormat.parse(orderInfo.endTime);
			setViewText(R.id.tv_time, getString(R.string.leave_time) + format.format(begin) + " - " + format.format(end));
		}catch(Exception e){

		}
		setViewText(R.id.tv_remark, getString(R.string.leave_remark) + orderInfo.remark);
		setViewClickListener(R.id.btn_call, this);


	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_call:
				String tel = PreferenceUtils.getValue(this, Constants.PREFERENCE_SERVICE_TEL, "");
				if(!TextUtils.isEmpty(tel)) {
					try {
						IntentUtils.dial(this, tel);
					}catch (Exception e){
					}

				}
				break;

		}
	}

	@Override
	public void setTitle(TextView title, ImageButton left, View right) {
		title.setText(R.string.levae_check);
	}
}
