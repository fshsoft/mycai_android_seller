package com.fiftyonemycai365.seller.business.adapter;

import java.util.List;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.WeekInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeekListAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<WeekInfo> list;

	public WeekListAdapter(Activity context, List<WeekInfo> list) {
		this.layoutInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (list != null) {
			count = list.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.week_item, null);
			holder.week_name = (TextView) convertView
					.findViewById(R.id.week_name);
			holder.week_chick = (ImageView) convertView
					.findViewById(R.id.week_chick);
			holder.week_chick_layout=(RelativeLayout) convertView.findViewById(R.id.week_chick_layout);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.week_name.setText(list.get(position).getWeekname());
		if (list.get(position).getIsChick() == 0) {
			holder.week_chick.setImageResource(R.drawable.week_no_chick);
		} else if (list.get(position).getIsChick() == 1) {
			holder.week_chick.setImageResource(R.drawable.week_chick);
		} else if (list.get(position).getIsChick() == 2) {
			holder.week_chick.setImageResource(R.drawable.no_week_chick);
		}
		holder.week_chick_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position).getIsChick() == 0) {
					list.get(position).setIsChick(1);
//					((ImageView) v).setImageResource(R.drawable.week_chick);
				} else if (list.get(position).getIsChick() == 1) {
					list.get(position).setIsChick(0);
//					((ImageView) v).setImageResource(R.drawable.week_no_chick);
				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	private class ViewHolder {

		public TextView week_name;
		public ImageView week_chick;
		public RelativeLayout week_chick_layout;
	}
}
