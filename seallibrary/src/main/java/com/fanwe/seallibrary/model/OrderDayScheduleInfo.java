package com.fanwe.seallibrary.model;

import com.fanwe.seallibrary.model.result.OrderSummary;

import java.io.Serializable;
import java.util.List;


public class OrderDayScheduleInfo implements Serializable{

	/**
	 * Order对像（订单)
	 */
	private static final long serialVersionUID = -6493200682612238775L;

	public String day;
	public List<OrderSummary> list;



}
