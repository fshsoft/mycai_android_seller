package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProvinceInfo implements Parcelable {

	public String id;
	public String name;
	public List<CityInfo> city;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeTypedList(city);
	}

	public ProvinceInfo() {
	}

	protected ProvinceInfo(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.city = in.createTypedArrayList(CityInfo.CREATOR);
	}

	public static final Parcelable.Creator<ProvinceInfo> CREATOR = new Parcelable.Creator<ProvinceInfo>() {
		public ProvinceInfo createFromParcel(Parcel source) {
			return new ProvinceInfo(source);
		}

		public ProvinceInfo[] newArray(int size) {
			return new ProvinceInfo[size];
		}
	};
}
