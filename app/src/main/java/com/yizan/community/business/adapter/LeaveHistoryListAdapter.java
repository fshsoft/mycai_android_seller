package com.yizan.community.business.adapter;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.LeaveTimeInfo;
import com.zongyou.library.widget.adapter.CommonAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeaveHistoryListAdapter extends CommonAdapter<LeaveTimeInfo> {

    public LeaveHistoryListAdapter(Activity context, List<LeaveTimeInfo> list) {
        super(context, list, R.layout.item_leave_history);
    }


    public void deleteon(int position) {
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void deleteon(List<LeaveTimeInfo> removeList) {
        mDatas.removeAll(removeList);
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).isChecked = true;
        }
        notifyDataSetChanged();
    }

    public List<String> getSelectedList(){
        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < mDatas.size(); i++) {
            if(mDatas.get(i).isChecked){
                ids.add(mDatas.get(i).id);
            }
        }
        return ids;
    }

    @Override
    public void convert(com.zongyou.library.widget.adapter.ViewHolder helper, final LeaveTimeInfo item, int position) {
        SimpleDateFormat format = new SimpleDateFormat(mContext.getResources().getString(R.string.date_fomat_1));
        SimpleDateFormat oldFormat = new SimpleDateFormat(mContext.getResources().getString(R.string.date_fomat_2));
        try {
//            Date begin = oldFormat.parse(item.beginTime);
//            Date end = oldFormat.parse(item.endTime);
            helper.setText(R.id.tv_begin_time, item.beginTime.replaceAll("-", "."));
            helper.setText(R.id.tv_end_time, item.endTime.replaceAll("-", "."));
//			item.createTime
            Date createTime = oldFormat.parse(item.createTime);
            helper.setText(R.id.tv_time, format.format(createTime));
            CheckBox cb = helper.getView(R.id.cb_check);
            cb.setChecked(item.isChecked);

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.isChecked = isChecked;
                }
            });
        } catch (Exception e) {

        }
        helper.setText(R.id.tv_remark, item.remark);


    }

}
