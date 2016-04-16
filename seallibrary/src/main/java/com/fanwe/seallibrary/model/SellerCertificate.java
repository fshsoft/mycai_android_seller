package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zzl on 2015/9/10.
 */
public class SellerCertificate implements Parcelable {
    public int sellerId;
    public String certificates;
    public int status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sellerId);
        dest.writeString(this.certificates);
        dest.writeInt(this.status);
    }

    public SellerCertificate() {
    }

    protected SellerCertificate(Parcel in) {
        this.sellerId = in.readInt();
        this.certificates = in.readString();
        this.status = in.readInt();
    }

    public static final Parcelable.Creator<SellerCertificate> CREATOR = new Parcelable.Creator<SellerCertificate>() {
        public SellerCertificate createFromParcel(Parcel source) {
            return new SellerCertificate(source);
        }

        public SellerCertificate[] newArray(int size) {
            return new SellerCertificate[size];
        }
    };
}
