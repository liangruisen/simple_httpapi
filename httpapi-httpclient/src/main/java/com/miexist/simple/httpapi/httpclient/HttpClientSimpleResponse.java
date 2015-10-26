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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import com.miexist.simple.httpapi.SimpleResponse;
import com.miexist.simple.httpapi.util.IOUtils;
import com.miexist.simple.httpapi.util.StringUtils;

/**
 * 简单的HTTP请求返回结果接口HttpClient实现，用于获取该次请求的信息
 * @author liangruisen
 *
 */
public class HttpClientSimpleResponse implements SimpleResponse {

	public HttpClientSimpleResponse(HttpRequest request, HttpResponse response) {
		super();
		this.request = request;
		this.response = response;
	}

	private HttpResponse response;
	
	private HttpRequest request;
	
	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getResponse()
	 */
	@Override
	public Object getResponse() {
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getString()
	 */
	@Override
	public String getString() throws IOException {
		return HttpClientUtils.getStringContent(response.getEntity());
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getBytes()
	 */
	@Override
	public byte[] getBytes() throws IOException {
		return IOUtils.toBytes(HttpClientUtils.getUngzippedContent(response.getEntity()));
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return HttpClientUtils.getUngzippedContent(response.getEntity());
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getCode()
	 */
	@Override
	public int getCode() {
		return response.getStatusLine().getStatusCode();
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getRequest()
	 */
	@Override
	public Object getRequest() {
		return request;
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getMessage()
	 */
	@Override
	public String getMessage() {
		return response.getStatusLine().toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeader(java.lang.String)
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
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeaders(java.lang.String)
	 */
	@Override
	public List<String> getHeaders(String name) {
		Header[] headers = response.getHeaders(name);
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
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		Header[] headers = response.getAllHeaders();
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

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#isSuccessful()
	 */
	@Override
	public boolean isSuccessful() {
		return getCode() >= 200 && getCode() < 300;
	}

	/*
	 * (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getProtocol()
	 */
	@Override
	public String getProtocol(){
		return response.getProtocolVersion().toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		HttpClientUtils.closeQuietly(response);
	}
}
