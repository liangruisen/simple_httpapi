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

import java.lang.reflect.Method;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * HttpClient创建对象工厂，优先考虑使用HttpClientBuilder来创建HttpClient对象
 * @author liangruisen
 *
 */
@SuppressWarnings("deprecation")
public abstract class HttpClientFactory {

	private static Class<?> HTTP_CLIENT_BUILDER_CLASS;
	private static Method HTTP_CLIENT_BUILDER_CLASS_CREATE_METHOD;
	private static Method HTTP_CLIENT_BUILDER_CLASS_BUILDER_METHOD;
	
	static{
		try {
			HTTP_CLIENT_BUILDER_CLASS = Class.forName("org.apache.http.impl.client.HttpClientBuilder");
			HTTP_CLIENT_BUILDER_CLASS_CREATE_METHOD = HTTP_CLIENT_BUILDER_CLASS.getMethod("create");
			HTTP_CLIENT_BUILDER_CLASS_BUILDER_METHOD = HTTP_CLIENT_BUILDER_CLASS.getMethod("build");
		} catch (Exception e) {
		}
	}
	
	/**
	 * 使用org.apache.http.impl.client.HttpClientBuilder来创建HttpClient对象
	 * @return HttpClient
	 */
	private static HttpClient createWhilHttpClientBuilder(){
		try{
			Object builder = HTTP_CLIENT_BUILDER_CLASS_CREATE_METHOD.invoke(HTTP_CLIENT_BUILDER_CLASS);
			return (HttpClient) HTTP_CLIENT_BUILDER_CLASS_BUILDER_METHOD.invoke(builder);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 创建HttpClient对象
	 * @return HttpClient
	 */
	public static HttpClient createHttpClient(){
		if(HTTP_CLIENT_BUILDER_CLASS != null){
			return createWhilHttpClientBuilder();
		}
		return new DefaultHttpClient();
	}
}
