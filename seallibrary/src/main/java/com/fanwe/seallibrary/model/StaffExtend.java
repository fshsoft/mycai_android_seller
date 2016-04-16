package com.fanwe.seallibrary.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author haojiahe
 * 
 * @time 2015-3-23下午8:18:00
 * 
 */
public class StaffExtend implements  Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8114529873373495685L;
	
	public int sellerId;
	public float goodsAvgPrice;
	public int orderCount;
	public sellerCreditRank creditRank;
	public int  commentTotalCount;
	public int  commentGoodCount;
	public int commentNeutralCount;
	public int  commentBadCount;
	public float commentSpecialtyAvgScore;
	public float commentCommunicateAvgScore;
	public float commentPunctualityAvgScore;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.sellerId);
		dest.writeFloat(this.goodsAvgPrice);
		dest.writeInt(this.orderCount);
		dest.writeParcelable(this.creditRank, 0);
		dest.writeInt(this.commentTotalCount);
		dest.writeInt(this.commentGoodCount);
		dest.writeInt(this.commentNeutralCount);
		dest.writeInt(this.commentBadCount);
		dest.writeFloat(this.commentSpecialtyAvgScore);
		dest.writeFloat(this.commentCommunicateAvgScore);
		dest.writeFloat(this.commentPunctualityAvgScore);
	}

	public StaffExtend() {
	}

	protected StaffExtend(Parcel in) {
		this.sellerId = in.readInt();
		this.goodsAvgPrice = in.readFloat();
		this.orderCount = in.readInt();
		this.creditRank = in.readParcelable(sellerCreditRank.class.getClassLoader());
		this.commentTotalCount = in.readInt();
		this.commentGoodCount = in.readInt();
		this.commentNeutralCount = in.readInt();
		this.commentBadCount = in.readInt();
		this.commentSpecialtyAvgScore = in.readFloat();
		this.commentCommunicateAvgScore = in.readFloat();
		this.commentPunctualityAvgScore = in.readFloat();
	}

	public static final Creator<StaffExtend> CREATOR = new Creator<StaffExtend>() {
		public StaffExtend createFromParcel(Parcel source) {
			return new StaffExtend(source);
		}

		public StaffExtend[] newArray(int size) {
			return new StaffExtend[size];
		}
	};
}
