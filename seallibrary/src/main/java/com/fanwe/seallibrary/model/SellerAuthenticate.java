package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zzl on 2015/9/10.
 */
public class SellerAuthenticate implements Parcelable {
    public int sellerId;
    public int status;
    public String realName;
    public String idcardSn;
    public String idcardPositiveImg;
    public String idcardNegativeImg;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sellerId);
        dest.writeInt(this.status);
        dest.writeString(this.realName);
        dest.writeString(this.idcardSn);
        dest.writeString(this.idcardPositiveImg);
        dest.writeString(this.idcardNegativeImg);
    }

    public SellerAuthenticate() {
    }

    protected SellerAuthenticate(Parcel in) {
        this.sellerId = in.readInt();
        this.status = in.readInt();
        this.realName = in.readString();
        this.idcardSn = in.readString();
        this.idcardPositiveImg = in.readString();
        this.idcardNegativeImg = in.readString();
    }

    public static final Parcelable.Creator<SellerAuthenticate> CREATOR = new Parcelable.Creator<SellerAuthenticate>() {
        public SellerAuthenticate createFromParcel(Parcel source) {
            return new SellerAuthenticate(source);
        }

        public SellerAuthenticate[] newArray(int size) {
            return new SellerAuthenticate[size];
        }
    };
}
