package com.fiftyonemycai365.seller.business.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.GoodsCate;
import com.fanwe.seallibrary.model.result.CateListResult;
import com.fiftyonemycai365.seller.business.ui.BaseActivity;
import com.fiftyonemycai365.seller.business.ui.CateAddActivity;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.widget.dslv.DragSortListView;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CateListAdapter extends CommonAdapter<GoodsCate> {
    private boolean mIsEditing = false;
    private DragSortListView mListView;
    private int mType = Constants.GOODS_TYPE.GOODS;

    public boolean isEditing() {
        return mIsEditing;
    }

    public void setIsEditing(boolean isEditing) {
        mIsEditing = isEditing;
        notifyDataSetChanged();
    }


    public CateListAdapter(BaseActivity context, List<GoodsCate> datas, DragSortListView listView, int type) {
        super(context, datas, R.layout.item_cate_list);
        mListView = listView;
        mType = type;
    }

    public void setList(List<GoodsCate> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int arg0) {//删除指定位置的item
        mDatas.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(GoodsCate item, int arg0) {
        mDatas.add(arg0, item);
        this.notifyDataSetChanged();
    }

    public List<Integer> getNewSortIds() {
        List<Integer> list = new ArrayList<>();
        if(getCount() <= 0){
            return list;
        }
        for(GoodsCate item: mDatas){
            list.add(item.id);
        }
        return list;
    }

    @Override
    public void convert(final ViewHolder helper, final GoodsCate item, final int number) {
        try {
            helper.setViewVisible(R.id.iv_del, isEditing() ? View.VISIBLE : View.GONE);
            helper.setViewVisible(R.id.iv_edit, isEditing() ? View.VISIBLE : View.GONE);
            helper.setViewVisible(R.id.drag_handle, isEditing() ? View.VISIBLE : View.GONE);
            helper.setViewVisible(R.id.iv_arrow, !isEditing() ? View.VISIBLE : View.GONE);
            helper.setText(R.id.tv_name, item.name);
            helper.setText(R.id.tv_nums, String.format(mContext.getResources().getString(R.string.msg_goods_number_text),item.goodsNum));

            helper.getView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    if(item.goodsNum > 0) {
                        builder
                                .setTitle(mContext.getResources().getString(R.string.msg_delete_classify_text))
                                .setMessage(mContext.getResources().getString(R.string.msg_delete_classify_hint))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                                        delItem(number);
                                    }
                                })
                                .show();
                    }else{

                        builder
                                .setTitle(mContext.getResources().getString(R.string.msg_delete_classify_text))
                                .setMessage(mContext.getResources().getString(R.string.msg_is_delete_hint))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        delItem(number);
                                    }
                                })
                                .setNegativeButton(mContext.getResources().getString(R.string.cancel), null)
                                .show();
                    }
                }
            });

            helper.getView(R.id.iv_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CateAddActivity.class);
                    intent.putExtra(Constants.EXTRA_DATA, item);
                    intent.putExtra(Constants.EXTRA_TYPE, mType);
                    ((BaseActivity)mContext).startActivityForResult(intent, 0x103);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void delItem(final int which) {
        if (!NetworkUtils.isNetworkAvaiable(mContext)) {
            ToastUtils.show(mContext, R.string.msg_error_network);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", getItem(which).id);
        CustomDialogFragment.getInstance(((BaseActivity) mContext).getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(mContext, URLConstants.GOODS_CATES_DEL, params,
                CateListResult.class, new Response.Listener<CateListResult>() {

                    @Override
                    public void onResponse(CateListResult response) {
                        if (O2OUtils.checkResponse(
                                mContext,
                                response)) {
                            mListView.removeItem(which);
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(mContext, R.string.msg_error_del);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }

}