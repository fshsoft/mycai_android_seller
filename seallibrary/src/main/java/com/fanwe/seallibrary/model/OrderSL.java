package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ztl on 2015/9/15.
 */
public class OrderSL implements Parcelable {

    public String time;
    public List<OrderScheduleInfo> data;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeTypedList(data);
    }

    public OrderSL() {
    }

    protected OrderSL(Parcel in) {
        this.time = in.readString();
        this.data = in.createTypedArrayList(OrderScheduleInfo.CREATOR);
    }

    public static final Creator<OrderSL> CREATOR = new Creator<OrderSL>() {
        public OrderSL createFromParcel(Parcel source) {
            return new OrderSL(source);
        }

        public OrderSL[] newArray(int size) {
            return new OrderSL[size];
        }
    };
}
