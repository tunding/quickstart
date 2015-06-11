/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springside.fi.common.httpclient;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.fi.common.SystemGlobal;

/**
 * Template for easier use of <code>HttpClient</code>.
 * 
 * @author David Winterfeldt
 */
public class HttpClientTemplate extends AbstractHttpClientTemplate<RequestEntity> {

    final Logger logger = LoggerFactory.getLogger(HttpClientTemplate.class);
    private final String appKey = SystemGlobal.getConfig("App-Key");
    private final String appSecret = SystemGlobal.getConfig("App-Secret");

    /**
     * Constructor.
     */
    public HttpClientTemplate() {}

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     */
    public HttpClientTemplate(String defaultUri) {
        super(defaultUri, false);
    }

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     * @param   init            Whether or not to initialize the bean.
     */
    public HttpClientTemplate(String defaultUri, boolean init) {
        super(defaultUri, init);
    }

    /**
     * Execute post method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   requestPayload  <code>RequestEntity</code> data to post.
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.
     */
    public byte[] executePostMethod(String uri,  
                                  RequestEntity requestPayload, Map<String, String> hParams) {
        PostMethod post = new PostMethod(uri);
        
        //添加数字签名
		String nonce = getRandom();
		String timestamp = String.valueOf(new Date().getTime()/1000);
		byte[] sign = DigestUtils.sha1(appSecret+nonce+timestamp);
		String signature = "";
		for (int i=0; i < sign.length; i++) {
			signature +=
					Integer.toString( ( sign[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		post.addRequestHeader("App-Key", appKey);
		post.addRequestHeader("Nonce", nonce);
		post.addRequestHeader("Timestamp", timestamp);
		post.addRequestHeader("Signature", signature);
		
        if (requestPayload != null) {
            post.setRequestEntity(requestPayload);
        }
        
        processHttpMethodParams(post, hParams);
        
        return processHttpMethod(post);
    }

    /**
     * Processes <code>HttpMethod</code> by executing the method, 
     * validating the response, and calling the callback.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to process.
     * @param   callback        Callback with HTTP method's response.
     */
    protected byte[] processHttpMethod(HttpMethod httpMethod) {
        try {
        	client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
        	client.getHttpConnectionManager().getParams().setSoTimeout(60000);
            client.executeMethod(httpMethod);
            
            validateResponse(httpMethod);
            
            return httpMethod.getResponseBody();
        } catch (HttpException e) {
            throw new HttpAccessException(e.getMessage(), e, httpMethod.getStatusCode());
        } catch (IOException e) {
            throw new HttpAccessException(e.getMessage(), e);
        } finally {
            httpMethod.releaseConnection();
        }
    }
    
	protected String getRandom(){
		Random r=new Random();
		return String.valueOf(r.nextInt(Integer.MAX_VALUE));
	}
    
}
