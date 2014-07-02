package com.test.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.service.UserService;
import com.zlb.core.annos.Resource;
import com.zlb.core.annos.mvc.Action;
import com.zlb.core.annos.mvc.ReqParam;

@Action("user")
public class UserAction {
	protected UserService userService;

	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Action("/get")
	public String get(HttpServletRequest request, @ReqParam("test") String info) {
		System.err.println(info);
		request.setAttribute("info", info);
		return "user";
	}

	public void execute(HttpServletResponse response) throws IOException {
		response.getWriter().println(userService.findOne(1l));
		response.getWriter().close();
	}
}
