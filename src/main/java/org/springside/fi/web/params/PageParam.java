package org.springside.fi.web.params;

import javax.validation.constraints.Min;

/**
 * @author tunding:wzc@tcl.com
 * @description
 * @version 1.0
 * @date 创建时间：2015年8月14日 上午11:01:31
 */
public class PageParam {
	@Min(1)
	private int pageNum=1;//列表返回页码，默认第一页
	@Min(1)
	private int pageSize=10;//列表返回每页数量，默认10条记录
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
