package com.fanwe.seallibrary.model;

import android.text.TextUtils;

import com.fanwe.seallibrary.model.result.map.GeoCoderInfo;
import com.fanwe.seallibrary.model.result.map.POIAddress;
import com.fanwe.seallibrary.model.result.map.POILocation;

import java.io.Serializable;
/**
* 经纬度实体
* @author haojiahe
* 
* @time 2015-3-23上午10:20:19
* 
*/
public class PointInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6035127890936099998L;
    public double x; //lat
    public double y; //lnt
    public String address;
    public String detailAddress;
    public PointInfo(){}
    public PointInfo(double x,double y){
    	this.x=x;
    	this.y=y;
    }
	public PointInfo(String point){
		if(!TextUtils.isEmpty(point)){
			String[] ps =point.split(",");
			if(ps.length==2 && TextUtils.isDigitsOnly(ps[0]) && TextUtils.isDigitsOnly(ps[1])){
				x=Double.valueOf(ps[0]);
				y=Double.valueOf(ps[1]);
			}
		}
	}
	public PointInfo(POIAddress info){
		address = info.address+info.title;
		final POILocation location = info.location;
		if (null != location) {
			x = location.lat;
			y = location.lng;
		}
	}
	public PointInfo(GeoCoderInfo info){
		address = info.title;
		final POILocation location = info.location;
		if (null != location) {
			x = location.lat;
			y = location.lng;
		}
	}
	@Override
	public String toString() {
		return "" + this.x + "," + this.y;
	}
    
    
}
