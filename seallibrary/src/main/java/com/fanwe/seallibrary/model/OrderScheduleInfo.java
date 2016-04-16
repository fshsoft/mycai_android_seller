package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;


public class OrderScheduleInfo implements Parcelable {

	/**
	 * Order对像（订单)
	 */
	private static final long serialVersionUID = -6493200682612238775L;

	public int id;
	public String sn;
	public String mobile;
	public String address;
	public int status;
	public String goodsName;
	public int totalFee;
	public int discountFee;
	public int payFee;
	public int appointTime;
	public int appointHour;
	public int appointDay;
	public String userName;
	public String buyRemark;
	public boolean isRate;
	public int payStatus;
	public int createTime;
	public long serviceStartTime;
	public long serviceFinishTime;
	public int reservationCode;
	public PointInfo mapPoint;
	public String orderStatusStr;
	public boolean isCanStartService;
	public boolean isCanFinishService;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.sn);
		dest.writeString(this.mobile);
		dest.writeString(this.address);
		dest.writeInt(this.status);
		dest.writeString(this.goodsName);
		dest.writeInt(this.totalFee);
		dest.writeInt(this.discountFee);
		dest.writeInt(this.payFee);
		dest.writeInt(this.appointTime);
		dest.writeInt(this.appointHour);
		dest.writeInt(this.appointDay);
		dest.writeString(this.userName);
		dest.writeString(this.buyRemark);
		dest.writeByte(isRate ? (byte) 1 : (byte) 0);
		dest.writeInt(this.payStatus);
		dest.writeInt(this.createTime);
		dest.writeLong(this.serviceStartTime);
		dest.writeLong(this.serviceFinishTime);
		dest.writeInt(this.reservationCode);
		dest.writeSerializable(this.mapPoint);
		dest.writeString(this.orderStatusStr);
		dest.writeByte(isCanStartService ? (byte) 1 : (byte) 0);
		dest.writeByte(isCanFinishService ? (byte) 1 : (byte) 0);
	}

	public OrderScheduleInfo() {
	}

	protected OrderScheduleInfo(Parcel in) {
		this.id = in.readInt();
		this.sn = in.readString();
		this.mobile = in.readString();
		this.address = in.readString();
		this.status = in.readInt();
		this.goodsName = in.readString();
		this.totalFee = in.readInt();
		this.discountFee = in.readInt();
		this.payFee = in.readInt();
		this.appointTime = in.readInt();
		this.appointHour = in.readInt();
		this.appointDay = in.readInt();
		this.userName = in.readString();
		this.buyRemark = in.readString();
		this.isRate = in.readByte() != 0;
		this.payStatus = in.readInt();
		this.createTime = in.readInt();
		this.serviceStartTime = in.readLong();
		this.serviceFinishTime = in.readLong();
		this.reservationCode = in.readInt();
		this.mapPoint = (PointInfo) in.readSerializable();
		this.orderStatusStr = in.readString();
		this.isCanStartService = in.readByte() != 0;
		this.isCanFinishService = in.readByte() != 0;
	}

	public static final Parcelable.Creator<OrderScheduleInfo> CREATOR = new Parcelable.Creator<OrderScheduleInfo>() {
		public OrderScheduleInfo createFromParcel(Parcel source) {
			return new OrderScheduleInfo(source);
		}

		public OrderScheduleInfo[] newArray(int size) {
			return new OrderScheduleInfo[size];
		}
	};
}
