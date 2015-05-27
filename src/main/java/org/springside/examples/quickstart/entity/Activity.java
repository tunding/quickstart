package org.springside.examples.quickstart.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

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
	private String uuid;
	private String info;
	private Date time;
	private String address;
	private Integer kilometer;
	private String longitude;
	private String latitude;
	private String geohashCode;
	private double distance;
	@Column(name="uuid")
	@NotBlank
	@Length(min=32,max=32)
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="info")
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Column(name="time")
	@JsonIgnore
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+08:00")
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Column(name="address")
	@NotBlank
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name="kilometer")
	@NotNull
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
	@Column(name="geohashCode")
	public String getGeohashCode() {
		return geohashCode;
	}
	public void setGeohashCode(String geohashCode) {
		this.geohashCode = geohashCode;
	}
	@Transient
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

}
