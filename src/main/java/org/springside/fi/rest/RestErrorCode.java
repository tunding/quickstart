package org.springside.fi.rest;

public class RestErrorCode {
	public final static int REST_SUCCESS_CODE = 1000;
	public final static String REST_SUCCESS_MSG = "success";
	
	public final static int REST_INTERNAL_ERROR_CODE = 1001;
	public final static String REST_INTERNAL_ERROR_MSG = "内部错误 (Try Again Later)";
	
	public final static int REST_PARAMS_NULL_CODE = 1002;
	public final static String REST_PARAMS_NULL_MSG = "参数不能为空";
	
	public final static int REST_SIGNAUTHC_CODE = 1003;
	public final static String REST_SIGNAUTHC_MSG = "签名验证失败";
	
	public final static int REST_ISFRIEND_CODE = 1004;
	public final static String REST_ISFRIEND_MSG = "你已经是对方好友";
	
	public final static int REST_ISBLACK_CODE = 1005;
	public final static String REST_ISBLACK_MSG = "对方已经将你拉黑";
}
