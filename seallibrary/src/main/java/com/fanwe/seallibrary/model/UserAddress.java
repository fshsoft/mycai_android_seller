package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author haojiahe
 * 
 * @time 2015-3-24下午2:50:09
 * 
 */
public class UserAddress implements Parcelable {

    public int id; //编号
    public String address; //地址
    public String name;//收货人
    public String mobile;//收货电话
    public boolean isDefault; //是否为默认地址
    public ProvinceInfo province;//省
    public CityInfo city;//城市
    public AreaInfo area;//区
    public String mapPoint; //地图坐标
    public String detailAddress;//详情地址
    public String doorplate;//门牌号

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeByte(isDefault ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.province, 0);
        dest.writeParcelable(this.city, 0);
        dest.writeParcelable(this.area, 0);
        dest.writeString(this.mapPoint);
        dest.writeString(this.detailAddress);
        dest.writeString(this.doorplate);
    }

    public UserAddress() {
    }

    protected UserAddress(Parcel in) {
        this.id = in.readInt();
        this.address = in.readString();
        this.name = in.readString();
        this.mobile = in.readString();
        this.isDefault = in.readByte() != 0;
        this.province = in.readParcelable(ProvinceInfo.class.getClassLoader());
        this.city = in.readParcelable(CityInfo.class.getClassLoader());
        this.area = in.readParcelable(AreaInfo.class.getClassLoader());
        this.mapPoint = in.readString();
        this.detailAddress = in.readString();
        this.doorplate = in.readString();
    }

    public static final Parcelable.Creator<UserAddress> CREATOR = new Parcelable.Creator<UserAddress>() {
        public UserAddress createFromParcel(Parcel source) {
            return new UserAddress(source);
        }

        public UserAddress[] newArray(int size) {
            return new UserAddress[size];
        }
    };
}
