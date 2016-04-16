package com.yizan.community.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.StaffLeaveInfo;

import java.util.List;

/**
 * Created by admin on 2015/10/29.
 */
public class LeaveHistoryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<StaffLeaveInfo> list;

    public LeaveHistoryAdapter(Context context,List<StaffLeaveInfo> listData){
        this.context = context;
        this.list = listData;
        this.mInflater = LayoutInflater.from(context);
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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null){
            view = mInflater.inflate(R.layout.item_leave_historysss,null);
            holder = new Holder(view);
            view.setTag(holder);
        }else{
            holder = (Holder)view.getTag();
        }
        holder.setData(holder,i);
        return view;
    }
    class Holder{
        private TextView content,status,time;

        public Holder(View view){
            content = (TextView)view.findViewById(R.id.leave_content);
            status = (TextView)view.findViewById(R.id.leave_status);
            time = (TextView)view.findViewById(R.id.leave_time);
        }

        public void setData(Holder holder,final int position){
            StaffLeaveInfo item = list.get(position);
            time.setText(context.getResources().getString(R.string.msg_leave_time_text)+item.beginTime+"-"+item.endTime);
            content.setText(context.getResources().getString(R.string.msg_leave_reason_text)+item.remark);
            status.setText(item.statusStr);
            if (item.status == 1){
                status.setTextColor(context.getResources().getColor(R.color.textLink));
            }else
            if(item.status == -1){
                status.setTextColor(context.getResources().getColor(R.color.primary));
            }else if (item.status == 0){
                status.setTextColor(context.getResources().getColor(R.color.sh));
            }
        }
    }
}
