package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class LeaveTimeInfo implements Serializable{

	/**
	 * 请假记录列表对象
	 */
	private static final long serialVersionUID = -4778060314021461952L;
	
	public String id;
	public String beginTime;
	public String endTime;
	public String createTime;
	public String remark;
	public boolean isChecked=false;
	public int status;
	public String statusStr;

}
