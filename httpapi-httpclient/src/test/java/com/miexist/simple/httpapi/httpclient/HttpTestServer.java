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

import com.miexist.simple.httpapi.util.IOUtils;
import com.sun.net.httpserver.Headers;  
import com.sun.net.httpserver.HttpExchange;  
import com.sun.net.httpserver.HttpHandler;  
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * 简单的http测试服务器
 * @author liangruisen
 */
@SuppressWarnings("restriction")
public class HttpTestServer {

	private static HttpTestServer httpTestServer = new HttpTestServer();
	
	public static HttpTestServer getHttpServer(){
		return httpTestServer;
	}
	
	private boolean stop = true;
	private HttpServer httpServer;
	
	/**
	 * 启动测试服务器
	 * @throws IOException
	 */
	public void start() throws IOException{
		if(stop){
			InetSocketAddress addr = new InetSocketAddress(8080);
			httpServer = HttpServer.create(addr, 0);
			httpServer.setExecutor(Executors.newCachedThreadPool());
			httpServer.createContext("/httpapi", new MyHandler());
			httpServer.start();
			stop = false;
		}
	}
	
	/**
	 * 停止测试服务器
	 */
	public void stop(){
		if(httpServer != null){
			httpServer.stop(0);
			stop = true;
		}
	}
	
	/**
	 * 请求处理器
	 * @author liangruisen
	 *
	 */
	class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String method = exchange.getRequestMethod();
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/html; charset=utf-8");
			exchange.sendResponseHeaders(200, 0);
			OutputStream out = exchange.getResponseBody();
			out.write("method=".getBytes());
			out.write(method.getBytes());
			out.write("\nheads={".getBytes());
			Headers requestHeaders = exchange.getRequestHeaders();  
            Set<String> keySet = requestHeaders.keySet();  
            Iterator<String> iter = keySet.iterator();  
            while (iter.hasNext()) {  
                String key = iter.next();  
                List<String> values = requestHeaders.get(key);  
                String s = key + " = " + values.toString() + "\n";  
                out.write(s.getBytes());  
            }
            out.write("}\nparams={".getBytes());
            IOUtils.copy(exchange.getRequestBody(), out);
            out.write("}\nhttpapiSuccessful".getBytes());
            out.close();
		}
	}
}
