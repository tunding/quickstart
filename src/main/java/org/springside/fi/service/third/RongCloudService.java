package org.springside.fi.service.third;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springside.fi.common.SystemGlobal;

@Service
public class RongCloudService {
	private final String RongHOST = SystemGlobal.getConfig("RongCloudAPI");
	private final String appSecret = SystemGlobal.getConfig("App-Key");
	
	public String getToken(String userId, String name) throws UnsupportedEncodingException{
		String nonce = getRandom();
		String timestamp = String.valueOf(new Date().getTime()/1000);
		byte[] sign = DigestUtils.sha1(appSecret+nonce+timestamp);
		String signature = "";
		for (int i=0; i < sign.length; i++) {
			signature +=
					Integer.toString( ( sign[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
	}
	
	private String getRandom(){
		Random r=new Random();
		return String.valueOf(r.nextInt(Integer.MAX_VALUE));
	}
	
}
