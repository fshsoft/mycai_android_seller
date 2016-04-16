package com.yizan.community.business.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yizan.community.business.R;
import com.yizan.community.business.adapter.HourAdapter;
import com.yizan.community.business.adapter.NewWeekAdapter;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.HourInfo;
import com.fanwe.seallibrary.model.NewWeekInfo;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015/11/3.
 * 修改营业时间
 */
public class EditShopTimeActivity extends BaseActivity implements BaseActivity.TitleListener {

    private GridView weekGrid,timeGrid;
    private NewWeekAdapter weekAdapter;
    private HourAdapter hourAdapter;
    private ShopInfo shopInfo;
    private List<NewWeekInfo> newWeekInfoList = new ArrayList<>();
    private List<HourInfo> hourInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shop_time);
        setTitleListener(this);
        shopInfo = this.getIntent().getExtras().getParcelable("text");
        initViews();
        setPageTag(TagManage.EDIT_SHOP_TIME_ACTIVITY);
    }

    private void initViews() {
        weekGrid = mViewFinder.find(R.id.week_grid);
        weekAdapter = new NewWeekAdapter(newWeekInfoList,this);
        weekGrid.setAdapter(weekAdapter);
        weekGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (newWeekInfoList.get(i).isChick == 1) {
                    newWeekInfoList.get(i).isChick = 0;
                } else {
                    newWeekInfoList.get(i).isChick = 1;
                }
                weekAdapter.notifyDataSetChanged();
            }
        });

        timeGrid = mViewFinder.find(R.id.hour_grid);
        hourAdapter = new HourAdapter(hourInfoList,this);
        timeGrid.setAdapter(hourAdapter);
        timeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (hourInfoList.get(i).isChick == 1) {
                    hourInfoList.get(i).isChick = 0;
                } else {
                    hourInfoList.get(i).isChick = 1;
                }
                hourAdapter.notifyDataSetChanged();
            }
        });
        initWeekData();
        initHourData();

    }

    private void initWeekData(){
        String[] weeks = {"周一","周二","周三","周四","周五","周六","周日"};
        for (int i=0;i<weeks.length;i++){
            NewWeekInfo weekInfo = new NewWeekInfo();
            weekInfo.name = weeks[i];
            if(shopInfo.businessHour.weeks !=null){
                weekInfo.isChick = shopInfo.businessHour.weeks.contains(String.valueOf(i+1))?1:0;
            }
            newWeekInfoList.add(weekInfo);
        }
        weekAdapter.notifyDataSetChanged();
    }

    private void initHourData(){
        String[] hours = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00"
                         ,"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00"
                         ,"16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};

        for (int i=0;i<hours.length;i++){
            HourInfo hourInfo = new HourInfo();
            hourInfo.name = hours[i];
            if (shopInfo.businessHour.hours != null){
                hourInfo.isChick = shopInfo.businessHour.hours.contains(hours[i])?1:0;
            }
           hourInfoList.add(hourInfo);
        }
        hourAdapter.notifyDataSetChanged();
    }
    private String chickStr(String str){
        String s = "";
        if (str.equals("周一")){
            s = "1";
        }else if(str.equals("周二")) {
            s = "2";
        }else if(str.equals("周三")) {
            s = "3";
        }else if(str.equals("周四")) {
            s = "4";
        }else if(str.equals("周五")) {
            s = "5";
        }else if(str.equals("周六")) {
            s = "6";
        }else if(str.equals("周日")) {
            s = "7";
        }
        return s;
    }
    private List<String> assemblyWeekData(){
        List<String> weekAssembly = new ArrayList<>();
        for (int i=0;i<newWeekInfoList.size();i++){
            if (newWeekInfoList.get(i).isChick == 1){
                weekAssembly.add(chickStr(newWeekInfoList.get(i).name));
            }
        }
        return weekAssembly;
    }

    private List<String> assemblyHourData(){
        List<String> hourAssembly = new ArrayList<>();
        for (int i=0;i<hourInfoList.size();i++){
            if (hourInfoList.get(i).isChick == 1){
                hourAssembly.add(hourInfoList.get(i).name);
            }
        }
        return hourAssembly;
    }

    private void editShop(){
        shopInfo.businessHour.weeks = assemblyWeekData();
        shopInfo.businessHour.hours = assemblyHourData();
        HashMap<String,Object> data = new HashMap<>();
        data.put("shopdatas",shopInfo);
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopTimeActivity.class.getName());
        ApiUtils.post(EditShopTimeActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    ToastUtils.show(getApplicationContext(),"更新成功");
                    finishActivity();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(EditShopTimeActivity.this, R.string.msg_error_update);
            }
        });
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("营业时间");
        ((TextView)right).setText("保存");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editShop();
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finishActivity();
            }
        });
    }
}
