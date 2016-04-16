package com.fanwe.seallibrary.model;

import java.io.Serializable;
import java.util.List;

public class StimelistsInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5506288337485591973L;

	public String id;
	public List<String> week;
	public String weeks;
	public List<String> times;
	public List<String> hours;
	public List<String> shifts;
	
}
