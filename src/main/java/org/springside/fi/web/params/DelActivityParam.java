package org.springside.fi.web.params;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author tunding:wzc@tcl.com
 * @description
 * @version 1.0
 * @date 创建时间：2015年7月28日 下午9:46:13
 */
public class DelActivityParam {
	@NotBlank
	private String actuuid;
	private String msg="活动组织者取消活动";
	public String getActuuid() {
		return actuuid;
	}
	public void setActuuid(String actuuid) {
		this.actuuid = actuuid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
