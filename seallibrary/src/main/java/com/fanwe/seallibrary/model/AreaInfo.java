package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/10/29.
 */
public class AreaInfo implements Parcelable {
    public int id;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public AreaInfo() {
    }

    protected AreaInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<AreaInfo> CREATOR = new Parcelable.Creator<AreaInfo>() {
        public AreaInfo createFromParcel(Parcel source) {
            return new AreaInfo(source);
        }

        public AreaInfo[] newArray(int size) {
            return new AreaInfo[size];
        }
    };
}
