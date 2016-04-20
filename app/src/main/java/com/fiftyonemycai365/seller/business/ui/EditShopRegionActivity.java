package com.fiftyonemycai365.seller.business.ui;

import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.RegionLocal;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.LogUtils;
import com.zongyou.library.util.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 店铺所在区修改Activity
 */
public class EditShopRegionActivity extends BaseActivity implements View.OnClickListener, BaseActivity.TitleListener {
    private RelativeLayout mPrivince, mCity, mArea;
    private TextView mProvinceTV, mCityTV, mAreaTV;
    private ImageView mPseleoctimg, mCityImg, mAreaImg;
    private PopupWindow mProvincePopupWindow;
    private ListView mProvinceListView;
    private mListViewAdapter mProvinceAdapter, mCityAdapter, mAreaAdapter;
    private List<RegionLocal> mSourceDatas;
    private RegionLocal mProvinceDatas, mCityDatas, mAreaDatas;
    private ShopInfo mShop;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit_region);
        mShop = getIntent().getParcelableExtra("text");
        initView();
        loadData();
        setPageTag(TagManage.EDIT_SHOP_REGION_ACTIVITY);
    }

    /**
     * 根据id查找区域
     * @param data
     * @param id
     * @return
     */
    private RegionLocal findRegionById(List<RegionLocal> data, String id) {
        if (data != null && data.size() > 0) {
            Iterator<RegionLocal> iterator = data.iterator();
            while (iterator.hasNext()) {
                RegionLocal next = iterator.next();
                if (next.i.equals(id)) {
                    return next;
                }
            }
        }
        return new RegionLocal();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RegionLocal selectedProvice = findRegionById(mSourceDatas, String.valueOf(mShop.provinceId));
            if (null == selectedProvice)
                selectedProvice = new RegionLocal();
            mProvinceTV.setText(selectedProvice.n);
            mProvinceDatas = selectedProvice;
            mProvinceDatas.group = mSourceDatas;
            mCityDatas = findRegionById(mProvinceDatas.child, String.valueOf(mShop.cityId));
            if (null == mCityDatas)
                mCityDatas = new RegionLocal();
            mCityTV.setText(mCityDatas.n);
            mCityDatas.group = mProvinceDatas.child;
            mAreaDatas = findRegionById(mCityDatas.child, String.valueOf(mShop.areaId));
            if (null == mAreaDatas)
                mAreaDatas = new RegionLocal();
            mAreaTV.setText(mAreaDatas.n);
            mAreaDatas.group = mCityDatas.child;
        }
    };

    private String readCity() {
        InputStream in = null;
        AssetManager assetManager = null;
        try {
            assetManager = getAssets();
            in = assetManager.open("city");
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.append(new String(b, 0, n));
            }
            if (null != in)
                in.close();
            return out.toString();
        } catch (IOException e) {
            if (null != in)
                try {
                    in.close();
                } catch (IOException e1) {
                }
            LogUtils.e("asserts error", e);
        }
        if (null != assetManager)
            assetManager.close();
        return null;
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSourceDatas = new GsonBuilder().create().fromJson(readCity(), new TypeToken<List<RegionLocal>>() {
                }.getType());
                if (mSourceDatas != null && mSourceDatas.size() > 0) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    private void initView() {
        setTitleListener(this);
        mProvinceTV = mViewFinder.find(R.id.province_name);
        mCityTV = mViewFinder.find(R.id.city_name);
        mAreaTV = mViewFinder.find(R.id.area_name);
        mPseleoctimg = mViewFinder.find(R.id.province_select_img);
        mCityImg = mViewFinder.find(R.id.city_select_img);
        mAreaImg = mViewFinder.find(R.id.area_select_img);
        mPrivince = (RelativeLayout) findViewById(R.id.province_select);
        mPrivince.setOnClickListener(this);
        mCity = (RelativeLayout) findViewById(R.id.city_select);
        mCity.setOnClickListener(this);
        mArea = (RelativeLayout) findViewById(R.id.area_select);
        mArea.setOnClickListener(this);
        mProvinceDatas = new RegionLocal();
        mCityDatas = new RegionLocal();
        mAreaDatas = new RegionLocal();
        mProvinceAdapter = new mListViewAdapter(mProvinceDatas);
        mCityAdapter = new mListViewAdapter(mCityDatas);
        mAreaAdapter = new mListViewAdapter(mAreaDatas);
        initProvinceListView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.province_select:
                mProvinceListView.setAdapter(mProvinceAdapter);
                mProvinceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mProvinceDatas = (RegionLocal) mProvinceAdapter.getItem(position);
                        mProvinceDatas.group = mSourceDatas;
                        mProvinceTV.setText(mProvinceDatas.n);
                        mProvincePopupWindow.dismiss();
                        mPseleoctimg.setBackgroundResource(R.drawable.icon_pulldown_off);
                        List<RegionLocal> temp = mProvinceDatas.child;
                        if (null != temp && temp.size() > 0) {
                            mCityDatas = temp.get(0);
                            mCityDatas.group = temp;
                            mCityTV.setText(mCityDatas.n);
                        }


                        temp = mCityDatas.child;
                        if (null != temp && temp.size() > 0) {
                            mAreaDatas = temp.get(0);
                            mAreaDatas.group = temp;
                        } else {
                            mAreaDatas = new RegionLocal();
                        }
                        mAreaTV.setText(mAreaDatas.n);
                    }
                });
                mProvinceAdapter.notifyDatas(mProvinceDatas);
                break;
            case R.id.city_select:
                mProvinceListView.setAdapter(mCityAdapter);
                mProvinceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mCityDatas = (RegionLocal) mCityAdapter.getItem(position);
                        mCityDatas.group = mProvinceDatas.child;
                        mCityTV.setText(mCityDatas.n);
                        mProvincePopupWindow.dismiss();
                        mCityImg.setBackgroundResource(R.drawable.icon_pulldown_off);

                        List<RegionLocal> temp = mCityDatas.child;
                        if (null != temp && temp.size() > 0) {
                            mAreaDatas = temp.get(0);
                            mAreaDatas.group = temp;
                        } else {
                            mAreaDatas = new RegionLocal();
                        }
                        mAreaTV.setText(mAreaDatas.n);

                    }
                });
                mCityAdapter.notifyDatas(mCityDatas);
                break;
            case R.id.area_select:
                mProvinceListView.setAdapter(mAreaAdapter);
                mProvinceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mAreaDatas = (RegionLocal) mAreaAdapter.getItem(position);
                        mAreaDatas.group = mCityDatas.child;
                        mAreaTV.setText(mAreaDatas.n);
                        mProvincePopupWindow.dismiss();
                        mAreaImg.setBackgroundResource(R.drawable.icon_pulldown_off);
                    }
                });
                mAreaAdapter.notifyDatas(mAreaDatas);
                break;
        }
        mProvincePopupWindow.setFocusable(true);
        mProvincePopupWindow.showAsDropDown(view);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.label_region);
        TextView right1 = (TextView) right;
        right1.setText(R.string.save);
        right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editShop();
            }
        });
    }

    private void editShop() {
        mShop.provinceId = Integer.valueOf(mProvinceDatas.i);
        mShop.cityId = Integer.valueOf(mCityDatas.i);
        mShop.areaId = Integer.valueOf(mAreaDatas.i);
        HashMap<String, Object> data = new HashMap<>();
        data.put("shopdatas", mShop);
        CustomDialogFragment.getInstance(getSupportFragmentManager(), EditShopAddressDetailActivity.class.getName());
        ApiUtils.post(EditShopRegionActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    ToastUtils.show(getApplicationContext(), getString(R.string.msg_update_scceed));
                    finishActivity();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(EditShopRegionActivity.this, R.string.msg_error_update);
                CustomDialogFragment.dismissDialog();
            }
        });
    }

    class mListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private RegionLocal data;

        public void notifyDatas(RegionLocal data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public mListViewAdapter(RegionLocal data) {
            this.inflater = LayoutInflater.from(EditShopRegionActivity.this);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.group.size();
        }

        @Override
        public Object getItem(int i) {
            return data.group.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            final RegionLocal item = (RegionLocal) getItem(i);
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.pulldown_list_item, null);
                holder.mContent = (TextView) view;
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mContent.setText(item.n);

            return view;
        }

        class ViewHolder {
            TextView mContent;
        }
    }


    private void initProvinceListView() {
        if (mProvincePopupWindow == null) {
            mProvinceListView = (ListView) View.inflate(this, R.layout.pulldown_list, null);
            mProvincePopupWindow = new PopupWindow(mProvinceListView, 400, 600);
            mProvincePopupWindow.setOutsideTouchable(true);
            mProvincePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
    }

}
