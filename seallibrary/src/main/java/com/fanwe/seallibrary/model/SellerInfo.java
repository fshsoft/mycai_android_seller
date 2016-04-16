package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/9/1.
 */
public class SellerInfo implements Parcelable {

    public  int id;
    public  int type;
    public String name;
    public String mobile;
    public String logo;
    public String bannar;
    public String brief;
    public String address;
    public PointInfo mapPoint;
    public int isCertificate;
    public SellerExtendInfo extend;
    public userCollectSeller collect;
    public  int userId;
    public  RegionInfo province;
    public  RegionInfo city;
    public  RegionInfo area;
    public SellerAuthenticate authenticate;
    public SellerCertificate certificate;
    public int businessStatus;
    public String businessDesc;
    public int status;
    public int sort;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeString(this.logo);
        dest.writeString(this.bannar);
        dest.writeString(this.brief);
        dest.writeString(this.address);
        dest.writeSerializable(this.mapPoint);
        dest.writeInt(this.isCertificate);
        dest.writeParcelable(this.extend, 0);
        dest.writeParcelable(this.collect, 0);
        dest.writeInt(this.userId);
        dest.writeSerializable(this.province);
        dest.writeSerializable(this.city);
        dest.writeSerializable(this.area);
        dest.writeParcelable(this.authenticate, 0);
        dest.writeParcelable(this.certificate, 0);
        dest.writeInt(this.businessStatus);
        dest.writeString(this.businessDesc);
        dest.writeInt(this.status);
        dest.writeInt(this.sort);
    }

    public SellerInfo() {
    }

    protected SellerInfo(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.name = in.readString();
        this.mobile = in.readString();
        this.logo = in.readString();
        this.bannar = in.readString();
        this.brief = in.readString();
        this.address = in.readString();
        this.mapPoint = (PointInfo) in.readSerializable();
        this.isCertificate = in.readInt();
        this.extend = in.readParcelable(SellerExtendInfo.class.getClassLoader());
        this.collect = in.readParcelable(userCollectSeller.class.getClassLoader());
        this.userId = in.readInt();
        this.province = (RegionInfo) in.readSerializable();
        this.city = (RegionInfo) in.readSerializable();
        this.area = (RegionInfo) in.readSerializable();
        this.authenticate = in.readParcelable(SellerAuthenticate.class.getClassLoader());
        this.certificate = in.readParcelable(SellerCertificate.class.getClassLoader());
        this.businessStatus = in.readInt();
        this.businessDesc = in.readString();
        this.status = in.readInt();
        this.sort = in.readInt();
    }

    public static final Creator<SellerInfo> CREATOR = new Creator<SellerInfo>() {
        public SellerInfo createFromParcel(Parcel source) {
            return new SellerInfo(source);
        }

        public SellerInfo[] newArray(int size) {
            return new SellerInfo[size];
        }
    };
}
