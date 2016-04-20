package com.fiftyonemycai365.seller.business.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiftyonemycai365.seller.business.R;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by admin on 2015/11/2.
 * 修改配送时间
 */
public class EditShopDeliveryActivity extends BaseActivity implements BaseActivity.TitleListener ,View.OnClickListener{

    private LinearLayout mSecond,mThird;
    private boolean isThirdShow = false;
    private boolean isSecndShow = false;
    private TextView mFirstStartText,mFirstEndText,mSecondStartText,mSecondEndText,mThirdStartText,mThirdEndText;
    private List<String> stimes;
    private List<String> etimes;
    private ShopInfo shop;

    private List<String> newStimes = new ArrayList<String>();
    private List<String> newEtimes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_delivery);
        setTitleListener(this);
        shop = this.getIntent().getExtras().getParcelable("text");
        stimes = shop.deliveryTime.stimes;
        etimes = shop.deliveryTime.etimes;
        setViewClickListener(R.id.ll_add_ll, this);
        setViewClickListener(R.id.ll_delete1_ll, this);
        setViewClickListener(R.id.ll_delete2_ll, this);
        setViewClickListener(R.id.date_time_start_first,this);
        setViewClickListener(R.id.date_time_end_first,this);
        setViewClickListener(R.id.date_time_start_second,this);
        setViewClickListener(R.id.date_time_end_second,this);
        setViewClickListener(R.id.date_time_start_third,this);
        setViewClickListener(R.id.date_time_end_third,this);
        initViews();
        setPageTag(TagManage.EDIT_SHOP_DELIVERY_ACTIVITY);
    }

    private void initViews() {
        mSecond = mViewFinder.find(R.id.ll_second);
        mThird = mViewFinder.find(R.id.ll_third);

        mFirstStartText = mViewFinder.find(R.id.date_time_start_first);
        mFirstEndText = mViewFinder.find(R.id.date_time_end_first);

        mSecondStartText = mViewFinder.find(R.id.date_time_start_second);
        mSecondEndText = mViewFinder.find(R.id.date_time_end_second);

        mThirdStartText = mViewFinder.find(R.id.date_time_start_third);
        mThirdEndText = mViewFinder.find(R.id.date_time_end_third);
        initDatas();
    }

    private void initDatas() {
        if (shop != null && shop.deliveryTime.stimes.size() ==1  && shop.deliveryTime.etimes.size() ==1){
            mFirstStartText.setText(shop.deliveryTime.stimes.get(0));
            mFirstEndText.setText(shop.deliveryTime.etimes.get(0));
        }else if(shop != null && shop.deliveryTime.stimes.size() ==2  && shop.deliveryTime.etimes.size() ==2){
            mFirstStartText.setText(shop.deliveryTime.stimes.get(0));
            mFirstEndText.setText(shop.deliveryTime.etimes.get(0));
            mSecondStartText.setText(shop.deliveryTime.stimes.get(1));
            mSecondEndText.setText(shop.deliveryTime.etimes.get(1));
        }else if (shop != null && shop.deliveryTime.stimes.size() ==3  && shop.deliveryTime.etimes.size() ==3){
            mFirstStartText.setText(shop.deliveryTime.stimes.get(0));
            mFirstEndText.setText(shop.deliveryTime.etimes.get(0));
            mSecondStartText.setText(shop.deliveryTime.stimes.get(1));
            mSecondEndText.setText(shop.deliveryTime.etimes.get(1));
            mThirdStartText.setText(shop.deliveryTime.stimes.get(2));
            mThirdEndText.setText(shop.deliveryTime.etimes.get(2));
        }else {
            mFirstStartText.setText("");
            mFirstEndText.setText("");
            mSecondStartText.setText("");
            mSecondEndText.setText("");
            mThirdStartText.setText("");
            mThirdEndText.setText("");
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.msg_delivery_time));
        ((TextView)right).setText(getString(R.string.save));
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chickString();
            }
        });
    }

    private void chickString(){
        String sf = mFirstStartText.getText().toString();
        String ef = mFirstEndText.getText().toString();
        String ss1 = mSecondStartText.getText().toString();
        String es = mSecondEndText.getText().toString();
        String st = mThirdStartText.getText().toString();
        String et = mThirdEndText.getText().toString();

        if (!sf.equals("") && !ef.equals("") && ss1.equals("") && es.equals("") && st.equals("") && et.equals("")){
            chickTimeOne(sf, ef);
        }else if(!sf.equals("") && !ef.equals("") && !ss1.equals("") && !es.equals("") && st.equals("") && et.equals("")){
            chickTimeTwo(sf,ef,ss1,es);
        }else if (!sf.equals("") && !ef.equals("") && ss1.equals("") && es.equals("") && !st.equals("") && !et.equals("")){
            chickTimeTwo(sf,ef,st,et);
        }else if (!sf.equals("") && !ef.equals("") && !ss1.equals("") && !es.equals("") && !st.equals("") && !et.equals("")){
            chickTime(sf,ef,ss1,es,st,et);
        }
    }

    private void editShop(List<String> st,List<String> et){
        shop.deliveryTime.stimes = st;
        shop.deliveryTime.etimes = et;
        HashMap<String,Object> data = new HashMap<>();
        data.put("shopdatas",shop);
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopDeliveryActivity.class.getName());
        ApiUtils.post(EditShopDeliveryActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    ToastUtils.show(getApplicationContext(),getString(R.string.msg_update_scceed));
                    finishActivity();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(EditShopDeliveryActivity.this, R.string.msg_error_update);
            }
        });
    }

    private void chickTimeTwo(String sf,String ef,String ss1,String es) {
        long fs =  formatTime(sf);
        long fe =  formatTime(ef);
        long ss2 =  formatTime(ss1);
        long se =  formatTime(es);

        if (fs >= fe || ss2 >= se){
            ToastUtils.show(EditShopDeliveryActivity.this,getString(R.string.msg_err_time_select));
        }else if (ss2>fs && ss2 < fe || se > fs && se < fe ||
                  fs >ss2 && fs < se || fe >ss2 && fe < se){
            ToastUtils.show(EditShopDeliveryActivity.this,getString(R.string.msg_err_time_overlap));
        }else{
            newStimes.clear();
            newEtimes.clear();

            newEtimes.add(ef);
            newEtimes.add(es);
            newStimes.add(sf);
            newStimes.add(ss1);

            editShop(newStimes,newEtimes);

//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            finishActivity();
        }
    }

    private void chickTimeOne(String s ,String e) {
        long fs =  formatTime(s);
        long fe =  formatTime(e);

        if (fs >= fe){
            ToastUtils.show(EditShopDeliveryActivity.this,getString(R.string.msg_err_time_select));
        }else{
            newStimes.clear();
            newEtimes.clear();

            newEtimes.add(e);
            newStimes.add(s);

            editShop(newStimes, newEtimes);

//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            finishActivity();
        }
    }

    private void chickTime(String sf,String ef,String ss1,String es,String st,String et) {

        long fs =  formatTime(sf);
        long fe =  formatTime(ef);
        long ss2 =  formatTime(ss1);
        long se =  formatTime(es);
        long ts =  formatTime(st);
        long te =  formatTime(et);

        if (fs >= fe || ss2 >= se || ts >= te){
            ToastUtils.show(EditShopDeliveryActivity.this,getString(R.string.msg_err_time_select));
        }else if (ss2>fs && ss2 < fe || se > fs && se < fe ||
                  ss2> ts && ss2< te || se > ts && se < te ||
                  ts > fs && ts < fe || te > fs && te < fe ||
                  ts >ss2 && ts < se || te >ss2 && te < se ||
                  fs >ss2 && fs < se || fe >ss2 && fe < se ||
                  fs > ts && fs < te || fe > ts && fe < te  )
        {
            ToastUtils.show(EditShopDeliveryActivity.this,getString(R.string.msg_err_time_overlap));
        }else{
            newStimes.clear();
            newEtimes.clear();

            newEtimes.add(ef);
            newEtimes.add(es);
            newEtimes.add(et);
            newStimes.add(sf);
            newStimes.add(ss1);
            newStimes.add(st);

            editShop(newStimes,newEtimes);

//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
//            finishActivity();
        }
    }

    private long formatTime(String str){
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_fomat_3));
        Date date = null;
        try {
             date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    private void showTimePicker(final TextView textView){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(EditShopDeliveryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timePicker.clearFocus();
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                StringBuffer s = new StringBuffer();
                s.append(hour).append(":").append(pad(minute));
                textView.setText(s);
            }
        }, hour, minute, true).show();
    }

    private static String pad(int time){
        if (time >= 10){
            return String.valueOf(time);
        }else{
            return "0"+String.valueOf(time);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.date_time_end_first:
                showTimePicker((TextView)mViewFinder.find(R.id.date_time_end_first));
                break;
            case R.id.date_time_start_first:
                showTimePicker((TextView)mViewFinder.find(R.id.date_time_start_first));
                break;
            case R.id.date_time_end_second:
                showTimePicker((TextView)mViewFinder.find(R.id.date_time_end_second));
                break;
            case R.id.date_time_start_second:
                showTimePicker((TextView)mViewFinder.find(R.id.date_time_start_second));
                break;
            case R.id.date_time_end_third:
                showTimePicker((TextView)mViewFinder.find(R.id.date_time_end_third));
                break;
            case R.id.date_time_start_third:
                showTimePicker((TextView)mViewFinder.find(R.id.date_time_start_third));
                break;
            case R.id.ll_add_ll:
                if (isSecndShow && !isThirdShow){
                    mThird.setVisibility(View.VISIBLE);
                    mViewFinder.find(R.id.third_view).setVisibility(View.VISIBLE);
                    isThirdShow = true;
                }else
                if (!isSecndShow && !isThirdShow){
                    mSecond.setVisibility(View.VISIBLE);
                    mViewFinder.find(R.id.second_view).setVisibility(View.VISIBLE);
                    isSecndShow = true;
                }else if (!isSecndShow && isThirdShow){
                    mSecond.setVisibility(View.VISIBLE);
                    mViewFinder.find(R.id.second_view).setVisibility(View.VISIBLE);
                    isSecndShow = true;
                }
                break;
            case R.id.ll_delete1_ll:
                mSecond.setVisibility(View.GONE);
                mViewFinder.find(R.id.second_view).setVisibility(View.GONE);
                isSecndShow = false;
                break;
            case R.id.ll_delete2_ll:
                mThird.setVisibility(View.GONE);
                mViewFinder.find(R.id.third_view).setVisibility(View.GONE);
                isThirdShow = false;
                break;
        }
    }
}
