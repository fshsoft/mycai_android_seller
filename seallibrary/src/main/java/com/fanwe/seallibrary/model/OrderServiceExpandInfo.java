package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.ArrayList;


public class OrderServiceExpandInfo implements Serializable{


	private static final long serialVersionUID = -6493200682612238775L;

	public int id;
	public int amount;
	public int price;
	public String images;
	public int status;
	public int orderId;
	public int serviceExpandId;
	public GoodsExpand expandGoods;
	public boolean isChecked;
	public ArrayList<String> localImages;

}
