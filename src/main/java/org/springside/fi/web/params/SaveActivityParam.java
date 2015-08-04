package org.springside.fi.web.params;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**  
* 创建时间：2015年7月25日 上午1:49:57  
* 项目名称：running  
* @author wangzhichao  
* @version 1.0   
* 文件名称：SaveActivityParam.java  
* 类说明：  保存activity需要的信息参数
*/
public class SaveActivityParam {
	@NotBlank
	private String name;//活动名称
	@NotBlank
	private String longitude;//经度
	@NotBlank
	private String latitude;//纬度
	@NotBlank
	private String address;//活动开始地址
	@NotBlank
	private String start_time;//活动开始时间
	private String info;//附加信息
	@NotNull
	private Integer kilometer;//活动跑步距离估算
	@NotNull
	private Integer limitCount;//参与人数上限
	@NotBlank
	private String tag;//标签：慢跑、散步等
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getKilometer() {
		return kilometer;
	}
	public void setKilometer(Integer kilometer) {
		this.kilometer = kilometer;
	}
	public Integer getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
