package com.yizan.community.business.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.Account;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;


public class FetchBillRecordListAdapter extends CommonAdapter<Account> {

    public FetchBillRecordListAdapter(Context context, List<Account> datas) {
        super(context, datas, R.layout.item_fetch_bill_record_list);
    }

    public void setList(List<Account> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void convert(final ViewHolder helper, Account item, final int number) {
        try {
            if(number >= getCount() - 1){
                helper.setViewVisible(R.id.line_deliver, View.INVISIBLE);
            }else{
                helper.setViewVisible(R.id.line_deliver, View.VISIBLE);
            }
            helper.setText(R.id.tv_time, item.createTime);
            helper.setText(R.id.tv_price, item.money);
            TextView tvState = helper.getView(R.id.tv_state);
            switch (item.status){
                case 0: //
                    helper.setText(R.id.tv_state, mContext.getResources().getString(R.string.msg_wait_check_text));
                    tvState.setTextColor(mContext.getResources().getColor(R.color.textYellow));
                    break;
                case 1:
                    helper.setText(R.id.tv_state, mContext.getResources().getString(R.string.msg_yet_to_account_text));
                    tvState.setTextColor(mContext.getResources().getColor(R.color.textLink));
                    break;
                case 2:
                    helper.setText(R.id.tv_state, mContext.getResources().getString(R.string.msg_yet_refuse_text));
                    tvState.setTextColor(mContext.getResources().getColor(R.color.textPrice));
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}