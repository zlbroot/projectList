package com.bos.kit.ext;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Ys168Kit {
	private String ly = "";
	private String m = "";
	private String p = "";

	public Ys168Kit(String ly, String m, String p) {
		this.ly = ly;
		this.m = m;
		this.p = p;
	}

	public String upload(String f, byte[] datas) throws MalformedURLException {
		return upload(f, datas, "by_jvm");
	}

	public String upload(String f, byte[] datas, String t) throws MalformedURLException {
		String l = "";
		f = f.replace(" ", "_");
		URL url = new URL("http://ys-m.ys168.com/?p=" + p + "&ly=" + ly + "&m=" + m + "&f=" + f + "&l=" + l + "&t=" + t);
		return realUpload(url, f, datas);
	}

	private String realUpload(URL url, String name, byte[] datas) {
		// 要上传的文件名,如：d:\haha.doc.你要实现自己的业务。我这里就是一个空list.
		try {
			final String BOUNDARY = "---" + UUID.randomUUID().toString().replace("-", "").substring(10) + "---"; // 定义数据分隔线
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;filename=\"" + name + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] data = sb.toString().getBytes();
			out.write(data);
			out.write(datas);
			out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
			out.write(end_data);
			out.flush();
			out.close();

			// // 定义BufferedReader输入流来读取URL的响应
			// BufferedReader reader = new BufferedReader(new
			// InputStreamReader(conn.getInputStream()));
			// String line = null;
			// while ((line = reader.readLine()) != null) {
			// System.out.println(line);
			// }
			if (200 == conn.getResponseCode()) {
				return "上传成功！";
			}
		} catch (Exception e) {
			System.err.println("发送POST请求出现异常！" + e.getLocalizedMessage());
			e.printStackTrace();
			return "上传失败：" + e.getLocalizedMessage();
		}
		return "上传失败！";
	}

	public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {// 01746491r9642z9s00cx3018cv0//0407b1f1ncnz9s00cy042180v0
		Ys168Kit d = new Ys168Kit("billing.ys168.com", "15", "01c06e31n0849z9s00cy0hp0v0");
		String result = d.upload("afdsa-b.txt", "你好中国在这里！".getBytes("GBK"));
		System.out.println(result);
	}
}
