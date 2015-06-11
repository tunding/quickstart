package org.springside.fi.service.third;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseThirdService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected String convertResult(byte[] response) {
		String responsestr;
		try {
			responsestr = new String(response, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		logger.info("response : {}", responsestr);
		return responsestr;
	}
}
