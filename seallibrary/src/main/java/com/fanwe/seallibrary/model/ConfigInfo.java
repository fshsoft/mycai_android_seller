package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午7:56:40
 */
public class ConfigInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5785577506398029021L;
	public String appVersion;
	public boolean forceUpgrade;
	public String appDownUrl;
	public String upgradeInfo;
	// 服务电话
	public String serviceTel;
	public OSSInfo oss;

	public String aboutUrl;
	public String protocolUrl;
	public String helpUrl;

	public List<PaymentInfo> payments;

}

