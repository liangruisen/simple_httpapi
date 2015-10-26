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
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miexist.simple.httpapi.SimpleRequest;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;

/**
 * 简单的HTTP请求接口okhttp实现
 * @author liangruisen
 *
 */
public class OkHttpSimpleRequest implements SimpleRequest {

	public OkHttpSimpleRequest(Request request) {
		super();
		this.request = request;
	}

	private Request request;
	
	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getRequest()
	 */
	@Override
	public Object getRequest() {
		return request;
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getURI()
	 */
	@Override
	public URI getURI() {
		try {
			return request.uri();
		} catch (IOException e) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getMethod()
	 */
	@Override
	public String getMethod() {
		return request.method();
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return request.header(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getHeaders(java.lang.String)
	 */
	@Override
	public List<String> getHeaders(String name) {
		return request.headers(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		Headers headers = request.headers();
		if(headers == null || headers.size() <= 0){
			return Collections.emptyMap();
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for(String name : headers.names()){
			map.put(name, headers.values(name));
		}
		return map;
	}
}
