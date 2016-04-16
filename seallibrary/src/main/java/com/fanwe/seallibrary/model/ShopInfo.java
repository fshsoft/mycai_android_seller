package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopInfo implements Parcelable {
    //营业额
    public double turnover;
    //订单数量
    public int orderNum;
    //账户余额
    public double balance;
    //店铺名称
    public String name;
    //LOGO
    public String img;
    //公告
    public String article;
    //营业状态（1：营业；2：暂停）
    public int status;
    //营业时间
    public BusinessHourInfo businessHour;
    //配送时间
    public DeliveryTimeInfo deliveryTime;
    //联系电话
    public String tel;
    //服务范围
    public String serviceRange;
    //简介
    public String brief;
    // 配送费
    public double deliveryFee;
    // 起送价
    public double serviceFee;
    public String region;
    public int provinceId;
    public int cityId;
    public int areaId;
    public String address;
    public String addressDetail;
    public String mapPointStr;

    public String contacts; // 负责人

    public int deduct; // 服务平台抽佣比例 0 - 100
    public int isCashOnDelivery; // 是否支持货到付款 0：不支持 1：支持

    public ShopInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.turnover);
        dest.writeInt(this.orderNum);
        dest.writeDouble(this.balance);
        dest.writeString(this.name);
        dest.writeString(this.img);
        dest.writeString(this.article);
        dest.writeInt(this.status);
        dest.writeParcelable(this.businessHour, 0);
        dest.writeParcelable(this.deliveryTime, 0);
        dest.writeString(this.tel);
        dest.writeString(this.serviceRange);
        dest.writeString(this.brief);
        dest.writeDouble(this.deliveryFee);
        dest.writeDouble(this.serviceFee);
        dest.writeString(this.region);
        dest.writeInt(this.provinceId);
        dest.writeInt(this.cityId);
        dest.writeInt(this.areaId);
        dest.writeString(this.address);
        dest.writeString(this.addressDetail);
        dest.writeString(this.mapPointStr);
        dest.writeString(this.contacts);
        dest.writeInt(this.deduct);
        dest.writeInt(this.isCashOnDelivery);
    }

    protected ShopInfo(Parcel in) {
        this.turnover = in.readDouble();
        this.orderNum = in.readInt();
        this.balance = in.readDouble();
        this.name = in.readString();
        this.img = in.readString();
        this.article = in.readString();
        this.status = in.readInt();
        this.businessHour = in.readParcelable(BusinessHourInfo.class.getClassLoader());
        this.deliveryTime = in.readParcelable(DeliveryTimeInfo.class.getClassLoader());
        this.tel = in.readString();
        this.serviceRange = in.readString();
        this.brief = in.readString();
        this.deliveryFee = in.readDouble();
        this.serviceFee = in.readDouble();
        this.region = in.readString();
        this.provinceId = in.readInt();
        this.cityId = in.readInt();
        this.areaId = in.readInt();
        this.address = in.readString();
        this.addressDetail = in.readString();
        this.mapPointStr = in.readString();
        this.contacts = in.readString();
        this.deduct = in.readInt();
        this.isCashOnDelivery = in.readInt();
    }

    public static final Creator<ShopInfo> CREATOR = new Creator<ShopInfo>() {
        public ShopInfo createFromParcel(Parcel source) {
            return new ShopInfo(source);
        }

        public ShopInfo[] newArray(int size) {
            return new ShopInfo[size];
        }
    };
}
