package org.springside.fi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="runner_relationship")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Relationship extends BaseEntity{
	private String attentionUuid;
	private String passiveAttentionUuid;
	private Integer state;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
