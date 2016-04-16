package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息
 */
public class UserInfo implements Parcelable {

	public int id;// 编号
	public String mobile; // 手机号码
	public String name;// 名称
	public String avatar; // 头像
	public int role;//权限，与判断；1:商家；2：配送人员；4：服务人员
	public String bg;//头像大背景图片

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.mobile);
		dest.writeString(this.name);
		dest.writeString(this.avatar);
		dest.writeInt(this.role);
		dest.writeString(this.bg);
	}

	public UserInfo() {
	}

	protected UserInfo(Parcel in) {
		this.id = in.readInt();
		this.mobile = in.readString();
		this.name = in.readString();
		this.avatar = in.readString();
		this.role = in.readInt();
		this.bg = in.readString();
	}

	public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
		public UserInfo createFromParcel(Parcel source) {
			return new UserInfo(source);
		}

		public UserInfo[] newArray(int size) {
			return new UserInfo[size];
		}
	};
}