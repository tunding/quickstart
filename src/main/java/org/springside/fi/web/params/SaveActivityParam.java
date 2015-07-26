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
	private String name;
	@NotBlank
	private String longitude;
	@NotBlank
	private String latitude;
	@NotBlank
	private String address;
	@NotBlank
	private String time;
	private String info;
	@NotNull
	private Integer kilometer;
	@NotNull
	private Integer limitCount;
	@NotBlank
	private String tag;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
