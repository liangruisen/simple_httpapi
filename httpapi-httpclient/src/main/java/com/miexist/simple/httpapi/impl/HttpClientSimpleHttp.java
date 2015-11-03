/**
 * Copyright 2015 the original author or authors.
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
package com.miexist.simple.httpapi.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;

import com.miexist.simple.httpapi.SimpleCallback;
import com.miexist.simple.httpapi.SimpleHttp;
import com.miexist.simple.httpapi.SimpleResponse;
import com.miexist.simple.httpapi.httpclient.HttpClientFactory;
import com.miexist.simple.httpapi.httpclient.HttpClientSimpleRequest;
import com.miexist.simple.httpapi.httpclient.HttpClientSimpleResponse;
import com.miexist.simple.httpapi.httpclient.HttpClientUtils;
import com.miexist.simple.httpapi.util.StringUtils;

/**
 * @author liangruisen
 *
 */
public class HttpClientSimpleHttp implements SimpleHttp {

	private HttpClient httpClient = HttpClientFactory.createHttpClient();

	private ExecutorService executorService = Executors.newFixedThreadPool(3);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.lang.String,
	 * com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void get(String url, SimpleCallback callback) {
		enqueue(null, url, "GET", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.util.Map,
	 * java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void get(Map<String, String> heads, String url,
			SimpleCallback callback) {
		enqueue(heads, url, "GET", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.lang.String)
	 */
	@Override
	public SimpleResponse get(String url) throws IOException {
		return execute(null, url, "GET", null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.util.Map,
	 * java.lang.String)
	 */
	@Override
	public SimpleResponse get(Map<String, String> heads, String url)
			throws IOException {
		return execute(heads, url, "GET", null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String,
	 * com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, SimpleCallback callback) {
		enqueue(null, url, "POST", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map,
	 * java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url,
			SimpleCallback callback) {
		enqueue(heads, url, "POST", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String)
	 */
	@Override
	public SimpleResponse post(String url) throws IOException {
		return execute(null, url, "POST", null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map,
	 * java.lang.String)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url)
			throws IOException {
		return execute(heads, url, "POST", null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String,
	 * java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, Map<String, String> params,
			SimpleCallback callback) {
		enqueue(null, url, "POST", params, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map,
	 * java.lang.String, java.util.Map,
	 * com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url,
			Map<String, String> params, SimpleCallback callback) {
		enqueue(heads, url, "POST", params, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public SimpleResponse post(String url, Map<String, String> params)
			throws IOException {
		return execute(null, url, "POST", params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map,
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url,
			Map<String, String> params) throws IOException {
		return execute(heads, url, "POST", params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#execute(java.util.Map,
	 * java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse execute(Map<String, String> heads, String url,
			String method, Map<String, String> params) throws IOException {
		HttpUriRequest request = createRequest(url, method, params);
		HttpClientUtils.modifyRequestToAcceptGzipResponse(request);
		HttpClientUtils.addHeaders(request, heads);
		HttpResponse response = httpClient.execute(request);
		return new HttpClientSimpleResponse(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#enqueue(java.util.Map,
	 * java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public void enqueue(Map<String, String> heads, String url, String method,
			Map<String, String> params, SimpleCallback callback) {
		HttpUriRequest request = createRequest(url, method, params);
		HttpClientUtils.modifyRequestToAcceptGzipResponse(request);
		HttpClientUtils.addHeaders(request, heads);
		executorService.submit(new Call(request, callback));
	}

	/**
	 * 根据HTTP方法名称method参数创建HttpUriRequest并设置请求参数
	 * @param url
	 * @param method
	 * @param params
	 * @return HttpUriRequest
	 * @throws IOException 
	 */
	private HttpUriRequest createRequest(String url, String method,
			Map<String, String> params) {
		if ("GET".equalsIgnoreCase(method)) {
			return new HttpGet(StringUtils.appendUrlParams(url, params));
		}
		if ("HEAD".equalsIgnoreCase(method)) {
			return new HttpHead(StringUtils.appendUrlParams(url, params));
		}
		if ("OPTIONS".equalsIgnoreCase(method)) {
			return new HttpOptions(StringUtils.appendUrlParams(url, params));
		}
		if ("DELETE".equalsIgnoreCase(method)) {
			return new HttpDelete(StringUtils.appendUrlParams(url, params));
		}
		if ("TRACE".equalsIgnoreCase(method)) {
			return new HttpTrace(StringUtils.appendUrlParams(url, params));
		}
		HttpEntityEnclosingRequestBase request;
		if ("PUT".equalsIgnoreCase(method)) {
			request = new HttpPut(url);
		}else{
			request = new HttpPost(url);
		}
		HttpClientUtils.setParams(request, params);
		return request;
	}

	private class Call implements Runnable {
		public Call(HttpUriRequest request, SimpleCallback callback) {
			super();
			this.request = request;
			this.callback = callback;
		}

		private HttpUriRequest request;
		private SimpleCallback callback;

		@Override
		public void run() {
			try {
				HttpResponse response = httpClient.execute(request);
				callback.onResponse(new HttpClientSimpleResponse(request,
						response));
			} catch (IOException e) {
				callback.onFailure(new HttpClientSimpleRequest(request), e);
			}
		}
	}
}
