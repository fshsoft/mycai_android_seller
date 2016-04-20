package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.view.View;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.StaffInfo;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class StaffListAdapter extends CommonAdapter<StaffInfo> {

    public StaffListAdapter(Context context, List<StaffInfo> datas) {
        super(context, datas, R.layout.item_staff_list);
    }

    public void setList(List<StaffInfo> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<StaffInfo> getSelList(){
        ArrayList<StaffInfo> list = new ArrayList<>();
        if(getCount() == 0){
            return list;
        }
        for (StaffInfo item: mDatas){
            if(item.bSel){
                list.add(item);
            }
        }
        return list;
    }

    public void selItem(int position, boolean bSel){
        if(position < getCount()){
            mDatas.get(position).bSel = bSel;
            notifyDataSetChanged();
        }
    }

    public void selSignalItem(int position, boolean bSel){
        if(position >= getCount() || getCount() == 0){
            return;
        }
        for (int i=0; i<mDatas.size(); i++){
            if(i == position){
                mDatas.get(i).bSel = bSel;
            }else {
                mDatas.get(i).bSel = false;
            }
        }
    }

    @Override
    public void convert(final ViewHolder helper, StaffInfo item, final int number) {
        try {
            if(number >= getCount() - 1){
                helper.setViewVisible(R.id.line_deliver, View.INVISIBLE);
            }else{
                helper.setViewVisible(R.id.line_deliver, View.VISIBLE);
            }
            helper.setText(R.id.tv_name, item.name);
            helper.setText(R.id.tv_phone, mContext.getString(R.string.msg_before_price,item.mobile));
            helper.setViewVisible(R.id.iv_sel, item.bSel?View.VISIBLE:View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}