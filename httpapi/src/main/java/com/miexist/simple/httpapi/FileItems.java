package com.miexist.simple.httpapi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;

public class FileItems {

	public static FileItem create(final String fieldName, final String name, final String contentType,
			final byte[] content) {
		return new FileItem() {
			@Override
			public long getContentLength() {
				return content == null ? 0 : content.length;
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(content);
			}

			@Override
			public void writeTo(OutputStream outstream) throws IOException {
				outstream.write(content);
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getFieldName() {
				return fieldName;
			}

			@Override
			public String getContentType() {
				return contentType;
			}
		};
	}

	public static FileItem create(final String fieldName, final String name, final File file) {
		return new FileItem() {
			@Override
			public long getContentLength() {
				return file.length();
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return new FileInputStream(file);
			}

			@Override
			public void writeTo(OutputStream outstream) throws IOException {
				InputStream input = null;
				try {
					input = getInputStream();
					byte[] b = new byte[2048];
					int len;
					while ((len = input.read(b)) != -1) {
						outstream.write(b, 0, len);
					}
					outstream.flush();
				} catch (IOException e) {
					throw e;
				} finally {
					if (input != null) {
						input.close();
					}
				}
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getFieldName() {
				return fieldName;
			}

			@Override
			public String getContentType() {
				return new MimetypesFileTypeMap().getContentType(file);
			}
		};
	}

	public static FileItem create(final String fieldName, final String name, final String contentType,
			final InputStream input) {
		return new FileItem() {
			@Override
			public long getContentLength() {
				try {
					return input == null ? 0 : input.available();
				} catch (IOException e) {
					return 0;
				}
			}

			@Override
			public InputStream getInputStream() {
				return input;
			}

			@Override
			public void writeTo(OutputStream outstream) throws IOException {
				try {
					byte[] b = new byte[2048];
					int len;
					while ((len = input.read(b)) != -1) {
						outstream.write(b, 0, len);
					}
					outstream.flush();
				} catch (IOException e) {
					throw e;
				}
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getFieldName() {
				return fieldName;
			}

			@Override
			public String getContentType() {
				return contentType;
			}
		};
	}
}
