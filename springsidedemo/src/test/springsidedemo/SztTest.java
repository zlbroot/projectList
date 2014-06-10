package springsidedemo;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.bos.kit.HttpConnection;
import com.bos.kit.HttpConnection.Response;

public class SztTest {
	public static void main(String[] args) throws IOException {
		String cardno = "290925671";
		Response response = HttpConnection.connect("http://121.15.13.49:8080/sztnet/qryCard.do")
				.data("cardno",cardno).execute();
		Document doc = Jsoup.parse(response.body());
//		System.out.println(doc);
		Elements s = doc.select("table.tableact td");
		System.out.println(s.get(1).text());
		System.out.println(s.get(3).text());
		System.out.println(s.get(5).text());
	}
}
