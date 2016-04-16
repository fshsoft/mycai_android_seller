package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by admin on 2015/11/2.
 */
public class BusinessHourInfo implements Parcelable {
    public List<String> weeks;
    public List<String> hours;

    public BusinessHourInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.weeks);
        dest.writeStringList(this.hours);
    }

    protected BusinessHourInfo(Parcel in) {
        this.weeks = in.createStringArrayList();
        this.hours = in.createStringArrayList();
    }

    public static final Creator<BusinessHourInfo> CREATOR = new Creator<BusinessHourInfo>() {
        public BusinessHourInfo createFromParcel(Parcel source) {
            return new BusinessHourInfo(source);
        }

        public BusinessHourInfo[] newArray(int size) {
            return new BusinessHourInfo[size];
        }
    };
}
