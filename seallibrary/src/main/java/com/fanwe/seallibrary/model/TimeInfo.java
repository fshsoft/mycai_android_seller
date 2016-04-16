package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zzl on 2015/9/14.
 */
public class TimeInfo implements Parcelable {

    public String date;
    public String week;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.week);
    }

    public TimeInfo() {
    }

    protected TimeInfo(Parcel in) {
        this.date = in.readString();
        this.week = in.readString();
    }

    public static final Parcelable.Creator<TimeInfo> CREATOR = new Parcelable.Creator<TimeInfo>() {
        public TimeInfo createFromParcel(Parcel source) {
            return new TimeInfo(source);
        }

        public TimeInfo[] newArray(int size) {
            return new TimeInfo[size];
        }
    };
}
