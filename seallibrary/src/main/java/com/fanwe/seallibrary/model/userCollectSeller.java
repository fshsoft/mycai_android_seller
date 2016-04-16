package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/9/1.
 */
public class userCollectSeller implements Parcelable {

    public int id;
    public int createTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.createTime);
    }

    public userCollectSeller() {
    }

    protected userCollectSeller(Parcel in) {
        this.id = in.readInt();
        this.createTime = in.readInt();
    }

    public static final Creator<userCollectSeller> CREATOR = new Creator<userCollectSeller>() {
        public userCollectSeller createFromParcel(Parcel source) {
            return new userCollectSeller(source);
        }

        public userCollectSeller[] newArray(int size) {
            return new userCollectSeller[size];
        }
    };
}
