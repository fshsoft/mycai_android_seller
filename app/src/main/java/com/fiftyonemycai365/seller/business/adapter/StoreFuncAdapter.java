package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;

import com.fiftyonemycai365.seller.business.R;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;

public class StoreFuncAdapter extends CommonAdapter<StoreFuncAdapter.FuncItem> {
    private final String TAG = "StoreFuncAdapter";

    public StoreFuncAdapter(Context context, List<FuncItem> datas) {
        super(context, datas, R.layout.item_store_func);
    }

    @Override
    public void convert(final ViewHolder helper, FuncItem item, int number) {
        try {
            helper.setImageResource(R.id.iv_icon, item.icon);
            helper.setText(R.id.tv_name, item.name);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class FuncItem {
        public String name;
        public int icon;
    }
}