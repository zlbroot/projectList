package com.bos.kit.ext;

import java.io.IOException;
import java.util.HashMap;

import com.bos.kit.HttpConnection;
import com.bos.kit.HttpConnection.Method;
import com.bos.kit.HttpConnection.Response;

public class FetionWap  {
	private String un;
	private String pwd;
	public void setUn(String un) {
		this.un = un;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	long login = 0;
	public String send(String msg) {
		if((System.currentTimeMillis() - login) > 60000){
			login(un,pwd);
		}
		return send2me(msg);
	}
	HashMap<String, String> cookies = new HashMap<String, String>();
	static HttpConnection getConnection(String url) {
		HttpConnection conn = HttpConnection.connect(url);
		conn.connTimeout(60000);
		conn.ignoreHttpErrors(true);
		conn.header("Content-Type", "application/x-www-form-urlencoded");
		conn.header("Host", "f.10086.cn");
		conn.userAgent("Mozilla/5.0 (Windows NT 5.1; rv:14.0) Gecko/20100101 Firefox/14.0.1");
		conn.method(Method.POST);
		return conn;
	}

	private String send2me(String msg) {
		try {
			HttpConnection conn = getConnection("http://f.10086.cn/im/user/sendMsgToMyselfs.action");
			conn.data("msg", msg);
			conn.cookies(cookies);
			Response response = conn.execute();
			String res = response.body();
			String[] sa = res.split("id=\"start\" title=\"");
			String result = sa[1].substring(0, sa[1].indexOf("\""));
			System.out.println("send[" + msg + "][" + result + "]");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String logout() throws IOException {
		System.out.println("=logout================");
		HttpConnection conn = null;
		Response response = null;
		conn = getConnection("http://f.10086.cn/im/index/logoutsubmit.action");
		conn.followRedirects(false);
		response = conn.execute();
		cookies.putAll(response.cookies());
		return response.body();
	}
	boolean cklogin() throws IOException{
		HttpConnection conn = getConnection("http://f.10086.cn/im/login/cklogin.action?t"
				+ Math.random());
		conn.method(Method.GET);
		conn.cookies(cookies);
		Response response = conn.execute();
		String s =  response.body();
		if(s.indexOf("/im/user/setstatus.action") > -1){
			return true;
		}
		return false;
	}
	
	public void login(String un, String pwd) {
		try {
			System.out.println("=load login================");
			HttpConnection conn = null;
			Response response = null;
			conn = getConnection("http://f.10086.cn/huc/jsp/login/tologin.jsp");
			conn.followRedirects(false);
			response = conn.execute();
			cookies.putAll(response.cookies());
			conn = getConnection("http://f.10086.cn/huc/user/foo/login.do");
			conn.followRedirects(true);
			conn.cookies(cookies);
			conn.data("mobilenum", un);
			conn.data("password", pwd);
			conn.data("m", "submit");
			conn.data("backurl", "http://f.10086.cn/");
			conn.data("fr", "foo");
			response = conn.execute();
			cookies.putAll(response.cookies());
			if(cklogin()){
				System.out.println("login success !");
			}else{
				System.out.println("login faild !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		FetionWap f = new FetionWap();
		f.login("13425140701", "billing6814740");
//		
//		Document doc = Jsoup.connect("http://ishuo.cn/duanzi").get();
//		Elements es = doc.select("div.content");
//		for (Element e : es) {
////			System.out.println(e.text());
//			f.send2me(e.text());
//			break;
//		}
//		
		f.send2me("test3");
		f.logout();
	}
}
