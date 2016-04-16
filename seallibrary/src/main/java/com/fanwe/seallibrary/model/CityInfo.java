package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CityInfo implements Parcelable {
	public String id;
	public String name;
	public List<AreaInfo> area;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeTypedList(area);
	}

	public CityInfo() {
	}

	protected CityInfo(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.area = in.createTypedArrayList(AreaInfo.CREATOR);
	}

	public static final Parcelable.Creator<CityInfo> CREATOR = new Parcelable.Creator<CityInfo>() {
		public CityInfo createFromParcel(Parcel source) {
			return new CityInfo(source);
		}

		public CityInfo[] newArray(int size) {
			return new CityInfo[size];
		}
	};
}
