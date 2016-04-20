package com.fiftyonemycai365.seller.business.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.ConfigInfo;
import com.fanwe.seallibrary.model.PaymentInfo;
import com.fanwe.seallibrary.model.request.WeixinPayRequest;
import com.fanwe.seallibrary.model.result.AliPayResult;
import com.fanwe.seallibrary.model.result.PayLogResult;
import com.fanwe.seallibrary.model.result.ShopResult;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.fiftyonemycai365.seller.business.BuildConfig;
import com.fiftyonemycai365.seller.business.R;
import com.fiftyonemycai365.seller.business.adapter.PayWayListAdapter;
import com.fiftyonemycai365.seller.business.util.ApiUtils;
import com.fiftyonemycai365.seller.business.util.NumFormat;
import com.fiftyonemycai365.seller.business.util.O2OUtils;
import com.fiftyonemycai365.seller.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.HashMap;

/**
 * 支付Activity
 */
public class PayWayActivity extends BaseActivity implements View.OnClickListener, BaseActivity.TitleListener {
    private PayWayListAdapter mPayWayListAdapter;
    private int mMoney;
    public final static int REQUEST_CODE = 0x201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_way);
        setTitleListener(this);
        mMoney = getIntent().getIntExtra(Constants.EXTRA_DATA, 0);
        mViewFinder.onClick(R.id.btn_pay, this);
        initView();
        loadData();
        setPageTag(TagManage.PAY_WAY_ACTIVITY);
    }

    private void loadData() {
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return ;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), this.getClass().getName());
        ApiUtils.post(this, URLConstants.SHOP_INFO, null,
                ShopResult.class, new Response.Listener<ShopResult>() {

                    @Override
                    public void onResponse(ShopResult response) {
                        if (O2OUtils.checkResponse(
                                PayWayActivity.this,
                                response)) {
                            mViewFinder.setText(R.id.tv_seller, response.data.name);
                        }
                        CustomDialogFragment.dismissDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(PayWayActivity.this, R.string.msg_error);
                        CustomDialogFragment.dismissDialog();
                    }
                });

    }
    private void initView() {
        if (mMoney <= 0) {
            finishActivity();
            return;
        }
//        if(!TextUtils.isEmpty(mOrderInfo.shopName)) {
//            mViewFinder.setText(R.id.tv_seller, mOrderInfo.shopName);
//        }else{
//            mViewFinder.setText(R.id.tv_seller, mOrderInfo.sellerName);
//        }
        mViewFinder.setText(R.id.tv_total, "¥" + NumFormat.formatPrice(mMoney));
        ConfigInfo initInfo = PreferenceUtils.getObject(this, ConfigInfo.class);
        if (null != initInfo && !ArraysUtils.isEmpty(initInfo.payments)) {
            ListView listView = mViewFinder.find(R.id.lv_list);
            for (PaymentInfo item : initInfo.payments) {
                if (item.code.compareTo("cashOnDelivery") == 0) {
                    initInfo.payments.remove(item);
                    break;
                }
            }
            mPayWayListAdapter = new PayWayListAdapter(this, initInfo.payments);
            listView.setAdapter(mPayWayListAdapter);
            mPayWayListAdapter.selectItem(0);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPayWayListAdapter.selectItem(position);
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if(null == mPayWayListAdapter){
                    ToastUtils.show(this, R.string.msg_select_pay_way);
                    return;
                }
                PaymentInfo paymentInfo = mPayWayListAdapter.getSelItem();
                if (paymentInfo == null) {
                    ToastUtils.show(this, R.string.msg_select_pay_way);
                    return;
                }
                buyGoods(paymentInfo.code);
                break;
        }
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText("充值");
    }

    private void buyGoods(final String payWay) {
        if (TextUtils.isEmpty(payWay)) {
            ToastUtils.show(this, R.string.msg_select_pay_way);
            return;
        }
        if (!NetworkUtils.isNetworkAvaiable(this)) {
            ToastUtils.show(this, R.string.msg_error_network);
            return;
        }
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(PayWayActivity.this, BuildConfig.WX_APP_ID);
        if (Constants.PAY_TYPE_WEICHAT.equals(payWay) && !iwxapi.isWXAppInstalled()) {
            ToastUtils.show(getApplicationContext(), getString(R.string.msg_weixin_un_installed));
            return;
        }
        CustomDialogFragment.getInstance(getSupportFragmentManager(), PayWayActivity.class.getName());
        HashMap<String, String> data = new HashMap<String, String>(2);
        data.put("money", String.valueOf(mMoney));
        data.put("payment", payWay);
        ApiUtils.post(this, URLConstants.SELLER_RECHARGE, data, PayLogResult.class, new Response.Listener<PayLogResult>() {

            @Override
            public void onResponse(PayLogResult response) {
                CustomDialogFragment.dismissDialog();
                if (O2OUtils.checkResponse(PayWayActivity.this, response)) {
                    if (null != response.data && null != response.data.payRequest)
                        if (Constants.PAY_TYPE_WEICHAT.equals(payWay))
                            weichatPay(response.data.payRequest);
                        else {
                            //  支付宝支付
                            try {
                                if (!TextUtils.isEmpty(response.data.payRequest.packages)) {
                                    alipay(PayWayActivity.this, mHandler, response.data.payRequest.packages);
                                }
                            } catch (Exception e) {
                                ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                            }
                        }
                    else {
                        ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CustomDialogFragment.dismissDialog();
                ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
            }
        });

    }

    IWXAPI mIWXAPI;

    private void weichatPay(WeixinPayRequest info) {

        mIWXAPI = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        mIWXAPI.registerApp(BuildConfig.WX_APP_ID);
//        if(mIWXAPI.isWXAppInstalled()){
//
//        }
        registerPayBroadCast();

        PayReq request = new PayReq();
        request.appId = BuildConfig.WX_APP_ID;
        request.partnerId = info.partnerid;
        request.prepayId = info.prepayid;
        request.packageValue = info.packages;
        request.nonceStr = info.noncestr;
        request.timeStamp = info.timestamp;
        request.sign = info.sign;
        mIWXAPI.registerApp(BuildConfig.WX_APP_ID);
        mIWXAPI.sendReq(request);

        // 暂时解决方法
//        mbNeedReloadForWx = true;
    }

    /**
     * 支付宝支付
     *
     * @param activity
     * @param handler
     * @param orderInfo
     */
    public static void alipay(final Activity activity, final Handler handler, final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(orderInfo);

                Message msg = new Message();
                msg.what = AliPayResult.ALIPAY_FLAG;
                msg.obj = new AliPayResult(result);
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    PayBroadCastReceiver mPayBroadCastReceiver;

    private void registerPayBroadCast() {
        if (mPayBroadCastReceiver != null) {
            return;
        }
        // 生成广播处理
        mPayBroadCastReceiver = new PayBroadCastReceiver();
        // 实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_PAY_RESULT);
        // 注册广播
        registerReceiver(mPayBroadCastReceiver, intentFilter);
    }

    private void unregisterPayBroadCast() {
        if (mPayBroadCastReceiver != null) {
            unregisterReceiver(mPayBroadCastReceiver);
        }
    }

    class PayBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final int code = intent.getIntExtra("code", BaseResp.ErrCode.ERR_COMM);
            Log.e("WXPay", "broadcast receiver: " + code + "");
            switch (code) {
                case BaseResp.ErrCode.ERR_OK:
                    ToastUtils.show(PayWayActivity.this, R.string.msg_pay_success);

                    closeSelf();
                    finishActivity();
                    RechargeActivity.activity.finishActivity();
                    UserBillActivity.activity.finishActivity();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);

                    closeSelf();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtils.show(PayWayActivity.this, R.string.msg_pay_error);
                    break;
            }
        }

    }

    private void closeSelf() {
        setResult(Activity.RESULT_OK);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AliPayResult.ALIPAY_FLAG) {
                AliPayResult aliPayResult = (AliPayResult) msg.obj;

                if (AliPayResult.ALI_PAY_RESULT_OK.equals(aliPayResult.getResultStatus())) {
                    ToastUtils.show(PayWayActivity.this, R.string.msg_pay_success);
                    closeSelf();
                    finishActivity();
                    RechargeActivity.activity.finishActivity();
                    UserBillActivity.activity.finishActivity();
                } else {
                    ToastUtils.show(PayWayActivity.this, aliPayResult.getResult());
                }
                closeSelf();
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterPayBroadCast();
        super.onDestroy();

    }
}
