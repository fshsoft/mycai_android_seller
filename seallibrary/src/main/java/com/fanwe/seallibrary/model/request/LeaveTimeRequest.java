package com.fanwe.seallibrary.model.request;

import java.util.List;

import com.fanwe.seallibrary.model.LeaveTimeInfo;
import com.fanwe.seallibrary.model.result.BaseResult;

public class LeaveTimeRequest extends BaseResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7559904887550209463L;
	
	public List<LeaveTimeInfo> data;
	
}
