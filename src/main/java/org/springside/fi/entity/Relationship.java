package org.springside.fi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="runner_relationship")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Relationship extends BaseEntity{
	private String attentionUuid;
	private String passiveAttentionUuid;
	private Integer state;//黑名单
	private Integer delFlag;
	@Column(name="attention_uuid")
	public String getAttentionUuid() {
		return attentionUuid;
	}
	public void setAttentionUuid(String attentionUuid) {
		this.attentionUuid = attentionUuid;
	}
	@Column(name="passive_attention_uuid")
	public String getPassiveAttentionUuid() {
		return passiveAttentionUuid;
	}
	public void setPassiveAttentionUuid(String passiveAttentionUuid) {
		this.passiveAttentionUuid = passiveAttentionUuid;
	}
	@Column(name="state")
	@JsonIgnore
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@JsonIgnore
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
}
