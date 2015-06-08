package org.springside.fi.map.utils;

import org.springside.fi.map.common.ConsArgs;


public class LongDistance {
	public static double getLongDistance(float lon1,float lat1,float lon2,float lat2){
		double ew1, ns1, ew2, ns2;
		double distance;
		// 角度转换为弧度
		ew1 = lon1 * ConsArgs.DEF_PI180;
		ns1 = lat1 * ConsArgs.DEF_PI180;
		ew2 = lon2 * ConsArgs.DEF_PI180;
		ns2 = lat2 * ConsArgs.DEF_PI180;
		// 求大圆劣弧与球心所夹的角(弧度)
		distance = Math.sin(ns1)* Math.sin(ns2)+ Math.cos(ns1)* Math.cos(ns2)* Math.cos(ew1- ew2);
		// 调整到[-1..1]范围内，避免溢出
		if(distance > 1.0){
			distance = 1.0;
		}else if(distance < -1.0){
			distance = -1.0;
		}
		// 求大圆劣弧长度
		distance = ConsArgs.DEF_R *Math.acos(distance);
		return distance;
	}
}
