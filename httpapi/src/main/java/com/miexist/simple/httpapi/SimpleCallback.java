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
/**
 * 异步发送HTTP请求的回调接口
 * @author liangruisen
 *
 */
public interface SimpleCallback {

	/**
	 * 当请求抛出IOException时调用该方法
	 * @param request
	 * @param e
	 */
	void onFailure(SimpleRequest request, IOException e);
	
	/**
	 * 当请求返回内容后调用该方法
	 * @param response
	 */
	void onResponse(SimpleResponse response);
}
