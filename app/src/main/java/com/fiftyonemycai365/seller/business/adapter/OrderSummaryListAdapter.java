package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.fanwe.seallibrary.model.result.OrderSummary;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

/**
 * 订单列表ListAdapter
 */
public class OrderSummaryListAdapter extends CommonAdapter<OrderSummary> {

    private Context context;
    private Resources mResources;

    public OrderSummaryListAdapter(Context context, List<OrderSummary> list) {
        super(context, list, R.layout.item_order);
        mResources = context.getResources();
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, final OrderSummary item, final int position) {
        CardView cardView = helper.getView(R.id.order_cardview);
        if (item.isFinished)
            cardView.setCardBackgroundColor(mResources.getColor(R.color.primary));
        else
            cardView.setCardBackgroundColor(mResources.getColor(R.color.grayDark));
        helper.setText(R.id.order_time, context.getString(R.string.order_time_arg, item.createTime));
        helper.setText(R.id.order_status, item.orderStatusStr);
        helper.setText(R.id.order_goods_name, item.shopName);
        helper.setText(R.id.order_status, item.orderStatusStr);
        helper.setText(R.id.order_status_pay, item.payStatusStr);
        if(TextUtils.isEmpty(item.totalFee)){
            item.totalFee = "0";
        }
        String price = NumFormat.formatPrice(item.payFee);
        SpannableString spannableString = new SpannableString(mContext.getString(R.string.order_amount_arg, price));
        spannableString.setSpan(new ForegroundColorSpan(mResources.getColor(R.color.textPrice)), spannableString.length() - 7 - price.length(), spannableString.length() - 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.order_amount, spannableString);
        helper.setText(R.id.order_no, mContext.getString(R.string.order_no_arg,String.valueOf(item.id)));
    }
}
