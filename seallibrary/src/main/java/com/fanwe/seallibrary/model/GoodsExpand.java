package com.fanwe.seallibrary.model;

import java.io.Serializable;

/**
 * @author Altas
 * @email Altas.Tutu@gmail.com
 * @time 2015-3-25 下午9:53:10
 */
public class GoodsExpand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7099504542131914230L;
	public int id;
	public String name;
	public String image;
	public int sort;
	public int goodsId;
	public int cateId;
	public int status;

}
