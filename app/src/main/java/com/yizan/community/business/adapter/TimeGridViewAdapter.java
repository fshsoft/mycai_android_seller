package com.yizan.community.business.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.SeverceTimeInfo;

import java.util.List;

public class TimeGridViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<SeverceTimeInfo> list;
    private Context mContext;

    public TimeGridViewAdapter(Activity context, List<SeverceTimeInfo> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.mContext = context;
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
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.time_item, null);
            holder.time_text = (Button) convertView.findViewById(R.id.time_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position < 47) {
            holder.time_text.setText(list.get(position).getHours() + "-" + list.get(position + 1).getHours());
        } else {
            holder.time_text.setText(list.get(position).getHours() + "-" + "24:00");
        }
        holder.time_text.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                if (list.get(position).isChick) {
                    list.get(position).isChick = false;
                    btn.setBackgroundResource(R.drawable.frame_gray_normal);
                    btn.setTextColor(mContext.getResources().getColor(R.color.textGray));

                } else {
                    list.get(position).isChick = true;
                    btn.setBackgroundResource(R.drawable.corners_pink_normal);
                    btn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
        });
        if (list.get(position).isChick) {
            holder.time_text.setBackgroundResource(R.drawable.corners_pink_normal);
            holder.time_text.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.time_text.setBackgroundResource(R.drawable.frame_gray_normal);
            holder.time_text.setTextColor(mContext.getResources().getColor(R.color.textGray));
        }
        return convertView;
    }

    private class ViewHolder {
        public Button time_text;
    }
}
