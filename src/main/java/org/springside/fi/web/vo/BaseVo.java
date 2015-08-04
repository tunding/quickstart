package org.springside.fi.web.vo;

import org.springside.fi.web.exception.RestExceptionCode;

/**  
 * 创建时间：2015年7月25日 上午2:29:35  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：BaseVO.java  
 * 类说明：  
 */
public class BaseVo {
	private Integer result = RestExceptionCode.REST_SUCCESS_CODE;
	private Object data = RestExceptionCode.REST_SUCCESS_MSG;
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
