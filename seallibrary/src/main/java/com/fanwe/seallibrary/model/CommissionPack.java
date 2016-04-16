package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 佣金包装实体类
 * Created by atlas on 15/9/22.
 * Email:atlas.tufei@gmail.com
 */
public class CommissionPack implements Parcelable {
    public String total;
    public List<Commission> commisssions;

    public CommissionPack() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total);
        dest.writeTypedList(commisssions);
    }

    protected CommissionPack(Parcel in) {
        this.total = in.readString();
        this.commisssions = in.createTypedArrayList(Commission.CREATOR);
    }

    public static final Creator<CommissionPack> CREATOR = new Creator<CommissionPack>() {
        public CommissionPack createFromParcel(Parcel source) {
            return new CommissionPack(source);
        }

        public CommissionPack[] newArray(int size) {
            return new CommissionPack[size];
        }
    };
}
