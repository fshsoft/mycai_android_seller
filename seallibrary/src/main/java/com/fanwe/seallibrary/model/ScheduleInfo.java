package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * 
 * @author haojiahe
 * 
 * @time 2015-3-23下午8:18:00
 * 
 */
public class ScheduleInfo implements Serializable {

	public interface IState {
		public int IDLE = 0;
		public int ORDERED = 1;
		public int STOP = -1;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String hour;
//	public String day;
//	public String week;
	public int status; //0：暂无安排 1：有单子 -1：停止接单
	public String goodName;
	public String userName;
	public String address;
	public String mobile;
	public int orderId;
	public boolean bSelected;
	
	public ScheduleInfo(){
		bSelected = false;
	}
}
