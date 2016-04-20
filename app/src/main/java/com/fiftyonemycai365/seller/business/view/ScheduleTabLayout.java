package com.fiftyonemycai365.seller.business.view;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.ScheduleDayInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleTabLayout extends LinearLayout {
	private TextView mTvDay, mTvWeek;
	private View mVSelected;

	public ScheduleTabLayout(Context context) {
		super(context);
		initView(context);
	}

	public ScheduleTabLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View rootView = inflater.inflate(R.layout.layout_schedule_tab,null);
		this.addView(rootView);
		mTvDay = (TextView) rootView.findViewById(R.id.tv_day);
		mTvWeek = (TextView) rootView.findViewById(R.id.tv_week);
		mVSelected = rootView.findViewById(R.id.v_selected);

	}

	public void setSelected(boolean bSelected){
		mVSelected.setVisibility(bSelected?View.VISIBLE:View.INVISIBLE);
	}
	
	public void initData(ScheduleDayInfo info){
		mTvDay.setText(info.day);
		mTvWeek.setText(info.week);
	}

}
