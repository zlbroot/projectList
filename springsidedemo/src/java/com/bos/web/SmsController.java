package com.bos.web;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bos.service.account.ShiroDbRealm.ShiroUser;
import com.bos.service.commons.FetionService;

@Controller
@RequestMapping(value = "/sms")
public class SmsController {

	private FetionService fetionService;
	@Resource
	public void setFetionService(FetionService fetionService) {
		this.fetionService = fetionService;
	}
	@RequestMapping(method = RequestMethod.GET)
	public String login() {
		return "sms";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam("msg") String msg, Model model) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String result = fetionService.send(user.loginName, msg);
		model.addAttribute("message", result);
		return "sms";
	}

}
