package org.springside.fi.web.exception;
/**  
 * 创建时间：2015年7月25日 上午8:44:58  
 * 项目名称：running  
 * @author wangzhichao  
 * @version 1.0   
 * 文件名称：RestExceptionCode.java  
 * 类说明：  返回对象的异常码和异常信息定义
 */
public class RestExceptionCode {
    public final static int REST_SUCCESS_CODE = 1000;
    public final static String REST_SUCCESS_MSG = "success";

    public final static int REST_PARAMETER_ERROR_CODE = 1001;
    public final static String REST_INPUT_PARAMETER_ERROR_MSG = "input parameter error";
    
    public final static int REST_SYSTEM_ERROR_CODE = 1002;
    public final static String REST_SYSTEM_ERROR_MSG = "system error";
    
    public final static int REST_SIGNATURE_ERROR_CODE = 1004;
    public final static String REST_SIGNATURE_ERROR_MSG = "authentication failure about digital signature";
    
    public final static int REST_DUPLICATION_ERROR_CODE = 1005;
    public final static String REST_DUPLICATION_ERROR_MSG = "duplicated data";
}
