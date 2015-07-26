package org.springside.fi.web.params;

import org.hibernate.validator.constraints.NotBlank;

/**  
 * 创建时间：2015年7月25日 上午2:04:15  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：CheckActivityParam.java  
 * 类说明：  同一天一个用户是否发布过一次活动检查参数
 */
public class CheckActivityParam {
	@NotBlank(message="活动时间为空")
	private String actTime;

	public String getActTime() {
		return actTime;
	}

	public void setActTime(String actTime) {
		this.actTime = actTime;
	}
}
