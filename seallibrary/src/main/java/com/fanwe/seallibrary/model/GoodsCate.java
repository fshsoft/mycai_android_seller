package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-25 下午9:53:10
 */
public class GoodsCate implements Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = -7099504542131914230L;
    public int id;
    public String name;
    public String icon;
    public int goodsNum;
    public int tradeId;
//    public Cates cates;
    public int type;

//    static public class Cates implements Serializable {
//        private static final long serialVersionUID = -7099504542131914230L;
//        public int type;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.goodsNum);
        dest.writeInt(this.tradeId);
        dest.writeInt(this.type);
    }

    public GoodsCate() {
    }

    protected GoodsCate(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
        this.goodsNum = in.readInt();
        this.tradeId = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<GoodsCate> CREATOR = new Creator<GoodsCate>() {
        public GoodsCate createFromParcel(Parcel source) {
            return new GoodsCate(source);
        }

        public GoodsCate[] newArray(int size) {
            return new GoodsCate[size];
        }
    };
}
