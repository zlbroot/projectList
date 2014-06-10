package billing.sb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wenchao.ren on 2014/4/26.
 */
@Controller
@EnableAutoConfiguration
public class SimpleController {

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	public String hello() {
		return "hello world";
	}

	public static void main(String[] args) {
		SpringApplication.run(SimpleController.class, args);
	}
}