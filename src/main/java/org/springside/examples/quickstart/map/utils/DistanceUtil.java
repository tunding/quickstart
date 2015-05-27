package org.springside.examples.quickstart.map.utils;

import org.springside.examples.quickstart.map.module.LatLng;


public class DistanceUtil {
	/**
	 * @param paramLatLng1
	 * @param paramLatLng2
	 * @return
	 * 经纬度1度60分，1分60秒，1分约1.85km。据查1km垂直高度差约20米
	 * 在大约1km内使用ShortDistance，1km外使用LongDistance计算距离，经纬度也就是0.01度。
	 */
	public static double getDistance(LatLng paramLatLng1, LatLng paramLatLng2){
	    if ((paramLatLng1 == null) || (paramLatLng2 == null))
	        return -1.0D;
	    float lon1 = (float)paramLatLng1.longitude;
	    float lat1 = (float)paramLatLng1.latitude;
	    float lon2 = (float)paramLatLng2.longitude;
	    float lat2 = (float)paramLatLng2.latitude;
	    if(Math.abs(lon1-lon2)<0.01||Math.abs(lat1-lat2)<0.01){
	    	return ShortDistance.GetShortDistance(lon1, lat1, lon2, lat2);
	    }else{
	    	return LongDistance.getLongDistance(lon1, lat1, lon2, lat2);
	    }
	}
}
