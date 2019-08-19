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

import com.miexist.simple.httpapi.FileItem;
import com.miexist.simple.httpapi.HttpBody;
import com.miexist.simple.httpapi.SimpleCallback;
import com.miexist.simple.httpapi.SimpleHttp;
import com.miexist.simple.httpapi.SimpleResponse;
import com.miexist.simple.httpapi.okhttp.OkHttpCallback;
import com.miexist.simple.httpapi.okhttp.OkHttpSimpleResponse;
import com.miexist.simple.httpapi.util.StringUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpMethod;

import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 简单的http请求客户端接口okhttp实现
 * 
 * @author liangruisen
 *
 */
public class OkHttpSimpleHttp implements SimpleHttp {

	private OkHttpClient okHttpClient = new OkHttpClient();

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
	public void get(Map<String, String> heads, String url, SimpleCallback callback) {
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
	public SimpleResponse get(Map<String, String> heads, String url) throws IOException {
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
	public void post(Map<String, String> heads, String url, SimpleCallback callback) {
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
	public SimpleResponse post(Map<String, String> heads, String url) throws IOException {
		return execute(heads, url, "POST", null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String,
	 * java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, Map<String, String> params, SimpleCallback callback) {
		enqueue(null, url, "POST", params, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map,
	 * java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url, Map<String, String> params, SimpleCallback callback) {
		enqueue(heads, url, "POST", params, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public SimpleResponse post(String url, Map<String, String> params) throws IOException {
		return execute(null, url, "POST", params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map,
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url, Map<String, String> params) throws IOException {
		return execute(heads, url, "POST", params);
	}

	/**
	 * 将请求信息拼装成okhttp的请求Request对象
	 * 
	 * @param heads
	 * @param url
	 * @param method
	 * @param params
	 * @param fileItems
	 * @return Request
	 */
	private Request buildRequest(Map<String, String> heads, String url, String method, Map<String, String> params, FileItem[] fileItems) {
		Request.Builder builder = new Request.Builder();
		if (!HttpMethod.permitsRequestBody(method)) {
			builder.url(StringUtils.appendUrlParams(url, params));
			builder.method(method.toUpperCase(), null);
		} else {
			builder.url(url);
			builder.method(method.toUpperCase(), parseParams(params, fileItems));
		}
		if (heads != null) {
			for (Map.Entry<String, String> entry : heads.entrySet()) {
				builder.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return builder.build();
	}

	/**
	 * 将请求参数进行编码
	 * 
	 * @param params
	 * @return RequestBody
	 */
	private RequestBody parseParams(Map<String, String> params, FileItem[] fileItems) {
		if (fileItems == null || fileItems.length <= 0) {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			if (params == null || params.isEmpty()) {
				return builder.build();
			}
			for (Map.Entry<String, String> entry : params.entrySet()) {
				builder.add(entry.getKey(), entry.getValue());
			}
			return builder.build();
		} else {
			MultipartBuilder builder = new MultipartBuilder();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					builder.addFormDataPart(entry.getKey(), entry.getValue());
				}
			}
			for (FileItem item : fileItems) {
				MediaType type = MediaType.parse(item.getContentType());
				RequestBody body = new FileItemRequestBody(type, item);
				builder.addFormDataPart(item.getFieldName(), item.getName(), body);
			}
			return builder.build();
		}
	}

	@Override
	public SimpleResponse execute(Map<String, String> heads, String url, String method, Map<String, String> params,
			FileItem... fileItems) throws IOException {
		Request request = buildRequest(heads, url, method, params, fileItems);
		Call call = okHttpClient.newCall(request);
		return new OkHttpSimpleResponse(request, call.execute());
	}

	@Override
	public SimpleResponse execute(String url, String method, HttpBody body) throws IOException {
		return execute(body.getHeaders(), url, method, body.getParams(), body.getFileItems());
	}

	@Override
	public void enqueue(Map<String, String> heads, String url, String method, Map<String, String> params,
			SimpleCallback callback, FileItem... fileItems) {
		Request request = buildRequest(heads, url, method, params, fileItems);
		Call call = okHttpClient.newCall(request);
		call.enqueue(new OkHttpCallback(callback));
	}

	@Override
	public void enqueue(String url, String method, HttpBody body, SimpleCallback callback) {
		enqueue(body.getHeaders(), url, method, body.getParams(), callback, body.getFileItems());
	}

	@Override
	public void get(Map<String, String> heads, String url, Map<String, String> params, SimpleCallback callback) {
		enqueue(heads, url, "GET", params, callback);
	}

	@Override
	public SimpleResponse get(Map<String, String> heads, String url, Map<String, String> params) throws IOException {
		return execute(heads, url, "GET", params);
	}
	
	class FileItemRequestBody extends RequestBody {

		private final MediaType medialType;
		private final FileItem fileItem;

		public FileItemRequestBody(MediaType medialType, FileItem fileItem) {
			super();
			this.medialType = medialType;
			this.fileItem = fileItem;
		}

		@Override
		public MediaType contentType() {
			return medialType;
		}

		@Override
		public void writeTo(BufferedSink sink) throws IOException {
			Source source = null;
			try {
				source = Okio.source(fileItem.getInputStream());
				sink.writeAll(source);
			} finally {
				Util.closeQuietly(source);
			}
		}
	}
}
