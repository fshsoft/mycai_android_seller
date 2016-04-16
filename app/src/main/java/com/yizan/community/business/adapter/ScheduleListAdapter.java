package com.yizan.community.business.adapter;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.OrderInfo;
import com.zongyou.library.util.DateUtil;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

public class ScheduleListAdapter extends CommonAdapter<OrderInfo> {
    private boolean mbEditAble = false;

    public ScheduleListAdapter(Context context, List<OrderInfo> datas, boolean bEditAble) {
        super(context, datas, R.layout.item_schedule_layout);
        mbEditAble = bEditAble;
    }

    public void setList(List<OrderInfo> list) {
        mDatas.clear();
        addAll(list);
    }

    public void selectedAll(boolean bSelectAll) {
        if (mDatas == null) {
            return;
        }
        for (int i = 0; i < mDatas.size(); i++) {
//			mDatas.get(i).bSelected = bSelectAll;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder helper, OrderInfo item, int position) {
        Calendar gc = GregorianCalendar.getInstance();
        gc.setTimeInMillis(item.appointTime * 1000 + gc.get(Calendar.ZONE_OFFSET));
        helper.setText(R.id.tv_hour, DateUtil.formatDate(mContext.getString(R.string.date_fomat_3), gc.getTimeInMillis()));
        //0：暂无安排 1：有单子 -1：停止接单
//        helper.setViewVisible(R.id.ll_no_order, (item.status != ScheduleInfo.IState.ORDERED) ? View.VISIBLE : View.INVISIBLE);
//        helper.setViewVisible(R.id.ll_ordered, (item.status == ScheduleInfo.IState.ORDERED) ? View.VISIBLE : View.INVISIBLE);
//        helper.setViewVisible(R.id.cb_check, mbEditAble ? View.VISIBLE : View.GONE);
//		if(mbEditAble){
//			CheckBox cb = (CheckBox)helper.getView(R.id.cb_check);
//			cb.setChecked(item.bSelected);
//		}
//		if(item.status == ScheduleInfo.IState.IDLE){
//			helper.setText(R.id.tv_state, "暂无安排");
//		}else if(item.status == ScheduleInfo.IState.ORDERED){
//			helper.setText(R.id.tv_service, item.goodsName);
//			helper.setText(R.id.tv_name, item.userName);
//			helper.setText(R.id.tv_phone, item.mobile);
//			helper.setText(R.id.tv_addr, item.address);
//		}else {
//			helper.setText(R.id.tv_state, "停止接单");
//		}

        helper.setText(R.id.tv_service, item.goodsName);
        helper.setText(R.id.tv_name, item.userName);
        helper.setText(R.id.tv_phone, item.mobile);
        helper.setText(R.id.tv_addr, item.address);
    }
}
