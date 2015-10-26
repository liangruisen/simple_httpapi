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

import java.util.ServiceLoader;

/**
 * 简单的http请求客户端接口单例工厂类，用于获取单例的SimpleHttp接口
 * @author liangruisen
 *
 */
public class SimpleHttpFactory {

	private static SimpleHttp simpleHttp;
	
	/**
	 * 获取SimpleHttp接口实例对象
	 * @return SimpleHttp
	 */
	public static SimpleHttp getSimpleHttp(){
		if(simpleHttp == null){
			synchronized (SimpleHttpFactory.class) {
				if(simpleHttp == null){
					simpleHttp = newInstance();
				}
			}
		}
		return simpleHttp;
	}
	
	/**
	 * 用于设置该工厂的SimpleHttp实例
	 * @param simpleHttp
	 */
	public static void setSimpleHttp(SimpleHttp simpleHttp){
		SimpleHttpFactory.simpleHttp = simpleHttp;
	}
	
	/**
	 * 创建SimpleHttp实例对象
	 * @return SimpleHttp
	 */
	private static SimpleHttp newInstance(){
		ServiceLoader<SimpleHttp> loader = ServiceLoader.load(SimpleHttp.class);
		for(SimpleHttp http : loader){
			return http;
		}
		try{
			Class.forName("com.squareup.okhttp.OkHttpClient");
			return (SimpleHttp)Class.forName("com.miexist.simple.httpapi.impl.OkHttpSimpleHttp").newInstance();
		}catch(Exception e){
		}
		try{
			Class.forName("org.apache.http.client.HttpClient");
			return (SimpleHttp)Class.forName("com.miexist.simple.httpapi.impl.HttpClientSimpleHttp").newInstance();
		}catch(Exception e){
		}
		try{
			return (SimpleHttp)Class.forName("com.miexist.simple.httpapi.impl.JdkSimpleHttp").newInstance();
		}catch(Exception e){
		}
		throw new UnsupportedOperationException("not SimpleHttp interface implement Class");
	}
}
