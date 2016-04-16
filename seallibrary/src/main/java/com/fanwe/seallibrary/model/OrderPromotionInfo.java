package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * 订单优惠
 * 
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-29 下午6:08:11
 */
public class OrderPromotionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1046166037103285897L;
	public int id;
	public String promotionName;
	public double discountFee;
}
