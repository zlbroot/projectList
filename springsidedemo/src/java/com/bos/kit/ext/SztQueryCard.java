package com.bos.kit.ext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bos.kit.HttpConnection;
import com.bos.kit.HttpConnection.Response;

public class SztQueryCard {
	private static final String QUERY_URL = "http://121.15.13.49:8080/sztnet/qryCard.do";

	public static Map<String, String> query(String cardno) throws IOException {
		Map<String, String> result = new HashMap<>();
		Response response = HttpConnection.connect(QUERY_URL)
				.data("cardno", cardno).execute();
		Document doc = Jsoup.parse(response.body());
		Elements s = doc.select("table.tableact td");
		result.put("cardno", s.get(1).text());
		String tmp = s.get(2).text();
		result.put("expiryDate",tmp.substring(tmp.indexOf("截止到") + 3,tmp.lastIndexOf(")")));
		result.put("balance",s.get(3).text().replace("元", ""));
		result.put("validDate",s.get(5).text());
		return result;
	}
}
