package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by admin on 2015/8/13.
 */
public class GoodsType implements Parcelable {

    public int id;
    public String name;
    public int count;
    public List<OrderGoods> goods;
    public String search;
    public int chickstatus;

    public GoodsType() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.count);
        dest.writeTypedList(goods);
        dest.writeString(this.search);
        dest.writeInt(this.chickstatus);
    }

    protected GoodsType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.count = in.readInt();
        this.goods = in.createTypedArrayList(OrderGoods.CREATOR);
        this.search = in.readString();
        this.chickstatus = in.readInt();
    }

    public static final Creator<GoodsType> CREATOR = new Creator<GoodsType>() {
        public GoodsType createFromParcel(Parcel source) {
            return new GoodsType(source);
        }

        public GoodsType[] newArray(int size) {
            return new GoodsType[size];
        }
    };
}
