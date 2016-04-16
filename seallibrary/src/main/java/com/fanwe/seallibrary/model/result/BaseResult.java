package com.fanwe.seallibrary.model.result;

import java.io.Serializable;

import com.fanwe.seallibrary.Hand.InterFace;

public class BaseResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int code;
	public String msg;
	public String debug;
	public String token;
	public String key;
	
	public BaseResult(){
		code = InterFace.ResponseCode.FAILURE.code;
		msg = InterFace.ResponseCode.FAILURE.msg;
	}
}