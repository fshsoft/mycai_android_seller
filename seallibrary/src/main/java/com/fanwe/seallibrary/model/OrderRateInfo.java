package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by admin on 2015/11/4.
 */
public class OrderRateInfo implements Parcelable {
    //编号.
    public int id;
    //评价星级（1-5）.
    public int star;
    //评价内容.
    public String content;
    //评价图片.
    public List<String> images;
    //商家回复.
    public String reply;
    public String result;
    //订单编号.
    public int orderId;
    //商家评价回复时间 2015-07-29
    public String replyTime;

    //创建时间
    public String createTime;
    public int isAno;
    public String userName;
    public String avatar;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.star);
        dest.writeString(this.content);
        dest.writeStringList(this.images);
        dest.writeString(this.reply);
        dest.writeString(this.result);
        dest.writeInt(this.orderId);
        dest.writeString(this.replyTime);
        dest.writeString(this.createTime);
        dest.writeInt(this.isAno);
        dest.writeString(this.userName);
        dest.writeString(this.avatar);
    }

    public OrderRateInfo() {
    }

    protected OrderRateInfo(Parcel in) {
        this.id = in.readInt();
        this.star = in.readInt();
        this.content = in.readString();
        this.images = in.createStringArrayList();
        this.reply = in.readString();
        this.result = in.readString();
        this.orderId = in.readInt();
        this.replyTime = in.readString();
        this.createTime = in.readString();
        this.isAno = in.readInt();
        this.userName = in.readString();
        this.avatar = in.readString();
    }

    public static final Creator<OrderRateInfo> CREATOR = new Creator<OrderRateInfo>() {
        public OrderRateInfo createFromParcel(Parcel source) {
            return new OrderRateInfo(source);
        }

        public OrderRateInfo[] newArray(int size) {
            return new OrderRateInfo[size];
        }
    };
}
