package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/11/4.
 */
public class BriefUserInfo implements Parcelable {
    public int id;
    public String mobile;
    public String name;
    public String avatar;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.mobile);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
    }

    public BriefUserInfo() {
    }

    protected BriefUserInfo(Parcel in) {
        this.id = in.readInt();
        this.mobile = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<BriefUserInfo> CREATOR = new Parcelable.Creator<BriefUserInfo>() {
        public BriefUserInfo createFromParcel(Parcel source) {
            return new BriefUserInfo(source);
        }

        public BriefUserInfo[] newArray(int size) {
            return new BriefUserInfo[size];
        }
    };
}
