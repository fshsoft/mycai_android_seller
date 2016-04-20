package com.fiftyonemycai365.seller.business.adapter;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.model.Norms;
import com.fiftyonemycai365.seller.business.ui.GoodAddActivity;
import com.zongyou.library.widget.adapter.CommonAdapter;
import com.zongyou.library.widget.adapter.ViewHolder;

import java.util.List;


public class NormsListAdapter extends CommonAdapter<Norms> {

    public NormsListAdapter(GoodAddActivity context, List<Norms> datas) {
        super(context, datas, R.layout.item_norms_list);

    }

    public void setList(List<Norms> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void addNew(){
        Norms norms = new Norms();
        mDatas.add(norms);
        notifyDataSetChanged();
    }

    public void remove(int position){
        if(position >= getCount()){
            return;
        }
        ((GoodAddActivity)mContext).reflashCurrNorms();
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void convert(final ViewHolder helper, Norms item, final int number) {
        try {

            EditText et = helper.getView(R.id.et_price);
            et.setTag(String.valueOf(item.id));
            et.setFilters(new InputFilter[]{lengthfilter});
            et.setText(String.valueOf(item.price));

            et = helper.getView(R.id.et_model);
            if(!TextUtils.isEmpty(item.name)){
                et.setText(item.name);
            }else{
                et.setText("");
            }
            et = helper.getView(R.id.et_stock);
            et.setText(String.valueOf(item.stock));

            helper.getView(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(number);
                    ((GoodAddActivity)mContext).reflashNormsBlock();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     *  设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String spilt = "\\.";
            String[] splitArray = dValue.split(spilt);
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - 2;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };
}