package com.bos.kit;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
	static final String[] urls = { "http://172.16.10.32:8888/group1/M00/00/00/rBAKIFNqGi8EAAAAAAAAAOz5e_4555.xls",
			"http://172.16.10.32:8888/group1/M00/00/00/rBAKIFNqGi8EAAAAAAAAAOz5e_4103.xls" };

	public static void main(String[] args) throws Exception {
		ZipOutputStream zip = new ZipOutputStream(new FileOutputStream("E:/aa.zip"));
		ZipEntry ze = null;
		for (String s : urls) {
			URL url = new URL(s);
			String path = url.getPath();
			path = path.substring(path.lastIndexOf("/") + 1);
			ze = new ZipEntry(path);
			InputStream is = url.openConnection().getInputStream();
			byte[] extra = in2bytes(is);
			// FileOutputStream oo = new FileOutputStream("E:/" + path);
			// oo.write(extra);
			// oo.flush();
			// oo.close();
			ze.setSize(extra.length);
			ze.setTime(System.currentTimeMillis());
			zip.putNextEntry(ze);
			zip.write(extra);
			zip.flush();
			zip.closeEntry();
		}
		zip.finish();
		zip.close();
	}

	public final static byte[] in2bytes(InputStream is) throws Exception {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = is.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		is.close();
		baos.flush();
		return baos.toByteArray();
	}
}
