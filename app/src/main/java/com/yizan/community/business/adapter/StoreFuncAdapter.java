package com.yizan.community.business.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.yizan.community.business.R;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
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