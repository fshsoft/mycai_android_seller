package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 机构员工信息实体
 *
 * @author atlas
 * @email atlas.tufei@gmail.com
 * @time 2015-5-13 上午11:12:14
 */
public class StaffInfo implements Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = 8114529873373495685L;

    public int id;
    public String name;
    public String mobile;
    public List<String> photos;
    public String avatar;
    public String sexStr;
    public int age;
    public int sex;
    public String logo;
    public String banner;
    public String brief;
    public String address;
    public PointInfo mapPoint;
    public String mapPointStr;
    public String mapPosStr;
    public int status;
    public RegionInfo province;
    public RegionInfo city;
    public RegionInfo area;
    public int sort;
    public SellerInfo seller;
    //	public UserInfo user;
    public UserCollectStaff collect;
    public int birthday;
    public String authentication;
    public String recruitment;
    public String hobbies;
    public String constellation;
    public StaffExtend extend;
    public int sellerType;
    public double totalMoney;
    public double withdrawMoney;
    public double frozenMoney;


    public boolean bSel;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.mobile);
        dest.writeStringList(this.photos);
        dest.writeString(this.avatar);
        dest.writeString(this.sexStr);
        dest.writeInt(this.age);
        dest.writeInt(this.sex);
        dest.writeString(this.logo);
        dest.writeString(this.banner);
        dest.writeString(this.brief);
        dest.writeString(this.address);
        dest.writeSerializable(this.mapPoint);
        dest.writeString(this.mapPointStr);
        dest.writeString(this.mapPosStr);
        dest.writeInt(this.status);
        dest.writeSerializable(this.province);
        dest.writeSerializable(this.city);
        dest.writeSerializable(this.area);
        dest.writeInt(this.sort);
        dest.writeParcelable(this.seller, 0);
        dest.writeParcelable(this.collect, 0);
        dest.writeInt(this.birthday);
        dest.writeString(this.authentication);
        dest.writeString(this.recruitment);
        dest.writeString(this.hobbies);
        dest.writeString(this.constellation);
        dest.writeParcelable(this.extend, 0);
        dest.writeInt(this.sellerType);
        dest.writeDouble(this.totalMoney);
        dest.writeDouble(this.withdrawMoney);
        dest.writeDouble(this.frozenMoney);
        dest.writeByte(bSel ? (byte) 1 : (byte) 0);
    }

    public StaffInfo() {
    }

    protected StaffInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.mobile = in.readString();
        this.photos = in.createStringArrayList();
        this.avatar = in.readString();
        this.sexStr = in.readString();
        this.age = in.readInt();
        this.sex = in.readInt();
        this.logo = in.readString();
        this.banner = in.readString();
        this.brief = in.readString();
        this.address = in.readString();
        this.mapPoint = (PointInfo) in.readSerializable();
        this.mapPointStr = in.readString();
        this.mapPosStr = in.readString();
        this.status = in.readInt();
        this.province = (RegionInfo) in.readSerializable();
        this.city = (RegionInfo) in.readSerializable();
        this.area = (RegionInfo) in.readSerializable();
        this.sort = in.readInt();
        this.seller = in.readParcelable(SellerInfo.class.getClassLoader());
        this.collect = in.readParcelable(UserCollectStaff.class.getClassLoader());
        this.birthday = in.readInt();
        this.authentication = in.readString();
        this.recruitment = in.readString();
        this.hobbies = in.readString();
        this.constellation = in.readString();
        this.extend = in.readParcelable(StaffExtend.class.getClassLoader());
        this.sellerType = in.readInt();
        this.totalMoney = in.readDouble();
        this.withdrawMoney = in.readDouble();
        this.frozenMoney = in.readDouble();
        this.bSel = in.readByte() != 0;
    }

    public static final Creator<StaffInfo> CREATOR = new Creator<StaffInfo>() {
        public StaffInfo createFromParcel(Parcel source) {
            return new StaffInfo(source);
        }

        public StaffInfo[] newArray(int size) {
            return new StaffInfo[size];
        }
    };
}
