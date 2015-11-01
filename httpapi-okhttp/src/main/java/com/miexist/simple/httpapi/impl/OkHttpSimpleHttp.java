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

import com.miexist.simple.httpapi.SimpleCallback;
import com.miexist.simple.httpapi.SimpleHttp;
import com.miexist.simple.httpapi.SimpleResponse;
import com.miexist.simple.httpapi.okhttp.OkHttpCallback;
import com.miexist.simple.httpapi.okhttp.OkHttpSimpleResponse;
import com.miexist.simple.httpapi.util.StringUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.http.HttpMethod;

/**
 * 简单的http请求客户端接口okhttp实现
 * @author liangruisen
 *
 */
public class OkHttpSimpleHttp implements SimpleHttp {
	
	private OkHttpClient okHttpClient = new OkHttpClient();
	
	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void get(String url, SimpleCallback callback) {
		enqueue(null, url, "GET", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.util.Map, java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void get(Map<String, String> heads, String url,
			SimpleCallback callback) {
		enqueue(heads, url, "GET", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.lang.String)
	 */
	@Override
	public SimpleResponse get(String url) throws IOException {
		return execute(null, url, "GET", null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.util.Map, java.lang.String)
	 */
	@Override
	public SimpleResponse get(Map<String, String> heads, String url)
			throws IOException {
		return execute(heads, url, "GET", null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, SimpleCallback callback) {
		enqueue(null, url, "POST", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url,
			SimpleCallback callback) {
		enqueue(heads, url, "POST", null, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String)
	 */
	@Override
	public SimpleResponse post(String url) throws IOException {
		return execute(null, url, "POST", null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url)
			throws IOException {
		return execute(heads, url, "POST", null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, Map<String, String> params,
			SimpleCallback callback) {
		enqueue(null, url, "POST", params, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url,
			Map<String, String> params, SimpleCallback callback) {
		enqueue(heads, url, "POST", params, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse post(String url, Map<String, String> params)
			throws IOException {
		return execute(null, url, "POST", params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url,
			Map<String, String> params) throws IOException {
		return execute(heads, url, "POST", params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#execute(java.util.Map, java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse execute(Map<String, String> heads, String url,
			String method, Map<String, String> params) throws IOException {
		Request request = buildRequest(heads, url, method, params);
		Call call = okHttpClient.newCall(request);
		return new OkHttpSimpleResponse(request, call.execute());
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#enqueue(java.util.Map, java.lang.String, java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void enqueue(Map<String, String> heads, String url, String method,
			Map<String, String> params, SimpleCallback callback) {
		Request request = buildRequest(heads, url, method, params);
		Call call = okHttpClient.newCall(request);
		call.enqueue(new OkHttpCallback(callback));
	}

	/**
	 * 将请求信息拼装成okhttp的请求Request对象
	 * @param heads
	 * @param url
	 * @param method
	 * @param params
	 * @return Request
	 */
	private Request buildRequest(Map<String, String> heads, String url, String method, Map<String, String> params){
		Request.Builder builder = new Request.Builder();
		if(!HttpMethod.permitsRequestBody(method)){
			builder.url(StringUtils.appendUrlParams(url, params));
			builder.method(method.toUpperCase(), null);
		}else{
			builder.url(url);
			builder.method(method.toUpperCase(), parseParams(params));
		}
		if(heads == null || heads.isEmpty()){
			return builder.build();
		}
		for(Map.Entry<String, String> entry : heads.entrySet()){
			builder.addHeader(entry.getKey(), entry.getValue());
		}
		return builder.build();
	}
	
	/**
	 * 将请求参数进行编码
	 * @param params
	 * @return RequestBody
	 */
	private RequestBody parseParams(Map<String, String> params){
		FormEncodingBuilder builder = new FormEncodingBuilder();
		if(params == null || params.isEmpty()){
			return builder.build();
		}
		for(Map.Entry<String, String> entry : params.entrySet()){
			builder.add(entry.getKey(), entry.getValue());
		}
		return builder.build();
	}
}
