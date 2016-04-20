package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.view.View;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.TimeInfo;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class NewScheduleDateAdapter extends CommonAdapter<TimeInfo> {
    private final String TAG = "ScheduleDateAdapter";
    private int MAX_COUNT = 5;
    private int mLastPositon = -1;
    public NewScheduleDateAdapter(Context context, List<TimeInfo> datas) {
        super(context, datas, R.layout.item_schedule_date);
    }


    @Override
    public int getCount() {
        if (mDatas.size() > MAX_COUNT) {
            return MAX_COUNT;
        }
        return super.getCount();
    }

    public void addItem(TimeInfo value) {
        mDatas.add(value);
        notifyDataSetChanged();
    }

    public void removeItem(int pos) {
        if (pos >= 0 && mDatas.size() > pos) {
            mDatas.remove(pos);
            notifyDataSetChanged();
        }
    }

    public List<TimeInfo> getDatas() {
        return mDatas;
    }

    public int getLastPositon(){
        return mLastPositon;
    }
    public void setLastPositon(int positon){
        mLastPositon = positon;
        notifyDataSetChanged();
    }

    @Override
    public void convert(final ViewHolder helper, TimeInfo item, int number) {
        helper.setText(R.id.tv_day, item.date);
        helper.setText(R.id.tv_week, item.week);
        if(number == mLastPositon){
            helper.setViewVisible(R.id.v_selected, View.VISIBLE);
        }else{
            helper.setViewVisible(R.id.v_selected, View.INVISIBLE);
        }
    }


}