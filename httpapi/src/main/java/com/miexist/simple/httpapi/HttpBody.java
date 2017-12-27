package com.miexist.simple.httpapi;

import java.util.Map;

public interface HttpBody {

	static final String MULTIPART_FORM_DATA = "multipart/form-data";
	
	static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
	
	Map<String, String> getHeaders();
	
	void addHeader(String name, String value);
	
	void setHeader(String name, String value);
	
	Map<String, String> getParams();
	
	void setParam(String name, String value);
	
	FileItem[] getFileItems();
	
	String getContentType();
}
