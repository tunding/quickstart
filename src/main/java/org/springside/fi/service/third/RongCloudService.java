package org.springside.fi.service.third;

import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.fi.common.SystemGlobal;
import org.springside.fi.common.httpclient.HttpClientTemplate;
import org.springside.fi.service.rong.models.Message;
import org.springside.fi.service.rong.models.TxtMessage;

/**
* @author tunding:wzc@tcl.com
* @project running
* @version 1.0
* @date 创建时间：2015年7月9日 下午2:25:30
* @name RongCloudService.java
* @description 融云http交互接口
*/
@Service
public class RongCloudService extends BaseThirdService{
	private final static String RongHOSTToken = SystemGlobal.getConfig("RongCloudAPI")+"user/getToken.json";
	private final static String RongHOSTSystemMsg = SystemGlobal.getConfig("RongCloudAPI")+"message/system/publish.json";
	
	@Autowired
	private HttpClientTemplate httpClientTemplate;
	
	/**
	 * @param userId
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 * 判断发送token需要的参数
	 */
	public String getToken(String userId, String name) throws UnsupportedEncodingException{
		if(StringUtils.isEmpty(userId)){
			logger.error("userId is empty");
			return null;
		}
		if(StringUtils.isEmpty(name)){
			logger.error("name is empty");
			return null;
		}
		return getTokenData(userId, name);
		
	}
	
	/**
	 * @param userId
	 * @param name
	 * @return
	 * 发送取得token的http请求
	 */
	private String getTokenData(String userId, String name){
		String reqParams = "userId="+userId+"&name="+name;
		StringRequestEntity requestEntity = null;
		try{
			requestEntity = new StringRequestEntity(reqParams, "application/x-www-form-urlencoded", "UTF-8");
			return convertResult(httpClientTemplate.executePostMethod(RongHOSTToken, requestEntity, null));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param fromUserId
	 * @param toUserId
	 * @param msg
	 * 组织message文本消息，向参加人员发送取消通知
	 */
	public void sendPartCancel(String fromUserId, String toUserId, String msg){
		Message message = new TxtMessage(msg);
		sendPartCancelData(fromUserId, toUserId, message);
	}
	
	/**
	 * @param fromUserId
	 * @param toUserId
	 * @param msg
	 * 发送取消活动系统消息
	 */
	private String sendPartCancelData(String fromUserId, String toUserId, Message msg){
		String reqParams = "fromUserId="+fromUserId+"&toUserId="+toUserId+"&objectName=RC:TxtMsg&content="+msg;
		System.out.println(RongHOSTSystemMsg);
		System.out.println(reqParams);
		StringRequestEntity requestEntity = null;
		try{
			requestEntity = new StringRequestEntity(reqParams, "application/x-www-form-urlencoded", "UTF-8");
			return convertResult(httpClientTemplate.executePostMethod(RongHOSTSystemMsg, requestEntity, null));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
