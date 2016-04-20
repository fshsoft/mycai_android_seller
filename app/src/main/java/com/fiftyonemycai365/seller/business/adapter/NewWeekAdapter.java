package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.NewWeekInfo;

import java.util.List;

/**
 * Created by admin on 2015/11/3.
 */
public class NewWeekAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<NewWeekInfo> list;

    public NewWeekAdapter(List<NewWeekInfo> listData, Context mContext){
        this.context = mContext;
        this.list = listData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_week_grid,null);
            holder.mText = (TextView)view.findViewById(R.id.week_grid_text);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        if (list.get(i).isChick == 1){
            holder.mText.setBackgroundResource(R.drawable.oval_bg);
            holder.mText.setTextColor(Color.WHITE);
        }else{
            holder.mText.setBackgroundResource(R.drawable.oval_bg_white);
            holder.mText.setTextColor(context.getResources().getColor(R.color.textPrimary));
        }
        holder.mText.setText(list.get(i).name);
        return view;
    }
    private class ViewHolder{
        public TextView mText;
    }
}
