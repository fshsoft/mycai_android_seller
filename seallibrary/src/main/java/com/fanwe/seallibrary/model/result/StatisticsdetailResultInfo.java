package com.fanwe.seallibrary.model.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.fanwe.seallibrary.model.StatisticsdetailInfo;

import java.util.List;

/**
 * Created by admin on 2015/9/1.
 */
public class StatisticsdetailResultInfo extends BaseResult implements Parcelable {

    public List<StatisticsdetailInfo> data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }

    public StatisticsdetailResultInfo() {
    }

    protected StatisticsdetailResultInfo(Parcel in) {
        this.data = in.createTypedArrayList(StatisticsdetailInfo.CREATOR);
    }

    public static final Parcelable.Creator<StatisticsdetailResultInfo> CREATOR = new Parcelable.Creator<StatisticsdetailResultInfo>() {
        public StatisticsdetailResultInfo createFromParcel(Parcel source) {
            return new StatisticsdetailResultInfo(source);
        }

        public StatisticsdetailResultInfo[] newArray(int size) {
            return new StatisticsdetailResultInfo[size];
        }
    };
}
