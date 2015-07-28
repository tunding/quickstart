package org.springside.fi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @author tunding
 * 活动Entity
 */
@Entity
@Table(name="activity_info")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity extends BaseEntity{
	private String name;
	private String uuid;
	private String actuuid;
	private String info;
	private Date time;
	private String address;
	private Integer kilometer;
	private String longitude;
	private String latitude;
	private double distance;
	private Integer state;
	private Integer participateCount;
	private Integer delFlag;
	private Integer limitCount;
	private Integer participateFlag;
	private String tag;
	private String publishName;
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="actuuid")
	public String getActuuid() {
		return actuuid;
	}
	public void setActuuid(String actuuid) {
		this.actuuid = actuuid;
	}
	@Column(name="info")
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Column(name="time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+08:00")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Column(name="address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name="kilometer")
	public Integer getKilometer() {
		return kilometer;
	}
	public void setKilometer(Integer kilometer) {
		this.kilometer = kilometer;
	}
	@Column(name="longitude")
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	@Column(name="latitude")
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Transient
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Transient
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Transient
	public Integer getParticipateCount() {
		return participateCount;
	}
	public void setParticipateCount(Integer participateCount) {
		this.participateCount = participateCount;
	}
	@JsonIgnore
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name="limit_count")
	public Integer getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}
	@Transient
	public Integer getParticipateFlag() {
		return participateFlag;
	}
	public void setParticipateFlag(Integer participateFlag) {
		this.participateFlag = participateFlag;
	}
	@Column(name="tag")
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Transient
	public String getPublishName() {
		return publishName;
	}
	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
}
