package org.springside.fi.web.params;

import org.hibernate.validator.constraints.NotBlank;

/**  
 * 创建时间：2015年7月26日 下午5:59:11  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：ActuuidParams.java  
 * 类说明：  actuuid参数
 */
public class ActuuidParam {
	@NotBlank
	private String actuuid;
	public String getActuuid() {
		return actuuid;
	}	public void setActuuid(String actuuid) {
		this.actuuid = actuuid;
	}
}
