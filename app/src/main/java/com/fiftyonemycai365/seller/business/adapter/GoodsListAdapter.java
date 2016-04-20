package com.fiftyonemycai365.seller.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.model.Goods;
import com.fiftyonemycai365.seller.business.ui.GoodAddActivity;
import com.fiftyonemycai365.seller.business.ui.ServiceAddActivity;
import com.fiftyonemycai365.seller.business.util.ImgUrl;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.fiftyonemycai365.seller.business.widget.dslv.DragSortListView;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.volley.RequestManager;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class GoodsListAdapter extends CommonAdapter<Goods> {
    private boolean mIsEditing = false;
    private DragSortListView mListView;
    private ISelNotify mISelNotify;
    private List<Integer> mSortIds;

    public void setISelNotify(ISelNotify notify) {
        mISelNotify = notify;
    }

    public boolean isEditing() {
        return mIsEditing;
    }

    public void setIsEditing(boolean isEditing) {
        mIsEditing = isEditing;
        notifyDataSetChanged();
        if(isEditing) {
            mSortIds = getNewSortIds();
        }
    }


    public void selAll(boolean check) {
        if (getCount() <= 0) {
            return;
        }
        for (Goods item : mDatas) {
            item.checked = check;
        }
        notifyDataSetChanged();
    }

    public void removeAll(List<Integer> ids){
        if(ArraysUtils.isEmpty(ids) || getCount() == 0){
            return;
        }
        for (int i=0; i<ids.size(); i++){
            for (Goods goods : mDatas){
                if(goods.id == ids.get(i).intValue()){
                    mDatas.remove(goods);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean isAllSel() {
        if (getCount() <= 0) {
            return false;
        }
        for (Goods item : mDatas) {
            if (!item.checked) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getSelIds() {
        List<Integer> ids = new ArrayList<>();
        if (getCount() <= 0) {
            return ids;
        }
        for (Goods item : mDatas) {
            if (item.checked) {
                ids.add(item.id);
            }
        }
        return ids;
    }

    public List<Integer> getNewSortIds() {
        List<Integer> ids = new ArrayList<>();
        if (getCount() <= 0) {
            return ids;
        }
        for (Goods item : mDatas) {
            ids.add(item.id);
        }
        return ids;
    }

    public boolean isNeedUpdateSort(){
        List<Integer> ids = getNewSortIds();
        if(ArraysUtils.isEmpty(ids) || ArraysUtils.isEmpty(mSortIds) || ids.size() != mSortIds.size()){
            return false;
        }
        for(int i=0; i<mSortIds.size(); i++){
            if(ids.get(i).intValue() != mSortIds.get(i).intValue()){
                return true;
            }
        }
        return false;
    }

    public GoodsListAdapter(Context context, List<Goods> datas, DragSortListView listView) {
        super(context, datas, R.layout.item_good_list);
        mListView = listView;
    }

    public void setList(List<Goods> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(int arg0) {//删除指定位置的item
        mDatas.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(Goods item, int arg0) {
        mDatas.add(arg0, item);
        this.notifyDataSetChanged();
    }

    @Override
    public void convert(final ViewHolder helper, final Goods item, final int number) {
        try {
            helper.setViewVisible(R.id.cb_check, isEditing() ? View.VISIBLE : View.GONE);
            helper.setViewVisible(R.id.iv_edit, isEditing() ? View.VISIBLE : View.GONE);
            helper.setViewVisible(R.id.iv_icon, !isEditing() ? View.VISIBLE : View.GONE);
            if(!ArraysUtils.isEmpty(item.imgs)){
                helper.setImageUrl(R.id.iv_icon, ImgUrl.scaleUrl(R.dimen.good_list_icon_width, R.dimen.good_list_icon_hegith, item.imgs.get(0)), RequestManager.getImageLoader(), R.drawable.ic_default);
            }
            helper.setText(R.id.tv_name, item.name);
            helper.setText(R.id.tv_sales, mContext.getString(R.string.msg_sales_volume_text,item.saleCount));
            helper.setText(R.id.tv_price, String.format(mContext.getResources().getString(R.string.msg_before_price), NumFormat.formatPrice(item.price)));
            final CheckBox cb = helper.getView(R.id.cb_check);
            cb.setChecked(item.checked);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item.checked = !item.checked;
                    cb.setChecked(item.checked);
                    if (mISelNotify != null) {
                        mISelNotify.onItemSelect(item);
                    }
                }
            });
            helper.getView(R.id.iv_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    try {
                        if(item.cate.type == Constants.GOODS_TYPE.GOODS){
                            intent = new Intent(mContext, GoodAddActivity.class);
                        }else {
                            intent = new Intent(mContext, ServiceAddActivity.class);
                        }
                        intent.putExtra(Constants.EXTRA_DATA, item);
                        mContext.startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void delItem(final int which) {
//        if (!NetworkUtils.isNetworkAvaiable(mContext)) {
//            ToastUtils.show(mContext, R.string.msg_error_network);
//            return;
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("id", getItem(which).id);
//        CustomDialogFragment.getInstance(((BaseActivity) mContext).getSupportFragmentManager(), this.getClass().getName());
//        ApiUtils.post(mContext, URLConstants.GOODS_OP, params,
//                CateListResult.class, new Response.Listener<CateListResult>() {
//
//                    @Override
//                    public void onResponse(CateListResult response) {
//                        if (O2OUtils.checkResponse(
//                                mContext,
//                                response)) {
//                            mListView.removeItem(which);
//                        }
//                        CustomDialogFragment.dismissDialog();
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        ToastUtils.show(mContext, R.string.msg_error_network);
//                        CustomDialogFragment.dismissDialog();
//                    }
//                });
//
//    }

    public interface ISelNotify {
        void onItemSelect(Goods goods);
    }
}