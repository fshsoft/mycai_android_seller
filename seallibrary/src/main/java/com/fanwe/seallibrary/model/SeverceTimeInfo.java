package com.fanwe.seallibrary.model;

import java.io.Serializable;

public class SeverceTimeInfo  implements Serializable{

	/**
	 * 服务时间设置
	 */
	private static final long serialVersionUID = -207266690059065546L;
	public String hours;
	public boolean isChick=false;
	
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
}
