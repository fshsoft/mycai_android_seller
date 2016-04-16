package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;


public class OrderInfo implements Serializable {


    public int id;
    public String sn;

    public SellerInfo seller;
    public List<Goods> goods;
    public UserInfo user;
    //预约时间
    public long appointTime;
    public long serviceEndTime;
    public OrderPromotionInfo promotion;

    public String userName;
    public String mobile;
    public String address;
    public PointInfo mapPoint;
    public double payFee;
    public double discountFee;
    public double totalFee;
    public String buyRemark;
    public String orderStatusStr;
    public int status;
    public int payStatus;
    public long createTime;

    public OrderRate orderRate;

    public boolean isCanStartService;//是否可以开始服务
    public boolean isCanFinishService;//是否可以完成服务

    public String goodsName;


    public String mapPointStr;

    public JZCleaning JzCleaning;

    public List<OrderServiceExpandInfo> orderServiceExpand;

    public long StaffEndTime;

    public String refundContent;

    public long refundTime;


    public String barcode;

    public List<StaffLog> staffLog;   // 工程师日志

    public String payment;  // 订单支付方式

    public String buyerCancelContent; // 买家取消订单原因

    public boolean isRate; // 会员是否已评价

    public OrderInfo() {
    }

//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeInt(this.id);
//		dest.writeString(this.sn);
//		dest.writeParcelable(this.seller, 0);
//		dest.writeSerializable(this.goods);
//		dest.writeSerializable(this.user);
//		dest.writeLong(this.appointTime);
//		dest.writeLong(this.serviceEndTime);
//		dest.writeSerializable(this.promotion);
//		dest.writeString(this.userName);
//		dest.writeString(this.mobile);
//		dest.writeString(this.address);
//		dest.writeSerializable(this.mapPoint);
//		dest.writeDouble(this.payFee);
//		dest.writeDouble(this.discountFee);
//		dest.writeDouble(this.totalFee);
//		dest.writeString(this.buyRemark);
//		dest.writeString(this.orderStatusStr);
//		dest.writeInt(this.status);
//		dest.writeInt(this.payStatus);
//		dest.writeLong(this.createTime);
//		dest.writeParcelable(this.orderRate, 0);
//		dest.writeByte(isCanStartService ? (byte) 1 : (byte) 0);
//		dest.writeByte(isCanFinishService ? (byte) 1 : (byte) 0);
//		dest.writeString(this.goodsName);
//		dest.writeString(this.mapPointStr);
//		dest.writeSerializable(this.JzCleaning);
//		dest.writeList(this.orderServiceExpand);
//		dest.writeLong(this.StaffEndTime);
//		dest.writeString(this.refundContent);
//		dest.writeLong(this.refundTime);
//		dest.writeString(this.barcode);
//	}
//
//	protected OrderInfo(Parcel in) {
//		this.id = in.readInt();
//		this.sn = in.readString();
//		this.seller = in.readParcelable(SellerInfo.class.getClassLoader());
//		this.goods = (Goods) in.readSerializable();
//		this.user = (UserInfo) in.readSerializable();
//		this.appointTime = in.readLong();
//		this.serviceEndTime = in.readLong();
//		this.promotion = (OrderPromotionInfo) in.readSerializable();
//		this.userName = in.readString();
//		this.mobile = in.readString();
//		this.address = in.readString();
//		this.mapPoint = (PointInfo) in.readSerializable();
//		this.payFee = in.readDouble();
//		this.discountFee = in.readDouble();
//		this.totalFee = in.readDouble();
//		this.buyRemark = in.readString();
//		this.orderStatusStr = in.readString();
//		this.status = in.readInt();
//		this.payStatus = in.readInt();
//		this.createTime = in.readLong();
//		this.orderRate = in.readParcelable(OrderRate.class.getClassLoader());
//		this.isCanStartService = in.readByte() != 0;
//		this.isCanFinishService = in.readByte() != 0;
//		this.goodsName = in.readString();
//		this.mapPointStr = in.readString();
//		this.JzCleaning = (JZCleaning) in.readSerializable();
//		this.orderServiceExpand = new ArrayList<OrderServiceExpandInfo>();
//		in.readList(this.orderServiceExpand, List.class.getClassLoader());
//		this.StaffEndTime = in.readLong();
//		this.refundContent = in.readString();
//		this.refundTime = in.readLong();
//		this.barcode = in.readString();
//	}
//
//	public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>() {
//		public OrderInfo createFromParcel(Parcel source) {
//			return new OrderInfo(source);
//		}
//
//		public OrderInfo[] newArray(int size) {
//			return new OrderInfo[size];
//		}
//	};
}
