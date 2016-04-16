package com.yizan.community.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizan.community.business.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by admin on 2015/11/3.
 */
public class DeliveryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> list;

    public DeliveryAdapter(List<String> listData,Context mContext){
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
            view = mInflater.inflate(R.layout.item_time_gridview,null);
            holder.mText = (TextView)view.findViewById(R.id.gridview_text);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        holder.mText.setText(list.get(i));
        return view;
    }
    private class ViewHolder{
        public TextView mText;
    }
}
