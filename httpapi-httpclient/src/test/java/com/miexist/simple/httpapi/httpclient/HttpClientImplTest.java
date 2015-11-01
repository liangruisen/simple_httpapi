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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.miexist.simple.httpapi.SimpleCallback;
import com.miexist.simple.httpapi.SimpleHttp;
import com.miexist.simple.httpapi.SimpleHttpFactory;
import com.miexist.simple.httpapi.SimpleRequest;
import com.miexist.simple.httpapi.SimpleResponse;

/**
 * HttpClientSimpleHttp Junit Test
 */
public class HttpClientImplTest {

	private String url = "http://localhost:8080/httpapi";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HttpTestServer.getHttpServer().start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		HttpTestServer.getHttpServer().stop();
	}
	
	/**
	 * 测试同步请求方法
	 */
	@Test
	public void testExecute() throws IOException {
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "text/plain, */*; q=0.01");
		heads.put("Accept-Encoding", "gzip, deflate, sdch");
		heads.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		Map<String, String> params = new HashMap<String, String>();
		params.put("wd", "测试");
		SimpleHttp simpleHttp = SimpleHttpFactory.getSimpleHttp();
		SimpleResponse response = simpleHttp.get(url);
		assertResult(response);
		response = simpleHttp.get(heads, url);
		assertResult(response);
		response = simpleHttp.post(url);
		assertResult(response);
		response = simpleHttp.post(heads, url);
		assertResult(response);
		response = simpleHttp.post(heads, url, params);
		assertResult(response);
		response = simpleHttp.execute(heads, url, "PUT", params);
		assertResult(response);
		response = simpleHttp.execute(heads, url, "DELETE", params);
		assertResult(response);
		response = simpleHttp.execute(heads, url, "OPTIONS", params);
		assertResult(response);
		response = simpleHttp.execute(heads, url, "HEAD", params);
		response = simpleHttp.execute(heads, url, "PATCH", params);
		assertResult(response);
		response = simpleHttp.execute(heads, url, "TRACE", params);
		assertResult(response);
	}

	/**
	 * 审核请求是否正确通过
	 * @param response
	 * @throws IOException
	 */
	private void assertResult(SimpleResponse response) throws IOException{
		System.out.println(response.getMessage());
		System.out.println(response.getString());
		assertTrue(response.isSuccessful());
		response.close();
	}
	
	/**
	 * 测试异步请求方法
	 */
	@Test
	public void testEnqueue() {
		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "text/plain, */*; q=0.01");
		heads.put("Accept-Encoding", "gzip, deflate, sdch");
		heads.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
		Map<String, String> params = new HashMap<String, String>();
		params.put("wd", "测试");
		SimpleHttp simpleHttp = SimpleHttpFactory.getSimpleHttp();
		Callback callback = new Callback();
		simpleHttp.get(url, callback);
		simpleHttp.get(heads, url, callback);
		simpleHttp.post(url, callback);
		simpleHttp.post(heads, url, callback);
		simpleHttp.post(heads, url, params, callback);
		simpleHttp.enqueue(heads, url, "PUT", params, callback);
		simpleHttp.enqueue(heads, url, "DELETE", params, callback); 
		simpleHttp.enqueue(heads, url, "OPTIONS", params, callback);
		simpleHttp.enqueue(heads, url, "HEAD", params, callback);
		simpleHttp.enqueue(heads, url, "PATCH", params, callback);
		simpleHttp.enqueue(heads, url, "TRACE", params, callback);
	}

	class Callback implements SimpleCallback {
		@Override
		public void onFailure(SimpleRequest request, IOException e) {
			fail(e.getMessage());
		}

		@Override
		public void onResponse(SimpleResponse response) {
			try {
				assertResult(response);
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}
	}
}
