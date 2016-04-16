package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/11/4.
 */
public class HourInfo implements Parcelable {
    public String name;
    public int isChick;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.isChick);
    }

    public HourInfo() {
    }

    protected HourInfo(Parcel in) {
        this.name = in.readString();
        this.isChick = in.readInt();
    }

    public static final Parcelable.Creator<HourInfo> CREATOR = new Parcelable.Creator<HourInfo>() {
        public HourInfo createFromParcel(Parcel source) {
            return new HourInfo(source);
        }

        public HourInfo[] newArray(int size) {
            return new HourInfo[size];
        }
    };
}
