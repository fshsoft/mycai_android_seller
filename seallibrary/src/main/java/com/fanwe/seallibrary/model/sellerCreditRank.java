package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/9/1.
 */
public class sellerCreditRank implements Parcelable {

    public  int id;
    public  String name;
    public  String icon;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
    }

    public sellerCreditRank() {
    }

    protected sellerCreditRank(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<sellerCreditRank> CREATOR = new Parcelable.Creator<sellerCreditRank>() {
        public sellerCreditRank createFromParcel(Parcel source) {
            return new sellerCreditRank(source);
        }

        public sellerCreditRank[] newArray(int size) {
            return new sellerCreditRank[size];
        }
    };
}
