package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by admin on 2015/11/4.
 */
public class EvaPackInfo implements Parcelable {
    //评分
    public float score;
    //未回复数量
    public int unReply;
    //已回复数量
    public int reply;
    //评价列表
    public List<OrderRateInfo> eva;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.score);
        dest.writeInt(this.unReply);
        dest.writeInt(this.reply);
        dest.writeTypedList(eva);
    }

    public EvaPackInfo() {
    }

    protected EvaPackInfo(Parcel in) {
        this.score = in.readFloat();
        this.unReply = in.readInt();
        this.reply = in.readInt();
        this.eva = in.createTypedArrayList(OrderRateInfo.CREATOR);
    }

    public static final Creator<EvaPackInfo> CREATOR = new Creator<EvaPackInfo>() {
        public EvaPackInfo createFromParcel(Parcel source) {
            return new EvaPackInfo(source);
        }

        public EvaPackInfo[] newArray(int size) {
            return new EvaPackInfo[size];
        }
    };
}
