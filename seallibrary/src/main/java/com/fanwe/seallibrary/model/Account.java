package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-03
 * Time: 10:46
 * FIXME
 */
public class Account implements Parcelable {
    public String createTime;
    public String money;
    public int status;
    public String remark;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createTime);
        dest.writeString(this.money);
        dest.writeInt(this.status);
        dest.writeString(this.remark);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.createTime = in.readString();
        this.money = in.readString();
        this.status = in.readInt();
        this.remark = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
