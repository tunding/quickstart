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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Base <code>HttpClient</code> template class.
 * 
 * @author David Winterfeldt
 */
public abstract class AbstractHttpClientTemplate<T> implements InitializingBean, DisposableBean {

    protected HttpClient client = null;
    protected HttpConnectionManager connectionManager = null;
    protected String defaultUri = null;
    protected boolean authenticationPreemptive = false;

    /**
     * Constructor.
     */
    public AbstractHttpClientTemplate() {}

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     */
    public AbstractHttpClientTemplate(String defaultUri) {
        this(defaultUri, false);
    }

    /**
     * Constructor.
     * 
     * @param   defaultUri      Default uri.
     * @param   init            Whether or not to initialize the bean 
     *                          (typically for programatic use).
     */
    public AbstractHttpClientTemplate(String defaultUri, boolean init) {
        this.defaultUri = defaultUri;
        
        if (init) {
            try {
                afterPropertiesSet();
            } catch(Exception e) {
                throw new HttpAccessException(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Gets http client.
     */
    public HttpClient getClient() {
        return client;
    }

    /**
     * Sets http client.
     */
    public void setClient(HttpClient client) {
        this.client = client;
    }

    /**
     * Gets connection manager.
     */
    public HttpConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * Sets connection manager.
     */
    public void setConnectionManager(HttpConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Gets default uri.
     */
    public String getDefaultUri() {
        return defaultUri;
    }

    /**
     * Sets default uri.
     */
    public void setDefaultUri(String defaultUri) {
        this.defaultUri = defaultUri;
    }

    /**
     * Whether or not authentication is preemptive.
     * If <code>true</code>, authentication credentials 
     * will be sent before a challenge is issued 
     * for an authentication scope with credentials.
     * Defaults to <code>false</code>.
     */
    public boolean isAuthenticationPreemptive() {
        return authenticationPreemptive;
    }

    /**
     * Sets whether or not authentication is preemptive.
     * If <code>true</code>, authentication credentials 
     * will be sent before a challenge is issued 
     * for an authentication scope with credentials.
     * Defaults to <code>false</code>.
     */
    public void setAuthenticationPreemptive(boolean authenticationPreemptive) {
        this.authenticationPreemptive = authenticationPreemptive;
    }

    
    /**
     * Implementation of <code>InitializingBean</code> 
     * that initializes the <code>HttpClient</code> if it is <code>null</code> 
     * and also sets the connection manager to <code>MultiThreadedHttpConnectionManager</code> 
     * if it is <code>null</code> while initializing the <code>HttpClient</code>.
     */
    public void afterPropertiesSet() throws Exception {
        
        if (client == null) {
            if (connectionManager == null) {
                connectionManager = new MultiThreadedHttpConnectionManager();
            }

            client = new HttpClient(connectionManager);
        }

        client.getParams().setAuthenticationPreemptive(authenticationPreemptive);
    }

    /**
     * Implementation of <code>DisposableBean</code> that 
     * shuts down the connection manager if it is an instance of 
     * <code>MultiThreadedHttpConnectionManager</code>.
     */
    public void destroy() throws Exception {
        if (client.getHttpConnectionManager() instanceof MultiThreadedHttpConnectionManager) {
            ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
        }
    }

    /**
     * Execute get method.
     */
    public byte[] executeGetMethod() {
        return executeGetMethod(defaultUri, null);
    }
    /**
     * Execute get method.
     * 
     * @param   hParams         Parameters for the HTTP get.
     * @param   callback        Callback with HTTP method's response.
     */
    public byte[] executeGetMethod(Map<String, String> hParams) {
        return executeGetMethod(defaultUri, hParams);
    }
    
    /**
     * Execute get method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   hParams         Parameters for the HTTP get.
     * @param   callback        Callback with HTTP method's response.
     */
    public byte[] executeGetMethod(String uri, Map<String, String> hParams) {
        GetMethod get = new GetMethod(uri);

        processHttpMethodParams(get, hParams);
        
        return processHttpMethod(get);
    }

    /**
     * Execute post method.
     */
    public byte[] executePostMethod() {
        return executePostMethod(defaultUri, null, null);
    }

    /**
     * Execute post method.
     * 
     * @param   hParams         Parameters for the HTTP post.
     */
    public byte[] executePostMethod(Map<String, String> hParams) {
        return executePostMethod(defaultUri, null, hParams);
    }

    /**
     * Execute post method.
     * 
     * @param   uri             URI to use when processing this HTTP request instead 
     *                          of using the default URI.
     * @param   requestPayload  Request data to post.
     * @param   hParams         Parameters for the HTTP post.
     * @param   callback        Callback with HTTP method's response.

     */
    public abstract byte[] executePostMethod(String uri,
                                           T requestPayload, Map<String, String> hParams);
    
    /**
     * Processes <code>HttpMethod</code> by executing the method, 
     * validating the response, and calling the callback.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to process.
     * @param   callback        Callback with HTTP method's response.
     */
    protected abstract byte[] processHttpMethod(HttpMethod httpMethod);

    /**
     * Processes <code>HttpMethod</code> parameters.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to process.
     * @param   hParams         Parameters for the HTTP get.
     */
    protected void processHttpMethodParams(HttpMethod httpMethod, Map<String, String> hParams) {
        if (hParams != null) {
            List<NameValuePair> lParams = new ArrayList<NameValuePair>();
            
            for (Object key : hParams.keySet()) {
                Object value = hParams.get(key);
                
                lParams.add(new NameValuePair(key.toString(), value.toString()));
            }

            if (httpMethod instanceof GetMethod) {
                ((GetMethod)httpMethod).setQueryString(lParams.toArray(new NameValuePair[] {}));
            } else if (httpMethod instanceof PostMethod) {
                ((PostMethod)httpMethod).setRequestBody(lParams.toArray(new NameValuePair[] {}));
            }
        }
    }

    /**
     * Validate response.
     * 
     * @param   httpMethod      <code>HttpMethod</code> to validate.
     */
    protected void validateResponse(HttpMethod httpMethod) {
        if (httpMethod.getStatusCode() >= 300) {
            throw new HttpAccessException(
                    "Did not receive successful HTTP response: status code = " + 
                    httpMethod.getStatusCode() + 
                    ", status message = [" + httpMethod.getStatusText() + "]", httpMethod.getStatusCode());
        }
    }
    
}
