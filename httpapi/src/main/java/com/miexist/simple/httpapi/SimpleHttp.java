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
package com.miexist.simple.httpapi;

import java.io.IOException;
import java.util.Map;

/**
 * 简单的http请求客户端接口，使用示例如下：
 * <p>
 * SimpleHttp simpleHttp = SimpleHttpFactory.getSimpleHttp();<br/>
 * String urlstring = "http://www.xxxx.com";<br/>
 * SimpleResponse response = simpleHttp.get(urlstring);<br/>
 * if(response.isSuccessful()){<br/>
 * 		String htmlContent = response.getString();<br/>
 * }
 * </p>
 * @author liangruisen
 *
 */
public interface SimpleHttp {

	/**
	 * 异步发送HTTP GET 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param url
	 * @param callback
	 */
	void get(String url, SimpleCallback callback);

	/**
	 * 设置HTTP Header信息并
	 * 异步发送HTTP GET 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param heads
	 * @param url
	 * @param callback
	 */
	void get(Map<String, String> heads, String url, SimpleCallback callback);

	/**
	 * 设置HTTP Header信息并
	 * 异步发送HTTP GET 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param heads
	 * @param url
	 * @param callback
	 */
	void get(Map<String, String> heads, String url, Map<String, String> params, SimpleCallback callback);
	
	/**
	 * 同步发送HTTP GET 请求
	 * @param url
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse get(String url) throws IOException;

	/**
	 * 设置HTTP Header信息并同步发送HTTP GET 请求
	 * @param heads
	 * @param url
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse get(Map<String, String> heads, String url) throws IOException;

	/**
	 * 设置HTTP Header信息并同步发送HTTP GET 请求
	 * @param heads
	 * @param url
	 * @param params
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse get(Map<String, String> heads, String url, Map<String, String> params) throws IOException;
	
	/**
	 * 异步发送HTTP POST 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param url
	 * @param callback
	 */
	void post(String url, SimpleCallback callback);

	/**
	 * 设置HTTP Header信息并
	 * 异步发送HTTP POST 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param heads
	 * @param url
	 * @param callback
	 */
	void post(Map<String, String> heads, String url, SimpleCallback callback);
	
	/**
	 * 同步发送HTTP POST 请求
	 * @param url
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse post(String url) throws IOException;

	/**
	 * 设置HTTP Header信息并同步发送HTTP POST 请求
	 * @param heads
	 * @param url
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse post(Map<String, String> heads, String url) throws IOException;
	
	/**
	 * 异步发送HTTP POST 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param url
	 * @param params
	 * @param callback
	 */
	void post(String url, Map<String, String> params, SimpleCallback callback);

	/**
	 * 设置HTTP Header信息并
	 * 异步发送HTTP POST 请求，当请求返回后掉用SimpleCallback 的 onResponse 方法；
	 * 当请求发生IO异常时调用SimpleCallback 的onFailure方法
	 * @param heads
	 * @param url
	 * @param params
	 * @param callback
	 */
	void post(Map<String, String> heads, String url, Map<String, String> params, SimpleCallback callback);

	/**
	 * 同步发送HTTP POST 请求
	 * @param url
	 * @param params
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse post(String url, Map<String, String> params) throws IOException;
	
	/**
	 * 设置HTTP Header信息并同步发送HTTP POST 请求
	 * @param heads
	 * @param url
	 * @param params
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse post(Map<String, String> heads, String url, Map<String, String> params) throws IOException;
	
	/**
	 * 设置HTTP Header信息并同步发送HTTP 请求
	 * @param heads
	 * @param url
	 * @param method
	 * @param params
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse execute(Map<String, String> heads, String url, String method, Map<String, String> params, FileItem ...fileItems) throws IOException;
	
	/**
	 * 同步执行http请求
	 * @param url
	 * @param method
	 * @param body
	 * @return SimpleResponse
	 * @throws IOException
	 */
	SimpleResponse execute(String url, String method, HttpBody body) throws IOException;
	
	/**
	 * 设置HTTP Header信息并异步发送HTTP 请求
	 * @param heads
	 * @param url
	 * @param method
	 * @param params
	 * @param fileItems
	 * @return SimpleResponse
	 */
	void enqueue(Map<String, String> heads, String url, String method, Map<String, String> params, SimpleCallback callback, FileItem ...fileItems);
	
	/**
	 * 异步执行http请求
	 * @param url
	 * @param method
	 * @param body
	 * @param callback
	 */
	void enqueue(String url, String method, HttpBody body, SimpleCallback callback);
}
