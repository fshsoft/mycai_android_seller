package com.yizan.community.business.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.StatisticsMonth;
import com.yizan.community.business.util.NumFormat;

import java.util.List;


public class StatisticsMonthAdapter extends BaseAdapter {

	private Context context;
	private List<StatisticsMonth> list;
	private LayoutInflater layoutInflater;

	public StatisticsMonthAdapter(Context context, List<StatisticsMonth> list) {

		this.context = context;
		this.list = list;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void setListData(List<StatisticsMonth> list) {

		this.list = list;

	}

	@Override
	public int getCount() {
		int count = 0;

		if (list != null && list.size() != 0) {
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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater
					.inflate(R.layout.item_statistics_month, null);

			holder.item_month=(TextView) convertView.findViewById(R.id.item_month);
			holder.item_year=(TextView) convertView.findViewById(R.id.item_year);
			holder.item_number=(TextView) convertView.findViewById(R.id.item_number);
			holder.item_price=(TextView) convertView.findViewById(R.id.item_price);
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		if(list.get(position).month>0){
			String year =String.valueOf(list.get(position).month).substring(0, 4);
			String month=String.valueOf(list.get(position).month).substring(4);
			holder.item_month.setText(Integer.valueOf(month)+context.getString(R.string.msg_month));
			holder.item_year.setText(year+context.getString(R.string.msg_year));
		}else{
			holder.item_month.setText("");
			holder.item_year.setText("");
		}
		
		holder.item_number.setText(context.getString(R.string.msg_turnover,list.get(position).num));
		
		String total=String.format(context.getString(R.string.msg_accumulative_money), NumFormat.formatPrice(list.get(position).total));
		SpannableStringBuilder style = new SpannableStringBuilder(total);
		
		style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.textPrice)), 4, total.length() ,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		
		holder.item_price.setText(style);
		return convertView;
	}

	private class ViewHolder {
		
		public TextView item_month,item_year,item_number,item_price;
	}

}
