package org.springside.fi.web.params;

import org.hibernate.validator.constraints.NotBlank;

/**  
 * 创建时间：2015年7月31日 下午9:13:29  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：NearActivityListParam.java  
 * 类说明：  附近活动列表查询传入参数
 */
public class NearActivityListParam {
	@NotBlank
	private String longitude;//经度
	@NotBlank
	private String latitude;//纬度
	private int distance=10000;//附近活动定义的距离范围，默认10000米
	private int pageNum=1;//列表返回页码，默认第一页
	private int pageSize=10;//列表返回每页数量，默认10条记录
	private String start_time;//附近活动列表返回某个时间点后才会开始的活动,时间格式为"20150701210000"
	private String sort;//排序字段，默认按照距离由近及远排序
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
}
