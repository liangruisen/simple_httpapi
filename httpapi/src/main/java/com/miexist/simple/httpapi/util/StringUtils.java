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
package com.miexist.simple.httpapi.util;

import java.util.Map;

/**
 * 字符串常用方法类
 * @author liangruisen
 *
 */
public abstract class StringUtils {

	/**
	 * 判断字符串是否是非空白的
	 * @param sources
	 * @return boolean
	 */
	public static boolean hasText(CharSequence sources){
		if(sources == null || sources.length() <= 0){
			return false;
		}
		int len = sources.length();
		for(int i = 0; i < len; i++){
			if(!Character.isWhitespace(sources.charAt(i))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 在URL字符串后面拼接请求参数
	 * @param urlstring
	 * @param params
	 * @return String
	 */
	public static String appendUrlParams(String urlstring, Map<String, String> params) {
		if(params == null || params.isEmpty()){
			return urlstring;
		}
		StringBuilder buf = new StringBuilder(512);
		for(Map.Entry<String, String> entry : params.entrySet()){
			buf.append("&").append(entry.getKey()).append("=");
			buf.append(entry.getValue());
		}
		boolean hasParams = urlstring.indexOf("?") > 0;
		if(!hasParams){
			buf.replace(0, 1, "?");
		}
		buf.insert(0, urlstring);
		return buf.toString();
	}
	
	/**
	 * 将字符串列表中的字符串使用分隔符separator拼接成一个字符串
	 * @param iterable
	 * @param separator
	 * @return String
	 */
	public static String join(Iterable<String> iterable, String separator){
		StringBuilder buf = new StringBuilder();
		for(String str : iterable){
			buf.append(separator).append(str);
		}
		return buf.length() > 0 ? buf.substring(separator.length()) : "";
	}
}
