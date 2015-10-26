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
package com.miexist.simple.httpapi.httpclient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpUriRequest;

import com.miexist.simple.httpapi.SimpleRequest;
import com.miexist.simple.httpapi.util.StringUtils;

/**
 * 简单的HTTP请求接口HttpClient实现，用于获取该次请求的信息
 * @author liangruisen
 *
 */
public class HttpClientSimpleRequest implements SimpleRequest {

	public HttpClientSimpleRequest(HttpUriRequest request) {
		super();
		this.request = request;
	}

	private HttpUriRequest request;
	
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
		return request.getURI();
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getMethod()
	 */
	@Override
	public String getMethod() {
		return request.getMethod();
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		List<String> list = getHeaders(name);
		if(list == null || list.isEmpty()){
			return "";
		}
		return StringUtils.join(list, ";");
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getHeaders(java.lang.String)
	 */
	@Override
	public List<String> getHeaders(String name) {
		Header[] headers = request.getHeaders(name);
		if(headers == null || headers.length <= 0){
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<String>(headers.length);
		for(Header header : headers){
			list.add(header.getValue());
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleRequest#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		Header[] headers = request.getAllHeaders();
		if(headers == null || headers.length <= 0){
			return Collections.emptyMap();
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for(Header header : headers){
			List<String> list = map.get(header.getName());
			if(list == null){
				list = new ArrayList<String>();
				map.put(header.getName(), list);
			}
			list.add(header.getName());
		}
		return map;
	}
}
