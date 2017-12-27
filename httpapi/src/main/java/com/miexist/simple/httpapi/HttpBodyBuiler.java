package com.miexist.simple.httpapi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpBodyBuiler {

	private HttpBodyBuiler() {
		super();
	}
	
	private Map<String, String> heads = new HashMap<String, String>(10);
	
	private Map<String, String> params = new HashMap<String, String>(20);
	
	private List<FileItem> fileItems = new ArrayList<FileItem>(1);
	
	private String contentType = HttpBody.FORM_URLENCODED;
	
	public HttpBodyBuiler setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}
	
	public HttpBodyBuiler addFileItem(String fieldName, String name, File file) {
		fileItems.add(FileItems.create(fieldName, name, file));
		return this;
	}
	
	public HttpBodyBuiler addFileItem(FileItem fileItem) {
		fileItems.add(fileItem);
		return this;
	}
	
	public HttpBodyBuiler addParam(String name, String value) {
		params.put(name, value);
		return this;
	}
	
	public HttpBodyBuiler addHeader(String name, String value) {
		String val = heads.get(name);
		if(val == null || val.trim().length() <= 0) {
			heads.put(name, value);
		}else {
			heads.put(name, val + ";" + value);
		}
		return this;
	}
	
	public HttpBodyBuiler setHeader(String name, String value) {
		heads.put(name, value);
		return this;
	}
	
	public HttpBody build() {
		return new HttpBodyImpl(heads, params, fileItems, contentType);
	}
	
	public static HttpBodyBuiler create() {
		return new HttpBodyBuiler();
	}
	
	private class HttpBodyImpl implements HttpBody {
		private final Map<String, String> heads;
		
		private final Map<String, String> params;
		
		private final List<FileItem> fileItems;
		
		private final String contentType;
		public HttpBodyImpl(Map<String, String> heads, Map<String, String> params, List<FileItem> fileItems,
				String contentType) {
			super();
			this.heads = heads;
			this.params = params;
			this.fileItems = fileItems;
			this.contentType = contentType;
		}

		@Override
		public Map<String, String> getHeaders() {
			return heads;
		}

		@Override
		public void addHeader(String name, String value) {
			String val = heads.get(name);
			if(val == null || val.trim().length() <= 0) {
				heads.put(name, value);
			}else {
				heads.put(name, val + ";" + value);
			}
		}

		@Override
		public void setHeader(String name, String value) {
			heads.put(name, value);
		}

		@Override
		public Map<String, String> getParams() {
			return params;
		}

		@Override
		public void setParam(String name, String value) {
			params.put(name, value);
		}

		@Override
		public FileItem[] getFileItems() {
			if(fileItems == null || fileItems.isEmpty()) {
				return null;
			}
			return fileItems.toArray(new FileItem[0]);
		}

		@Override
		public String getContentType() {
			if(fileItems.isEmpty()) {
				return contentType;
			}
			return HttpBody.MULTIPART_FORM_DATA;
		}
		
	}
}
