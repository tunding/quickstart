package org.springside.fi.web.params.account;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author tunding:wzc@tcl.com
 * @description 检查loginName是否已被注册
 * @version 1.0
 * @date 创建时间：2015年8月5日 下午1:58:14
 */
public class CheckLoginNameParam {
	@NotBlank
	private String loginName;
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
