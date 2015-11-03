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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import com.miexist.simple.httpapi.util.IOUtils;

/**
 * HttpClient的一些工具方法类
 * @author liangruisen
 *
 */
public abstract class HttpClientUtils {

	private static HttpClient httpClient;
	
	/**
	 * 获取单例的HttpClient，支持通过调用setHttpClient来设置httpClient单例对象
	 * @return HttpClient
	 */
	public static HttpClient getHttpClient(){
		if(httpClient == null){
			synchronized (HttpClientUtils.class) {
				if(httpClient == null){
					httpClient = HttpClientFactory.createHttpClient();
				}
			}
		}
		return httpClient;
	}
	
	/**
	 * 设置httpClient单例对象
	 * @param httpClient
	 */
	public static void setHttpClient(HttpClient httpClient){
		HttpClientUtils.httpClient = httpClient;
	}
	
	/**
	 * Modifies a request to indicate to the server that we would like a gzipped
	 * response. (Uses the "Accept-Encoding" HTTP header.)
	 * 
	 * @param request
	 *            the request to modify
	 * @see #getUngzippedContent
	 */
	public static void modifyRequestToAcceptGzipResponse(HttpRequest request) {
		request.addHeader("Accept-Encoding", "gzip");
	}
	
	/**
	 * 添加http头信息
	 * @param request
	 * @param headers
	 */
	public static void addHeaders(HttpRequest request, Map<String, String> headers){
		if(headers != null && !headers.isEmpty()){
			for(Map.Entry<String, String> entry : headers.entrySet()){
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * 设置HTTP请求的表单参数
	 * @param request
	 * @param params
	 * @throws IOException 
	 */
	public static void setParams(HttpEntityEnclosingRequest request, Map<String, String> params){
		if(params != null && !params.isEmpty()){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for(Map.Entry<String, String> entry : params.entrySet()){
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			try {
				request.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
	}
	
	/**
	 * 从返回结果实体中获取内容字符串
	 * @param entity
	 * @return String
	 * @throws IOException
	 */
	public static String getStringContent(HttpEntity entity) throws IOException {
		InputStream input = getUngzippedContent(entity);
		return IOUtils.toString(input, "UTF-8");
	}

	/**
	 * Gets the input stream from a response entity. If the entity is gzipped
	 * then this will get a stream over the uncompressed data.
	 *
	 * @param entity
	 *            the entity whose content should be read
	 * @return the input stream to read from
	 * @throws IOException
	 */
	public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
		InputStream responseStream = entity.getContent();
		if (responseStream == null) {
			return responseStream;
		}
		Header header = entity.getContentEncoding();
		if (header == null) {
			return responseStream;
		}
		String contentEncoding = header.getValue();
		if (contentEncoding == null) {
			return responseStream;
		}
		if (contentEncoding.toLowerCase().contains("gzip")) {
			responseStream = new GZIPInputStream(responseStream);
		}
		return responseStream;
	}
	
	/**
	 * 忽略错误关闭HttpResponse
	 * @param response
	 */
	public static void closeQuietly(HttpResponse response){
		if (response != null) {
			try{
                try {
                	HttpEntity entity = response.getEntity();
                	if(entity != null){
                		if (entity.isStreaming()) {
                            InputStream instream = entity.getContent();
                            if (instream != null) {
                                instream.close();
                            }
                        }
                	}
                } finally{
                	if(response instanceof Closeable){
                		((Closeable)response).close();
                	}
                }
			 } catch (final IOException ignore) {
	         }
        }
	}
	
	/**
	 * 忽略错误关闭HttpClient
	 * @param httpClient
	 */
	public static void closeQuietly(HttpClient httpClient){
		if(httpClient != null){
			if (httpClient instanceof Closeable) {
                try {
                    ((Closeable) httpClient).close();
                } catch (final IOException ignore) {
                }
            }
		}
	}
}
