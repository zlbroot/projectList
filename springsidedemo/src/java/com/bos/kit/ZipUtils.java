package com.bos.kit;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	public final static void zip(OutputStream out, ZipEntry... zipEntries) throws IOException {
		ZipOutputStream zip = new ZipOutputStream(out);
		byte[] datas = null;
		for (ZipEntry ze : zipEntries) {
			datas = ze.getExtra();
			ze.setExtra(null);
			ze.setSize(datas.length);
			ze.setTime(System.currentTimeMillis());
			zip.putNextEntry(ze);
			zip.write(datas);
			zip.flush();
			zip.closeEntry();
		}
		zip.finish();
		zip.close();
	}

	public final static byte[] inputStream2bytes(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		inputStream.close();
		baos.flush();
		return baos.toByteArray();
	}

	public static void main(String[] args) throws Exception {
		String[] urls = { "http://172.16.10.32:8888/group1/M00/00/00/rBAKIFNqGi8EAAAAAAAAAOz5e_4555.xls",
				"http://172.16.10.32:8888/group1/M00/00/00/rBAKIFNqGi8EAAAAAAAAAOz5e_4103.xls" };
		FileOutputStream out = new FileOutputStream("E:/bb.zip");
		ZipEntry ze = null;
		ZipEntry[] tmp = new ZipEntry[urls.length];
		int i = 0;
		for (String s : urls) {
			URL url = new URL(s);
			String path = url.getPath();
			path = path.substring(path.lastIndexOf("/") + 1);
			ze = new ZipEntry(path);
			InputStream is = url.openConnection().getInputStream();
			ze.setExtra(inputStream2bytes(is));
			tmp[i] = ze;
			i++;
		}
		zip(out, tmp);
	}
}
