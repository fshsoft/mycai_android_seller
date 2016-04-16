package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-03
 * Time: 10:46
 * FIXME
 */
public class WithDraw implements Parcelable {
    public String bankName;
    public String name;
    public String bankNo;
    public String notice;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankName);
        dest.writeString(this.name);
        dest.writeString(this.bankNo);
        dest.writeString(this.notice);
    }

    public WithDraw() {
    }

    protected WithDraw(Parcel in) {
        this.bankName = in.readString();
        this.name = in.readString();
        this.bankNo = in.readString();
        this.notice = in.readString();
    }

    public static final Creator<WithDraw> CREATOR = new Creator<WithDraw>() {
        public WithDraw createFromParcel(Parcel source) {
            return new WithDraw(source);
        }

        public WithDraw[] newArray(int size) {
            return new WithDraw[size];
        }
    };
}
