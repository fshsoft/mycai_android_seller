package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by admin on 2015/11/2.
 */
public class DeliveryTimeInfo implements Parcelable {
    public List<String> stimes;
    public List<String> etimes;

    public DeliveryTimeInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.stimes);
        dest.writeStringList(this.etimes);
    }

    protected DeliveryTimeInfo(Parcel in) {
        this.stimes = in.createStringArrayList();
        this.etimes = in.createStringArrayList();
    }

    public static final Creator<DeliveryTimeInfo> CREATOR = new Creator<DeliveryTimeInfo>() {
        public DeliveryTimeInfo createFromParcel(Parcel source) {
            return new DeliveryTimeInfo(source);
        }

        public DeliveryTimeInfo[] newArray(int size) {
            return new DeliveryTimeInfo[size];
        }
    };
}
