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

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO操作的常用方法集合类
 * @author liangruisen
 *
 */
public abstract class IOUtils {

	/**
	 * 调用Closeable的close方法
	 * @param closeable
	 * @throws IOException
	 */
	public static void close(Closeable closeable) throws IOException{
		if(closeable != null){
			closeable.close();
		}
	}
	
	/**
	 * 忽略IOException调用Closeable的close方法
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable){
		try{
			close(closeable);
		}catch(IOException e){}
	}
	
	/**
	 * 从输入流中复制内容到输出流
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] b = new byte[1024];
		int len;
		while((len = input.read(b)) != -1){
			output.write(b, 0, len);
		}
		output.flush();
	}
	
	/**
	 * 从输入流中将内容转换成字节数组
	 * @param input
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] toBytes(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(input, baos);
		return baos.toByteArray();
	}
	
	/**
	 * 从输入流中读取内容为字符串
	 * @param input
	 * @param charset
	 * @return String
	 * @throws IOException
	 */
	public static String toString(InputStream input, String charset) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(input, baos);
		return baos.toString(charset);
	}
}
