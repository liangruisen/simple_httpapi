package com.miexist.simple.httpapi.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miexist.simple.httpapi.FileItem;
import com.miexist.simple.httpapi.HttpBody;

public class JdkHttpBody implements HttpBody {

	public JdkHttpBody() {
		super();
	}
	
	public JdkHttpBody(HttpBody body) {
		this(body.getHeaders(), body.getParams(), null);
		FileItem[] fileItems = body.getFileItems();
		List<FileItem> list = fileItems == null ? null : Arrays.asList(fileItems);
		this.fileItems = list;
	}

	public JdkHttpBody(Map<String, String> params, List<FileItem> fileItems) {
		this(null, params, fileItems);
	}

	public JdkHttpBody(Map<String, String> header, Map<String, String> params, List<FileItem> fileItems) {
		super();
		if(header != null && !header.isEmpty()) {
			this.header.putAll(header);
		}
		if(params != null && !params.isEmpty()) {
			this.params.putAll(params);
		}
		if(fileItems != null && !fileItems.isEmpty()) {
			this.fileItems.addAll(fileItems);
		}
	}

	public JdkHttpBody(Map<String, String> header, Map<String, String> params) {
		this(header, params, null);
	}

	public JdkHttpBody(Map<String, String> params) {
		this(null, params, null);
	}

	private Map<String, String> header = new HashMap<String, String>(10);
	private Map<String, String> params = new HashMap<String, String>(10);
	private List<FileItem> fileItems = new ArrayList<FileItem>(1);
	private String contentType = HttpBody.FORM_URLENCODED;
	private boolean modifyed = true;
	private long contentLength = 0;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private String boundary = "----****----" + System.currentTimeMillis() + "----****----";

	@Override
	public Map<String, String> getHeaders() {
		return header;
	}

	public void addHeader(String name, String value) {
		modifyed = true;
		header.put(name, value);
	}

	@Override
	public Map<String, String> getParams() {
		return params;
	}

	public void addParam(String name, String value) {
		modifyed = true;
		params.put(name, value);
	}

	@Override
	public FileItem[] getFileItems() {
		return fileItems == null ? null : fileItems.toArray(new FileItem[0]);
	}

	public void addFileItem(FileItem fileItem) {
		modifyed = true;
		fileItems.add(fileItem);
	}

	public long getContentLength() {
		initParams();
		return contentLength;
	}

	@Override
	public String getContentType() {
		if (fileItems != null && !fileItems.isEmpty()) {
			return HttpBody.MULTIPART_FORM_DATA + "; boundary=" + boundary;
		}
		return contentType;
	}

	public void writeTo(OutputStream out) throws IOException {
		if (fileItems == null || fileItems.isEmpty()) {
			initParams();
			baos.writeTo(out);
		} else {
			StringBuilder buf = new StringBuilder(500);
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					buf.append("--").append(boundary).append("\r\n");
					buf.append("Content-Disposition: form-data; name=\"");
					buf.append(entry.getKey()).append("\"\r\n\r\n");
					buf.append(entry.getValue()).append("\r\n");
				}
			}
			if (fileItems != null && !fileItems.isEmpty()) {
				for (FileItem item : fileItems) {
					buf.append("--").append(boundary).append("\r\n");
					buf.append("Content-Disposition: form-data; name=\"");
					buf.append(item.getFieldName()).append("\"; filename=\"");
					buf.append(item.getName()).append("\"\r\n");
					buf.append("Content-Type: ").append(item.getContentType());
					buf.append("\r\n\r\n");
					out.write(buf.toString().getBytes());
					buf.setLength(0);
					item.writeTo(out);
					buf.append("\r\n");
				}
			}
			buf.append("--").append(boundary).append("--");
			out.write(buf.toString().getBytes());
		}
	}

	private void initParams() {
		if (modifyed) {
			baos.reset();
			try {
				if (fileItems == null || fileItems.isEmpty()) {
					if (params != null && !params.isEmpty()) {
						for (Map.Entry<String, String> entry : params.entrySet()) {
							if (baos.size() > 0) {
								baos.write('&');
							}
							baos.write(entry.getKey().getBytes());
							baos.write('=');
							baos.write(URLEncoder.encode(entry.getValue(), "UTF-8").getBytes());
						}
					}
					contentLength = baos.size();
				} else {
					long length = 0;
					StringBuilder buf = new StringBuilder(500);
					if (params != null && !params.isEmpty()) {
						for (Map.Entry<String, String> entry : params.entrySet()) {
							buf.append("--").append(boundary).append("\r\n");
							buf.append("Content-Disposition: form-data; name=\"");
							buf.append(entry.getKey()).append("\"\r\n\r\n");
							buf.append(entry.getValue());
						}
					}
					if (fileItems != null && !fileItems.isEmpty()) {
						for (FileItem item : fileItems) {
							buf.append("--").append(boundary).append("\r\n");
							buf.append("Content-Disposition: form-data; name=\"");
							buf.append(item.getFieldName()).append("\"; filename=\"");
							buf.append(item.getName()).append("\"\r\n");
							buf.append("Content-Type: ").append(item.getContentType());
							buf.append("\r\n\r\n");
							length += item.getContentLength();
						}
					}
					buf.append("--").append(boundary).append("--");
					baos.write(buf.toString().getBytes());
					length += baos.size();
					contentLength = length;
					baos.reset();
				}
			} catch (IOException e) {
			}
			modifyed = false;
		}
	}

	@Override
	public void setHeader(String name, String value) {
		String head = header.get(name);
		if(head == null || head.trim().length() <= 0) {
			header.put(name, value);
		}else {
			header.put(name, head + ";" + value);
		}
	}

	@Override
	public void setParam(String name, String value) {
		params.put(name, value);
	}
}
