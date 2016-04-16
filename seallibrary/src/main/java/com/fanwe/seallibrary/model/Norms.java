package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Norms implements Parcelable {

    /**
     *
     */
    private static final long serialVersionUID = -7099504542131914230L;
    public int id;
    public String name;
    public double price;
    public int stock;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeInt(this.stock);
    }

    public Norms() {
    }

    protected Norms(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readDouble();
        this.stock = in.readInt();
    }

    public static final Creator<Norms> CREATOR = new Creator<Norms>() {
        public Norms createFromParcel(Parcel source) {
            return new Norms(source);
        }

        public Norms[] newArray(int size) {
            return new Norms[size];
        }
    };
}
