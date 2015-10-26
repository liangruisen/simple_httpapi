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
package com.miexist.simple.httpapi.okhttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miexist.simple.httpapi.SimpleResponse;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 简单的HTTP请求返回结果接口okhttp实现
 * 
 * @author liangruisen
 *
 */
public class OkHttpSimpleResponse implements SimpleResponse {

	public OkHttpSimpleResponse(Request request, Response response) {
		super();
		this.request = request;
		this.response = response;
	}

	private Request request;

	private Response response;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getResponse()
	 */
	@Override
	public Object getResponse() {
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getString()
	 */
	@Override
	public String getString() throws IOException {
		return response.body().string();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getBytes()
	 */
	@Override
	public byte[] getBytes() throws IOException {
		return response.body().bytes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return response.body().byteStream();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getCode()
	 */
	@Override
	public int getCode() {
		return response.code();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getRequest()
	 */
	@Override
	public Object getRequest() {
		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getMessage()
	 */
	@Override
	public String getMessage() {
		return response.message();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.miexist.simple.httpapi.SimpleResponse#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return response.header(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.miexist.simple.httpapi.SimpleResponse#getHeaders(java.lang.String)
	 */
	@Override
	public List<String> getHeaders(String name) {
		return response.headers(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		Headers headers = response.headers();
		if (headers == null || headers.size() <= 0) {
			return Collections.emptyMap();
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String name : headers.names()) {
			map.put(name, headers.values(name));
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#isSuccessful()
	 */
	@Override
	public boolean isSuccessful() {
		return response.isSuccessful();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleResponse#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return response.protocol().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		
	}

}
