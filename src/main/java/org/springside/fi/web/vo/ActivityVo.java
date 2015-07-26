package org.springside.fi.web.vo;

import java.util.Date;

/**
 * @author tunding:wzc@tcl.com
 * @description 返回活动的信息对象
 * @version 1.0
 * @date 创建时间：2015年7月24日 下午9:26:02
 */
public class ActivityVo extends BaseVo{
	private String actUuid;//uuid
	private String actName;//名称
	private String pubUuid;//发布者uuid
	private String pubName;//发布者名称
	private Date actTime;//开始时间
	private Date pubTime;//发布时间
	private String address;//地址
	private Integer distance;//路程大概距离
	private String longitude;//经度
	private String latitude;//纬度
	private String info;//详情
	private Integer limitCount;//参与人数上限
	private Integer participateCount;//参与人数
	private Integer participateFlag;//当前用户是否参与 1参与 0未参与
	private String tag;//标签
	public String getActUuid() {
		return actUuid;
	}
	public void setActUuid(String actUuid) {
		this.actUuid = actUuid;
	}
	public String getActName() {
		return actName;
	}
	public void setActName(String actName) {
		this.actName = actName;
	}
	public String getPubUuid() {
		return pubUuid;
	}
	public void setPubUuid(String pubUuid) {
		this.pubUuid = pubUuid;
	}
	public String getPubName() {
		return pubName;
	}
	public void setPubName(String pubName) {
		this.pubName = pubName;
	}
	public Date getActTime() {
		return actTime;
	}
	public void setActTime(Date actTime) {
		this.actTime = actTime;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}
	public Integer getParticipateCount() {
		return participateCount;
	}
	public void setParticipateCount(Integer participateCount) {
		this.participateCount = participateCount;
	}
	public Integer getParticipateFlag() {
		return participateFlag;
	}
	public void setParticipateFlag(Integer participateFlag) {
		this.participateFlag = participateFlag;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
