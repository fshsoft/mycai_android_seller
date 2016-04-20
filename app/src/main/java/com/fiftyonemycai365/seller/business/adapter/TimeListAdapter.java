
package com.fiftyonemycai365.seller.business.adapter;

import java.util.List;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.StimelistsInfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimeListAdapter extends  BaseAdapter{

	private LayoutInflater layoutInflater;
	private List<StimelistsInfo> listdata;
	private StringBuffer sb = new StringBuffer();
	
	public TimeListAdapter(Activity context,List<StimelistsInfo> listdata) {
		this.layoutInflater = LayoutInflater.from(context);
		this.listdata=listdata;
	}
	
	@Override
	public int getCount() {
		int count =0;
		if(null!=listdata){
			count=listdata.size();
		}
		
		return count;
	}
	
	public void deleteon(int position) {
		listdata.remove(position);
	}
	public void setnotifyDataSetChanged(List<StimelistsInfo> listdata) {
		this.listdata=listdata;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		
		return listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.time_list_item, null);
			holder.time_text=(TextView) convertView.findViewById(R.id.time_text);
			holder.week_text=(TextView) convertView.findViewById(R.id.week_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(!"".equals(listdata.get(position).times)){
			for(int i=0;i<listdata.get(position).shifts.size();i++){
				List<String> shifts = listdata.get(position).shifts;
				List<String> hours = listdata.get(position).hours;
				sb.append(shifts.get(i)+"("+hours.get(i)+")   ");
			}
			holder.time_text.setText(sb);
			sb.delete(0,sb.length());
		}else{
			holder.time_text.setText("");
		}
		
		if(!"".equals(listdata.get(position).weeks)){
			holder.week_text.setText(listdata.get(position).weeks);
		}else{
			holder.week_text.setText("");
		}
		return convertView;
	}
	private class ViewHolder {
		public  TextView  time_text,week_text;
		
	}
}
