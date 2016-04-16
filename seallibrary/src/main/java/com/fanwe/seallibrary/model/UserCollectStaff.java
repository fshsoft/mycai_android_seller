package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zzl on 2015/9/10.
 */
public class UserCollectStaff implements Parcelable {

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

    public UserCollectStaff() {
    }

    protected UserCollectStaff(Parcel in) {
        this.id = in.readInt();
        this.createTime = in.readInt();
    }

    public static final Creator<UserCollectStaff> CREATOR = new Creator<UserCollectStaff>() {
        public UserCollectStaff createFromParcel(Parcel source) {
            return new UserCollectStaff(source);
        }

        public UserCollectStaff[] newArray(int size) {
            return new UserCollectStaff[size];
        }
    };
}
