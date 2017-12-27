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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.miexist.simple.httpapi.FileItem;
import com.miexist.simple.httpapi.HttpBody;
import com.miexist.simple.httpapi.SimpleCallback;
import com.miexist.simple.httpapi.SimpleHttp;
import com.miexist.simple.httpapi.SimpleResponse;
import com.miexist.simple.httpapi.jdk.JdkSimpleRequest;
import com.miexist.simple.httpapi.jdk.JdkSimpleResponse;
import com.miexist.simple.httpapi.util.StringUtils;

/**
 * 简单的http请求客户端接口jdk实现
 * @author liangruisen
 *
 */
public class JdkSimpleHttp implements SimpleHttp {

	private ExecutorService executorService = null;
	
	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void get(String url, SimpleCallback callback) {
		enqueue(null, url, "POST", null, callback);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.util.Map, java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void get(Map<String, String> heads, String url,
			SimpleCallback callback) {
		enqueue(heads, url, "GET", null, callback);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.lang.String)
	 */
	@Override
	public SimpleResponse get(String url) throws IOException {
		return execute(null, url, "GET", null);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#get(java.util.Map, java.lang.String)
	 */
	@Override
	public SimpleResponse get(Map<String, String> heads, String url)
			throws IOException {
		return execute(heads, url, "GET", null);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, SimpleCallback callback) {
		enqueue(null, url, "POST", null, callback);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url,
			SimpleCallback callback) {
		enqueue(heads, url, "POST", null, callback);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String)
	 */
	@Override
	public SimpleResponse post(String url) throws IOException {
		return execute(null, url, "POST", null);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url)
			throws IOException {
		return execute(heads, url, "POST", null);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(String url, Map<String, String> params,
			SimpleCallback callback) {
		enqueue(null, url, "POST", params, callback);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void post(Map<String, String> heads, String url,
			Map<String, String> params, SimpleCallback callback) {
		enqueue(heads, url, "POST", params, callback);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse post(String url, Map<String, String> params)
			throws IOException {
		return execute(null, url, "POST", params);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#post(java.util.Map, java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse post(Map<String, String> heads, String url,
			Map<String, String> params) throws IOException {
		return execute(heads, url, "POST", params);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#execute(java.util.Map, java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public SimpleResponse execute(Map<String, String> heads, String url,
			String method, Map<String, String> params) throws IOException {
		HttpBody body = new JdkHttpBody(heads, params);
		JdkSimpleRequest request = createRequest(url, method.toUpperCase(), body);
		return execute(request);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleHttp#enqueue(java.util.Map, java.lang.String, java.lang.String, java.util.Map, com.miexist.simple.httpapi.SimpleCallback)
	 */
	@Override
	public void enqueue(Map<String, String> heads, String url, String method,
			Map<String, String> params, SimpleCallback callback) {
		HttpBody body = new JdkHttpBody(heads, params);
		enqueue(url, method, body, callback);
	}
	
	private JdkSimpleResponse execute(JdkSimpleRequest request) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) request.getUrl().openConnection();
		JdkSimpleResponse response = new JdkSimpleResponse(request, connection);
		Map<String, List<String>> headers = request.getHeaders();
		if(headers != null){
			for(Map.Entry<String, List<String>> entry : headers.entrySet()){
				String name = entry.getKey();
				List<String> list = entry.getValue();
				for(String value : list){
					connection.addRequestProperty(name, value);
				}
			}
		}
		connection.setReadTimeout(1000*30);
		connection.setConnectTimeout(1000*10);
		connection.setRequestMethod(request.getMethod());
		HttpBody body = request.getBody();
		JdkHttpBody httpBody = null;
		if(body != null){
			if(body instanceof JdkHttpBody) {
				httpBody = (JdkHttpBody)body;
			}else {
				httpBody = new JdkHttpBody(body);
			}
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", "UTF-8");
			connection.setRequestProperty("Content-Type", httpBody.getContentType());
			connection.setRequestProperty("Content-Length", String.valueOf(httpBody.getContentLength()));
		}
		connection.connect();
		if(body != null){
			httpBody.writeTo(connection.getOutputStream());
			connection.getOutputStream().flush();
			connection.getOutputStream().close();
		}
		return response;
	}
	
	private JdkSimpleRequest createRequest(String url, String method, HttpBody body) throws IOException{
		if(body == null) {
			throw new IllegalArgumentException("body is null");
		}
		JdkSimpleRequest request = new JdkSimpleRequest();
		request.setMethod(method);
		if(!isHasBodyMethod(method)){
			url = StringUtils.appendUrlParams(url, body.getParams());
		}else{
			request.setBody(body);
		}
		URL urlobj = new URL(url);
		request.setUrl(urlobj);
		return request;
	}

	private boolean isHasBodyMethod(String method){
		 return method.equals("POST")
			        || method.equals("PATCH")
			        || method.equals("PUT")
			        || method.equals("DELETE");
	}
	
	private class Call implements Runnable {
		public Call(SimpleCallback callback, JdkSimpleRequest request) {
			super();
			this.callback = callback;
			this.request = request;
		}
		private SimpleCallback callback;
		private JdkSimpleRequest request;
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try{
				JdkSimpleResponse response = execute(request);
				callback.onResponse(response);
			}catch(IOException e){
				callback.onFailure(request, e);
			}
		}
	}

	@Override
	public SimpleResponse execute(Map<String, String> heads, String url, String method, Map<String, String> params,
			FileItem... fileItems) throws IOException {
		List<FileItem> fileList = null;
		if(fileItems != null) {
			fileList = Arrays.asList(fileItems);
		}
		HttpBody body = new JdkHttpBody(heads, params, fileList);
		return execute(url, method, body);
	}

	@Override
	public void enqueue(Map<String, String> heads, String url, String method, Map<String, String> params,
			SimpleCallback callback, FileItem... fileItems) {
		List<FileItem> fileList = null;
		if(fileItems != null) {
			fileList = Arrays.asList(fileItems);
		}
		HttpBody body = new JdkHttpBody(heads, params, fileList);
		enqueue(url, method, body, callback);
	}

	@Override
	public SimpleResponse execute(String url, String method, HttpBody body) throws IOException {
		JdkSimpleRequest request = createRequest(url, method, body);
		return execute(request);
	}

	@Override
	public void enqueue(String url, String method, HttpBody body, SimpleCallback callback) {
		JdkSimpleRequest request = null;
		try {
			request = createRequest(url, method.toUpperCase(), body);
			getExecutorService().submit(new Call(callback, request));
		} catch (IOException e) {
			if(callback != null) {
				callback.onFailure(request, e);
			}else {
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	private ExecutorService getExecutorService() {
		if(executorService == null) {
			synchronized (JdkSimpleHttp.class) {
				if(executorService == null) {
					executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
				}
			}
		}
		return executorService;
	}
}
