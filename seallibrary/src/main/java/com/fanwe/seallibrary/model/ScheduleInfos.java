package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ztl on 2015/9/14.
 */
public class ScheduleInfos implements Parcelable {

    public int id;
    public String userName;
    public String mobile;
    public String address;
    public String serviceDay;
    public String beginHour;
    public String endHour;
    public String goodsName;
    public String orderStatusStr;
    public String mapPointStr;
    public String serviceType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.mobile);
        dest.writeString(this.address);
        dest.writeString(this.serviceDay);
        dest.writeString(this.beginHour);
        dest.writeString(this.endHour);
        dest.writeString(this.goodsName);
        dest.writeString(this.orderStatusStr);
        dest.writeString(this.mapPointStr);
        dest.writeString(this.serviceType);
    }

    public ScheduleInfos() {
    }

    protected ScheduleInfos(Parcel in) {
        this.id = in.readInt();
        this.userName = in.readString();
        this.mobile = in.readString();
        this.address = in.readString();
        this.serviceDay = in.readString();
        this.beginHour = in.readString();
        this.endHour = in.readString();
        this.goodsName = in.readString();
        this.orderStatusStr = in.readString();
        this.mapPointStr = in.readString();
        this.serviceType = in.readString();
    }

    public static final Parcelable.Creator<ScheduleInfos> CREATOR = new Parcelable.Creator<ScheduleInfos>() {
        public ScheduleInfos createFromParcel(Parcel source) {
            return new ScheduleInfos(source);
        }

        public ScheduleInfos[] newArray(int size) {
            return new ScheduleInfos[size];
        }
    };
}
