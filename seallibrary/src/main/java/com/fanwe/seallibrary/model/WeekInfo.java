package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class WeekInfo implements Serializable {

	/**
	 * 星期
	 */
	private static final long serialVersionUID = 4683748602888396007L;

	private String weekname;
	private String weeknumbe;
	private int isChick;

	public String getWeekname() {
		return weekname;
	}

	public void setWeekname(String weekname) {
		this.weekname = weekname;
	}

	public String getWeeknumbe() {
		return weeknumbe;
	}

	public void setWeeknumbe(String weeknumbe) {
		this.weeknumbe = weeknumbe;
	}

	public int getIsChick() {
		return isChick;
	}

	public void setIsChick(int isChick) {
		this.isChick = isChick;
	}
}
