package org.springside.fi.web.params.acitvity;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author tunding:wzc@tcl.com
 * @description 参加取消活动参数
 * @version 1.0
 * @date 创建时间：2015年8月5日 下午4:28:21
 */
public class PartParam {
	@NotBlank
	private String uuid;
	@NotBlank
	private String actuuid;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getActuuid() {
		return actuuid;
	}
	public void setActuuid(String actuuid) {
		this.actuuid = actuuid;
	}
}
