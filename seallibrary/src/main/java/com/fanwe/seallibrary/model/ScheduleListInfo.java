package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztl on 2015/9/14.
 */
public class ScheduleListInfo implements Parcelable {
    public List<Long> time;
    public List<OrderSL> list;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.time);
        dest.writeTypedList(list);
    }

    public ScheduleListInfo() {
    }

    protected ScheduleListInfo(Parcel in) {
        this.time = new ArrayList<Long>();
        in.readList(this.time, List.class.getClassLoader());
        this.list = in.createTypedArrayList(OrderSL.CREATOR);
    }

    public static final Creator<ScheduleListInfo> CREATOR = new Creator<ScheduleListInfo>() {
        public ScheduleListInfo createFromParcel(Parcel source) {
            return new ScheduleListInfo(source);
        }

        public ScheduleListInfo[] newArray(int size) {
            return new ScheduleListInfo[size];
        }
    };
}
