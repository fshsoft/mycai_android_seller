package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/9/1.
 */
public class StatisticsdetailInfo implements Parcelable {

    public Goods goods;
    public float payFee;
    public long serviceEndTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.goods, 0);
        dest.writeFloat(this.payFee);
        dest.writeLong(this.serviceEndTime);
    }

    public StatisticsdetailInfo() {
    }

    protected StatisticsdetailInfo(Parcel in) {
        this.goods = in.readParcelable(Goods.class.getClassLoader());
        this.payFee = in.readFloat();
        this.serviceEndTime = in.readLong();
    }

    public static final Creator<StatisticsdetailInfo> CREATOR = new Creator<StatisticsdetailInfo>() {
        public StatisticsdetailInfo createFromParcel(Parcel source) {
            return new StatisticsdetailInfo(source);
        }

        public StatisticsdetailInfo[] newArray(int size) {
            return new StatisticsdetailInfo[size];
        }
    };
}
