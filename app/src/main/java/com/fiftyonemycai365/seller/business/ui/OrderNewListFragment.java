package com.fiftyonemycai365.seller.business.ui;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.model.OrderCount;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.BaseFragment;
import com.zongyou.library.app.FragmentUtil;

/**
 * 新订单列表ListFragment
 */
public class OrderNewListFragment extends BaseFragment implements OrderListFragment.OrderRefershListener {

    public static OrderNewListFragment newInstance(Bundle args) {
        OrderNewListFragment fragment = new OrderNewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_order_new, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPageTag(TagManage.ORDER_NEW_LIST_FRAGMENT);
    }

    @Override
    protected void initView() {
        Bundle args = new Bundle(1);
        args.putString(ParamConstants.STATUS, String.valueOf(ParamConstants.ORDER_NEW));
        OrderListFragment orderListFragment = OrderListFragment.newInstance(args);
        orderListFragment.setOrderRefershListener(this);
        FragmentUtil.turnToFragment(getFragmentManager(), R.id.frame_content, orderListFragment, false);
    }


    @Override
    public void orderRefresh(OrderCount orderCount) {
        if(isAdded()) {
            SpannableString spannableString = new SpannableString(getString(R.string.order_count_amount_arg, String.valueOf(orderCount.count), String.valueOf(orderCount.amount)));
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textPrice)), 2, 2 + String.valueOf(orderCount.count).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.textPrice)), spannableString.length() - 1 - String.valueOf(orderCount.amount).length(), spannableString.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setViewText(R.id.order_count_amount, spannableString);
        }
    }
}