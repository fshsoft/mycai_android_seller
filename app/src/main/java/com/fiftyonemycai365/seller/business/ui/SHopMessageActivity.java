package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ShopInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.tu.crop.CropHandler;
import com.tu.crop.CropHelper;
import com.tu.crop.CropParams;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.DeliveryAdapter;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.OSSUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.fiftyonemycai365.seller.business.widget.MyGridView;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;
import com.zongyou.library.volley.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admin on 2015/10/30.
 * 店铺信息 界面
 */
public class SHopMessageActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener, CropHandler {

    private TextView shop_name, shop_announcement, shop_status, shop_time, shop_tell, shop_service_scope, shop_introduction, mAddressTV, mAddressDetailTV, mRegionTV;
    private MyGridView myGridView;
    private NetworkImageView image;
    private ImageView shop_status_off;
    private List<ShopInfo> shopInfo = new ArrayList<>();
    private boolean isClick = false;
    private ImageSwitcherPopupWindow mPopupWinddow;
    private CropParams mCropParams;
    private DeliveryAdapter deliveryAdapter;
    private List<String> deliveryData = new ArrayList<String>();
    private boolean mIsCashOnDelivery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        setTitleListener(this);
        setViewClickListener(R.id.shop_names, this);
        setViewClickListener(R.id.shop_logos, this);
        setViewClickListener(R.id.shop_logo_img, this);
        setViewClickListener(R.id.shop_announcement, this);
        setViewClickListener(R.id.shop_time, this);
        setViewClickListener(R.id.shop_delivery, this);
        setViewClickListener(R.id.shop_tell, this);
        setViewClickListener(R.id.shop_service_scope, this);
        setViewClickListener(R.id.shop_introduction, this);
        setViewClickListener(R.id.shop_status_img, this);
        setViewClickListener(R.id.shop_service_fee, this);
        setViewClickListener(R.id.shop_delivery_fee, this);
        setViewClickListener(R.id.shop_bank_info, this);
        setViewClickListener(R.id.iv_offline_pay, this);
        initViews();
        OSSUtils.init(SHopMessageActivity.this);
        setPageTag(TagManage.SHOP_MESSAGE_ACTIVITY);
    }

    private void initViews() {
        mAddressTV = mViewFinder.find(R.id.shop_address_text);
        mRegionTV = mViewFinder.find(R.id.shop_area_text);
        mAddressDetailTV = mViewFinder.find(R.id.shop_address_detail_text);
        mViewFinder.find(R.id.shop_address_container).setOnClickListener(this);
        mViewFinder.find(R.id.shop_address_detail_container).setOnClickListener(this);
        mViewFinder.find(R.id.shop_area_container).setOnClickListener(this);
        myGridView = mViewFinder.find(R.id.shop_delivery_grid);
        deliveryAdapter = new DeliveryAdapter(deliveryData, this);
        myGridView.setAdapter(deliveryAdapter);

        shop_name = mViewFinder.find(R.id.shop_name_text);
        image = mViewFinder.find(R.id.shop_logo_img);
        image.setDefaultImageResId(R.drawable.shop_default);
        image.setErrorImageResId(R.drawable.shop_default);
        shop_status_off = mViewFinder.find(R.id.shop_status_img);
        shop_announcement = mViewFinder.find(R.id.shop_announcement_text);
        shop_status = mViewFinder.find(R.id.shop_status_text);
        shop_time = mViewFinder.find(R.id.shop_time_text);
        shop_tell = mViewFinder.find(R.id.shop_tell_text);
        shop_service_scope = mViewFinder.find(R.id.shop_service_scope_text);
        shop_introduction = mViewFinder.find(R.id.shop_introduction_text);

        mCropParams = new CropParams();
        mCropParams.outputX = 200;
        mCropParams.outputY = 200;

    }

    private void initDatas(ShopInfo infos) {
        if (TextUtils.isEmpty(infos.address))
            infos.address = "";
        mAddressTV.setText(getString(R.string.store_address_arg, infos.address));
        if (TextUtils.isEmpty(infos.region))
            infos.region = "";
        mRegionTV.setText(getString(R.string.store_region_arg, infos.region));
        if (TextUtils.isEmpty(infos.addressDetail))
            infos.addressDetail = "";
        mAddressDetailTV.setText(getString(R.string.store_address_detail_arg, infos.addressDetail));
        if (!("").equals(infos.name) && infos.name != null) {
            shop_name.setText(getString(R.string.business_name) + infos.name);
        } else {
            shop_name.setText(getString(R.string.business_name));
        }
        if (!("").equals(infos.img) && infos.img != null) {
            image.setImageUrl(infos.img, RequestManager.getImageLoader());
        }
        if (!("").equals(infos.article) && infos.article != null) {
            shop_announcement.setText(getString(R.string.msg_store_notice) + infos.article);
        } else {
            shop_announcement.setText(getString(R.string.msg_store_notice));
        }
        if (infos.status == 1) {
            shop_status.setText(getString(R.string.msg_Operating_Status_open));
            shop_status_off.setImageResource(R.drawable.btn_on);
            isClick = true;
        } else {
            shop_status.setText(getString(R.string.msg_Operating_Status_pause));
            shop_status_off.setImageResource(R.drawable.btn_off);
            isClick = false;
        }
        if (!("").equals(infos.businessHour.hours) && infos.businessHour.hours != null && infos.businessHour.hours.size() > 0) {
            shop_time.setText(getString(R.string.msg_business_time) + infos.businessHour.hours.get(0) + "-" + infos.businessHour.hours.get(infos.businessHour.hours.size() - 1));
        } else {
            shop_time.setText(getString(R.string.msg_business_time));
        }
        if (!("").equals(infos.tel) && infos.tel != null) {
            shop_tell.setText(getString(R.string.msg_contact_number) + infos.tel);
        } else {
            shop_tell.setText(getString(R.string.msg_contact_number));
        }
        if (!("").equals(infos.serviceRange) && infos.serviceRange != null) {
            shop_service_scope.setText(getString(R.string.msg_server_area) + infos.serviceRange);
        } else {
            shop_service_scope.setText(getString(R.string.msg_server_area));
        }
        if (!("").equals(infos.brief) && infos.brief != null) {
            shop_introduction.setText(infos.brief);
        } else {
            shop_introduction.setText(getString(R.string.msg_not_available));
        }



        mViewFinder.setText(R.id.tv_service_fee, getString(R.string.msg_send_price, String.valueOf(infos.serviceFee)));
        mViewFinder.setText(R.id.tv_delivery_fee, getString(R.string.msg_freight_text, String.valueOf(infos.deliveryFee)));

        mViewFinder.setText(R.id.tv_commission, getString(R.string.title_activity_edit_shop_commission) + infos.deduct + "%");
        ImageView imageView = mViewFinder.find(R.id.iv_offline_pay);
        imageView.setImageResource(infos.isCashOnDelivery == 1?R.drawable.btn_on:R.drawable.btn_off);
        mIsCashOnDelivery = infos.isCashOnDelivery == 1?true:false;
    }

    private void loadData() {
        CustomDialogFragment.getInstance(getSupportFragmentManager(), SHopMessageActivity.class.getName());
        HashMap<String, String> data = new HashMap<>();
        ApiUtils.post(SHopMessageActivity.this, URLConstants.SHOP_INFO, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                if (response.data != null) {
                    shopInfo.clear();
                    shopInfo.add(response.data);
                    assemblyData(response.data);
                    deliveryAdapter.notifyDataSetChanged();
                    initDatas(response.data);

                    //更新用户数据
                   updateUserInfo(response.data);
                }
                CustomDialogFragment.dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(SHopMessageActivity.this, R.string.msg_error);
            }
        });
    }

    /**
     * 更新用户信息
     * @param shop
     */
    private void updateUserInfo(final ShopInfo shop){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserInfo info = PreferenceUtils.getObject(SHopMessageActivity.this, UserInfo.class);
                info.name = shop.name;
                info.avatar = shop.img;
                PreferenceUtils.setObject(SHopMessageActivity.this, info);
            }
        }).start();
    }

    /**
     * 组装数据
     *
     * @param shop
     */
    private void assemblyData(ShopInfo shop) {
        deliveryData.clear();
        for (int i = 0; i < shop.deliveryTime.stimes.size(); i++) {
            try {
                StringBuffer s = new StringBuffer();
                s.append(pad(shop.deliveryTime.stimes.get(i))).append("-").append(pad(shop.deliveryTime.etimes.get(i)));
                deliveryData.add(s.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String pad(String time) {
        String[] s = time.split(":");
        if (s[0].length() == 2) {
            return time;
        } else {
            String str = "0" + s[0] + ":" + s[1];
            return str;
        }

    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(getString(R.string.store_fun_detail));
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        Bundle data = new Bundle();

        if(ArraysUtils.isEmpty(shopInfo)){
            return;
        }
        final ShopInfo shop = shopInfo.get(0);
        data.putParcelable("text", shop);
        intent.putExtras(data);
        switch (view.getId()) {
            case R.id.shop_address_detail_container:
                intent.setClass(SHopMessageActivity.this, EditShopAddressDetailActivity.class);
                break;
            case R.id.shop_address_container:
                intent.setClass(SHopMessageActivity.this, EditShopAddressActivity.class);
                break;
            case R.id.shop_area_container:
                intent.setClass(SHopMessageActivity.this, EditShopRegionActivity.class);
                break;
            case R.id.shop_names:
                intent.setClass(SHopMessageActivity.this, EditShopNameActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("text", shopInfo.get(0));
                intent.putExtras(b);
                startActivityForResult(intent, 1001);
                return;
            case R.id.shop_logos:
            case R.id.shop_logo_img:
                if (null == mPopupWinddow) {
                    mPopupWinddow = new ImageSwitcherPopupWindow(SHopMessageActivity.this, SHopMessageActivity.this, SHopMessageActivity.class.getName());
                }
                mPopupWinddow.show(view);
                return;
            case R.id.photograph:
                mPopupWinddow.dismiss();
                startActivityForResult(CropHelper.buildCaptureIntent(mCropParams.uri), CropHelper.REQUEST_CAMERA);
                return;
            case R.id.photo_album:
                mPopupWinddow.dismiss();
                startActivityForResult(CropHelper.buildGalleryIntent(), CropHelper.REQUEST_GALLERY);
                return;
            case R.id.shop_announcement:
                Intent intent3 = new Intent(SHopMessageActivity.this, EditShopAnnountmentActivity.class);
                Bundle b1 = new Bundle();
                b1.putParcelable("text", shopInfo.get(0));
                intent3.putExtras(b1);
                startActivityForResult(intent3, 1003);
                return;
            case R.id.shop_status_img:
                if (isClick) {
                    shop_status_off.setImageResource(R.drawable.btn_on);
                    shop_status.setText(getString(R.string.msg_Operating_Status_open));
                    isClick = false;
                    editShopStatus(1);
                } else {
                    shop_status_off.setImageResource(R.drawable.btn_off);
                    shop_status.setText(getString(R.string.msg_Operating_Status_pause));
                    isClick = true;
                    editShopStatus(2);
                }
                return;
            case R.id.shop_time:
                Intent intent4 = new Intent(SHopMessageActivity.this, EditShopTimeActivity.class);
                Bundle b6 = new Bundle();
                b6.putParcelable("text", shopInfo.get(0));
                intent4.putExtras(b6);
                startActivityForResult(intent4, 1004);
                return;
            case R.id.shop_delivery:
                Intent intent5 = new Intent(SHopMessageActivity.this, EditShopDeliveryActivity.class);
                Bundle b3 = new Bundle();

                b3.putParcelable("text", shopInfo.get(0));
                intent5.putExtras(b3);
                startActivityForResult(intent5, 1005);
                return;
            case R.id.shop_tell:
                Intent intent6 = new Intent(SHopMessageActivity.this, EditShopTellActivity.class);
                Bundle b2 = new Bundle();
                b2.putParcelable("text", shopInfo.get(0));
                intent6.putExtras(b2);
                startActivityForResult(intent6, 1006);
                return;
            case R.id.shop_service_scope:
                Intent intent19 = new Intent(SHopMessageActivity.this, WebViewActivity.class);
                final UserInfo info = PreferenceUtils.getObject(SHopMessageActivity.this, UserInfo.class);
                String token = PreferenceUtils.getValue(SHopMessageActivity.this, Constants.TOKEN, "");
                String url = URLConstants.SELLERMAP + "?token=" + token + "&userId=" + info.id;
                intent19.putExtra(Constants.EXTRA_URL, url);
                startActivity(intent19);
                return;
            case R.id.shop_introduction:
                Intent intent8 = new Intent(SHopMessageActivity.this, EditShopIntroductionActivity.class);
                Bundle b4 = new Bundle();
                b4.putParcelable("text", shopInfo.get(0));
                intent8.putExtras(b4);
                startActivityForResult(intent8, 1008);
                return;
            case R.id.shop_service_fee:
                Intent intent9 = new Intent(SHopMessageActivity.this, EditShopServiceFeeActivity.class);
                intent9.putExtra(Constants.EXTRA_DATA, shopInfo.get(0));
                startActivityForResult(intent9, 1009);
                return;
            case R.id.shop_delivery_fee:
                Intent intent10 = new Intent(SHopMessageActivity.this, EditShopDeliveryFeeActivity.class);
                intent10.putExtra(Constants.EXTRA_DATA, shopInfo.get(0));
                startActivityForResult(intent10, 1010);
                return;
            case R.id.shop_bank_info:
                Intent intent11 = new Intent(SHopMessageActivity.this, BankMgrActivity.class);
                intent11.putExtra(Constants.EXTRA_DATA, shopInfo.get(0));
                startActivityForResult(intent11, 1011);
                return;
            case R.id.iv_offline_pay:
                ImageView iv = mViewFinder.find(R.id.iv_offline_pay);
                if (mIsCashOnDelivery) {
                    iv.setImageResource(R.drawable.btn_off);
                    editShopOfflinePay(0);
                } else {
                    iv.setImageResource(R.drawable.btn_on);
                    editShopOfflinePay(1);
                }
                mIsCashOnDelivery = !mIsCashOnDelivery;
                return;

        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CropHelper.REQUEST_CROP:
            case CropHelper.REQUEST_CAMERA:
            case CropHelper.REQUEST_GALLERY:
                CropHelper.handleResult(this, requestCode, resultCode, data);
                break;
//            case 1001:
//                shop_name.setText("店铺名称："+data.getStringExtra("text"));
//                break;
//            case 1003:
//                shop_announcement.setText("店铺公告："+data.getStringExtra("text"));
//                break;
//            case 1006:
//                shop_tell.setText("联系电话："+data.getStringExtra("text"));
//            case 1008:
//                shop_introduction.setText(data.getStringExtra("text"));
//                break;
//            case 1004:
//            case 1005:
//                loadData();
//                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        image.setImageURI(uri);
        image.setTag(uri.getPath());
        sendData();
    }

    private void sendData() {
        OSSUtils.save(image.getTag().toString(), new SaveCallback() {
            @Override
            public void onSuccess(String s) {
                editShop(s);
            }

            @Override
            public void onProgress(String s, int i, int i1) {

            }

            @Override
            public void onFailure(String s, OSSException e) {
                ToastUtils.show(SHopMessageActivity.this, getString(R.string.msg_failed_update));
            }
        });
    }

    private void editShopStatus(int status) {
        shopInfo.get(0).status = status;
        HashMap<String, Object> data = new HashMap<>();
        data.put("shopdatas", shopInfo.get(0));
        ApiUtils.post(SHopMessageActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                if (!O2OUtils.checkResponse(getApplicationContext(), response)) {
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(SHopMessageActivity.this, R.string.msg_error);
            }
        });
    }

    private void editShopOfflinePay(int isCashOnDelivery) {
        final int oldStatus = shopInfo.get(0).isCashOnDelivery;
        shopInfo.get(0).isCashOnDelivery = isCashOnDelivery;
        HashMap<String, Object> data = new HashMap<>();
        data.put("shopdatas", shopInfo.get(0));
        ApiUtils.post(SHopMessageActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                if (!O2OUtils.checkResponse(getApplicationContext(), response)) {
                    shopInfo.get(0).isCashOnDelivery = oldStatus;
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shopInfo.get(0).isCashOnDelivery = oldStatus;
                ToastUtils.show(SHopMessageActivity.this, R.string.msg_error);
            }
        });
    }

    private void editShop(String path) {
        shopInfo.get(0).img = path;
        HashMap<String, Object> data = new HashMap<>();
        data.put("shopdatas", shopInfo.get(0));
        ApiUtils.post(SHopMessageActivity.this, URLConstants.SHOP_EDIT, data, ShopResult.class, new Response.Listener<ShopResult>() {
            @Override
            public void onResponse(ShopResult response) {
                if (O2OUtils.checkResponse(getApplicationContext(), response)) {
                    updateUserInfo(response.data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.show(SHopMessageActivity.this, R.string.msg_error);
            }
        });
    }

    @Override
    public void onCropCancel() {

    }

    @Override
    public void onCropFailed(String message) {

    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public Activity getContext() {
        return this;
    }
}
