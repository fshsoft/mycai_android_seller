package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2015/10/30.
 */
public class NewMessage implements Parcelable {
    public boolean hasNewMessage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(hasNewMessage ? (byte) 1 : (byte) 0);
    }

    public NewMessage() {
    }

    protected NewMessage(Parcel in) {
        this.hasNewMessage = in.readByte() != 0;
    }

    public static final Parcelable.Creator<NewMessage> CREATOR = new Parcelable.Creator<NewMessage>() {
        public NewMessage createFromParcel(Parcel source) {
            return new NewMessage(source);
        }

        public NewMessage[] newArray(int size) {
            return new NewMessage[size];
        }
    };
}
