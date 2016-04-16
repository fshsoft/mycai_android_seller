package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/8/12.
 */
public class OrderGoods implements Parcelable {
    public int id;
    public int sellerId;
    public String  name;
    public int  count;
    public double  price;
    public double  marketPrice;
    public String goodsName;
    //型号
    public String goodsNorms;
    public int num;
    public int goodsId;

    public OrderGoods() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.sellerId);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeDouble(this.price);
        dest.writeDouble(this.marketPrice);
        dest.writeString(this.goodsName);
        dest.writeString(this.goodsNorms);
        dest.writeInt(this.num);
        dest.writeInt(this.goodsId);
    }

    protected OrderGoods(Parcel in) {
        this.id = in.readInt();
        this.sellerId = in.readInt();
        this.name = in.readString();
        this.count = in.readInt();
        this.price = in.readDouble();
        this.marketPrice = in.readDouble();
        this.goodsName = in.readString();
        this.goodsNorms = in.readString();
        this.num = in.readInt();
        this.goodsId = in.readInt();
    }

    public static final Creator<OrderGoods> CREATOR = new Creator<OrderGoods>() {
        public OrderGoods createFromParcel(Parcel source) {
            return new OrderGoods(source);
        }

        public OrderGoods[] newArray(int size) {
            return new OrderGoods[size];
        }
    };
}
