package com.yizan.community.business.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.tencent.map.geolocation.TencentLocationUtils;
import com.yizan.community.business.R;
import com.fanwe.seallibrary.common.Constants;
import com.fanwe.seallibrary.common.ParamConstants;
import com.fanwe.seallibrary.common.URLConstants;
import com.fanwe.seallibrary.model.Order;
import com.fanwe.seallibrary.model.OrderGoods;
import com.fanwe.seallibrary.model.PointInfo;
import com.fanwe.seallibrary.model.UserInfo;
import com.fanwe.seallibrary.model.result.OrderResult;
import com.yizan.community.business.service.O2OService;
import com.yizan.community.business.util.ApiUtils;
import com.yizan.community.business.util.NumFormat;
import com.yizan.community.business.util.O2OUtils;
import com.yizan.community.business.util.TagManage;
import com.zongyou.library.app.CustomDialogFragment;
import com.zongyou.library.app.IntentUtils;
import com.zongyou.library.util.ArraysUtils;
import com.zongyou.library.util.DoubleUtil;
import com.zongyou.library.util.NetworkUtils;
import com.zongyou.library.util.ToastUtils;
import com.zongyou.library.util.storage.PreferenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 订单详情Activity
 *
 * @author tu
 * @time 2015-6-12 下午8:20:38
 */
public class OrderDetailsActivity extends BaseActivity implements BaseActivity.TitleListener, View.OnClickListener {
    //0:查询 1:取消订单（商家端）；2：确认订单（商家端）；3：订单完成（配送人员、服务人员）  4:开始服务
    private final int ORDER_OP_LOAD = 0;
    private final int ORDER_OP_CANCEL = 1;
    private final int ORDER_OP_ACCEPT = 2;
    private final int ORDER_OP_FINISH = 3;
    private final int ORDER_OP_START = 4;
    private int orderId = 0;
    private Order mOrderInfo;
    private TextView mDeliveryTimeTV, mCreateTimeTV, mStatusTextView, mUserTextView, mAddressTextView, mDistanceTV, mAppTimeTextView, mDeliveryFeeTV, mTotalTV, mPayTypeTV, mSNTextView, mStaffNameTV, mStaffMobileTV, mRemarkTV, mShopNameTV;
    private ViewGroup mGoodsContainer, mDeliveryContainer, mStaffContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setTitleListener(this);
        orderId = getIntent().getIntExtra(Constants.ORDERID, 0);
        initView();
        loadData(ORDER_OP_LOAD, null);
        setPageTag(TagManage.ORDER_DETAILS_ACTIVITY);
    }

    private void initView() {
        mViewFinder.find(R.id.order_btn2).setOnClickListener(this);
        mViewFinder.find(R.id.order_btn1).setOnClickListener(this);
        mViewFinder.find(R.id.order_user_container).setOnClickListener(this);
        mDeliveryTimeTV = mViewFinder.find(R.id.order_delivery_time);
        mCreateTimeTV = mViewFinder.find(R.id.order_create_time);
        mStatusTextView = mViewFinder.find(R.id.order_status);
        mUserTextView = mViewFinder.find(R.id.order_user);
        mAddressTextView = mViewFinder.find(R.id.order_address);
        mDistanceTV = mViewFinder.find(R.id.order_distance);
        mShopNameTV = mViewFinder.find(R.id.order_shop_name);

        mAppTimeTextView = mViewFinder.find(R.id.order_appointment);

        mGoodsContainer = mViewFinder.find(R.id.order_goods_container);
        mDeliveryContainer = mViewFinder.find(R.id.order_delivery_container);
        mDeliveryFeeTV = mViewFinder.find(R.id.order_delivery_fee);
        mTotalTV = mViewFinder.find(R.id.order_total);
        mPayTypeTV = mViewFinder.find(R.id.order_pay_type);
        mSNTextView = mViewFinder.find(R.id.order_sn);
        mStaffContainer = mViewFinder.find(R.id.order_staff_container);

        mStaffNameTV = mViewFinder.find(R.id.order_staff_name);
        mStaffMobileTV = mViewFinder.find(R.id.order_staff_mobile);
        mRemarkTV = mViewFinder.find(R.id.order_remark);

        mOrderButtons[1] = mViewFinder.find(R.id.order_btn1);
        mOrderButtons[0] = mViewFinder.find(R.id.order_btn2);
        mOrderButtons[0].setOnClickListener(this);
        mOrderButtons[1].setOnClickListener(this);

        mStaffMobileTV.setOnClickListener(this);
        mStaffContainer.setOnClickListener(this);
        mViewFinder.find(R.id.order_navigation).setOnClickListener(this);

    }

    private ArrayList<Integer> orderButtonStrs = new ArrayList<>();
    private TextView[] mOrderButtons = new TextView[2];

    /**
     * 订单详情刷新
     */
    private void setViewData() {
        if (!TextUtils.isEmpty(mOrderInfo.buyerFinishTime)) {
            mDeliveryTimeTV.setText(getString(mOrderInfo.orderType == Constants.ORDER_TYPE_GOODS ? R.string.order_goods_finish_time_arg : R.string.order_service_finish_time_arg, mOrderInfo.buyerFinishTime));
            mDeliveryTimeTV.setVisibility(View.VISIBLE);
        } else
            mDeliveryTimeTV.setVisibility(View.GONE);
        mCreateTimeTV.setText(getString(R.string.order_time_arg, mOrderInfo.createTime));
        mStatusTextView.setText(mOrderInfo.orderStatusStr);
        if (!TextUtils.isEmpty(mOrderInfo.name))
            mUserTextView.setText(O2OUtils.formatTextPrimary(this, getString(R.string.order_user_arg, mOrderInfo.name, mOrderInfo.mobile), mOrderInfo.mobile.length()));
        if (!TextUtils.isEmpty(mOrderInfo.address))
            mAddressTextView.setText(mOrderInfo.address);
        mDistanceTV.setText(getString(R.string.km_arg, String.valueOf(mOrderInfo.distance)));
        initDistance();
        mAppTimeTextView.setText(getString(R.string.appointment_arg, mOrderInfo.appTime));
        mShopNameTV.setText("店铺: "+mOrderInfo.sellerName);

//        LayoutInflater layoutInflater = getLayoutInflater();
//        mGoodsContainer.removeAllViews();
//        for (OrderGoods goods : mOrderInfo.orderGoods) {
//            View view = layoutInflater.inflate(R.layout.item_order_goods, mGoodsContainer, false);
//            if (!TextUtils.isEmpty(goods.goodsNorms))
//                ((TextView) (view.findViewById(R.id.order_item_goods))).setText(getString(R.string.goods_name_arg, goods.goodsName, goods.goodsNorms));
//            else
//                ((TextView) (view.findViewById(R.id.order_item_goods))).setText(goods.goodsName);
//            ((TextView) (view.findViewById(R.id.order_item_amount))).setText(getString(R.string.goods_amount_arg, DoubleUtil.getString(goods.price, 2), String.valueOf(goods.num)));
//            mGoodsContainer.addView(view);
//        }
        initOrderGoods();

        mDeliveryFeeTV.setText(getString(R.string.price_arg, DoubleUtil.getString(mOrderInfo.freight, 2)));
        mTotalTV.setText(getString(R.string.price_arg, mOrderInfo.payFee));
        if (mOrderInfo.discountFee > 0.0f) {
            mViewFinder.setText(R.id.order_coupon_money, "-" + NumFormat.formatPrice(mOrderInfo.discountFee));
            mViewFinder.find(R.id.order_coupon_container).setVisibility(View.VISIBLE);
        } else {
            mViewFinder.find(R.id.order_coupon_container).setVisibility(View.GONE);
        }

        mPayTypeTV.setText(getString(R.string.detail_pay_way, mOrderInfo.payType));
        mSNTextView.setText(mOrderInfo.sn);

        if (null != mOrderInfo.staff) {
            mStaffContainer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mOrderInfo.staff.mobile))
                mStaffMobileTV.setText(mOrderInfo.staff.mobile);
            if (!TextUtils.isEmpty(mOrderInfo.staff.name))
                mStaffNameTV.setText(getString(mOrderInfo.orderType == Constants.ORDER_TYPE_GOODS ? R.string.dilivery_staff_name : R.string.service_staff_name, mOrderInfo.staff.name));
            mViewFinder.find(R.id.order_staff_change).setVisibility(mOrderInfo.isCanChangeStaff ? View.VISIBLE : View.GONE);
            mStaffMobileTV.setVisibility(View.VISIBLE);
        } else {
            if (mOrderInfo.isCanChangeStaff && O2OService.isSeller(PreferenceUtils.getObject(this, UserInfo.class).role)) {
                mStaffNameTV.setText(R.string.title_activity_staff_list);
                mStaffMobileTV.setVisibility(View.GONE);
                mStaffContainer.setVisibility(View.VISIBLE);
            } else
                mStaffContainer.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(mOrderInfo.buyRemark))
            mRemarkTV.setVisibility(View.GONE);
        else {
            mRemarkTV.setText(getString(R.string.remark_arg, mOrderInfo.buyRemark));
            mRemarkTV.setVisibility(View.VISIBLE);
        }

        UserInfo user = PreferenceUtils.getObject(this, UserInfo.class);
        orderButtonStrs.clear();
        if (O2OService.isService(user.role) || O2OService.isStaff(user.role)) {
            if (mOrderInfo.isCanStartService)
                if (mOrderInfo.orderType == Constants.ORDER_TYPE_GOODS)
                    orderButtonStrs.add(R.string.order_start_goods);
                else
                    orderButtonStrs.add(R.string.order_start_service);

            if (mOrderInfo.isCanFinish)
                orderButtonStrs.add(R.string.order_finish);
        }
        if (O2OService.isSeller(user.role)) {
            if (mOrderInfo.isCanCancel)
                orderButtonStrs.add(R.string.order_cancel);
            if (mOrderInfo.isCanAccept)
                orderButtonStrs.add(R.string.order_accept);
        }

        Collections.reverse(orderButtonStrs);
        int index = 0;
        for (int i = mOrderButtons.length - 1, size = mOrderButtons.length; i >= 0; i--) {
            TextView button = mOrderButtons[i];
            if (orderButtonStrs.size() > index) {
                button.setText(orderButtonStrs.get(index));
                button.setTag(orderButtonStrs.get(index));
                button.setVisibility(View.VISIBLE);
            } else
                button.setVisibility(View.GONE);
            index++;
        }

    }

    protected void initDistance() {
        PointInfo pointInfo = PreferenceUtils.getObject(this, PointInfo.class);
        if (pointInfo == null || pointInfo.x <= 0.0f || pointInfo.y <= 0.0f) {
            return;
        }
        if (mOrderInfo == null || mOrderInfo.mapPoint == null || mOrderInfo.mapPoint.x <= 0.0f || mOrderInfo.mapPoint.y <= 0.0f) {
            return;
        }
        try {
            double distance = TencentLocationUtils.distanceBetween(pointInfo.x,
                    pointInfo.y, mOrderInfo.mapPoint.x, mOrderInfo.mapPoint.y);
            if (distance > 1000) {
                mDistanceTV.setText(NumFormat.formatPrice(distance / 1000) + "km");
            } else {
                mDistanceTV.setText((int) distance + "m");
            }

        } catch (Exception e) {
        }
    }

    protected void loadData(final int opType, String remark) {
        if (NetworkUtils.isNetworkAvaiable(OrderDetailsActivity.this)) {

            CustomDialogFragment.getInstance(getSupportFragmentManager(), OrderDetailsActivity.class.getName());
            String url = URLConstants.ORDER_DETAIL;
            HashMap<String, Object> data = new HashMap<String, Object>(2);
            data.put(ParamConstants.ID, String.valueOf(orderId));
            switch (opType) {
                case ORDER_OP_LOAD:
                    url = URLConstants.ORDER_DETAIL;//请求服务器中订单信息的url
                    break;
                case ORDER_OP_ACCEPT:
                case ORDER_OP_CANCEL:
                case ORDER_OP_FINISH:
                    //配送员点击开始服务时，把要更新后的订单状态，以及内容添加到data中去
                case ORDER_OP_START:
                    data.put(ParamConstants.STATUS, opType);
                    data.put(ParamConstants.REMARK, remark);
                    url = URLConstants.ORDER_STATUS;
                    break;
            }
            ApiUtils.post(OrderDetailsActivity.this, url, data, OrderResult.class, new Response.Listener<OrderResult>() {

                @Override
                public void onResponse(OrderResult response) {
                    CustomDialogFragment.dismissDialog();
                    if (O2OUtils.checkResponse(OrderDetailsActivity.this, response)) {
                        mOrderInfo = response.data;
                        if (mOrderInfo != null) {
                            switch (opType) {
                                case ORDER_OP_ACCEPT:
                                    ToastUtils.show(OrderDetailsActivity.this, getString(R.string.msg_receive_order_succeed));
                                    break;
                                case ORDER_OP_CANCEL:
                                    ToastUtils.show(OrderDetailsActivity.this, getString(R.string.msg_cancel_order_succeed));
                                    break;
                                case ORDER_OP_FINISH:
                                    ToastUtils.show(OrderDetailsActivity.this, getString(R.string.msg_finish_order_succeed));
                                    break;
                            }

                            setViewData();
                        }
                    }
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialogFragment.dismissDialog();
                    ToastUtils.show(OrderDetailsActivity.this, R.string.msg_error);
                }

            });

        } else
            ToastUtils.show(OrderDetailsActivity.this, R.string.msg_error_network);
    }

    /**
     * 分配服务、配送人员
     */
    private void allocStaff() {
        Intent intent = new Intent(this, StaffListActivity.class);
        intent.putExtra(Constants.EXTRA_CHOICE_MODE, true);
        intent.putExtra(Constants.EXTRA_DATA, mOrderInfo.orderType);
        startActivityForResult(intent, StaffListActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == StaffListActivity.REQUEST_CODE) {
                setStaff(String.valueOf(data.getIntExtra(Constants.EXTRA_DATA, 0)));
            }
        }
    }

    private void setStaff(String staffId) {
        if (NetworkUtils.isNetworkAvaiable(OrderDetailsActivity.this)) {

            CustomDialogFragment.getInstance(getSupportFragmentManager(), OrderDetailsActivity.class.getName());
            HashMap<String, String> param = new HashMap<String, String>(2);
            param.put(ParamConstants.ID, String.valueOf(orderId));
            param.put(ParamConstants.STAFF_ID, staffId);
            ApiUtils.post(OrderDetailsActivity.this, URLConstants.ORDER_ALLOC_STAFF, param, OrderResult.class, new Response.Listener<OrderResult>() {

                @Override
                public void onResponse(OrderResult response) {
                    CustomDialogFragment.dismissDialog();
                    if (O2OUtils.checkResponse(OrderDetailsActivity.this, response)) {
                        mOrderInfo = response.data;
                        if (mOrderInfo != null) {
                            ToastUtils.show(OrderDetailsActivity.this, getString(R.string.msg_select_staff));
                            setViewData();
                        }
                    }
                }
            }, new ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    CustomDialogFragment.dismissDialog();
                    ToastUtils.show(OrderDetailsActivity.this, R.string.msg_error);
                }

            });

        } else
            ToastUtils.show(OrderDetailsActivity.this, R.string.msg_error_network);
    }

    @Override
    public void setTitle(TextView title, ImageButton left, View right) {
        title.setText(R.string.order_detail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_navigation://导航
                try {
                    PointInfo mapPoint = mOrderInfo.mapPoint;
                    if (null != mapPoint)
                        mapPoint = new PointInfo();
                    mapPoint.address = mOrderInfo.address;
                    O2OUtils.openSysMap(this, mapPoint);
                } catch (Exception e) {
                    ToastUtils.show(this, R.string.err_map_info);
                }
                break;
            case R.id.order_btn1:
            case R.id.order_btn2:
                switch ((Integer) view.getTag()) {
                    case R.string.order_cancel:
                        O2OUtils.initPop(view, new O2OUtils.OKCallback() {
                            @Override
                            public void callback(String text) {
                                loadData(ORDER_OP_CANCEL, text);
                            }
                        });
                        break;
                    case R.string.order_start_service:
                    case R.string.order_start_goods:
                        loadData(ORDER_OP_START, null);
                        break;
                    case R.string.order_accept:
                        loadData(ORDER_OP_ACCEPT, null);
                        break;
                    case R.string.order_finish:
                        loadData(ORDER_OP_FINISH, null);
                        break;
                }

                break;
            case R.id.order_staff_mobile:
                if (null != mOrderInfo.staff)
                    try {
                        IntentUtils.dial(this, mOrderInfo.staff.mobile);
                    } catch (Exception e) {
                    }
                break;
            case R.id.order_user_container:
                try {
                    IntentUtils.dial(this, mOrderInfo.mobile);
                } catch (Exception e) {
                }
                break;
            case R.id.order_staff_container:
                if (mOrderInfo.isCanChangeStaff) {
                    // change
                    allocStaff();
                }
                break;
        }
    }

    public static class GoodItem {
        public int goodId;
        public String name;
        public int num;
        public double price;
        public List<GoodNorms> norms;
    }

    public static class GoodNorms {
        public int normsId;
        public int goodId;
        public String name;
        public double price;
        public int num;
    }


    private List<GoodItem> mOrderGoodList = new ArrayList<>();

    protected GoodItem getItemByGoodId(int goodId) {
        if (mOrderGoodList.size() == 0) {
            return null;
        }
        for (GoodItem item : mOrderGoodList) {
            if (item.goodId == goodId) {
                return item;
            }
        }
        return null;
    }

    protected void reflashData() {
        if (null == mOrderInfo || ArraysUtils.isEmpty(mOrderInfo.orderGoods)) {
            return;
        }
        mOrderGoodList.clear();
        for (OrderGoods item : mOrderInfo.orderGoods) {
            GoodItem good = getItemByGoodId(item.goodsId);
            if (good == null) {
                good = new GoodItem();
                good.goodId = item.goodsId;
                good.name = item.goodsName;
                good.norms = new ArrayList<>();
                good.price = item.price;
                good.num = item.num;
                mOrderGoodList.add(good);
            }
            if (!TextUtils.isEmpty(item.goodsNorms)) {
                GoodNorms goodNorms = new GoodNorms();
                goodNorms.goodId = item.goodsId;
                goodNorms.name = item.goodsNorms;
                goodNorms.price = item.price;
                goodNorms.num = item.num;
                good.norms.add(goodNorms);
            }

        }

    }

    protected void initOrderGoods() {
        LayoutInflater layoutInflater = getLayoutInflater();
        mGoodsContainer.removeAllViews();
        reflashData();
        for (GoodItem goods : mOrderGoodList) {
            View view = layoutInflater.inflate(R.layout.item_order_goods, mGoodsContainer, false);
            LinearLayout layoutNorms = (LinearLayout) view.findViewById(R.id.ll_norms_container);
            layoutNorms.removeAllViews();
            ((TextView) (view.findViewById(R.id.order_item_goods))).setText(goods.name);
            if (ArraysUtils.isEmpty(goods.norms)) {
                ((TextView) (view.findViewById(R.id.order_item_amount))).setText(getString(R.string.goods_amount_arg, DoubleUtil.getString(goods.price, 2), String.valueOf(goods.num)));
            } else {
                double price = 0.0f;
                for (GoodNorms norms : goods.norms) {
                    price += (norms.price * norms.num);
                    View normsView = layoutInflater.inflate(R.layout.item_order_good_norms, null);
                    TextView tv = (TextView) normsView.findViewById(R.id.tv_name);
                    tv.setText(norms.name);
                    tv = (TextView) normsView.findViewById(R.id.tv_price);
                    tv.setText(getString(R.string.goods_amount_arg, DoubleUtil.getString(norms.price, 2), String.valueOf(norms.num)));
                    layoutNorms.addView(normsView);
                }
                ((TextView) (view.findViewById(R.id.order_item_amount))).setText("¥" + DoubleUtil.getString(price, 2));
            }
            mGoodsContainer.addView(view);

        }
    }
}
