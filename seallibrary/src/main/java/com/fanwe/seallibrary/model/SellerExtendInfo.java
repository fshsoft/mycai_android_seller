package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/9/1.
 */
public class SellerExtendInfo implements Parcelable {

    public  int sellerId;
    public  double totalMoney;
    public  double money;
    public  double useMoney;
    public  double goodsAvgPrice;
    public  int orderCount;
    public  sellerCreditRank creditRank;
    public  int commentTotalCount;
    public  int commentGoodCount;
    public  int commentNeutralCount;
    public  int commentBadCount;
    public double withdrawMoney;
    public double frozenMoney;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sellerId);
        dest.writeDouble(this.totalMoney);
        dest.writeDouble(this.money);
        dest.writeDouble(this.useMoney);
        dest.writeDouble(this.goodsAvgPrice);
        dest.writeInt(this.orderCount);
        dest.writeParcelable(this.creditRank, 0);
        dest.writeInt(this.commentTotalCount);
        dest.writeInt(this.commentGoodCount);
        dest.writeInt(this.commentNeutralCount);
        dest.writeInt(this.commentBadCount);
        dest.writeDouble(this.withdrawMoney);
        dest.writeDouble(this.frozenMoney);
    }

    public SellerExtendInfo() {
    }

    protected SellerExtendInfo(Parcel in) {
        this.sellerId = in.readInt();
        this.totalMoney = in.readDouble();
        this.money = in.readDouble();
        this.useMoney = in.readDouble();
        this.goodsAvgPrice = in.readDouble();
        this.orderCount = in.readInt();
        this.creditRank = in.readParcelable(sellerCreditRank.class.getClassLoader());
        this.commentTotalCount = in.readInt();
        this.commentGoodCount = in.readInt();
        this.commentNeutralCount = in.readInt();
        this.commentBadCount = in.readInt();
        this.withdrawMoney = in.readDouble();
        this.frozenMoney = in.readDouble();
    }

    public static final Creator<SellerExtendInfo> CREATOR = new Creator<SellerExtendInfo>() {
        public SellerExtendInfo createFromParcel(Parcel source) {
            return new SellerExtendInfo(source);
        }

        public SellerExtendInfo[] newArray(int size) {
            return new SellerExtendInfo[size];
        }
    };
}
