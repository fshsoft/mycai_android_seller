package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 订单
 * Created by atlas on 15/9/23.
 * Email:atlas.tufei@gmail.com
 */
public class Order implements Parcelable {
    public int id;
    public String sn;
    public String name;
    public String province;
    public String city;
    public String area;
    public String address;
    public String mobile;

    public String count;
    public String appTime;
    public String payType;

    //1：商品类；2：服务类
    public int orderType;

    public StaffInfo staff;
    public boolean isCanCancel;
    public boolean isCanAccept;
    public boolean isCanFinish;
    public boolean isCanChangeStaff;
    //是否可以开始服务、配送
    public boolean isCanStartService;
    public int distance;

    public PointInfo mapPoint;
    public String orderStatusStr;
    public String totalFee;
    public String createTime;
    public List<OrderGoods> orderGoods;
    public double freight;
    public String buyRemark;
    public int status;
    public String cancelRemark;
    public String sellerName;
    public String buyerFinishTime;
    public double payFee;
    public double goodsFee;
    public double discountFee;

    public Order() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.sn);
        dest.writeString(this.name);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.address);
        dest.writeString(this.mobile);
        dest.writeString(this.count);
        dest.writeString(this.appTime);
        dest.writeString(this.payType);
        dest.writeInt(this.orderType);
        dest.writeParcelable(this.staff, 0);
        dest.writeByte(isCanCancel ? (byte) 1 : (byte) 0);
        dest.writeByte(isCanAccept ? (byte) 1 : (byte) 0);
        dest.writeByte(isCanFinish ? (byte) 1 : (byte) 0);
        dest.writeByte(isCanChangeStaff ? (byte) 1 : (byte) 0);
        dest.writeByte(isCanStartService ? (byte) 1 : (byte) 0);
        dest.writeInt(this.distance);
        dest.writeSerializable(this.mapPoint);
        dest.writeString(this.orderStatusStr);
        dest.writeString(this.totalFee);
        dest.writeString(this.createTime);
        dest.writeTypedList(orderGoods);
        dest.writeDouble(this.freight);
        dest.writeString(this.buyRemark);
        dest.writeInt(this.status);
        dest.writeString(this.cancelRemark);
        dest.writeString(this.sellerName);
        dest.writeString(this.buyerFinishTime);
        dest.writeDouble(this.payFee);
        dest.writeDouble(this.goodsFee);
        dest.writeDouble(this.discountFee);
    }

    protected Order(Parcel in) {
        this.id = in.readInt();
        this.sn = in.readString();
        this.name = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.address = in.readString();
        this.mobile = in.readString();
        this.count = in.readString();
        this.appTime = in.readString();
        this.payType = in.readString();
        this.orderType = in.readInt();
        this.staff = in.readParcelable(StaffInfo.class.getClassLoader());
        this.isCanCancel = in.readByte() != 0;
        this.isCanAccept = in.readByte() != 0;
        this.isCanFinish = in.readByte() != 0;
        this.isCanChangeStaff = in.readByte() != 0;
        this.isCanStartService = in.readByte() != 0;
        this.distance = in.readInt();
        this.mapPoint = (PointInfo) in.readSerializable();
        this.orderStatusStr = in.readString();
        this.totalFee = in.readString();
        this.createTime = in.readString();
        this.orderGoods = in.createTypedArrayList(OrderGoods.CREATOR);
        this.freight = in.readDouble();
        this.buyRemark = in.readString();
        this.status = in.readInt();
        this.cancelRemark = in.readString();
        this.sellerName = in.readString();
        this.buyerFinishTime = in.readString();
        this.payFee = in.readDouble();
        this.goodsFee = in.readDouble();
        this.discountFee = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
