package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/9/1.
 */
public class Region implements Parcelable {
    public int id;
    public String  name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    public Region() {
    }

    protected Region(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Region> CREATOR = new Parcelable.Creator<Region>() {
        public Region createFromParcel(Parcel source) {
            return new Region(source);
        }

        public Region[] newArray(int size) {
            return new Region[size];
        }
    };
}
