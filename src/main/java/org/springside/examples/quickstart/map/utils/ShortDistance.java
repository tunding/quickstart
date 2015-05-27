package org.springside.examples.quickstart.map.utils;

import org.springside.examples.quickstart.map.common.ConsArgs;


public class ShortDistance {

	public static double GetShortDistance(float lon1,float lat1,float lon2,float lat2){
	    double ew1, ns1, ew2, ns2;
	    double dx, dy, dew;
	    double distance;
	    // 角度转换为弧度
	    ew1 = lon1 * ConsArgs.DEF_PI180;
	    ns1 = lat1 * ConsArgs.DEF_PI180;
	    ew2 = lon2 * ConsArgs.DEF_PI180;
	    ns2 = lat2 * ConsArgs.DEF_PI180;
	    // 经度差
	    dew = (ew1 - ew2);
	    // 若跨东经和西经180 度，进行调整
	    if (dew> ConsArgs.DEF_PI)
	        dew = ConsArgs.DEF_2PI - dew;
	    else if(dew <-ConsArgs.DEF_PI)
	        dew = ConsArgs.DEF_2PI + dew;
	    dx = ConsArgs.DEF_R *Math.cos(ns1)* dew;// 东西方向长度(在纬度圈上的投影长度)
	    dy = ConsArgs.DEF_R *(ns1 - ns2);// 南北方向长度(在经度圈上的投影长度)
	    // 勾股定理求斜边长
	    distance = Math.sqrt(dx* dx + dy* dy);
	    return distance;
	}
}
