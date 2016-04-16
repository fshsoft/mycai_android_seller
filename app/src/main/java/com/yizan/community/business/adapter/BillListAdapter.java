package com.yizan.community.business.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.yizan.community.business.R;
import com.fanwe.seallibrary.model.Account;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;


public class BillListAdapter extends CommonAdapter<Account> {

    public BillListAdapter(Context context, List<Account> datas) {
        super(context, datas, R.layout.item_bill_list);
    }

    public void setList(List<Account> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void convert(final ViewHolder helper, Account item, final int number) {
        try {
            helper.setText(R.id.tv_time, item.createTime);
            helper.setText(R.id.tv_price, item.money);
            helper.setText(R.id.tv_remark, mContext.getResources().getString(R.string.msg_remark_text) + (TextUtils.isEmpty(item.remark)?mContext.getResources().getString(R.string.msg_nothing_text):item.remark));
            TextView tvPrice = helper.getView(R.id.tv_price);
            TextView tvState = helper.getView(R.id.tv_state);
            switch (item.status){
                case 0: //
                    helper.setText(R.id.tv_state, mContext.getResources().getString(R.string.msg_wait_check_text));
                    tvPrice.setTextColor(mContext.getResources().getColor(R.color.textPrice));
                    tvState.setTextColor(mContext.getResources().getColor(R.color.textYellow));
                    break;
                case 1:
                    helper.setText(R.id.tv_state, mContext.getResources().getString(R.string.msg_yet_to_account_text));
                    tvPrice.setTextColor(mContext.getResources().getColor(R.color.textLink));
                    tvState.setTextColor(mContext.getResources().getColor(R.color.textLink));
                    break;
                case 2:
                    helper.setText(R.id.tv_state, mContext.getResources().getString(R.string.msg_yet_refuse_text));
                    tvPrice.setTextColor(mContext.getResources().getColor(R.color.textPrice));
                    tvState.setTextColor(mContext.getResources().getColor(R.color.textYellow));
                    break;
                default:
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}