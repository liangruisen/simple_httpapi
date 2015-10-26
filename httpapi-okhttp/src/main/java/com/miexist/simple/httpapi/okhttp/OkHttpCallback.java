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
package com.miexist.simple.httpapi.okhttp;

import java.io.IOException;

import com.miexist.simple.httpapi.SimpleCallback;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * okhttp异步请求的回调接口httpapi回调实现
 * @author liangruisen
 *
 */
public class OkHttpCallback implements Callback {

	public OkHttpCallback(SimpleCallback callback) {
		super();
		this.callback = callback;
	}

	private SimpleCallback callback;
	
	/* (non-Javadoc)
	 * @see com.squareup.okhttp.Callback#onFailure(com.squareup.okhttp.Request, java.io.IOException)
	 */
	@Override
	public void onFailure(Request request, IOException e) {
		callback.onFailure(new OkHttpSimpleRequest(request), e);
	}

	/* (non-Javadoc)
	 * @see com.squareup.okhttp.Callback#onResponse(com.squareup.okhttp.Response)
	 */
	@Override
	public void onResponse(Response response) throws IOException {
		callback.onResponse(new OkHttpSimpleResponse(response.request(), response));
	}

}
