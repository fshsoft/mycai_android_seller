package com.yizan.community.business.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.OrderRateInfo;
import com.fanwe.seallibrary.model.result.BaseResult;
import com.yizan.community.business.event.CommentEvent;
import com.yizan.community.business.ui.ViewImageActivity;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.ypy.eventbus.EventBus;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015/11/3.
 */
public class EvalutionManagerAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<OrderRateInfo> list;

    public EvalutionManagerAdapter(List<OrderRateInfo> listData, Context mContext){
        this.context = mContext;
        this.list = listData;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_evalution,null);
            holder.name = (TextView)view.findViewById(R.id.item_user_name);
            holder.replytime = (TextView)view.findViewById(R.id.item_reply_time);
            holder.replycontent_customer = (TextView)view.findViewById(R.id.item_reply_content_customer);
            holder.replycontent_business = (TextView)view.findViewById(R.id.item_reply_content_business);
            holder.imgstar = (ImageView)view.findViewById(R.id.item_img_star);
            holder.reply_oc = (TextView)view.findViewById(R.id.item_reply_oc);
            holder.ll_two = (LinearLayout)view.findViewById(R.id.item_ll2);
            holder.imgItems = new NetworkImageView[8];
            holder.imgItems[0] = (NetworkImageView)view.findViewById(R.id.item_image1);
            holder.imgItems[1] = (NetworkImageView)view.findViewById(R.id.item_image2);
            holder.imgItems[2] = (NetworkImageView)view.findViewById(R.id.item_image3);
            holder.imgItems[3] = (NetworkImageView)view.findViewById(R.id.item_image4);
            holder.imgItems[4] = (NetworkImageView)view.findViewById(R.id.item_image5);
            holder.imgItems[5] = (NetworkImageView)view.findViewById(R.id.item_image6);
            holder.imgItems[6] = (NetworkImageView)view.findViewById(R.id.item_image7);
            holder.imgItems[7] = (NetworkImageView)view.findViewById(R.id.item_image8);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        holder.setData(i);
        return view;
    }
    private class ViewHolder{
        public TextView name,replytime,replycontent_customer,replycontent_business,reply_oc;
        public NetworkImageView []imgItems;
        public LinearLayout ll_two;
        private ImageView imgstar;

        public void setData(final int index){
            final OrderRateInfo item = list.get(index);
            name.setText(item.userName);
            replytime.setText(item.createTime);
            if (item.star == 0){
                imgstar.setImageResource(R.drawable.zerostar);
            }else if (item.star == 1){
                imgstar.setImageResource(R.drawable.onestar);
            }else if(item.star == 2){
                imgstar.setImageResource(R.drawable.twostar);
            }else if (item.star == 3){
                imgstar.setImageResource(R.drawable.threestar);
            }else if (item.star == 4){
                imgstar.setImageResource(R.drawable.fourstar);
            }else if (item.star == 5){
                imgstar.setImageResource(R.drawable.fivestar);
            }
            replycontent_customer.setText(item.content);
            if (!("").equals(item.reply)){
                reply_oc.setVisibility(View.GONE);
                replycontent_business.setVisibility(View.VISIBLE);
                replycontent_business.setText(context.getResources().getString(R.string.ok)+":"+item.reply);
            }else{
                reply_oc.setVisibility(View.VISIBLE);
                replycontent_business.setVisibility(View.GONE);
                reply_oc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initPop(view, list.get(index).id, index);

                    }
                });
            }
            if (item.images.size() < 5){
                ll_two.setVisibility(View.GONE);
            }else{
                ll_two.setVisibility(View.VISIBLE);
            }
            int imageSize = ArraysUtils.isEmpty(item.images)?0:item.images.size();
            for (int i=0; i<8; i++){
                imgItems[i].setTag(String.valueOf(i));
                if (i<imageSize){
                    imgItems[i].setVisibility(View.VISIBLE);
                    imgItems[i].setDefaultImageResId(R.drawable.ic_default);
                    imgItems[i].setImageUrl(item.images.get(i), RequestManager.getImageLoader());
                    imgItems[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ViewImageActivity.class);
                            intent.putStringArrayListExtra(Constants.EXTRA_DATA, (ArrayList<String>) item.images);
                            intent.putExtra(Constants.EXTRA_INDEX,Integer.parseInt(v.getTag().toString()));
                            context.startActivity(intent);
                        }
                    });
                }else{
                    imgItems[i].setVisibility(View.GONE);
                }
            }

        }
    }

    private void initPop(View parent,final int id,final int index){
        View view = LayoutInflater.from(context).inflate(R.layout.reply_pop,null);
        final RelativeLayout cancel = (RelativeLayout)view.findViewById(R.id.rl_pop_cancel);
        final RelativeLayout sure = (RelativeLayout)view.findViewById(R.id.rl_pop_sure);
        RelativeLayout top = (RelativeLayout)view.findViewById(R.id.rl_top);
        LinearLayout bottom = (LinearLayout)view.findViewById(R.id.ll_bottom);
        bottom.setBackgroundResource(R.drawable.text_white_half_bottom_border);
        top.setBackgroundResource(R.drawable.text_red_half_border);
        final EditText reply_content = (EditText)view.findViewById(R.id.reply_content);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!("").equals(reply_content.getText().toString())) {
                    commitReply(reply_content.getText().toString(), id, index);
                    popupWindow.dismiss();
                } else {
                    ToastUtils.show(context, context.getResources().getString(R.string.opinion_edi_text));
                }

            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int height = view.findViewById(R.id.pop_ll).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popupWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void commitReply(final String content,int id,final int index){
        HashMap<String,String> data = new HashMap<>();
        data.put("id", String.valueOf(id));
        data.put("content", content);
        ApiUtils.post(context, URLConstants.SELLER_EVAREPLY, data, BaseResult.class, new Response.Listener<BaseResult>() {
            @Override
            public void onResponse(BaseResult response) {
                if(O2OUtils.checkResponse(context, response)) {
                    list.remove(index);
                    notifyDataSetChanged();
                    ToastUtils.show(context, context.getResources().getString(R.string.msg_reply_succeed));
                    EventBus.getDefault().post(new CommentEvent());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
