package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User: ldh (394380623@qq.com)
 * Date: 2015-11-03
 * Time: 10:46
 * FIXME
 */
public class BankInfo implements Parcelable {
    public int id;
    public String bank;
    public String name;
    public String bankNo;
    public String mobile;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.bank);
        dest.writeString(this.name);
        dest.writeString(this.bankNo);
        dest.writeString(this.mobile);
    }

    public BankInfo() {
    }

    protected BankInfo(Parcel in) {
        this.id = in.readInt();
        this.bank = in.readString();
        this.name = in.readString();
        this.bankNo = in.readString();
        this.mobile = in.readString();
    }

    public static final Creator<BankInfo> CREATOR = new Creator<BankInfo>() {
        public BankInfo createFromParcel(Parcel source) {
            return new BankInfo(source);
        }

        public BankInfo[] newArray(int size) {
            return new BankInfo[size];
        }
    };
}
