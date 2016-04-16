package com.fanwe.seallibrary.model.request;

import java.io.Serializable;

/**
 * 配置请求信息实体
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午7:46:46
 */
public class ConfigRequestInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3032795558842183665L;
	//设备信息
	public String systemInfo;
	//设备类型
	public String deviceType;
	//操作系统版本
	public String systemVersion;
	//当前app版本号
	public String appVersion;
	public ConfigRequestInfo(String systemInfo, String deviceType, String systemVersion, String appVersion) {
		super();
		this.systemInfo = systemInfo;
		this.deviceType = deviceType;
		this.systemVersion = systemVersion;
		this.appVersion = appVersion;
	}
	
	
}
