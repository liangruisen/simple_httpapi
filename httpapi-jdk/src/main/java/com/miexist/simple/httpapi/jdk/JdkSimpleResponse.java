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
package com.miexist.simple.httpapi.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import com.miexist.simple.httpapi.SimpleResponse;
import com.miexist.simple.httpapi.util.IOUtils;
import com.miexist.simple.httpapi.util.StringUtils;

/**
 * @author liangruisen
 *
 */
public class JdkSimpleResponse implements SimpleResponse {

	public JdkSimpleResponse(JdkSimpleRequest request,
			HttpURLConnection connection) {
		super();
		this.request = request;
		this.connection = connection;
	}

	private JdkSimpleRequest request;
	
	private HttpURLConnection connection;
	
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if(connection != null){
			connection.disconnect();
		}
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getResponse()
	 */
	@Override
	public Object getResponse() {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getString()
	 */
	@Override
	public String getString() throws IOException {
		return IOUtils.toString(getInputStream(), "UTF-8");
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getBytes()
	 */
	@Override
	public byte[] getBytes() throws IOException {
		return IOUtils.toBytes(getInputStream());
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		String contentEncoding = connection.getContentEncoding();
		if(StringUtils.hasText(contentEncoding) && contentEncoding.toLowerCase().indexOf("gzip") >= 0){
			return new GZIPInputStream(connection.getInputStream());
		}
		return connection.getInputStream();
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getCode()
	 */
	@Override
	public int getCode() {
		try {
			return connection.getResponseCode();
		} catch (IOException e) {
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getRequest()
	 */
	@Override
	public Object getRequest() {
		return request;
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getMessage()
	 */
	@Override
	public String getMessage() {
		try {
			return connection.getResponseMessage();
		} catch (IOException e) {
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		return connection.getHeaderField(name);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeaders(java.lang.String)
	 */
	@Override
	public List<String> getHeaders(String name) {
		return getHeaders().get(name);
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getHeaders()
	 */
	@Override
	public Map<String, List<String>> getHeaders() {
		return connection.getHeaderFields();
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#isSuccessful()
	 */
	@Override
	public boolean isSuccessful() {
		int code = getCode();
		return code >= 200 && code < 300;
	}

	/* (non-Javadoc)
	 * @see com.miexist.simple.httpapi.SimpleResponse#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return request.getUrl().getProtocol();
	}

}
