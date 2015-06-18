package org.springside.fi.service.third;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.fi.common.SystemGlobal;
import org.springside.fi.common.httpclient.HttpClientTemplate;

@Service
public class RongCloudService extends BaseThirdService{
	private final String RongHOSTToken = SystemGlobal.getConfig("RongCloudAPI")+"user/getToken.json";
	
	@Autowired
	private HttpClientTemplate httpClientTemplate;
	
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
	
}
