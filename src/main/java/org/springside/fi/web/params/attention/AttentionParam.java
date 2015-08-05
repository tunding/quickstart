package org.springside.fi.web.params.attention;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author tunding:wzc@tcl.com
 * @description 关注动作参数对象
 * @version 1.0
 * @date 创建时间：2015年8月5日 下午6:56:54
 */
public class AttentionParam {
	@NotBlank
	private String passiveAttentionUuid;
	private String message;
	public String getPassiveAttentionUuid() {
		return passiveAttentionUuid;
	}
	public void setPassiveAttentionUuid(String passiveAttentionUuid) {
		this.passiveAttentionUuid = passiveAttentionUuid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
