package com.fiftyonemycai365.seller.business.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.OrderInfo;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.fiftyonemycai365.seller.business.util.TimestampUtil;

public class StatisticsAdapter extends BaseAdapter {

	private Context context;
	private List<OrderInfo> list;
	private LayoutInflater layoutInflater;
	public StatisticsAdapter(Context context, List<OrderInfo> list) {

		this.context = context;
		this.list = list;
		this.layoutInflater = LayoutInflater.from(context);
	}
	
public void setListData(List<OrderInfo> list){
		
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
					.inflate(R.layout.statistics_item, null);
			
			holder.statistics_iamge=(NetworkImageView) convertView.findViewById(R.id.statistics_iamge);
			holder.statistics_price=(TextView) convertView.findViewById(R.id.statistics_price);
			holder.sevice_title=(TextView) convertView.findViewById(R.id.sevice_title);
			holder.sevice_time=(TextView) convertView.findViewById(R.id.sevice_time);
			
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.statistics_iamge.setDefaultImageResId(R.drawable.ic_default);
		holder.statistics_iamge.setErrorImageResId(R.drawable.ic_default);
//		if(list.get(position).goods!=null){
//			holder.statistics_iamge.setImageUrl(list.get(position).goods.icon,
//					RequestManager.getImageLoader());
//		}else{
//			holder.statistics_iamge.setImageUrl("",
//					RequestManager.getImageLoader());
//		}
//
//		if(list.get(position).goods!=null){
//			holder.sevice_title.setText(context.getResources().getText(R.string.detail_servicer_title)+"  "+list.get(position).goods.name);
//		}else{
//			holder.sevice_title.setText(context.getResources().getText(R.string.detail_servicer_title));
//		}
			holder.statistics_price.setText(context.getString(R.string.msg_before_price, NumFormat.formatPrice(list.get(position).payFee)));
		if(list.get(position).createTime>0){
			holder.sevice_time.setText(TimestampUtil.getFormatString(
					String.valueOf(list.get(position).createTime), context.getString(R.string.date_fomat_4)));
		}else{
			holder.sevice_time.setText("");
		}
		
		return convertView;
	}
	
	private class ViewHolder {

		public NetworkImageView statistics_iamge;
		public TextView statistics_price,sevice_title,sevice_time;
	}

}
