package com.miexist.simple.httpapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileItem {

	long getContentLength();
	
	InputStream getInputStream() throws IOException;
	
	void writeTo(OutputStream outstream) throws IOException;
	
	String getName();
	
	String getFieldName();
	
	String getContentType();
}
