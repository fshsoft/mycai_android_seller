package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 佣金
 * Created by atlas on 15/9/22.
 * Email:atlas.tufei@gmail.com
 */
public class Commission implements Parcelable {
    public String money;
    public String createTime;
    public String content;
    public String orderSn;

    public Commission() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.money);
        dest.writeString(this.createTime);
        dest.writeString(this.content);
        dest.writeString(this.orderSn);
    }

    protected Commission(Parcel in) {
        this.money = in.readString();
        this.createTime = in.readString();
        this.content = in.readString();
        this.orderSn = in.readString();
    }

    public static final Creator<Commission> CREATOR = new Creator<Commission>() {
        public Commission createFromParcel(Parcel source) {
            return new Commission(source);
        }

        public Commission[] newArray(int size) {
            return new Commission[size];
        }
    };
}
