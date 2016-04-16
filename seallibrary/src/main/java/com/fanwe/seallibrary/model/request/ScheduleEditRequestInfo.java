package com.fanwe.seallibrary.model.request;

import java.io.Serializable;
import java.util.List;

/**
 * 配置请求信息实体
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午7:46:46
 */
public class ScheduleEditRequestInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Integer> hours;
	public int status; // 1:允许接单,0:停止接单
	
}
