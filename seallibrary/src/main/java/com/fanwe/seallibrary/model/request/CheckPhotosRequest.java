package com.fanwe.seallibrary.model.request;

import com.fanwe.seallibrary.model.WashPhotoItem;

import java.io.Serializable;
import java.util.List;

/**
 * 配置请求信息实体
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-19 下午7:46:46
 */
public class CheckPhotosRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3032795558842183665L;
	//设备信息
	public int orderId;
	public List<WashPhotoItem> orderServiceExpand;
	public String barcode;
	
}
