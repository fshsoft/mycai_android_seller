package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/11/4.
 */
public class NewWeekInfo implements Parcelable {
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

    public NewWeekInfo() {
    }

    protected NewWeekInfo(Parcel in) {
        this.name = in.readString();
        this.isChick = in.readInt();
    }

    public static final Creator<NewWeekInfo> CREATOR = new Creator<NewWeekInfo>() {
        public NewWeekInfo createFromParcel(Parcel source) {
            return new NewWeekInfo(source);
        }

        public NewWeekInfo[] newArray(int size) {
            return new NewWeekInfo[size];
        }
    };
}
